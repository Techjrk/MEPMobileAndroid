package com.lecet.app.content;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.contentbase.DashboardChartFragmentBase;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.FragmentDashboardBidsHappeningSoonBinding;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.interfaces.MHSDataSource;
import com.lecet.app.interfaces.MHSDelegate;
import com.lecet.app.viewmodel.CalendarViewModel;

import java.util.List;

import io.realm.Realm;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardBidsHappeningSoonFragment extends Fragment {

    private static final String TAG = "CalendarBidsHappeningSoonFrag";

    private MHSDataSource dataSource;
    private MHSDelegate delegate;

    private List<Bid> mBids;
    private FragmentDashboardBidsHappeningSoonBinding binding;
    private int page;
    private String title;
    private String subtitle;

    public static DashboardBidsHappeningSoonFragment newInstance(int page, String title, String subtitle) {
        DashboardBidsHappeningSoonFragment fragmentInstance = new DashboardBidsHappeningSoonFragment();
        Bundle args = new Bundle();
        args.putInt(DashboardChartFragmentBase.ARG_PAGE, page);
        args.putString(DashboardChartFragmentBase.ARG_TITLE, title);
        args.putString(DashboardChartFragmentBase.ARG_SUBTITLE, subtitle);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    public DashboardBidsHappeningSoonFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt(DashboardChartFragmentBase.ARG_PAGE);
            title = getArguments().getString(DashboardChartFragmentBase.ARG_TITLE);
            subtitle = getArguments().getString(DashboardChartFragmentBase.ARG_SUBTITLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            delegate = (MHSDelegate) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MHSDelegate");
        }

        try {
            dataSource = (MHSDataSource) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MHSDataSource");
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
        binding.setViewModel(new CalendarViewModel(this, dataSource, delegate));
        View view = binding.getRoot();
        binding.getViewModel().initializeCalendar(binding.calendarView);
        return view;
    }
}