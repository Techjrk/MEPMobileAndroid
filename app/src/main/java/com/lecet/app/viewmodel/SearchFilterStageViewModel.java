package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.SearchFilterStageAdapter;
import com.lecet.app.adapters.SearchFilterStageSingleSelectAdapter;
import com.lecet.app.data.models.SearchFilterStage;
import com.lecet.app.data.models.SearchFilterStagesMain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * View Model for Search Filter Activity: Stage
 */
public class SearchFilterStageViewModel extends BaseObservable {
    private static final String TAG = "SearchFilterStageVM";
    private boolean foundParent, foundChild, hasChild;
    public static final String BUNDLE_KEY_VIEW_TYPE = "com.lecet.app.viewmodel.SearchFilterStageViewModel.viewType";
    public static final String BUNDLE_KEY_ID = "com.lecet.app.viewmodel.SearchFilterStageViewModel.id";
    public static final String BUNDLE_KEY_NAME = "com.lecet.app.viewmodel.SearchFilterStageViewModel.name";
    private AppCompatActivity activity;
    private Bundle bundle;
   // private Bundle bundleCheck;
    private RealmResults<SearchFilterStagesMain> realmStages;
    private String query;
    private CheckBox lastChecked;
  //  private SearchFilterStageAdapter adapter;

    public CheckBox getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(CheckBox lastChecked) {
        this.lastChecked = lastChecked;
    }

    @Bindable
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
        notifyPropertyChanged(BR.query);
        searchItem(query);
    }

    /**
     * Constructor
     */
    public SearchFilterStageViewModel(AppCompatActivity activity) {
        this.activity = activity;
        if (getPrefBundle() == null) {
            bundle = new Bundle();
            //bundleCheck = new Bundle();
        }
        else {
            bundle = getPrefBundle();
           // bundleCheck = getPrefBundle();
        }
        //bundle = new Bundle();
        getProjectStages();
        searchItem("");
    }

    /**
     * Clear the Stage bundle
     */
    public void clearBundle() {
        bundle.clear();
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
            }
        });
    }

