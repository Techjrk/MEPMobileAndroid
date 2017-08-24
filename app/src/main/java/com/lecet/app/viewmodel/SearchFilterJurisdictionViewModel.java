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
import com.lecet.app.adapters.SearchFilterJurisdictionAdapter;
import com.lecet.app.content.SearchFilterJurisdictionActivity;
import com.lecet.app.data.models.SearchFilterJurisdictionDistrictCouncil;
import com.lecet.app.data.models.SearchFilterJurisdictionLocal;
import com.lecet.app.data.models.SearchFilterJurisdictionMain;
import com.lecet.app.data.models.SearchFilterJurisdictionNoDistrictCouncil;

import java.util.ArrayList;
import java.util.List;

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
    public static final int NO_TYPE = -1;
    private SearchFilterJurisdictionActivity activity;

    // Note: For tracking last checked name item
    private String lastName;
    private int lastSection; //keep track of last section used by the selected item
    private int lastFamilyChecked = NO_TYPE;
    private int lastPosition; //keep track of last position used by the selected item
    private int lastChildParentPosition; //keep track of last child parent used by the selected item

    // Note: for searching the text typed by the user if the customSearch is true.
    private boolean customSearch;
    private Bundle bundle;
    private RealmResults<SearchFilterJurisdictionMain> realmJurisdictions;
    private String query;
    private List<SearchFilterJurisdictionAdapter.JurisdictionMain> data;
    private SearchFilterJurisdictionAdapter adapter;

    /**
     * Constructor
     */
    public SearchFilterJurisdictionViewModel(AppCompatActivity activity) {
        this.activity = (SearchFilterJurisdictionActivity) activity;
        //Note: process the getting  the last jurisdiction item selected by the user.
        Intent i = activity.getIntent();
        bundle = i.getBundleExtra(activity.getString(R.string.FilterJurisdictionData));
        if (bundle == null) bundle = new Bundle();

        //Note: get all the jurisdiction items to be displayed from the Realm.
        getJurisdictions();

        //Note: start the initial process of searching the required jurisdiction items to be displayed.
        searchItem("");
    }

    // Setter & getter methods
    public int getLastChildParentPosition() {
        return lastChildParentPosition;
    }

    public void setLastChildParentPosition(int lastChildParentPosition) {
        this.lastChildParentPosition = lastChildParentPosition;
    }

    public int getLastFamilyChecked() {
        return lastFamilyChecked;
    }

    public void setLastFamilyChecked(int lastFamilyChecked) {
        this.lastFamilyChecked = lastFamilyChecked;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean getCustomSearch() {
        return customSearch;
    }

    public void setCustomSearch(boolean customSearch) {
        this.customSearch = customSearch;
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

        Intent intent = activity.getIntent();
        intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE, bundle);
        activity.setResult(Activity.RESULT_OK, intent);
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
            setCustomSearch(true);
        } else {
            setCustomSearch(false);
            if (adapter != null) adapter.notifyDataSetChanged();
        }
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        if (data != null) data.clear();
        else
            data = new ArrayList<>();

        List<SearchFilterJurisdictionAdapter.JurisdictionCouncil> children = null;

        for (SearchFilterJurisdictionMain jMain : getRealmJurisdictions()) {
            hasGrandChild = false;
            hasChild = false;
            foundParent = false;
            containGrandChild.clear();
            SearchFilterJurisdictionAdapter.JurisdictionMain adapterMain = new SearchFilterJurisdictionAdapter.JurisdictionMain();
            adapterMain.setId(jMain.getId());
            adapterMain.setName(jMain.getName());
            adapterMain.setAbbreviation(jMain.getAbbreviation());
            adapterMain.setLongName(jMain.getLongName());
            if (adapterMain.getName().trim().toLowerCase().contains(searchKey.trim().toLowerCase())) {
                foundParent = true;
            }
            if (!bundle.isEmpty() && bundle.getString(BUNDLE_KEY_ID).equalsIgnoreCase(String.valueOf(adapterMain.getId()))) {
                adapterMain.setSelected(true);
            }
            children = new ArrayList<>();

            /*** For processing the No DistrictCouncil */
            processNoDistrict(jMain, children, searchKey);

            /*** For processing the DistrictCouncil */
            processDistrict(jMain, children, searchKey);

            if (!children.isEmpty()) {
                adapterMain.setChildren(children);
            }

            if (adapterMain != null && (hasChild || hasGrandChild) || foundParent)
                data.add(adapterMain);
        }

        adapter = new SearchFilterJurisdictionAdapter(data, this);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void processDistrict(SearchFilterJurisdictionMain jMain, List<SearchFilterJurisdictionAdapter.JurisdictionCouncil> children, String searchKey) {
        foundChild = false;
        foundGrandChild = false;
        containGrandChild.clear();

        for (SearchFilterJurisdictionDistrictCouncil dcouncil : jMain.getDistrictCouncils()) {
            if (dcouncil != null) {
                foundChild = false;
                containGrandChild.clear();
                SearchFilterJurisdictionAdapter.JurisdictionCouncil child = new SearchFilterJurisdictionAdapter.JurisdictionCouncil();
                child.setName(dcouncil.getName());
                if (child.getName().trim().toLowerCase().contains(searchKey.toLowerCase())) {
                    hasChild = true;
                    foundChild = true;
                }
                if (!bundle.isEmpty() && bundle.getString(BUNDLE_KEY_ID).equalsIgnoreCase(String.valueOf(child.getId()))) {
                    child.setSelected(true);
                }
                if (dcouncil.getLocals() != null) {

                    List<SearchFilterJurisdictionAdapter.JurisdictionLocal> grandChildren1 = new ArrayList<>();
                    // Locals
                    for (SearchFilterJurisdictionLocal dclocals : dcouncil.getLocals()) {
                        if (dclocals != null) {

                            foundGrandChild = false;
                            SearchFilterJurisdictionAdapter.JurisdictionLocal grandChild1 = new SearchFilterJurisdictionAdapter.JurisdictionLocal();
                            grandChild1.setId(dclocals.getId());
                            grandChild1.setName(dclocals.getName());
                            if (grandChild1.getName().toLowerCase().contains(searchKey.toLowerCase())) {
                                hasGrandChild = true;
                                foundGrandChild = true;
                                containGrandChild.add(hasGrandChild);
                            }
                            //Restore the grandchildren selected item later if needed
                            if (!bundle.isEmpty() && bundle.getString(BUNDLE_KEY_ID).equalsIgnoreCase(String.valueOf(grandChild1.getId()))) {
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

    private void processNoDistrict(SearchFilterJurisdictionMain jMain, List<SearchFilterJurisdictionAdapter.JurisdictionCouncil> children, String searchKey) {
        foundChild = false;
        foundGrandChild = false;
        containGrandChild.clear();
        for (SearchFilterJurisdictionNoDistrictCouncil dcouncil : jMain.getLocalsWithNoDistrict()) {
            if (dcouncil != null) {
                foundChild = false;
                containGrandChild.clear();
                SearchFilterJurisdictionAdapter.JurisdictionCouncil child = new SearchFilterJurisdictionAdapter.JurisdictionCouncil();
                child.setName(dcouncil.getName());
                if (child.getName().trim().toLowerCase().contains(searchKey.toLowerCase())) {
                    hasChild = true;
                    foundChild = true;
                }
                if (!bundle.isEmpty() && bundle.getString(BUNDLE_KEY_ID).equalsIgnoreCase(String.valueOf(child.getId()))) {
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

}
