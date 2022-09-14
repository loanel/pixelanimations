package simpanim;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;
import simpanim.multithread.PointCollisionTask;
import simpanim.transformations.point.wallcollision.WallCollisionStrategy;
import simpanim.transformations.population.PopulationTransformation;

import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class PixelGameField extends AnimationTimer {
    private int frameCount = 0;
    private final double resolution;
    private final List<Point> nodes;
    private final Group pixelHandle;
    private WallCollisionStrategy strategy;
    ExecutorService executorService;
    final double distance;

    public PixelGameField(List<Point> nodesList, Group pixelHandle, double resolution, double speed) {
        this.nodes = nodesList;
        this.pixelHandle = pixelHandle;
        this.resolution = resolution;
//        distance = resolution / (resolution - speed);
        distance = speed;
        executorService = Executors.newFixedThreadPool(10);
    }

    public WallCollisionStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(WallCollisionStrategy strategy) {
        this.strategy = strategy;
    }

    public void initializeGameField(PopulationTransformation transformation) {
        transformation.apply(nodes);
    }

//    private void swapDimension(Point point) {
//        if (point.getXPosition() > resolution || point.getXPosition() < 0
//                || point.getYPosition() > resolution || point.getYPosition() < 0) {
//            point.setAngle(point.getAngle() + Math.PI / 2);
//        }
//    }

    @Override
    public void handle(long now) {
        frameCount++;
        if(frameCount > 300){
            checkCollisions();
        }
        performPointOperations();
    }

    private void performPointOperations() {
        ///Seems like javafx doesn't allow for concurrent modification of the scene, at least i'm not sure how to do it
        ///you can do Platform.runLater() but then its not multithreaded and is actually slower due to overhead
        performPointMovementProcedural(strategy);
    }

    private void performPointMovementProcedural(WallCollisionStrategy strategy) {
//        for(Point point : nodes){
//            if (shouldPointBeProcessed(point)) {
//                final Node node = point.getCircle();
//                final double angle = point.getAngle();
//                final double currentX = point.getXPosition();
//                final double currentY = point.getYPosition();
//
//                ///slices of radius to beat in one frame
//                final double d = radius / ((Math.sqrt(2)*resolution) - speed);
//                point.setXPosition(point.getXPosition() + Math.cos(angle) * d);
//                point.setYPosition(point.getYPosition() + Math.sin(angle) * d);
//                node.setTranslateX(currentX + Math.cos(angle) * d);
//                node.setTranslateY(currentY + Math.sin(angle) * d);
//                if(isNearWall(point)){
//                    strategy.apply(point, resolution);
//                }
//            }
//        }

        Random rand = new Random();
        for (Point point : nodes) {
            if (rand.nextInt() % 1 == 0) {
                if (shouldPointBeProcessed(point)) {
                    ///check if point shouldn't be removed
                    if(point.isEaten()){
                        pixelHandle.getChildren().remove(point.getCircle());
                        point.removePoint();
                        continue;
                    }

                    ///move point if not eaten
                    final Node node = point.getCircle();
                    final double angle = point.getAngle();
                    final double currentX = point.getXPosition();
                    final double currentY = point.getYPosition();
                    //slices of radius to beat in one frame
                    int skippedFrames = point.getFramesAfterUpdate();
                    point.setXPosition(point.getXPosition() + (skippedFrames * Math.cos(angle) * distance));
                    point.setYPosition(point.getYPosition() + (skippedFrames * Math.sin(angle) * distance));
                    node.setTranslateX(currentX + (skippedFrames * Math.cos(angle) * distance));
                    node.setTranslateY(currentY + (skippedFrames * Math.sin(angle) * distance));
                    point.updateCircleToSize();
                    strategy.apply(point, resolution);
                }
                point.resetFramesSkipped();
            } else {
                point.skipFrame();
            }
        }
    }

    private boolean shouldPointBeProcessed(Point point) {
        return point.getStartFrame() < frameCount && !point.isRemoved();
    }


    private void processPointCollisionConcurrent(int bucketAmount, int bucketDimension, List<List<Bucket>> unmodifiableBuckets) {
        Collection<Callable<Integer>> tasks = new ArrayList<>();
        List<Point> pointsToCalculate = new ArrayList<>();
        for (Point node : nodes) {
            pointsToCalculate.add(node);
            if (pointsToCalculate.size() > 63) {
                tasks.add(new PointCollisionTask(pixelHandle, bucketDimension, bucketAmount, unmodifiableBuckets, pointsToCalculate));
                pointsToCalculate = new ArrayList<>();
            }
        }
        try{
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private void checkCollisions() {
        int bucketAmount = 64;
        int bucketDimension = (int) Math.round(resolution)/bucketAmount;

        List<List<Bucket>> buckets = new LinkedList<>();

        for(int i = 0; i < bucketAmount; i++){
            LinkedList<Bucket> bucketContainer = new LinkedList<>();
            for(int j = 0; j < bucketAmount; j++){
                bucketContainer.add(new Bucket());
            }
            buckets.add(bucketContainer);
        }

        for(Point point : nodes){
            int xCoord = 0;
            int yCoord = 0;

            boolean xPicked = false;
            boolean yPicked = false;

            for(int j = 0; j < bucketAmount; j++){
                if(!xPicked && point.getXPosition() < (j+1)*bucketDimension){
                    xCoord = j;
                    xPicked = true;
                }
                if(!yPicked && point.getYPosition() < (j+1)*bucketDimension){
                    yCoord = j;
                    yPicked = true;
                }
                if(yPicked && xPicked){
                    break;
                }
            }
            buckets.get(xCoord).get(yCoord).putIntoBucket(point);
        }
        List<List<Bucket>> unmodifiableBuckets = Collections.unmodifiableList(buckets);
        processPointCollisionConcurrent(bucketAmount, bucketDimension, unmodifiableBuckets);
    }
}
