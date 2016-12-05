package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lecet.app.R;
import com.lecet.app.adapters.DashboardPagerAdapter;
import com.lecet.app.adapters.MTMMenuAdapter;
import com.lecet.app.adapters.OverflowMenuAdapter;
import com.lecet.app.contentbase.NavigationBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.data.models.User;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityMainBinding;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.domain.UserDomain;
import com.lecet.app.enums.LacetFont;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MBRDataSource;
import com.lecet.app.interfaces.MBRDelegate;
import com.lecet.app.interfaces.MHSDataSource;
import com.lecet.app.interfaces.MHSDelegate;
import com.lecet.app.interfaces.MRADataSource;
import com.lecet.app.interfaces.MRADelegate;
import com.lecet.app.interfaces.MRUDataSource;
import com.lecet.app.interfaces.MRUDelegate;
import com.lecet.app.interfaces.MTMMenuCallback;
import com.lecet.app.interfaces.OverflowMenuCallback;
import com.lecet.app.utility.DateUtility;
import com.lecet.app.utility.TextViewUtility;
import com.lecet.app.viewmodel.MainViewModel;

import java.lang.reflect.Field;
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
public class MainActivity extends NavigationBaseActivity implements MHSDelegate, MHSDataSource, MBRDelegate, MBRDataSource, OverflowMenuCallback, MRADataSource,
        MRADelegate, MRUDelegate, MRUDataSource, MTMMenuCallback {

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
    ListPopupWindow mtmMenu;
    MTMMenuAdapter mtmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupBinding();
        setupToolbar();

        if (isNetworkConnected()) {

            setupViewPager();
            setupPageIndicator();
            setupPageButtons();
            viewModel.getTrackingLists();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

        if (isConnected && viewPager == null) {

            setupViewPager();
            setupPageIndicator();
            setupPageButtons();
            viewModel.getTrackingLists();
        }
    }

    private void setupBinding() {
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        BidDomain bidDomain = new BidDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        Calendar calendar = Calendar.getInstance();
        viewModel = new MainViewModel(this, bidDomain, projectDomain, calendar, trackingListDomain);
        binding.setViewModel(viewModel);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.dashboard_projects);
        TextView toolbarTitle = null;
        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            toolbarTitle = (TextView) f.get(toolbar);
            TextViewUtility.changeTypeface(toolbarTitle, LacetFont.LATO_REGULAR);
            toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 23);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
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
                setTitle(R.string.dashboard_projects);
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

        viewModel.fetchBidsRecentlyMade(group, DateUtility.addDays(-30));
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

        viewModel.fetchProjectsByBidDate(selectedDate);
    }

    /**
     * MRA Implementation
     **/

    @Override
    public void refreshRecentlyAddedProjects(LecetCallback<TreeMap<Long, TreeSet<Project>>> callback) {

        viewModel.getProjectsRecentlyAdded(callback);
    }

    @Override
    public void mraBidGroupSelected(@BidDomain.BidGroup int group) {

        Date publishDate = DateUtility.addDays(-30);
        viewModel.fetchProjectsRecentlyAdded(group, publishDate);
    }


    /**
     * MRU Implementation
     **/

    @Override
    public void refreshRecentlyUpdatedProjects(LecetCallback<TreeMap<Long, TreeSet<Project>>> callback) {

        viewModel.getProjectsRecentlyUpdated(callback);
    }

    @Override
    public void mruBidGroupSelected(@BidDomain.BidGroup int group) {

        Date publishDate = DateUtility.addDays(-30);
        viewModel.fetchProjectsRecentlyUpdated(group, publishDate);
    }


    /**
     * Menu
     **/

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
                startActivity(new Intent(this, ProjectsNearMeActivity.class));
                return true;
            case R.id.menu_item_folder:
                toogleMTMMenu();
                return true;
            case R.id.menu_item_search:
                startActivity(new Intent(this, SearchActivity.class));
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

    private void toogleMTMMenu() {
        if (mtmMenu == null) {
            createMTMMenu(findViewById(R.id.menu_item_folder));
        } else {
            TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
            mtmAdapter.setCompanyTrackingList(trackingListDomain.fetchUserCompanyTrackingList());
            mtmAdapter.setProjectTrackingList(trackingListDomain.fetchUserProjectTrackingList());
        }
        mtmMenu.show();
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
            int width = size.x - getResources().getDimensionPixelSize(R.dimen.overflow_menu_margin_space);
            int[] coordinates = new int[2];
            anchor.getLocationOnScreen(coordinates);
            int offset = (int) (coordinates[0] - (getResources().getDimensionPixelSize(R.dimen.overflow_menu_margin_space) / 2.0));
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

    private void createMTMMenu(View anchor) {
        if (mtmMenu == null) {
            mtmMenu = new ListPopupWindow(this);

            TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());

            mtmAdapter
                    = new MTMMenuAdapter(this
                    , getResources().getStringArray(R.array.mtm_menu)
                    , trackingListDomain.fetchUserProjectTrackingList()
                    , trackingListDomain.fetchUserCompanyTrackingList()
                    , this);


            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x - getResources().getDimensionPixelSize(R.dimen.overflow_menu_margin_space);
            int[] coordinates = new int[2];
            anchor.getLocationOnScreen(coordinates);
            int offset = (int) (coordinates[0] - (getResources().getDimensionPixelSize(R.dimen.overflow_menu_margin_space) / 2.0));

            mtmMenu.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.mtm_back));
            mtmMenu.setAnchorView(anchor);
            mtmMenu.setModal(true);
            mtmMenu.setWidth(width);
            mtmMenu.setHorizontalOffset(-offset);
            mtmMenu.setAdapter(mtmAdapter);
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

    @Override
    public void onProjectTrackingListClicked(ProjectTrackingList projectTrackingList) {
        Intent intent = new Intent(getBaseContext(), ProjectTrackingListActivity.class);
        intent.putExtra(ProjectTrackingListActivity.PROJECT_LIST_ITEM_ID, projectTrackingList.getId());
        intent.putExtra(ProjectTrackingListActivity.PROJECT_LIST_ITEM_SIZE, projectTrackingList.getProjects().size());
        intent.putExtra(ProjectTrackingListActivity.PROJECT_LIST_ITEM_TITLE, projectTrackingList.getName());
        startActivity(intent);
    }

    @Override
    public void onCompanyTrackingListClicked(CompanyTrackingList companyTrackingList) {
        //TODO open activity?
        Toast.makeText(this, "Company Tracking List Clicked", Toast.LENGTH_SHORT).show();
    }
}
