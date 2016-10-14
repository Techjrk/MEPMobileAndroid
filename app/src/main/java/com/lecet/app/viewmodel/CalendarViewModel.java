package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.lecet.app.BR;
import com.lecet.app.content.widget.LecetCalendar;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MHSDataSource;
import com.lecet.app.interfaces.MHSDelegate;
import com.lecet.app.utility.DateUtility;
import com.p_v.flexiblecalendar.FlexibleCalendarView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Josué Rodríguez on 5/10/2016.
 */

public class CalendarViewModel extends BaseObservable implements FlexibleCalendarView.OnDateClickListener {

    private final Fragment fragment;
    private MHSDataSource dataSource;
    private MHSDelegate delegate;

    private String month;
    private Calendar lastDateSelected;

    public CalendarViewModel(Fragment fragment, MHSDataSource dataSource, MHSDelegate delegate) {

        this.fragment = fragment;
        this.dataSource = dataSource;
        this.delegate = delegate;
    }

    @Bindable
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
        notifyPropertyChanged(BR.month);
    }

    public void initializeCalendar(final LecetCalendar calendarView) {
        calendarView.setOnDateClickListener(this);
        final Calendar cal = Calendar.getInstance();
        cal.set(calendarView.getSelectedDateItem().getYear(), calendarView.getSelectedDateItem().getMonth(), 1);
        setMonth(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
    }

    public void fetchBids(final LecetCalendar calendarView) {

        dataSource.refreshProjectsHappeningSoon(new LecetCallback<Project[]>() {
            @Override
            public void onSuccess(Project[] result) {

                calendarView.addEventsToCalendar(Arrays.asList(result));
            }

            @Override
            public void onFailure(int code, String message) {

            }
        });
    }

    @Override
    public void onDateClick(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        lastDateSelected = cal;
        delegate.calendarSelected(lastDateSelected.getTime());
    }
}
