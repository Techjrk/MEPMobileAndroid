package com.lecet.app.content;

import android.location.Location;
import android.support.v4.app.Fragment;

import com.lecet.app.data.models.Project;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */


public class PostBidFragment extends BidFragment {

    public static PostBidFragment newInstance(ArrayList<Project> data , Location currentLocation) {
        PostBidFragment fragmentInstance = new PostBidFragment();
        fragmentInstance.setBidData(data);
        fragmentInstance.setPreBid(false);
        fragmentInstance.setCurrentLocation(currentLocation);
        return fragmentInstance;
    }

    public PostBidFragment() {
        // Required empty public constructor
    }

}
