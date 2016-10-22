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
import com.lecet.app.databinding.FragmentDashboardBidsRecentlyMadeBinding;
import com.lecet.app.interfaces.DashboardChart;
import com.lecet.app.interfaces.MBRDataSource;
import com.lecet.app.interfaces.MBRDelegate;
import com.lecet.app.viewmodel.DashboardBidsRecentlyMadeVM;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardBidsRecentlyMadeFragment extends BaseDashboardChartFragment implements DashboardChart {

    private static final String TAG = "BidsRecentlyMadeFrag";

    private MBRDataSource dataSource;
    private MBRDelegate delegate;

    private FragmentDashboardBidsRecentlyMadeBinding binding;

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
        binding.getViewModel().fetchData(binding.pieChartView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = initDataBinding(inflater, container);
        return view;
    }

    @Override
    public View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_bids_recently_made, container, false);
        binding.setViewModel(new DashboardBidsRecentlyMadeVM(this));
        View view = binding.getRoot();
        pieChart = binding.pieChartView;
        DashboardBidsRecentlyMadeVM viewModel = binding.getViewModel();
        viewModel.initialize(view, dataSource, delegate);
        viewModel.initializeChart(pieChart);
        return view;
    }

}