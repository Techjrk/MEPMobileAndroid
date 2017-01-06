package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterMps30Binding;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;

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
                case R.id.location & 0xfff:
                    //       info = data.getStringArrayExtra("data");
                    //       Log.d("location:", "location: " + info.length + " : " + info[0]);
                    String city = !info[0].trim().equals("") ? info[0] : "";
                    String state = !info[1].trim().equals("") ? (!city.equals("") ? ", " + info[1] : " " + info[1]) : "";
                    String county = !info[2].trim().equals("") ? (!state.equals("") ? ", " + info[2] : " " + info[2]) : "";
                    String zip = !info[3].trim().equals("") ? (!county.equals("") ? ", " + info[3] : " " + info[3]) : "";
                    viewModel.setLocation_select(city + state + county + zip);
//                    String projectLocation = "\"projectLocation\": {";
                    String projectLocation="";
                    projectLocation+=( !info[0].equals("") ? "\"zip5\":\"" + info[0] + "\"":"");
                    projectLocation+=( !info[1].equals("") ? (!info[0].equals("") ? ",\"city\":\"" + info[1] + "\"" : "\"city\":\"" + info[1] + "\""):"");
                    projectLocation+=( !info[2].equals("") ? (!info[0].equals("") || !info[1].equals("") ? ",\"county\":\"" + info[2] + "\"" : "\"county\":\"" + info[2] + "\""):"");
//                    projectLocation+=( !info[2].equals("") ? "\"county\":\"" + info[2] + "\",":"");
                    projectLocation+=( !info[3].equals("") ? (!info[0].equals("") || !info[1].equals("") || !info[2].equals("")  ? ",\"state\":\"" + info[3] + "\"" : "\"state\":\"" + info[3] + "\""):"");
//                    projectLocation+=( !info[3].equals("") ? "\"state\":\"" + info[3] + "\"}":"}");
                    if ( !projectLocation.trim().equals("")) {
                      projectLocation = "\"projectLocation\": {"+projectLocation+"}";
                         viewModel.setSearchFilterResult("projectLocation", projectLocation);
                    } else  viewModel.setSearchFilterResult("projectLocation", "");
                    break;
                case R.id.type & 0xfff:
                    //TODO: Compose the correct search filter result for Type and set it to setSearchFilterResult
                    viewModel.setType_select(info[0]);
                    break;
                case R.id.value & 0xfff:
                    //TODO: Compose the correct search filter result for Value and set it to setSearchFilterResult
                    viewModel.setValue_select("$ " + info[0] + " - $" + info[1]);
                    break;
                case R.id.updated_within & 0xfff:
                    //TODO: Compose the correct search filter result for UpdatedWithin and set it to setSearchFilterResult
                    viewModel.setUpdated_within_select(info[0]);
                    break;
                case R.id.jurisdiction & 0xfff:
                    //TODO: Compose the correct search filter result for Jurisdiction and set it to setSearchFilterResult
                    viewModel.setJurisdiction_select(info[0]);
                    break;
                case R.id.stage & 0xfff:
                    //TODO: Compose the correct search filter result for Stage and set it to setSearchFilterResult
                    viewModel.setStage_select(info[0]);
                    break;
                case R.id.bidding_within & 0xfff:
                    //TODO: Compose the correct search filter result for Bidding Within and set it to setSearchFilterResult
                    viewModel.setBidding_within_select(info[0]);
                    break;
                case R.id.bh & 0xfff:
                    //TODO: Compose the correct search filter result for BH and set it to setSearchFilterResult
                    viewModel.setBh_select(info[0]);
                    break;
                case R.id.ownertype & 0xfff:
                    //TODO: Compose the correct search filter result for OwnerType and set it to setSearchFilterResult
                    viewModel.setOwner_type_select(info[0]);
                    break;
                case R.id.worktype & 0xfff:
                    //TODO: Compose the correct search filter result for WorkType and set it to setSearchFilterResult
                    viewModel.setWork_type_select(info[0]);
                    break;
            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //store the search filter result data to be used by the Search activity. 
        viewModel.saveResult();
    }
}
