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
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.SearchFilterJurisdictionAdapter;
import com.lecet.app.content.SearchFilterJurisdictionActivity;
import com.lecet.app.data.models.SearchFilterJurisdictionDistrictCouncil;
import com.lecet.app.data.models.SearchFilterJurisdictionLocal;
import com.lecet.app.data.models.SearchFilterJurisdictionMain;
import com.lecet.app.data.models.SearchFilterJurisdictionNoDistrictCouncil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * View Model for Search Filter Activity: Jurisdiction
 * Created by getdevsinc on 12/29/16.
 */
public class SearchFilterJurisdictionViewModel extends BaseObservable {
    private boolean foundParent, foundChild, foundGrandChild, hasChild, hasGrandChild;
    private ArrayList<Boolean> containGrandChild = new ArrayList<Boolean>();
    private CheckBox lastChecked;
    private static final String TAG = "SearchFilterMPFJurisVM";
    public static final String BUNDLE_KEY_VIEW_TYPE = "com.lecet.app.viewmodel.SearchFilterJurisdictionViewModel.viewType";
    public static final String BUNDLE_KEY_ID = "com.lecet.app.viewmodel.SearchFilterJurisdictionViewModel.id";
    public static final String BUNDLE_KEY_REGION_ID = "com.lecet.app.viewmodel.SearchFilterJurisdictionViewModel.regionId";
    public static final String BUNDLE_KEY_NAME = "com.lecet.app.viewmodel.SearchFilterJurisdictionViewModel.name";
    public static final String BUNDLE_KEY_ABBREVIATION = "com.lecet.app.viewmodel.SearchFilterJurisdictionViewModel.abbreviation";
    public static final String BUNDLE_KEY_LONG_NAME = "com.lecet.app.viewmodel.SearchFilterJurisdictionViewModel.longName";
    private SearchFilterJurisdictionActivity activity;
    public static final int NO_TYPE = -1;

    public static int lastFamilyChecked = NO_TYPE;
    public static int lastSection; //keep track of last section used by the selected item
    public static int lastPosition; //keep track of last position used by the selected item
    public static int lastChildParentPosition; //keep track of last child parent used by the selected item
    private  String lastName;
    private Bundle bundle;

    private RealmResults<SearchFilterJurisdictionMain> realmJurisdictions;
    private String query;
    private List<SearchFilterJurisdictionAdapter.Parent> data;
    private SearchFilterJurisdictionAdapter adapter;

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


    /**
     * Constructor
     */
    public SearchFilterJurisdictionViewModel(AppCompatActivity activity) {
        this.activity = (SearchFilterJurisdictionActivity) activity;
       // bundle = new Bundle();
        getLastCheckedItems();
        bundle = getPrefBundle();
        if (bundle == null) bundle = new Bundle();
        getJurisdictions();
        searchItem("");
    }

    public CheckBox getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(CheckBox lastChecked) {
        this.lastChecked = lastChecked;
    }

