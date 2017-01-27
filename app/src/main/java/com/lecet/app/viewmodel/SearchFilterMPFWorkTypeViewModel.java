package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterMPFWorkTypeViewModel extends BaseObservable {
    private AppCompatActivity activity;

    private String[] worktype = {"", ""};

    /**
     * Constructor
     */
    public SearchFilterMPFWorkTypeViewModel(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, worktype);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public void onSelected(View view) {
        worktype[0] = ((CheckBox) view).getText().toString();
    }
}
