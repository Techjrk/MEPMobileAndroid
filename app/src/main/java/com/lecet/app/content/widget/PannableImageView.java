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

/**
 * Created by alionque on 6/1/17.
 */

public class PannableImageView extends android.support.v7.widget.AppCompatImageView {
    private static final String TAG = "PannableImageView";
    private static final int INVALID_POINTER_ID = -1;

    private float xPos = getX();
    private float yPos = getY();

    private float lastTouchX;
    private float imageWidth = 0;
    private float minX = 0;
    private float maxX = 0;


    private int firstTouchId = -1;

    public PannableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PannableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Let the ScaleGestureDetector inspect all events.

        final int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final float x = MotionEventCompat.getX(event, pointerIndex);

                // Remember where we started (for dragging)
                lastTouchX = x;
                // Save the ID of this pointer (for dragging)
                firstTouchId = MotionEventCompat.getPointerId(event, 0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(event, firstTouchId);

                final float x = MotionEventCompat.getX(event, pointerIndex);

                // Calculate the distance moved
                final float dx = x - lastTouchX;

                xPos -= dx;

                super.
                invalidate();

                // Remember this touch position for the next move event
                lastTouchX = x;

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
        if(maxX == 0){
            imageWidth = (getDrawable().getIntrinsicWidth() * (getMeasuredHeight()/ getDrawable().getIntrinsicHeight()));
            maxX = (imageWidth - getMeasuredWidth())/ 2;
            minX = maxX * -1;
            Log.d(TAG, "onDraw: Intrinsic Width: " + getDrawable().getIntrinsicWidth());
            Log.d(TAG, "onDraw: \nMin X: " + minX + "\nMax X: " + maxX);
            Log.d(TAG, "onDraw: Height: " + getHeight());
            Log.d(TAG, "onDraw: Width: " + getWidth());
        }

        Log.d(TAG, "onDraw: X Pos: " +  xPos);
        xPos = Math.min(maxX, Math.max(minX, xPos));
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            scrollTo((int) xPos, (int) yPos);
        }
        super.onDraw(canvas);
        canvas.restore();
    }


}
