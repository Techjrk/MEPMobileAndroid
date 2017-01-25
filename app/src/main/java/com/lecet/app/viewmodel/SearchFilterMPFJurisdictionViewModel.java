package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.lecet.app.data.models.SearchFilterJurisdictionMain;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterMPFJurisdictionViewModel extends BaseObservable {
    private AppCompatActivity activity;

    private RealmResults<SearchFilterJurisdictionMain> realmJurisdictions;
    private String[] jurisdictionExtra;  // = {"", ""};

    /**
     * Constructor
     */
    public SearchFilterMPFJurisdictionViewModel(AppCompatActivity activity) {
        this.activity = activity;
        getJurisdictions();
        jurisdictionExtra = new String[2];
    }

    /**
     * Read Realm Jurisdiction data
     */
    private void getJurisdictions() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmJurisdictions = realm.where(SearchFilterJurisdictionMain.class).findAll();
                Log.d("SearchFilterMPFJurisVM:", "realmJurisdictions size: " + realmJurisdictions.size());
                Log.d("SearchFilterMPFJurisVM:", "realmJurisdictions list: " + realmJurisdictions);
            }
        });
    }

    public void onClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, jurisdictionExtra);
//        activity.setResult(R.id.jurisdiction & 0xfff, intent);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public void onSelected(View view) {
        jurisdictionExtra[0] = ((CheckBox) view).getText().toString();  //TODO - Check these values
        jurisdictionExtra[1] = (String) view.getTag();
        Log.d("checkbox", "checkbox:" + ((CheckBox) view).getText().toString());
    }

    public void setJurisdictionExtraName(String str) {
        this.jurisdictionExtra[0] = str;
    }

    public RealmResults<SearchFilterJurisdictionMain> getRealmJurisdictions() {
        return realmJurisdictions;
    }
}
