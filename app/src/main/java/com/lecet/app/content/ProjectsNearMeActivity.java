package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.lecet.app.R;
import com.lecet.app.content.widget.LacetInfoWindowAdapter;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityProjectsNearMeBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectsNearMeViewModel;

import io.realm.Realm;

public class ProjectsNearMeActivity extends AppCompatActivity implements OnMapReadyCallback {

    ActivityProjectsNearMeBinding binding;
    ProjectsNearMeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupBinding();
        setupToolbar();
        setupMap();
    }

    private void setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_projects_near_me);
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        viewModel = new ProjectsNearMeViewModel(this, projectDomain);
        binding.setViewModel(viewModel);

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = getLayoutInflater();
            View searchBarView = inflater.inflate(R.layout.projects_near_me_search_bar_layout, null);
            actionBar.setCustomView(searchBarView);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    private void setupMap() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.map_container, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setInfoWindowAdapter(new LacetInfoWindowAdapter(this));
        LatLng dummyLatLong = new LatLng(34.7364493, -86.5501654);
        viewModel.setMap(map);
        viewModel.fetchProjectsNearMe(dummyLatLong);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
