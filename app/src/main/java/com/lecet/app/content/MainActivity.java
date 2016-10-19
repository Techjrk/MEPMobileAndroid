package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.lecet.app.R;
import com.lecet.app.adapters.DashboardPagerAdapter;
import com.lecet.app.adapters.OverflowMenuAdapter;
import com.lecet.app.contentbase.NavigationBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.User;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityMainBinding;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.UserDomain;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MBRDataSource;
import com.lecet.app.interfaces.MBRDelegate;
import com.lecet.app.interfaces.MHSDataSource;
import com.lecet.app.interfaces.MHSDelegate;
import com.lecet.app.interfaces.OverflowMenuCallback;
import com.lecet.app.utility.DateUtility;
import com.lecet.app.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;
import java.util.TreeSet;

import io.realm.Realm;

/**
 * MainActivity Created by jasonm on 8/15/16. This Activity represents the Dashboard, landed on
 * after logging in.
 */
public class MainActivity extends NavigationBaseActivity implements MHSDelegate, MHSDataSource, MBRDelegate, MBRDataSource, OverflowMenuCallback {

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

    ListPopupWindow overflowMenu;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        setTitle(R.string.dashboard_bids);
    }

    public void prevViewPage() {
        //Log.d(TAG, "prevViewPage");
        try {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } catch (Exception e) {
            Log.w(TAG, "prevViewPage: viewPager may be null or page may not exist");
        }
    }

    public void nextViewPage() {
        //Log.d(TAG, "nextViewPage");
        try {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        } catch (Exception e) {
            Log.w(TAG, "nextViewPage: viewPager may be null or page may not exist");
        }
    }


    /**
     * Set up the Fragment Pager which contains the four main fragments used in the upper third of
     * the Dashboard
     */
    private void setupViewPager() {

        viewPager = (ViewPager) findViewById(R.id.dashboard_viewpager);
        viewPager.setOffscreenPageLimit(DashboardPagerAdapter.NUM_ITEMS);
        viewPagerAdapter = new DashboardPagerAdapter(getSupportFragmentManager(), this.getBaseContext());
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                // Notify view model of latest position.
                viewModel.currentPagerPosition(position);

                int title = 0;
                switch (position) {
                    case 0:
                    case 1:
                        title = R.string.dashboard_bids;
                        break;
                    case 2:
                    case 3:
                        title = R.string.dashboard_projects;
                        break;
                }
                setTitle(title);
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
     */
    private void updatePageIndicator(int position) {
        //Log.d(TAG, "updatePageIndicator: " + position);

        Drawable unselectedDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_dot_unselected, null);
        Drawable selectedDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_dot_selected, null);

        // first set all to unselected
        for (View dotView : dots) {
            dotView.setBackground(unselectedDrawable);
        }

        // set the selected dot
        View selectedView = dots.get(position);
        selectedView.setBackground(selectedDrawable);
    }

    private void setupPageButtons() {
        pageLeftButton = (ImageButton) findViewById(R.id.dashboard_page_left_button);
        pageRightButton = (ImageButton) findViewById(R.id.dashboard_page_right_button);
        updateViewPagerArrows();
    }

    /**
     * Update the visibility of the left/right arrow ViewPager buttons so they do not appear on the
     * first or last pages as appropriate
     */
    private void updateViewPagerArrows() {
        if (viewPager.getCurrentItem() == 0) {
            pageLeftButton.setVisibility(View.INVISIBLE);
        } else pageLeftButton.setVisibility(View.VISIBLE);

        if (viewPager.getCurrentItem() == viewPagerAdapter.getCount() - 1) {
            pageRightButton.setVisibility(View.INVISIBLE);
        } else pageRightButton.setVisibility(View.VISIBLE);
    }


    /**
     * MBR Implementation
     **/

    @Override
    public void refreshRecentlyMadeBids(LecetCallback<TreeMap<Long, TreeSet<Bid>>> callback) {

        viewModel.getBidsRecentlyMade(DateUtility.addDays(-30), callback);
    }

    @Override
    public void bidGroupSelected(@BidDomain.BidGroup int group) {

    }

    /**
     * MHS Implementation
     **/
    @Override
    public void refreshProjectsHappeningSoon(final LecetCallback<Project[]> callback) {

        viewModel.getProjectsHappeningSoon(callback);
    }

    @Override
    public void calendarSelected(Date selectedDate) {

        Log.d(TAG, "CalendarSelected: " + selectedDate.toString());
        viewModel.fetchProjectsByBidDate(selectedDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_item_arrow:
                return true;
            case R.id.menu_item_folder:
                return true;
            case R.id.menu_item_search:
                return true;
            case R.id.menu_item_more:
                toogleOverflowMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Call this when you want to show the ListPopupWindow
    private void toogleOverflowMenu() {
        if (overflowMenu == null) {
            createOverflowMenu(findViewById(R.id.menu_item_more));
        }
        overflowMenu.show();
    }

    private void createOverflowMenu(View anchor) {
        if (overflowMenu == null) {
            overflowMenu = new ListPopupWindow(this);

            UserDomain userDomain = new UserDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
            User user = userDomain.fetchLoggedInUser();

            OverflowMenuAdapter adapter = new OverflowMenuAdapter(this, user, getResources().getStringArray(R.array.overflow_menu_options));

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x - getResources().getDimensionPixelSize(R.dimen.overflow_menu_width);
            int offset = (int) (width * 0.875); //enough to show it below more item menu
            overflowMenu.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.overflow_menu_background));
            overflowMenu.setAnchorView(anchor);
            overflowMenu.setModal(true);
            overflowMenu.setWidth(width);
            overflowMenu.setHeight(getResources().getDimensionPixelSize(R.dimen.overflow_menu_height));
            overflowMenu.setHorizontalOffset(-offset);
            overflowMenu.setAdapter(adapter);
            overflowMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                        case 1:
                            MainActivity.this.onProfileClicked();
                            break;
                        case 2:
                            MainActivity.this.onHiddenObjectsClicked();
                            break;
                        case 3:
                            MainActivity.this.onSettingsClicked();
                            break;
                    }
                }
            }); // the callback for when a list item is selected
        }
    }

    @Override
    public void onProfileClicked() {

    }

    @Override
    public void onHiddenObjectsClicked() {

    }

    @Override
    public void onSettingsClicked() {

    }
}
