package com.lecet.app.domain;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.County;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;

/**
 * Created by getdevs on 05/07/2017.
 */

public class CountyDomain {
    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;

    public CountyDomain(LecetClient lecetClient, final LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {

        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
    }

    public Call<List<County>> getCountyInAState(String county){
        String token = sharedPreferenceUtil.getAccessToken();
        String filter = String.format("{\"where\":{\"state\":\"%s\"}, \"order\":\"countyName\"}" , county);
        Call<List<County>> call = lecetClient.getCountyService().getCounty(token, filter);
        return call;

    }

    public RealmResults<County> getCounty(String county){
        RealmResults<County> results =  realm.where(County.class).equalTo("state",county).findAll();
        return results;
    }
}
