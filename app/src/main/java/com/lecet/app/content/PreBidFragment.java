package com.lecet.app.content;

import android.support.v4.app.Fragment;

import com.lecet.app.data.models.Project;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */

//TODO: Make an inheritance for this fragment and all common attributes and methods for PreBid and postbid.-Done
public class PreBidFragment extends BidFragment {
    public static PreBidFragment newInstance(ArrayList<Project> data) {
        PreBidFragment fragmentInstance = new PreBidFragment();
        bidData = data;
        return fragmentInstance;
    }

    public PreBidFragment() {
        // Required empty public constructor
    }
}
