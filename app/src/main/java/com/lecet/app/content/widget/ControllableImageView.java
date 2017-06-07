package com.lecet.app.content.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by alionque on 6/5/17.
 */

public class ControllableImageView extends android.support.v7.widget.AppCompatImageView {
    public static String TAG = "ControllableImageView";

    float xScroll = 0;
    float yScroll = 0;
    float imageWidth = 0;

    public ControllableImageView(Context context) {
        super(context);
    }

    public ControllableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTranslateValues(float xScroll, float yScroll){
        this.xScroll = xScroll;
        this.yScroll = yScroll;
        invalidate();
    }

    public void setImageWidth(float imageWidth) {
        this.imageWidth = imageWidth;

    }

    public void setViewBounds(int width){
        this.imageWidth -= (width/2);
    }

    public float getImageWidth() {
        return imageWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        Log.d(TAG, "onDraw: xScroll: " + xScroll);
        canvas.translate((int)(xScroll * imageWidth), (int)yScroll);
        super.onDraw(canvas);
        canvas.restore();
    }
}
