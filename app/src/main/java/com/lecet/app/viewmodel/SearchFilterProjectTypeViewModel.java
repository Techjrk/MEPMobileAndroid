package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lecet.app.data.models.SearchFilterProjectTypesMain;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * View Model for Search Filter Activity: Project Type
 * Created by getdevsinc on 12/29/16.
 */
public class SearchFilterProjectTypeViewModel extends BaseObservable {
    private final String TAG = "SearchFilterProjTypeVM";

    private AppCompatActivity activity;
    private Bundle bundle;
    private RealmResults<SearchFilterProjectTypesMain> realmProjectTypes;

    /**
     * Constructor
     */
    public SearchFilterProjectTypeViewModel(AppCompatActivity activity) {
        this.activity = activity;
        bundle = new Bundle();
        getProjectTypes();
    }

    /**
     * Read Realm SearchFilterProjectTypesMain data
     */
    private void getProjectTypes() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmProjectTypes = realm.where(SearchFilterProjectTypesMain.class).findAll();
                Log.d(TAG, "realmProjectTypes size: " + realmProjectTypes.size());
                Log.d(TAG, "realmProjectTypes list: " + realmProjectTypes);
            }
        });
    }

    /**
     * Apply the filter and return to the main Search activity
     */
    public void onApplyButtonClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE, bundle);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public RealmResults<SearchFilterProjectTypesMain> getRealmProjectTypes() {
        return realmProjectTypes;
    }

    public void addProjectTypeData(String key, String value) {
        bundle.putString(key, value);
    }

    public void removeProjectTypeData(String key) {
        bundle.remove(key);
    }
}
