package com.lecet.app.contentbase;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.lecet.app.R;
import com.lecet.app.data.models.Bid;
import com.lecet.app.databinding.FragmentDashboardChartBaseBinding;
import com.lecet.app.interfaces.MBRDataSource;
import com.lecet.app.interfaces.MBRDelegate;
import com.lecet.app.interfaces.MHSDataSource;
import com.lecet.app.interfaces.MHSDelegate;
import com.lecet.app.viewmodel.DashboardChartBaseViewModel;

import java.util.List;


/**
 * Created by jasonm on 10/5/16.
 */
public class BaseDashboardChartFragment extends Fragment {

    private static final String TAG = "DashboardChartFragBase";

    public static final String ARG_PAGE = "page";
    public static final String ARG_TITLE = "title";
    public static final String ARG_SUBTITLE = "subtitle";

    private MBRDataSource dataSource;
    private MBRDelegate delegate;

    private List<Bid> bids;
    private FragmentDashboardChartBaseBinding binding;
    protected int page;
    protected String title = "Title";
    protected String subtitle = "Subtitle";
    PieChart pieChart;

    public static BaseDashboardChartFragment newInstance(int page, String title, String subtitle) {
        Log.d(TAG, "newInstance");
        BaseDashboardChartFragment fragmentInstance = new BaseDashboardChartFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_SUBTITLE, subtitle);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt(ARG_PAGE, 0);
            title = getArguments().getString(ARG_TITLE);
            subtitle = getArguments().getString(ARG_SUBTITLE);
        }
        Log.d(TAG, "onCreate: " + title + " " + subtitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d(TAG, "onCreateView");

        //View view = inflater.inflate(R.layout.fragment_dashboard_chart_base, container, false);

        View view = initDataBinding(inflater, container);

        //TextView fragmentSubtitleNum = (TextView) view.findViewById(R.id.subtitle_num);
        //fragmentSubtitleText.setText(this.subtitle);

        //TextView fragmentTitleText = (TextView) view.findViewById(R.id.subtitle_text);
        //fragmentSubtitleText.setText(this.subtitle);

        return view;
    }

    protected View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_chart_base, container, false);
        binding.setViewModel(new DashboardChartBaseViewModel(this, dataSource, delegate));
        View view = binding.getRoot();
        PieChart pieChartView = binding.pieChartView;
        DashboardChartBaseViewModel viewModel = binding.getViewModel();
        viewModel.initialize(view);
        viewModel.initializeChart(pieChartView);
        return view;
    }

}