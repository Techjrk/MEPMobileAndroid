package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterBuildingOrHighwayViewModel extends BaseObservable {
    private AppCompatActivity activity;
    private String[] bh = {"Any", "0"};

    /**
     * Constructor
     */
    public SearchFilterBuildingOrHighwayViewModel(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, bh);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public void onSelected(View view) {
        bh[0] = ((RadioButton) view).getText().toString();

    }
}
