package com.lecet.app.content;

import android.support.v4.app.Fragment;

import com.lecet.app.data.models.Project;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */

public class PreBidFragment extends BidFragment {

    public static PreBidFragment newInstance(ArrayList<Project> data) {
        PreBidFragment fragmentInstance = new PreBidFragment();
        fragmentInstance.setBidData(data);

        return fragmentInstance;
    }

    public PreBidFragment() {
        // Required empty public constructor
    }
}
