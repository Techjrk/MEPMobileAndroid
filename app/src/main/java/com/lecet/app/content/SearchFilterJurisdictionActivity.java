package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;

import com.lecet.app.R;
import com.lecet.app.adapters.SearchFilterJurisdictionAdapter;
import com.lecet.app.data.models.SearchFilterJurisdictionDistrictCouncil;
import com.lecet.app.data.models.SearchFilterJurisdictionLocal;
import com.lecet.app.data.models.SearchFilterJurisdictionMain;
import com.lecet.app.data.models.SearchFilterJurisdictionNoDistrictCouncil;
import com.lecet.app.databinding.ActivitySearchFilterJurisdictionBinding;
import com.lecet.app.viewmodel.SearchFilterJurisdictionViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for Search Filter: Jurisdiction
 */
public class SearchFilterJurisdictionActivity extends AppCompatActivity {
    private boolean foundParent, foundChild, foundGrandChild, hasChild, hasGrandChild;
    private ArrayList<String> foundDistrictLocals = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ActivitySearchFilterJurisdictionBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_jurisdiction);
        SearchFilterJurisdictionViewModel viewModel = new SearchFilterJurisdictionViewModel(this);
        sfilter.setViewModel(viewModel);
        // initRecycleViewMain(viewModel);
        searchItem(viewModel, "");
    }

    /**
     * Process the multi-level display item of the jurisdiction with adapter
     * and Process the search Item  of the jurisdiction typed by the user
     */


    public void searchItem(SearchFilterJurisdictionViewModel viewModel, String key) {
        foundParent = false;
        foundChild = false;
        foundGrandChild = false;
        hasChild = false;
        hasGrandChild = false;
        String searchKey = key;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.test_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        List<SearchFilterJurisdictionAdapter.Parent> data = new ArrayList<>();
        List<SearchFilterJurisdictionAdapter.Child> children = null;

        for (SearchFilterJurisdictionMain jMain : viewModel.getRealmJurisdictions()) {
            hasGrandChild = false;
            hasChild = false;
            foundParent = false;
            foundDistrictLocals.clear();
            SearchFilterJurisdictionAdapter.Parent parent = new SearchFilterJurisdictionAdapter.Parent();
            parent.setId(jMain.getId());
            parent.setName(jMain.getName());
            parent.setAbbreviation(jMain.getAbbreviation());
            parent.setLongName(jMain.getLongName());
            if (parent.getName().trim().toLowerCase().contains(searchKey.trim().toLowerCase())) {
                foundParent = true; //foundDistrictLocals.add("jmain");
            }
            children = new ArrayList<>();
           /* Removing the local main display of Jurisdiction. No need anymore. This will be deleted.

           for (SearchFilterJurisdictionLocal jlocal : jMain.getLocals()) {
                if (jlocal != null) {
                    foundKey2=false;
                    SearchFilterJurisdictionAdapter.Child child = new SearchFilterJurisdictionAdapter.Child();
                    child.setId(jlocal.getId());
                    child.setName(jlocal.getName());
                    child.setDistrictCouncilId(jlocal.getDistrictCouncilId());
                    if (child.getName().trim().contains(searchKey)) {
                        hasChild=true;
                        foundKey2=true;// foundDistrictLocals.add("jlocal");
                    }
                 //   if (foundKey2 || foundParent ) children.add(child);
                }
            }
            */

            /*** For processing the No DistrictCouncil */
            processNoDistrict(jMain, children, searchKey);

            /*** For processing the DistrictCouncil */
            processDistrict(jMain, children, searchKey);

            if (children != null) {
                parent.setChildren(children);
                Log.d("parent1 added", "parent1 added");
            }
            if (parent != null && (hasChild || hasGrandChild) || foundParent) data.add(parent);
        }
        SearchFilterJurisdictionAdapter adapter = new SearchFilterJurisdictionAdapter(data, viewModel);
        recyclerView.setAdapter(adapter);
    }

    private void processDistrict(SearchFilterJurisdictionMain jMain, List<SearchFilterJurisdictionAdapter.Child> children, String searchKey) {
        foundChild = false;
        foundGrandChild = false;
        foundDistrictLocals.clear();

        for (SearchFilterJurisdictionDistrictCouncil dcouncil : jMain.getDistrictCouncils()) {
            if (dcouncil != null) {
                foundChild = false;
                foundDistrictLocals.clear();
                SearchFilterJurisdictionAdapter.Child child = new SearchFilterJurisdictionAdapter.Child();
                child.setName(dcouncil.getName());
                if (child.getName().trim().toLowerCase().contains(searchKey.toLowerCase())) {
                    hasChild = true;
                    foundChild = true; //foundDistrictLocals.add("dcouncil");
                    Log.d("found3", "found3" + child.getName());
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
                                foundDistrictLocals.add("dclocal");
                            }
                            if (foundGrandChild || foundChild || foundParent)
                                grandChildren1.add(grandChild1);
                        }
//                        if (dclocals !=null)   Log.d("jdcouncillocals","jdcouncillocals = name:"+dclocals.getName()+" id:"+dclocals.getId()+" dcid:"+dclocals.getDistrictCouncilId());
                    }
                    if (child != null && grandChildren1 != null)
                        child.setGrandChildren(grandChildren1);
                }
                if ((foundChild || foundDistrictLocals.contains("dclocal")) || foundParent) {
                    children.add(child);
                    foundDistrictLocals.remove("dclocal");
                    Log.d("child3 added", "haschild3: " + hasChild + "hasgchild" + hasGrandChild + "child1 added" + child.getName() + " f1:" + foundParent + " f3:" + foundChild + " fdc:" + foundDistrictLocals.contains("dclocal"));
                }
            }
        }
    }

    private void processNoDistrict(SearchFilterJurisdictionMain jMain, List<SearchFilterJurisdictionAdapter.Child> children, String searchKey) {
        foundChild = false;
        foundGrandChild = false;
        foundDistrictLocals.clear();
        for (SearchFilterJurisdictionNoDistrictCouncil dcouncil : jMain.getLocalsWithNoDistrict()) {
            if (dcouncil != null) {
                foundChild = false;
                foundDistrictLocals.clear();
                SearchFilterJurisdictionAdapter.Child child = new SearchFilterJurisdictionAdapter.Child();
                child.setName(dcouncil.getName());
                if (child.getName().trim().toLowerCase().contains(searchKey.toLowerCase())) {
                    hasChild = true;
                    foundChild = true; //foundDistrictLocals.add("dcouncil");
                    Log.d("found3", "found3" + child.getName());
                }

                if ((foundChild || foundDistrictLocals.contains("dclocal")) || foundParent) {
                    children.add(child);
                    Log.d("child3 added", "hasndcchild3: " + hasChild + "hasgchild" + hasGrandChild + "child1 added" + child.getName() + " f1:" + foundParent + " f3:" + foundChild + " fdc:" + foundDistrictLocals.contains("dclocal"));
                }
            }
        }

    }
}
