package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.SearchDomain;
import com.lecet.app.viewmodel.SearchViewModel;

import io.realm.Realm;

public class SearchActivity extends AppCompatActivity {
    private final String TAG = "SearchActivity";
    private SearchViewModel viewModel;
    private SearchDomain searchDomain;
    private String companyFilter;

    public String getCompanyFilter() {
        return companyFilter;
    }

    public void setCompanyFilter(String companyFilter) {
        this.companyFilter = companyFilter;
    }

    public SearchViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(SearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setupBinding();
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = getLayoutInflater();
            View searchBarView = inflater.inflate(R.layout.projects_near_me_search_bar_layout, null);
            //viewModel.setToolbar(searchBarView);
            //  actionBar.setCustomView(searchBarView);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.d(TAG, "onResume");
        SearchViewModel.usingInstantSearch=true;

        if (viewModel.getDetailVisible()) {
            viewModel.checkDisplayMSESectionOrMain();
        } else {
            viewModel.init();
            viewModel.updateViewQuery();
        }
    }

    private void setupBinding() {
        com.lecet.app.databinding.ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        searchDomain = new SearchDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        viewModel = new SearchViewModel(this, searchDomain);
        binding.setViewModel(viewModel);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");

       /* if (viewModel.getIsMSE2SectionVisible()) {
            searchDomain.initFilter();
            viewModel.init();
            viewModel.updateViewQuery();
        }*/
       /*if (viewModel.getDetailVisible()) {
           viewModel.setDetailVisible(false);
       } else*/
        viewModel.checkDisplayMSESectionOrMain();
    }


    /**
     * Handle the result of all filters being applied, returning to this main
     * Search Activity with the relevant filters, and make the filtered query.
     * Projects and Companies support filters; Contacts do not.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED || data == null) return;

        // SEARCH CATEGORY (Project vs Company)
        String category = processSearchCategory(data); //processing the SearchViewModel.SAVE_SEARCH_CATEGORY  - Could be contact, company or project.
        boolean isCompanyCateg = false;
        if (category != null) {
            viewModel.setSaveSearchCategory(category);
            if (category.equals(SearchViewModel.SAVE_SEARCH_CATEGORY_COMPANY)) {
                isCompanyCateg = true;
            }
        }
        StringBuilder projectsSb = new StringBuilder();     // used to construct the combined search filter
        StringBuilder companiesSb = new StringBuilder();    //used for combined company for searchFilter
        StringBuilder esFilterSb = new StringBuilder(); //used for combined company for esFilter;

        // PROJECT-SPECIFIC SEARCH FILTERS

        // Project Location Filter e.g. {"projectLocation":{"city":"Brooklyn","state":"NY","county":"Kings","zip5":"11215"}}
        String projectLocationFilter = processProjectLocationFilter(data);
        if (projectLocationFilter.length() > 0) {
            projectsSb.append(projectLocationFilter);
        }
        // companyFilter = processCompanyLocationFilter(data);
        // Updated Within Filter
        String updatedWithinFilter = processUpdatedWithinFilter(data);
        if (updatedWithinFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(updatedWithinFilter);
        }

        // Stage Filter
        String stageFilter = processStageFilter(data);
        if (stageFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(stageFilter);
        }

        // Building-or-Highway Filter
        String buildingOrHighwayFilter = processBuildingOrHighwayFilter(data);
        if (buildingOrHighwayFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(buildingOrHighwayFilter);
        }

        // Owner Type Filter
        String ownerTypeFilter = processOwnerTypeFilter(data);
        if (ownerTypeFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(ownerTypeFilter);
        }

        // Work Type Filter
        String workTypeFilter = processWorkTypeFilter(data);
        if (workTypeFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(workTypeFilter);
        }

        // COMPANY-SPECIFIC SEARCH FILTERS

        // Company Location Filter e.g. {"companyLocation":{"city":"Brooklyn","state":"NY","county":"Kings","zip5":"11215"}}
        companyFilter = processCompanyLocationFilter(data);
        // String companyLocationFilter = processCompanyLocationFilter(data);
        /*if(companyLocationFilter.length() > 0) {
            companiesSb.append(companyLocationFilter);
        }*/
        if (companyFilter.length() > 0) {
            companiesSb.append(companyFilter);
        }
        // SEARCH FILTERS USED IN BOTH PROJECT AND COMPANY SEARCHES

