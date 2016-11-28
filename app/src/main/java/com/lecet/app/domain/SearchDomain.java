package com.lecet.app.domain;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.api.service.SearchService;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchList;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.data.models.User;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by getdevsinc on 11/21/16.
 */

public class SearchDomain {
    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;
    private String sfilter="";

    public SearchDomain(LecetClient lecetClient, LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {
        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
    }
    public SearchDomain(LecetClient lecetClient, LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm, String filter) {
        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
        sfilter = filter;
    }

    public void getSearchRecentlyViewed(long userId, Callback<List<SearchList>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        Call<List<SearchList>> call = lecetClient.getSearchService().getSearchRecentlyViewed(token, userId);
        call.enqueue(callback);
    }
    public void getSearchRecentlyViewedWithFilter(long userId, Callback<List<SearchList>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
     //   sfilter = sfilter.replace("10","5");
        Call<List<SearchList>> call = lecetClient.getSearchService().getSearchRecentlyViewedWithFilter(token, userId,sfilter);
        call.enqueue(callback);
    }

    public void getSearchSaved(long userId, Callback<List<SearchSaved>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        Call<List<SearchSaved>> call = lecetClient.getSearchService().getSearchSaved(token, userId);
        call.enqueue(callback);
    }

    public void getSearchProject(String q, Callback<List<Project>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        Call<List<Project>> call = lecetClient.getSearchService().getSearchProject(token,q, sfilter);
        call.enqueue(callback);
    }

    public void getSearchCompany(String q, Callback<List<Company>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        Call<List<Company>> call = lecetClient.getSearchService().getSearchCompany(token,q, sfilter);
        call.enqueue(callback);
    }
   /* public SearchList copyToRealmTransaction(SearchList searchList) {

        realm.beginTransaction();
        SearchList persistedSearchList = realm.copyToRealmOrUpdate(searchList);
        realm.commitTransaction();

        return persistedSearchList;
    }*/
}

