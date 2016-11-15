package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.MenuTitleListAdapter;
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

    private ListPopupWindow moveMenu;
    private MoveToAdapter moveToAdapter;
    private ListPopupWindow mtmSortMenu;
    private MenuTitleListAdapter mtmSortAdapter;
    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private ImageView sortButton;

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

    private void toogleMoveMenu(View view) {
        if (moveMenu == null) {
            createMoveMenu(view);
        } else {
            moveToAdapter.setProjectTrackingList(trackingListDomain.fetchUserProjectTrackingList());
        }
        moveMenu.show();
    }

    private void createMoveMenu(View anchor) {
        if (moveMenu == null) {
            moveMenu = new ListPopupWindow(appCompatActivity);

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
            moveMenu.setBackgroundDrawable(ContextCompat.getDrawable(appCompatActivity, R.drawable.more_menu_upsidedown_background));
            moveMenu.setAnchorView(anchor);
            moveMenu.setModal(true);
            moveMenu.setWidth(width);
            moveMenu.setHorizontalOffset(-offset);
            moveMenu.setAdapter(moveToAdapter);
        }
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
            mtmSortMenu.setBackgroundDrawable(ContextCompat.getDrawable(appCompatActivity, R.drawable.overflow_menu_background));
            mtmSortMenu.setAnchorView(anchor);
            mtmSortMenu.setModal(true);
            mtmSortMenu.setWidth(width);
            mtmSortMenu.setHorizontalOffset(-offset);
            mtmSortMenu.setVerticalOffset(anchor.getHeight());
            mtmSortMenu.setAdapter(mtmSortAdapter);
            mtmSortMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TODO sort the current project list
                }
            }); // the callback for when a list item is selected
        }
    }

    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);
        backButton = (ImageView) toolbar.findViewById(R.id.back_button);
        sortButton = (ImageView) toolbar.findViewById(R.id.sort_menu_button);

        //TODO - check the binding in the layout, which is not triggering the button clicks in this VM
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonClick(v);
            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSortButtonClick(v);
            }
        });
        titleTextView.setText(title);
        subtitleTextView.setText(subtitle);
    }

    @Override
    public void onProjectTrackingListClicked(ProjectTrackingList projectTrackingList) {
        //TODO call the things to change the project of list
    }

    @Override
    public void onCompanyTrackingListClicked(CompanyTrackingList companyTrackingList) {
        //DO NOTHING
    }

    public void onMoveButtonClicked(View view) {
        toogleMoveMenu(view);
    }

    public void onRemoveButtonClicked(View view) {
        Toast.makeText(appCompatActivity, "Remove button clicked", Toast.LENGTH_SHORT).show();
    }

    public void onBackButtonClick(View view) {
        appCompatActivity.onBackPressed();
    }

    public void onSortButtonClick(View view) {
        toogleMTMSortMenu(view);
    }

}