        // Primary Project Type Filter e.g. {"type": {Engineering}}
        String primaryProjectTypeFilter = processPrimaryProjectTypeFilter(data);
        if (primaryProjectTypeFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(primaryProjectTypeFilter);
            //added to Companies filter the Project Type
            //Uncomment this if-statement if there's a need to separate the filtering it from the Project.
            //if (isCompanyCateg) {
            if (esFilterSb.length() > 0) esFilterSb.append(",");
            esFilterSb.append(primaryProjectTypeFilter);
            // }
        }

        // Project ID Type Filter
        String projectTypeIdFilter = processProjectTypeIdFilter(data);
        if (projectTypeIdFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(projectTypeIdFilter);
            //added to Companies filter the Project Type
            //if (isCompanyCateg) {
           // projectTypeIdFilter = projectTypeIdFilter.replace("projectTypeId","projectTypes");
            if (esFilterSb.length() > 0) esFilterSb.append(",");
            esFilterSb.append(projectTypeIdFilter);
            // }
        }

        // Value Filter
        String valueFilter = processValueFilter(data);
        if (valueFilter.length() > 0) {

            if (valueFilter.contains(getString(R.string.MAX))) {
                valueFilter = valueFilter.replace(",\"max\":MAX", "");
            }

            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(valueFilter);
            //if (isCompanyCateg) {
            //added to Companies filter - the Project Value
            if (esFilterSb.length() > 0) esFilterSb.append(",");
            esFilterSb.append(valueFilter);
            //}
        }

        // Jurisdiction Filter
        String jurisdictionFilter = processJurisdictionFilter(data);
        if (jurisdictionFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(jurisdictionFilter);
            //added to Companies filter - Jurisdiction
            //if (isCompanyCateg) {
            if (esFilterSb.length() > 0) esFilterSb.append(",");
            esFilterSb.append(jurisdictionFilter);
            //}
        }

        // Bidding Within Filter
        String biddingWithinFilter = processBiddingWithinFilter(data);
        if (biddingWithinFilter.length() > 0) {
            if (projectsSb.length() > 0) projectsSb.append(",");
            projectsSb.append(biddingWithinFilter);
            //added to Companies filter when this filter is supported by the API
            //if (isCompanyCateg) {
            if (esFilterSb.length() > 0) esFilterSb.append(",");
            esFilterSb.append(biddingWithinFilter);
            // }
        }

        // prepend searchFilter param if there are any filters used

        // project search
        if (projectsSb.length() > 0) {
            projectsSb.insert(0, ",\"searchFilter\":{");
            projectsSb.append("}");

        }
        //Final search filter result for Project
        String projectsCombinedFilter = projectsSb.toString();
        String projectsSearchStr = "{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"]" + projectsCombinedFilter + "}";
       // String projectsSearchStr = "{\"include\":[{\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}},\"secondaryProjectTypes\",\"projectStage\",{\"bids\"]"+ projectsCombinedFilter + "}";

        viewModel.setProjectSearchFilter(projectsSearchStr);


        projectsCombinedFilter.replaceFirst(",", "");
        Log.d(TAG, "onActivityResult: projectsSearchStr: " + projectsSearchStr);
        Log.d(TAG, "onActivityResult: projectsCombinedFilter: " + projectsCombinedFilter);

        // company search with searchFilter
/*
        if(companiesSb.length() > 0) {
            companiesSb.insert(0, ",\"searchFilter\":{");
            companiesSb.append("}");
        }
*/

        String companiesCombinedFilter = companiesSb.toString();

        /**
         * This will apply the search filter to company filter coming from the project filter if the parsing will be done for project only.
         * If it is not needed, then we could comment the conditional statement below.
         */


        if (companiesSb == null || companiesSb.toString().trim().equals("")) {
//            if (companyFilter == null || companyFilter.trim().equals("")){
            String locationFilter = projectLocationFilter.substring(projectLocationFilter.indexOf(':') + 1);
            String cFilter = "";
            if (locationFilter == null || locationFilter.toString().trim().equals("")) {
                cFilter = "";
            } else
                cFilter = "\"companyLocation\":" + locationFilter;
            viewModel.setCompanySearchFilter(cFilter);
            companyFilter = cFilter;

        } else {
            companyFilter = companiesSb.toString();
//            viewModel.setCompanySearchFilter(companiesSb.toString());
            viewModel.setCompanySearchFilter(companyFilter);
        }

