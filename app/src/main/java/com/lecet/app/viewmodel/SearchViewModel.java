package com.lecet.app.viewmodel;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.SearchAllCompanyRecyclerViewAdapter;
import com.lecet.app.adapters.SearchAllContactRecyclerViewAdapter;
import com.lecet.app.adapters.SearchAllProjectRecyclerViewAdapter;
import com.lecet.app.adapters.SearchCompanyRecyclerViewAdapter;
import com.lecet.app.adapters.SearchProjectRecyclerViewAdapter;
import com.lecet.app.adapters.SearchRecentRecyclerViewAdapter;
import com.lecet.app.adapters.SearchSummaryCompanyRecyclerViewAdapter;
import com.lecet.app.adapters.SearchSummaryContactRecyclerViewAdapter;
import com.lecet.app.adapters.SearchSummaryProjectRecyclerViewAdapter;
import com.lecet.app.content.SearchActivity;
import com.lecet.app.content.SearchFilterMPSActivity;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Filter;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchCompany;
import com.lecet.app.data.models.SearchContact;
import com.lecet.app.data.models.SearchFilter;
import com.lecet.app.data.models.SearchProject;
import com.lecet.app.data.models.SearchResult;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.SearchDomain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DomandTom 2016.
 */

public class SearchViewModel extends BaseObservable {
    private static final String TAG = "SearchViewModel";
    public static final String FILTER_EXTRA_DATA = "data";
    public static final String FILTER_EXTRA_DATA_BUNDLE = "data_result";
    public static final String FILTER_PROJECT_LOCATION = "projectLocation";
    public static final String FILTER_COMPANY_LOCATION = "companyLocation";
    public static final String FILTER_PROJECT_TYPE = "projectType";
    public static final String FILTER_PROJECT_TYPE_ID = "projectTypeId";
    public static final String FILTER_PROJECT_VALUE = "projectValue";
    public static final String FILTER_PROJECT_UPDATED_IN_LAST = "updatedInLast";
    public static final String FILTER_PROJECT_JURISDICTION = "projectJurisdiction";
    public static final String FILTER_PROJECT_STAGE = "projectStage";
    public static final String FILTER_PROJECT_BIDDING_WITHIN = "biddingWithin";
    public static final String FILTER_PROJECT_BUILDING_OR_HIGHWAY = "buildingOrHighway";
    public static final String FILTER_PROJECT_OWNER_TYPE = "ownerType";
    public static final String FILTER_PROJECT_WORK_TYPE = "workType";
    public static final String FILTER_INSTANT_SEARCH = "instantSearch";
    public static final String SAVE_SEARCH_CATEGORY = "saveSearchCategory";
    public static final String SAVE_SEARCH_CATEGORY_PROJECT = "project";
    public static final String SAVE_SEARCH_CATEGORY_COMPANY = "company";
    public static final String SAVE_SEARCH_CATEGORY_CONTACT = "contact";
    public static boolean USING_INSTANT_SEARCH = false;     //TODO - convert to a private var with accessors
    public static final int REQUEST_CODE_ZERO = 0;
    public static boolean INIT_SEARCH = true;                 //TODO - convert to a private var with accessors
    static final String CONTACT_TEXT = " Contact";
    static final String COMPANY_TEXT = " Company";
    static final String PROJECT_TEXT = " Project";
    public static final int SEE_ALL_NO_RESULT = -1;
    public static final int SEE_ALL_PROJECTS = 0;
    public static final int SEE_ALL_COMPANIES = 1;
    public static final int SEE_ALL_CONTACTS = 2;
    private int seeAllForResult = SEE_ALL_NO_RESULT;
    private static AlertDialog.Builder dialogBuilder;
    private String errorMessage = null;
    private Filter filterSearchSaved;
    private SearchFilter searchFilterSearchSaved;
    private String saveSearchCategory = SAVE_SEARCH_CATEGORY_PROJECT;
    private String saveCompanyFilter;

    public Filter getFilterSearchSaved() {
        return filterSearchSaved;
    }

    public void setFilterSearchSaved(Filter filterSearchSaved) {
        this.filterSearchSaved = filterSearchSaved;
    }

    public SearchFilter getSearchFilterSearchSaved() {
        return searchFilterSearchSaved;
    }

    public void setSearchFilterSearchSaved(SearchFilter searchFilterSearchSaved) {
        this.searchFilterSearchSaved = searchFilterSearchSaved;
    }

    public String getSaveSearchCategory() {
        Log.d(TAG, "getSaveSearchCategory: " + this.saveSearchCategory);
        return this.saveSearchCategory;
    }

    public void setSaveSearchCategory(String saveSearchCategory) {
        Log.d(TAG, "setSaveSearchCategory: " + saveSearchCategory);
        this.saveSearchCategory = saveSearchCategory;
    }

