package com.lecet.app.content.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import javax.security.auth.login.LoginException;

/**
 * Created by alionque on 6/1/17.
 */

public class PannableImageView extends android.support.v7.widget.AppCompatImageView {
    private static final String TAG = "PannableImageView";
    private static final int INVALID_POINTER_ID = -1;

    private OnDrawListener onDrawListener = new OnDrawListener() {
        @Override
        public void onDrawCallback(Canvas canvas) { }
    };

    private float xPos = getX();
    private float yPos = getY();
    private float xPosPercentage = 0;
    private float yPosPercentage = 0;

    private float lastTouchY;
    private float minY = 0;
    private float maxY = 0;

    private float lastTouchX;
    private float minX = 0;
    private float maxX = 0;

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1f;
    private boolean needsUpdate = true;


    private int firstTouchId = -1;

    public PannableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public PannableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Let the ScaleGestureDetector inspect all events.

        final int action = MotionEventCompat.getActionMasked(event);
        scaleGestureDetector.onTouchEvent(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final float x = MotionEventCompat.getX(event, pointerIndex);
                final float y = MotionEventCompat.getY(event, pointerIndex);

                // Remember where we started (for dragging)
                lastTouchX = x;
                lastTouchY = y;

                // Save the ID of this pointer (for dragging)
                firstTouchId = MotionEventCompat.getPointerId(event, 0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(event, firstTouchId);

                final float x = MotionEventCompat.getX(event, pointerIndex);
                final float y = MotionEventCompat.getY(event, pointerIndex);

                // Calculate the distance moved
                final float dx = x - lastTouchX;
                final float dy = y - lastTouchY;

                xPos -= dx;
                yPos -= dy;

                super.
                invalidate();

                // Remember this touch position for the next move event
                lastTouchX = x;
                lastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                firstTouchId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                firstTouchId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);

                if (pointerId == firstTouchId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    lastTouchX = MotionEventCompat.getX(event, newPointerIndex);
                    lastTouchY = MotionEventCompat.getY(event, newPointerIndex);
                    firstTouchId = MotionEventCompat.getPointerId(event, newPointerIndex);
                }
                break;
            }
        }
        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        if(needsUpdate){

            float imageWidth = ((float) getDrawable().getIntrinsicWidth() * ((float)getHeight()/
                    (float)getDrawable().getIntrinsicHeight()));

            float imageHeight = getHeight();

            maxX = (imageWidth - ((float)getWidth()/scaleFactor)) * scaleFactor /2;
            minX = (((imageWidth) - ((float)getWidth())) * scaleFactor)/ - 2;

            maxY = ((imageHeight * scaleFactor) - imageHeight) /2;
            minY = 0;

            needsUpdate = false;
        }

        xPos = Math.max(minX, Math.min(maxX, xPos));
        yPos = Math.max(minY, Math.min(maxY, yPos));

        if(xPos > 0) {
            xPosPercentage = xPos / maxX;
        }else{
            xPosPercentage = xPos * -1 / minX;
        }

        yPosPercentage = yPos / maxY;


        xPosPercentage = Math.min(1, Math.max(-1, xPosPercentage));
        yPosPercentage = Math.min(1, Math.max(-1, yPosPercentage));

        Log.d(TAG, "onDraw: Y Pos: " + yPos);
        Log.d(TAG, "onDraw: ScaleFactor: " + scaleFactor);
        Log.d(TAG, "onDraw: Max Y: " + maxY);
        Log.d(TAG, "onDraw: Y Position Percentage: " + yPosPercentage);
        Log.d(TAG, "onDraw: X Pos: " + xPos);
        Log.d(TAG, "onDraw: Max X: " + maxX);
        Log.d(TAG, "onDraw: X Position Percentage: " + xPosPercentage);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            scrollTo((int) xPos, (int) yPos);
            canvas.scale(scaleFactor, scaleFactor);

        }
        super.onDraw(canvas);
        canvas.restore();
        onDrawListener.onDrawCallback(canvas);
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();

            needsUpdate = true;

            // Don't let the object get too small or too large.
            scaleFactor = Math.max(1f, Math.min(scaleFactor, 5f));

            invalidate();
            return true;
        }
    }

    public interface OnDrawListener{
        void onDrawCallback(Canvas canvas);
    }

    public void setOnDrawListener(OnDrawListener onDrawListener){
        this.onDrawListener = onDrawListener;
    }

    public float getXPos(){
        return xPos;
    }

    public float getxPosPercentage(){
        return xPosPercentage;
    }

    public float getYPos(){
        return yPos;
    }
}
