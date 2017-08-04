package com.lecet.app.domain;

import android.support.annotation.NonNull;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.County;
import com.lecet.app.data.models.SearchCompany;
import com.lecet.app.data.models.SearchContact;
import com.lecet.app.data.models.SearchFilterJurisdictionMain;
import com.lecet.app.data.models.SearchFilterProjectTypesMain;
import com.lecet.app.data.models.SearchFilterStagesMain;
import com.lecet.app.data.models.SearchProject;
import com.lecet.app.data.models.SearchResult;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.utility.Log;

import java.util.List;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DomAndTom 2016.
 */

public class SearchDomain {
    private final String TAG = "SearchDomain";
    private final LecetClient lecetClient;
    private LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;
    private String projectFilter;
    private String companyFilter = "{\"searchFilter\":{}}";
    private String contactFilter = "{\"include\":[\"company\"],\"searchFilter\":{}}";
    private static String recentToken;
    public static Call<SearchProject> callProjectService;
    public static Call<SearchCompany> callCompanyService;
    public static Call<SearchContact> callContactService;
    private String savedSearchFilter = "{\"include\":[\"company\"],\"searchFilter\":{}}";

    public String getCompanyFilter() {

        return companyFilter;
    }

   /* public void setCompanyFilter(String companyFilter) {
        this.companyFilter = companyFilter;
    }
   */
   public void setCompanyFilter(String xfilter) {
        String sCompanyFilter = "{\"include\":[\"contacts\",{\"projects\":[\"projectStage\"]},{\"projectContacts\":[\"contactType\"]}],\"limit\":28,\"skip\":0, \"searchFilter\":{" +xfilter + "}}"; //#1

       /**
        * Commented below are different scenarios for reviewing the esFilter and searchFilter.
        */
//        String sCompanyFilter = "{\"include\":[\"contacts\",{\"projects\":[\"projectStage\"]},{\"projectContacts\":[\"contactType\"]}],\"limit\":28,\"skip\":0, \"esFilter\":{" +xfilter + "}}";
//       xfilter =  xfilter.replaceAll("companyLocation","projectLocation"); //#6
//       String sCompanyFilter = "{\"include\":[\"contacts\",{\"projects\":[\"projectStage\"]},{\"projectContacts\":[\"contactType\"]}]," +
//               "\"limit\":28,\"skip\":0, \"esFilter\":{" +xfilter + "}}"; //#6
//             xfilter =  xfilter.replaceAll("companyLocation","projectLocation"); //#7
//             String sCompanyFilter = "{\"include\":[\"contacts\",{\"projects\":[\"projectStage\"]},{\"projectContacts\":[\"contactType\"]}]," +
//                     "\"limit\":28,\"skip\":0, \"esFilter\":{" +xfilter + "},\"searchFilter\":{ }}"; //#7
//       String sCompanyFilter = "{\"include\":[\"contacts\",{\"projects\":[\"projectStage\"]},{\"projectContacts\":[\"contactType\"]}]," +
//               "\"limit\":28,\"skip\":0, \"searchFilter\":{" +xfilter + "},\"esFilter\":{ }}"; //#2
//       String sCompanyFilter = "{\"include\":[\"contacts\",{\"projects\":[\"projectStage\"]},{\"projectContacts\":[\"contactType\"]}]," +
//               "\"limit\":28,\"skip\":0, \"searchFilter\":{" +xfilter + "},\"esFilter\":{\" +xfilter + \" }}";
       if (xfilter.equals("{\"searchFilter\":{}}")) sCompanyFilter = "{\"searchFilter\":{}}";
       this.companyFilter = sCompanyFilter;
       //Log.d("companyfilter",xfilter+":companyfilter"+sCompanyFilter);

    }
    public void setCompanyFilter(String searchFilter, String esFilter) {
 //       String sCompanyFilter = "{\"include\":[\"contacts\",{\"projects\":[\"projectStage\"]},{\"projectContacts\":[\"contactType\"]}],\"limit\":28,\"skip\":0, \"searchFilter\":{" +sFilter + "}}"; //#1

        /**
         * Commented below are different scenarios for reviewing the esFilter and searchFilter.
         */
//        String sCompanyFilter = "{\"include\":[\"contacts\",{\"projects\":[\"projectStage\"]},{\"projectContacts\":[\"contactType\"]}],\"limit\":28,\"skip\":0, \"esFilter\":{" +xfilter + "}}";
//       xfilter =  xfilter.replaceAll("companyLocation","projectLocation"); //#6
//       String sCompanyFilter = "{\"include\":[\"contacts\",{\"projects\":[\"projectStage\"]},{\"projectContacts\":[\"contactType\"]}]," +
//               "\"limit\":28,\"skip\":0, \"esFilter\":{" +xfilter + "}}"; //#6
//             xfilter =  xfilter.replaceAll("companyLocation","projectLocation"); //#7
//             String sCompanyFilter = "{\"include\":[\"contacts\",{\"projects\":[\"projectStage\"]},{\"projectContacts\":[\"contactType\"]}]," +
//                     "\"limit\":28,\"skip\":0, \"esFilter\":{" +xfilter + "},\"searchFilter\":{ }}"; //#7
//       String sCompanyFilter = "{\"include\":[\"contacts\",{\"projects\":[\"projectStage\"]},{\"projectContacts\":[\"contactType\"]}]," +
//               "\"limit\":28,\"skip\":0, \"searchFilter\":{" +xfilter + "},\"esFilter\":{ }}"; //#2
       String sCompanyFilter = "{\"include\":[\"contacts\",{\"projects\":[\"projectStage\"]},{\"projectContacts\":[\"contactType\"]}]," +
               "\"limit\":28,\"skip\":0, \"searchFilter\":{" +searchFilter + "},\"esFilter\":{" +esFilter + " }}";
        if (searchFilter.equals("{\"searchFilter\":{}}")) sCompanyFilter = "{\"searchFilter\":{}}";
        this.companyFilter = sCompanyFilter;
        //Log.d("companyfilter","searchFilter & esFilter"+sCompanyFilter);

    }
    public void setCompanyFilter2(String xfilter) {
//        String sCompanyFilter = "{\"include\":[\"contacts\",{\"projects\":[\"projectStage\"]},{\"projectContacts\":[\"contactType\"]}],\"limit\":28,\"skip\":0, \"searchFilter\":{" +xfilter + "}}";
        String sCompanyFilter = "{\"include\":[\"contacts\",{\"projects\":[\"projectStage\"]},{\"projectContacts\":[\"contactType\"]}],\"limit\":28,\"skip\":0," +xfilter + "}";
        if (xfilter.equals("{\"searchFilter\":{}}")) sCompanyFilter = "{\"searchFilter\":{}}";
        this.companyFilter = sCompanyFilter;
        // setCompanyFilter(sCompanyFilter);
        //Log.d("companyfilter2",xfilter+":companyfilter2"+sCompanyFilter);

    }
    public void setCompanyFilterComplete(String sCompanyFilter) {
        this.companyFilter = sCompanyFilter;
    }
    public void initFilter() {
        //This is the default search filter for Project filter when no custom search filter occurs.
        setProjectFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":{},\"skip\":0}");
        //{"include":["primaryProjectType","secondaryProjectTypes","bids","projectStage"],"searchFilter":{}}
        setCompanyFilter("{\"searchFilter\":{}}");
        //***


