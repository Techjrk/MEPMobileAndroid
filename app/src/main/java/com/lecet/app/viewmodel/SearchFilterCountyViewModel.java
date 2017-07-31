package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.adapters.SearchFilterCountyAdapter;
import com.lecet.app.contentbase.BaseObservableViewModel;
import com.lecet.app.data.models.County;
import com.lecet.app.domain.CountyDomain;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by getdevs on 05/07/2017.
 */

public class SearchFilterCountyViewModel extends BaseObservableViewModel {
    private CountyDomain countyDomain;
    private AppCompatActivity appCompatActivity;
    private String state;
    private SearchFilterCountyAdapter adapter;
    private String fipsCounty;
    public SearchFilterCountyViewModel(AppCompatActivity appCompatActivity, CountyDomain countyDomain , String state , String fipsCounty) {
        super(appCompatActivity);
        this.countyDomain = countyDomain;
        this.appCompatActivity = appCompatActivity;
        this.state = state;
        this.fipsCounty = fipsCounty;
        getCounty(state);
    }
    public void getCounty(String state){
        //TODO check first database if search in the said state already done : check database


        Call<List<County>> call = countyDomain.getCountyInAState(state);
        call.enqueue(new Callback<List<County>>() {
            @Override
            public void onResponse(Call<List<County>> call, Response<List<County>> response) {
                if(response.isSuccessful()){
                    List<County> counties = response.body();
                    if(counties != null && counties.size() > 0){
                        initRecyclerView(counties);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<County>> call, Throwable t) {

            }
        });
    }

    private void initRecyclerView(List<County> counties) {


        RecyclerView recycler_county = (RecyclerView)appCompatActivity.findViewById(R.id.recycler_county);
        adapter = new SearchFilterCountyAdapter(counties);
        for(County county : counties){
            if(county.getFipsCountyId().equals(fipsCounty)){
                county.setSelected(true);
                adapter.setSelectedCounty(county);
                break;
            }
        }
        recycler_county.setLayoutManager(new LinearLayoutManager(appCompatActivity));
        recycler_county.setAdapter(adapter);
    }
    public void apply(View v){
        if(adapter.getSelectedCounty() != null){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();

            bundle.putString("display", adapter.getSelectedCounty().getCountyName());
            bundle.putString("id",adapter.getSelectedCounty().getFipsCountyId());
            intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE, bundle);

            appCompatActivity.setResult(Activity.RESULT_OK ,intent);
            appCompatActivity.finish();
        }
        else{
            cancel(v);
        }

    }
    public void cancel(View v){
        appCompatActivity.setResult(Activity.RESULT_CANCELED);
        appCompatActivity.finish();
    }
}
