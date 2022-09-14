package simpanim.transformations.point.wallcollision;

import simpanim.Point;

///on wall collision, returns the point to the middle of the game field
public class LoopOnWallCollision implements WallCollisionStrategy {

    @Override
    public void apply(Point point, double resolution) {
        if (point.getXPosition() > resolution || point.getXPosition() < 0
                || point.getYPosition() > resolution || point.getYPosition() < 0) {
            point.setXPosition(resolution / 2);
            point.setYPosition(resolution / 2);
        }
    }
}
