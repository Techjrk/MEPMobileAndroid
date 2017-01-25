package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.lecet.app.data.models.ProjectType;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterMPFTypeViewModel extends BaseObservable {
    private AppCompatActivity activity;

    private String[] ptype;// = {"", ""};   // name, id
    private RealmResults<ProjectType> realmProjectTypes;

    /**
     * Constructor
     */
    public SearchFilterMPFTypeViewModel(AppCompatActivity activity) {
        this.activity = activity;
        getProjectTypes();
    }

    /**
     * Read Realm ProjectType data - TODO: use for generating nested list views
     */
    private void getProjectTypes() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmProjectTypes = realm.where(ProjectType.class).equalTo("parentId", 0).findAll();     // parentId = 0 should be all parent ProjectType, which each contain a list of child ProjectType
                Log.d("SearchFilterMPFTypeVM:", "realmProjectTypes size: " + realmProjectTypes.size());
                Log.d("SearchFilterMPFTypeVM:", "realmProjectTypes list: " + realmProjectTypes);
            }
        });
    }

    public void onClicked(View view) {
        Intent intent = activity.getIntent();
        if (ptype == null || ptype.length == 1) ptype = new String[]{"",""};
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, ptype);
//        activity.setResult(R.id.type & 0xfff, intent);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }
    public void setTypeName(String name, String id) {
        Log.d("ptype","ptype size"+ptype.length);
      //  ptype[1]="";
        if (ptype == null || ptype.length == 1) ptype = new String[2];
        ptype[0] = name;
        ptype[1] = id;
    }
    public void onSelected(View view) {
        ptype = new String[2];
        ptype[0] = ((CheckBox) view).getText().toString();
        ptype[1] = "103";   //(String)view.getTag();    //TODO - HARD-CODED. Use setTag(id) during dynamic generation of list views, and pass getTag() as type[0] rather than the String name
    }

    public String[] getType() {
        return ptype;
    }

    public void setType(String[] ptype) {
        this.ptype = ptype;
    }


}
