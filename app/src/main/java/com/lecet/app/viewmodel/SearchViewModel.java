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

    public static final int SEARCH_ADAPTER_TYPE_RECENT = 0;
    public static final int SEARCH_ADAPTER_TYPE_PROJECTS = 1;
    public static final int SEARCH_ADAPTER_TYPE_COMPANIES = 2;
    public static final int SEARCH_ADAPTER_TYPE_PQS = 3;   //PQS - Project Query Summary
    private final AppCompatActivity activity;
    private final SearchDomain searchDomain;
    private static final String TAG = "SearchViewModel";
    //private Project project;    //TODO - unused
    //private String query;
    private String customString = ""; //TODO: Just for personal testing/checking of the response result of the MSE APIs call using customString variable (customString - String Binding :).
    private List<SearchResult> adapterDataRecentlyViewed;
    private List<SearchSaved> adapterDataProjectSearchSaved;
    private List<SearchSaved> adapterDataCompanySearchSaved;
    private SearchRecyclerViewAdapter searchAdapterRecentlyViewed;
    private SearchRecyclerViewAdapter searchAdapterProject;
    private SearchRecyclerViewAdapter searchAdapterCompany;
    public static boolean isMSE2SectionVisible=false;
    //For MSE 2.0
    private TextView viewProjQueryString;
    private TextView viewProjQueryTotal;
    private TextView viewCompanyQueryString;
    private TextView viewCompanyQueryTotal;
    private TextView viewContactQueryString;
    private TextView viewContactQueryTotal;
    private EditText searchfield;
    private List<Project> adapterDataProjectQuerySummary;
    private SearchRecyclerViewAdapter searchAdapterPQS;  //PQS - Project Query Summary

    public EditText getSearchfield() {
        return searchfield;
    }

    public void setSearchfield(String query) {
       if (query !=null) this.searchfield.setText(query.trim());
    }

    private int queryProjTotal;
    private int queryCompanyTotal;
    private int queryContactTotal;

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
        viewProjQueryString = (TextView) activity.findViewById(R.id.queryproject_title);
        viewCompanyQueryString = (TextView) activity.findViewById(R.id.querycompany_title);
        viewContactQueryString = (TextView) activity.findViewById(R.id.querycontact_title);

        searchfield.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable !=null)  updateViewQuery(editable.toString());
            }
        });
    }

    public void updateViewQuery(String query) {

        if (query == null || query.trim().equals("") ) {
            isMSE2SectionVisible = false;
            displayMSESectionVisible();
            return;
        }
        isMSE2SectionVisible = true;
        displayMSESectionVisible();
     if (viewProjQueryString == null ) return;
        viewProjQueryString.setText(query + " in Projects");
        viewCompanyQueryString.setText(query + " in Companies");
        viewContactQueryString.setText(query + " in Contacts");

        query= searchfield.getText().toString().trim();
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

    /* Get the list of Project in Query Search Summary*/
    public void getProjectQueryListSummary(SearchProject sp){
        RealmList<Project> slist = sp.getResults();
        final int CONTENT_MAX_SIZE = 4;
        adapterDataProjectQuerySummary.clear();
            int ctr = 0;
        Log.d("PROJECTS NOEL ","PROJECT NOEL SIZE "+slist.size());
            for (Project s : slist) {
                try {

                    if (s != null) {
                        Log.d("PROJECTS NOEL ","PROJECT NOEL SIZE "+s.getTitle());
                        if (ctr < CONTENT_MAX_SIZE) adapterDataProjectQuerySummary.add(s);
                        ctr++;

                    }
                } catch (Exception e) {
                    //TODO - handle exception
                    Log.d("No project","No project in the list");
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
                String cs = getCustomString();
                if (response.isSuccessful()) {
                    slist = response.body();
                    adapterDataRecentlyViewed.clear();
                    cs += "getUserRecentlyViewed\r\n";
                    int ctr = 0;
                    for (SearchResult s : slist) {
                        Log.d(TAG, "getUserRecentlyViewed: onResponse: " + s);
                        //TODO: testing for getting the result of  RecentlyViewed search
                        try {
                            //                           cs += ("\r\n" + ctr + " code1:" + s.getCode() + " id:" + s.getId() + " pid:" + s.getProjectId() + " cid:" + s.getCompanyId() + " createdAt:" + s.getCreatedAt() +
                            //                                   " Lat: " + s.getProject().getGeocode().getLat() + " long: " + s.getProject().getGeocode().getLng() + "\r\n");
                            ctr++;
                            if (s.getProject() != null) {
                                adapterDataRecentlyViewed.add(s);
                            }
                        } catch (Exception e) {
                            //TODO - handle exception
                        }
                    }

                    setCustomString(cs);
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
                String cs = getCustomString();
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
                                cs += "\r\n" + adapterDataProjectSearchSaved.get(projectcounter).getTitle();
                                projectcounter++;
                            } else if (s.getModelName().equalsIgnoreCase("Company")) {
                                adapterDataCompanySearchSaved.add(s);
                                cs += "\r\n" + adapterDataCompanySearchSaved.get(companycounter).getTitle();
                                companycounter++;
                            }
                        }
                        //cs += getCustomString() + ("\r\n" + projectcounter + " title:" + s.getTitle() + " modelName:" + s.getModelName() + " id:" + s.getId() + " userid:" + s.getUserId() + " query:" + s.getQuery() + "\r\n");
                        setCustomString(cs);
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
        //TODO: List testing
        // SearchResult sr1 = new SearchResult();
        // sr1.setCode("code123");
        // SearchResult sr2 = new SearchResult();
        // adapterDataRecentlyViewed.add(sr1);
        // adapterDataRecentlyViewed.add(sr2);
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
    

    @Bindable
    public String getCustomString() {
        return customString;
    }

    public void setCustomString(String customString) {
        this.customString = customString;
        notifyPropertyChanged(BR.customString);
    }

    public void onBackClicked(View view) {
        checkDisplayMSESectionOrMain();
    }

    private void checkDisplayMSESectionOrMain() {

        if (isMSE2SectionVisible) {
            isMSE2SectionVisible = false;
            displayMSESectionVisible();
            if (searchfield != null) searchfield.setText("");
        } else {
            activity.finish();
        }
    }

    public void displayMSESectionVisible() {
        if (isMSE2SectionVisible) {
            activity.findViewById(R.id.mse1section).setVisibility(View.GONE);
            activity.findViewById(R.id.mse2section).setVisibility(View.VISIBLE);
        } else {
            activity.findViewById(R.id.mse1section).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.mse2section).setVisibility(View.GONE);
        }
    }

    public AppCompatActivity getActivity() {
        return activity;
    }
}
