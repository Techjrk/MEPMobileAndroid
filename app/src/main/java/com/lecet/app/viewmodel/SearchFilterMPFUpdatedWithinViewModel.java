package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.lecet.app.R;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterMPFUpdatedWithinViewModel extends BaseObservable {
    private AppCompatActivity activity;
    private String[] time = {"Any", "0"};

    /**
     * Constructor
     */
    public SearchFilterMPFUpdatedWithinViewModel(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra("data", time);
        activity.setResult(R.id.updated_within & 0xfff, intent);
        activity.finish();
    }

    public void onSelected(View view) {
        time[0] = ((RadioButton) view).getText().toString();
        time[1] = (String) view.getTag();
    }
}
