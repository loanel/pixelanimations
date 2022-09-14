package simpanim.transformations.point.wallcollision;

import simpanim.Point;

///collides with the wall, changing the angle by 90 degrees in the current flight direction
public class SquareWallCollision implements WallCollisionStrategy {

    @Override
    public void apply(Point point, double resolution) {
        if (point.getXPosition() < 0) {
            if (Math.sin(point.getAngle()) > 0) {
                point.setAngle(point.getAngle() - Math.PI / 2);
            } else {
                point.setAngle(point.getAngle() + Math.PI / 2);
            }
            point.setXPosition(0.0);
        }
        if (point.getXPosition() > resolution) {
            if (Math.sin(point.getAngle()) > 0) {
                point.setAngle(point.getAngle() + Math.PI / 2);
            } else {
                point.setAngle(point.getAngle() - Math.PI / 2);

            }
            point.setXPosition(resolution);
        }
        if (point.getYPosition() < 0) {
            if (Math.cos(point.getAngle()) > 0) {
                point.setAngle(point.getAngle() + Math.PI / 2);
            } else {
                point.setAngle(point.getAngle() - Math.PI / 2);
            }
            point.setYPosition(0.0);
        }
        if (point.getYPosition() > resolution) {
            if (Math.cos(point.getAngle()) > 0) {
                point.setAngle(point.getAngle() - Math.PI / 2);
            } else {
                point.setAngle(point.getAngle() + Math.PI / 2);
            }
            point.setYPosition(resolution);
        }
    }
}
