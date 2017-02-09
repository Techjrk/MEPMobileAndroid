package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterUpdatedWithinViewModel extends BaseObservable {
    private AppCompatActivity activity;

    private String[] time = {"Any", "0"};

    /**
     * Constructor
     */
    public SearchFilterUpdatedWithinViewModel(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onApplyButtonClick(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, time);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public void onCancelButtonClick(View view) {
        activity.setResult(Activity.RESULT_CANCELED);   //TODO - add reset of selection
        activity.finish();
    }

    public void onSelected(View view) {
        time[0] = ((RadioButton) view).getText().toString();
        time[1] = (String) view.getTag();
    }

    @Bindable
    public String[] getTime() {
        return time;
    }

    public void setTime(String[] time) {
        this.time = time;
    }


}
