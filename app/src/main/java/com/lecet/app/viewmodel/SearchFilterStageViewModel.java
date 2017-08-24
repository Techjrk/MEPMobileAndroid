package com.lecet.app.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
    public static final int NO_TYPE = -1;
    private AppCompatActivity activity;

    private String lastName;
    private int lastSection; //keep track of last section used by the selected item
    private int lastPosition; //keep track of last position used by the selected item
    private int lastChildParentPosition; //keep track of last child parent used by the selected item
    private int lastFamilyChecked = NO_TYPE;
    private boolean customSearch;
    private Bundle bundle;
    private RealmResults<SearchFilterStagesMain> realmStages;
    private String query;
    private CheckBox lastChecked;

    /**
     * Constructor
     */
    public SearchFilterStageViewModel(AppCompatActivity activity) {
        this.activity = activity;
        //getLastCheckedItems();
        Intent i = activity.getIntent();
        bundle = i.getBundleExtra(activity.getString(R.string.FilterStageData));
        if (bundle == null) bundle = new Bundle();
        getProjectStages();
        searchItem("");
    }

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


    public boolean getCustomSearch() {
        return customSearch;
    }

    public void setCustomSearch(boolean customSearch) {
        this.customSearch = customSearch;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

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

    void showAlertMessage() {
        DialogInterface.OnClickListener onClickAddListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
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
        AlertDialog alert = new AlertDialog.Builder(activity).create();
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
            if (bundle.isEmpty()) {
                showAlertMessage();
                return;
            }
            // saveLastCheckedItems();
        }

        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE, bundle);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    /**
     * Set the selected Stage data in the bundle based on the checked selection.
     *
     * @viewType The adapter child type which is the source of the data passed (0=parent, 1=child, 2=grandchild)
     */

    public void setStageData(int viewType, int id, String name) {

        // overwrite the Bundle instance with each selection since Stage only supports single-selection
        Bundle b = new Bundle();
        setBundleData(b, BUNDLE_KEY_VIEW_TYPE, Integer.toString(viewType));
        setBundleData(b, BUNDLE_KEY_ID, Integer.toString(id));
        setBundleData(b, BUNDLE_KEY_NAME, name);
        String sid = Integer.toString(id);
        bundle.putBundle(sid, b);
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
            setCustomSearch(true);
        } else {
            setCustomSearch(false);
        }
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);

        List<SearchFilterStageAdapter.StageMain> data = new ArrayList<>();
        List<SearchFilterStageAdapter.StageCouncil> children = null;
        for (SearchFilterStagesMain parentStage : getRealmStages()) {
            hasChild = false;
            foundParent = false;
            foundChild = false;
            SearchFilterStageAdapter.StageMain parent = new SearchFilterStageAdapter.StageMain();
            parent.setId(parentStage.getId());
            parent.setName(parentStage.getName());
            if (parent.getName().trim().toLowerCase().contains(searchKey.trim().toLowerCase())) {
                foundParent = true;
            }
            if (!bundle.isEmpty() && bundle.containsKey(String.valueOf(parent.getId()))) {
                parent.setSelected(true);
            }

            children = new ArrayList<>();
            for (SearchFilterStage childStage : parentStage.getStages()) {
                if (childStage != null) {
                    foundChild = false;
                    SearchFilterStageAdapter.StageCouncil child = new SearchFilterStageAdapter.StageCouncil();
                    child.setId(childStage.getId());
                    child.setName(childStage.getName());
                    if (child.getName().trim().toLowerCase().contains(searchKey.toLowerCase())) {
                        hasChild = true;
                        foundChild = true;
                    }
                    if (!bundle.isEmpty() && bundle.containsKey(String.valueOf(child.getId()))) {
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
}
