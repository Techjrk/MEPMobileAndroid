package com.lecet.app.viewmodel;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.lecet.app.data.models.Bid;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.interfaces.DashboardChartFetchData;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MBRDataSource;
import com.lecet.app.interfaces.MBRDelegate;

import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Jason M on 5/10/2016.
 */

public class DashboardBidsRecentlyMadeVM extends BaseDashboardChartViewModel implements DashboardChartFetchData {

    private final String TAG = "DashboardBidsRtlyMadeVM";

    private MBRDataSource dataSource;
    private MBRDelegate delegate;

    public DashboardBidsRecentlyMadeVM(Fragment fragment) {
        super(fragment);
    }

    public void initialize(View view, String subtitle, MBRDataSource dataSource, MBRDelegate delegate) {
        this.dataSource = dataSource;
        this.delegate = delegate;
        setSubtitle(subtitle);
        setReferences();
    }

    @Override
    public void fetchData(final PieChart pieChartView) {

        dataSource.refreshRecentlyMadeBids(new LecetCallback<TreeMap<Long, TreeSet<Bid>>>() {

            @Override
            public void onSuccess(TreeMap<Long, TreeSet<Bid>> result) {
                Log.d(TAG, "onSuccess: " + result);

                TreeSet<Bid> housingSet     = result.get(RESULT_CODE_HOUSING);
                TreeSet<Bid> engineeringSet = result.get(RESULT_CODE_ENGINEERING);
                TreeSet<Bid> buildingSet    = result.get(RESULT_CODE_BUILDING);
                TreeSet<Bid> utilitiesSet   = result.get(RESULT_CODE_UTILITIES);

                resetDataSetSizes();

                if(housingSet != null)     housingSetSize     = housingSet.size();
                if(engineeringSet != null) engineeringSetSize = engineeringSet.size();
                if(buildingSet != null)    buildingSetSize    = buildingSet.size();
                if(utilitiesSet != null)   utilitiesSetSize   = utilitiesSet.size();

                Log.d(TAG, "onSuccess: housingSetSize: " + housingSetSize);
                Log.d(TAG, "onSuccess: engineeringSetSize: " + engineeringSetSize);
                Log.d(TAG, "onSuccess: buildingSetSize: " + buildingSetSize);
                Log.d(TAG, "onSuccess: utilitiesSetSize: " + utilitiesSetSize);

                handleIncomingData();
            }

            @Override
            public void onFailure(int code, String message) {
                Log.e(TAG, "onFailure: " + message);
                // TODO - check behavior of chart on no data - currently displays 'no data' text
            }
        });
    }

    @Override
    public void notifyDelegateOfSelection(Long category) {
        if(category == RESULT_CODE_HOUSING) {
            delegate.bidGroupSelected(BidDomain.HOUSING);
        }
        else if(category == RESULT_CODE_ENGINEERING) {
            delegate.bidGroupSelected(BidDomain.ENGINEERING);
        }
        else if(category == RESULT_CODE_BUILDING) {
            delegate.bidGroupSelected(BidDomain.BUILDING);
        }
        else if(category == RESULT_CODE_UTILITIES) {
            delegate.bidGroupSelected(BidDomain.UTILITIES);
        }
    }


}
