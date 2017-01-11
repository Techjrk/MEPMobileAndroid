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

import java.util.ArrayList;
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

        String[] info = data.getStringArrayExtra("data");
        if (info != null)
            switch (resultCode) {

                // Location
                case R.id.location & 0xfff:
                    processLocation(info);
                    break;

                // Type
                case R.id.type & 0xfff:
                    //processPrimaryProjectType(info);
                    processProjectTypeId(info);
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
                    processJurisdiction(info);
                    break;

                // Project Stage
                case R.id.stage & 0xfff:
                    processStage(info);
                    break;

                // Bidding Within
                case R.id.bidding_within & 0xfff:
                    processBiddingWithin(info);
                    break;

                // Building / Highway
                case R.id.bh & 0xfff:
                    processBuildingOrHighway(info);
                    break;

                // Owner Type
                case R.id.ownertype & 0xfff:
                    processOwnerType(info);
                    break;

                // Work Type
                case R.id.worktype & 0xfff:
                    processWorkType(info);
                    break;

                default:
                    Log.w("SearchFilterMPSAct", "onActivityResult: WARNING: no case for resultCode " + resultCode);
                    break;
            }
    }

    /**
     * Process the Location input data
     */
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
        StringBuilder sb = new StringBuilder();
        String projectLocation = "";

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

    /**
     * Process the Primary Project Type input data
     * TODO: Use in conjunction with processProjectTypeId()
     */
    private void processPrimaryProjectType(String[] arr) {
        String typeStr = arr[0];
        String projectType = "";
        viewModel.setType_select(typeStr);
        if(typeStr != null && !typeStr.trim().equals("")) {
            projectType = "\"primaryProjectType\":{" + typeStr + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_TYPE, projectType);
    }

    /**
     * Process the Project Type Id code based on input data from list
     * TODO - HARD-CODED. Get from map of project categories mapped to type ID codes **********
     */
    private void processProjectTypeId(String[] arr) {
        String typeIdStr = arr[0];
        String projectTypeId = "";
        viewModel.setType_select(typeIdStr);    //TODO - this is the same var set by processPrimaryProjectType
        if(typeIdStr != null && !typeIdStr.trim().equals("")) {
            List<Integer> idList = new ArrayList<>();
            idList.add(501);
            idList.add(502);
            idList.add(503);
            String ids = idList.toString();
            projectTypeId = "\"projectTypeId\":{\"inq\":" + ids + "}";         // square brackets [ ] come for free when the list is converted to a String
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_TYPE_ID, projectTypeId);
    }

    /**
     * Process the dollar Value from input data
     */
    private void processValue(String[] arr) {
        String min = arr[0];                          // int for query
        String max = arr[1];                          // int for query
        String valueStr = "$" + min + " - $" + max;   // text for display
        String projectValue = "";
        viewModel.setValue_select(valueStr);
        if(valueStr != null && !valueStr.trim().equals("")) {
            projectValue = "\"projectValue\":{"+ "\"min\":" + min + ",\"max\":" + max + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_VALUE, projectValue);
    }

    /**
     * Process the Updated Within input data
     */
    private void processUpdatedWithin(String[] arr) {
        String updatedWithinStr = arr[0];   // text for display
        String updatedWithinInt = arr[1];   // int for query
        String projectUpdatedWithin = "";
        viewModel.setUpdated_within_select(updatedWithinStr);
        if(updatedWithinStr != null && !updatedWithinStr.trim().equals("")) {
            projectUpdatedWithin = "\"updatedInLast\":" + updatedWithinInt;
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_UPDATED_IN_LAST, projectUpdatedWithin);
    }

    /**
     * Process the Jurisdiction input data
     * TODO - jurisdiction search may require "jurisdiction":true as well as "jurisdictions":{"inq":[array]} and "deepJurisdictionId":[ids]
     * TODO - also we need to map input to jurisdiction codes. Hard-coded for now.
     */
    private void processJurisdiction(String[] arr) {
        String jurisdiction = arr[0];
        String jurisdictions = "";
        viewModel.setJurisdiction_select(jurisdiction);
        if(jurisdiction != null && !jurisdiction.trim().equals("")) {
            List<String> jList = new ArrayList<>();
            jList.add("\"Eastern\"");
            jList.add("\"New Jersey\"");
            jList.add("\"3\"");
            String js = jList.toString();
            jurisdictions = "\"jurisdictions\":{\"inq\":" + js + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_JURISDICTION, jurisdictions);
    }

    /**
     * Process the Stage input data
     * TODO - map project Stage from input. Hard-coded for now.
     */
    private void processStage(String[] arr) {
        String stageStr = arr[0];
        String stages = "";
        viewModel.setStage_select(stageStr);
        if(stageStr != null && !stageStr.trim().equals("")) {
            List<Integer> sList = new ArrayList<>();
            sList.add(203);
            sList.add(201);
            sList.add(206);
            stages = "\"projectStageId\":{\"inq\":" + sList.toString() + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_STAGE, stages);
    }

    /**
     * Process the Bidding Within input data, which is an int, # of days
     */
    private void processBiddingWithin(String[] arr) {
        String biddingWithinStr = arr[0];   // text for display
        String biddingWithinInt = arr[1];   // int for query
        String projectBiddingWithin = "";
        viewModel.setBidding_within_select(biddingWithinStr);
        if(biddingWithinStr != null && !biddingWithinStr.trim().equals("")) {
            projectBiddingWithin = "\"biddingInNext\":" + biddingWithinInt;
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_BIDDING_WITHIN, projectBiddingWithin);
    }

    /**
     * Process the Building-or-Highway input data, which is an array of one or two elements
     */
    private void processBuildingOrHighway(String[] arr) {
        String bhStr = arr[0];      // could come in as "Both", "Building" or "Heavy-Highway", to be converted to array ["B"] or ["H"] or ["B","H"]
        String bh = "";
        viewModel.setBh_select(bhStr);
        if(bhStr != null && !bhStr.trim().equals("")) {
            List<String> bhList = new ArrayList<>();
            if(bhStr.equals("Building")) bhList.add("\"B\"");
            else if(bhStr.equals("Heavy-Highway")) bhList.add("\"H\"");
            else {
                bhList.add("\"B\"");
                bhList.add("\"H\"");
            }
            bh = "\"buildingOrHighway\":{\"inq\":" + bhList.toString() + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_BUILDING_OR_HIGHWAY, bh);
    }

    /**
     * Process the Owner Type input data, which is a String array with one element
     * TODO - make sure UI only allows single selection, as per iOS
     */
    private void processOwnerType(String[] arr) {
        String ownerTypeStr = arr[0];
        String ownerType = "";
        viewModel.setOwner_type_select(ownerTypeStr);
        if(ownerTypeStr != null && !ownerTypeStr.trim().equals("")) {
            List<String> oList = new ArrayList<>();
            oList.add("\"" + ownerTypeStr + "\"");
            ownerType = "\"ownerType\":{\"inq\":" + oList.toString() + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_OWNER_TYPE, ownerType);
    }

    /**
     * Process the Work Type input data, which is a String array with one element
     * TODO - make sure UI only allows single selection, as per iOS
     * TODO - work types need to be mapped to integer IDs
     */
    private void processWorkType(String[] arr) {
        String workTypeStr = arr[0];
        String workType = "";
        viewModel.setWork_type_select(workTypeStr);
        if(workTypeStr != null && !workTypeStr.trim().equals("")) {
            List<String> wList = new ArrayList<>();
            wList.add("\"" + workTypeStr + "\"");
            workType = "\"workTypeId\":{\"inq\":" + wList.toString() + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_WORK_TYPE, workType);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //store the search filter result data to be used by the Search activity. 
        viewModel.saveResult();
    }
}
