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
 * View Model for Search Filter Activity: Jurisdiction
 * Created by getdevsinc on 12/29/16.
 */
public class SearchFilterMPFJurisdictionViewModel extends BaseObservable {

    private static final String TAG = "SearchFilterMPFJurisVM";

    private AppCompatActivity activity;

    private RealmResults<SearchFilterJurisdictionMain> realmJurisdictions;
    private String[] jurisdictionExtra;  // = {"", ""};

    /**
     * Constructor
     */
    public SearchFilterMPFJurisdictionViewModel(AppCompatActivity activity) {
        this.activity = activity;
        getJurisdictions();
        jurisdictionExtra = new String[3];
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

    public void onClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, jurisdictionExtra);
//        activity.setResult(R.id.jurisdiction & 0xfff, intent);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public void onSelected(View view) {
        String tag = (String) view.getTag();
        String viewText = ((CheckBox) view).getText().toString();
        //jurisdictionExtra[0] = tag;
        //jurisdictionExtra[1] = viewText;
        //Log.d(TAG, "onSelected: tag: " + tag + ", viewText: " + viewText);
    }

    /**
     * Set the selected Jurisdiction data based on the checked selection.
     * @viewType The adapter child type which is the source of the data passed (0=parent, 1=child, or 2=grandchild)
     * Each has its own ID starting at 0.
     * While 'name' usually looks like an int, it needs to be a String to support names like '18A'
     */
    public void setJurisdictionData(int viewType, int id, String name, String abbreviation, String longName) {
        //TODO - map this to a Jurisdiction object here? or in the main search viewmodel
        Log.d(TAG, "setJurisdictionData: viewType: " + viewType + ", id: " + id + ", name: " + name + ", abbreviation: " + abbreviation + ", longName: " + longName);
        jurisdictionExtra[0] = Integer.toString(viewType);
        jurisdictionExtra[1] = Integer.toString(id);
        jurisdictionExtra[2] = name;
    }

    public RealmResults<SearchFilterJurisdictionMain> getRealmJurisdictions() {
        return realmJurisdictions;
    }
}
