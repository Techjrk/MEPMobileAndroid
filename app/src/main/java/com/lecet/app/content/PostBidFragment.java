package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lecet.app.R;
import com.lecet.app.adapters.BidProjectListRecyclerViewAdapter;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.FragmentPrePostBidBinding;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostBidFragment extends Fragment {
    static private ArrayList<Project> bidData;

    public static PostBidFragment newInstance(ArrayList<Project> data) {
        PostBidFragment fragmentInstance = new PostBidFragment();
        bidData = data;

        return fragmentInstance;
    }

    public PostBidFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = initDataBinding(inflater, container);

        initAdapter(view);

        return view;
    }


    private View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentPrePostBidBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pre_post_bid, container, false);

        return binding.getRoot();
    }

    void initAdapter(View view) {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_prepostbid);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        BidProjectListRecyclerViewAdapter searchAdapterProjectAll =
                new BidProjectListRecyclerViewAdapter(getActivity(), SearchViewModel.SEARCH_ADAPTER_TYPE_PROJECT_QUERY_ALL, bidData);
        //recyclerView.scrollToPosition(1);
        recyclerView.setAdapter(searchAdapterProjectAll);
    }
}
