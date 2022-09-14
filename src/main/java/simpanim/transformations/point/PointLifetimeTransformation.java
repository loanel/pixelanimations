package simpanim.transformations.point;

import simpanim.Point;

public interface PointLifetimeTransformation {
    void apply(Point point, int originFrames);
}
