package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private String searchFilter;
    private String companyFilter;
    private SearchDomain searchDomain;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setupBinding();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.d(TAG, "onResume");
        viewModel.init();
        viewModel.updateViewQuery();
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
        searchDomain.initFilter();
        viewModel.checkDisplayMSESectionOrMain();
    }


    /**
     * Handle the result of all filters being applied, returning to this main
     * Search Activity with the relevant filters, and make the filtered query.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        StringBuilder projectsSb  = new StringBuilder();     // used to construct the combined search filter
        StringBuilder companiesSb = new StringBuilder();

        if (data != null) {

            // Project Location Filter e.g. {"projectLocation":{"city":"Brooklyn","state":"NY","county":"Kings","zip5":"11215"}}
            String projectLocationFilter = processProjectLocationFilter(data);
            if(projectLocationFilter.length() > 0) {
                projectsSb.append(projectLocationFilter);
            }

            // Company Location Filter
            String companyLocationFilter = processCompanyLocationFilter(data);
            if(companyLocationFilter.length() > 0) {
                companiesSb.append(companyLocationFilter); //TODO - check. Also make sure companyFilter data is saved for retrieval in SearchViewModel
            }

            // Primary Project Type Filter e.g. {"type": {Engineering}}
            String primaryProjectTypeFilter = processPrimaryProjectTypeFilter(data);
            if(primaryProjectTypeFilter.length() > 0) {
                if(projectsSb.length() > 0) projectsSb.append(",");
                projectsSb.append(primaryProjectTypeFilter);
                companiesSb.append(primaryProjectTypeFilter);
            }

            // Project ID Type Filter
            String projectTypeIdFilter = processProjectTypeIdFilter(data);
            if(projectTypeIdFilter.length() > 0) {
                if(projectsSb.length() > 0) projectsSb.append(",");
                projectsSb.append(projectTypeIdFilter);
            }

            // Value Filter
            String valueFilter = processValueFilter(data);
            if(valueFilter.length() > 0) {
                if(projectsSb.length() > 0) projectsSb.append(",");
                projectsSb.append(valueFilter);
                companiesSb.append(valueFilter);
            }

            // Updated Within Filter
            String updatedWithinFilter = processUpdatedWithinFilter(data);
            if(updatedWithinFilter.length() > 0) {
                if(projectsSb.length() > 0) projectsSb.append(",");
                projectsSb.append(updatedWithinFilter);
            }

            // Jurisdiction Filter
            String jurisdictionFilter = processJurisdictionFilter(data);
            if(jurisdictionFilter.length() > 0) {
                if(projectsSb.length() > 0) projectsSb.append(",");
                projectsSb.append(jurisdictionFilter);
                companiesSb.append(jurisdictionFilter);
            }

            // Stage Filter
            String stageFilter = processStageFilter(data);
            if(stageFilter.length() > 0) {
                if(projectsSb.length() > 0) projectsSb.append(",");
                projectsSb.append(stageFilter);
            }

            // Bidding Within Filter
            String biddingWithinFilter = processBiddingWithinFilter(data);
            if(biddingWithinFilter.length() > 0) {
                if(projectsSb.length() > 0) projectsSb.append(",");
                projectsSb.append(biddingWithinFilter);
                companiesSb.append(biddingWithinFilter);
            }

            // Building-or-Highway Filter
            String buildingOrHighwayFilter = processBuildingOrHighwayFilter(data);
            if(buildingOrHighwayFilter.length() > 0) {
                if(projectsSb.length() > 0) projectsSb.append(",");
                projectsSb.append(buildingOrHighwayFilter);
            }

            // Owner Type Filter
            String ownerTypeFilter = processOwnerTypeFilter(data);
            if(ownerTypeFilter.length() > 0) {
                if(projectsSb.length() > 0) projectsSb.append(",");
                projectsSb.append(ownerTypeFilter);
            }

            // Work Type Filter
            String workTypeFilter = processWorkTypeFilter(data);
            if(workTypeFilter.length() > 0) {
                if(projectsSb.length() > 0) projectsSb.append(",");
                projectsSb.append(workTypeFilter);
            }

            // prepend searchFilter param if there are any filters used
            if(projectsSb.length() > 0) {
                projectsSb.insert(0, ",\"searchFilter\":{");
                projectsSb.append("}");
            }
            if(companiesSb.length() > 0) {
                companiesSb.insert(0, ",\"searchFilter\":{");
                companiesSb.append("}");
            }
        }

        // project search
        String projectsCombinedFilter = projectsSb.toString();
        String projectsSearchStr = "{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"]" + projectsCombinedFilter + "}";
        viewModel.setProjectSearchFilter(projectsSearchStr);
        Log.d(TAG, "onActivityResult: projectsSearchStr: " + projectsSearchStr);
        Log.d(TAG, "onActivityResult: projectsCombinedFilter: " + projectsCombinedFilter.replaceFirst(",",""));
        Log.d(TAG, "onActivityResult: projectsCombinedFilter: " + projectsCombinedFilter);

        // company search
        String companiesCombinedFilter = companiesSb.toString();
        String companiesSearchStr = "{\"include\":[\"contacts\",{\"projects\":[\"projectStage\"]},{\"projectContacts\":[\"contactType\"]}]" + companiesCombinedFilter + "}";
        viewModel.setCompanySearchFilter(companiesSearchStr);
        Log.d(TAG, "onActivityResult: companiesSearchStr:  " + companiesSearchStr);
        Log.d(TAG, "onActivityResult: companiesCombinedFilter: " + companiesCombinedFilter.replaceFirst(",",""));
        Log.d(TAG, "onActivityResult: companiesCombinedFilter: " + companiesCombinedFilter);
        setCompanyFilter(companiesSearchStr);   //TODO - check, it seems some searches are being only saved to Company Search rather than Project Search


        //Revisit this:
        //This area is for setting the filter for Company and Contact..
      //  viewModel.setCompanySearchFilter(filter?); //Pls. check what correct filter value should be passed for company
//        viewModel.setContactSearchFilter(filter?); //Pls. check what correct filter value should be passed for contact

//Default section page once you clicked the Apply button of the Search Filter section page.
        viewModel.setIsMSE2SectionVisible(true);
        viewModel.updateViewQuery();

        // if there were any filters applied, show the Save Search? header
        if(projectsSb.length() > 0 || companiesSb.length() > 0) {
            viewModel.setSaveSearchHeaderVisible(true);
        }
    }
    
    /**
     * Process the Location filter data
     * Ex: "projectLocation":{"city":"Brooklyn","county":"Kings","zip5":"11215"}
     */
    private String processProjectLocationFilter(Intent data) {
        String filter = "";
        String projectLocation = data.getStringExtra(SearchViewModel.FILTER_PROJECT_LOCATION);
        /*String companyLocation = data.getStringExtra(SearchViewModel.FILTER_COMPANY_LOCATION);
        if (companyLocation !=null) {
            viewModel.setCompanySearchFilter(companyLocation); //Pls. check what correct filter value should be passed for company
            Log.d("companyfilter","companyfilter:"+companyLocation);
            filter = companyLocation;
        }*/
        if (projectLocation != null && !projectLocation.equals("")) {
            Log.d("SearchActivity", "projectLocation = " + projectLocation);
            filter = projectLocation;
            Log.d("projectfilter","projectfilter:"+projectLocation);
        }

        return filter;
    }

    private String processCompanyLocationFilter(Intent data) {
        String filter = "";
        String companyLocation = data.getStringExtra(SearchViewModel.FILTER_COMPANY_LOCATION);
        if (companyLocation !=null) {
            Log.d("companyfilter","companyfilter:"+companyLocation);
            viewModel.setCompanySearchFilter(companyLocation); //Pls. check what correct filter value should be passed for company

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
        if(projectType != null && !projectType.equals("")) {
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
        if(projectTypeIds != null && !projectTypeIds.equals("")) {
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
        if(projectValue != null && !projectValue.equals("")) {
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
        if(projectUpdatedWithin != null && !projectUpdatedWithin.equals("")) {
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
        if(jurisdiction != null && !jurisdiction.equals("")) {
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
        if(stage != null && !stage.equals("")) {
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
        if(projectBiddingWithin != null && !projectBiddingWithin.equals("")) {
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
        if(projectBuildingOrHighwayWithin != null && !projectBuildingOrHighwayWithin.equals("")) {
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
        if(ownerType != null && !ownerType.equals("")) {
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
        if(workType != null && !workType.equals("")) {
            Log.d(TAG, "onActivityResult: workType: " + workType);
            filter = workType;
        }
        return filter;
    }


    ///////////////////
    // GETTERS/SETTERS

    public void setCompanyFilter(String companyFilter) {
        this.companyFilter = companyFilter;
    }

    public String getCompanyFilter() {
        return this.companyFilter;
    }



}

