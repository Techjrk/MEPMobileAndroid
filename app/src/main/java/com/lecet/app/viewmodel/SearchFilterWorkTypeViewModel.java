package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterWorkTypeViewModel extends BaseObservable {
    private AppCompatActivity activity;
    private String displayStr = "Any";
    private CheckBox lastChecked;

    private String[] worktype = {"", "",""};

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
        worktype[2] = String.valueOf(cb.getId());

        if (cb.isChecked()) {
            if (lastChecked != null) {
                lastChecked.setChecked(false);
            }
            lastChecked = cb;
        }  else {  //unchecked checkbox
            worktype[0]="";
            worktype[1]="";
            worktype[2]="";
            lastChecked=null;
        }
    }

    public CheckBox getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(CheckBox lastChecked) {
        this.lastChecked = lastChecked;
        if (lastChecked !=null) {
            lastChecked.setChecked(true);
            worktype[0] = lastChecked.getText().toString();
            worktype[1] = lastChecked.getTag().toString();
            worktype[2] = String.valueOf(lastChecked.getId());
        }
    }
}
