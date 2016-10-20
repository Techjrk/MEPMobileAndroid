package com.lecet.app.content;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lecet.app.contentbase.BaseDashboardChartFragment;
import com.lecet.app.data.models.Bid;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MBRDataSource;
import com.lecet.app.interfaces.MBRDelegate;

import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardBidsRecentlyMadeFragment extends BaseDashboardChartFragment {

    private static final String TAG = "BidsRecentlyMadeFrag";

    private MBRDelegate delegate;
    private MBRDataSource dataSource;


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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            throw new ClassCastException(context.toString() + " must implement MHSDataSource");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataSource.refreshRecentlyMadeBids(new LecetCallback<TreeMap<Long, TreeSet<Bid>>>() {
            @Override
            public void onSuccess(TreeMap<Long, TreeSet<Bid>> result) {

            }

            @Override
            public void onFailure(int code, String message) {

            }
        });
    }
}