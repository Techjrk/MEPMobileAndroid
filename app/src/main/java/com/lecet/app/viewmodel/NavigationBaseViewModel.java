package com.lecet.app.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.lecet.app.R;
import com.lecet.app.content.CompanyDetailProjectBidsActivity;
import com.lecet.app.content.MobileProjTrackingListActivity;
import com.lecet.app.content.ProfileActivity;
import com.lecet.app.content.SettingsActivity;
import com.lecet.app.contentbase.NavigationBaseActivity;

/**
 * UNUSED - MAY NOT NEED DEPENDING ON HOW MENUS ARE SET UP. SEE NavigationBaseActivity.
 *
 * File: NavigationBaseViewModel
 * Created: 8/23/16
 * Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class NavigationBaseViewModel extends BaseObservable {

    private final String TAG = "NavigationBaseViewModel";

    private final Context context;

    private ImageView headerImage;


    public NavigationBaseViewModel(Context context) {

        this.context = context;
    }

    @BindingAdapter({"android:src"})
    public static void setHeaderImage(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

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

        intent = new Intent(this.context, launchClass);        //TODO - check?
        this.context.startActivity(intent);
    }

}
