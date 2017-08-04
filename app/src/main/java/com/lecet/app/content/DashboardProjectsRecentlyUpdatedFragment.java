package com.lecet.app.content;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.contentbase.BaseDashboardChartFragment;
import com.lecet.app.databinding.FragmentDashboardChartBaseBinding;
import com.lecet.app.interfaces.DashboardChart;
import com.lecet.app.interfaces.MRUDataSource;
import com.lecet.app.interfaces.MRUDelegate;
import com.lecet.app.utility.Log;
import com.lecet.app.viewmodel.BaseDashboardChartViewModel;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardProjectsRecentlyUpdatedFragment extends BaseDashboardChartFragment implements DashboardChart {

    private static final String TAG = "ProjectsRecentlyUpdated";

    private MRUDataSource dataSource;
    private MRUDelegate delegate;

    private FragmentDashboardChartBaseBinding binding;

    public static DashboardProjectsRecentlyUpdatedFragment newInstance(String subtitle) {
        Log.d(TAG, "newInstance");
        DashboardProjectsRecentlyUpdatedFragment fragmentInstance = new DashboardProjectsRecentlyUpdatedFragment();
        Bundle args = new Bundle();
        args.putString(BaseDashboardChartFragment.ARG_SUBTITLE, subtitle);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }


    public DashboardProjectsRecentlyUpdatedFragment() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            delegate = (MRUDelegate) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MRUDelegate");
        }

        try {
            dataSource = (MRUDataSource) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MRUDataSource");
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
        binding.setViewModel(new BaseDashboardChartViewModel(this, BaseDashboardChartViewModel.DATA_SOURCE_TYPE_MRU));
        View view = binding.getRoot();
        pieChart = binding.pieChartView;
        BaseDashboardChartViewModel viewModel = binding.getViewModel();
        viewModel.initializeMRU(view, subtitle, dataSource, delegate);
        viewModel.initializeChart(pieChart);
        return view;
    }

}