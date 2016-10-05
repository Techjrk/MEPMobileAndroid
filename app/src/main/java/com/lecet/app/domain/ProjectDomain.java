package com.lecet.app.domain;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.utility.DateUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * File: ProjectDomain Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectDomain {

    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;

    public ProjectDomain(LecetClient lecetClient, final LecetSharedPreferenceUtil sharedPreferenceUtil) {

        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
    }

    public void getBidsHappeningSoon(Date startDate, Date endDate, int limit, Callback<List<Project>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStart = sdf.format(startDate);
        String formattedEnd = sdf.format(endDate);

        String filter = String.format("{\"include\":[\"projectStage\", {\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}], " +
                "\"where\":{\"and\":[{\"bidDate\":{\"gte\":\"%s\"}},{\"bidDate\":{\"lte\":\"%s\"}}]}," +
                " \"limit\":%d, \"order\":\"firstPublishDate DESC\"}", formattedStart, formattedEnd, limit);

        Call<List<Project>> call = lecetClient.getProjectService().projects(token, filter);
        call.enqueue(callback);
    }


    public void getBidsHappeningSoon(int limit, Callback<List<Project>> callback) {

        Date current = new Date();
        Date endDate = DateUtility.addDays(30);
        getBidsHappeningSoon(current, endDate, limit, callback);
    }
}
