package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.content.SearchFilterJurisdictionActivity;
import com.lecet.app.data.models.SearchFilterJurisdictionMain;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * View Model for Search Filter Activity: Jurisdiction
 * Created by getdevsinc on 12/29/16.
 */
public class SearchFilterJurisdictionViewModel extends BaseObservable {
    private static final String TAG = "SearchFilterMPFJurisVM";
    public static final String BUNDLE_KEY_VIEW_TYPE = "viewType";
    public static final String BUNDLE_KEY_ID = "id";
    public static final String BUNDLE_KEY_NAME = "name";
    public static final String BUNDLE_KEY_ABBREVIATION = "abbreviation";
    public static final String BUNDLE_KEY_LONG_NAME = "longName";


    private SearchFilterJurisdictionActivity activity;
    private Bundle bundle;
    private RealmResults<SearchFilterJurisdictionMain> realmJurisdictions;
    private String query;

    @Bindable
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
        notifyPropertyChanged(BR.query);
        activity.searchItem(this,query);
    }

    /**
     * Constructor
     */
    public SearchFilterJurisdictionViewModel(AppCompatActivity activity) {
        this.activity = (SearchFilterJurisdictionActivity) activity;
        bundle = new Bundle();
        getJurisdictions();
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
                Log.d(TAG, "realmJurisdictions size: " + realmJurisdictions.size());
                Log.d(TAG, "realmJurisdictions list: " + realmJurisdictions);
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
     * Set the selected Jurisdiction data in the bundle based on the checked selection.
     * @viewType The adapter child type which is the source of the data passed (0=parent, 1=child, or 2=grandchild)
     * Each has its own ID starting at 0.
     * Note: While 'name' usually looks like an int, it needs to be a String to support variant names like '18A'
     */
    public void setJurisdictionData(int viewType, int id, String name, String abbreviation, String longName) {
        Log.d(TAG, "setJurisdictionData: viewType: " + viewType + ", id: " + id + ", name: " + name + ", abbreviation: " + abbreviation + ", longName: " + longName);

        // overwrite the Bundle instance with each selection since Jurisdiction only supports single-selection
        bundle = new Bundle();
        setBundleData(BUNDLE_KEY_VIEW_TYPE, Integer.toString(viewType));
        setBundleData(BUNDLE_KEY_ID, Integer.toString(id));
        setBundleData(BUNDLE_KEY_NAME, name);
        setBundleData(BUNDLE_KEY_ABBREVIATION, abbreviation);
        setBundleData(BUNDLE_KEY_LONG_NAME, longName);
    }

    private void setBundleData(String key, String value) {
        bundle.putString(key, value);
    }

    public RealmResults<SearchFilterJurisdictionMain> getRealmJurisdictions() {
        return realmJurisdictions;
    }
}
