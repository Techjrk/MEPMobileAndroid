package com.lecet.app.domain;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.utility.DateUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * File: BidDomain Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class BidDomain {

    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;

    public BidDomain(LecetClient lecetClient, final LecetSharedPreferenceUtil sharedPreferenceUtil) {

        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
    }

    public void getBidsRecentlyMade(Date startDate, int limit, Callback<List<Bid>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
        String filter = String.format("{\"include\":[\"contact\",{\"project\":{\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}}], " +
                "\"limit\":%d, \"where\":{\"and\":[{\"createDate\":{\"gt\":\"%s\"}}, {\"rank\":1}]}}", limit, formattedDate);

        Call<List<Bid>> call = lecetClient.getBidService().bids(token, filter);
        call.enqueue(callback);
    }


    public void getBidsRecentlyMade(int limit, Callback<List<Bid>> callback) {

        Date startDate = DateUtility.addDays(-30);
        getBidsRecentlyMade(startDate, limit, callback);
    }
}
