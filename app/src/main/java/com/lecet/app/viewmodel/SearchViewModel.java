package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.SearchRecyclerViewAdapter;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchProject;
import com.lecet.app.data.models.SearchResult;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ListItemSearchQuerySummaryBinding;
import com.lecet.app.domain.SearchDomain;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DomandTom 2016.
 */

public class SearchViewModel extends BaseObservable {

    private static final String TAG = "SearchViewModel";

    public static final int SEARCH_ADAPTER_TYPE_RECENT = 0;
    public static final int SEARCH_ADAPTER_TYPE_PROJECTS = 1;
    public static final int SEARCH_ADAPTER_TYPE_COMPANIES = 2;
    public static final int SEARCH_ADAPTER_TYPE_PQS = 3;   //PQS - Project Query Summary

    private AppCompatActivity activity;
    private final SearchDomain searchDomain;
    private List<SearchResult> adapterDataRecentlyViewed;
    private List<SearchSaved> adapterDataProjectSearchSaved;
    private List<SearchSaved> adapterDataCompanySearchSaved;
    private SearchRecyclerViewAdapter searchAdapterRecentlyViewed;
    private SearchRecyclerViewAdapter searchAdapterProject;
    private SearchRecyclerViewAdapter searchAdapterCompany;
    private boolean isMSE2SectionVisible = false;
    private final int CONTENT_MAX_SIZE = 4;

    //For MSE 2.0
    private static EditText searchfield;
    private List<Project> adapterDataProjectQuerySummary;
    private SearchRecyclerViewAdapter searchAdapterPQS;  //PQS - Project Query Summary
    private String queryProjectTitle;
    private String queryCompanyTitle;
    private String queryContactTitle;
    private int queryProjTotal;
    private int queryCompanyTotal;
    private int queryContactTotal;

    /**
     * Constructor without input query - For RecentlyViewed and SavedSearch API
     */
    public SearchViewModel(AppCompatActivity activity, SearchDomain sd) {
        this.activity = activity;
        this.searchDomain = sd;
        //TODO: 1 ***TESTING THE SEARCH FUNCTIONALITY FOR THIS CONSTRUCTOR ***

        // Init the Recently Viewed Items adapter and fetch its data
        initializeAdapterRecentlyViewed();
        getUserRecentlyViewed(LecetSharedPreferenceUtil.getInstance(activity.getApplication()).getId());

        // Init the Saved Search adapters (Projects and Companies) and fetch date for each of them
        initializeAdapterSavedProject();
        initializeAdapterSavedCompany();
        getUserSavedSearches(LecetSharedPreferenceUtil.getInstance(activity.getApplication()).getId());

        //Init the search query
        initializeAdapterProjectQuerySummary();
        initQuery();
    }

