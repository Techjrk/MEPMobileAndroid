package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lecet.app.R;
import com.lecet.app.data.models.Bid;
import com.lecet.app.databinding.FragmentDashboardBidsHappeningSoonBinding;
import com.p_v.flexiblecalendar.FlexibleCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardBidsHappeningSoonFragment extends Fragment implements FlexibleCalendarView.OnDateClickListener {

    private static final String TAG = "CalendarBidsHappeningSoonFrag";

    private static final String ARG_PAGE = "page";
    private static final String ARG_TITLE = "title";

    private List<Bid> mBids;
    private FragmentDashboardBidsHappeningSoonBinding binding;
    private int mPage;
    private String mTitle;

    public static DashboardBidsHappeningSoonFragment newInstance(int page, String title) {
        DashboardBidsHappeningSoonFragment fragmentInstance = new DashboardBidsHappeningSoonFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_TITLE, title);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
            mTitle = getArguments().getString(ARG_TITLE);
        }
    }

    public DashboardBidsHappeningSoonFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_bids_happening_soon, container, false);
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