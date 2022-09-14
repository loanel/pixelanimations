package simpanim.transformations.point.property;

import simpanim.Point;

import java.util.Random;

public class RandomAngle implements simpanim.transformations.point.property.AngleModifier {
    private final Random random = new Random();

    @Override
    public void apply(Point point) {
        point.setAngle(2*Math.PI * random.nextDouble());
    }
}
