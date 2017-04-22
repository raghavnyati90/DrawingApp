package edu.sdu.rnyati.drawingapp;

import android.graphics.PointF;

/**
 * Created by raghavnyati on 2/19/17.
 */

public class Circle {

    protected float radius, xVelocity, yVelocity;
    protected int color;
    protected boolean isMovable, isMoving;
    protected PointF current, origin;

    public Circle() {}

    public Circle(PointF point){
        xVelocity = 0f;
        yVelocity= 0f;
        origin = point;
        current = point;
        radius = 0f;
        isMoving = false;

    }

    public float getXVelocity(){
        return xVelocity;
    }

    public void setXVelocity(float xVelocityValue){
        xVelocity = xVelocityValue;
    }

    public float getYVelocity(){
        return yVelocity;
    }

    public void setYVelocity(float yVelocityValue){
        yVelocity = yVelocityValue;
    }

    public PointF getOrigin(){
        return origin;
    }

    public void setOrigin(PointF point){
        origin = point;
    }

    public PointF getCurrent(){
        return current;
    }

    public void setCurrent(PointF point){
        current = point;
    }

    public float getRadius(){
        return radius;
    }

    public void setRadius(float circleRadius){
        radius = circleRadius;
    }

    public int getColor(){
        return color;
    }

    public void setColor(int color){
        this.color = color;
    }

    public boolean getIsMovable(){
        return isMovable;
    }

    public void setIsMovable(boolean isInMovableRegion){
        isMovable = isInMovableRegion;
    }

    public boolean getIsMoving(){
        return isMoving;
    }

    public void setIsMoving(boolean isAlreadyMoving){
        isMoving = isAlreadyMoving;
    }
}
