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
import com.lecet.app.databinding.FragmentDashboardChartBaseBinding;
import com.lecet.app.interfaces.DashboardChart;
import com.lecet.app.interfaces.MBRDataSource;
import com.lecet.app.interfaces.MBRDelegate;
import com.lecet.app.viewmodel.BaseDashboardChartViewModel;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardBidsRecentlyMadeFragment extends BaseDashboardChartFragment implements DashboardChart {

    private static final String TAG = "BidsRecentlyMadeFrag";

    private MBRDataSource dataSource;
    private MBRDelegate delegate;

    private FragmentDashboardChartBaseBinding binding;

    public static DashboardBidsRecentlyMadeFragment newInstance(String subtitle) {
        Log.d(TAG, "newInstance");
        DashboardBidsRecentlyMadeFragment fragmentInstance = new DashboardBidsRecentlyMadeFragment();
        Bundle args = new Bundle();
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
        binding.getViewModel().fetchData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = initDataBinding(inflater, container);
        return view;
    }

    @Override
    public View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_chart_base, container, false);
        binding.setViewModel(new BaseDashboardChartViewModel(this, BaseDashboardChartViewModel.DATA_SOURCE_TYPE_MBR));
        View view = binding.getRoot();
        pieChart = binding.pieChartView;
        BaseDashboardChartViewModel viewModel = binding.getViewModel();
        viewModel.initializeMBR(view, subtitle, dataSource, delegate);
        viewModel.initializeChart(pieChart);
        return view;
    }

}