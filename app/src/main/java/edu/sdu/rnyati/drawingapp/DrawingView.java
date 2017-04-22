package edu.sdu.rnyati.drawingapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by raghavnyati on 2/19/17.
 */

public class DrawingView extends View {

    private static final float DEFAULT_RADIUS = 100f;

    private Circle circle;
    private List<Circle> circleList = new ArrayList<>();

    private VelocityTracker velocityTracker;

    private Paint paint;

    private float initialX = 0f;
    private float initialY = 0f;
    private float currentX = 0f;
    private float currentY = 0f;
    private float radius = 0f;
    private float xVelocity = 0f;
    private float yVelocity = 0f;

    private PointF currentPoint;
    private PointF mDeleteInitialPoint;
    private PointF mMoveInitialPoint;

    private String appMode;
    private String actionTouch;

    public DrawingView(Context context){
        super(context);
        paint = new Paint();
    }

    public DrawingView(Context context, AttributeSet attrSet){
        super(context, attrSet);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        currentPoint = new PointF(event.getX(), event.getY());
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                goDownEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                goMoveEvent(event);
                break;
            case MotionEvent.ACTION_UP:
                goUpEvent(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                cancelEvent();
                break;
            default:
                circle = null;
        }
        return true;
    }

    private void goMoveEvent(MotionEvent event){
        actionTouch = "MOVE";
        if(appMode == "draw"){
            if(circle != null) {
                circle.setCurrent(currentPoint);
                circleList.add(circle);
                invalidate();
            }
        }else if(appMode == "move"){
            velocityTracker.addMovement(event);
        }
    }

    private void goDownEvent(MotionEvent event){
        actionTouch = "DOWN";
        if(appMode == "draw") {
            circle = new Circle(currentPoint);
            circle.setColor(paint.getColor());
        }else if(appMode == "move"){
            velocityTracker = VelocityTracker.obtain();
            velocityTracker.addMovement(event);
            mMoveInitialPoint = currentPoint;
            invalidate();
        }else if(appMode == "delete"){
            mDeleteInitialPoint = currentPoint;
        }
    }

    private void goUpEvent(MotionEvent event){
        actionTouch = "UP";
        if(appMode == "draw"){
            if(circle != null) {
                circle.setCurrent(currentPoint);
                circleList.add(circle);
                invalidate();
            }
        }else if(appMode == "move"){
            velocityTracker.addMovement(event);
            velocityTracker.computeCurrentVelocity(1);
            xVelocity = velocityTracker.getXVelocity();
            yVelocity = velocityTracker.getYVelocity();
            invalidate();
        } else if(appMode == "delete"){
            invalidate();
        }
    }

    private void cancelEvent(){
        circle = null;
        velocityTracker.clear();
    }

    public void setDefaultColor(String color){
        if(color == "blue")
            paint.setColor(Color.BLUE);
        else if(color == "green")
            paint.setColor(Color.GREEN);
        else if(color == "red")
            paint.setColor(Color.RED);
        else if(color == "black")
            paint.setColor(Color.BLACK);
    }

    public void setDefaultMode(String mode){
        appMode = mode;
        mMoveInitialPoint = new PointF();
        mDeleteInitialPoint = new PointF();
        if(appMode == "move") {
            velocityTracker = null;
            invalidate();
        }
    }

    private void moveOnDraw(Circle circle){
        if(actionTouch == "UP"){
            float distance = (float) (Math.sqrt(Math.pow((mMoveInitialPoint.x -  initialX), 2) + Math.pow((mMoveInitialPoint.y - initialY), 2)));
            if(distance <= circle.getRadius() && !circle.getIsMovable()){
                circle.setXVelocity(0);
                circle.setYVelocity(0);
                circle.setIsMovable(true);
            }else if(!circle.getIsMovable()){
                circle.setIsMovable(false);
            }
            if(!circle.getIsMoving() && circle.getIsMovable()) {
                circle.setXVelocity(xVelocity);
                circle.setYVelocity(yVelocity);
                circle.setIsMoving(true);
            }
        }
    }

    private void getCircleMoving(Canvas canvas, Circle circle){
        float circleXVelocity = circle.getXVelocity();
        float circleYVelocity = circle.getYVelocity();
        radius = circle.getRadius();
        if( initialX - radius + circleXVelocity <= 0 ||  initialX + radius + circleXVelocity >= canvas.getWidth()) {
            circleXVelocity = -circleXVelocity;
            circle.setXVelocity(circleXVelocity);
        }else if(initialY - radius + circleYVelocity <= 0 || initialY + radius + circleYVelocity >= canvas.getHeight()) {
            circleYVelocity = -circleYVelocity;
            circle.setYVelocity(circleYVelocity);
        }
        circle.setOrigin(new PointF( initialX + circleXVelocity, initialY + circleYVelocity));
    }

    @Override
    protected void onDraw(Canvas canvas){
        Iterator<Circle> circleIterator = circleList.iterator();
        while (circleIterator.hasNext()) {
            Circle circle = circleIterator.next();
             initialX = circle.getOrigin().x;
            initialY = circle.getOrigin().y;

            if(appMode == "move"){
                moveOnDraw(circle);
            }else{
                circle.setIsMoving(false);
                circle.setIsMovable(false);
            }

            if(appMode == "delete"){
                float initialPointDistance = (float) (Math.sqrt(Math.pow((mDeleteInitialPoint.x -  initialX), 2) + Math.pow((mDeleteInitialPoint.y - initialY), 2)));
                float endPointDistance = (float) (Math.sqrt(Math.pow((currentPoint.x -  initialX), 2) + Math.pow((currentPoint.y - initialY), 2)));
                if(initialPointDistance <= circle.getRadius() && endPointDistance <= circle.getRadius()) {
                    circleIterator.remove();
                    continue;
                }
            }

            if(appMode == "move"
                    && circle.getIsMoving()){
                getCircleMoving(canvas, circle);
            }else{
                currentX = circle.getCurrent().x;
                currentY = circle.getCurrent().y;
                if ( initialX == currentX && initialY == currentY) {
                    radius = DEFAULT_RADIUS;
                }
                else
                    radius = (float) (Math.sqrt(Math.pow((currentX -  initialX), 2) + Math.pow((currentY - initialY), 2)));
                if( initialX - radius >= 0
                        && initialY - radius >= 0
                        &&  initialX + radius <= canvas.getWidth()
                        && initialY + radius <= canvas.getHeight())
                    circle.setRadius(radius);
                else
                    radius = circle.getRadius();
            }
            paint.setColor(circle.getColor());
            canvas.drawCircle(circle.getOrigin().x, circle.getOrigin().y, radius, paint);
        }
        actionTouch = "";
        if(appMode == "move")
            invalidate();
    }
}
