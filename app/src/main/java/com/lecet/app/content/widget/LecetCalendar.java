package com.lecet.app.content.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.lecet.app.R;
import com.lecet.app.data.models.Project;
import com.lecet.app.utility.DateUtility;
import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.MonthViewPager;
import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.entity.SelectedDateItem;
import com.p_v.flexiblecalendar.view.BaseCellView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LecetCalendar extends FlexibleCalendarView {

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
        Calendar calendar = Calendar.getInstance();
        final int maxWeeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        final int screenDensity = getResources().getDisplayMetrics().densityDpi;
        setCalendarView(new CalendarView() {
            @Override
            public BaseCellView getCellView(int position, View convertView, ViewGroup parent, @BaseCellView.CellType int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    cellView = (BaseCellView) inflater.inflate(R.layout.calendar_date_cell_view, null, false);
                    int size = 0;
                    if(screenDensity == DisplayMetrics.DENSITY_420){
                        size = maxWeeks > 5 ? getResources().getDimensionPixelSize(R.dimen.small_calendar_cell_size_420dp) : getResources().getDimensionPixelSize(R.dimen.calendar_cell_size_420dp);

                    }
                    else{
                        size = maxWeeks > 5 ? getResources().getDimensionPixelSize(R.dimen.small_calendar_cell_size) : getResources().getDimensionPixelSize(R.dimen.calendar_cell_size);

                    }
                    ViewGroup.LayoutParams params = new GridView.LayoutParams(size, size);
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
                    ViewGroup.LayoutParams params = new GridView.LayoutParams(0, 0);
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
        viewPager.requestDisallowInterceptTouchEvent(true);
        viewPager.setCanScrollHorizontally(false);
        viewPager.setSwipeEnable(false);

    }

    public void addEventsToCalendar(List<Project> projects) {
        if (projects == null) {
            projects = new ArrayList<>();
        }
        final List<Calendar> listOfProjectDates = DateUtility.getProjectDates(projects);
        setEventDataProvider(new EventDataProvider() {
            @Override
            public List<? extends Event> getEventsForTheDay(int year, int month, int day) {
                Calendar calendar = DateUtility.getCalendarHour0(year, month, day);
                if (listOfProjectDates.contains(calendar)) {
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
    public void onDateClick(SelectedDateItem selectedItem) {
        boolean dispatchClick = false;
        if (this.selectedDateItem.getYear() == selectedItem.getYear() && this.selectedDateItem.getMonth() == selectedItem.getMonth()) {
            dispatchClick = true;
            this.selectedDateItem = selectedItem;
        }

        if (dispatchClick) {
            this.redrawMonthGrid(this.lastPosition % 4);
            if (this.disableAutoDateSelection) {
                this.userSelectedItem = selectedItem.clone();
            }

            if (this.onDateClickListener != null) {
                this.onDateClickListener.onDateClick(selectedItem.getYear(), selectedItem.getMonth(), selectedItem.getDay());
            }
        }
    }
}
