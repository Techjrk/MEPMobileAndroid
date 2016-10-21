package com.lecet.app.content;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.contentbase.BaseDashboardChartFragment;
import com.lecet.app.data.models.Bid;
import com.lecet.app.databinding.FragmentDashboardChartBaseBinding;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MBRDataSource;
import com.lecet.app.interfaces.MBRDelegate;
import com.lecet.app.viewmodel.CalendarViewModel;
import com.lecet.app.viewmodel.DashboardChartBaseViewModel;

import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardBidsRecentlyMadeFragment extends BaseDashboardChartFragment {

    private static final String TAG = "BidsRecentlyMadeFrag";

    private MBRDataSource dataSource;
    private MBRDelegate delegate;

    private List<Bid> mBids;
    private FragmentDashboardChartBaseBinding binding;
    private int page;
    private String title;
    private String subtitle;

    public static DashboardBidsRecentlyMadeFragment newInstance(int page, String title, String subtitle) {
        Log.d(TAG, "newInstance");
        DashboardBidsRecentlyMadeFragment fragmentInstance = new DashboardBidsRecentlyMadeFragment();
        Bundle args = new Bundle();
        args.putInt(BaseDashboardChartFragment.ARG_PAGE, page);
        args.putString(BaseDashboardChartFragment.ARG_TITLE, title);
        args.putString(BaseDashboardChartFragment.ARG_SUBTITLE, subtitle);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    public DashboardBidsRecentlyMadeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt(BaseDashboardChartFragment.ARG_PAGE);
            title = getArguments().getString(BaseDashboardChartFragment.ARG_TITLE);
            subtitle = getArguments().getString(BaseDashboardChartFragment.ARG_SUBTITLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            delegate = (MBRDelegate) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MBRDelegate");
        }

        try {
            dataSource = (MBRDataSource) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MBRDataSource");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.getViewModel().fetchBids(binding.pieChartView);

        /*dataSource.refreshRecentlyMadeBids(new LecetCallback<TreeMap<Long, TreeSet<Bid>>>() {
            @Override
            public void onSuccess(TreeMap<Long, TreeSet<Bid>> result) {
                Log.d(TAG, "********************* onSuccess: " + result);
            }

            @Override
            public void onFailure(int code, String message) {
                Log.e(TAG, "********************* onFailure: " + message);

            }
        });*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_chart_base, container, false);
        binding.setViewModel(new DashboardChartBaseViewModel(this, dataSource, delegate));
        View view = binding.getRoot();
        binding.getViewModel().initializeChart(binding.pieChartView);
        return view;
    }

}