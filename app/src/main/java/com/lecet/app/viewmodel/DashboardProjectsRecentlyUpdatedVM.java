package com.lecet.app.viewmodel;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.lecet.app.data.models.Project;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.interfaces.DashboardChartFetchData;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MHSDataSource;
import com.lecet.app.interfaces.MHSDelegate;
import com.lecet.app.interfaces.MRUDataSource;
import com.lecet.app.interfaces.MRUDelegate;

import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Jason M on 5/10/2016.
 */

public class DashboardProjectsRecentlyUpdatedVM extends BaseDashboardChartViewModel implements DashboardChartFetchData {

    private final String TAG = "DashboardProjsRtlyUpdVM";

    private MRUDataSource dataSource;
    private MRUDelegate delegate;

    public DashboardProjectsRecentlyUpdatedVM(Fragment fragment) {
        super(fragment);
    }

    public void initialize(View view, String subtitle, MRUDataSource dataSource, MRUDelegate delegate) {
        this.dataSource = dataSource;
        this.delegate = delegate;
        setSubtitle(subtitle);
        setReferences();
    }

    @Override
    public void fetchData(final PieChart pieChartView) {

        dataSource.refreshRecentlyUpdatedProjects(new LecetCallback<TreeMap<Long, TreeSet<Project>>>() {
            @Override
            public void onSuccess(TreeMap<Long, TreeSet<Project>> result) {

                resetDataSetSizes();

                TreeSet<Project> housing = result.get(Long.valueOf(RESULT_CODE_HOUSING));
                TreeSet<Project> engineering = result.get(Long.valueOf(RESULT_CODE_ENGINEERING));
                TreeSet<Project> building = result.get(Long.valueOf(RESULT_CODE_BUILDING));
                TreeSet<Project> utilities = result.get(Long.valueOf(RESULT_CODE_UTILITIES));

                if (housing != null) {
                    housingSetSize = housing.size();
                }

                if (engineering != null) {
                    engineeringSetSize = engineering.size();
                }

                if (building != null) {
                    buildingSetSize = building.size();
                }

                if (utilities != null) {
                    utilitiesSetSize = utilities.size();
                }

                handleIncomingData();
            }

            @Override
            public void onFailure(int code, String message) {

                // TODO - check behavior of chart on no data - currently displays 'no data' text
            }
        });
    }

    @Override
    public void notifyDelegateOfSelection(Long category) {
        if(category == RESULT_CODE_HOUSING) {
            delegate.mruBidGroupSelected(BidDomain.HOUSING);
        }
        else if(category == RESULT_CODE_ENGINEERING) {
            delegate.mruBidGroupSelected(BidDomain.ENGINEERING);
        }
        else if(category == RESULT_CODE_BUILDING) {
            delegate.mruBidGroupSelected(BidDomain.BUILDING);
        }
        else if(category == RESULT_CODE_UTILITIES) {
            delegate.mruBidGroupSelected(BidDomain.UTILITIES);
        }
    }

}
