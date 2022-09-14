package simpanim.transformations.point;

import simpanim.Point;

public interface PointLocationTransformation {
    /// generic point transformation with no additional parameters
    void apply(Point point, double resolution);
}
