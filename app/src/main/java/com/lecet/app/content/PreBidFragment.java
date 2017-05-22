package com.lecet.app.content;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lecet.app.R;
import com.lecet.app.adapters.BidProjectListRecyclerViewAdapter;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.FragmentPrePostBidBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectsNearMeViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */

//TODO: make an inheritance for this fragment and all common attributes and methods for PreBid and postbid.
public class PreBidFragment extends Fragment {
static private ProjectsNearMeViewModel viewModel;
static private ProjectsNearMeActivity activity;
static private ArrayList<Project> bidData;
    public static PreBidFragment newInstance(ProjectsNearMeActivity ac, ArrayList<Project> data) {
        PreBidFragment fragmentInstance = new PreBidFragment();
       // viewModel = vm;
        activity = ac;
        bidData = data;
       // Bundle args = new Bundle();
       // args.putLong("BID_ID_EXTRA", bidId);
       // fragmentInstance.setArguments(args);

        return fragmentInstance;
    }
    public PreBidFragment() {
        // Required empty public constructor
    }
    BidProjectListRecyclerViewAdapter searchAdapterProjectAll;

    public BidProjectListRecyclerViewAdapter getSearchAdapterProjectAll() {
        return searchAdapterProjectAll;
    }

    public void setSearchAdapterProjectAll(BidProjectListRecyclerViewAdapter searchAdapterProjectAll) {
        this.searchAdapterProjectAll = searchAdapterProjectAll;
    }

    static void initAdapter() {

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view_prepostbid);
//        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view_prepostbid);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        BidProjectListRecyclerViewAdapter searchAdapterProjectAll =
                new BidProjectListRecyclerViewAdapter(activity, SearchViewModel.SEARCH_ADAPTER_TYPE_PROJECT_QUERY_ALL, bidData);
        //recyclerView.scrollToPosition(1);
        recyclerView.setAdapter(searchAdapterProjectAll);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     //   TextView textView = new TextView(getActivity());
    //    textView.setText(R.string.hello_blank_fragment);
        View view = initDataBinding(inflater, container);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_prepostbid);
//        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view_prepostbid);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

         searchAdapterProjectAll =
                new BidProjectListRecyclerViewAdapter(activity, SearchViewModel.SEARCH_ADAPTER_TYPE_PROJECT_QUERY_ALL, bidData);
        Log.d("biddata","biddata"+bidData);
        //recyclerView.scrollToPosition(1);
        recyclerView.setAdapter(searchAdapterProjectAll);
          return view;
    }
    private FragmentPrePostBidBinding binding;
    private View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this.getContext()), Realm.getDefaultInstance());
       // ProjectLocationViewModel viewModel = new ProjectLocationViewModel(this, 3, projectDomain);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pre_post_bid, container, false);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        //binding.getViewModel().init();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    //  initAdapter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // initAdapter();
    }
}
