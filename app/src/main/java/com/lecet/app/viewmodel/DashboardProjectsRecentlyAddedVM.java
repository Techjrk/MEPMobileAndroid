package com.lecet.app.viewmodel;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.lecet.app.data.models.Project;
import com.lecet.app.interfaces.DashboardChartFetchData;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MHSDataSource;
import com.lecet.app.interfaces.MHSDelegate;

/**
 * Created by Jason M on 5/10/2016.
 */

public class DashboardProjectsRecentlyAddedVM extends BaseDashboardChartViewModel implements DashboardChartFetchData {

    private final String TAG = "DashboardProjsRtlyAddVM";

    private MHSDataSource dataSource;
    private MHSDelegate delegate;

    public DashboardProjectsRecentlyAddedVM(Fragment fragment) {
        super(fragment);
    }

    public void initialize(View view, String subtitle, MHSDataSource dataSource, MHSDelegate delegate) {
        this.dataSource = dataSource;
        this.delegate = delegate;
        setSubtitle(subtitle);
        setReferences();
    }

    @Override
    public void fetchData(final PieChart pieChartView) {

        dataSource.refreshProjectsHappeningSoon(new LecetCallback<Project[]>()  {

            @Override
            public void onSuccess(Project[] result) {
                Log.d(TAG, "onSuccess: " + result);

                Project p;
                long parentId;
                int len = result.length;

                resetDataSetSizes();

                for(int i=0; i<len; i++) {
                    p = result[i];
                    if(p.getProjectStage() != null) {
                        parentId = p.getProjectStage().getParentId();
                        if(parentId > 0) {
                            if(parentId == RESULT_CODE_HOUSING) {
                                ++housingSetSize;
                                continue;
                            }
                            else if(parentId == RESULT_CODE_ENGINEERING) {
                                ++engineeringSetSize;
                                continue;
                            }
                            else if(parentId == RESULT_CODE_BUILDING) {
                                ++buildingSetSize;
                                continue;
                            }
                            else if(parentId == RESULT_CODE_UTILITIES) {
                                ++utilitiesSetSize;
                                continue;
                            }
                        }
                    }
                }

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
            //TODO: add delegate call equivalent to bidGoupSelected
        }
        else if(category == RESULT_CODE_ENGINEERING) {
            //TODO: add delegate call equivalent to bidGoupSelected
        }
        else if(category == RESULT_CODE_BUILDING) {
            //TODO: add delegate call equivalent to bidGoupSelected
        }
        else if(category == RESULT_CODE_UTILITIES) {
            //TODO: add delegate call equivalent to bidGoupSelected
        }
    }

}
