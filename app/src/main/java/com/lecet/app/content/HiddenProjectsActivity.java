package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityHiddenProjectsBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.HiddenProjectsViewModel;

import io.realm.Realm;

public class HiddenProjectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityHiddenProjectsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_hidden_projects);
        HiddenProjectsViewModel viewModel = new HiddenProjectsViewModel(this, LecetSharedPreferenceUtil.getInstance(this).getId(),
                new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance()));
        binding.setViewModel(viewModel);
        setupToolbar(viewModel, getString(R.string.hidden_projects_title));
    }

    private void setupToolbar(HiddenProjectsViewModel viewModel, String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = getLayoutInflater();

            View tb = inflater.inflate(R.layout.include_app_bar_layout_save, null);

            viewModel.setToolbar(tb, title, "");

            actionBar.setCustomView(tb);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }
}
