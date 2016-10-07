package com.lecet.app.content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.R;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardBidsRecentlyMadeFragment extends Fragment {

    private static final String TAG = "BidsRecentlyMadeFrag";

    private String title;
    private int page;

    public static DashboardBidsRecentlyMadeFragment newInstance(int page, String title) {
        Log.d(TAG, "newInstance");
        DashboardBidsRecentlyMadeFragment fragmentFirst = new DashboardBidsRecentlyMadeFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_bids_recently_made, container, false);
        return view;
    }
}