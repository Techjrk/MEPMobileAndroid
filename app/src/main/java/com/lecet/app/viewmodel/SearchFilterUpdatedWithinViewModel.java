package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.lecet.app.BR;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterUpdatedWithinViewModel extends BaseObservable {
    private AppCompatActivity activity;
    public static final String BUNDLE_KEY_DISPLAY_STR = "com.lecet.app.viewmodel.SearchFilterUpdatedWithinViewModel.display.displayText.extra";
    public static final String BUNDLE_KEY_DAYS_INT = "com.lecet.app.viewmodel.SearchFilterUpdatedWithinViewModel.display.daysInt.extra";
    private String displayStr = "Any";
    private String daysInt = "0";
    private Bundle bundle;

    /**
     * Constructor
     */
    public SearchFilterUpdatedWithinViewModel(AppCompatActivity activity) {
        this.activity = activity;
        bundle = new Bundle();
    }

    public void onApplyButtonClick(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE, bundle);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public void onCancelButtonClick(View view) {
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }

    public void onSelected(View view) {
        String displayStr = ((RadioButton) view).getText().toString();
        String daysInt = (String) view.getTag();
        setUpdatedWithinData(displayStr, daysInt);
    }

    public void setUpdatedWithinData(String displayStr, String daysInt) {
        setDisplayStr(displayStr);
        setDaysInt(daysInt);
        // overwrite the Bundle instance with each selection since Bidding Within only supports single-selection
        bundle = new Bundle();
        setBundleData(BUNDLE_KEY_DISPLAY_STR, this.displayStr);
        setBundleData(BUNDLE_KEY_DAYS_INT, this.daysInt);
    }

    private void setBundleData(String key, String value) {
        bundle.putString(key, value);
    }

    @Bindable
    public String getDisplayStr() {
        return displayStr;
    }

    public void setDisplayStr(String displayStr) {
        this.displayStr = displayStr;
    }

    @Bindable
    public String getDaysInt() {
        return daysInt;
    }

    public void setDaysInt(String daysInt) {
        this.daysInt = daysInt;
        notifyPropertyChanged(BR.daysInt);
    }
}
