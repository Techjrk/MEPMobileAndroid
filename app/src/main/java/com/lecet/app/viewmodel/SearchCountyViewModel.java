package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.contentbase.BaseObservableViewModel;
import com.lecet.app.data.models.County;

/**
 * Created by getdevs on 05/07/2017.
 */

public class SearchCountyViewModel extends BaseObservable {
    private County county;
    public SearchCountyViewModel(County county) {
        this.county = county;
    }
    public County getCounty(){
        return county;
    }
}
