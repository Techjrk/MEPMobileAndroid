package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lecet.app.R;
import com.lecet.app.databinding.ActivitySearchFilterMps30Binding;
import com.lecet.app.databinding.ActivitySearchFilterMseBinding;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;
import com.lecet.app.viewmodel.SearchFilterMSEViewModel;

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
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Toast.makeText(this, "SearchFilterMPF Activity Result request: "+requestCode+"  result: "+resultCode, Toast.LENGTH_SHORT).show();
        String[] info = null;
        switch (resultCode) {
            case R.id.location & 0xfff:
                info = data.getStringArrayExtra("data");
                Log.d("location:", "location: " + info.length + " : " + info[0]);
                String city = !info[0].trim().equals("") ? info[0] : "";
                String state = !info[1].trim().equals("") ? (!city.equals("") ? ", "+info[1] :" "+ info[1]) : "";
                String county = !info[2].trim().equals("") ? (!state.equals("") ? ", "+info[2] :" "+ info[2]) : "";
                String zip = !info[3].trim().equals("") ? (!county.equals("") ? ", "+info[3] :" "+ info[3]) : "";
                viewModel.setLocation_select(city+state+county+zip);
                break;
            case R.id.type & 0xfff:
                info = data.getStringArrayExtra("data");
                Log.d("value:", "value: " + info.length);
                viewModel.setType_select(info[0]);
                break;
            case R.id.value & 0xfff:
                info = data.getStringArrayExtra("data");
                Log.d("value:", "value: " + info.length + " min:" + info[0]);
                viewModel.setValue_select("$ " + info[0] + " - $" + info[1]);
                break;
            case R.id.updated_within & 0xfff:
                info = data.getStringArrayExtra("data");
                Log.d("value:", "value1: " + info[0] + " value2: " + info[1]);
                viewModel.setUpdated_within_select(info[0]);
                break;
            case R.id.bidding_within & 0xfff:
                info = data.getStringArrayExtra("data");
                Log.d("value:", "value1: " + info[0] + " value2: " + info[1]);
                viewModel.setBidding_within_select(info[0]);
                break;
            case R.id.bh & 0xfff:
                info = data.getStringArrayExtra("data");
                Log.d("value:", "value1: " + info[0]);
                viewModel.setBh_select(info[0]);
                break;
        }
    }
}
