package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.lecet.app.R;
import com.lecet.app.data.models.ProjectStage;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterMPFStageViewModel extends BaseObservable {
    private AppCompatActivity activity;

    private String[] stage = {"", ""};  // name, id
    private RealmResults<ProjectStage> realmStages;

    /**
     * Constructor
     */
    public SearchFilterMPFStageViewModel(AppCompatActivity activity) {
        this.activity = activity;
        getProjectStages();
    }

    /**
     * Read Realm ProjectStage data - TODO: use for generating nested list views
     */
    private void getProjectStages() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmStages = realm.where(ProjectStage.class).equalTo("parentId", 0).findAll();     // parentId = 0 should be all parent ProjectStages, which each contain a list of child ProjectStages
                Log.d("SearchFilterMPFStageVM:","realmStages size: " + realmStages.size());
                Log.d("SearchFilterMPFStageVM:","realmStages list: " + realmStages);
            }
        });
    }

    public void onClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, stage);
        activity.setResult(R.id.stage & 0xfff, intent);
        activity.finish();
    }

    public void onSelected(View view) {
        stage[0] = ((CheckBox) view).getText().toString();
        stage[1] = "103";   //(String)view.getTag();    //TODO - HARD-CODED. Use setTag(id) during dynamic generation of list views, and pass getTag() as stage[0] rather than the String name
    }
}
