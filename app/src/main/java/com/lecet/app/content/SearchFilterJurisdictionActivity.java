package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        initRecycleView(viewModel);
    }

    /**
     * Process the multi-level display item of the jurisdiction with adapter
     */
    public void initRecycleView(SearchFilterJurisdictionViewModel viewModel) {
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
                    child.setRegionId(dcouncil.getRegionId());

                    if (dcouncil.getLocals() != null) {
                        List<SearchFilterJurisdictionAdapter.GrandChild> grandChildren1 = new ArrayList<>();
//                        List<SearchFilterJurisdictionAdapter.SubSubtype> subSubtypes = new ArrayList<>();

                        // Locals
                        for (SearchFilterJurisdictionLocal local : dcouncil.getLocals()) {
                            if (local != null) {
                                SearchFilterJurisdictionAdapter.GrandChild grandChild1 = new SearchFilterJurisdictionAdapter.GrandChild();
                                grandChild1.setId(local.getId());
                                grandChild1.setName(local.getName());
                                grandChildren1.add(grandChild1);
                            }
//                        if (local !=null)   Log.d("jdcouncillocals","jdcouncillocals = name:"+local.getName()+" id:"+local.getId()+" dcid:"+local.getDistrictCouncilId());
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

}
