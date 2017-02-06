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

    /**g
     * Process the multi-level display item of the jurisdiction with adapter
     */
/*
    public void initRecycleViewMain(SearchFilterJurisdictionViewModel viewModel) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.test_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        SearchFilterJurisdictionAdapter.GrandChild grandChild = new SearchFilterJurisdictionAdapter.GrandChild();

        List<SearchFilterJurisdictionAdapter.Parent> data = new ArrayList<>();

//        int ctr = 0;

        List<SearchFilterJurisdictionAdapter.Child> children = null;

        for (SearchFilterJurisdictionMain jMain : viewModel.getRealmJurisdictions()) {
            SearchFilterJurisdictionAdapter.Parent parent = new SearchFilterJurisdictionAdapter.Parent();
            parent.setId(jMain.getId());
            parent.setName(jMain.getName());
            parent.setAbbreviation(jMain.getAbbreviation());
            parent.setLongName(jMain.getLongName());
            //  ctr++;

            // Children (District Councils)
            children = new ArrayList<>();
            // int childctr=0;
            for (SearchFilterJurisdictionLocal jlocal : jMain.getLocals()) {
                if (jlocal != null) {
                    SearchFilterJurisdictionAdapter.Child child = new SearchFilterJurisdictionAdapter.Child();
                    child.setId(jlocal.getId());
                    child.setName(jlocal.getName());
                    child.setDistrictCouncilId(jlocal.getDistrictCouncilId());
                    children.add(child);
                }
            }
            // parent.setSubtypes(subtypes);
            //   int childctr2=0;
            for (SearchFilterJurisdictionDistrictCouncil dcouncil : jMain.getDistrictCouncils()) {
                if (dcouncil != null) {
//                    ctr++;
                    SearchFilterJurisdictionAdapter.Child child = new SearchFilterJurisdictionAdapter.Child();
                    child.setName(dcouncil.getName());

                    if (dcouncil.getLocals() != null) {
                        List<SearchFilterJurisdictionAdapter.GrandChild> grandChildren1 = new ArrayList<>();
//                        List<SearchFilterJurisdictionAdapter.SubSubtype> subSubtypes = new ArrayList<>();

                        // Locals
                        for (SearchFilterJurisdictionLocal dclocals : dcouncil.getLocals()) {
                            if (dclocals != null) {
                                SearchFilterJurisdictionAdapter.GrandChild grandChild1 = new SearchFilterJurisdictionAdapter.GrandChild();
                                grandChild1.setId(dclocals.getId());
                                grandChild1.setName(dclocals.getName());
                                grandChildren1.add(grandChild1);
                            }
//                        if (dclocals !=null)   Log.d("jdcouncillocals","jdcouncillocals = name:"+dclocals.getName()+" id:"+dclocals.getId()+" dcid:"+dclocals.getDistrictCouncilId());
                        }
                        child.setGrandChildren(grandChildren1);
                    }
                    children.add(child);
                }
            }
            parent.setChildren(children);
            data.add(parent);
        }

        SearchFilterJurisdictionAdapter adapter = new SearchFilterJurisdictionAdapter(data, viewModel);
        recyclerView.setAdapter(adapter);
    }
*/

    /**
     * Process the search Item  of thejurisdiction typed by the user
     */
    boolean foundKey1, foundKey2, foundKey3, foundKey4;
    ArrayList<String> foundDistrictLocals = new ArrayList<String>();

    public void searchItem(SearchFilterJurisdictionViewModel viewModel, String key) {
        String searchKey = key;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.test_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        List<SearchFilterJurisdictionAdapter.Parent> data = new ArrayList<>();
        List<SearchFilterJurisdictionAdapter.Child> children = null;

        for (SearchFilterJurisdictionMain jMain : viewModel.getRealmJurisdictions()) {
            boolean hasGrandChild = false;
            boolean hasChild = false;
            foundKey1 = false;
            foundDistrictLocals.clear();
            SearchFilterJurisdictionAdapter.Parent parent = new SearchFilterJurisdictionAdapter.Parent();
            parent.setId(jMain.getId());
            parent.setName(jMain.getName());
            parent.setAbbreviation(jMain.getAbbreviation());
            parent.setLongName(jMain.getLongName());
            if (parent.getName().contains(searchKey)) {
                foundKey1 = true; //foundDistrictLocals.add("jmain");
            }
            children = new ArrayList<>();
/*            for (SearchFilterJurisdictionLocal jlocal : jMain.getLocals()) {
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
                    if (foundKey2 || foundKey1 ) children.add(child);
                }
            }*/

            /*** For processing the No DistrictCouncil */
            processNoDistrict(data, jMain, children, searchKey, hasChild, hasGrandChild);

            /*** For processing the DistrictCouncil */
            processDistrict(data, jMain, children, searchKey, hasChild, hasGrandChild);

            if (children != null) {
                parent.setChildren(children);
                Log.d("parent1 added", "parent1 added");
            }
            if (parent != null && (hasChild || hasGrandChild)) data.add(parent);
        }
        SearchFilterJurisdictionAdapter adapter = new SearchFilterJurisdictionAdapter(data, viewModel);
        recyclerView.setAdapter(adapter);
    }

    private void processDistrict(List<SearchFilterJurisdictionAdapter.Parent> data, SearchFilterJurisdictionMain jMain, List<SearchFilterJurisdictionAdapter.Child> children, String searchKey, boolean hasChild, boolean hasGrandChild) {
        foundKey3 = false;
        foundKey4 = false;
        foundDistrictLocals.clear();

        for (SearchFilterJurisdictionDistrictCouncil dcouncil : jMain.getDistrictCouncils()) {
            if (dcouncil != null) {
                foundKey3 = false;
                foundDistrictLocals.clear();
                SearchFilterJurisdictionAdapter.Child child = new SearchFilterJurisdictionAdapter.Child();
                child.setName(dcouncil.getName());
                if (child.getName().trim().contains(searchKey)) {
                    hasChild = true;
                    foundKey3 = true; //foundDistrictLocals.add("dcouncil");
                    Log.d("found3", "found3" + child.getName());
                }
                if (dcouncil.getLocals() != null) {

                    List<SearchFilterJurisdictionAdapter.GrandChild> grandChildren1 = new ArrayList<>();
                    // Locals
                    for (SearchFilterJurisdictionLocal dclocals : dcouncil.getLocals()) {
                        if (dclocals != null) {

                            foundKey4 = false;
                            SearchFilterJurisdictionAdapter.GrandChild grandChild1 = new SearchFilterJurisdictionAdapter.GrandChild();
                            grandChild1.setId(dclocals.getId());
                            grandChild1.setName(dclocals.getName());
                            if (grandChild1.getName().contains(searchKey)) {
                                hasGrandChild = true;
                                foundKey4 = true;
                                foundDistrictLocals.add("dclocal");
                            }
                            if (foundKey4 || foundKey3 || foundKey1)
                                grandChildren1.add(grandChild1);
                        }
//                        if (dclocals !=null)   Log.d("jdcouncillocals","jdcouncillocals = name:"+dclocals.getName()+" id:"+dclocals.getId()+" dcid:"+dclocals.getDistrictCouncilId());
                    }
                    if (child != null && grandChildren1 != null)
                        child.setGrandChildren(grandChildren1);
                }
                if ((foundKey3 || foundDistrictLocals.contains("dclocal")) || foundKey1) {
                    children.add(child);
                    foundDistrictLocals.remove("dclocal");
                    Log.d("child3 added", "haschild3: " + hasChild + "hasgchild" + hasGrandChild + "child1 added" + child.getName() + " f1:" + foundKey1 + " f3:" + foundKey3 + " fdc:" + foundDistrictLocals.contains("dclocal"));
                }
            }
        }

    }

    private void processNoDistrict(List<SearchFilterJurisdictionAdapter.Parent> data, SearchFilterJurisdictionMain jMain, List<SearchFilterJurisdictionAdapter.Child> children, String searchKey, boolean hasChild, boolean hasGrandChild) {
        for (SearchFilterJurisdictionNoDistrictCouncil dcouncil : jMain.getLocalsWithNoDistrict()) {
            if (dcouncil != null) {
                foundKey3 = false;
                foundDistrictLocals.clear();
                SearchFilterJurisdictionAdapter.Child child = new SearchFilterJurisdictionAdapter.Child();
                child.setName(dcouncil.getName());
                if (child.getName().trim().contains(searchKey)) {
                    hasChild = true;
                    foundKey3 = true; //foundDistrictLocals.add("dcouncil");
                    Log.d("found3", "found3" + child.getName());
                }

                if ((foundKey3 || foundDistrictLocals.contains("dclocal")) || foundKey1) {
                    children.add(child);
                    Log.d("child3 added", "hasndcchild3: " + hasChild + "hasgchild" + hasGrandChild + "child1 added" + child.getName() + " f1:" + foundKey1 + " f3:" + foundKey3 + " fdc:" + foundDistrictLocals.contains("dclocal"));
                }
            }
        }

    }
}
