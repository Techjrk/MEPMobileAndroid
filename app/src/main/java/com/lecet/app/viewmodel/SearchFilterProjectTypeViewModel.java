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
import android.view.View;
import android.widget.CheckBox;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.SearchFilterProjectTypeAdapter;
import com.lecet.app.adapters.SearchFilterProjectTypeSingleSelectAdapter;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.SearchFilterProjectTypesMain;
import com.lecet.app.data.models.SearchFilterProjectTypesProjectCategory;
import com.lecet.app.utility.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public static final int NO_TYPE = -1;
    private int lastFamilyChecked = NO_TYPE;
    private int lastSection; //keep track of last section used by the selected item
    private int lastPosition; //keep track of last position used by the selected item
    private int lastChildParentPosition; //keep track of last child parent used by the selected item
    private String lastName;
    private boolean customSearch;
    private Bundle bundle;
    private RealmResults<SearchFilterProjectTypesMain> realmProjectTypes;
    private String query;
    private CheckBox lastChecked;

    /**
     * Constructor
     */
    public SearchFilterProjectTypeViewModel(AppCompatActivity activity) {
        this.activity = activity;
        getLastCheckedItems();
        bundle = getPrefBundle();
        if (bundle == null) bundle = new Bundle();
        getProjectTypes();
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

    public  String getLastName() {
        return lastName;
    }

    public  void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
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
        String message = "Please Select A Project Type Item.";
        alert.setButton(DialogInterface.BUTTON_POSITIVE, activity.getString(android.R.string.ok), onClickAddListener);
        alert.setMessage(message);
        alert.show();
    }


    /**
     * Apply the filter and return to the main Search activity
     */
    public void onApplyButtonClicked(View view) {
        if (SearchFilterAllTabbedViewModel.userCreated) {
            if (bundle.isEmpty() ) {
                showAlertMessage();
                return;
            }
            saveLastCheckedItems();
        }
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
            setCustomSearch(true);
        } else {
            setCustomSearch(false);
        }
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);

        List<SearchFilterProjectTypeAdapter.ProjectTypeMain> data = new ArrayList<>();
        List<SearchFilterProjectTypeAdapter.ProjectTypeCouncil> children = null;
        for (SearchFilterProjectTypesMain ptMain : getRealmProjectTypes()) {
            hasGrandChild = false;
            hasChild = false;
            foundParent = false;
            foundChild = false;
            foundGrandChild = false;
            containGrandChild.clear();
            SearchFilterProjectTypeAdapter.ProjectTypeMain parent = new SearchFilterProjectTypeAdapter.ProjectTypeMain();
            parent.setName(ptMain.getTitle());
            parent.setId("" + ptMain.getId());
            if (parent.getName().trim().toLowerCase().contains(searchKey.trim().toLowerCase())) {
                foundParent = true;
            }

            if (!bundle.isEmpty() && bundle.containsKey(parent.getId()) ) {
                parent.setSelected(true);
            }

            children = new ArrayList<>();
            for (SearchFilterProjectTypesProjectCategory ptpc : ptMain.getProjectCategories()) {
                if (ptpc != null) {
                    foundChild = false;
                    containGrandChild.clear();
                    SearchFilterProjectTypeAdapter.ProjectTypeCouncil child = new SearchFilterProjectTypeAdapter.ProjectTypeCouncil();
                    child.setName(ptpc.getTitle());
                    child.setId("" + ptpc.getId());
                    if (child.getName().trim().toLowerCase().contains(searchKey.toLowerCase())) {
                        hasChild = true;
                        foundChild = true;
                    }
                    if (!bundle.isEmpty() && bundle.containsKey(child.getId()) ) {
                        child.setSelected(true);
                    }
                    List<PrimaryProjectType> gchildTypes = ptpc.getProjectTypes();
                    List<SearchFilterProjectTypeAdapter.ProjectTypeLocal> grandChildren = new ArrayList<>();
                    for (PrimaryProjectType ppType : gchildTypes) {
                        if (ppType != null) {
                            foundGrandChild = false;
                            SearchFilterProjectTypeAdapter.ProjectTypeLocal gchild = new SearchFilterProjectTypeAdapter.ProjectTypeLocal();
                            gchild.setName(ppType.getTitle());
                            gchild.setId("" + ppType.getId());
                            if (gchild.getName().toLowerCase().contains(searchKey.toLowerCase())) {
                                hasGrandChild = true;
                                foundGrandChild = true;
                                containGrandChild.add(hasGrandChild);
                            }
                            //Restore the grandchildren selected item later if needed
                            if (!bundle.isEmpty() && bundle.containsKey(gchild.getId()) ) {
                                gchild.setSelected(true);
                            }

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

        //SearchFilterAllTabbedViewModel.userCreated=true; //might be needing this.
        if (SearchFilterAllTabbedViewModel.userCreated) {
            SearchFilterProjectTypeSingleSelectAdapter adapter = new SearchFilterProjectTypeSingleSelectAdapter(data, this);
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

    public Bundle getPrefBundle() {
        Bundle b = null;
        SharedPreferences spref = activity.getSharedPreferences(activity.getString(R.string.FilterTypeData), Context.MODE_PRIVATE);
        if (spref != null) {
            Set<String> sIDs = spref.getAll().keySet();
            if (sIDs == null || sIDs.size() == 0) return null;
            b = new Bundle();
            for (String keyID : sIDs) {
                b.putString(keyID, spref.getString(keyID, ""));
               // Log.d("getPrefTypeBundle","getPrefTypeBundle"+keyID+" : "+spref.getString(keyID,""));
            }
        }
        return b;
    }
    void saveLastCheckedItems() {
        SharedPreferences spref = activity.getSharedPreferences(activity.getString(R.string.lastcheckedTypeItems), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spref.edit();
        edit.putInt(activity.getString(R.string.lastFamilyChecked),getLastFamilyChecked());
        edit.putString(activity.getString(R.string.lastName),lastName);
        edit.putInt(activity.getString(R.string.lastChildParentPosition),getLastChildParentPosition());
        edit.putInt(activity.getString(R.string.lastSection),getLastSection());
        edit.putInt(activity.getString(R.string.lastPosition),getLastPosition());
        edit.apply();

    }
    void getLastCheckedItems(){
        SharedPreferences spref = activity.getSharedPreferences(activity.getString(R.string.lastcheckedTypeItems), Context.MODE_PRIVATE);
        setLastFamilyChecked(spref.getInt(activity.getString(R.string.lastFamilyChecked),getLastFamilyChecked()));
        setLastName(spref.getString(activity.getString(R.string.lastName),getLastName()));
        setLastChildParentPosition(spref.getInt(activity.getString(R.string.lastChildParentPosition),getLastChildParentPosition()));
        setLastSection(spref.getInt(activity.getString(R.string.lastSection),getLastSection()));
        setLastPosition(spref.getInt(activity.getString(R.string.lastPosition),getLastPosition()));
    }
}
