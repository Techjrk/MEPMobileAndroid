package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.Search1RecyclerViewAdapter;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchResult;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.SearchDomain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DomandTom 2016.
 */

public class SearchViewModel extends BaseObservable {

    public final int SEARCH_ADAPTER_TYPE_PROJECTS = 1;
    public final int SEARCH_ADAPTER_TYPE_COMPANIES = 2;

    private final AppCompatActivity activity;
    private final SearchDomain searchDomain;
    private final String mapsApiKey = "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU";
    private static final String TAG = "SearchViewModel";
    private Project project;
    private String query;
    private String customString = ""; //TODO: Just for personal testing/checking of the response result of the MSE APIs call using customString variable (customString - String Binding :).

    private List<SearchResult> adapterData = Collections.EMPTY_LIST;
    private List<SearchSaved> adapterDataProjectSearchSaved = Collections.EMPTY_LIST;
    private List<SearchSaved> adapterDataCompanySearchSaved = Collections.EMPTY_LIST;
    private Search1RecyclerViewAdapter searchAdapter;
    private Search1RecyclerViewAdapter searchAdapterProject;
    private Search1RecyclerViewAdapter searchAdapterCompany;


/*    public SearchViewModel(Project project, String mapsApiKey) {
        this.project = project;
        this.mapsApiKey = mapsApiKey;
    }*/

    /**
     * Constructor without input query - For RecentlyViewed and SavedSearch API
     */
    public SearchViewModel(AppCompatActivity activity, SearchDomain sd) {
        this.activity = activity;
        this.searchDomain = sd;
        //TODO: 1 ***TESTING THE SEARCH FUNCTIONALITY FOR THIS CONSTRUCTOR ***
        //    getUserSavedSearches(LecetSharedPreferenceUtil.getInstance(activity.getApplication()).getId());   //testing for getting savedsearches result data
        initializeAdapter();
        getUserRecentlyViewed(LecetSharedPreferenceUtil.getInstance(activity.getApplication()).getId());   //testing for getting savedsearches result data

        initializeAdapterSavedProject();
        initializeAdapterSavedCompany();
        getUserSavedSearches(LecetSharedPreferenceUtil.getInstance(activity.getApplication()).getId());
    }

