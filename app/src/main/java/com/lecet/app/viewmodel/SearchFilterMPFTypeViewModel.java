package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.lecet.app.R;
import com.lecet.app.data.models.ProjectStage;
import com.lecet.app.data.models.ProjectType;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterMPFTypeViewModel extends BaseObservable {
    private AppCompatActivity activity;

    private String[] type;// = {"", ""};   // name, id
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
                Log.d("SearchFilterMPFTypeVM:","realmProjectTypes size: " + realmProjectTypes.size());
                Log.d("SearchFilterMPFTypeVM:","realmProjectTypes list: " + realmProjectTypes);
            }
        });
    }

    public void onClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, type);
        activity.setResult(R.id.type & 0xfff, intent);
        activity.finish();
    }

    public void onSelected(View view) {
        type = new String[2];
        type[0] = ((CheckBox) view).getText().toString();
        type[1] = "103";   //(String)view.getTag();    //TODO - HARD-CODED. Use setTag(id) during dynamic generation of list views, and pass getTag() as type[0] rather than the String name
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }


}
