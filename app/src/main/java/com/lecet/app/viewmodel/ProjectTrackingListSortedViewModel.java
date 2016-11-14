package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.MoveToAdapter;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.interfaces.MTMMenuCallback;

import java.util.List;

/**
 * Created by Josué Rodríguez on 12/11/2016.
 */

public class ProjectTrackingListSortedViewModel extends BaseObservable implements MTMMenuCallback {

    private final AppCompatActivity appCompatActivity;
    private final TrackingListDomain trackingListDomain;

    private ListPopupWindow mtmSortMenu;
    private MoveToAdapter moveToAdapter;

    private String projectsSelected;

    public ProjectTrackingListSortedViewModel(AppCompatActivity appCompatActivity, TrackingListDomain trackingListDomain) {
        this.appCompatActivity = appCompatActivity;
        this.trackingListDomain = trackingListDomain;
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
        } else {
            moveToAdapter.setProjectTrackingList(trackingListDomain.fetchUserProjectTrackingList());
        }
        mtmSortMenu.show();
    }

    private void createMTMSortMenu(View anchor) {
        if (mtmSortMenu == null) {
            mtmSortMenu = new ListPopupWindow(appCompatActivity);

            List<ProjectTrackingList> projectTrackingLists = trackingListDomain.fetchUserProjectTrackingList();
            moveToAdapter
                    = new MoveToAdapter(appCompatActivity
                    , appCompatActivity.getResources().getString(R.string.move_to)
                    , projectTrackingLists, this);

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
            mtmSortMenu.setAdapter(moveToAdapter);
        }
    }

    @Override
    public void onProjectTrackingListClicked(ProjectTrackingList projectTrackingList) {
        //TODO call the things to change the project of list
    }

    @Override
    public void onCompanyTrackingListClicked(CompanyTrackingList companyTrackingList) {
        //DO NOTHING
    }
}
