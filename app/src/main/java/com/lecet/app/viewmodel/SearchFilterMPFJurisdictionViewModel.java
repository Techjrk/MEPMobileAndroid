package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.lecet.app.R;
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
    }

    /**
     * Read Realm Jurisdiction data - TODO: use for generating nested list views
     */
    private void getJurisdictions() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmJurisdictions = realm.where(SearchFilterJurisdictionMain.class).findAll();
                Log.d("SearchFilterMPFJurisVM:","realmJurisdictions size: " + realmJurisdictions.size());
                Log.d("SearchFilterMPFJurisVM:","realmJurisdictions list: " + realmJurisdictions);
            }
        });
    }

    public void onClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, jurisdictionExtra);
        activity.setResult(R.id.jurisdiction & 0xfff, intent);
        activity.finish();
    }

    public void onSelected(View view) {
        jurisdictionExtra = new String[2];
        jurisdictionExtra[0] = ((CheckBox) view).getText().toString();  //TODO - the Views and their Tags are hard-coded until dynamic nested Views are working. Use setTag(id) during dynamic generation of list views, and pass getTag() as type[0] rather than the String name
        jurisdictionExtra[1] = (String)view.getTag();
    }
}
