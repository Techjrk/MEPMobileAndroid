package com.lecet.app.viewmodel;

import com.lecet.app.data.models.DistrictCouncil;
import com.lecet.app.data.models.Jurisdiction;
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
                        DistrictCouncil districtCouncil = jurisdiction.getDistrictCouncil();
                        String abbreviation = districtCouncil.getAbbreviation();

                        if (abbreviation != null && abbreviation.length() > 0) {

                            jurString = jurString + " " + abbreviation;
                        }

                        if (i != (jurisdictions.size() - 1)) {
                            jurString = jurString + ", ";
                        }
                    }

                    setInfo(jurString);

                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Jurisdiction>> call, Throwable t) {
            }
        });
    }
}
