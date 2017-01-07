package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterMps30Binding;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

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
        //       arr = data.getStringArrayExtra("data");
        //       Log.d("location:", "location: " + arr.length + " : " + arr[0]);
        String city = !arr[0].trim().equals("") ? arr[0] : "";
        String state = !arr[1].trim().equals("") ? (!city.equals("") ? ", " + arr[1] : " " + arr[1]) : "";       //TODO - strip comma and validate state
        String county = !arr[2].trim().equals("") ? (!state.equals("") ? ", " + arr[2] : " " + arr[2]) : "";
        String zip = !arr[3].trim().equals("") ? (!county.equals("") ? ", " + arr[3] : " " + arr[3]) : "";
        viewModel.setLocation_select(city + state + county + zip);
//                    String projectLocation = "\"projectLocation\": {";
        String projectLocation="";
        projectLocation += ( !arr[0].equals("") ? "\"zip5\":\"" + arr[0] + "\"":"");
        projectLocation += ( !arr[1].equals("") ? (!arr[0].equals("") ? ",\"city\":\"" + arr[1] + "\"" : "\"city\":\"" + arr[1] + "\""):"");
        projectLocation += ( !arr[2].equals("") ? (!arr[0].equals("") || !arr[1].equals("") ? ",\"county\":\"" + arr[2] + "\"" : "\"county\":\"" + arr[2] + "\""):"");
//                    projectLocation += ( !arr[2].equals("") ? "\"county\":\"" + arr[2] + "\",":"");
        projectLocation += ( !arr[3].equals("") ? (!arr[0].equals("") || !arr[1].equals("") || !arr[2].equals("")  ? ",\"state\":\"" + arr[3] + "\"" : "\"state\":\"" + arr[3] + "\""):"");
//                    projectLocation += ( !arr[3].equals("") ? "\"state\":\"" + arr[3] + "\"}":"}");
        if (!projectLocation.trim().equals("")) {
            projectLocation = "\"projectLocation\": {"+projectLocation+"}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_LOCATION, projectLocation);  // this should work whether or not projectLocation is empty

        /*else {
            viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_LOCATION, "");
        }*/
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
