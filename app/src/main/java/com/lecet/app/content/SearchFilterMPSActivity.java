package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterMps30Binding;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.Arrays;
import java.util.List;

public class SearchFilterMPSActivity extends AppCompatActivity {
    SearchFilterMPFViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //     setContentView(R.layout.activity_search_filter_mse);
        // setContentView(R.layout.activity_search_filter_mps30);
        ActivitySearchFilterMps30Binding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mps30);
        viewModel = new SearchFilterMPFViewModel(this);
        sfilter.setViewModel(viewModel);
        // intent = getIntent();
    }

    /**
     * Get data back from user input from a Filter Activity
     * and put that data into the View Model
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;

        String[] info = data.getStringArrayExtra("data");   //TODO - get values by name rather than array access such as [0] etc. Refactor into private helpers such as getCity(info), getProjectLocation(info), etc
        if (info != null)
            switch (resultCode) {

                // Location
                case R.id.location & 0xfff:
                    processLocation(info);
                    break;

                // Type
                case R.id.type & 0xfff:
                    processType(info);
                    break;

                // Dollar Value
                case R.id.value & 0xfff:
                    processValue(info);
                    break;

                // Updated Within
                case R.id.updated_within & 0xfff:
                    processUpdatedWithin(info);
                    break;

                // Jurisdiction
                case R.id.jurisdiction & 0xfff:
                    //TODO: Compose the correct search filter result for Jurisdiction and set it to setSearchFilterResult
                    viewModel.setJurisdiction_select(info[0]);
                    break;

                // Project Stage
                case R.id.stage & 0xfff:
                    //TODO: Compose the correct search filter result for Stage and set it to setSearchFilterResult
                    viewModel.setStage_select(info[0]);
                    break;

                // Bidding Within
                case R.id.bidding_within & 0xfff:
                    //TODO: Compose the correct search filter result for Bidding Within and set it to setSearchFilterResult
                    viewModel.setBidding_within_select(info[0]);
                    break;

                // Building / Highway
                case R.id.bh & 0xfff:
                    //TODO: Compose the correct search filter result for BH and set it to setSearchFilterResult
                    viewModel.setBh_select(info[0]);
                    break;

                // Owner Type
                case R.id.ownertype & 0xfff:
                    //TODO: Compose the correct search filter result for OwnerType and set it to setSearchFilterResult
                    viewModel.setOwner_type_select(info[0]);
                    break;

                // Work Type
                case R.id.worktype & 0xfff:
                    //TODO: Compose the correct search filter result for WorkType and set it to setSearchFilterResult
                    viewModel.setWork_type_select(info[0]);
                    break;
            }
    }

    private void processLocation(String[] arr) {

        // initial vars from raw data
        String city   = arr[0];
        String state  = arr[1];
        String county = arr[2];
        String zip    = arr[3];

        // comma-separated constructed String for UI display
        String cityStr   = city;
        String stateStr  = !state.equals("")  ? (!city.equals("")   ? "," + state  : " " + state)  : "";       //TODO - any validation here? compare iOS version
        String countyStr = !county.equals("") ? (!state.equals("")  ? "," + county : " " + county) : "";
        String zipStr    = !zip.equals("")    ? (!zip.equals("")    ? "," + zip    : " " + zip)    : "";

        String locationText = cityStr + stateStr + countyStr + zipStr;
        viewModel.setLocation_select(locationText);
        Log.d("SearchFilterMPSAct", "location: " + locationText);

        // StringBuilder used for generating query with commas as appropriate
        String projectLocation = "";
                //projectLocation += ( !city.equals("") ? "\"city\":\"" + city + "\"":"");
                        //projectLocation += ( !city.equals("") ? (!city.equals("") ? ",\"city\":\"" + city + "\"" : "\"city\":\"" + city + "\""):"");
                //projectLocation += ( !state.equals("") ? (!state.equals("") || !state.equals("") || !state.equals("")  ? ",\"state\":\"" + state + "\"" : "\"state\":\"" + state + "\""):"");
                //projectLocation += ( !county.equals("") ? (!county.equals("") || !county.equals("") ? ",\"county\":\"" + county + "\"" : "\"county\":\"" + county + "\""):"");
                        //projectLocation += ( !zip.equals("") ? "\"zip5\":\"" + zip + "\"":"");
               // projectLocation += ( !zip.equals("") ? (!zip.equals("") ? ",\"zip5\":\"" + zip + "\"" : "\"zip5\":\"" + zip + "\""):"");

        StringBuilder sb = new StringBuilder();
        if(city.length() > 0) {
            sb.append("\"city\":\"" + city + "\"");
        }
        if(state.length() > 0) {
            if(sb.length() > 0) sb.append(",");
            sb.append("\"state\":\"" + state + "\"");
        }
        if(county.length() > 0) {
            if(sb.length() > 0) sb.append(",");
            sb.append("\"county\":\"" + county + "\"");
        }
        if(zip.length() > 0) {
            if(sb.length() > 0) sb.append(",");
            sb.append("\"zip5\":\"" + zip + "\"");
        }

        projectLocation = sb.toString();

        if (!projectLocation.trim().equals("")) {
            projectLocation = "\"projectLocation\":{" + projectLocation + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_LOCATION, projectLocation);  // this should work whether or not projectLocation is empty
    }

    //TODO: Compose the correct search filter result for Type and set it to setSearchFilterResult
    private void processType(String[] arr) {
        String typeStr = arr[0];
        String projectType = "";
        viewModel.setType_select(typeStr);
        if(typeStr != null && !typeStr.trim().equals("")) {
            projectType = "\"type\": {"+typeStr+"}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_TYPE, projectType);
    }

    //TODO: Compose the correct search filter result for Value and set it to setSearchFilterResult (needs a low and high value, two vars)
    private void processValue(String[] arr) {
        String min = arr[0];                          // int for query
        String max = arr[1];                          // int for query
        String valueStr = "$" + min + " - $" + max;   // text for display
        String projectValue = "";
        viewModel.setValue_select(valueStr);
        if(valueStr != null && !valueStr.trim().equals("")) {
            projectValue = "\"projectValue\": {"+ "\"min\":" + min + ",\"max\":" + max + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_VALUE, projectValue);
    }

    //TODO: Compose the correct search filter result for UpdatedWithin and set it to setSearchFilterResult
    private void processUpdatedWithin(String[] arr) {
        String updatedWithinStr = arr[0];   // text for display
        String updatedWithinInt = arr[1];   // int for query
        String projectUpdatedWithin = "";
        viewModel.setUpdated_within_select(updatedWithinStr);
        if(updatedWithinStr != null && !updatedWithinStr.trim().equals("")) {
            projectUpdatedWithin = "\"updatedInLast\": " + updatedWithinInt;
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_UPDATED_IN_LAST, projectUpdatedWithin);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //store the search filter result data to be used by the Search activity. 
        viewModel.saveResult();
    }
}
