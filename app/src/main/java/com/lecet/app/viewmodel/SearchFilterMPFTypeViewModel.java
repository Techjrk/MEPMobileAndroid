package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.lecet.app.data.models.SearchFilterProjectTypesMain;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * View Model for Search Filter Activity: Project Type
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterMPFTypeViewModel extends BaseObservable {
    private final String TAG = "SearchFilterMPFTypeVM";

    private AppCompatActivity activity;

    private ArrayList<String> listTypeDataResult = new ArrayList<String>();
    private Bundle pTypeData = new Bundle();
//    private String[] ptype;     // = {"", ""};   // name, id
    private RealmResults<SearchFilterProjectTypesMain> realmProjectTypes;

    /**
     * Constructor
     */
    public SearchFilterMPFTypeViewModel(AppCompatActivity activity) {
        this.activity = activity;
        getProjectTypes();
//        ptype = new String[2];
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
//        if (ptype == null || ptype.length == 1) ptype = new String[]{"",""};
 //       intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, ptype);
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE, pTypeData);
//        activity.setResult(R.id.type & 0xfff, intent);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    /*public void onSelected(View view) {
        ptype[0] = ((CheckBox) view).getText().toString();
        ptype[1] = "103";   //(String)view.getTag();
    }*/

    /*public String[] getType() {
        return ptype;
    }*/

   /*public void setTypeName(String name, String id) {
        Log.d("ptype","ptype size"+ptype.length);
        if (ptype == null || ptype.length == 1) ptype = new String[2];
        ptype[0] = name;
        ptype[1] = id;
    }*/

    /*public void setType(String[] ptype) {
        this.ptype = ptype;
    }*/

    public RealmResults<SearchFilterProjectTypesMain> getRealmProjectTypes() {
        return realmProjectTypes;
    }

    public void addPTypeData(String key, String value) {
        pTypeData.putString(key, value);
        Log.d(TAG, "addPTypeData: " + key + ", value: " + value);
        Log.d(TAG, "addPTypeData: pTypeData is now: " + pTypeData);
    }

    public void removePTypeData(String key) {
        Log.d(TAG, "removePTypeData removing key: " + key);
        pTypeData.remove(key);
        Log.d(TAG, "addPTypeData: pTypeData is now: " + pTypeData);
    }

    public ArrayList<String> getListTypeDataResult() {
        return listTypeDataResult;
    }

    public void setListTypeDataResult(ArrayList<String> listTypeDataResult) {
        this.listTypeDataResult = listTypeDataResult;
    }

    public void addListTypeDataResult(String data) {
        this.listTypeDataResult.add(data);
    }
    public void removeListTypeDataResult(String data) {
        this.listTypeDataResult.remove(data);
    }



}
