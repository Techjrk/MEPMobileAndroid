package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.lecet.app.R;
import com.lecet.app.domain.SearchDomain;


/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterMPFJurisdictionViewModel extends BaseObservable {
    private AppCompatActivity activity;
    private SearchDomain searchDomain;
    private String[] jurisdiction = {"", ""};

    /**
     * Constructor
     */
    public SearchFilterMPFJurisdictionViewModel(AppCompatActivity activity, SearchDomain sd) {
        this.activity = activity;
        this.searchDomain = sd;
    }
    public SearchFilterMPFJurisdictionViewModel(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, jurisdiction);
        activity.setResult(R.id.jurisdiction & 0xfff, intent);
        activity.finish();
    }

    public void onSelected(View view) {
        jurisdiction[0] = ((CheckBox) view).getText().toString();
        Toast.makeText(activity, ""+jurisdiction[0], Toast.LENGTH_SHORT).show();
//        time[1] = (String)view.getTag();
    }
}
