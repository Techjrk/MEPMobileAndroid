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

public class SearchFilterOwnerTypeViewModel extends BaseObservable {
    private AppCompatActivity activity;
    private String displayStr = "Any";
    private CheckBox lastChecked;
    private String[] ownertype = {"", ""};

    /**
     * Constructor
     */
    public SearchFilterOwnerTypeViewModel(AppCompatActivity activity) {

        this.activity = activity;
        if (lastChecked != null) {
            CheckBox cb = (CheckBox) activity.findViewById(lastChecked.getId());
            cb.setChecked(true);
            lastChecked = cb;
        }
    }

    public void onApplyButtonClick(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, ownertype);
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
        ownertype[0] = displayStr;
        ownertype[1] = String.valueOf(cb.getId());
       // Log.d("checkselected","checkselected"+cb.isChecked());
        if (cb.isChecked()) {
            if (lastChecked != null) {
                lastChecked.setChecked(false);

            }
            lastChecked = cb;
        } else {  //unchecked checkbox
            ownertype[0]="";
            ownertype[1]="";
            lastChecked=null;
          //  Log.d("empty","empty"+ownertype[1]);
        }
    }

    public CheckBox getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(CheckBox lastChecked) {
        this.lastChecked = lastChecked;
        if (lastChecked !=null) {
            lastChecked.setChecked(true);
            ownertype[0] = lastChecked.getText().toString();
            ownertype[1] = String.valueOf(lastChecked.getId());
        }
    }
}
