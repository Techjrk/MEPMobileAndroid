package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.lecet.app.data.models.SearchFilterStagesMain;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * View Model for Search Filter Activity: Stage
 */
public class SearchFilterMPFStageViewModel extends BaseObservable {
    private AppCompatActivity activity;

    private String[] stageExtra;     // = {"", ""};  // name, id
    private RealmResults<SearchFilterStagesMain> realmStages;

    /**
     * Constructor
     */
    public SearchFilterMPFStageViewModel(AppCompatActivity activity) {
        this.activity = activity;
        getProjectStages();
        stageExtra = new String[2];
    }
    public void setStageName(String name) {
        stageExtra[0] = name;
    }
    /**
     * Read Realm ProjectStage data
     */
    private void getProjectStages() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmStages = realm.where(SearchFilterStagesMain.class).findAll();     // parentId = 0 should be all parent ProjectStages, which each contain a list of child ProjectStages
                Log.d("SearchFilterMPFStageVM:", "realmStages size: " + realmStages.size());
                Log.d("SearchFilterMPFStageVM:", "realmStages list: " + realmStages);
            }
        });
    }

    //TODO - rename to specify that this is coming only from the Apply button for this filter
    public void onClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, stageExtra);
//        activity.setResult(R.id.stageExtra & 0xfff, intent);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    //TODO - this is never called
    public void onSelected(View view) {
        stageExtra[0] = ((CheckBox) view).getText().toString();
        stageExtra[1] = "102";   //(String)view.getTag();    //TODO - HARD-CODED. Use setTag(id) during dynamic generation of list views, and pass getTag() as stageExtra[0] rather than the String name
    }

    public RealmResults<SearchFilterStagesMain> getRealmStages() {
        return realmStages;
    }
}
