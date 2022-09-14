package simpanim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bucket{
    private List<Point> bucketPoints = new ArrayList<>();

    public void putIntoBucket(Point point){
        bucketPoints.add(point);
    }

    public List<Point> getBucketContent(){
        return bucketPoints;
    }
}
