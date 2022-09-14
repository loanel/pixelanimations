package simpanim.transformations.point.wallcollision;

import simpanim.Point;

///teleports the point to the opposite side of the map, keeping the inital angle intact
public class TeleportOnWallCollision implements WallCollisionStrategy {

    @Override
    public void apply(Point point, double resolution) {
        if (point.getXPosition() > resolution) {
            point.setXPosition(0.0);
        }
        if (point.getXPosition() < 0) {
            point.setXPosition(resolution);
        }
        if (point.getYPosition() > resolution) {
            point.setYPosition(0.0);
        }
        if (point.getYPosition()  < 0) {
            point.setYPosition(resolution);
        }
    }
}
