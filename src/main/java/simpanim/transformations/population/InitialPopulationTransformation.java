package simpanim.transformations.population;

import simpanim.Point;
import simpanim.transformations.point.duration.RandomStart;
import simpanim.transformations.point.duration.StartModifier;
import simpanim.transformations.point.location.LocationModifier;
import simpanim.transformations.point.location.Middle;
import simpanim.transformations.point.property.AngleModifier;
import simpanim.transformations.point.property.RandomAngle;

import java.util.List;

public class InitialPopulationTransformation implements PopulationTransformation{

    private int originFrames;
    private double resolution;

    private AngleModifier angleModifier = new RandomAngle();
    private LocationModifier locationModifier = new Middle();
    private StartModifier startModifier = new RandomStart();

    public InitialPopulationTransformation(int originFrames, double resolution){
        this.originFrames = originFrames;
        this.resolution = resolution;
    }

    public AngleModifier getAngleModifier() {
        return angleModifier;
    }

    public void setAngleModifier(AngleModifier angleModifier) {
        this.angleModifier = angleModifier;
    }

    public LocationModifier getLocationModifier() {
        return locationModifier;
    }

    public void setLocationModifier(LocationModifier locationModifier) {
        this.locationModifier = locationModifier;
    }

    public StartModifier getStartModifier() {
        return startModifier;
    }

    public void setStartModifier(StartModifier startModifier) {
        this.startModifier = startModifier;
    }

    @Override
    public void apply(List<Point> points) {
        for(Point point: points){
            startModifier.apply(point, originFrames);
            locationModifier.apply(point, resolution);
            angleModifier.apply(point);
        }
    }
}
