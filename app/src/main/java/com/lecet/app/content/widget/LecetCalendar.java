package com.lecet.app.content.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.lecet.app.R;
import com.lecet.app.data.models.Bid;
import com.lecet.app.utility.DateUtility;
import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.MonthViewPager;
import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.view.BaseCellView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LecetCalendar extends FlexibleCalendarView {

    private Calendar lastDateSelected;

    private int displayYear;
    private int displayMonth;
    private int startDisplayDay;
    private int weekdayHorizontalSpacing;
    private int weekdayVerticalSpacing;
    private int monthDayHorizontalSpacing;
    private int monthDayVerticalSpacing;
    private int monthViewBackground;
    private int weekViewBackground;
    private boolean showDatesOutsideMonth;
    private boolean decorateDatesOutsideMonth;
    private boolean disableAutoDateSelection;

    /**
     * First day of the week in the calendar
     */
    private int startDayOfTheWeek;


    public LecetCalendar(Context context) {
        super(context);
        initializeCalendar();
    }

    public LecetCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeCalendar();
    }

    public LecetCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeCalendar();
    }

    private void initializeCalendar() {
        setCalendarView(new CalendarView() {
            @Override
            public BaseCellView getCellView(int position, View convertView, ViewGroup parent, @BaseCellView.CellType int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    cellView = (BaseCellView) inflater.inflate(R.layout.calendar_date_cell_view, null, false);
                    int size = getResources().getDimensionPixelSize(R.dimen.calendar_cell_size);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(size, size);
                    cellView.setLayoutParams(params);
                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(int position, View convertView, ViewGroup parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) { //removing week day name
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    cellView = (BaseCellView) inflater.inflate(R.layout.calendar_date_cell_view, null, false);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(0, 0);
                    cellView.setLayoutParams(params);
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
                return "";
            }
        });
        removeSwipe();
    }

    private void removeSwipe() {
        MonthViewPager viewPager = (MonthViewPager) getChildAt(1);
        viewPager.setSwipeEnable(false);

        setMyScroller(viewPager);
    }

    private void setMyScroller(ViewPager viewPager) {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(viewPager, new DisableSwipeScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DisableSwipeScroller extends Scroller {
        public DisableSwipeScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/);
        }
    }

    public void addEventsToCalendar(List<Bid> bids) {
        if (bids == null) {
            bids = new ArrayList<>();
        }
        final List<Calendar> listOfBidDates = DateUtility.getBidDates(bids);
        setEventDataProvider(new EventDataProvider() {
            @Override
            public List<? extends Event> getEventsForTheDay(int year, int month, int day) {
                Calendar calendar = DateUtility.getCalendarHour0(year, month, day);
                if (listOfBidDates.contains(calendar)) {
                    List<EventW> eventList = new ArrayList<>();
                    eventList.add(new EventW());
                    return eventList;
                }
                return null;
            }
        });
        selectDate(Calendar.getInstance());
    }

    public class EventW implements Event {

        @Override
        public int getColor() {
            return 0;
        }
    }

    @Override
    public void selectDate(Calendar calendar) {
        super.selectDate(calendar);
        lastDateSelected = calendar;
    }

    public Calendar getLastDateSelected() {
        return lastDateSelected;
    }

}
