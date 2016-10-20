package com.lecet.app.content.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.lecet.app.R;
import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.view.BaseCellView;

import java.util.List;

public class LacetCellView extends BaseCellView {

    private boolean hasEvents;

    public LacetCellView(Context context) {
        super(context);
    }

    public LacetCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LacetCellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getStateSet().contains(STATE_SELECTED)) {
            this.setBackgroundResource(R.drawable.calendar_day_selected_background);
        } else if ((getStateSet().contains(STATE_REGULAR) || getStateSet().contains(STATE_TODAY))
                && hasEvents) {
            this.setBackgroundResource(R.drawable.bid_event_background);
        }

        if (getStateSet().contains(STATE_OUTSIDE_MONTH)) {
            setTextColor(ContextCompat.getColor(getContext(), R.color.lecetTextLightGray));
        } else {
            setTextColor(ContextCompat.getColor(getContext(), R.color.lecetTextWhite));
        }
    }

    @Override
    public void setEvents(List<? extends Event> colorList) {
        this.hasEvents = colorList != null && !colorList.isEmpty();
        invalidate();
        requestLayout();
    }
}
