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

        StringBuilder sb = new StringBuilder();     // used to construct the combined search filter

        if (data != null) {

            // Location Filter e.g. {"projectLocation":{"city":"Brooklyn","state":"NY","county":"Kings","zip5":"11215"}}
            String locationFilter = processLocationFilter(data);
            if(locationFilter.length() > 0) {
                sb.append(locationFilter);
            }

            // Primary Project Type Filter e.g. {"type": {Engineering}}
            String primaryProjectTypeFilter = processPrimaryProjectTypeFilter(data);
            if(primaryProjectTypeFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(primaryProjectTypeFilter);
            }

            // Project ID Type Filter
            String projectTypeIdFilter = processProjectTypeIdFilter(data);
            if(projectTypeIdFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(projectTypeIdFilter);
            }

            // Value Filter
            String valueFilter = processValueFilter(data);
            if(valueFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(valueFilter);
            }

            // Updated Within Filter
            String updatedWithinFilter = processUpdatedWithinFilter(data);
            if(updatedWithinFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(updatedWithinFilter);
            }

            // Jurisdiction Filter
            String jurisdictionFilter = processJurisdictionFilter(data);
            if(jurisdictionFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(jurisdictionFilter);
            }

            // Stage Filter
            String stageFilter = processStageFilter(data);
            if(stageFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(stageFilter);
            }

            // Bidding Within Filter
            String biddingWithinFilter = processBiddingWithinFilter(data);
            if(biddingWithinFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(biddingWithinFilter);
            }

            // Building-or-Highway Filter
            String buildingOrHighwayFilter = processBuildingOrHighwayFilter(data);
            if(buildingOrHighwayFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(buildingOrHighwayFilter);
            }

            // Owner Type Filter
            String ownerTypeFilter = processOwnerTypeFilter(data);
            if(ownerTypeFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(ownerTypeFilter);
            }

            // Work Type Filter
            String workTypeFilter = processWorkTypeFilter(data);
            if(workTypeFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(workTypeFilter);
            }

            // prepend searchFilter param if there are any filters used
            if(sb.length() > 0) {
                sb.insert(0, ",\"searchFilter\":{");
                sb.append("}");
            }
        }

        String combinedFilter = sb.toString();
        Log.d(TAG, "onActivityResult: combinedFilter: " + combinedFilter);

        String searchStr = "{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"]" + combinedFilter + "}";
        Log.d(TAG, "onActivityResult: searchStr: " + searchStr);

        viewModel.setProjectSearchFilter(searchStr);
        Log.d(TAG, "onActivityResult: new combinedFilter: " + combinedFilter.replaceFirst(",",""));
        //TODO - Noel, do we need to take care of Company and Contact?
        //This area is for setting the filter for Company and Contact..
//        viewModel.setCompanySearchFilter(filter?); //Pls. check what correct filter value should be passed for company
//        viewModel.setContactSearchFilter(filter?); //Pls. check what correct filter value should be passed for contact

//Default section page once you clicked the Apply button of the Search Filter section page.
        viewModel.setIsMSE2SectionVisible(true);
        viewModel.updateViewQuery();

        // if there were any filters applied, show the Save Search? header
        if(sb.length() > 0) {
            viewModel.setSaveSearchHeaderVisible(true);
        }
    }
    
    /**
     * Process the Location filter data
     * Ex: "projectLocation":{"city":"Brooklyn","county":"Kings","zip5":"11215"}
     */
    private String processLocationFilter(Intent data) {
        String filter = "";
        String projectLocation = data.getStringExtra(SearchViewModel.FILTER_PROJECT_LOCATION);
        if (projectLocation != null && !projectLocation.equals("")) {
            Log.d("SearchActivity", "projectLocation = " + projectLocation);
            filter = projectLocation;
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

}

