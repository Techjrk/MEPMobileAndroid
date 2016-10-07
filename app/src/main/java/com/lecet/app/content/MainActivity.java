package com.lecet.app.content;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.adapters.DashboardPagerAdapter;
import com.lecet.app.contentbase.NavigationBaseActivity;

import java.util.ArrayList;

/**
 * MainActivity Created by jasonm on 8/15/16.
 * This Activity represents the Dashboard, landed on after logging in.
 */
public class MainActivity extends NavigationBaseActivity {

    private final String TAG = "MainActivity";

    // Fragment Pager
    FragmentPagerAdapter adapterViewPager;
    View dot_1;
    View dot_2;
    View dot_3;
    View dot_4;

    ArrayList<View> dots;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViewPager();
        setupPageIndicator();
    }

    /**
     * Set up the Fragment Pager which contains the four main fragments used
     * in the upper third of the Dashboard
     */
    private void setupViewPager() {

        ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
        adapterViewPager = new DashboardPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                //Log.d("MainActivity", "onPageSelected: " + position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.d("MainActivity", "onPageScrolled: " + position);
                updatePageIndicator(position);
            }

            // Called when the scroll state changes
            @Override
            public void onPageScrollStateChanged(int state) {
                //Log.d("MainActivity", "onPageScrollStateChanged: " + state); // 0 SCROLL_STATE_IDLE, 1 SCROLL_STATE_DRAGGING, 2 SCROLL_STATE_SETTLING
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        break;

                    case ViewPager.SCROLL_STATE_DRAGGING:
                        break;

                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
    }

    /**
     * Create references to the page indicator UI dots
     */
    private void setupPageIndicator() {
        dot_1 = findViewById(R.id.dot_1);
        dot_2 = findViewById(R.id.dot_2);
        dot_3 = findViewById(R.id.dot_3);
        dot_4 = findViewById(R.id.dot_4);

        dots = new ArrayList<>();
        dots.add(dot_1);
        dots.add(dot_2);
        dots.add(dot_3);
        dots.add(dot_4);
    }

    /**
     * Update the page indicator dots UI to show the current page/fragment shown
     * @param position
     */
    private void updatePageIndicator(int position) {
        //Log.d(TAG, "updatePageIndicator: " + position);

        Drawable unselectedDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_dot_unselected, null);
        Drawable selectedDrawable   = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_dot_selected, null);

        // first set all to unselected
        for (View dotView : dots) {
            dotView.setBackground(unselectedDrawable);
        }

        // set the selected dot
        View selectedView = dots.get(position);
        selectedView.setBackground(selectedDrawable);
    }


}
