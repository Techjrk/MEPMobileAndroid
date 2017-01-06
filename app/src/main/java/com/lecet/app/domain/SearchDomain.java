package com.lecet.app.domain;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.api.service.SearchService;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchList;
import com.lecet.app.data.models.SearchProject;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.data.models.User;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by getdevsinc on 11/21/16.
 */

public class SearchDomain {
    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;
    private String filter="";
    private String query;
//constructor used for recentlyviewed, and savedsearch without filter
  /*  public SearchDomain(LecetClient lecetClient, LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {
        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
    }*/
  //constructor used for  with filter and project and company search
    public SearchDomain(LecetClient lecetClient, LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm, String filter) {
        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
        this.filter = filter;
    }
/*    public SearchDomain(LecetClient lecetClient, LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm,String query, String filter) {
        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
        this.filter = filter;
        this.query= query;
    }*/

    public void getSearchRecentlyViewed(long userId, Callback<List<SearchList>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        Call<List<SearchList>> call = lecetClient.getSearchService().getSearchRecentlyViewed(token, userId);
        call.enqueue(callback);
//        call.enqueue(callback);
    }

    public Response<List<SearchList>> getSearchRecentlyViewedSync(long userId) {
        String token = sharedPreferenceUtil.getAccessToken();

        Call<List<SearchList>> call = lecetClient.getSearchService().getSearchRecentlyViewedWithFilter(token, userId,filter);
        try {
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
//        call.enqueue(callback);
    }


    public void getSearchRecentlyViewedWithFilter(long userId, Callback<List<SearchList>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
     //   sfilter = sfilter.replace("10","5");
        Call<List<SearchList>> call = lecetClient.getSearchService().getSearchRecentlyViewedWithFilter(token, userId,filter);
        call.enqueue(callback);
    }

    public void getSearchSaved(long userId, Callback<List<SearchSaved>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        Call<List<SearchSaved>> call = lecetClient.getSearchService().getSearchSaved(token, userId);
        call.enqueue(callback);
    }

    public void getSearchProjectInit(String q, Callback<SearchProject> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        Call<SearchProject> call = lecetClient.getSearchService().getSearchProjectInit(token,q, filter);
        call.enqueue(callback);
    }

    public void getSearchProject(String q, Callback<List<Project>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        Call<List<Project>> call = lecetClient.getSearchService().getSearchProject(token,q, filter);
        call.enqueue(callback);
    }
    public void getProjectDetail(long pId, Callback<Project> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        Call<Project> call = lecetClient.getSearchService().getProjectDetail(token, pId);
        call.enqueue(callback);
    }
    public Response<Project> getProjectDetailSync(long pId) {

        String token = sharedPreferenceUtil.getAccessToken();

        Call<Project> call = lecetClient.getSearchService().getProjectDetail(token, pId);
      //  call.enqueue(callback);
        try {
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void getSearchCompany(String q, Callback<List<Company>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        Call<List<Company>> call = lecetClient.getSearchService().getSearchCompany(token,q, filter);
        call.enqueue(callback);
    }
   /* public SearchList copyToRealmTransaction(SearchList searchList) {

        realm.beginTransaction();
        SearchList persistedSearchList = realm.copyToRealmOrUpdate(searchList);
        realm.commitTransaction();

        return persistedSearchList;
    }*/
}

