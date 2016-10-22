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
import com.lecet.app.databinding.FragmentDashboardProjectsRecentlyUpdatedBinding;
import com.lecet.app.interfaces.DashboardChart;
import com.lecet.app.interfaces.MHSDataSource;
import com.lecet.app.interfaces.MHSDelegate;
import com.lecet.app.viewmodel.DashboardProjectsRecentlyUpdatedVM;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardProjectsRecentlyUpdatedFragment extends BaseDashboardChartFragment implements DashboardChart  {

    private static final String TAG = "ProjectsRecentlyUpdated";

    private MHSDataSource dataSource;
    private MHSDelegate delegate;

    private FragmentDashboardProjectsRecentlyUpdatedBinding binding;

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_projects_recently_updated, container, false);
        binding.setViewModel(new DashboardProjectsRecentlyUpdatedVM(this));
        View view = binding.getRoot();
        pieChart = binding.pieChartView;
        DashboardProjectsRecentlyUpdatedVM viewModel = binding.getViewModel();
        viewModel.initialize(view, subtitle, dataSource, delegate);
        viewModel.initializeChart(pieChart);
        return view;
    }

}