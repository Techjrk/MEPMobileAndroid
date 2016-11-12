package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.MenuTitleListAdapter;
import com.lecet.app.content.ProjectTrackingListSortedActivity;
import com.lecet.app.domain.BidDomain;
import com.lecet.app.domain.ProjectDomain;

/**
 * Created by Josué Rodríguez on 12/11/2016.
 */

public class ProjectTrackingListSortedViewModel extends BaseObservable {

    private final BidDomain bidDomain;
    private final ProjectDomain projectDomain;
    private final AppCompatActivity appCompatActivity;

    private ListPopupWindow mtmSortMenu;
    private MenuTitleListAdapter mtmSortAdapter;

    private String projectsSelected;

    public ProjectTrackingListSortedViewModel(AppCompatActivity appCompatActivity, BidDomain bidDomain, ProjectDomain projectDomain) {
        this.appCompatActivity = appCompatActivity;
        this.bidDomain = bidDomain;
        this.projectDomain = projectDomain;
    }

    @Bindable
    public String getProjectsSelected() {
        return projectsSelected;
    }

    public void setProjectsSelected(String projectsSelected) {
        this.projectsSelected = projectsSelected;
        notifyPropertyChanged(BR.projectsSelected);
    }

    public void onMoveButtonClicked(View view) {
        toogleMTMSortMenu(view);
    }

    public void onRemoveButtonClicked(View view) {
        Toast.makeText(appCompatActivity, "Remove button clicked", Toast.LENGTH_SHORT).show();
    }

    private void toogleMTMSortMenu(View view) {
        if (mtmSortMenu == null) {
            createMTMSortMenu(view);
        }
        mtmSortMenu.show();
    }

    private void createMTMSortMenu(View anchor) {
        if (mtmSortMenu == null) {
            mtmSortMenu = new ListPopupWindow(appCompatActivity);

            mtmSortAdapter
                    = new MenuTitleListAdapter(appCompatActivity
                    , appCompatActivity.getResources().getString(R.string.mtm_sort_menu_title)
                    , appCompatActivity.getResources().getStringArray(R.array.mobile_tracking_list_sort_menu));

            Display display = appCompatActivity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x - appCompatActivity.getResources().getDimensionPixelSize(R.dimen.mtm_sort_menu_space);
            int[] coordinates = new int[2];
            anchor.getLocationOnScreen(coordinates);
            int offset = (int) (coordinates[0]
                    - (appCompatActivity.getResources().getDimensionPixelSize(R.dimen.mtm_sort_menu_space) / 2.0));
            mtmSortMenu.setBackgroundDrawable(ContextCompat.getDrawable(appCompatActivity, R.drawable.more_menu_upsidedown_background));
            mtmSortMenu.setAnchorView(anchor);
            mtmSortMenu.setModal(true);
            mtmSortMenu.setWidth(width);
            mtmSortMenu.setHorizontalOffset(-offset);
//            mtmSortMenu.setVerticalOffset(anchor.getHeight());
            mtmSortMenu.setAdapter(mtmSortAdapter);
            mtmSortMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TODO send the sort option selected
                    appCompatActivity.startActivity(new Intent(appCompatActivity, ProjectTrackingListSortedActivity.class));
                }
            }); // the callback for when a list item is selected
        }
    }

}
