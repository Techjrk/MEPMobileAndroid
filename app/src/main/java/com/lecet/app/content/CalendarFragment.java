package com.lecet.app.content;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lecet.app.R;
import com.lecet.app.data.models.Bid;
import com.lecet.app.databinding.FragmentCalendarBinding;
import com.p_v.flexiblecalendar.FlexibleCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment implements FlexibleCalendarView.OnDateClickListener {

    private List<Bid> mBids;
    FragmentCalendarBinding binding;

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);
        View view = binding.getRoot();

//        binding.calendarView.setOnDateClickListener(this);

        Calendar cal = Calendar.getInstance();
        cal.set(binding.calendarView.getSelectedDateItem().getYear(), binding.calendarView.getSelectedDateItem().getMonth(), 1);
        binding.monthTextView.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));

        updateBids(new ArrayList<Bid>());
        return view;
    }

    private void updateBids(List<Bid> bids) {
        this.mBids = bids;
        binding.calendarView.addEventsToCalendar(bids);
    }

    @Override
    public void onDateClick(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        Toast.makeText(getActivity(), cal.getTime().toString() + " Clicked", Toast.LENGTH_SHORT).show();
    }

}
