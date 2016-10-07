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

public class DashboardBidsHappeningSoonFragment extends Fragment {

    private static final String TAG = "BidsHappeningSoonFrag";

    private String title;
    private int page;

    public static DashboardBidsHappeningSoonFragment newInstance(int page, String title) {
        Log.d(TAG, "newInstance");
        DashboardBidsHappeningSoonFragment fragmentSecond = new DashboardBidsHappeningSoonFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentSecond.setArguments(args);
        return fragmentSecond;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_bids_happening_soon, container, false);
        return view;
    }
}