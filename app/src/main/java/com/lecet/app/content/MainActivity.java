package com.lecet.app.content;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lecet.app.R;
import com.lecet.app.contentbase.NavigationBaseActivity;

import java.util.ArrayList;

/**
 * MainActivity Created by jasonm on 8/15/16.
 */
public class MainActivity extends NavigationBaseActivity {

    private final String TAG = "MainActivity";

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

    private void setupViewPager() {

        ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        // Attach the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Log.d("MainActivity", "onPageSelected: " + position);
                Toast.makeText(MainActivity.this, "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("MainActivity", "onPageScrolled: " + position);
                updatePageIndicator(position);
            }

            // Called when the scroll state changes:
            @Override
            public void onPageScrollStateChanged(int state) {
                //Log.d("MainActivity", "onPageScrollStateChanged: " + state);
                // 0 SCROLL_STATE_IDLE
                // 1 SCROLL_STATE_DRAGGING
                // 2 SCROLL_STATE_SETTLING
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

    private void updatePageIndicator(int position) {
        try {
            Log.d("MyPagerAdapter", "updatePageIndicator: " + position);

            Drawable unselectedDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_dot_unselected, null);
            Drawable selectedDrawable   = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_dot_selected, null);

            // set all to unselected
            for (View dotView : dots) {
                dotView.setBackground(unselectedDrawable);
            }

            // set the selected dot
            View selectedView = dots.get(position);
            selectedView.setBackground(selectedDrawable);
        }
        catch (Exception e) {
            Log.e("MyPagerAdapter", "Error: " + e);
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 4;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            Log.d("MyPagerAdapter", "getItem: " + position);
            switch (position) {
                case 0:
                    return FirstFragment.newInstance(0, "First Fragment");

                case 1:
                    return SecondFragment.newInstance(1, "Second Fragment");

                case 2:
                    return ThirdFragment.newInstance(2, "Third Fragment");

                case 3:
                    return FourthFragment.newInstance(3, "Fourth Fragment");

                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

}
