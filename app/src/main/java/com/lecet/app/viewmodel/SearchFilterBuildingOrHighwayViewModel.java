package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterBuildingOrHighwayViewModel extends BaseObservable {
    private AppCompatActivity activity;
    public static final String BUNDLE_KEY_DISPLAY_STR = "com.lecet.app.viewmodel.SearchFilterBuildingOrHighwayViewModel.displayText.extra";
    public static final String BUNDLE_KEY_TAG = "com.lecet.app.viewmodel.SearchFilterBuildingOrHighwayViewModel.tag.extra";
    private String strBH;
    private String tagBH;
    private Bundle bundle;

    /**
     * Constructor
     */
    public SearchFilterBuildingOrHighwayViewModel(AppCompatActivity activity) {
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
        String str = ((RadioButton) view).getText().toString();
//        if (str.equals("Both")) str = SearchFilterMPFViewModel.ANY;
        setStrBH(str);
        setTagBH((String) view.getTag());
        setBHData(getStrBH(), getTagBH());
    }

    public void setBHData(String str, String tag) {
        bundle = new Bundle();
        setBundleData(BUNDLE_KEY_DISPLAY_STR, str);
        setBundleData(BUNDLE_KEY_TAG, tag);
    }

    private void setBundleData(String key, String value) {
        bundle.putString(key, value);
    }

    @Bindable
    public String getStrBH() {
        return strBH;
    }

    public void setStrBH(String strBH) {
        this.strBH = strBH;
    }

    @Bindable
    public String getTagBH() {
        return tagBH;
    }

    public void setTagBH(String tagBH) {
        this.tagBH = tagBH;
    }
}
