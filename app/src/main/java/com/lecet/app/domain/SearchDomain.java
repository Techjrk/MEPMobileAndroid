package com.lecet.app.domain;

import android.util.Log;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchCompany;
import com.lecet.app.data.models.SearchContact;
import com.lecet.app.data.models.SearchFilterJurisdictionMain;
import com.lecet.app.data.models.SearchProject;
import com.lecet.app.data.models.SearchResult;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by DomAndTom 2016.
 */

public class SearchDomain {
    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;
    private String projectFilter;

//// TODO: 1/5/17 getting the nested content items from url to be used for jurisdiction, if there's available.
//// TODO: 1/5/17 getting the nested content items from url to be used for type, if there's available.
//// TODO: 1/5/17 getting the nested content items from url to be used for stage, if there's available.

    public String getProjectFilter() {
        return projectFilter;
    }

    public void setProjectFilter(String projectFilter) {
        this.projectFilter = projectFilter;
    }


    public SearchDomain(LecetClient lecetClient, LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {
        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
        initFilter();
    }

    /**
     * To call the retrofit service for the jurisdiction list items to be displayed in the UI layout for jurisdiciton section.
     * @param callback
     */
    public void getJurisdictionList(Callback<List<SearchFilterJurisdictionMain>> callback) {
        Call<List<SearchFilterJurisdictionMain>> call = lecetClient.getSearchService().getSearchFilterJurisdictionItems();
        call.enqueue(callback);
    }
    public void initFilter() {
        //This is the default search filter for Project filter when no custom search filter occurs.
        setProjectFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"]}");
    }

    public void getSearchRecentlyViewed(long userId, Callback<List<SearchResult>> callback) {
        String token = sharedPreferenceUtil.getAccessToken();
        String filter = "{\"include\":[\"project\",\"company\"],\"where\":{\"code\":{\"inq\":[\"VIEW_PROJECT\",\"VIEW_COMPANY\"]}},\"limit\":30,\"order\":\"updatedAt DESC\"}";
        Call<List<SearchResult>> call = lecetClient.getSearchService().getSearchRecentlyViewedWithFilter(token, userId, filter);
//        Call<List<SearchResult>> call = lecetClient.getSearchService().getSearchRecentlyViewed(token, userId);
        call.enqueue(callback);
    }

    public void getSearchSaved(long userId, Callback<List<SearchSaved>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        Call<List<SearchSaved>> call = lecetClient.getSearchService().getSearchSaved(token, userId);
        call.enqueue(callback);
    }


    public void getSearchProjectQuery(String q, Callback<SearchProject> callback) {
        //  String filter="{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"]}";
        String token = sharedPreferenceUtil.getAccessToken();
        Log.d("Project Domain", "Project Domain: " + getProjectFilter());
        Call<SearchProject> call = lecetClient.getSearchService().getSearchProjectQuery(token, q, getProjectFilter());
//        Call<SearchProject> call = lecetClient.getSearchService().getSearchProjectQuery(token,q,filter );
        call.enqueue(callback);
    }

    public void getSearchCompanyQuery(String q, Callback<SearchCompany> callback) {
        String filter = "{}";
        String token = sharedPreferenceUtil.getAccessToken();
        Call<SearchCompany> call = lecetClient.getSearchService().getSearchCompanyQuery(token, q, filter);
        call.enqueue(callback);
    }

    public void getSearchContactQuery(String q, Callback<SearchContact> callback) {
        String filter = "{}";
        String token = sharedPreferenceUtil.getAccessToken();
        Call<SearchContact> call = lecetClient.getSearchService().getSearchContactQuery(token, q, filter);
        call.enqueue(callback);
    }

    public void getProjectDetail(long pId, Callback<Project> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        Call<Project> call = lecetClient.getSearchService().getProjectDetail(token, pId);
        call.enqueue(callback);
    }


}
