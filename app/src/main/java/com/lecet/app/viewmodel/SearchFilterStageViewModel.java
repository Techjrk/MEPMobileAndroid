package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lecet.app.data.models.SearchFilterStagesMain;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * View Model for Search Filter Activity: Stage
 */
public class SearchFilterStageViewModel extends BaseObservable {
    private static final String TAG = "SearchFilterStageVM";
    public static final String BUNDLE_KEY_VIEW_TYPE = "viewType";
    public static final String BUNDLE_KEY_ID = "id";
    public static final String BUNDLE_KEY_NAME = "name";

    private AppCompatActivity activity;

    private Bundle bundle;
    private RealmResults<SearchFilterStagesMain> realmStages;

    /**
     * Constructor
     */
    public SearchFilterStageViewModel(AppCompatActivity activity) {
        this.activity = activity;
        bundle = new Bundle();
        getProjectStages();
    }

    /**
     * Read Realm ProjectStage data
     */
    private void getProjectStages() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmStages = realm.where(SearchFilterStagesMain.class).findAll();
                Log.d("SearchFilterMPFStageVM:", "realmStages size: " + realmStages.size());
                Log.d("SearchFilterMPFStageVM:", "realmStages list: " + realmStages);
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

    /**
     * Set the selected Stage data in the bundle based on the checked selection.
     * @viewType The adapter child type which is the source of the data passed (0=parent, 1=child, 2=grandchild)
     * Each has its own ID starting at 0.
     * Note: While 'name' usually looks like an int, it needs to be a String to support variant names like '18A'
     */
    public void setStageData(int viewType, int id, String name) {
        Log.d(TAG, "setStageData: viewType: " + viewType + ", id: " + id + ", name: " + name);

        // overwrite the Bundle instance with each selection since Stage only supports single-selection
        bundle = new Bundle();
        setBundleData(BUNDLE_KEY_VIEW_TYPE, Integer.toString(viewType));
        setBundleData(BUNDLE_KEY_ID, Integer.toString(id));
        setBundleData(BUNDLE_KEY_NAME, name);
    }

    private void setBundleData(String key, String value) {
        bundle.putString(key, value);
    }

    public RealmResults<SearchFilterStagesMain> getRealmStages() {
        return realmStages;
    }
}
