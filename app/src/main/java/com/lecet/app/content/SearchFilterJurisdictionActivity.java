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
        searchItem(viewModel,"");
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
    boolean foundKey1, foundKey2, foundKey3,foundKey4;
    ArrayList<String> foundMain= new ArrayList<String>();
    public void searchItem(SearchFilterJurisdictionViewModel viewModel, String key) {
        String searchKey=key;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.test_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        SearchFilterJurisdictionAdapter.GrandChild grandChild = new SearchFilterJurisdictionAdapter.GrandChild();

        List<SearchFilterJurisdictionAdapter.Parent> data = new ArrayList<>();

//        int ctr = 0;

        List<SearchFilterJurisdictionAdapter.Child> children = null;

        for (SearchFilterJurisdictionMain jMain : viewModel.getRealmJurisdictions()) {
            foundKey1=false; foundMain.clear();
            SearchFilterJurisdictionAdapter.Parent parent = new SearchFilterJurisdictionAdapter.Parent();
            parent.setId(jMain.getId());
            parent.setName(jMain.getName());
            parent.setAbbreviation(jMain.getAbbreviation());
            parent.setLongName(jMain.getLongName());
            if (parent.getName().contains(searchKey)) {
                foundKey1=true; foundMain.add("jmain");
            }
            //  ctr++;

            // Children (District Councils)
            children = new ArrayList<>();
            // int childctr=0;
            for (SearchFilterJurisdictionLocal jlocal : jMain.getLocals()) {
                if (jlocal != null) {
                    foundKey2=false;
                    SearchFilterJurisdictionAdapter.Child child = new SearchFilterJurisdictionAdapter.Child();
                    child.setId(jlocal.getId());
                    child.setName(jlocal.getName());
                    child.setDistrictCouncilId(jlocal.getDistrictCouncilId());
                    if (child.getName().trim().contains(searchKey)) {foundKey2=true; foundMain.add("jlocal");}

                    if (foundKey2 || foundMain.contains("jmain")) children.add(child);


                }
            }
            // parent.setSubtypes(subtypes);
            //   int childctr2=0;
            for (SearchFilterJurisdictionDistrictCouncil dcouncil : jMain.getDistrictCouncils()) {
                if (dcouncil != null) {
//                    ctr++;
                    foundKey3=false;
                    SearchFilterJurisdictionAdapter.Child child = new SearchFilterJurisdictionAdapter.Child();
                    child.setName(dcouncil.getName());
                    if (child.getName().trim().contains(searchKey)) {
                        foundKey3=true; foundMain.add("dcouncil");
                        Log.d("found3","found3"+child.getName());
                    }
                    if (dcouncil.getLocals() != null) {

                        List<SearchFilterJurisdictionAdapter.GrandChild> grandChildren1 = new ArrayList<>();
//                        List<SearchFilterJurisdictionAdapter.SubSubtype> subSubtypes = new ArrayList<>();

                        // Locals
                        for (SearchFilterJurisdictionLocal dclocals : dcouncil.getLocals()) {
                            if (dclocals != null) {
                                foundKey4=false;
                                SearchFilterJurisdictionAdapter.GrandChild grandChild1 = new SearchFilterJurisdictionAdapter.GrandChild();
                                grandChild1.setId(dclocals.getId());
                                grandChild1.setName(dclocals.getName());
                                if (grandChild1.getName().contains(searchKey)) {foundKey4=true;foundMain.add("dclocal");}
                                if (foundKey4 || foundKey3 || foundMain.contains("jmain")  ) grandChildren1.add(grandChild1);
                            }
//                        if (dclocals !=null)   Log.d("jdcouncillocals","jdcouncillocals = name:"+dclocals.getName()+" id:"+dclocals.getId()+" dcid:"+dclocals.getDistrictCouncilId());
                        }
                        if (child !=null && grandChildren1 !=null)  child.setGrandChildren(grandChildren1);
                    }
                    if (foundKey3 ||  foundMain.contains("jmain") || foundMain.contains("dclocal")  ) {
                        children.add(child);
                        foundMain.remove("dclocal");
                       // foundMain.remove("dcouncil");
                        Log.d("child added","child added");
                    }
                }
            }
            if (children !=null) {
                parent.setChildren(children);
             //   foundMain.remove("dcouncil");
                Log.d("parent added","parent added");
            }
            if (parent !=null && foundMain.size() > 0) data.add(parent);
        }

        SearchFilterJurisdictionAdapter adapter = new SearchFilterJurisdictionAdapter(data, viewModel);
        recyclerView.setAdapter(adapter);
    }
}
