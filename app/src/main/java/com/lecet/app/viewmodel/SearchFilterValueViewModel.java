package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterValueViewModel extends BaseObservable {
    private AppCompatActivity activity;
    private String min = "", max = "";

    /**
     * Constructor
     */
    public SearchFilterValueViewModel(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, new String[]{min, max});
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    @Bindable
    public String getMin() {
        return min;
    }

    @Bindable
    public void setMin(String min) {
        this.min = min;
    }

    @Bindable
    public String getMax() {
        return max;
    }

    @Bindable
    public void setMax(String max) {
        this.max = max;
    }
}
