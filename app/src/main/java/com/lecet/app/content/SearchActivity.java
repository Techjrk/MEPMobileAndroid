package com.lecet.app.content;

import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivitySearchBinding;
import com.lecet.app.domain.SearchDomain;
import com.lecet.app.viewmodel.SearchViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.realm.Realm;

public class SearchActivity extends AppCompatActivity {
    private final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

       // setContentView(R.layout.activity_search);
        setupBinding();
    }

    private void setupBinding() {
        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        SearchViewModel viewModel = new SearchViewModel(this,
                new SearchDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance()));
        binding.setViewModel(viewModel);
    } //end of setupBinding

}

