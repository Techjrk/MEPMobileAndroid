package com.lecet.app.content.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.lecet.app.R;
import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.view.BaseCellView;

import java.util.List;

/**
 * @author p-v
 */
public class BidEvent extends BaseCellView {

    private boolean hasEvents;

    public BidEvent(Context context) {
        super(context);
    }

    public BidEvent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BidEvent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!getStateSet().contains(STATE_SELECTED) && !getStateSet().contains(SELECTED_TODAY) &&
                getStateSet().contains(STATE_REGULAR) && hasEvents) {
            this.setBackgroundResource(R.drawable.bid_event_background);

        }
        if (getStateSet().contains(STATE_SELECTED) && hasEvents) {
            this.setBackgroundResource(R.drawable.calendar_day_selected_background);
        }
    }

    @Override
    public void setEvents(List<? extends Event> colorList) {
        this.hasEvents = colorList != null && !colorList.isEmpty();
        invalidate();
        requestLayout();
    }

}
