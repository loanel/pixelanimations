package simpanim.transformations.population;

import simpanim.Point;
import simpanim.transformations.point.duration.StartModifier;
import simpanim.transformations.point.location.LocationModifier;
import simpanim.transformations.point.property.AngleModifier;

import java.util.List;

public interface PopulationTransformation {
    void apply(List<Point> points);
}
