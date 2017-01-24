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

import java.util.ArrayList;
import java.util.List;

public class SearchFilterMPFJurisdictionActivity2 extends AppCompatActivity {
    //RealmResults<SearchFilterJurisdictionMain> realmJurisdictions;    //removed as this is already available from the view model
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //   setContentView(R.layout.activity_search_filter_mpfjurisdiction);
        ActivitySearchFilterMpfjurisdiction2Binding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mpfjurisdiction2);
        SearchFilterMPFJurisdictionViewModel viewModel = new SearchFilterMPFJurisdictionViewModel(this);
        sfilter.setViewModel(viewModel);
        //getJurisdictionList();
        initRecycleView(viewModel);
    }

    /**
     * Process the multi-level display item of the jurisdiction with adapter
     */
    public void initRecycleView(SearchFilterMPFJurisdictionViewModel viewModel) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.test_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        JurisdictionAdapter.GrandChild grandChild = new JurisdictionAdapter.GrandChild();

        List<JurisdictionAdapter.Parent> data = new ArrayList<>();

//        int ctr = 0;

    //    List<SearchFilterJurisdictionMain> sMain = SearchViewModel.jurisdictionMainList;
        List<SearchFilterJurisdictionMain> sMain = viewModel.getRealmJurisdictions();
        List<JurisdictionAdapter.Child> children = null;

        for (SearchFilterJurisdictionMain jmain : sMain) {

            // Parent
            JurisdictionAdapter.Parent parent = new JurisdictionAdapter.Parent();
            parent.setId(jmain.getId());
            parent.setName(jmain.getName());
            parent.setAbbreviation(jmain.getAbbreviation());
            parent.setLongName(jmain.getLongName());
            //  ctr++;

            // Children (District Councils)
            children = new ArrayList<>();
            // int childctr=0;
            for (SearchFilterJurisdictionLocal jlocal : jmain.getLocals()) {
                if (jlocal != null) {
                    JurisdictionAdapter.Child child = new JurisdictionAdapter.Child();
                    child.setId(jlocal.getId());
                    child.setName(jlocal.getName());
                    child.setDistrictCouncilId(jlocal.getDistrictCouncilId());
                    children.add(child);
                }
            }
            // parent.setSubtypes(subtypes);
            //   int childctr2=0;
            for (SearchFilterJurisdictionDistrictCouncil dcouncil : jmain.getDistrictCouncils()) {
                if (dcouncil != null) {
//                    ctr++;
                    JurisdictionAdapter.Child child = new JurisdictionAdapter.Child();
                    child.setName(dcouncil.getName());

                    if (dcouncil.getLocals() != null) {
                        List<JurisdictionAdapter.GrandChild> grandChildren1 = new ArrayList<>();
//                        List<JurisdictionAdapter.SubSubtype> subSubtypes = new ArrayList<>();

                        // Locals
                        for (SearchFilterJurisdictionLocal dclocals : dcouncil.getLocals()) {
                            if (dclocals != null) {
                                JurisdictionAdapter.GrandChild grandChild1 = new JurisdictionAdapter.GrandChild();
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

        JurisdictionAdapter adapter = new JurisdictionAdapter(data, viewModel);
        recyclerView.setAdapter(adapter);
    }

    /*private void getJurisdictionList() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmJurisdictions = realm.where(SearchFilterJurisdictionMain.class).findAll();
            }
        });
    }*/
}
