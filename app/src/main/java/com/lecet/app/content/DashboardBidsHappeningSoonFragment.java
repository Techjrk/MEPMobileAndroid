package com.lecet.app.content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lecet.app.R;

/**
 * Created by jasonm on 10/5/16.
 */

public class DashboardBidsHappeningSoonFragment extends Fragment {

    private static final String TAG = "BidsHappeningSoonFrag";

    private String title;
    private String subtitle;
    private int page;

    public static DashboardBidsHappeningSoonFragment newInstance(int page, String title, String subtitle) {
        Log.d(TAG, "newInstance");
        DashboardBidsHappeningSoonFragment fragmentInstance = new DashboardBidsHappeningSoonFragment();
        Bundle args = new Bundle();
        //args.putInt("someInt", page);
        args.putString("fragmentTitle", title);
        args.putString("fragmentSubtitle", subtitle);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        //page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("fragmentTitle");
        subtitle = getArguments().getString("fragmentSubtitle");
        Log.d(TAG, "onCreate: " + title + " " + subtitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_dashboard_bids_happening_soon, container, false);

        TextView fragmentTitleText = (TextView) view.findViewById(R.id.title_text);
        fragmentTitleText.setText(this.title);

        TextView fragmentSubtitleText = (TextView) view.findViewById(R.id.subtitle_text);
        fragmentSubtitleText.setText(this.subtitle);

        return view;
    }


}