    // Adapter types
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SEARCH_ADAPTER_TYPE_RECENT, SEARCH_ADAPTER_TYPE_PROJECTS, SEARCH_ADAPTER_TYPE_COMPANIES,
            SEARCH_ADAPTER_TYPE_PROJECT_QUERY_SUMMARY, SEARCH_ADAPTER_TYPE_COMPANY_QUERY_SUMMARY, SEARCH_ADAPTER_TYPE_CONTACT_QUERY_SUMMARY,
            SEARCH_ADAPTER_TYPE_PROJECT_QUERY_ALL, SEARCH_ADAPTER_TYPE_COMPANY_QUERY_ALL, SEARCH_ADAPTER_TYPE_CONTACT_QUERY_ALL})
    public @interface SearchAdapterType {
    }

    public static final int SEARCH_ADAPTER_TYPE_RECENT = 0;
    public static final int SEARCH_ADAPTER_TYPE_PROJECTS = 1;
    public static final int SEARCH_ADAPTER_TYPE_COMPANIES = 2;
    public static final int SEARCH_ADAPTER_TYPE_PROJECT_QUERY_SUMMARY = 3;  // Project Query Summary
    public static final int SEARCH_ADAPTER_TYPE_COMPANY_QUERY_SUMMARY = 4;  // Company Query Summary
    public static final int SEARCH_ADAPTER_TYPE_CONTACT_QUERY_SUMMARY = 5;  // Contact Query Summary
    public static final int SEARCH_ADAPTER_TYPE_PROJECT_QUERY_ALL = 6;      // Project Query All
    public static final int SEARCH_ADAPTER_TYPE_COMPANY_QUERY_ALL = 7;      // Company Query All
    public static final int SEARCH_ADAPTER_TYPE_CONTACT_QUERY_ALL = 8;      // Contact Query All

    private SearchActivity activity;
    private final SearchDomain searchDomain;
    private List<SearchResult> adapterDataRecentlyViewed;
    private List<SearchSaved> adapterDataProjectSearchSaved;
    private List<SearchSaved> adapterDataCompanySearchSaved;
    private SearchRecentRecyclerViewAdapter searchAdapterRecentlyViewed;
    private SearchProjectRecyclerViewAdapter searchAdapterProject;
    private SearchCompanyRecyclerViewAdapter searchAdapterCompany;
    private AlertDialog saveSearchDialog = null;
    private boolean saveSearchHeaderVisible = false;
    private boolean isMSE1SectionVisible = true;
    private boolean isMSE2SectionVisible = false;
    private boolean isMSR11Visible = false;
    private boolean isMSR12Visible = false;
    private boolean isMSR13Visible = false;
    private boolean detailVisible = false;
    private final int CONTENT_MAX_SIZE = 4;

    //For MSE 2.0
    private boolean displaySeeAllProject;
    private boolean displaySeeAllCompany;
    private boolean displaySeeAllContact;

    private List<Project> adapterDataProjectAll;
    private List<Company> adapterDataCompanyAll;
    private List<Contact> adapterDataContactAll;

    private List<Project> adapterDataProjectSummary;
    private List<Company> adapterDataCompanySummary;
    private List<Contact> adapterDataContactSummary;
    private SearchSummaryProjectRecyclerViewAdapter searchAdapterProjectSummary;  // adapter for Project Summary Results
    private SearchSummaryCompanyRecyclerViewAdapter searchAdapterCompanySummary;  // adapter for Company Summary Results
    private SearchSummaryContactRecyclerViewAdapter searchAdapterContactSummary;  // adapter for Contact Summary Results
    private SearchAllProjectRecyclerViewAdapter searchAdapterProjectAll;          // adapter for Project Query All Results
    private SearchAllCompanyRecyclerViewAdapter searchAdapterCompanyAll;          // adapter for Company Query All Results
    private SearchAllContactRecyclerViewAdapter searchAdapterContactAll;          // adapter for Contact Query All Results
    private String query = "";
    private String queryProjectTitle;
    private String queryCompanyTitle;
    private String queryContactTitle;
    private int queryProjectTotal;
    private int queryCompanyTotal;
    private int queryContactTotal;
    private boolean isQueryProjectTotalZero;
    private boolean isQueryCompanyTotalZero;
    private boolean isQueryContactTotalZero;
    private boolean queryEmpty;
    private int hideProjectSummary;
    private int hideCompanySummary;
    private int hideContactSummary;

    /**
     * Variables used for displaying the next batch search results for Project, Company and Contacts
     */
    private boolean loadingProject;
    private int pastVisiblesItems, visibleItemCount, totalItemCount, projectSkipCounter;
    private final static int VIEW_MAX_COUNT = 25;

    private boolean loadingCompany;
    private int companySkipCounter;

    private boolean loadingContact;
    private int contactSkipCounter;

    @Bindable
    public boolean getDetailVisible() {
        return detailVisible;
    }

    public void setDetailVisible(boolean detailVisible) {
        this.detailVisible = detailVisible;
    }

    @Bindable
    public int getHideProjectSummary() {
        return hideProjectSummary;
    }

    public void setHideProjectSummary(int hideProjectSummary) {
        this.hideProjectSummary = hideProjectSummary;
        notifyPropertyChanged(BR.hideProjectSummary);
    }

    @Bindable
    public int getHideCompanySummary() {
        return hideCompanySummary;
    }

    public void setHideCompanySummary(int hideCompanySummary) {
        this.hideCompanySummary = hideCompanySummary;
        notifyPropertyChanged(BR.hideCompanySummary);
    }

    @Bindable
    public int getHideContactSummary() {
        return hideContactSummary;
    }

    public void setHideContactSummary(int hideContactSummary) {
        this.hideContactSummary = hideContactSummary;
        notifyPropertyChanged(BR.hideContactSummary);
    }

    public void setProjectSearchFilter(String filter) {
        searchDomain.setProjectFilter(filter);
    }

    //passing the search filter content only of the project
    public void setProjectSearchFilterOnly(String filter) {
        searchDomain.setProjectFilterOnly(filter);
    }

    public void setCompanySearchFilter(String filter) {
        searchDomain.setCompanyFilter(filter);
        Log.d("setcompanyfilter", "setcompanyfilter" + filter);

    }
   /* public void setCompanySearchFilter2(String filter) {
        searchDomain.setCompanyFilter2(filter);
        Log.d("setcompanyfilter","setcompanyfilter"+filter);

    }*/

    //Passing the whole search filter content coming from the web.
    public void setCompanySearchFilterComplete(String filter) {
        searchDomain.setCompanyFilterComplete(filter);
        Log.d("filtercomplete", "filtercomplete" + filter);

    }

    public void setContactSearchFilter(String filter) {
        searchDomain.setContactFilter(filter);
    }

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
    public SearchViewModel(SearchActivity activity, SearchDomain sd) {
        this.activity = activity;
        this.searchDomain = sd;
        //  projectmodel = new SearchProjectViewModel(this,activity,sd);
        setErrorMessage(null);
        seeAllForResult = -1;
        //init();
    }


    public void init() {

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
        initializeAdapterProjectQueryAll();
        initializeAdapterCompanyQueryAll();
        initializeAdapterContactQueryAll();
        setQueryEmpty(true);
    }

    public void updateViewQuery(/*String query*/) {
        //  Log.d("updateviewquery1","updateviewquery1");
      /* if (!USING_INSTANT_SEARCH && query.equals("")) {
            setIsMSE1SectionVisible(true);
            setIsMSE2SectionVisible(false);
            setIsMSR11Visible(false);
            setIsMSR12Visible(false);
            setIsMSR13Visible(false);
            return;
        }*/
        if (INIT_SEARCH) {
            INIT_SEARCH = false;
            setIsMSE1SectionVisible(true);
            setIsMSE2SectionVisible(false);
            setIsMSR11Visible(false);
            setIsMSR12Visible(false);
            setIsMSR13Visible(false);
            setDetailVisible(false);
            return;
        }
        setHideProjectSummary(View.GONE);
        setHideCompanySummary(View.GONE);
        setHideContactSummary(View.GONE);
        if (SearchDomain.callProjectService != null) {
            setQueryProjectTotal(0);
            setIsQueryProjectTotalZero(true);
            SearchDomain.callProjectService.cancel();
        }
        if (SearchDomain.callCompanyService != null) {
            setQueryCompanyTotal(0);
            setIsQueryCompanyTotalZero(true);
            SearchDomain.callCompanyService.cancel();
        }
        if (SearchDomain.callContactService != null) {
            setQueryContactTotal(0);
            setIsQueryContactTotalZero(true);
            SearchDomain.callContactService.cancel();
        }
        checkQueryAndTotal();
        setIsMSE2SectionVisible(true);
        setIsMSE1SectionVisible(false);
        setQueryProjectTitle(query + " in Projects");
        setQueryCompanyTitle(query + " in Companies");
        setQueryContactTitle(query + " in Contacts");

    }

    /**
     * Get the list of Project in Query Search Summary
     */
    private void checkQueryAndTotal() {
        /** For Project query total view
         */
        getProjectQuery(query);
        getQueryProjectTotal();
        setHideProjectSummary(View.VISIBLE);
        /**
         * For Company query total view
         */
        getCompanyQuery(query);
        getQueryCompanyTotal();
        setHideCompanySummary(View.VISIBLE);
        /**
         * For Contact query total view
         */
        getContactQuery(query);
        getQueryContactTotal();
        setHideContactSummary(View.VISIBLE);
    }


    public void getProjectQueryListSummary(SearchProject sp) {
        RealmList<Project> slist = sp.getResults();

        adapterDataProjectSummary.clear();
        adapterDataProjectAll.clear();
        int ctr = 0;
        for (Project s : slist) {
            if (s != null) {
                adapterDataProjectAll.add(s);
                if (ctr < CONTENT_MAX_SIZE) adapterDataProjectSummary.add(s);
                //else break;
                ctr++;

            }
        }
        searchAdapterProjectSummary.notifyDataSetChanged();
        searchAdapterProjectAll.notifyDataSetChanged();
    }

    /**
     * Get the list of Company in Query Search Summary
     */

    public void getCompanyQueryListSummary(SearchCompany sp) {
        RealmList<Company> slist = sp.getResults();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(slist);
        realm.commitTransaction();

        adapterDataCompanySummary.clear();
        adapterDataCompanyAll.clear();
        int ctr = 0;
        for (Company s : slist) {
            if (s != null) {
                adapterDataCompanyAll.add(s);
                if (ctr < CONTENT_MAX_SIZE) adapterDataCompanySummary.add(s);
                ctr++;

            }
        }

        searchAdapterCompanySummary.notifyDataSetChanged();
        searchAdapterCompanyAll.notifyDataSetChanged();
    }

    /**
     * Get the list of Contacts in Query Search Summary
     */
    public void getContactQueryListSummary(SearchContact sp) {
        RealmList<Contact> slist = sp.getResults();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(slist);
        realm.commitTransaction();
        adapterDataContactSummary.clear();
        adapterDataContactAll.clear();
        int ctr = 0;
        for (Contact s : slist) {
            if (s != null) {
                adapterDataContactAll.add(s);
                if (ctr < CONTENT_MAX_SIZE) adapterDataContactSummary.add(s);
                ctr++;
            }
        }

        searchAdapterContactSummary.notifyDataSetChanged();
        searchAdapterContactAll.notifyDataSetChanged();
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
                        ctr++;
                        if (s != null) {
                            adapterDataRecentlyViewed.add(s);
                        }
                    }

                    searchAdapterRecentlyViewed.notifyDataSetChanged();

                } else {
                    handleError(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SearchResult>> call, Throwable t) {
                handleError(t.getLocalizedMessage());
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

                    for (SearchSaved s : slist) {
                        if (s != null) {
                            if (s.getModelName().equalsIgnoreCase("Project")) {
                                adapterDataProjectSearchSaved.add(s);

                            } else if (s.getModelName().equalsIgnoreCase("Company")) {
                                adapterDataCompanySearchSaved.add(s);
                            }
                        }
                    }
                    searchAdapterProject.notifyDataSetChanged();
                    searchAdapterCompany.notifyDataSetChanged();
                } else {
                    handleError(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SearchSaved>> call, Throwable t) {
                handleError(t.getLocalizedMessage());
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
                    handleError("Unsuccessful Query. " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchProject> call, Throwable t) {
                handleError("Network is busy. Pls. try again. ");
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
                    handleError("Unsuccessful Query. " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchCompany> call, Throwable t) {
                handleError("Network is busy. Pls. try again. ");
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
                    handleError("Unsuccessful Query. " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchContact> call, Throwable t) {
                handleError("Network is busy. Pls. try again. ");
            }
        });
    }

    /**
     * Show the Saved Search Dialog for user to confirm saving of the current search query and filter.
     */
    public void showSaveSearchDialog() {
        if (saveSearchDialog != null && saveSearchDialog.isShowing()) {
            saveSearchDialog.dismiss();
        }

        final EditText nameInput = new EditText(this.activity);
        nameInput.setInputType(InputType.TYPE_CLASS_TEXT);

        saveSearchDialog = new AlertDialog.Builder(this.activity).create();

        saveSearchDialog.setTitle(this.activity.getResources().getString(R.string.save_search_dialog_title));
        saveSearchDialog.setMessage(this.activity.getResources().getString(R.string.save_search_dialog_msg));
        saveSearchDialog.setView(nameInput);

        saveSearchDialog.setButton(AlertDialog.BUTTON_NEGATIVE, this.activity.getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        saveSearchDialog.setButton(AlertDialog.BUTTON_POSITIVE, this.activity.getResources().getString(R.string.save),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String title = nameInput.getText().toString();
                        if (title != null && title.length() > 0) {
                            Log.d(TAG, "showSaveSearchDialog: Save: title: " + title);
                            // String companyLocation = data.getStringExtra(SearchViewModel.FILTER_COMPANY_LOCATION);
                            //String saveCompanyFilter = ((SearchActivity) getActivity()).getCompanyFilter();
                            Log.d("saveCompany", "saveCompany" + saveCompanyFilter);
                            if (saveCompanyFilter != null && !saveCompanyFilter.equals("")) {
                                saveCurrentCompanySearch(title, saveCompanyFilter);
                            } else {
                                saveCurrentProjectSearch(title);
                            }
                        }
                        dialog.dismiss();
                        setSaveSearchHeaderVisible(false);
                    }
                });

        saveSearchDialog.show();
    }

    /**
     * Make the save search call and handle its callback
     */
    private void saveCurrentProjectSearch(String title) {
        Log.d("SearchActivity", "saveCurrentProjectSearch: searchDomain.getProjectFilter(): " + searchDomain.getProjectFilter());


        searchDomain.saveCurrentProjectSearch(title, this.getQuery(), new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "saveCurrentProjectSearch: onResponse: success: Project search saved.");
                } else {
                    Log.e(TAG, "saveCurrentProjectSearch: onResponse: Project search save unsuccessful. " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "saveCurrentProjectSearch: onFailure: Network is busy. Pls. try again. ");
            }
        });
    }

    private void saveCurrentCompanySearch(String title, String filter) {
        searchDomain.setCompanyFilter(filter);
        Log.d("SearchActivity", "saveCurrentCompanySearch: searchDomain.getCompanyFilter(): " + searchDomain.getCompanyFilter());

        searchDomain.saveCurrentCompanySearch(title, this.getQuery(), new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "saveCurrentCompanySearch: onResponse: success: Company search saved.");

                } else {
                    Log.e(TAG, "saveCurrentCompanySearch: onResponse: Company search save unsuccessful. " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "saveCurrentCompanySearch: onFailure: Network is busy. Pls. try again. ");
            }
        });
    }

    /**
     * Display error message
     */
    public void errorDisplayMsg(String message) {
        if (errorMessage != null || activity == null) return;
        errorMessage = message + "\r\n";
        try {
            if (dialogBuilder == null)
                dialogBuilder = new AlertDialog.Builder(activity); //Applying singleton;
            dialogBuilder.setTitle(activity.getString(R.string.error_network_title) + "\r\n" + errorMessage + "\r\n");
            Log.e("Error:", "Error " + errorMessage);
            dialogBuilder.setMessage(errorMessage);
            dialogBuilder.setNegativeButton(activity.getString(R.string.ok), null);
            Log.e("SearchViewModel", "onFailure: " + errorMessage);
            dialogBuilder.show();
        } catch (Exception e) {
            handleError(message);
        }
    }

    /**
     * Handle error
     */
    private void handleError(String errMsg) {
        Log.e("SearchViewModel", "ERROR: " + errMsg);
    }

    /**
     * Used for fetching the next 25 batch company results.
     *
     * @param q - query search value
     */

    public void getContactAdditionalData(String q) {
        searchDomain.getSearchContactQuery(q, new Callback<SearchContact>() {
            @Override
            public void onResponse(Call<SearchContact> call, Response<SearchContact> response) {
                SearchContact sc;
                if (response.isSuccessful()) {
                    sc = response.body();
                    if (sc == null) return;
                    RealmList<Contact> slist = sc.getResults();
                    if (slist == null) return;
                    int ctr = 0;
                    for (Contact s : slist) {
                        if (s != null) {
                            adapterDataContactAll.add(s);
                            ctr++;
                        }
                    }
                    searchAdapterContactAll.notifyDataSetChanged();
                    if (ctr > 0) loadingContact = true;
                } else {
                    handleError("Unsuccessful Query. " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchContact> call, Throwable t) {
                handleError("Network is busy. Pls. try again. ");
            }
        });
    }
    /**
     * Initialize Contact Items Adapter Search Query All
     */
    private void initializeAdapterContactQueryAll() {
        contactSkipCounter=0; loadingContact=true;
        adapterDataContactAll = new ArrayList<Contact>();
        RecyclerView recyclerView = getRecyclerViewById(R.id.recycler_view_contact_query_all);
        setupRecyclerView(recyclerView, LinearLayoutManager.VERTICAL);
        searchAdapterContactAll = new SearchAllContactRecyclerViewAdapter(this.activity, SEARCH_ADAPTER_TYPE_CONTACT_QUERY_ALL, adapterDataContactAll);
        recyclerView.setAdapter(searchAdapterContactAll);
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        /**
         * Variables for checking if the display reaches the bottom recycle's items list.
         */
        visibleItemCount = layoutManager.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

        /**
         * Scroll event for triggering the fetching of the next 25 project results to be displayed.
         *
         */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                // Log.d("count3","count visible:"+visibleItemCount+" total:"+totalItemCount+" past:"+pastVisiblesItems);
                Log.v("scroll", "filterContact" + searchDomain.getContactFilter());
                if (dy > 0) //check for scroll down
                {
                    if (loadingContact) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loadingContact = false;
                            String sf = searchDomain.getContactFilter();
                            sf = sf.substring(0, sf.lastIndexOf('}'));
                            contactSkipCounter += VIEW_MAX_COUNT;
                            if (sf.contains("skip")) {
                                sf = sf.replace("" + (contactSkipCounter - VIEW_MAX_COUNT), "" + contactSkipCounter) + "}";
                            } else {
                                sf = sf + ",\"skip\":" + contactSkipCounter + "}";
                            }
                            searchDomain.setContactFilter(sf);
                            getContactAdditionalData(getQuery());
                            Log.v("Bottom", "Bottom. filter:" + sf);
                        }
                    }
                }
            }
        });
    }

    /**
     * Used for fetching the next 25 batch company results.
     *
     * @param q - query search value
     */

    public void getCompanyAdditionalData(String q) {
        searchDomain.getSearchCompanyQuery(q, new Callback<SearchCompany>() {
            @Override
            public void onResponse(Call<SearchCompany> call, Response<SearchCompany> response) {
                SearchCompany sc;
                if (response.isSuccessful()) {
                    sc = response.body();
                    if (sc == null) return;
                    RealmList<Company> slist = sc.getResults();
                    if (slist == null) return;
                    int ctr = 0;
                    for (Company s : slist) {
                        if (s != null) {
                            adapterDataCompanyAll.add(s);
                            ctr++;
                        }
                    }
                    searchAdapterCompanyAll.notifyDataSetChanged();
                    if (ctr > 0) loadingCompany = true;
                } else {
                    handleError("Unsuccessful Query. " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchCompany> call, Throwable t) {
                handleError("Network is busy. Pls. try again. ");
            }
        });
    }

    /**
     * Initialize Company Items Adapter Search Query All
     */
    private void initializeAdapterCompanyQueryAll() {
        companySkipCounter=0; loadingCompany=true;
        adapterDataCompanyAll = new ArrayList<Company>();
        RecyclerView recyclerView = getRecyclerViewById(R.id.recycler_view_company_query_all);
        setupRecyclerView(recyclerView, LinearLayoutManager.VERTICAL);
        searchAdapterCompanyAll = new SearchAllCompanyRecyclerViewAdapter(this.activity, SEARCH_ADAPTER_TYPE_COMPANY_QUERY_ALL, adapterDataCompanyAll);
        recyclerView.setAdapter(searchAdapterCompanyAll);
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        /**
         * Variables for checking if the display reaches the bottom recycle's items list.
         */
        visibleItemCount = layoutManager.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

        /**
         * Scroll event for triggering the fetching of the next 25 project results to be displayed.
         *
         */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                // Log.d("count3","count visible:"+visibleItemCount+" total:"+totalItemCount+" past:"+pastVisiblesItems);
                Log.v("scroll", "filterCompany" + searchDomain.getCompanyFilter());
                    if (dy > 0) //check for scroll down
                    {
                        if (loadingCompany) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                loadingCompany = false;
                                String sf = searchDomain.getCompanyFilter();
                                sf = sf.substring(0, sf.lastIndexOf('}'));
                                companySkipCounter += VIEW_MAX_COUNT;
                                if (sf.contains("skip")) {
                                    sf = sf.replace("" + (companySkipCounter - VIEW_MAX_COUNT), "" + companySkipCounter) + "}";
                                } else {
                                    sf = sf + ",\"skip\":" + companySkipCounter + "}";
                                }
                                searchDomain.setCompanyFilterComplete(sf);
                                getCompanyAdditionalData(getQuery());
                                Log.v("Bottom", "Bottom. filter:" + sf);
                            }
                        }
                    }
            }
        });
    }

    /**
     * Used for fetching the next 25 batch project results.
     *
     * @param q - query search value
     */

    public void getProjectAdditionalData(String q) {
        searchDomain.getSearchProjectQuery(q, new Callback<SearchProject>() {
            @Override
            public void onResponse(Call<SearchProject> call, Response<SearchProject> response) {
                SearchProject sp;
                if (response.isSuccessful()) {
                    sp = response.body();
                    if (sp == null) return;
                    RealmList<Project> slist = sp.getResults();
                    if (slist == null) return;
                    int ctr = 0;
                    for (Project s : slist) {
                        if (s != null) {
                            adapterDataProjectAll.add(s);
                            ctr++;
                        }
                    }
                    searchAdapterProjectAll.notifyDataSetChanged();
                    if (ctr > 0) loadingProject = true;
                } else {
                    handleError("Unsuccessful Query. " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchProject> call, Throwable t) {
                handleError("Network is busy. Pls. try again. ");
            }
        });
    }


    /**
     * Initialize Project Items Adapter Search Query All
     */

    private void initializeAdapterProjectQueryAll() {
        projectSkipCounter=0; loadingProject=true;
        adapterDataProjectAll = new ArrayList<Project>();
        RecyclerView recyclerView = getRecyclerViewById(R.id.recycler_view_project_query_all);
        setupRecyclerView(recyclerView, LinearLayout.VERTICAL);
        searchAdapterProjectAll = new SearchAllProjectRecyclerViewAdapter(this.activity, SEARCH_ADAPTER_TYPE_PROJECT_QUERY_ALL, adapterDataProjectAll);
        //recyclerView.scrollToPosition(1);
        recyclerView.setAdapter(searchAdapterProjectAll);
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        /**
         * Variables for checking if the display reaches the bottom recycle's items list.
         */
        visibleItemCount = layoutManager.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

        /**
         * Scroll event for triggering the fetching of the next 25 project results to be displayed.
         *
         */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                // Log.d("count3","count visible:"+visibleItemCount+" total:"+totalItemCount+" past:"+pastVisiblesItems);
                 Log.v("scroll", "filter" + searchDomain.getProjectFilter());
                    if (dy > 0) //check for scroll down
                    {
                        if (loadingProject) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                loadingProject = false;
                                String sf = searchDomain.getProjectFilter();
                                sf = sf.substring(0, sf.lastIndexOf('}'));
                                projectSkipCounter += VIEW_MAX_COUNT;
                                if (sf.contains("skip")) {
                                    sf = sf.replace("" + (projectSkipCounter - VIEW_MAX_COUNT), "" + projectSkipCounter) + "}";
                                } else {
                                    sf = sf + ",\"skip\":" + projectSkipCounter + "}";
                                }
                                searchDomain.setProjectFilter(sf);
                                getProjectAdditionalData(getQuery());
                                Log.v("Bottom", "Bottom. filter:" + sf);
                            }
                        }
                    }
            }
        });
    }

    /**
     * Initialize Project Items Adapter Search Query Summary
     */
    private void initializeAdapterProjectQuerySummary() {
        adapterDataProjectSummary = new ArrayList<Project>();
        RecyclerView recyclerView = getRecyclerViewById(R.id.recycler_view_project_query_summary);
        setupRecyclerView(recyclerView, LinearLayoutManager.VERTICAL);
        searchAdapterProjectSummary = new SearchSummaryProjectRecyclerViewAdapter(this.activity, SEARCH_ADAPTER_TYPE_PROJECT_QUERY_SUMMARY, adapterDataProjectSummary);
        recyclerView.setAdapter(searchAdapterProjectSummary);
    }

    /**
     * Initialize Company Items Adapter Search Query
     */
    private void initializeAdapterCompanyQuerySummary() {
        adapterDataCompanySummary = new ArrayList<Company>();
        RecyclerView recyclerView = getRecyclerViewById(R.id.recycler_view_company_query_summary);
        setupRecyclerView(recyclerView, LinearLayoutManager.VERTICAL);
        searchAdapterCompanySummary = new SearchSummaryCompanyRecyclerViewAdapter(this.activity, SEARCH_ADAPTER_TYPE_COMPANY_QUERY_SUMMARY, adapterDataCompanySummary);
        recyclerView.setAdapter(searchAdapterCompanySummary);
    }

    /**
     * Initialize Project Items Adapter Search Query
     */
    private void initializeAdapterContactQuerySummary() {
        adapterDataContactSummary = new ArrayList<Contact>();
        RecyclerView recyclerView = getRecyclerViewById(R.id.recycler_view_contact_query_summary);
        setupRecyclerView(recyclerView, LinearLayoutManager.VERTICAL);
        searchAdapterContactSummary = new SearchSummaryContactRecyclerViewAdapter(this.activity, SEARCH_ADAPTER_TYPE_CONTACT_QUERY_SUMMARY, adapterDataContactSummary);
        recyclerView.setAdapter(searchAdapterContactSummary);
    }

    /**
     * Initialize Recent Items Adapter
     */
    private void initializeAdapterRecentlyViewed() {
        adapterDataRecentlyViewed = new ArrayList<SearchResult>();
        RecyclerView recyclerView = getRecyclerViewById(R.id.recycler_view_recent);
        setupRecyclerView(recyclerView, LinearLayoutManager.HORIZONTAL);
        searchAdapterRecentlyViewed = new SearchRecentRecyclerViewAdapter(this.activity, SEARCH_ADAPTER_TYPE_RECENT, adapterDataRecentlyViewed);
        recyclerView.setAdapter(searchAdapterRecentlyViewed);
    }

    /**
     * Initialize Saved Projects Adapter
     */
    private void initializeAdapterSavedProject() {
        adapterDataProjectSearchSaved = new ArrayList<SearchSaved>();
        RecyclerView recyclerView = getRecyclerViewById(R.id.recycler_view_project);
        setupRecyclerView(recyclerView, LinearLayoutManager.VERTICAL);
        searchAdapterProject = new SearchProjectRecyclerViewAdapter(SEARCH_ADAPTER_TYPE_PROJECTS, this, adapterDataProjectSearchSaved);
        recyclerView.setAdapter(searchAdapterProject);
    }

    /**
     * Initialize Saved Company Adapter
     */
    private void initializeAdapterSavedCompany() {
        adapterDataCompanySearchSaved = new ArrayList<SearchSaved>();
        RecyclerView recyclerView = getRecyclerViewById(R.id.recycler_view_company);
        setupRecyclerView(recyclerView, LinearLayoutManager.VERTICAL);
        searchAdapterCompany = new SearchCompanyRecyclerViewAdapter(SEARCH_ADAPTER_TYPE_COMPANIES, this, adapterDataCompanySearchSaved);
        recyclerView.setAdapter(searchAdapterCompany);
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
        Log.d("detailvisible","detailvisible"+getDetailVisible());
       if (getDetailVisible()) {
           // setIsMSE2SectionVisible(false);
            if (getIsMSR11Visible()) setIsMSR11Visible(true);
            else if (getIsMSR12Visible()) setIsMSR12Visible(true);
            else if (getIsMSR13Visible()) setIsMSR13Visible(true);
            else if (getIsMSE2SectionVisible()) setIsMSE2SectionVisible(true);
            else setIsMSE1SectionVisible(true);
            setDetailVisible(false);
            return;
        }
        else if (isMSE1SectionVisible) {
            activity.finish();
            return;
        }
        else if (isMSE2SectionVisible) {
            /*setIsMSE2SectionVisible(false);
            setIsMSE1SectionVisible(true);*/
            USING_INSTANT_SEARCH = true;
           INIT_SEARCH=true;
            callInit();
            return;
            //  setQuery("");
        } else if (isMSR13Visible) {
            setIsMSR13Visible(false);
            setIsMSE2SectionVisible(true);
            setIsMSE1SectionVisible(false);

            return;
        } else if (isMSR12Visible) {
            setIsMSR12Visible(false);
            setIsMSE2SectionVisible(true);
            setIsMSE1SectionVisible(false);
            return;
        } else if (isMSR11Visible) {
            setIsMSR11Visible(false);
            setIsMSE2SectionVisible(true);
            setIsMSE1SectionVisible(false);
            return;
        } else {
            activity.finish();
        }
    }
    void callInit() {
        searchDomain.initFilter();
        init();
        updateViewQuery();
    }

    public void setDisplaySeeAllProject(boolean displaySeeAllProject) {
        this.displaySeeAllProject = displaySeeAllProject;
        notifyPropertyChanged(BR.displaySeeAllProject);
    }

    public void setDisplaySeeAllCompany(boolean displaySeeAllCompany) {
        this.displaySeeAllCompany = displaySeeAllCompany;
        notifyPropertyChanged(BR.displaySeeAllCompany);
    }

    public void setDisplaySeeAllContact(boolean displaySeeAllContact) {
        this.displaySeeAllContact = displaySeeAllContact;
        notifyPropertyChanged(BR.displaySeeAllContact);
    }

    ///////////////////////////////////
    // BINDABLE
    @Bindable
    public boolean getDisplaySeeAllProject() {
        return displaySeeAllProject;
    }

    @Bindable
    public boolean getDisplaySeeAllCompany() {
        return displaySeeAllCompany;
    }

    @Bindable
    public boolean getDisplaySeeAllContact() {
        return displaySeeAllContact;
    }

    @Bindable
    public boolean getQueryEmpty() {
        return queryEmpty;
    }

    public void setQueryEmpty(boolean queryEmpty) {
        this.queryEmpty = queryEmpty;
    }

    @Bindable
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
        if (query.equals("")) {
            USING_INSTANT_SEARCH = false;
            setQueryEmpty(true);
        } else setQueryEmpty(false);
        notifyPropertyChanged(BR.query);
        updateViewQuery();
        notifyPropertyChanged(BR.queryEmpty);
    }

    @Bindable
    public boolean getSaveSearchHeaderVisible() {
        return this.saveSearchHeaderVisible;
    }

    public void setSaveSearchHeaderVisible(boolean saveSearchHeaderVisible) {
        this.saveSearchHeaderVisible = saveSearchHeaderVisible;
        notifyPropertyChanged(BR.saveSearchHeaderVisible);
    }

    @Bindable
    public boolean getIsMSE1SectionVisible() {
        return isMSE1SectionVisible;
    }

    public void setIsMSE1SectionVisible(boolean MSE1SectionVisible) {
        isMSE1SectionVisible = MSE1SectionVisible;
        notifyPropertyChanged(BR.isMSE1SectionVisible);
        notifyPropertyChanged(BR.isMSE2SectionVisible);
        notifyPropertyChanged(BR.isMSR11Visible);
        notifyPropertyChanged(BR.isMSR12Visible);
        notifyPropertyChanged(BR.isMSR13Visible);

    }

    @Bindable
    public boolean getIsMSE2SectionVisible() {
        return isMSE2SectionVisible;
    }

    public void setIsMSE2SectionVisible(boolean isMSE2SectionVisible) {
        this.isMSE2SectionVisible = isMSE2SectionVisible;
        notifyPropertyChanged(BR.isMSE1SectionVisible);
        notifyPropertyChanged(BR.isMSE2SectionVisible);
        notifyPropertyChanged(BR.isMSR11Visible);
        notifyPropertyChanged(BR.isMSR12Visible);
        notifyPropertyChanged(BR.isMSR13Visible);
    }

    @Bindable
    public boolean getIsMSR11Visible() {
        return isMSR11Visible;
    }

    @Bindable
    public void setIsMSR11Visible(boolean MSR11Visible) {
        isMSR11Visible = MSR11Visible;
        notifyPropertyChanged(BR.isMSE1SectionVisible);
        notifyPropertyChanged(BR.isMSE2SectionVisible);
        notifyPropertyChanged(BR.isMSR11Visible);
    }

    @Bindable
    public boolean getIsMSR12Visible() {
        return isMSR12Visible;
    }

    @Bindable
    public void setIsMSR12Visible(boolean MSR12Visible) {
        isMSR12Visible = MSR12Visible;
        notifyPropertyChanged(BR.isMSE1SectionVisible);
        notifyPropertyChanged(BR.isMSE2SectionVisible);
        notifyPropertyChanged(BR.isMSR12Visible);
    }

    @Bindable
    public boolean getIsMSR13Visible() {
        return isMSR13Visible;
    }

    @Bindable
    public void setIsMSR13Visible(boolean MSR13Visible) {
        isMSR13Visible = MSR13Visible;
        notifyPropertyChanged(BR.isMSE1SectionVisible);
        notifyPropertyChanged(BR.isMSE2SectionVisible);
        notifyPropertyChanged(BR.isMSR13Visible);
    }

    @Bindable
    public boolean getIsQueryProjectTotalZero() {
        // return queryProjectTotal <= 0;
        return isQueryProjectTotalZero;
    }

    @Bindable
    public void setIsQueryProjectTotalZero(boolean queryProjectTotalZero) {
        isQueryProjectTotalZero = queryProjectTotalZero;
        notifyPropertyChanged(BR.isQueryProjectTotalZero);
    }

    @Bindable
    public boolean getIsQueryCompanyTotalZero() {
        //return queryCompanyTotal <= 0;
        return isQueryCompanyTotalZero;
    }

    @Bindable
    public void setIsQueryCompanyTotalZero(boolean queryCompanyTotalZero) {
        isQueryCompanyTotalZero = queryCompanyTotalZero;
        notifyPropertyChanged(BR.isQueryCompanyTotalZero);
    }


    @Bindable
    public boolean getIsQueryContactTotalZero() {
        //return queryContactTotal <= 0;
        return isQueryContactTotalZero;
    }

    @Bindable
    public void setIsQueryContactTotalZero(boolean queryContactTotalZero) {
        isQueryContactTotalZero = queryContactTotalZero;
        notifyPropertyChanged(BR.isQueryContactTotalZero);
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
        boolean moreThanOneTotal = (queryProjectTotal > 1);
        setDisplaySeeAllProject(queryProjectTotal > 4);
        setIsQueryProjectTotalZero(queryProjectTotal <= 0);
        return queryProjectTotal + (moreThanOneTotal ? PROJECT_TEXT + "s" : PROJECT_TEXT);
    }

    @Bindable
    public String getQueryCompanyTotal() {
        boolean moreThanOneTotal = (queryCompanyTotal > 1);
        setDisplaySeeAllCompany(queryCompanyTotal > 4);
        setIsQueryCompanyTotalZero(queryCompanyTotal <= 0);
        return queryCompanyTotal + (moreThanOneTotal ? " Companies" : COMPANY_TEXT);
    }

    @Bindable
    public String getQueryContactTotal() {
        boolean moreThanOneTotal = (queryContactTotal > 1);
        setDisplaySeeAllContact(queryContactTotal > 4);
        setIsQueryContactTotalZero(queryContactTotal <= 0);
        return queryContactTotal + (moreThanOneTotal ? CONTACT_TEXT + "s" : CONTACT_TEXT);
    }

    /**
     * OnClick handlers
     **/

    public void onClearClicked(View view) {
        searchDomain.initFilter();
        setQuery("");
        setIsMSE1SectionVisible(true);
    }

    public void onFilterClicked(View view) {
        Intent intent = new Intent(activity, SearchFilterMPSActivity.class);
        USING_INSTANT_SEARCH = getIsMSE1SectionVisible();                   // refers to whether or not we are launching from a Saved Search view or not
        intent.putExtra(FILTER_INSTANT_SEARCH, USING_INSTANT_SEARCH);
        setDetailVisible(true);
        activity.startActivityForResult(intent, REQUEST_CODE_ZERO);
    }

    public void onCancelSaveSearchClicked(View view) {
        setSaveSearchHeaderVisible(false);
    }

    public void onSaveSearchClicked(View view) {
        saveCompanyFilter = ((SearchActivity) getActivity()).getCompanyFilter();
        searchDomain.setCompanyFilter(saveCompanyFilter);
        showSaveSearchDialog();
    }

    public int getSeeAllForResult() {
        return seeAllForResult;
    }

    public void onClickSeeAllProject(View view) {
        setSaveSearchCategory(SAVE_SEARCH_CATEGORY_PROJECT);
        setSeeAll(SEE_ALL_PROJECTS);
        Log.d("all_project", "allproject" + SEE_ALL_PROJECTS);
    }

    public void onClickSeeAllCompany(View view) {
        setSaveSearchCategory(SAVE_SEARCH_CATEGORY_COMPANY);
        setSeeAll(SEE_ALL_COMPANIES);
        Log.d("all_company", "allcompany:" + SEE_ALL_COMPANIES);
    }

    public void onClickSeeAllContact(View view) {
        setSaveSearchCategory(SAVE_SEARCH_CATEGORY_CONTACT);
        setSeeAll(SEE_ALL_CONTACTS);
    }

    /**
     * setSeeAll - Used to display the previous search view correctly  after using the search filter section by the user.
     *
     * @param seeOrder
     */


    public void setSeeAll(int seeOrder) {
        seeAllForResult = seeOrder;
        Log.d("all_see", "all_see" + seeOrder);
        switch (seeAllForResult) {
            case SEE_ALL_PROJECTS:  //for see all Project
                setIsMSR11Visible(true);
                setIsMSR12Visible(false);
                setIsMSR13Visible(false);
                break;
            case SEE_ALL_COMPANIES:
                setIsMSR11Visible(false);
                setIsMSR12Visible(true);
                setIsMSR13Visible(false);
                break;
            case SEE_ALL_CONTACTS:
                setIsMSR11Visible(false);
                setIsMSR12Visible(false);
                setIsMSR13Visible(true);
                break;
            default:
                return;
        }

        setIsMSE2SectionVisible(false);
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String parseSearchFilter(String parse1) {
        parse1 = parse1.replace("{", "").replace("}", "");
        Log.d("companysearchfilter2", "companysearchfilter2" + parse1);
        String parseLoc[] = parse1.split(",");
        Log.d("parseLength", "parseLength: " + parseLoc.length);
        if (parseLoc == null) return null;
        String st = "";
        int j = 0;
        for (int i = 0; ; ++i) {
            String[] parseEach = parseLoc[i].split("=");
            st += "\"" + parseEach[0].trim() + "\":" + "\"" + parseEach[1].trim() + "\"";
            ++j;
            if (i == (parseLoc.length - 1)) break;
            st += ",";
        }
        Log.d("st", "st:" + st);
        return st;
    }

}
