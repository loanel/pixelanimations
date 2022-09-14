package simpanim.multithread;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import simpanim.Bucket;
import simpanim.Point;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

public class PointCollisionTask implements Callable<Integer> {

    private List<List<Bucket>> gameField;
    private List<Point> pointsToCheck;
    private int bucketDimension;
    private int bucketAmount;
    private Group pixelHandle;

    public PointCollisionTask(Group pixelHandle, int bucketDimension, int bucketAmount, List<List<Bucket>> gameField, List<Point> pointsToCheck){
        this.pixelHandle = pixelHandle;
        this.bucketDimension = bucketDimension;
        this.bucketAmount = bucketAmount;
        this.gameField = gameField;
        this.pointsToCheck = pointsToCheck;
    }

    private void eatAnotherPoint(Point hunter, Point prey) {
        synchronized (pixelHandle){
            if (hunter.isRemoved() || hunter.isEaten()) {
                return;
            }
            if (prey.isRemoved() || prey.isEaten()) {
                return;
            }
            prey.eatPoint();
            double num = 0.1;
            hunter.increaseSize(num);
        }
    }

    @Override
    public Integer call() throws Exception {
        for (Point point : pointsToCheck) {
            if (point.isRemoved() || point.isEaten()) {
                continue;
            }

            double xCoord = point.getXPosition();
            double yCoord = point.getYPosition();

            double radius = point.getSize();
            int startX = (int) Math.round((xCoord - radius)) / bucketDimension;
            if (startX < 0) {
                startX = 0;
            }
            int startY = (int) Math.round((yCoord - radius)) / bucketDimension;
            if (startY < 0) {
                startY = 0;
            }

            boolean originalEaten = false;
            for (int x = startX; (x + 1) * bucketDimension < xCoord + radius; x++) {
                if (x >= bucketAmount) break;
                for (int y = startY; (y + 1) * bucketDimension < yCoord + radius; y++) {
                    if (y >= bucketAmount) break;
                    for (Point secondPoint : gameField.get(x).get(y).getBucketContent()) {
                        if (secondPoint.isEaten()|| point.equals(secondPoint)) {
                            continue;
                        }
                        double middleDistance = Math.sqrt(Math.pow(point.getXPosition() - secondPoint.getXPosition(), 2)
                                + Math.pow(point.getYPosition() - secondPoint.getYPosition(), 2));
                        int roundedMiddleDistance = (int) Math.round(middleDistance);
                        if (roundedMiddleDistance < point.getSize() || roundedMiddleDistance < secondPoint.getSize()) {
                            if (point.getSize() >= secondPoint.getSize()) {
                                eatAnotherPoint(point, secondPoint);
                            } else {
                                eatAnotherPoint(secondPoint, point);
                                originalEaten = true;
                                break;
                            }
                        }
                    }
                    if (originalEaten) break;
                }
                if (originalEaten) break;
            }
        }
        return 0;
    }
}
