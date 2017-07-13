package com.lecet.app.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    public static final int NO_TYPE = -1;

 //   public static int lastFamilyChecked = NO_TYPE;
 //   public static int lastSection; //keep track of last section used by the selected item
 //   public static int lastPosition; //keep track of last position used by the selected item
 //   public static int lastChildParentPosition; //keep track of last child parent used by the selected item
    private String lastName;
    private int lastSection; //keep track of last section used by the selected item
    private  int lastPosition; //keep track of last position used by the selected item
    private int lastChildParentPosition; //keep track of last child parent used by the selected item
    private int lastFamilyChecked = NO_TYPE;

    public int getLastFamilyChecked() {
        return lastFamilyChecked;
    }

    public void setLastFamilyChecked(int lastFamilyChecked) {
        this.lastFamilyChecked = lastFamilyChecked;
    }

    public int getLastChildParentPosition() {
        return lastChildParentPosition;
    }

    public void setLastChildParentPosition(int lastChildParentPosition) {
        this.lastChildParentPosition = lastChildParentPosition;
    }

    public int getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

    public int getLastSection() {
        return lastSection;
    }

    public void setLastSection(int lastSection) {
        this.lastSection = lastSection;
    }

    private boolean customSearch;

    public boolean getCustomSearch() {
        return customSearch;
    }

    public void setCustomSearch(boolean customSearch) {
        this.customSearch = customSearch;
    }

    public  String getLastName() {
        return lastName;
    }

    public  void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private Bundle bundle;

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

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
        getLastCheckedItems();
        bundle = getPrefBundle();
        Log.d("bundleStage","bundleStage"+bundle);
        if (bundle == null) bundle = new Bundle();
/*
        if (getPrefBundle() == null) {
            bundle = new Bundle();
            //bundleCheck = new Bundle();
        }
        else {
            bundle = getPrefBundle();
           // bundleCheck = getPrefBundle();
        }
*/
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

void saveLastCheckedItems() {
    SharedPreferences spref = activity.getSharedPreferences(activity.getString(R.string.lastcheckedStageItems), Context.MODE_PRIVATE);
    SharedPreferences.Editor edit = spref.edit();
    //edit.clear();
    edit.putInt("lastFamilyChecked",lastFamilyChecked);
    edit.putString("lastName",lastName);
    edit.putInt("lastChildParentPosition",lastChildParentPosition);
    edit.putInt("lastSection",lastSection);
    edit.putInt("lastPosition",lastPosition);
    edit.apply();

}
void getLastCheckedItems(){
    SharedPreferences spref = activity.getSharedPreferences(activity.getString(R.string.lastcheckedStageItems), Context.MODE_PRIVATE);
    lastFamilyChecked= spref.getInt("lastFamilyChecked",lastFamilyChecked);
    lastName=spref.getString("lastName",lastName);
    lastChildParentPosition=spref.getInt("lastChildParentPosition",lastChildParentPosition);
    lastSection=spref.getInt("lastSection",lastSection);
    lastPosition=spref.getInt("lastPosition",lastPosition);

}
    void showAlertMessage() {
        DialogInterface.OnClickListener onClickAddListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        //postProject();
                        //dialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEUTRAL:
                        dialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };
      AlertDialog  alert = new AlertDialog.Builder(activity).create();
        String message = "Please Select A Stage Item.";
        alert.setButton(DialogInterface.BUTTON_POSITIVE, activity.getString(android.R.string.ok), onClickAddListener);
        alert.setMessage(message);
        alert.show();
    }


    /**
     * Apply the filter and return to the main Search activity
     */
    public void onApplyButtonClicked(View view) {
        if (SearchFilterAllTabbedViewModel.userCreated) {
            //setLastChecked(null);
            //clearLast();
            if (bundle.isEmpty() ) {
                showAlertMessage();
                return;
            }
            saveLastCheckedItems();
        }

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

    public void setStageData(int viewType, int id, String name) {

        // overwrite the Bundle instance with each selection since Stage only supports single-selection
        Bundle  b = new Bundle();
        setBundleData(b, BUNDLE_KEY_VIEW_TYPE, Integer.toString(viewType));
        setBundleData(b, BUNDLE_KEY_ID, Integer.toString(id));
        setBundleData(b, BUNDLE_KEY_NAME, name);
        String sid = Integer.toString(id);
        bundle.putBundle(sid,b);
    }

    public void removeStageData(String key) {
        bundle.remove(key);
    }

    private void setBundleData(Bundle b, String key, String value) {
        b.putString(key, value);
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
            //SearchFilterStageAdapter.customSearch = true;
            setCustomSearch(true);
        } else {
            //SearchFilterStageAdapter.customSearch = false;
            setCustomSearch(false);
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
        Log.d("getPrefBundle","getPrefBundle"+sprefName);
        if (sprefName != null) {
            Set<String> sIDs = sprefName.getAll().keySet();
            Log.d("sids","sids"+sIDs);
            if (sIDs == null || sIDs.size() == 0) return null;
            b = new Bundle();

            for (String keyID : sIDs) {
                Bundle b2 = new Bundle();
                b2.putString(BUNDLE_KEY_ID,keyID);
                b2.putString(BUNDLE_KEY_NAME, sprefName.getString(keyID, ""));
                b2.putString(BUNDLE_KEY_VIEW_TYPE,sprefView.getString(keyID,""));
                b.putBundle(keyID,b2);
                Log.d("getPrefStageBundle2","getPrefStageBundle2"+keyID+" : "+sprefName.getString(keyID,"")+":"+sprefView.getString(keyID,""));

                if (sprefView.getString(keyID,"").equals("")) lastFamilyChecked = Integer.valueOf(sprefView.getString(keyID,""));
                setLastName(sprefName.getString(keyID, ""));
            }
        }
        return b;
    }
}
