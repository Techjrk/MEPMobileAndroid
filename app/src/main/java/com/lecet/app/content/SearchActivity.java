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
     * Handle the result of all filters being applied and make the filtered query.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //TODO - make filtered data call

        Toast.makeText(this, "Search Activity Result request 1: " + requestCode + "  result: " + resultCode, Toast.LENGTH_SHORT).show();
        String projectLocation = data.getStringExtra("projectLocation");
        Log.d("projectLocation", "projectLocation = " + projectLocation);
        searchFilter = "{" + projectLocation + "}";
        Log.d("searchFilter", "searchFilter = " + projectLocation);
        //TODO set the result filter to the domain...
        viewModel.setProjectSearchFilter("{\"include\":[\"primaryProjectType\",\"secondaryProjectTypes\",\"bids\",\"projectStage\"],\"searchFilter\":" + searchFilter + "}");
        viewModel.updateViewQuery();
        viewModel.setSeeAll(viewModel.getSeeAllForResult());
    }

}