    public void initQuery() {
        searchfield = (EditText) activity.findViewById(R.id.search_edit_text);
/*        viewProjQueryString = (TextView) activity.findViewById(R.id.queryproject_title);
        viewCompanyQueryString = (TextView) activity.findViewById(R.id.querycompany_title);
        viewContactQueryString = (TextView) activity.findViewById(R.id.querycontact_title);*/

        searchfield.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) updateViewQuery(editable.toString());
            }
        });
    }

    public void updateViewQuery(String query) {

        if (query == null || query.trim().equals("")) {
            setIsMSE2SectionVisible(false);
            return;
        }
        setIsMSE2SectionVisible(true);

        queryProjectTitle = query + " in Projects";
        queryCompanyTitle = query + " in Companies";
        queryContactTitle = query + " in Contacts";

        notifyPropertyChanged(BR.queryProjectTitle);
        notifyPropertyChanged(BR.queryCompanyTitle);
        notifyPropertyChanged(BR.queryContactTitle);

        query = searchfield.getText().toString().trim();
        /** For Project query total view
         */
        //      viewProjQueryTotal.setText("? Projects ");
        getQueryProjTotal();
        getProjectQuery(query);

        /**
         * For Company query total view
         */
//        viewCompanyQueryTotal.setText("? Companies ");
        getQueryCompanyTotal();
        getCompanyQuery(query);

        /**
         * For Contact query total view
         */
//        viewContactQueryTotal.setText("? Contacts ");
        getQueryContactTotal();
        getContactQuery(query);

    }

    /**
    * Get the list of Project in Query Search Summary
    */
    public void getProjectQueryListSummary(SearchProject sp) {
        RealmList<Project> slist = sp.getResults();

        adapterDataProjectQuerySummary.clear();
        int ctr = 0;
        for (Project s : slist) {
            try {

                if (s != null) {
                    if (ctr < CONTENT_MAX_SIZE) adapterDataProjectQuerySummary.add(s);
                    ctr++;

                }
            } catch (Exception e) {
                //TODO - handle exception
                Log.w("No project", "No project in the list");
            }
        }

        searchAdapterPQS.notifyDataSetChanged();
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
                    adapterDataRecentlyViewed.clear();
                    int ctr = 0;
                    for (SearchResult s : slist) {
                        try {
                            ctr++;
                            if (s.getProject() != null) {
                                adapterDataRecentlyViewed.add(s);
                            }
                        } catch (Exception e) {
                            //TODO - handle exception
                            Log.e("tUserRecentlyViewed","Exception in getUserRecentlyViewed"+e.getMessage());
                        }
                    }


                    searchAdapterRecentlyViewed.notifyDataSetChanged();

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
     * Get the list of saved user searches, which returns Projects and/or Companies
     */
    public void getUserSavedSearches(long userId) {
        //Using the searchDomain to call the method to start retrofitting...
        searchDomain.getSearchSaved(userId, new Callback<List<SearchSaved>>() {
            @Override
            public void onResponse(Call<List<SearchSaved>> call, Response<List<SearchSaved>> response) {
                List<SearchSaved> slist;
                if (response.isSuccessful()) {
                    slist = response.body();
                    if (adapterDataProjectSearchSaved == null) new ArrayList<SearchSaved>();
                    adapterDataProjectSearchSaved.clear();
                    adapterDataCompanySearchSaved.clear();
                    int projectcounter = 0, companycounter = 0;  //TODO - rename variables for more clarity

                    for (SearchSaved s : slist) {
                        Log.d(TAG, "getUserSavedSearches: onResponse: " + s);
                        //TODO: testing for getting the result of  UserSaved search
                        //projectcounter++;
                        if (s != null) {
                            if (s.getModelName().equalsIgnoreCase("Project")) {
                                adapterDataProjectSearchSaved.add(s);

                                projectcounter++;
                            } else if (s.getModelName().equalsIgnoreCase("Company")) {
                                adapterDataCompanySearchSaved.add(s);

                                companycounter++;
                            }
                        }

                    }
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

    public void getProjectQuery(String q) {
        searchDomain.getSearchProjectQuery(q, new Callback<SearchProject>() {
            @Override
            public void onResponse(Call<SearchProject> call, Response<SearchProject> response) {
                if (response.isSuccessful()) {
                    SearchProject searchproject = response.body();
                    queryProjTotal = searchproject.getTotal();
                    notifyPropertyChanged(BR.queryProjTotal);
                    getProjectQueryListSummary(searchproject);
                    //  Log.d("TotalProject", "Total: " + queryProjTotal);
                } else {
                    errorDisplayMsg("Unsuccessful Query. " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchProject> call, Throwable t) {
                errorDisplayMsg("Network is busy. Pls. try again. ");
            }
        });
    }

    public void getCompanyQuery(String q) {
        searchDomain.getSearchCompanyQuery(q, new Callback<SearchProject>() {
            @Override
            public void onResponse(Call<SearchProject> call, Response<SearchProject> response) {
                if (response.isSuccessful()) {
                    SearchProject searchproject = response.body();
                    queryCompanyTotal = searchproject.getTotal();
                    notifyPropertyChanged(BR.queryCompanyTotal);
                    //     Log.d("TotalProject", "Total: " + queryCompanyTotal);
                } else {
                    errorDisplayMsg("Unsuccessful Query. " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchProject> call, Throwable t) {
                errorDisplayMsg("Network is busy. Pls. try again. ");
            }
        });
    }

    public void getContactQuery(String q) {
        searchDomain.getSearchContactQuery(q, new Callback<SearchProject>() {
            @Override
            public void onResponse(Call<SearchProject> call, Response<SearchProject> response) {
                if (response.isSuccessful()) {
                    SearchProject searchproject = response.body();
                    queryContactTotal = searchproject.getTotal();
                    notifyPropertyChanged(BR.queryContactTotal);
                    //    Log.d("TotalProject", "Total: " + queryContactTotal);
                } else {
                    errorDisplayMsg("Unsuccessful Query. " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchProject> call, Throwable t) {
                errorDisplayMsg("Network is busy. Pls. try again. ");
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
        builder.setMessage(message);
//        builder.setMessage(activity.getString(R.string.error_network_message));
        builder.setNegativeButton(activity.getString(R.string.ok), null);
        Log.e("onFailure", "onFailure: " + message);
        builder.show();
    }

    /**
     * Initialize Project Items Adapter Search Query
     */
    private void initializeAdapterProjectQuerySummary() {
        adapterDataProjectQuerySummary = new ArrayList<Project>();
        RecyclerView recyclerViewPQS = getRecyclerViewById(R.id.recycler_view_project_query_summary);
        setupRecyclerView(recyclerViewPQS, LinearLayoutManager.VERTICAL);
        searchAdapterPQS = new SearchRecyclerViewAdapter(this, adapterDataProjectQuerySummary);
        searchAdapterPQS.setAdapterType(SEARCH_ADAPTER_TYPE_PQS);
        recyclerViewPQS.setAdapter(searchAdapterPQS);
    }

    /**
     * Initialize Recent Items Adapter
     */
    private void initializeAdapterRecentlyViewed() {

        adapterDataRecentlyViewed = new ArrayList<SearchResult>();
        RecyclerView recyclerViewRecentlyViewed = getRecyclerViewById(R.id.recycler_view_recent);
        setupRecyclerView(recyclerViewRecentlyViewed, LinearLayoutManager.HORIZONTAL);
        searchAdapterRecentlyViewed = new SearchRecyclerViewAdapter(this, adapterDataRecentlyViewed);
        searchAdapterRecentlyViewed.setAdapterType(SEARCH_ADAPTER_TYPE_RECENT);
        recyclerViewRecentlyViewed.setAdapter(searchAdapterRecentlyViewed);
    }

    /**
     * Initialize Saved Projects Adapter
     */
    private void initializeAdapterSavedProject() {
        adapterDataProjectSearchSaved = new ArrayList<SearchSaved>();
        RecyclerView recyclerViewProject = getRecyclerViewById(R.id.recycler_view_project);
        setupRecyclerView(recyclerViewProject, LinearLayoutManager.VERTICAL);
        searchAdapterProject = new SearchRecyclerViewAdapter(this, adapterDataProjectSearchSaved);
        searchAdapterProject.setAdapterType(SEARCH_ADAPTER_TYPE_PROJECTS);
        recyclerViewProject.setAdapter(searchAdapterProject);
    }

    /**
     * Initialize Saved Company Adapter
     */
    private void initializeAdapterSavedCompany() {
        adapterDataCompanySearchSaved = new ArrayList<SearchSaved>();
        RecyclerView recyclerViewCompany = getRecyclerViewById(R.id.recycler_view_company);
        setupRecyclerView(recyclerViewCompany, LinearLayoutManager.VERTICAL);
        searchAdapterCompany = new SearchRecyclerViewAdapter(this, adapterDataCompanySearchSaved);
        searchAdapterCompany.setAdapterType(SEARCH_ADAPTER_TYPE_COMPANIES);
        recyclerViewCompany.setAdapter(searchAdapterCompany);
    }

    private void setupRecyclerView(RecyclerView recyclerView, int orientation) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, orientation, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private RecyclerView getRecyclerViewById(@IdRes int recyclerView) {

        return (RecyclerView) activity.findViewById(recyclerView);
    }


    public AppCompatActivity getActivity() {
        return activity;
    }


    ///////////////////////////////////
    // BINDABLE

    @Bindable
    public  boolean getIsMSE2SectionVisible() {
        return isMSE2SectionVisible;
    }

    public  void setIsMSE2SectionVisible(boolean isMSE2SectionVisible) {
        this.isMSE2SectionVisible = isMSE2SectionVisible;
        notifyPropertyChanged(BR.isMSE2SectionVisible);
    }

    @Bindable
    public String getQueryProjectTitle() {
        return queryProjectTitle;
    }

    @Bindable
    public String getQueryCompanyTitle() {
        return queryCompanyTitle;
    }

    @Bindable
    public String getQueryContactTitle() {
        return queryContactTitle;
    }

    public EditText getSearchfield() {
        return searchfield;
    }

    public void setSearchfield(String query) {
        // query = querysearch;
        if (query != null) searchfield.setText(query.trim());
    }

    @Bindable
    public String getQueryProjTotal() {
        return queryProjTotal + ((queryProjTotal > 0) ? " Projects" : " Project");
    }

    @Bindable
    public String getQueryCompanyTotal() {
        return queryCompanyTotal + ((queryCompanyTotal > 0) ? " Companies" : " Company");
    }

    @Bindable
    public String getQueryContactTotal() {
        return queryContactTotal + ((queryContactTotal > 0) ? " Contacts" : " Contact");
    }


    /**
     * OnClick handlers
     **/

    public void onClearClicked(View view) {
        searchfield.setText("");
    }

    public void onFilterClicked(View view) {
        Toast.makeText(activity, "Filter clicked.", Toast.LENGTH_SHORT).show();
    }
}