//    if (companiesSb !=null && esFilterSb !=null && !companiesSb.toString().trim().equals("") && !esFilterSb.toString().trim().equals("")){
        if (esFilterSb != null && !esFilterSb.toString().trim().equals("")) {
           String esFilter = esFilterSb.toString();

              esFilter = esFilter.replace("projectTypeId","projectTypes");
            //parameter: searchFilter, esFilter
            viewModel.setCompanySearchFilter(companyFilter, esFilter);

//            viewModel.setCompanySearchFilter(companyFilter, esFilterSb.toString());
        }
        Log.d("companyfilterx", "companyfilterx:" + searchDomain.getCompanyFilter());
        companiesCombinedFilter.replaceFirst(",", "");
        Log.d(TAG, "onActivityResult: companySb: " + companiesSb.toString());
//        Log.d(TAG, "onActivityResult: companyCombinedFilter: " + companiesCombinedFilter);

        //Default section page once you clicked the Apply button of the Search Filter section page.
        viewModel.setIsMSE2SectionVisible(true);
        viewModel.init();
        viewModel.updateViewQuery();

        // if there were any filters applied, show the Save Search? header dialog -- unless the search was for Contacts, which aren't allowed to be saved
        if ((projectsSb.length() > 0 || companiesSb.length() > 0 || esFilterSb.length() > 0) && !viewModel.getSaveSearchCategory().equals(SearchViewModel.SAVE_SEARCH_CATEGORY_CONTACT)) {
            viewModel.setSaveSearchHeaderVisible(true);
        }
    }

    /**
     * Process the Search Category which is either Project or Company,
     * so that the search may be saved to the correct list.
     */
    private String processSearchCategory(Intent data) {
        if (viewModel.getSaveSearchCategory().equals(SearchViewModel.SAVE_SEARCH_CATEGORY_CONTACT)) {
            return SearchViewModel.SAVE_SEARCH_CATEGORY_CONTACT;
        } else if (viewModel.getSaveSearchCategory().equals(SearchViewModel.SAVE_SEARCH_CATEGORY_COMPANY)) {
            return SearchViewModel.SAVE_SEARCH_CATEGORY_COMPANY;
        } else {
            String category = data.getStringExtra(SearchViewModel.SAVE_SEARCH_CATEGORY);
            if (category != null && !category.isEmpty()) {
                return category;
            }
        }
        return SearchViewModel.SAVE_SEARCH_CATEGORY_PROJECT;   // use project by default when none is specified
    }

    /**
     * Process the Project Location filter data
     * Ex: "projectLocation":{"city":"Brooklyn","county":"Kings","zip5":"11215"}
     */
    private String processProjectLocationFilter(Intent data) {
        String filter = "";
        String projectLocation = data.getStringExtra(SearchViewModel.FILTER_PROJECT_LOCATION);
        if (projectLocation != null && !projectLocation.equals("")) {
            Log.d(TAG, "onActivityResult: projectLocation: " + projectLocation);
            filter = projectLocation;
        }

        return filter;
    }

    /**
     * Process the Company Location filter data
     */
    private String processCompanyLocationFilter(Intent data) {
        String filter = "";
        String companyLocation = data.getStringExtra(SearchViewModel.FILTER_COMPANY_LOCATION);
        if (companyLocation != null) {
            Log.d(TAG, "onActivityResult: companyLocation: " + companyLocation);
            Log.d("companyfilterl", "companyfilterl:" + companyLocation);

            filter = companyLocation;
        }

        return filter;
    }

    /**
     * Process the Primary Project Type filter data
     */
    private String processPrimaryProjectTypeFilter(Intent data) {
        String filter = "";
        String projectType = data.getStringExtra(SearchViewModel.FILTER_PROJECT_TYPE);
        if (projectType != null && !projectType.equals("")) {
            Log.d(TAG, "onActivityResult: projectType: " + projectType);
            filter = projectType;
        }
        return filter;
    }

    /**
     * Process the Project Type IDs filter data
     * Ex: "projectTypeId":{"inq": [501, 502, 503]}
     */
    private String processProjectTypeIdFilter(Intent data) {
        String filter = "";
        String projectTypeIds = data.getStringExtra(SearchViewModel.FILTER_PROJECT_TYPE_ID);
        if (projectTypeIds != null && !projectTypeIds.equals("")) {
            Log.d(TAG, "onActivityResult: projectTypeIds: " + projectTypeIds);
            filter = projectTypeIds;
        }
        return filter;
    }

    /**
     * Process the $ Value filter data
     * Ex: "projectValue":{"min":555,"max":666}
     */
    private String processValueFilter(Intent data) {
        String filter = "";
        String projectValue = data.getStringExtra(SearchViewModel.FILTER_PROJECT_VALUE);
        if (projectValue != null && !projectValue.equals("")) {
            Log.d(TAG, "onActivityResult: projectValue: " + projectValue);
            filter = projectValue;
        }
        return filter;
    }

    /**
     * Process the Updated In Last filter data
     * Ex, using 12 months = 366 days: "updatedInLast":366
     */
    private String processUpdatedWithinFilter(Intent data) {
        String filter = "";
        String projectUpdatedWithin = data.getStringExtra(SearchViewModel.FILTER_PROJECT_UPDATED_IN_LAST);
        if (projectUpdatedWithin != null && !projectUpdatedWithin.equals("")) {
            Log.d(TAG, "onActivityResult: projectUpdatedWithin: " + projectUpdatedWithin);
            filter = projectUpdatedWithin;
        }
        return filter;
    }

    /**
     * Process the Jurisdictions filter data
     * Ex: "jurisdictions":{"inq":["Eastern","New Jersey","3"]}
     */
    private String processJurisdictionFilter(Intent data) {
        String filter = "";
        String jurisdiction = data.getStringExtra(SearchViewModel.FILTER_PROJECT_JURISDICTION);
        if (jurisdiction != null && !jurisdiction.equals("")) {
            Log.d(TAG, "onActivityResult: jurisdiction: " + jurisdiction);
            filter = jurisdiction;
        }
        return filter;
    }

    /**
     * Process the Stage filter data
     * Ex: "projectStageId":{"inq":[203, 201, 206]}
     */
    private String processStageFilter(Intent data) {
        String filter = "";
        String stage = data.getStringExtra(SearchViewModel.FILTER_PROJECT_STAGE);
        if (stage != null && !stage.equals("")) {
            Log.d(TAG, "onActivityResult: stage: " + stage);
            filter = stage;
        }
        return filter;
    }

    /**
     * Process the Bidding Within filter data
     * Ex, using 21 days: "biddingInNext":21
     */
    private String processBiddingWithinFilter(Intent data) {
        String filter = "";
        String projectBiddingWithin = data.getStringExtra(SearchViewModel.FILTER_PROJECT_BIDDING_WITHIN);
        if (projectBiddingWithin != null && !projectBiddingWithin.equals("")) {
            Log.d(TAG, "onActivityResult: projectBiddingWithin: " + projectBiddingWithin);
            filter = projectBiddingWithin;
        }
        return filter;
    }

    /**
     * Process the Building-or-Highway filter data
     * Ex: "buildingOrHighway":{"inq":["B","H"]}
     */
    private String processBuildingOrHighwayFilter(Intent data) {
        String filter = "";
        String projectBuildingOrHighwayWithin = data.getStringExtra(SearchViewModel.FILTER_PROJECT_BUILDING_OR_HIGHWAY);
        if (projectBuildingOrHighwayWithin != null && !projectBuildingOrHighwayWithin.equals("")) {
            Log.d(TAG, "onActivityResult: projectBuildingOrHighwayWithin: " + projectBuildingOrHighwayWithin);
            filter = projectBuildingOrHighwayWithin;
        }
        return filter;
    }

    /**
     * Process the Owner Type filter data
     * Ex: "ownerType":{"inq":["Federal"]}
     */
    private String processOwnerTypeFilter(Intent data) {
        String filter = "";
        String ownerType = data.getStringExtra(SearchViewModel.FILTER_PROJECT_OWNER_TYPE);
        if (ownerType != null && !ownerType.equals("")) {
            Log.d(TAG, "onActivityResult: ownerType: " + ownerType);
            filter = ownerType;
        }
        return filter;
    }

    /**
     * Process the Work Type filter data
     * Ex: "workTypeId":{"inq":["2"]}
     */
    private String processWorkTypeFilter(Intent data) {
        String filter = "";
        String workType = data.getStringExtra(SearchViewModel.FILTER_PROJECT_WORK_TYPE);
        if (workType != null && !workType.equals("")) {
            Log.d(TAG, "onActivityResult: workType: " + workType);
            filter = workType;
        }
        return filter;
    }


}

