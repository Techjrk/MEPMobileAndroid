package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.SearchFilterStageAdapter;
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

    private AppCompatActivity activity;

    private Bundle bundle;
    private RealmResults<SearchFilterStagesMain> realmStages;

    private String query;

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
        bundle = new Bundle();
        getProjectStages();
        searchItem("");
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

    /**
     * Apply the filter and return to the main Search activity
     */
    public void onApplyButtonClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE, bundle);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    /**
     * Set the selected Stage data in the bundle based on the checked selection.
     *
     * @viewType The adapter child type which is the source of the data passed (0=parent, 1=child, 2=grandchild)
     * Each has its own ID starting at 0.
     * Note: While 'name' usually looks like an int, it needs to be a String to support variant names like '18A'
     */
    public void setStageData(int viewType, int id, String name) {
        // overwrite the Bundle instance with each selection since Stage only supports single-selection
        bundle = new Bundle();
        setBundleData(BUNDLE_KEY_VIEW_TYPE, Integer.toString(viewType));
        setBundleData(BUNDLE_KEY_ID, Integer.toString(id));
        setBundleData(BUNDLE_KEY_NAME, name);
    }

    private void setBundleData(String key, String value) {
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
                    if (foundChild || foundParent) {
                        children.add(child);
                    }
                }
            }
            //parent.setChildren(children);
            if (children != null) {
                parent.setChildren(children);
            }
            // data.add(parent);
            if (parent != null && (hasChild || foundParent)) data.add(parent);
        }
        SearchFilterStageAdapter adapter = new SearchFilterStageAdapter(data, this);
        recyclerView.setAdapter(adapter);
    }

}