        setContactFilter("{\"include\":[\"company\"],\"searchFilter\":{}}");
        //  {\"include\":[\"company\"]}
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

    //passing the search filter content only of the project
    public void setProjectFilterOnly(String filter) {
        String pfilter = "{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":{"+filter+"}}";
//        String pfilter = "{\"include\":[{\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}},\"secondaryProjectTypes\",\"projectStage\",{\"bids\"],\"searchFilter\":{"+filter+"}}";

        this.projectFilter = pfilter;
    }

    public SearchDomain(LecetClient lecetClient, LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {
        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        recentToken = sharedPreferenceUtil.getAccessToken();
        this.realm = realm;
        initFilter();
    }
    public SearchDomain(LecetClient lecetClient,  Realm realm) {
        this.lecetClient = lecetClient;
        this.realm = realm;
        initFilter();
    }

    /**
     * To call the retrofit service for the stages list items to be displayed in the UI layout for Stage section.
     *
     * @param callback
     */
    public Call<List<SearchFilterStagesMain>> getStagesList(Callback<List<SearchFilterStagesMain>> callback) {
        //if (SearchViewModel.stageMainList !=null) return;
        String filter = "stages";
        String token = sharedPreferenceUtil.getAccessToken();
        Call<List<SearchFilterStagesMain>> call = lecetClient.getSearchService().getSearchFilterStagesItems(token, filter);
        call.enqueue(callback);

        return call;
    }

    /**
     * Retrieve the list of Project Stages and store them, along with their child Project Stages, in a Realm list.
     */
    public Call<List<SearchFilterStagesMain>> generateRealmStageList(@NonNull final LecetCallback<List<SearchFilterStagesMain>> callback) {

        Call<List<SearchFilterStagesMain>> call = getStagesList(new Callback<List<SearchFilterStagesMain>>() {
            @Override
            public void onResponse(final Call<List<SearchFilterStagesMain>> call, Response<List<SearchFilterStagesMain>> response) {
                Log.d(TAG,"Create List of Project Stages");
                if (response.isSuccessful()) {
                    final List<SearchFilterStagesMain> stageMainList = response.body();
                    Realm realm = Realm.getDefaultInstance();

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                        //    Log.d("SearchDomain:","stageMainList: size: " + stageMainList.size());
                        //    Log.d("SearchDomain:","stageMainList: " + stageMainList);
                            realm.copyToRealmOrUpdate(stageMainList);
                            callback.onSuccess(stageMainList);
                        }
                    });

                } else {
                    callback.onFailure(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SearchFilterStagesMain>> call, Throwable t) {
                callback.onFailure(-999, "Please check your internet connection and try again");
            }
        });

        return call;
    }

    /**
     * To call the retrofit service for the project types list items to be displayed in the UI layout for project types section.
     *
     * @param callback
     */
    public Call<List<SearchFilterProjectTypesMain>> getProjectTypesList(Callback<List<SearchFilterProjectTypesMain>> callback) {
        String filter = "projectTypes";
        String token = sharedPreferenceUtil.getAccessToken();
        Call<List<SearchFilterProjectTypesMain>> call = lecetClient.getSearchService().getSearchFilterProjectTypesItems(token, filter);
        call.enqueue(callback);

        return call;
    }

    /**
     * Retrieve the list of Project Types and store them, along with their child Project Types and grandchild Project Types, in a Realm list.
     */
    public Call<List<SearchFilterProjectTypesMain>> generateRealmProjectTypesList(@NonNull final LecetCallback<List<SearchFilterProjectTypesMain>> callback) {
        Call<List<SearchFilterProjectTypesMain>> call = getProjectTypesList(new Callback<List<SearchFilterProjectTypesMain>>() {
            @Override
            public void onResponse(Call<List<SearchFilterProjectTypesMain>> call, Response<List<SearchFilterProjectTypesMain>> response) {
                Log.d(TAG, "Create List of Project Types");
                if (response.isSuccessful()) {
                    final List<SearchFilterProjectTypesMain> projectTypesMainList = response.body();
                    Realm realm = Realm.getDefaultInstance();

                    realm.executeTransaction(new Realm.Transaction() {

                        @Override
                        public void execute(Realm realm) {
                          //  Log.d("SearchDomain:","projectTypesMainList: size: " + projectTypesMainList.size());
                          //  Log.d("SearchDomain:","projectTypesMainList: " + projectTypesMainList);
                            realm.copyToRealmOrUpdate(projectTypesMainList);

                            callback.onSuccess(projectTypesMainList);
                        }
                    });

                } else {

                    callback.onFailure(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SearchFilterProjectTypesMain>> call, Throwable t) {

                callback.onFailure(-999, "Please check your internet connection and try again");
            }
        });

        return call;
    }


    /**
     * To call the retrofit service for the jurisdiction list items to be displayed in the UI layout for jurisdiciton section.
     *
     * @param callback
     */
    public Call<List<SearchFilterJurisdictionMain>> getJurisdictionList(Callback<List<SearchFilterJurisdictionMain>> callback) {
        //if (SearchViewModel.jurisdictionMainList !=null) return;
        Call<List<SearchFilterJurisdictionMain>> call = lecetClient.getSearchService().getSearchFilterJurisdictionItems();
        call.enqueue(callback);

        return call;
    }

    /**
     * Retrieve the list of Jurisdictions and store them, along with their children and grandchild object, in a Realm list.
     */
    public Call<List<SearchFilterJurisdictionMain>> generateRealmJurisdictionList(@NonNull final LecetCallback<List<SearchFilterJurisdictionMain>> callback) {

        Call<List<SearchFilterJurisdictionMain>> call = getJurisdictionList(new Callback<List<SearchFilterJurisdictionMain>>() {
            @Override
            public void onResponse(Call<List<SearchFilterJurisdictionMain>> call, Response<List<SearchFilterJurisdictionMain>> response) {
               // Log.d(TAG, "Create list of Jurisdictions");
                if (response.isSuccessful()) {
                    final List<SearchFilterJurisdictionMain> jurisdictionMainList = response.body();

                    Realm realm = Realm.getDefaultInstance();

                    realm.executeTransaction(new Realm.Transaction() {

                        @Override
                        public void execute(Realm realm) {
                          //  Log.d("SearchDomain:","jurisdictionMainList: size: " + jurisdictionMainList.size());
                          //  Log.d("SearchDomain:","jurisdictionMainList: " + jurisdictionMainList);
                            realm.copyToRealmOrUpdate(jurisdictionMainList);
                            callback.onSuccess(jurisdictionMainList);
                        }
                    });

                } else {

                    callback.onFailure(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SearchFilterJurisdictionMain>> call, Throwable t) {
                callback.onFailure(-999, "Please check your internet connection and try again");
            }
        });

        return call;
    }

    /**
     * To call the retrofit service for the server-stored list of Counties and related data
     *
     * @param callback
     */
    public Call<List<County>> getCountyList(Callback<List<County>> callback) {
        String token = sharedPreferenceUtil.getAccessToken();
        Call<List<County>> call = lecetClient.getSearchService().getCounties(token);
        call.enqueue(callback);

        return call;
    }

    /**
     * Retrieve the list of Counties and store them
     */
    public Call<List<County>> generateRealmCountyList(@NonNull final LecetCallback<List<County>> callback) {

        Call<List<County>> call = getCountyList(new Callback<List<County>>() {
            @Override
            public void onResponse(final Call<List<County>> call, Response<List<County>> response) {
                Log.d(TAG,"Create List of Counties");
                if (response.isSuccessful()) {
                    final List<County> counties = response.body();
                    Realm realm = Realm.getDefaultInstance();

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Log.d("SearchDomain:","counties: size: " + counties.size());
                            Log.d("SearchDomain:","counties: " + counties);
                            realm.copyToRealmOrUpdate(counties);
                            callback.onSuccess(counties);
                        }
                    });

                } else {
                    callback.onFailure(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<List<County>> call, Throwable t) {
                callback.onFailure(-999, "Please check your internet connection and try again");
            }
        });

        return call;
    }

    public void getSearchRecentlyViewed(long userId, Callback<List<SearchResult>> callback) {
        String token = sharedPreferenceUtil.getAccessToken();
        String filter = "{\"include\":[\"project\",\"company\"],\"where\":{\"code\":{\"inq\":[\"VIEW_PROJECT\",\"VIEW_COMPANY\"]}},\"limit\":10,\"order\":\"updatedAt DESC\"}";
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
//        Call<SearchProject> call = lecetClient.getSearchService().getSearchProjectQuery(token, q, getProjectFilter());
  //      if (q !=null && q.equals("")) q="{}";
        callProjectService = lecetClient.getSearchService().getSearchProjectQuery(token, q, getProjectFilter());
        callProjectService.enqueue(callback);
    }

    public void getSearchCompanyQuery(String q, Callback<SearchCompany> callback) {
        //   String filter = "{}";
//        if (q !=null && q.equals("")) q="{}";
        String token = sharedPreferenceUtil.getAccessToken();
//        Call<SearchCompany> call = lecetClient.getSearchService().getSearchCompanyQuery(token, q, getCompanyFilter());
        callCompanyService = lecetClient.getSearchService().getSearchCompanyQuery(token, q, getCompanyFilter());
        callCompanyService.enqueue(callback);
        Log.d("companyfilter3","companyfilter3"+getCompanyFilter());
        Log.d("query1","query1:"+q+":filter:"+getCompanyFilter());
    }

    public void getSearchContactQuery(String q, Callback<SearchContact> callback) {
        //   String filter = "{}";
    //    if (q !=null && q.equals("")) q="{}";
        String token = sharedPreferenceUtil.getAccessToken();
//        Call<SearchContact> call = lecetClient.getSearchService().getSearchContactQuery(token, q, getContactFilter());
        callContactService = lecetClient.getSearchService().getSearchContactQuery(token, q, getContactFilter());
        callContactService.enqueue(callback);
    }

/*
    public void getProjectDetail(long pId, Callback<Project> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        Call<Project> call = lecetClient.getSearchService().getProjectDetail(token, pId);
        call.enqueue(callback);
    }
*/

    public void saveRecentProject(long projId, Callback<ResponseBody> callback) {
        String bodyContent ="{ \"code\" : \"VIEW_PROJECT\" , \"projectId\" : "+projId+" }";
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),bodyContent);
        Call<ResponseBody> call = lecetClient.getSearchService().saveRecent(recentToken, body);
      //  Log.d("body","body "+bodyContent);
        call.enqueue(callback);
    }

    public void saveRecentCompany(long companyId, Callback<ResponseBody> callback) {
        String bodyContent ="{ \"code\" : \"VIEW_COMPANY\" , \"companyId\" : "+companyId+" }";
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),bodyContent);
        Call<ResponseBody> call = lecetClient.getSearchService().saveRecent(recentToken, body);
        //Log.d("body","body "+bodyContent);
        call.enqueue(callback);
    }

    public void saveCurrentProjectSearch(String title, String query, Callback<ResponseBody> callback) {
        String searchFilter =  getProjectFilter();
        if(searchFilter !=  null && searchFilter.length() > 0) {
            Log.d(TAG, "saveCurrentProjectSearch: title: " + title + ", query: " + query + ", searchFilter: " + searchFilter);

            //works String bodyContent ="{\"title\":\""+title+"\",\"modelName\":\"project\",\"query\":\"apartment\",\"filter\":{\"include\":[{\"bids\":[\"company\",{\"project\":[\"projectStage\"]}]},{\"contacts\":[\"company\",\"contact\",\"contactType\"]},\"csiCodes\",{\"primaryProjectType\":[\"projectCategory\"]},\"projectStage\",\"secondaryProjectTypes\",\"specAlerts\",\"userNotes\",\"workTypes\"],\"jurisdiction\":true,\"limit\":28,\"searchFilter\":{\"projectValue\":{\"min\":333,\"max\":9999999},\"projectLocation\":{\"city\":\"Brookfield\"}},\"skip\":0}}";
            String bodyContent ="{\"title\":\""+title+"\",\"modelName\":\"project\",\"query\":\"" + query + "\",\"filter\":" + searchFilter + "}";
            //String bodyContent ="{ \"title\" : " + title + " , \"modelName\" : " + "project" + " }";
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),bodyContent);

            //RequestBody body = RequestBody.create(MediaType.parse("text/plain"),bodyContent);
            //"application/json; charset=utf-8"
            Call<ResponseBody> call = lecetClient.getSearchService().saveProjectSearch(recentToken, body);
            Log.d(TAG,"saveCurrentProjectSearch: bodyContent: " + bodyContent);
            call.enqueue(callback);

        }
    }
    public void saveCurrentCompanySearch(String title, String query, Callback<ResponseBody> callback) {
        String searchFilter =  getCompanyFilter();
        if(searchFilter !=  null && searchFilter.length() > 0) {
            Log.d(TAG, "saveCurrentCompanySearch: title: " + title + ", query: " + query + ", searchFilter: " + searchFilter);

            //works String bodyContent ="{\"title\":\""+title+"\",\"modelName\":\"project\",\"query\":\"apartment\",\"filter\":{\"include\":[{\"bids\":[\"company\",{\"project\":[\"projectStage\"]}]},{\"contacts\":[\"company\",\"contact\",\"contactType\"]},\"csiCodes\",{\"primaryProjectType\":[\"projectCategory\"]},\"projectStage\",\"secondaryProjectTypes\",\"specAlerts\",\"userNotes\",\"workTypes\"],\"jurisdiction\":true,\"limit\":28,\"searchFilter\":{\"projectValue\":{\"min\":333,\"max\":9999999},\"projectLocation\":{\"city\":\"Brookfield\"}},\"skip\":0}}";
            String bodyContent ="{\"title\":\""+title+"\",\"modelName\":\"company\",\"query\":\"" + query + "\",\"filter\":" + searchFilter + "}";
            //String bodyContent ="{ \"title\" : " + title + " , \"modelName\" : " + "project" + " }";
            RequestBody body = RequestBody.create(MediaType.parse("text/plain"),bodyContent);
            Call<ResponseBody> call = lecetClient.getSearchService().saveProjectSearch(recentToken, body);
            Log.d(TAG,"saveCurrentCompanySearch: bodyContent: " + bodyContent);
            call.enqueue(callback);

        }
    }
}
