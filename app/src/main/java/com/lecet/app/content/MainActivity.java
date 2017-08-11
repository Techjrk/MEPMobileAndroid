package com.lecet.app.content;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.actions.SearchIntents;
import com.lecet.app.R;
import com.lecet.app.adapters.DashboardPagerAdapter;
import com.lecet.app.adapters.MTMMenuAdapter;
import com.lecet.app.adapters.OverflowMenuAdapter;
import com.lecet.app.contentbase.LecetBaseActivity;
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
import com.lecet.app.domain.SearchDomain;
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
import com.lecet.app.utility.Log;
import com.lecet.app.utility.TextViewUtility;
import com.lecet.app.viewmodel.MainViewModel;
import com.lecet.app.viewmodel.SearchFilterAllTabbedViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;
import java.util.TreeSet;

import io.realm.Realm;
import io.realm.RealmChangeListener;

import static com.lecet.app.content.ProjectsNearMeActivity.EXTRA_VOICE_ACTIVATED;

/**
 * MainActivity Created by jasonm on 8/15/16. This Activity represents the Dashboard, landed on
 * after logging in.
 */
public class MainActivity extends LecetBaseActivity implements MHSDelegate, MHSDataSource, MBRDelegate, MBRDataSource, OverflowMenuCallback, MRADataSource,
        MRADelegate, MRUDelegate, MRUDataSource, MTMMenuCallback {

    private static final String TAG = "MainActivity";
    private static final int VOICE_LAUNCH_CODE_NONE = -1;
    private static final int VOICE_LAUNCH_CODE_PROJECTS_NEAR_ME = 0;
    private static final int VOICE_LAUNCH_CODE_PROJECTS_UPDATED_RECENTLY = 1;
    private static final int VOICE_LAUNCH_CODE_TRACKING_LISTS = 2;
    private static final int VOICE_LAUNCH_CODE_PROJECT_TRACKING_LISTS = 3;
    private static final int VOICE_LAUNCH_CODE_COMPANY_TRACKING_LISTS = 4;

    private MainViewModel viewModel;
    private SearchDomain searchDomain;

    private static AlertDialog.Builder dialogBuilder;
    private String errorMessage = null;

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
        Intent intent = getIntent();

        setupBinding();
        setupToolbar();

        int voiceLaunchCode = checkForVoiceActivation(intent);

        if (isNetworkConnected()) {
            setupViewPager(voiceLaunchCode);
            setupPageIndicator();
            setupPageButtons();
        }
    }

    /*
      Voice Activation - TODO: move to view model
      Required Phrasing:
      "Search for Projects Near Me in MEP"
      "Search for Projects Recently Updated in MEP"
      "Search for Tracking Lists in MEP"
     */

    private int checkForVoiceActivation(Intent intent) {
        Log.d(TAG, "checkForVoiceActivation");

        if(SearchIntents.ACTION_SEARCH.equals(intent.getAction())) {

            String query = intent.getStringExtra(SearchManager.QUERY);
            if((query == null || query.isEmpty())) {
                finish();
                return VOICE_LAUNCH_CODE_NONE;
            }

            Intent newIntent;

            // Launch Projects Near Me and go directly to Table View
            if(matchesPhraseProjectsNearMe(query)){
                Log.d(TAG, "checkForVoiceActivation: MATCH with Projects Near Me");
                newIntent = new Intent(this, ProjectsNearMeActivity.class);
                newIntent.putExtra(EXTRA_VOICE_ACTIVATED, true);

                startActivity(newIntent);
                finish();   // removing this finish() call would mean that the user returns to the MainActivity after backing out of the list activity
                return VOICE_LAUNCH_CODE_PROJECTS_NEAR_ME;
            }

            // Stay on Dashboard (i.e. MainActivity) and to Projects Updated Recently tab
            else if(matchesPhraseProjectsUpdatedRecently(query)) {
                Log.d(TAG, "checkForVoiceActivation: MATCH with Projects Recently Updated");
                //functionality will be handled in rest of onCreate() flow in this Activity
                return VOICE_LAUNCH_CODE_PROJECTS_UPDATED_RECENTLY;
            }

            // Stay on Dashboard (i.e. MainActivity) and show the Tracking Lists dropdown menu
            else if(matchesPhraseProjectTrackingList(query) || matchesPhraseCompanyTrackingList(query)){
                Log.d(TAG, "checkForVoiceActivation: MATCH with either Project or Company Tracking Lists");

                final int voiceLaunchCode;
                if(matchesPhraseProjectTrackingList(query)) {
                    voiceLaunchCode = VOICE_LAUNCH_CODE_PROJECT_TRACKING_LISTS;
                }
                else if(matchesPhraseCompanyTrackingList(query)) {
                    voiceLaunchCode = VOICE_LAUNCH_CODE_COMPANY_TRACKING_LISTS;
                }
                else voiceLaunchCode = -1;

                //create a handler to delay a call and show the progress screen in the meantime //TODO - convert this to a listener for when the layouts have completed being drawn
                viewModel.showProgressDialog();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "onCreate: opening MTM menu");
                        ActionMenuItemView folderMenu = (ActionMenuItemView) findViewById(R.id.menu_item_folder);
                        Log.d(TAG, "onCreate: folderMenu: " + folderMenu);
                        toogleMTMMenu(voiceLaunchCode);
                        viewModel.dismissProgressDialog();
                    }
                }, 1000);
                return voiceLaunchCode;
            }
            else {
                finish();
            }
        }

        return VOICE_LAUNCH_CODE_NONE;
    }

    private boolean matchesPhraseProjectsNearMe(String rawPhrase){
        if(rawPhrase == null) return false;

        String phrase = rawPhrase.toLowerCase();
        if(phrase.contains(getString(R.string.voice_project_near_me))
        || phrase.contains(getString(R.string.voice_projects_near_me))
        || phrase.contains(getString(R.string.voice_projects_by_me))
        || phrase.contains(getString(R.string.voice_project_by_me))
        || phrase.contains(getString(R.string.voice_projects_nearby))) {
            Log.d(TAG, "matchesPhraseProjectsNearMe: MATCH FOUND. query phrase was: " + rawPhrase);
            return true;
        }
        Log.d(TAG, "matchesPhraseProjectsNearMe: query phrase was: " + rawPhrase);
        return false;
    }

    private boolean matchesPhraseProjectsUpdatedRecently(String rawPhrase) {
        if(rawPhrase == null) return false;

        String phrase = rawPhrase.toLowerCase();
        if(phrase.contains(getString(R.string.voice_projects_updated_recently))
        || phrase.contains(getString(R.string.voice_projects_recently_updated))
        || phrase.contains(getString(R.string.voice_recent_project_updates))
        || phrase.contains(getString(R.string.voice_recently_updated_projects))
        || phrase.contains(getString(R.string.voice_recent_projects))) {
            Log.d(TAG, "matchesPhraseProjectsUpdatedRecently: MATCH FOUND. query phrase was: " + rawPhrase);
            return true;
        }
        Log.d(TAG, "matchesPhraseProjectsUpdatedRecently: query phrase was: " + rawPhrase);
        return false;
    }

    private boolean matchesPhraseProjectTrackingList(String rawPhrase){
        if(rawPhrase == null) return false;
        String phrase = rawPhrase.toLowerCase();
        if(phrase.contains(getString(R.string.voice_project_tracking_list))
        || phrase.contains(getString(R.string.voice_project_tracking_lists))){
            Log.d(TAG, "matchesPhraseProjectTrackingList: MATCH FOUND. query phrase was: " + rawPhrase);
            return true;
        }
        Log.d(TAG, "matchesPhraseProjectTrackingList: query phrase was: " + rawPhrase);
        return false;
    }

    private boolean matchesPhraseCompanyTrackingList(String rawPhrase){
        if(rawPhrase == null) return false;
        String phrase = rawPhrase.toLowerCase();
        if(phrase.contains(getString(R.string.voice_company_tracking_list))
        || phrase.contains(getString(R.string.voice_company_tracking_lists))){
            Log.d(TAG, "matchesPhraseCompanyTrackingList: MATCH FOUND. query phrase was: " + rawPhrase);
            return true;
        }
        Log.d(TAG, "matchesPhraseCompanyTrackingList: query phrase was: " + rawPhrase);
        return false;
    }


    /*
     Methods
     */

    @Override
    protected void onResume() {
        super.onResume();


        SearchViewModel.companyInstantSearch=false;
        //clear the Shared pref for Project Type
        clearSharedPref(getString(R.string.FilterTypeData));
        //clear the Shared pref for Stage with name content
        clearSharedPref(getString(R.string.FilterStageData)+"name");
        //clear the Shared pref for Stage with view type content
        clearSharedPref(getString(R.string.FilterStageData)+"view");
        clearSharedPref(getString(R.string.FilterSharedJData));

        clearSharedPref(getString(R.string.LastCheckedJurisdictionItems));
        clearSharedPref(getString(R.string.lastcheckedTypeItems));
        clearSharedPref(getString(R.string.lastcheckedStageItems));

        //clear the Shared pref for other filters
        clearSharedPref(getString(R.string.Filter));
      //  if (viewModel !=null) viewModel.refreshMRA();
        if (SearchFilterAllTabbedViewModel.userCreated) {
            SearchFilterAllTabbedViewModel.userCreated=false;
            Intent intent = new Intent(this, DashboardIntermediaryActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void clearSharedPref(String dataName) {
      SharedPreferences spref = getSharedPreferences(dataName, Context.MODE_PRIVATE);
      if (spref == null) return;
      SharedPreferences.Editor editData = spref.edit();
      editData.clear();
      editData.commit();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

        if (isConnected && viewPager == null) {

            setupViewPager(-1);
            setupPageIndicator();
            setupPageButtons();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (overflowMenu != null && overflowMenu.isShowing()) {
            overflowMenu.dismiss();
        }

        if (mtmMenu != null && mtmMenu.isShowing()) {
            mtmMenu.dismiss();
        }
    }

    private void setupBinding() {
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        BidDomain bidDomain = new BidDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        searchDomain = new SearchDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
        TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance(), new RealmChangeListener() {
            @Override
            public void onChange(Object element) {

            }
        });

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
    private void setupViewPager(int voiceLaunchCode) {

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

        if(voiceLaunchCode > -1) {
            viewPager.setCurrentItem(3);
        }

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
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String selectDate = df.format(selectedDate);
        String prevDate = (viewModel.getPreviousDate() == null) ? null : df.format(viewModel.getPreviousDate());
       if (!selectDate.equals(prevDate))
        {  viewModel.fetchProjectsByBidDate(selectedDate);
           viewModel.setPreviousDate(selectedDate);
       } else {
           viewModel.clickRefreshMHS();

       }

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
        Log.d(TAG, "onOptionsItemSelected: " + item);
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_item_arrow:
                startActivity(new Intent(this, ProjectsNearMeActivity.class));
                return true;
            case R.id.menu_item_folder:
                toogleMTMMenu(-1);
                return true;
            case R.id.menu_item_search:
                SearchViewModel.setInitSearch(true);
//                SearchViewModel.INIT_SEARCH=true;
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

    private void toogleMTMMenu(int voiceLaunchCode) {
        Log.d(TAG, "toogleMTMMenu: voiceLaunchCode: " + voiceLaunchCode);
        if (mtmMenu == null) {
            createMTMMenu(findViewById(R.id.menu_item_folder));
        } else {
            TrackingListDomain trackingListDomain = new TrackingListDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance());
            mtmAdapter.setCompanyTrackingList(trackingListDomain.fetchUserCompanyTrackingList());
            mtmAdapter.setProjectTrackingList(trackingListDomain.fetchUserProjectTrackingList());
            mtmAdapter.notifyDataSetChanged();
        }
        if(mtmMenu != null) mtmMenu.show();

        if(voiceLaunchCode == VOICE_LAUNCH_CODE_PROJECT_TRACKING_LISTS) {
            mtmAdapter.setProjectListVisible(true, true);
            mtmAdapter.setCompanyListVisible(false, true);
        }
        else if(voiceLaunchCode == VOICE_LAUNCH_CODE_COMPANY_TRACKING_LISTS) {
            mtmAdapter.setCompanyListVisible(true, true);
            mtmAdapter.setProjectListVisible(false, true);
        }

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
        Log.d(TAG, "createMTMMenu: " + anchor);
        if(anchor == null) return;

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

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onHiddenObjectsClicked() {

        Intent intent = new Intent(this, HiddenProjectsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSettingsClicked() {

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onProjectTrackingListClicked(ProjectTrackingList projectTrackingList) {

        // Dismiss menu so that it refreshes properly
        mtmMenu.dismiss();

        Intent intent = new Intent(getBaseContext(), ProjectTrackingListActivity.class);
        intent.putExtra(TrackingListActivity.PROJECT_LIST_ITEM_ID, projectTrackingList.getId());
        intent.putExtra(TrackingListActivity.PROJECT_LIST_ITEM_SIZE, projectTrackingList.getProjects().size());
        intent.putExtra(TrackingListActivity.PROJECT_LIST_ITEM_TITLE, projectTrackingList.getName());
        startActivity(intent);
    }

    @Override
    public void onCompanyTrackingListClicked(CompanyTrackingList companyTrackingList) {

        // Dismiss menu so that it refreshes properly
        mtmMenu.dismiss();

        Intent intent = new Intent(getBaseContext(), CompanyTrackingListActivity.class);
        intent.putExtra(TrackingListActivity.PROJECT_LIST_ITEM_ID, companyTrackingList.getId());
        intent.putExtra(TrackingListActivity.PROJECT_LIST_ITEM_SIZE, companyTrackingList.getCompanies().size());
        intent.putExtra(TrackingListActivity.PROJECT_LIST_ITEM_TITLE, companyTrackingList.getName());
        startActivity(intent);
    }
}
