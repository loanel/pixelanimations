package simpanim.transformations.point.wallcollision;

import simpanim.Point;

///changes the angle by symmetrical refraction
public class PerfectWallCollision implements WallCollisionStrategy {

    @Override
    public void apply(Point point, double resolution) {
        if (point.getXPosition() < 0) {
            if (Math.sin(point.getAngle()) > 0) {
                point.setAngle(Math.PI - point.getAngle());

            } else {
                point.setAngle(point.getAngle() + Math.PI - 2 * (point.getAngle() - Math.PI));
            }
            point.setXPosition(0.0);
        }
        if (point.getXPosition() > resolution) {
            if (Math.sin(point.getAngle()) > 0) {
                point.setAngle(Math.PI - 2 * point.getAngle());
            } else {
                point.setAngle(Math.PI + 2 * Math.PI - point.getAngle());
            }
            point.setXPosition(resolution);
        }
        if (point.getYPosition() < 0) {
            point.setAngle(2 * Math.PI - point.getAngle());
            point.setYPosition(0.0);
        }
        if (point.getYPosition() > resolution) {
            point.setAngle(2 * Math.PI - point.getAngle());
            point.setYPosition(resolution);
        }
    }
}
