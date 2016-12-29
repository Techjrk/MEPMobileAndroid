package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lecet.app.R;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterMPFLocationViewModel extends BaseObservable{
    private String city="";
    private String state="";
    private String county="";
    private String zipcode="";

    private AppCompatActivity activity;

    /**
     * Constructor
     */
    public SearchFilterMPFLocationViewModel(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra("data",new String[]{getCity(),getState(),getCounty(),getZipcode()});
        activity.setResult(R.id.location & 0xfff,intent);
        activity.finish();
    }
    /**
     * Bindable
     *
     */
    @Bindable
    public String getCity() {
        return city;
    }

    @Bindable
    public void setCity(String city) {
        this.city = city;
    }

    @Bindable
    public String getState() {
        return state;
    }

    @Bindable
    public void setState(String state) {
        this.state = state;
    }

    @Bindable
    public String getCounty() {
        return county;
    }

    @Bindable
    public void setCounty(String county) {
        this.county = county;
    }

    @Bindable
    public String getZipcode() {
        return zipcode;
    }

    @Bindable
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
