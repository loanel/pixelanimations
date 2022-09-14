package simpanim.multithread;

import javafx.application.Platform;
import javafx.scene.Node;
import simpanim.Point;
import simpanim.transformations.point.wallcollision.PerfectWallCollision;
import simpanim.transformations.point.wallcollision.WallCollisionStrategy;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class WallCollisionTask implements Callable<Integer> {
    List<Point> pointsList;
    Set<Point> removedPoints;
    double resolution;
    int frameCount;

    public WallCollisionTask(List<Point> pointsList, Set<Point> removedPoints, double resolution, int frameCount){
        this.pointsList = pointsList;
        this.removedPoints = removedPoints;
        this.resolution = resolution;
        this.frameCount = frameCount;
    }


    @Override
    public Integer call() throws Exception {
        final double width = 0.5 * resolution;
        final double height = 0.5 * resolution;
        final double radius = Math.sqrt(2) * Math.max(width, height);
        WallCollisionStrategy strategy = new PerfectWallCollision();
        for(Point point : pointsList){
            if (point.getStartFrame() < frameCount && !removedPoints.contains(point)) {
                final Node node = point.getCircle();
                final double angle = point.getAngle();
                final double currentX = point.getXPosition();
                final double currentY = point.getYPosition();

                ///slices of radius to beat in one frame
                final double d = radius / 500;
                point.setXPosition(point.getXPosition() + Math.cos(angle) * d);
                point.setYPosition(point.getYPosition() + Math.sin(angle) * d);

                ///only way for the callable to work without causing exceptions but makes the entire concept pointless
                Platform.runLater(() -> {
                    node.setTranslateX(currentX + Math.cos(angle) * d);
                    node.setTranslateY(currentY + Math.sin(angle) * d);
                });
                strategy.apply(point, resolution);
            }
        }
        return 0;
    }

}
