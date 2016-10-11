package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.lecet.app.BR;
import com.lecet.app.content.widget.LecetCalendar;
import com.lecet.app.data.models.Bid;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.utility.DateUtility;
import com.p_v.flexiblecalendar.FlexibleCalendarView;

import java.util.Calendar;
import java.util.Date;
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

    public CalendarViewModel(Fragment fragment, BidDomain bidDomain) {
        this.fragment = fragment;
        this.bidDomain = bidDomain;
    }

    public void initializeCalendar(LecetCalendar calendarView) {
        calendarView.setOnDateClickListener(this);
        Calendar cal = Calendar.getInstance();
        cal.set(calendarView.getSelectedDateItem().getYear(), calendarView.getSelectedDateItem().getMonth(), 1);
        setMonth(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));

    }

    @Bindable
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
        notifyPropertyChanged(BR.month);
    }

    public void fetchBids(final LecetCalendar calendarView) {
        Date date = DateUtility.getFirstDateOfTheMonth(new Date());
        bidDomain.getBidsRecentlyMade(date, 100, new Callback<List<Bid>>() {
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
        Toast.makeText(fragment.getContext(), cal.getTime().toString() + " Clicked", Toast.LENGTH_SHORT).show();
    }
}
