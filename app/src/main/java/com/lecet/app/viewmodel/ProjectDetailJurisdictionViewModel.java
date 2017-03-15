package com.lecet.app.viewmodel;

import android.util.Log;

import com.lecet.app.data.models.DistrictCouncil;
import com.lecet.app.data.models.Jurisdiction;
import com.lecet.app.data.models.Region;
import com.lecet.app.domain.ProjectDomain;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: ProjectDetailJurisdictionViewModel Created: 1/13/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class ProjectDetailJurisdictionViewModel extends ProjDetailItemViewModel {

    private final ProjectDomain projectDomain;
    private final long projectId;

    public ProjectDetailJurisdictionViewModel(ProjectDomain projectDomain, long projectId, String title) {
        super(title, "");

        this.projectDomain = projectDomain;
        this.projectId = projectId;
        getJurisdiction();
    }

    private void getJurisdiction() {

        projectDomain.getProjectJurisdiction(projectId, new Callback<List<Jurisdiction>>() {
            @Override
            public void onResponse(Call<List<Jurisdiction>> call, Response<List<Jurisdiction>> response) {

                if (response.isSuccessful()) {

                    List<Jurisdiction> jurisdictions = response.body();

                    String jurString = "";

                    for (int i = 0; i < jurisdictions.size(); i++) {

                        Jurisdiction jurisdiction = jurisdictions.get(i);
                        if (jurisdiction != null && jurisdiction.getDistrictCouncil() != null) {

                            // local
                            String jName = jurisdiction.getName().trim();
                            if(jName != null && !jName.isEmpty()) {
                                jurString = jurString + "Local: " + jName + "\n";
                            }

                            // council
                            DistrictCouncil districtCouncil = jurisdiction.getDistrictCouncil();
                            String dcName = districtCouncil.getName().trim();
                            if(dcName != null && !dcName.isEmpty()) {
                                jurString = jurString + "District: " + dcName + "\n";
                            }

                            // region
                            List<Region> regions = jurisdiction.getRegions();
                            for(Region region : regions) {
                                String rName = region.getName();
                                if (rName != null && !rName.isEmpty()) {
                                    jurString = jurString + "Region: " + rName + "\n";
                                }
                            }

                            if (i != (jurisdictions.size() - 1)) {
                                jurString = jurString + "\n";
                            }
                        }

                    }

                    setInfo(jurString);

                } else {
                    Log.d("ProjectDetailJurisdVM", "onResponse: ");
                }
            }

            @Override
            public void onFailure(Call<List<Jurisdiction>> call, Throwable t) {
            }
        });
    }
}
