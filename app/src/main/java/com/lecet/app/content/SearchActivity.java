package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivitySearchBinding;
import com.lecet.app.databinding.ListItemSearchQuerySummaryBinding;
import com.lecet.app.domain.SearchDomain;
import com.lecet.app.viewmodel.SearchViewModel;

import io.realm.Realm;

public class SearchActivity extends AppCompatActivity {
    private final String TAG = "SearchActivity";
    SearchViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(TAG, "onCreate");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setupBinding();
    }

    private void setupBinding() {
        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        viewModel = new SearchViewModel(this,
                new SearchDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance()));
        binding.setViewModel(viewModel);
    }

    @Override
    public void onBackPressed() {
        viewModel.checkDisplayMSESectionOrMain();
    }

}

