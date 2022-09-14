package simpanim.transformations.point.duration;

import simpanim.Point;
import java.util.Random;

public class RandomStart implements StartModifier {
    private final Random random = new Random();

    @Override
    public void apply(Point point, int originFrames) {
        point.setStartFrame(random.nextInt(originFrames));
    }
}
