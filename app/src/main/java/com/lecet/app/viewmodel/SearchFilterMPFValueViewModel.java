package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lecet.app.R;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterMPFValueViewModel extends BaseObservable {
    private AppCompatActivity activity;
    private String min="", max="";

    /**
     * Constructor
     */
    public SearchFilterMPFValueViewModel(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra("data", new String[]{min,max});
        activity.setResult(R.id.value & 0xfff,intent);
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
