package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.lecet.app.BR;
import com.lecet.app.content.widget.LecetCalendar;
import com.lecet.app.data.models.Bid;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.utility.DateUtility;
import com.p_v.flexiblecalendar.FlexibleCalendarView;

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
    private final BidDomain bidDomain;

    private String month;
    private Calendar lastDateSelected;

    public CalendarViewModel(Fragment fragment, BidDomain bidDomain) {
        this.fragment = fragment;
        this.bidDomain = bidDomain;
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
        calendarView.setOnMonthChangeListener(new FlexibleCalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month, int direction) {
                calendarView.goToCurrentMonth();
                if (lastDateSelected != null) {
                    calendarView.selectDate(lastDateSelected);
                } else {
                    calendarView.selectDate(calendarView.getLastDateSelected());
                }
            }
        });
    }

    public void fetchBids(final LecetCalendar calendarView) {
        Calendar calendar = DateUtility.getFirstDateOfTheCurrentMonth();
        bidDomain.getBidsRecentlyMade(calendar.getTime(), 100, new Callback<List<Bid>>() {
            @Override
            public void onResponse(Call<List<Bid>> call, Response<List<Bid>> response) {
                calendarView.addEventsToCalendar(response.body());
            }

            @Override
            public void onFailure(Call<List<Bid>> call, Throwable t) {
                Log.d("TAG", "log");
            }
        });
    }

    @Override
    public void onDateClick(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        lastDateSelected = cal;
    }
}
