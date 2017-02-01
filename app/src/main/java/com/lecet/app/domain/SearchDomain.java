package com.lecet.app.domain;

import android.util.Log;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectStage;
import com.lecet.app.data.models.ProjectType;
import com.lecet.app.data.models.SearchCompany;
import com.lecet.app.data.models.SearchContact;
import com.lecet.app.data.models.SearchFilter;
import com.lecet.app.data.models.SearchFilterJurisdictionDistrictCouncil;
import com.lecet.app.data.models.SearchFilterJurisdictionMain;
import com.lecet.app.data.models.SearchFilterProjectTypesMain;
import com.lecet.app.data.models.SearchFilterProjectTypesProjectCategory;
import com.lecet.app.data.models.SearchFilterStage;
import com.lecet.app.data.models.SearchFilterStagesMain;
import com.lecet.app.data.models.SearchProject;
import com.lecet.app.data.models.SearchResult;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DomAndTom 2016.
 */

public class SearchDomain {
    private final String TAG = "SearchDomain";
    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;
    private String projectFilter;
    private String companyFilter="{\"searchFilter\":{}}";
    private String contactFilter="{\"searchFilter\":{}}";

    public String getCompanyFilter() {
        return companyFilter;
    }

    public void setCompanyFilter(String companyFilter) {
        this.companyFilter = companyFilter;
    }

    public String getContactFilter() {
        return contactFilter;
    }

    public void setContactFilter(String contactFilter) {
        this.contactFilter = contactFilter;
    }

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
     * To call the retrofit service for the stages list items to be displayed in the UI layout for Stage section.
     * @param callback
     */
    public void getStagesList(Callback<List<SearchFilterStagesMain>> callback) {
        //if (SearchViewModel.stageMainList !=null) return;
        String filter = "stages";
        String token = sharedPreferenceUtil.getAccessToken();
        Call<List<SearchFilterStagesMain>> call = lecetClient.getSearchService().getSearchFilterStagesItems(token, filter);
        call.enqueue(callback);
    }

    /**
     * Retrieve the list of Project Stages and store them, along with their child Project Stages, in a Realm list.
     */
    public void generateRealmStageList() {
        getStagesList(new Callback<List<SearchFilterStagesMain>>() {
            @Override
            public void onResponse(Call<List<SearchFilterStagesMain>> call, Response<List<SearchFilterStagesMain>> response) {
                Log.d(TAG,"Create List of Project Stages");
                if (response.isSuccessful()) {
                    final List<SearchFilterStagesMain> stageMainList = response.body();
                    Realm realm = Realm.getDefaultInstance();

                    realm.executeTransaction(new Realm.Transaction() {

                        @Override
                        public void execute(Realm realm) {
                            Log.d("SearchDomain:","stageMainList: size: " + stageMainList.size());
                            Log.d("SearchDomain:","stageMainList: " + stageMainList);
                            realm.copyToRealmOrUpdate(stageMainList);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<SearchFilterStagesMain>> call, Throwable t) {
                Log.e("onFailure: ", "Network is busy. Pls. try again. ");  //TODO - handle error
            }
        });
    }

    /**
     * To call the retrofit service for the project types list items to be displayed in the UI layout for project types section.
     * @param callback
     */
    public void getProjectTypesList(Callback<List<SearchFilterProjectTypesMain>> callback) {
        //if (SearchViewModel.typeMainList !=null) return;
        String filter = "projectTypes";
        String token = sharedPreferenceUtil.getAccessToken();
        Call<List<SearchFilterProjectTypesMain>> call = lecetClient.getSearchService().getSearchFilterProjectTypesItems(token, filter);
        call.enqueue(callback);
    }

    /**
     * Retrieve the list of Project Types and store them, along with their child Project Types and grandchild Project Types, in a Realm list.
     */
    public void generateRealmProjectTypesList() {
        getProjectTypesList(new Callback<List<SearchFilterProjectTypesMain>>() {
            @Override
            public void onResponse(Call<List<SearchFilterProjectTypesMain>> call, Response<List<SearchFilterProjectTypesMain>> response) {
                Log.d(TAG,"Create List of Project Types");
                if (response.isSuccessful()) {
                    final List<SearchFilterProjectTypesMain> projectTypesMainList = response.body();
                    Realm realm = Realm.getDefaultInstance();

                    realm.executeTransaction(new Realm.Transaction() {

                        @Override
                        public void execute(Realm realm) {
                            Log.d("SearchDomain:","projectTypesMainList: size: " + projectTypesMainList.size());
                            Log.d("SearchDomain:","projectTypesMainList: " + projectTypesMainList);
                            realm.copyToRealmOrUpdate(projectTypesMainList);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<SearchFilterProjectTypesMain>> call, Throwable t) {
                Log.e("onFailure: ", "Network is busy. Pls. try again. ");  //TODO - handle error
            }
        });
    }



    /**
     * To call the retrofit service for the jurisdiction list items to be displayed in the UI layout for jurisdiciton section.
     * @param callback
     */
    public void getJurisdictionList(Callback<List<SearchFilterJurisdictionMain>> callback) {
        //if (SearchViewModel.jurisdictionMainList !=null) return;
        Call<List<SearchFilterJurisdictionMain>> call = lecetClient.getSearchService().getSearchFilterJurisdictionItems();
        call.enqueue(callback);
    }

    /**
     * Retrieve the list of Jurisdictions and store them, along with their children and grandchild object, in a Realm list.
     */
    public void generateRealmJurisdictionList() {
        getJurisdictionList(new Callback<List<SearchFilterJurisdictionMain>>() {
            @Override
            public void onResponse(Call<List<SearchFilterJurisdictionMain>> call, Response<List<SearchFilterJurisdictionMain>> response) {
                Log.d(TAG,"Create list of Jurisdictions");
                if (response.isSuccessful()) {
                    final List<SearchFilterJurisdictionMain> jurisdictionMainList = response.body();

                    Realm realm = Realm.getDefaultInstance();

                    realm.executeTransaction(new Realm.Transaction() {

                        @Override
                        public void execute(Realm realm) {
                            Log.d("SearchDomain:","jurisdictionMainList: size: " + jurisdictionMainList.size());
                            Log.d("SearchDomain:","jurisdictionMainList: " + jurisdictionMainList);
                            realm.copyToRealmOrUpdate(jurisdictionMainList);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<SearchFilterJurisdictionMain>> call, Throwable t) {
                Log.e("onFailure: ", "Network is busy. Pls. try again. ");  //TODO - handle error
            }
        });
    }

    public void initFilter() {
        //This is the default search filter for Project filter when no custom search filter occurs.
        setProjectFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":{}}");
        //{"include":["primaryProjectType","secondaryProjectTypes","bids","projectStage"],"searchFilter":{}}
        setCompanyFilter("{\"searchFilter\":{}}");
        setContactFilter("{\"searchFilter\":{}}");
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
     //   String filter = "{}";
        String token = sharedPreferenceUtil.getAccessToken();
        Call<SearchCompany> call = lecetClient.getSearchService().getSearchCompanyQuery(token, q, getCompanyFilter());
        call.enqueue(callback);
    }

    public void getSearchContactQuery(String q, Callback<SearchContact> callback) {
     //   String filter = "{}";
        String token = sharedPreferenceUtil.getAccessToken();
        Call<SearchContact> call = lecetClient.getSearchService().getSearchContactQuery(token, q, getContactFilter());
        call.enqueue(callback);
    }

    public void getProjectDetail(long pId, Callback<Project> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        Call<Project> call = lecetClient.getSearchService().getProjectDetail(token, pId);
        call.enqueue(callback);
    }



}