/*
    public void clearLast() {
        adapter.clearLast();
    }
*/

    /**
     * Apply the filter and return to the main Search activity
     */
    public void onApplyButtonClicked(View view) {
       // clearLast();
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE, bundle);
       // intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE, bundleCheck);
        activity.setResult(Activity.RESULT_OK, intent);
       /* if (!bundle.isEmpty()) {
            activity.setResult(Activity.RESULT_OK, intent);
        } else {
            activity.setResult(Activity.RESULT_CANCELED);
        }*/

        activity.finish();
    }

    /**
     * Set the selected Stage data in the bundle based on the checked selection.
     *
     * @viewType The adapter child type which is the source of the data passed (0=parent, 1=child, 2=grandchild)
     */

 /*   public void setStageCheck(String id, String name) {
//        public void setStageData(int viewType, int id, String name) {
    //    bundle.putString(String.valueOf(id),name);
       bundleCheck.putString(id,name);
    }*/


    public void setStageData(int viewType, int id, String name) {

        // overwrite the Bundle instance with each selection since Stage only supports single-selection
        Bundle  b = new Bundle();
        setBundleData(b, BUNDLE_KEY_VIEW_TYPE, Integer.toString(viewType));
        setBundleData(b, BUNDLE_KEY_ID, Integer.toString(id));
        setBundleData(b, BUNDLE_KEY_NAME, name);
        String sid = Integer.toString(id);
        bundle.putBundle(sid,b);
      //  setStageCheck(sid,name);
      //  setBundleData(Integer.toString(id), name);
    }

    public void setStageDataold(int viewType, int id, String name) {
        // overwrite the Bundle instance with each selection since Stage only supports single-selection
        //bundle = new Bundle();
       // setBundleData(BUNDLE_KEY_VIEW_TYPE, Integer.toString(viewType));
       // setBundleData(BUNDLE_KEY_ID, Integer.toString(id));
       // setBundleData(BUNDLE_KEY_NAME, name);
    }
    public void removeStageData(String key) {
        bundle.remove(key);
       // bundleCheck.remove(key);
    }

    private void setBundleData(Bundle b, String key, String value) {
        b.putString(key, value);
    }
    private void setBundleDataold (String key, String value) {
        bundle.putString(key, value);
    }
    public RealmResults<SearchFilterStagesMain> getRealmStages() {
        return realmStages;
    }

    /**
     * Process the multi-level display item of the Stage with adapter
     */
    public void searchItem(String key) {
        foundParent = false;
        foundChild = false;
        hasChild = false;
        String searchKey = key;
        if (!searchKey.equals("")) {
            SearchFilterStageAdapter.customSearch = true;
        } else {
            SearchFilterStageAdapter.customSearch = false;
        }
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);

        List<SearchFilterStageAdapter.Parent> data = new ArrayList<>();
        List<SearchFilterStageAdapter.Child> children = null;
        for (SearchFilterStagesMain parentStage : getRealmStages()) {
            hasChild = false;
            foundParent = false;
            foundChild = false;
            SearchFilterStageAdapter.Parent parent = new SearchFilterStageAdapter.Parent();
            parent.setId(parentStage.getId());
            parent.setName(parentStage.getName());
            if (parent.getName().trim().toLowerCase().contains(searchKey.trim().toLowerCase())) {
                foundParent = true;
            }
            //***
            if (!bundle.isEmpty() && bundle.containsKey(String.valueOf(parent.getId())) ) {
                parent.setSelected(true);
                //  getSelectedParent().putInt(parent.getName(), Integer.parseInt(parent.getId()));
               /* for (String parentSelected : getSelectedParent().keySet()) {
                    Log.d("selectParent0", "selectParent0" + parentSelected);
                }*/
            }

            //***

            children = new ArrayList<>();
            for (SearchFilterStage childStage : parentStage.getStages()) {
                if (childStage != null) {
                    foundChild = false;
                    SearchFilterStageAdapter.Child child = new SearchFilterStageAdapter.Child();
                    child.setId(childStage.getId());
                    child.setName(childStage.getName());
                    if (child.getName().trim().toLowerCase().contains(searchKey.toLowerCase())) {
                        hasChild = true;
                        foundChild = true;
                    }
                    if (!bundle.isEmpty() && bundle.containsKey(String.valueOf(child.getId())) ) {
                        child.setSelected(true);
                    }


                    if (foundChild || foundParent) {
                        children.add(child);
                    }
                }
            }

            if (children != null) {
                parent.setChildren(children);
            }

            if (parent != null && (hasChild || foundParent)) data.add(parent);
        }
       // adapter = new SearchFilterStageAdapter(data, this);
       // recyclerView.setAdapter(adapter);

        if (SearchFilterAllTabbedViewModel.userCreated) {
            SearchFilterStageSingleSelectAdapter adapter = new SearchFilterStageSingleSelectAdapter(data, this);
            recyclerView.setAdapter(adapter);
        } else {
            SearchFilterStageAdapter adapter = new SearchFilterStageAdapter(data, this);
            recyclerView.setAdapter(adapter);
        }
    }
    public Bundle getPrefBundle() {
        Bundle b = null;
        SharedPreferences sprefName = activity.getSharedPreferences(activity.getString(R.string.FilterStageData)+"name", Context.MODE_PRIVATE);
        SharedPreferences sprefView = activity.getSharedPreferences(activity.getString(R.string.FilterStageData)+"view", Context.MODE_PRIVATE);
        if (sprefName != null) {
            Set<String> sIDs = sprefName.getAll().keySet();
            b = new Bundle();

            for (String keyID : sIDs) {
                Bundle b2 = new Bundle();
                b2.putString(BUNDLE_KEY_ID,keyID);
                b2.putString(BUNDLE_KEY_NAME, sprefName.getString(keyID, ""));
                b2.putString(BUNDLE_KEY_VIEW_TYPE,sprefView.getString(keyID,""));
                b.putBundle(keyID,b2);
            }
        }
        return b;
    }
}
