package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivitySearchFilterCountyBinding;
import com.lecet.app.databinding.ActivitySearchFilterWorkTypeBinding;
import com.lecet.app.domain.CountyDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.SearchFilterAllTabbedViewModel;
import com.lecet.app.viewmodel.SearchFilterCountyViewModel;

import io.realm.Realm;

/**
 * Created by getdevs on 05/07/2017.
 */

public class SearchFilterCountyActivity extends LecetBaseActivity{
    private SearchFilterCountyViewModel viewModel;
    public static final int REQUEST_COUNTY = 10;
    public static final String REQUEST_STATE_EXTRA = "requestState";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchFilterCountyBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_county);
        CountyDomain countyDomain = new CountyDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        String state = getIntent().getStringExtra(REQUEST_STATE_EXTRA);
        if(!TextUtils.isEmpty(state)){
            viewModel = new SearchFilterCountyViewModel(this , countyDomain , state);
            sfilter.setViewModel(viewModel);
        }
        setUpToolbar();

    }
    private void setUpToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(false);
        }
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }
}
