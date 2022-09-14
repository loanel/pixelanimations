package simpanim.transformations.point.location;

import simpanim.Point;

public class Middle implements simpanim.transformations.point.location.LocationModifier {
    @Override
    public void apply(Point point, double resolution) {
        point.setXPosition(resolution / 2);
        point.setYPosition(resolution / 2);
    }
}
