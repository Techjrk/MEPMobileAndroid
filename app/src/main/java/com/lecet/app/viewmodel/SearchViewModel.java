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
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchCompany;
import com.lecet.app.data.models.SearchContact;
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
    public static final int SEARCH_ADAPTER_TYPE_CQS = 4;   //CQS - Company Query Summary
    public static final int SEARCH_ADAPTER_TYPE_CONTACTQS = 5;

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
    //private static EditText searchfield;
    private List<Project> adapterDataProjectQuerySummary;
    private List<Company> adapterDataCompanyQuerySummary;
    private List<Contact> adapterDataContactQuerySummary;
    private SearchRecyclerViewAdapter searchAdapterPQS;  //PQS - Project Query Summary
    private SearchRecyclerViewAdapter searchAdapterCQS;  //CQS - Company Query Summary
    private SearchRecyclerViewAdapter searchAdapterContactQS;
    private String query;
    private String queryProjectTitle;
    private String queryCompanyTitle;
    private String queryContactTitle;
    private int queryProjectTotal;
    private int queryCompanyTotal;
    private int queryContactTotal;

    public void setQueryProjectTotal(int queryProjectTotal) {
        this.queryProjectTotal = queryProjectTotal;
        notifyPropertyChanged(BR.queryProjectTotal);
    }

    public void setQueryCompanyTotal(int queryCompanyTotal) {
        this.queryCompanyTotal = queryCompanyTotal;
        notifyPropertyChanged(BR.queryCompanyTotal);
    }

    public void setQueryContactTotal(int queryContactTotal) {
        this.queryContactTotal = queryContactTotal;
        notifyPropertyChanged(BR.queryContactTotal);
    }

    public void setQueryProjectTitle(String queryProjectTitle) {
        this.queryProjectTitle = queryProjectTitle;
        notifyPropertyChanged(BR.queryProjectTitle);
    }

    public void setQueryCompanyTitle(String queryCompanyTitle) {
        this.queryCompanyTitle = queryCompanyTitle;
        notifyPropertyChanged(BR.queryCompanyTitle);
    }

    public void setQueryContactTitle(String queryContactTitle) {
        this.queryContactTitle = queryContactTitle;
        notifyPropertyChanged(BR.queryContactTitle);
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
        initializeAdapterCompanyQuerySummary();
        initializeAdapterContactQuerySummary();

    }


    public void updateViewQuery(/*String query*/) {

        if (query == null || query.trim().equals("")) {
            setIsMSE2SectionVisible(false);
            return;
        }
        setIsMSE2SectionVisible(true);

        setQueryProjectTitle(query + " in Projects");
        setQueryCompanyTitle(query + " in Companies");
        setQueryContactTitle(query + " in Contacts");


        //query = searchfield.getText().toString().trim();
        /** For Project query total view
         */
        getQueryProjectTotal();
        getProjectQuery(query);

        /**
         * For Company query total view
         */
        getQueryCompanyTotal();
        getCompanyQuery(query);

        /**
         * For Contact query total view
         */
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
                    else break;
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
     * Get the list of Company in Query Search Summary
     */
    public void getCompanyQueryListSummary(SearchCompany sp) {
        RealmList<Company> slist = sp.getResults();

        adapterDataCompanyQuerySummary.clear();
        int ctr = 0;
        for (Company s : slist) {
            try {

                if (s != null) {
                    if (ctr < CONTENT_MAX_SIZE) adapterDataCompanyQuerySummary.add(s);
                    else break;
                    ctr++;

                }
            } catch (Exception e) {
                //TODO - handle exception
                Log.w("No company", "No company in the list");
            }
        }

        searchAdapterCQS.notifyDataSetChanged();
    }

    /**
     * Get the list of Contacts in Query Search Summary
     */
    public void getContactQueryListSummary(SearchContact sp) {
        RealmList<Contact> slist = sp.getResults();

        adapterDataContactQuerySummary.clear();
        int ctr = 0;
        for (Contact s : slist) {
            try {

                if (s != null) {
                    if (ctr < CONTENT_MAX_SIZE) adapterDataContactQuerySummary.add(s);
                    else break;
                    ctr++;

                }
            } catch (Exception e) {
                //TODO - handle exception
                Log.w("No contact", "No contact in the list");
            }
        }

        searchAdapterContactQS.notifyDataSetChanged();
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
                            Log.e("tUserRecentlyViewed", "Exception in getUserRecentlyViewed" + e.getMessage());
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
                    setQueryProjectTotal(searchproject.getTotal());
                    getProjectQueryListSummary(searchproject);

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
        searchDomain.getSearchCompanyQuery(q, new Callback<SearchCompany>() {
            @Override
            public void onResponse(Call<SearchCompany> call, Response<SearchCompany> response) {
                if (response.isSuccessful()) {
                    SearchCompany searchcompany = response.body();
                    setQueryCompanyTotal(searchcompany.getTotal());
                    getCompanyQueryListSummary(searchcompany);

                } else {
                    errorDisplayMsg("Unsuccessful Query. " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchCompany> call, Throwable t) {
                errorDisplayMsg("Network is busy. Pls. try again. ");
            }
        });
    }

    public void getContactQuery(String q) {
        searchDomain.getSearchContactQuery(q, new Callback<SearchContact>() {
            @Override
            public void onResponse(Call<SearchContact> call, Response<SearchContact> response) {
                if (response.isSuccessful()) {
                    SearchContact searchcontact = response.body();
                    setQueryContactTotal(searchcontact.getTotal());
                    getContactQueryListSummary(searchcontact);
                } else {
                    errorDisplayMsg("Unsuccessful Query. " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchContact> call, Throwable t) {
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
     * Initialize Project Items Adapter Search Query
     */
    private void initializeAdapterCompanyQuerySummary() {
        adapterDataCompanyQuerySummary = new ArrayList<Company>();
        RecyclerView recyclerViewCQS = getRecyclerViewById(R.id.recycler_view_company_query_summary);
        setupRecyclerView(recyclerViewCQS, LinearLayoutManager.VERTICAL);
        searchAdapterCQS = new SearchRecyclerViewAdapter(this, adapterDataCompanyQuerySummary);
        searchAdapterCQS.setAdapterType(SEARCH_ADAPTER_TYPE_CQS);
        recyclerViewCQS.setAdapter(searchAdapterCQS);
    }

    /**
     * Initialize Project Items Adapter Search Query
     */
    private void initializeAdapterContactQuerySummary() {
        adapterDataContactQuerySummary = new ArrayList<Contact>();
        RecyclerView recyclerViewContactQS = getRecyclerViewById(R.id.recycler_view_contact_query_summary);
        setupRecyclerView(recyclerViewContactQS, LinearLayoutManager.VERTICAL);
        searchAdapterContactQS = new SearchRecyclerViewAdapter(this, adapterDataContactQuerySummary);
        searchAdapterContactQS.setAdapterType(SEARCH_ADAPTER_TYPE_CONTACTQS);
        recyclerViewContactQS.setAdapter(searchAdapterContactQS);
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

    public void checkDisplayMSESectionOrMain() {
        if (isMSE2SectionVisible) {
            setIsMSE2SectionVisible(false);
            setQuery("");
        } else activity.finish();
    }

    ///////////////////////////////////
    // BINDABLE

    @Bindable
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
        notifyPropertyChanged(BR.query);
        updateViewQuery();
    }

    @Bindable
    public boolean getIsMSE2SectionVisible() {
        return isMSE2SectionVisible;
    }

    public void setIsMSE2SectionVisible(boolean isMSE2SectionVisible) {
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

    @Bindable
    public String getQueryProjectTotal() {
        return queryProjectTotal + ((queryProjectTotal > 0) ? " Projects" : " Project");
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
        setQuery(null);
    }

    public void onFilterClicked(View view) {
        Toast.makeText(activity, "Filter clicked.", Toast.LENGTH_SHORT).show();
    }

}
