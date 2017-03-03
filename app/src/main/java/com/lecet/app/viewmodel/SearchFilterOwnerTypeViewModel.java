package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterOwnerTypeViewModel extends BaseObservable {
    private AppCompatActivity activity;
    private String displayStr = "Any";
    private CheckBox lastChecked;

    private String[] ownertype = {"", ""};
    //private CheckBox checkView;
    /**
     * Constructor
     */
    public SearchFilterOwnerTypeViewModel(AppCompatActivity activity) {

        this.activity = activity;
        if (lastChecked != null) {
            CheckBox cb = (CheckBox)  activity.findViewById(lastChecked.getId())  ;
            cb.setChecked(true);
           lastChecked = cb;
        //    lastChecked.notify();
       //     Log.d("lastchecked","lastchecked"+lastChecked);
        }

    }

    public void onApplyButtonClick(View view) {
        Intent intent = activity.getIntent();
        //intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE, bundle);
         intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, ownertype);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public void onCancelButtonClick(View view) {
        activity.setResult(Activity.RESULT_CANCELED);
     //   lastChecked=null;
        activity.finish();
    }
    
    public void onSelected(View view) {

        CheckBox cb = (CheckBox) view;
        //        ownertype[0] = ((CheckBox) view).getText().toString();
        displayStr = cb.getText().toString();
        ownertype[0] = displayStr;
        if (cb.isChecked()) {
            if (lastChecked != null) {
                lastChecked.setChecked(false);
               // checkLastSelect(false);
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
