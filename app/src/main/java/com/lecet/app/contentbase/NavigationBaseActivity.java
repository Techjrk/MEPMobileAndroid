package com.lecet.app.contentbase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.lecet.app.R;
import com.lecet.app.content.CompanyDetailProjectBidsActivity;
import com.lecet.app.content.MobileProjTrackingListActivity;
import com.lecet.app.content.ProfileActivity;
import com.lecet.app.content.SettingsActivity;


/**
 * NavigationBaseActivity
 * Base class for Activities which use the sliding-drawer menu.
 */
public class NavigationBaseActivity extends AppCompatActivity {

    private final String TAG = "NavigationBaseActivity";

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }*/

    /**
     * Menu Navigation Click Handler
     * @param item
     */
    public void onMenuItemClicked(MenuItem item) {
        Log.d(TAG, "onMenuItemClicked: Title: " + item.getTitle());

        Intent intent = null;
        Class launchClass = null;

        switch (item.getItemId()) {
            case R.id.menu_item_home:
                launchClass = NavigationBaseActivity.class;   //TODO - change
                break;

            case R.id.menu_item_profile:
                launchClass = ProfileActivity.class;
                break;

            case R.id.menu_item_list1:
                launchClass = CompanyDetailProjectBidsActivity.class;
                break;

            case R.id.menu_item_list2:
                launchClass = MobileProjTrackingListActivity.class;
                break;

            case R.id.menu_item_settings:
                launchClass = SettingsActivity.class;
                break;

            case R.id.menu_item_help:
                launchClass = NavigationBaseActivity.class;   //TODO - change
                break;

            default:
                launchClass = NavigationBaseActivity.class;   //TODO - change
                break;
        }

        intent = new Intent(this, launchClass);
        this.startActivity(intent);
    }

}
