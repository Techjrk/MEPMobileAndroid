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

public class SearchFilterWorkTypeViewModel extends BaseObservable {
    private AppCompatActivity activity;
    private String displayStr = "Any";
    private CheckBox lastChecked;

    private String[] worktype = {"", ""};

    /**
     * Constructor
     */
    public SearchFilterWorkTypeViewModel(AppCompatActivity activity) {

        this.activity = activity;
        if (lastChecked != null) {
            CheckBox cb = (CheckBox) activity.findViewById(lastChecked.getId());
            cb.setChecked(true);
            lastChecked = cb;
        }
    }

    public void onApplyButtonClick(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, worktype);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public void onCancelButtonClick(View view) {
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }

    public void onSelected(View view) {
        CheckBox cb = (CheckBox) view;
        displayStr = cb.getText().toString();
        worktype[0] = displayStr;
        worktype[1] = ((CheckBox) view).getTag().toString();

        if (cb.isChecked()) {
            if (lastChecked != null) {
                lastChecked.setChecked(false);
            }
            lastChecked = cb;
        }
    }

    public CheckBox getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(CheckBox lastChecked) {
        this.lastChecked = lastChecked;
    }
}