    /**
     * Read Realm Jurisdiction data
     */
    private void getJurisdictions() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmJurisdictions = realm.where(SearchFilterJurisdictionMain.class).findAll();
            }
        });
    }

    public void clearLast() {
        adapter.clearLast();
    }

    /**
     * Apply the filter and return to the main Search activity
     */
    public void onApplyButtonClicked(View view) {
        //clearLast();
        saveLastCheckedItems();
        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE, bundle);
        Log.d("jurisdictiondatab","jurisdictiondatab"+bundle);
        activity.setResult(Activity.RESULT_OK, intent);
       /* if (!bundle.isEmpty()) {
            activity.setResult(Activity.RESULT_OK, intent);
        } else {
            activity.setResult(Activity.RESULT_CANCELED);
        }*/

        activity.finish();
    }

    /**
     * Set the selected Jurisdiction data in the bundle based on the checked selection. Support single selection, not multiple.
     *
     * @viewType The adapter child type which is the source of the data passed (0=parent, 1=child, or 2=grandchild)
     * @id The Jurisdiction ID. Each has its own ID starting at 0.
     * Note: While 'name' usually looks like an int, it needs to be a String to support variant names like '18A'
     */
    public void setJurisdictionData(int viewType, int id, int regionId, String name, String abbreviation, String longName) {
        // overwrite the Bundle instance with each selection since Jurisdiction only supports single-selection
       // bundle = new Bundle();
        setBundleData(BUNDLE_KEY_VIEW_TYPE, Integer.toString(viewType));
        setBundleData(BUNDLE_KEY_ID, Integer.toString(id));
        setBundleData(BUNDLE_KEY_REGION_ID, Integer.toString(regionId));
        setBundleData(BUNDLE_KEY_NAME, name);
        setBundleData(BUNDLE_KEY_ABBREVIATION, abbreviation);
        setBundleData(BUNDLE_KEY_LONG_NAME, longName);
    }

    private void setBundleData(String key, String value) {
        bundle.putString(key, value);
    }

    public void clearBundle() {
        bundle.clear();
    }

    public RealmResults<SearchFilterJurisdictionMain> getRealmJurisdictions() {
        return realmJurisdictions;
    }

    /**
     * Process the multi-level display item of the jurisdiction with adapter
     * and Process the search Item  of the jurisdiction typed by the user
     */
    public void searchItem(String key) {
        foundParent = false;
        foundChild = false;
        foundGrandChild = false;
        hasChild = false;
        hasGrandChild = false;
        String searchKey = key;
        if (!searchKey.equals("")) {
            SearchFilterJurisdictionAdapter.customSearch = true;
        } else {
            SearchFilterJurisdictionAdapter.customSearch = false;
            if (adapter != null) adapter.notifyDataSetChanged();
        }
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        if (data != null) data.clear();
        else
            data = new ArrayList<>();

        List<SearchFilterJurisdictionAdapter.Child> children = null;

        for (SearchFilterJurisdictionMain jMain : getRealmJurisdictions()) {
            hasGrandChild = false;
            hasChild = false;
            foundParent = false;
            containGrandChild.clear();
            SearchFilterJurisdictionAdapter.Parent parent = new SearchFilterJurisdictionAdapter.Parent();
            parent.setId(jMain.getId());
            parent.setName(jMain.getName());
            parent.setAbbreviation(jMain.getAbbreviation());
            parent.setLongName(jMain.getLongName());
            if (parent.getName().trim().toLowerCase().contains(searchKey.trim().toLowerCase())) {
                foundParent = true;
            }

            //***
//            if (!bundle.isEmpty() && bundle.containsKey(String.valueOf(parent.getId()))) {
                if (!bundle.isEmpty() &&  bundle.getString(BUNDLE_KEY_ID).equalsIgnoreCase(String.valueOf(parent.getId()))) {
                parent.setSelected(true);
                //  getSelectedParent().putInt(parent.getName(), Integer.parseInt(parent.getId()));
               /* for (String parentSelected : getSelectedParent().keySet()) {
                    Log.d("selectParent0", "selectParent0" + parentSelected);
                }*/
            }
            //***
            children = new ArrayList<>();

            /*** For processing the No DistrictCouncil */
            processNoDistrict(jMain, children, searchKey);

            /*** For processing the DistrictCouncil */
            processDistrict(jMain, children, searchKey);

            if (!children.isEmpty()) {
                parent.setChildren(children);
            }

            if (parent != null && (hasChild || hasGrandChild) || foundParent) data.add(parent);
        }

        adapter = new SearchFilterJurisdictionAdapter(data, this);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    private void processDistrict(SearchFilterJurisdictionMain jMain, List<SearchFilterJurisdictionAdapter.Child> children, String searchKey) {
        foundChild = false;
        foundGrandChild = false;
        containGrandChild.clear();

        for (SearchFilterJurisdictionDistrictCouncil dcouncil : jMain.getDistrictCouncils()) {
            if (dcouncil != null) {
                foundChild = false;
                containGrandChild.clear();
                SearchFilterJurisdictionAdapter.Child child = new SearchFilterJurisdictionAdapter.Child();
                child.setName(dcouncil.getName());
                if (child.getName().trim().toLowerCase().contains(searchKey.toLowerCase())) {
                    hasChild = true;
                    foundChild = true;
                }
                if (!bundle.isEmpty() && bundle.getString(BUNDLE_KEY_ID).equalsIgnoreCase(String.valueOf(child.getId())) ) {
                    child.setSelected(true);
                }
                if (dcouncil.getLocals() != null) {

                    List<SearchFilterJurisdictionAdapter.GrandChild> grandChildren1 = new ArrayList<>();
                    // Locals
                    for (SearchFilterJurisdictionLocal dclocals : dcouncil.getLocals()) {
                        if (dclocals != null) {

                            foundGrandChild = false;
                            SearchFilterJurisdictionAdapter.GrandChild grandChild1 = new SearchFilterJurisdictionAdapter.GrandChild();
                            grandChild1.setId(dclocals.getId());
                            grandChild1.setName(dclocals.getName());
                            if (grandChild1.getName().toLowerCase().contains(searchKey.toLowerCase())) {
                                hasGrandChild = true;
                                foundGrandChild = true;
                                containGrandChild.add(hasGrandChild);
                            }
                            //Restore the grandchildren selected item later if needed
                            if (!bundle.isEmpty() && bundle.getString(BUNDLE_KEY_ID).equalsIgnoreCase(String.valueOf(grandChild1.getId())) ) {
                                grandChild1.setSelected(true);
                            }

                            if (foundGrandChild || foundChild || foundParent)
                                grandChildren1.add(grandChild1);
                        }
                    }
                    if (child != null && grandChildren1 != null)
                        child.setGrandChildren(grandChildren1);
                }
                if ((foundChild || containGrandChild.contains(true)) || foundParent) {
                    children.add(child);
                    containGrandChild.remove(true);
                }
            }
        }
    }

    private void processNoDistrict(SearchFilterJurisdictionMain jMain, List<SearchFilterJurisdictionAdapter.Child> children, String searchKey) {
        foundChild = false;
        foundGrandChild = false;
        containGrandChild.clear();
        for (SearchFilterJurisdictionNoDistrictCouncil dcouncil : jMain.getLocalsWithNoDistrict()) {
            if (dcouncil != null) {
                foundChild = false;
                containGrandChild.clear();
                SearchFilterJurisdictionAdapter.Child child = new SearchFilterJurisdictionAdapter.Child();
                child.setName(dcouncil.getName());
                if (child.getName().trim().toLowerCase().contains(searchKey.toLowerCase())) {
                    hasChild = true;
                    foundChild = true;
                }
                if (!bundle.isEmpty() && bundle.getString(BUNDLE_KEY_ID).equalsIgnoreCase(String.valueOf(child.getId())) ) {
                    child.setSelected(true);
                }
                if ((foundChild || containGrandChild.contains(true)) || foundParent) {
                    children.add(child);
                }
            }
        }
    }

    @Bindable
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
        searchItem(query);
        notifyPropertyChanged(BR.query);

    }
    public Bundle getPrefBundle() {
        Bundle b = null;
        SharedPreferences spref = activity.getSharedPreferences(activity.getString(R.string.FilterSharedJData), Context.MODE_PRIVATE);
        if (spref != null) {
            Set<String> sIDs = spref.getAll().keySet();
            if (sIDs == null || sIDs.size() == 0) return null;
            b = new Bundle();
            for (String keyID : sIDs) {
                b.putString(keyID, spref.getString(keyID, ""));
                Log.d("getPrefJBundle2","getPrefJBundle2"+keyID+" : "+spref.getString(keyID,""));
            }
        }
        return b;
    }
/*    setBundleData(BUNDLE_KEY_VIEW_TYPE, Integer.toString(viewType));
    setBundleData(BUNDLE_KEY_ID, Integer.toString(id));
    setBundleData(BUNDLE_KEY_REGION_ID, Integer.toString(regionId));
    setBundleData(BUNDLE_KEY_NAME, name);
    setBundleData(BUNDLE_KEY_ABBREVIATION, abbreviation);
    setBundleData(BUNDLE_KEY_LONG_NAME, longName);
    */
    void saveLastCheckedItems() {
        SharedPreferences spref = activity.getSharedPreferences(activity.getString(R.string.LastCheckedJurisdictionItems), Context.MODE_PRIVATE);
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
        SharedPreferences spref = activity.getSharedPreferences(activity.getString(R.string.LastCheckedJurisdictionItems), Context.MODE_PRIVATE);
        lastFamilyChecked= spref.getInt("lastFamilyChecked",lastFamilyChecked);
        lastName=spref.getString("lastName",lastName);
        lastChildParentPosition=spref.getInt("lastChildParentPosition",lastChildParentPosition);
        lastSection=spref.getInt("lastSection",lastSection);
        lastPosition=spref.getInt("lastPosition",lastPosition);

    }

}
