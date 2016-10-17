package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;

import com.lecet.app.R;
import com.lecet.app.adapters.DashboardPagerAdapter;
import com.lecet.app.contentbase.NavigationBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityMainBinding;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MBRDataSource;
import com.lecet.app.interfaces.MBRDelegate;
import com.lecet.app.interfaces.MHSDataSource;
import com.lecet.app.interfaces.MHSDelegate;
import com.lecet.app.utility.DateUtility;
import com.lecet.app.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;
import java.util.TreeSet;

import io.realm.Realm;

/**
 * MainActivity Created by jasonm on 8/15/16.
 * This Activity represents the Dashboard, landed on after logging in.
 */
public class MainActivity extends NavigationBaseActivity implements MHSDelegate, MHSDataSource, MBRDelegate, MBRDataSource {

    private static final String TAG = "MainActivity";

    private MainViewModel viewModel;

    // Fragment Pager
    ViewPager viewPager;
    FragmentPagerAdapter viewPagerAdapter;
    ArrayList<View> dots;
    View dot_1;
    View dot_2;
    View dot_3;
    View dot_4;

    ImageButton pageLeftButton;
    ImageButton pageRightButton;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        //setContentView(R.layout.activity_main);
        setupBinding();
        setupToolbar();
        setupViewPager();
        setupPageIndicator();
        setupPageButtons();
    }

    private void setupBinding() {
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        BidDomain bidDomain = new BidDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        Calendar calendar = Calendar.getInstance();
        viewModel = new MainViewModel(this, bidDomain, projectDomain, calendar);
        binding.setViewModel(viewModel);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void prevViewPage() {
        //Log.d(TAG, "prevViewPage");
        try {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
        catch (Exception e) {
            Log.w(TAG, "prevViewPage: viewPager may be null or page may not exist" );
        }
    }

    public void nextViewPage() {
        //Log.d(TAG, "nextViewPage");
        try {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
        catch (Exception e) {
            Log.w(TAG, "nextViewPage: viewPager may be null or page may not exist" );
        }
    }


    /**
     * Set up the Fragment Pager which contains the four main fragments used
     * in the upper third of the Dashboard
     */
    private void setupViewPager() {

        viewPager = (ViewPager) findViewById(R.id.dashboard_viewpager);
        viewPager.setOffscreenPageLimit(DashboardPagerAdapter.NUM_ITEMS);
        viewPagerAdapter = new DashboardPagerAdapter(getSupportFragmentManager(), this.getBaseContext());
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
                //Log.d(TAG, "onPageScrollStateChanged: " + state);
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:       // 0
                        updateViewPagerArrows();
                        break;

                    case ViewPager.SCROLL_STATE_DRAGGING:   // 1
                        break;

                    case ViewPager.SCROLL_STATE_SETTLING:   // 2
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

    private void setupPageButtons() {
        pageLeftButton  = (ImageButton) findViewById(R.id.dashboard_page_left_button);
        pageRightButton = (ImageButton) findViewById(R.id.dashboard_page_right_button);
        updateViewPagerArrows();
    }

    /**
     * Update the visibility of the left/right arrow ViewPager buttons
     * so they do not appear on the first or last pages as appropriate
     */
    private void updateViewPagerArrows() {
        if(viewPager.getCurrentItem() == 0) {
            pageLeftButton.setVisibility(View.INVISIBLE);
        }
        else pageLeftButton.setVisibility(View.VISIBLE);

        if(viewPager.getCurrentItem() == viewPagerAdapter.getCount() - 1) {
            pageRightButton.setVisibility(View.INVISIBLE);
        }
        else pageRightButton.setVisibility(View.VISIBLE);
    }





    /** MBR Implementation **/

    @Override
    public void refreshRecentlyMadeBids(LecetCallback<TreeMap<Long, TreeSet<Bid>>> callback) {

        viewModel.getBidsRecentlyMade(DateUtility.addDays(-30), callback);
    }

    @Override
    public void bidGroupSelected(@BidDomain.BidGroup int group) {

    }

    /** MHS Implementation **/
    @Override
    public void refreshProjectsHappeningSoon(final LecetCallback<Project[]> callback) {

        viewModel.getProjectsHappeningSoon(callback);
    }

    @Override
    public void calendarSelected(Date selectedDate) {

        Log.d(TAG, "CalendarSelected: " + selectedDate.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity, menu);
        return true;
    }
}
