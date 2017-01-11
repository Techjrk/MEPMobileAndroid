package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(TAG, "onCreate");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setupBinding();
    }

    private void setupBinding() {
        com.lecet.app.databinding.ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        viewModel = new SearchViewModel(this,
                new SearchDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance()));
        binding.setViewModel(viewModel);
    }

    @Override
    public void onBackPressed() {
        viewModel.checkDisplayMSESectionOrMain();
    }


    /**
     * Handle the result of all filters being applied, returning to this main
     * Search Activity with the relevant filters, and make the filtered query.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //TODO - make filtered data call

        Toast.makeText(this, "Search Activity Result request 1: " + requestCode + "  result: " + resultCode, Toast.LENGTH_SHORT).show();

        String locationFilter = "";
        String primaryProjectTypeFilter = "";
        String projectTypeIdFilter = "";
        String valueFilter = "";
        String updatedWithinFilter = "";
        String jurisdictionFilter = "";
        String stageFilter = "";
        String biddingWithinFilter = "";
        String buildingOrHighwayFilter = "";
        String ownerTypeFilter = "";
        String workTypeFilter = "";

        StringBuilder sb = new StringBuilder();     // used to construct the combined search filter

        if (data != null) {

            // Location Filter (valid) - {"projectLocation":{"city":"Brooklyn","state":"NY","county":"Kings","zip5":"11215"}}
            locationFilter = (processLocationFilter(data));
            if(locationFilter.length() > 0) {
                sb.append(locationFilter);
            }

            // Primary Project Type Filter (valid?) {"type": {Engineering}}
            primaryProjectTypeFilter = processPrimaryProjectTypeFilter(data);
            if(primaryProjectTypeFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(primaryProjectTypeFilter);
            }

            // Project ID Type Filter (valid)
            projectTypeIdFilter = processProjectTypeIdFilter(data);
            if(projectTypeIdFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(projectTypeIdFilter);
            }

            // Value Filter (valid)
            valueFilter = processValueFilter(data);
            if(valueFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(valueFilter);
            }

            // Updated Within Filter (valid)
            updatedWithinFilter = processUpdatedWithinFilter(data);
            if(updatedWithinFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(updatedWithinFilter);
            }

            // Jurisdiction Filter (valid)
            jurisdictionFilter = processJurisdictionFilter(data);
            if(jurisdictionFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(jurisdictionFilter);
            }

            // Stage Filter (valid)
            stageFilter = processStageFilter(data);
            if(stageFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(stageFilter);
            }

            // Bidding Within Filter (valid)
            biddingWithinFilter = processBiddingWithinFilter(data);
            if(biddingWithinFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(biddingWithinFilter);
            }

            // Building-or-Highway Filter (valid)
            buildingOrHighwayFilter = processBuildingOrHighwayFilter(data);
            if(buildingOrHighwayFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(buildingOrHighwayFilter);
            }

            // Owner Type Filter (valid)
            ownerTypeFilter = processOwnerTypeFilter(data);
            if(ownerTypeFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(ownerTypeFilter);
            }

            // Work Type Filter (valid)
            workTypeFilter = processWorkTypeFilter(data);
            if(workTypeFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(workTypeFilter);
            }

            // prepend searchFilter param if there are any filters used
            if(sb.length() > 0) {
                //sb.insert(0, ",\"searchFilter\":\"");
                sb.insert(0, ",\"searchFilter\":{");
                sb.append("}");
            }
        }

        String combinedFilter = sb.toString();
        Log.d(TAG, "onActivityResult: combinedFilter: " + combinedFilter);

        String searchStr = "{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"]" + combinedFilter + "}";
        Log.d(TAG, "onActivityResult: searchStr: " + searchStr);

        viewModel.setProjectSearchFilter(searchStr);
        viewModel.updateViewQuery();
        viewModel.setSeeAll(viewModel.getSeeAllForResult());
    }

    /**
     * Process the Location filter data
     * Ex: "projectLocation":{"city":"Brooklyn","county":"Kings","zip5":"11215"}
     */
    private String processLocationFilter(Intent data) {
        String filter = "";
        String projectLocation = data.getStringExtra(SearchViewModel.FILTER_PROJECT_LOCATION);
        if (projectLocation != null && !projectLocation.equals("")) {
            Log.d("projectLocation", "projectLocation = " + projectLocation);
            //filter = "{" + projectLocation + "}";
            filter = projectLocation;
            //Log.d(TAG, "onActivityResult: searchFilter: " + projectLocation);
            //TODO set the result filter to the domain...
            //viewModel.setProjectSearchFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":" + searchFilter + "}");
        }
        //else viewModel.setProjectSearchFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"]}");
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
            //filter = "{" + projectType + "}";
            filter = projectType;
            //Log.d(TAG, "onActivityResult: searchFilter: " + projectType);
            //TODO set the result filter to the domain...
            //viewModel.setProjectSearchFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":" + searchFilter + "}");
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
            //filter = "{" + projectTypeIds + "}";
            filter = projectTypeIds;
            //TODO set the result filter to the domain...
        }
        return filter;
    }

    /**
     * Process the $ Value filter data
     * Ex: "projectValue":{"min":555,"max":666}
     */
    private String processValueFilter(Intent data) {
        String filter = "";
        //String min = "";
        //String max = "";
        String projectValue = data.getStringExtra(SearchViewModel.FILTER_PROJECT_VALUE);
        if(projectValue != null && !projectValue.equals("")) {
            Log.d(TAG, "onActivityResult: projectValue: " + projectValue);
            //filter = "{" + projectValue + "}";
            filter = projectValue;
            //Log.d(TAG, "onActivityResult: searchFilter: " + projectValue);
            //TODO set the result filter to the domain...
            //viewModel.setProjectSearchFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":" + searchFilter + "}");
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
            //filter = "{" + projectUpdatedWithin + "}";
            filter = projectUpdatedWithin;
            //Log.d(TAG, "onActivityResult: searchFilter: " + projectUpdatedWithin);
            //TODO set the result filter to the domain...
            //viewModel.setProjectSearchFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":" + searchFilter + "}");
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
            //filter = "{" + jurisdiction + "}";
            filter = jurisdiction;
            //TODO set the result filter to the domain...
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
            //filter = "{" + stage + "}";
            filter = stage;
            //TODO set the result filter to the domain...
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
            //filter = "{" + projectBiddingWithin + "}";
            filter = projectBiddingWithin;
            //Log.d(TAG, "onActivityResult: searchFilter: " + projectBiddingWithin);
            //TODO set the result filter to the domain...
            //viewModel.setProjectSearchFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":" + searchFilter + "}");
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
            //filter = "{" + projectBuildingOrHighwayWithin + "}";
            filter = projectBuildingOrHighwayWithin;
            //Log.d(TAG, "onActivityResult: searchFilter: " + projectBuildingOrHighwayWithin);
            //TODO set the result filter to the domain...
            //viewModel.setProjectSearchFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":" + searchFilter + "}");
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
            //filter = "{" + ownerType + "}";
            filter = ownerType;
            //Log.d(TAG, "onActivityResult: searchFilter: " + projectBuildingOrHighwayWithin);
            //TODO set the result filter to the domain...
            //viewModel.setProjectSearchFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":" + searchFilter + "}");
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
            //filter = "{" + workType + "}";
            filter = workType;
            //Log.d(TAG, "onActivityResult: searchFilter: " + filter);
            //TODO set the result filter to the domain...
            //viewModel.setProjectSearchFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":" + searchFilter + "}");
        }
        return filter;
    }





}

