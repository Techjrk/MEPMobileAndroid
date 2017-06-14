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
import android.widget.CheckBox;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.SearchFilterProjectTypeAdapter;
import com.lecet.app.adapters.SearchFilterProjectTypeUserCreatedAdapter;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.SearchFilterProjectTypesMain;
import com.lecet.app.data.models.SearchFilterProjectTypesProjectCategory;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * View Model for Search Filter Activity: Project Type
 * Created by getdevsinc on 12/29/16.
 */
public class SearchFilterProjectTypeViewModel extends BaseObservable {
    private boolean foundParent, foundChild, foundGrandChild, hasChild, hasGrandChild;
    private ArrayList<Boolean> containGrandChild = new ArrayList<Boolean>();

    private final String TAG = "SearchFilterProjTypeVM";
    private AppCompatActivity activity;
    private Bundle bundle;
    private RealmResults<SearchFilterProjectTypesMain> realmProjectTypes;
    private String query;
    private CheckBox lastChecked;

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
    public SearchFilterProjectTypeViewModel(AppCompatActivity activity) {
        this.activity = activity;
        bundle = new Bundle();
        getProjectTypes();
        searchItem("");
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

    public RealmResults<SearchFilterProjectTypesMain> getRealmProjectTypes() {
        return realmProjectTypes;
    }

    public void addProjectTypeData(String key, String value) {
        bundle.putString(key, value);
    }

    public void removeProjectTypeData(String key) {
        bundle.remove(key);
    }

    public void searchItem(String key) {
        foundParent = false;
        foundChild = false;
        foundGrandChild = false;
        hasChild = false;
        hasGrandChild = false;
        String searchKey = key;
        if (!searchKey.equals("")) {
            SearchFilterProjectTypeAdapter.customSearch = true;
        } else {
            SearchFilterProjectTypeAdapter.customSearch = false;

        }
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);

        List<SearchFilterProjectTypeAdapter.Parent> data = new ArrayList<>();
        List<SearchFilterProjectTypeAdapter.Child> children = null;
        for (SearchFilterProjectTypesMain ptMain : getRealmProjectTypes()) {
            hasGrandChild = false;
            hasChild = false;
            foundParent = false;
            foundChild = false;
            foundGrandChild = false;
            containGrandChild.clear();
            SearchFilterProjectTypeAdapter.Parent parent = new SearchFilterProjectTypeAdapter.Parent();
            parent.setName(ptMain.getTitle());
            parent.setId("" + ptMain.getId());
            if (parent.getName().trim().toLowerCase().contains(searchKey.trim().toLowerCase())) {
                foundParent = true;
            }
            children = new ArrayList<>();
            for (SearchFilterProjectTypesProjectCategory ptpc : ptMain.getProjectCategories()) {
                if (ptpc != null) {
                    foundChild = false;
                    containGrandChild.clear();
                    SearchFilterProjectTypeAdapter.Child child = new SearchFilterProjectTypeAdapter.Child();
                    child.setName(ptpc.getTitle());
                    child.setId("" + ptpc.getId());
                    if (child.getName().trim().toLowerCase().contains(searchKey.toLowerCase())) {
                        hasChild = true;
                        foundChild = true;
                    }
                    List<PrimaryProjectType> gchildTypes = ptpc.getProjectTypes();
                    List<SearchFilterProjectTypeAdapter.GrandChild> grandChildren = new ArrayList<>();
                    for (PrimaryProjectType ppType : gchildTypes) {
                        if (ppType != null) {
                            foundGrandChild = false;
                            SearchFilterProjectTypeAdapter.GrandChild gchild = new SearchFilterProjectTypeAdapter.GrandChild();
                            gchild.setName(ppType.getTitle());
                            gchild.setId("" + ppType.getId());
                            if (gchild.getName().toLowerCase().contains(searchKey.toLowerCase())) {
                                hasGrandChild = true;
                                foundGrandChild = true;
                                containGrandChild.add(hasGrandChild);
                            }
                            // grandChildren.add(gchild);
                            if (foundGrandChild || foundChild || foundParent)
                                grandChildren.add(gchild);
                        }
                    }
                    if (child != null && grandChildren != null)
                        child.setGrandChildren(grandChildren);
                    if ((foundChild || containGrandChild.contains(true)) || foundParent) {
                        children.add(child);
                        containGrandChild.remove(true);
                    }
                }

            }
            if (children != null) {
                parent.setChildren(children);
            }
            if (parent != null && (hasChild || hasGrandChild) || foundParent) data.add(parent);
        }

//SearchFilterAllTabbedViewModel.userCreated=true;
        if (SearchFilterAllTabbedViewModel.userCreated) {
            SearchFilterProjectTypeUserCreatedAdapter adapter = new SearchFilterProjectTypeUserCreatedAdapter(data, this);
            recyclerView.setAdapter(adapter);
        } else {
            SearchFilterProjectTypeAdapter adapter = new SearchFilterProjectTypeAdapter(data, this);
            recyclerView.setAdapter(adapter);
        }
    }


    public void clearBundle() {
        bundle.clear();
    }

    public CheckBox getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(CheckBox lastChecked) {
        this.lastChecked = lastChecked;
    }

}
