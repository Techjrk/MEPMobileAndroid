package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.FragmentDashboardBidsHappeningSoonBinding;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.viewmodel.CalendarViewModel;

import java.util.List;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardBidsHappeningSoonFragment extends Fragment {

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

    public DashboardBidsHappeningSoonFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
            mTitle = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.getViewModel().fetchBids(binding.calendarView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_bids_happening_soon, container, false);
        binding.setViewModel(new CalendarViewModel(this, new BidDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getContext()))));
        View view = binding.getRoot();
        binding.getViewModel().initializeCalendar(binding.calendarView);
        return view;
    }
}