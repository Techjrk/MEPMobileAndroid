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
        String typeFilter = "";
        String valueFilter = "";
        String updatedWithinFilter = "";

        StringBuilder sb = new StringBuilder();

        if (data != null) {

            // Location Filter (valid) - {"projectLocation":{"city":"Brooklyn","state":"NY","county":"Kings","zip5":"11215"}}
            locationFilter = (processLocationFilter(data));
            if(locationFilter.length() > 0) {
                sb.append(locationFilter);
            }

            // Type Filter (INVALID) {"type": {Engineering}}
            typeFilter = (processTypeFilter(data));
            if(typeFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(typeFilter);
            }

            // Value Filter (valid)
            valueFilter = (processValueFilter(data));
            if(valueFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(valueFilter);
            }

            // Updated Within Filter (valid)
            updatedWithinFilter = (processUpdatedWithinFilter(data));
            if(valueFilter.length() > 0) {
                if(sb.length() > 0) sb.append(",");
                sb.append(updatedWithinFilter);
            }

            //TODO - append commas as necessary when building the filter string (in progress)

            // prepend searchFilter param if there are any filters used
            if(sb.length() > 0) {
                //sb.insert(0, ",\"searchFilter\":\"");
                sb.insert(0, ",\"searchFilter\":");
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
            filter = "{" + projectLocation + "}";
            //Log.d(TAG, "onActivityResult: searchFilter: " + projectLocation);
            //TODO set the result filter to the domain...
            //viewModel.setProjectSearchFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":" + searchFilter + "}");
        }
        //else viewModel.setProjectSearchFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"]}");
        return filter;
    }

    /**
     * Process the Type filter data
     */
    private String processTypeFilter(Intent data) {
        String filter = "";
        String projectType = data.getStringExtra(SearchViewModel.FILTER_PROJECT_TYPE);
        if(projectType != null && !projectType.equals("")) {
            Log.d(TAG, "onActivityResult: projectType: " + projectType);
            filter = "{" + projectType + "}";
            //Log.d(TAG, "onActivityResult: searchFilter: " + projectType);
            //TODO set the result filter to the domain...
            //viewModel.setProjectSearchFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":" + searchFilter + "}");
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
            filter = "{" + projectValue + "}";
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
            filter = "{" + projectUpdatedWithin + "}";
            //Log.d(TAG, "onActivityResult: searchFilter: " + projectUpdatedWithin);
            //TODO set the result filter to the domain...
            //viewModel.setProjectSearchFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":" + searchFilter + "}");
        }
        return filter;
    }

}

