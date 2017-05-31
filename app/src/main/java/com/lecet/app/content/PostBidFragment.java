package com.lecet.app.content;

import android.support.v4.app.Fragment;

import com.lecet.app.data.models.Project;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostBidFragment extends BidFragment {

    public static PostBidFragment newInstance(ArrayList<Project> data) {
        PostBidFragment fragmentInstance = new PostBidFragment();
        bidData = data;

        return fragmentInstance;
    }

    public PostBidFragment() {
        // Required empty public constructor
    }

}
