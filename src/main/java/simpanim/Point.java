package simpanim;

import javafx.scene.shape.Circle;

public class Point {
    private Circle circle;
    private double xPosition;
    private double yPosition;
    private double angle;

    private boolean eaten = false;

    private boolean removed = false;

    private double size;

    private int startFrame;
    public void eatPoint(){
        eaten = true;
    }

    public void removePoint(){
        removed = true;
    }

    public boolean isEaten(){
        return eaten;
    }

    public boolean isRemoved(){
        return removed;
    }

    public void skipFrame(){
        framesAfterUpdate++;
    }
    public void resetFramesSkipped(){
        framesAfterUpdate = 1;
    }

    public int getFramesAfterUpdate() {
        return framesAfterUpdate;
    }

    public void setFramesAfterUpdate(int framesAfterUpdate) {
        this.framesAfterUpdate = framesAfterUpdate;
    }

    private int framesAfterUpdate = 1;

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public double getXPosition() {
        return xPosition;
    }

    public void setXPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public double getYPosition() {
        return yPosition;
    }

    public void setYPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }


    public int getStartFrame() {
        return startFrame;
    }

    public void setStartFrame(int startFrame) {
        this.startFrame = startFrame;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getSize(){
        return size;
    }

    public void increaseSize(double size){
        if(this.size < 100){
            this.size += size;
        }
    }

    public void updateCircleToSize(){
        circle.setRadius(size);
    }
}
