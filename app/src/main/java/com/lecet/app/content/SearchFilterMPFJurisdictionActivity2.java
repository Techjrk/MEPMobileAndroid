package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lecet.app.R;
import com.lecet.app.adapters.JurisdictionAdapter;
import com.lecet.app.data.models.SearchFilterJurisdictionDistrictCouncil;
import com.lecet.app.data.models.SearchFilterJurisdictionLocal;
import com.lecet.app.data.models.SearchFilterJurisdictionMain;
import com.lecet.app.databinding.ActivitySearchFilterMpfjurisdiction2Binding;
import com.lecet.app.viewmodel.SearchFilterMPFJurisdictionViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFilterMPFJurisdictionActivity2 extends AppCompatActivity {
    //// TODO: 1/5/17 Create nested items for jurisdiction layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.activity_search_filter_mpfjurisdiction);
        ActivitySearchFilterMpfjurisdiction2Binding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpfjurisdiction2);
        SearchFilterMPFJurisdictionViewModel viewModel = new SearchFilterMPFJurisdictionViewModel(this);
        sfilter.setViewModel(viewModel);
/**
 * Process the multi-level display item of the jurisdiction with adapter
 */

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.test_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        // Setup some dummy data
        // JurisdictionAdapter.Parent parent1 = new JurisdictionAdapter.Parent();
        // JurisdictionAdapter.Parent parent2 = new JurisdictionAdapter.Parent();

        JurisdictionAdapter.Parent parent;
        List<JurisdictionAdapter.Parent> data = new ArrayList<>();

        JurisdictionAdapter.Subtype subtype = null; // new JurisdictionAdapter.Subtype();
        JurisdictionAdapter.SubSubtype subSubtype = null; // new JurisdictionAdapter.Subtype();
        // subtypes.add(subtype1);

        int ctr = 0;

        List<SearchFilterJurisdictionMain> sMain = SearchViewModel.jurisdictionMainList;
        for (SearchFilterJurisdictionMain jmain : sMain) {
            parent = new JurisdictionAdapter.Parent();
            parent.setName(jmain.getName());
            ctr++;
            List<JurisdictionAdapter.Subtype> subtypes = new ArrayList<>();
            for (SearchFilterJurisdictionLocal jlocal : jmain.getLocals()) {
                //     if (jlocal !=null)   Log.d("jlocal","jlocal = name:"+jlocal.getName()+ " id:"+jlocal.getId()+" districtcouncilid:"+jlocal.getDistrictCouncilId());
                if (jlocal != null) {
                    subtype = new JurisdictionAdapter.Subtype();
                    subtype.setName(jlocal.getName());
                    subtypes.add(subtype);
                }
            }
            for (SearchFilterJurisdictionDistrictCouncil dcouncil : jmain.getDistrictCouncils()) {
//                if (dcouncil !=null)  Log.d("jdcouncil","jdcouncil = name:"+ dcouncil.getName()+" abbreviation:"+dcouncil.getAbbreviation()+" id:"+dcouncil.getId()+" regionId:"+dcouncil.getRegionId());
                if (dcouncil != null) {
                    subtype = new JurisdictionAdapter.Subtype();
                    subtype.setName(dcouncil.getName());

                    List<JurisdictionAdapter.SubSubtype> subSubtypes = new ArrayList<>();
                    if (dcouncil.getLocals() != null) {
                        for (SearchFilterJurisdictionLocal dclocals : dcouncil.getLocals()) {
                            if (dclocals != null) {
                                subSubtype = new JurisdictionAdapter.SubSubtype();
                                subSubtype.setName(dclocals.getName());
                                subSubtypes.add(subSubtype);
                            }
//                        if (dclocals !=null)   Log.d("jdcouncillocals","jdcouncillocals = name:"+dclocals.getName()+" id:"+dclocals.getId()+" dcid:"+dclocals.getDistrictCouncilId());
                        }
                        subtype.setSubtypes(subSubtypes);
                    }
                    subtypes.add(subtype);
                }
            }
//        parent1.setSubtypes(subtypes);

//        List<JurisdictionAdapter.Parent> data = new ArrayList<>();

            parent.setSubtypes(subtypes);
            data.add(parent);

            //   data.add(parent3);
        }

/*
        // Setup some dummy data
        JurisdictionAdapter.Parent parent1 = new JurisdictionAdapter.Parent();
        parent1.setName("Parent 1");
      //  parent1.setSubtypes(subtypes);

        JurisdictionAdapter.Parent parent2 = new JurisdictionAdapter.Parent();
        parent2.setName("Parent 2");
     //   parent2.setSubtypes(subtypes);

        JurisdictionAdapter.Parent parent3 = new JurisdictionAdapter.Parent();
        parent3.setName("Parent 3");
      //  parent3.setSubtypes(subtypes);

        List<JurisdictionAdapter.Parent> data = new ArrayList<>();
        data.add(parent1);
        data.add(parent2);
        data.add(parent3);
        */
        // Create Adapter
        JurisdictionAdapter adapter = new JurisdictionAdapter(data);
        recyclerView.setAdapter(adapter);
    }

}
