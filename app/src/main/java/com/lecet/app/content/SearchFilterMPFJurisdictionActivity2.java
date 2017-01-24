package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

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
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //   setContentView(R.layout.activity_search_filter_mpfjurisdiction);
        ActivitySearchFilterMpfjurisdiction2Binding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpfjurisdiction2);
        SearchFilterMPFJurisdictionViewModel viewModel = new SearchFilterMPFJurisdictionViewModel(this);
        sfilter.setViewModel(viewModel);
        initRecycleView(viewModel);
    }
    public void initRecycleView(SearchFilterMPFJurisdictionViewModel viewModel){
        /**
         * Process the multi-level display item of the jurisdiction with adapter
         */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.test_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        JurisdictionAdapter.GrandChild grandChild = new JurisdictionAdapter.GrandChild();
        //    List<JurisdictionAdapter.GrandChild> grandChildren = new ArrayList<>();

        List<JurisdictionAdapter.Parent> data = new ArrayList<>();

        // JurisdictionAdapter.Subtype subtype = null; // new JurisdictionAdapter.Subtype();
        // JurisdictionAdapter.SubSubtype subSubtype = null; // new JurisdictionAdapter.Subtype();
        // subtypes.add(subtype1);

        int ctr = 0;

        List<SearchFilterJurisdictionMain> sMain = SearchViewModel.jurisdictionMainList;
        //     List<JurisdictionAdapter.Subtype> subtypes;
        List<JurisdictionAdapter.Child> children = null;
        for (SearchFilterJurisdictionMain jmain : sMain) {
            JurisdictionAdapter.Parent parent = new JurisdictionAdapter.Parent();
            parent.setName(jmain.getName());
            //  ctr++;

            children = new ArrayList<>();
            // int childctr=0;
            for (SearchFilterJurisdictionLocal jlocal : jmain.getLocals()) {
                //     ctr++; childctr++;
                //     if (childctr>3) break;
                //     if (jlocal !=null)   Log.d("jlocal","jlocal = name:"+jlocal.getName()+ " id:"+jlocal.getId()+" districtcouncilid:"+jlocal.getDistrictCouncilId());
                if (jlocal != null) {
                    JurisdictionAdapter.Child child = new JurisdictionAdapter.Child();
                    //     JurisdictionAdapter.Subtype subtype = new JurisdictionAdapter.Subtype();
                    child.setName(jlocal.getName());
                    children.add(child);
                }
            }
            // parent.setSubtypes(subtypes);
            //   int childctr2=0;
            for (SearchFilterJurisdictionDistrictCouncil dcouncil : jmain.getDistrictCouncils()) {
                // childctr2++;
                //   if (childctr2 > 0 ) break;
//                if (dcouncil !=null)  Log.d("jdcouncil","jdcouncil = name:"+ dcouncil.getName()+" abbreviation:"+dcouncil.getAbbreviation()+" id:"+dcouncil.getId()+" regionId:"+dcouncil.getRegionId());
                if (dcouncil != null) {
                    ctr++;
                    JurisdictionAdapter.Child child = new JurisdictionAdapter.Child();
//                    JurisdictionAdapter.Subtype subtype = new JurisdictionAdapter.Subtype();
                    child.setName(dcouncil.getName());

                    if (dcouncil.getLocals() != null) {
                        List<JurisdictionAdapter.GrandChild> grandChildren1 = new ArrayList<>();
//                        List<JurisdictionAdapter.SubSubtype> subSubtypes = new ArrayList<>();
                        for (SearchFilterJurisdictionLocal dclocals : dcouncil.getLocals()) {
                            if (dclocals != null) {
                                JurisdictionAdapter.GrandChild grandChild1 = new JurisdictionAdapter.GrandChild();
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


//        parent1.setSubtypes(subtypes);
//        List<JurisdictionAdapter.Parent> data = new ArrayList<>();
            parent.setChildren(children);
//            parent.setSubtypes(subtypes);
            data.add(parent);
            //  if (ctr > 10) break;
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

        //****
   /*    data = null;
        data = new ArrayList<>();
        children = new ArrayList<>();
        grandChildren = new ArrayList<>();
        JurisdictionAdapter.GrandChild gc1 = new JurisdictionAdapter.GrandChild();
        gc1.setName("gc 0");
        JurisdictionAdapter.GrandChild gc2 = new JurisdictionAdapter.GrandChild();
        gc2.setName("gc 1");
        JurisdictionAdapter.GrandChild gc3 = new JurisdictionAdapter.GrandChild();
        gc3.setName("gc 2");
        grandChildren.add(gc1); grandChildren.add(gc2); grandChildren.add(gc3);
        JurisdictionAdapter.Child child1 = new JurisdictionAdapter.Child();
        child1.setName("child0");
        child1.setGrandChildren(grandChildren);
        JurisdictionAdapter.Child child2 = new JurisdictionAdapter.Child();
        child2.setName("child1");
        child2.setGrandChildren(grandChildren);
        JurisdictionAdapter.Child child3 = new JurisdictionAdapter.Child();
        child3.setName("child2");
        JurisdictionAdapter.Child child4 = new JurisdictionAdapter.Child();
        child4.setName("child3");
        JurisdictionAdapter.Child child5 = new JurisdictionAdapter.Child();
        child5.setName("child4");
        JurisdictionAdapter.Child child6 = new JurisdictionAdapter.Child();
        child6.setName("child5");
        child6.setGrandChildren(grandChildren);

        children.add(child1);
        children.add(child2);

        children.add(child3);
        children.add(child4);
        children.add(child5);

        children.add(child6);
        JurisdictionAdapter.Parent parent1 = new JurisdictionAdapter.Parent();
        parent1.setName("Parent 0");
        parent1.setChildren(children);
        JurisdictionAdapter.Parent parent2 = new JurisdictionAdapter.Parent();
        parent2.setName("Parent 1");
        parent2.setChildren(children);

        JurisdictionAdapter.Parent parent3 = new JurisdictionAdapter.Parent();
        parent3.setName("Parent 2");
        parent3.setChildren(children);

        data.add(parent1);
        data.add(parent2);
        data.add(parent3);*/
        JurisdictionAdapter adapter = new JurisdictionAdapter(data, viewModel);
        recyclerView.setAdapter(adapter);
    }

}