    /**
     * Get the list of recently viewed by the user
     */
    public void getUserRecentlyViewed(long userId) {
        //Using the searchDomain to call the method to start retrofitting...
        searchDomain.getSearchRecentlyViewed(userId, new Callback<List<SearchResult>>() {
            @Override
            public void onResponse(Call<List<SearchResult>> call, Response<List<SearchResult>> response) {
                List<SearchResult> slist;
                if (response.isSuccessful()) {
                    slist = response.body();
                    adapterData.clear();

                    //                   customString += ("getUserRecentlyViewed\r\n");
                    int ctr = 0;
                    for (SearchResult s : slist) {
                        //TODO: testing for getting the result of  RecentlyViewed search

                        try {
                            //                           customString += ("\r\n" + ctr + " code1:" + s.getCode() + " id:" + s.getId() + " pid:" + s.getProjectId() + " cid:" + s.getCompanyId() + " createdAt:" + s.getCreatedAt() +
                            //                                   " Lat: " + s.getProject().getGeocode().getLat() + " long: " + s.getProject().getGeocode().getLng() + "\r\n");
                            ctr++;
                            if (ctr <= 10 && s.getProject() != null) adapterData.add(s);
                        } catch (Exception e) {
                        }
                    }

                    //                   notifyPropertyChanged(BR.customString);
                    searchAdapter.notifyDataSetChanged();

                } else {
                    errorDisplayMsg(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SearchResult>> call, Throwable t) {
                errorDisplayMsg(t.getLocalizedMessage());
            }
        });
    }

    /**
     * Get the list of recently viewed by the user
     */
    public void getUserSavedSearches(long userId) {
        //Using the searchDomain to call the method to start retrofitting...
        searchDomain.getSearchSaved(userId, new Callback<List<SearchSaved>>() {
            @Override
            public void onResponse(Call<List<SearchSaved>> call, Response<List<SearchSaved>> response) {
                List<SearchSaved> slist;
                customString = "";
                if (response.isSuccessful()) {
                    slist = response.body();
                    if (adapterDataProjectSearchSaved == null) new ArrayList<SearchSaved>();
                    adapterDataProjectSearchSaved.clear();
                    adapterDataCompanySearchSaved.clear();
                    int ctr = 0, ctrc = 0;
                    for (SearchSaved s : slist) {
                        //TODO: testing for getting the result of  UserSaved search
                        //ctr++;
                        if (s != null) {
                            if (s.getModelName().equalsIgnoreCase("Project")) {
                                adapterDataProjectSearchSaved.add(s);
                           /*     customString += "\r\n"+((SearchSaved)adapterDataProjectSearchSaved.get(ctr)).getTitle();
                                ctr++;*/
                            } else if (s.getModelName().equalsIgnoreCase("Company")) {
                                adapterDataCompanySearchSaved.add(s);
                              /*  customString += "\r\n"+((SearchSaved)adapterDataCompanySearchSaved.get(ctrc)).getTitle();
                                ctrc++;*/
                            }

                        }
                        //customString += ("\r\n" + ctr + " title:" + s.getTitle() + " modelName:" + s.getModelName() + " id:" + s.getId() + " userid:" + s.getUserId() + " query:" + s.getQuery() + "\r\n");
                    }
                    notifyPropertyChanged(BR.customString);
                    searchAdapterProject.notifyDataSetChanged();
                    searchAdapterCompany.notifyDataSetChanged();
                } else {
                    errorDisplayMsg(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SearchSaved>> call, Throwable t) {
                errorDisplayMsg(t.getLocalizedMessage());
            }
        });
    }

    /**
     * Search the project based on query string
     * TODO: UNUSED
     */
    public void getProject(String q) {
        //Using the searchDomain to call the method to start retrofitting...
        searchDomain.getSearchProject(q, new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                List<Project> slist;
                if (response.isSuccessful()) {
                    slist = response.body();
                    //searchDomain.copyToRealmTransaction(slist);
                    int ctr = 0;
                    for (Project ps : slist) {
                        ctr++;
                        //   Log.d("Search ListCode: "+ctr,"search code:"+s.getCode()+ " id:"+s.getId()+" pid:"+s.getProjectId()+" cid:"+s.getCompanyId());
                        customString += ("\r\n" + ctr + " id:" + ps.getId() + " Title:" + ps.getTitle() + " Address:" + ps.getAddress1() + " GeoCode:" + ps.getGeocode() + " BidDate:" + ps.getBidDate() + "\r\n");
                    }
                    notifyPropertyChanged(BR.customString);
                } else {
                    errorDisplayMsg(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                errorDisplayMsg(t.getLocalizedMessage());
            }
        });
    }

    /**
     * Search the company based on query string
     * TODO: UNUSED
     */
    public void getCompany(String q) {
        //Using the searchDomain to call the method to start retrofitting...
        searchDomain.getSearchCompany(q, new Callback<List<Company>>() {
            @Override
            public void onResponse(Call<List<Company>> call, Response<List<Company>> response) {
                List<Company> slist;
                if (response.isSuccessful()) {
                    slist = response.body();
                    int ctr = 0;
                    for (Company cs : slist) {
                        ctr++;
                        customString += ("\r\n" + ctr + " id:" + cs.getId() + " Name:" + cs.getName() + " Address:" + cs.getAddress1() + " City:" + cs.getCity() + " Country:" + cs.getCountry() + "\r\n");
                    }
                    notifyPropertyChanged(BR.customString);
                    searchAdapter.notifyDataSetChanged();
                } else {
                    errorDisplayMsg(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Company>> call, Throwable t) {
                errorDisplayMsg(t.getLocalizedMessage());
            }
        });
    }

    /**
     * Display error message
     */
    public void errorDisplayMsg(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.error_network_title) + "\r\n" + message + "\r\n");
        Log.e("Error:", "Error " + message);
        builder.setMessage(activity.getString(R.string.error_network_message));
        builder.setNegativeButton(activity.getString(R.string.ok), null);
        Log.e("onFailure", "onFailure: " + message);
        builder.show();
    }

    /**
     * Setting up the recylerview recent adapter
     */
    private void initializeAdapter() {

        adapterData = new ArrayList<SearchResult>();
//TODO: List testing
//        SearchResult sr1 = new SearchResult();
//        sr1.setCode("code123");
//        SearchResult sr2 = new SearchResult();
        // adapterData.add(sr1);
        // adapterData.add(sr2);
//** 0: For recent adapter
        RecyclerView recyclerView = getProjectRecyclerView(R.id.recycler_view_recent);
        setupRecyclerView(recyclerView, LinearLayoutManager.HORIZONTAL);
        searchAdapter = new Search1RecyclerViewAdapter(adapterData);
        //searchAdapterProject.setAdapterType(0);
        recyclerView.setAdapter(searchAdapter);
    }

    /**
     * Initialize Saved Projects Adapter
     */
    private void initializeAdapterSavedProject() {
        adapterDataProjectSearchSaved = new ArrayList<SearchSaved>();
        RecyclerView recyclerViewProject = getProjectRecyclerView(R.id.recycler_view_project);
        setupRecyclerView(recyclerViewProject, LinearLayoutManager.VERTICAL);
        searchAdapterProject = new Search1RecyclerViewAdapter(adapterDataProjectSearchSaved);
        searchAdapterProject.setAdapterType(SEARCH_ADAPTER_TYPE_PROJECTS);
        recyclerViewProject.setAdapter(searchAdapterProject);
    }

    /**
     * Initialize Saved Company Adapter
     */
    private void initializeAdapterSavedCompany() {
        adapterDataCompanySearchSaved = new ArrayList<SearchSaved>();
        RecyclerView recyclerViewCompany = getProjectRecyclerView(R.id.recycler_view_company);
        setupRecyclerView(recyclerViewCompany, LinearLayoutManager.VERTICAL);
        searchAdapterCompany = new Search1RecyclerViewAdapter(adapterDataCompanySearchSaved);
        //  searchAdapterProject.setData(adapterDataCompanySearchSaved);
        searchAdapterCompany.setAdapterType(SEARCH_ADAPTER_TYPE_COMPANIES);
        recyclerViewCompany.setAdapter(searchAdapterCompany);
    }

    private void setupRecyclerView(RecyclerView recyclerView, int orientation) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, orientation, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private RecyclerView getProjectRecyclerView(@IdRes int recyclerView) {

        return (RecyclerView) activity.findViewById(recyclerView);
    }

    public String getMapUrl(Project project) {

        if (project.getGeocode() == null) return null;

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=400x400&" +
                        "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
    }



    /////////////////////////////////////
    // BINDABLE

    @Bindable
    public String getQuery() {
        return query;
    }

    @Bindable
    public void setQuery(String query) {
        this.query = query;
    }

    @Bindable
    public String getCustomString() {
        return customString;
    }

    public void setCustomString(String customString) {
        this.customString = customString;
    }

}
