package com.lecet.app.viewmodel;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Point;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.MenuTitleListAdapter;
import com.lecet.app.adapters.ModifyListAdapter;
import com.lecet.app.adapters.MoveToAdapter;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.interfaces.MTMMenuCallback;
import com.lecet.app.interfaces.MoveToListCallback;
import com.lecet.app.interfaces.TrackedObject;
import com.lecet.app.interfaces.TrackingListObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * File: ModifyTrackingListViewModel Created: 12/7/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public abstract class ModifyTrackingListViewModel<T extends RealmObject & TrackingListObject, U extends RealmObject & TrackedObject> extends BaseObservable implements MoveToListCallback<T>, AdapterView.OnItemClickListener {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SORT_BID_DATE, SORT_LAST_UPDATE, SORT_DATE_ADDED, SORT_VALUE_HIGH, SORT_VALUE_LOW, SORT_COMPANY_NAME, SORT_COMPANY_UPDATED, SORT_NONE})
    public @interface TrackingSort {
    }

    public static final int SORT_BID_DATE = 0;
    public static final int SORT_LAST_UPDATE = 1;
    public static final int SORT_DATE_ADDED = 2;
    public static final int SORT_VALUE_HIGH = 3;
    public static final int SORT_VALUE_LOW = 4;
    public static final int SORT_COMPANY_NAME = 5;
    public static final int SORT_COMPANY_UPDATED = 6;
    public static final int SORT_NONE = 7;

    private final AppCompatActivity appCompatActivity;

    private Dialog dialog;
    private ListPopupWindow moveMenu;
    private MoveToAdapter moveToAdapter;
    private ListPopupWindow mtmSortMenu;
    private MenuTitleListAdapter mtmSortAdapter;
    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private ImageView sortButton;
    private ListView listView;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    RelativeLayout rfooter;
    private T trackingList;
    private RealmResults<U> dataItems;
    private String objectsSelected;

    @TrackingSort
    int selectedSort;

    public ModifyTrackingListViewModel(AppCompatActivity appCompatActivity, T trackingList, @TrackingSort int sortBy) {
        this.appCompatActivity = appCompatActivity;
        this.trackingList = trackingList;
        this.selectedSort = sortBy;
        rfooter = (RelativeLayout) appCompatActivity.findViewById(R.id.project_track_footer);
        this.dataItems = getData(trackingList, sortBy);
        setupAdapter(dataItems);
    }

    public int getSelectedSort() {
        return selectedSort;
    }

    public void setTrackingList(T trackingList) {
        this.trackingList = trackingList;
    }

    public abstract RealmResults<U> getData(T trackingList, @TrackingSort int sortBy);

    @TrackingSort
    public abstract int getSortBySelectedPosition(int position);

    public abstract ModifyListAdapter getListAdapter(AppCompatActivity appCompatActivity, RealmResults<U> dataItems);

    public abstract MoveToAdapter getMoveToListAdapter(AppCompatActivity appCompatActivity, String title, MoveToListCallback callback, RealmResults<T> lists);

    public abstract RealmResults<T> getUserTrackingListsExcludingCurrentList(T currentTrackingList);

    public abstract void handleDoneClicked(List<U> selectedItems);

    public abstract void handleMoveItemsClicked(List<U> selectedItems, T trackingList);

    public abstract void handleRemoveItemsClicked(List<U> selectedItems);

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public T getTrackingList() {
        return trackingList;
    }

    @Bindable
    public String getObjectsSelected() {
        return objectsSelected;
    }

    public void setObjectsSelected(String objectsSelected) {
        this.objectsSelected = objectsSelected;
        notifyPropertyChanged(BR.objectsSelected);
    }

    public void updateDataItems(RealmResults<U> dataItems) {

        listView.setAdapter(getListAdapter(appCompatActivity, dataItems));
    }

    private void setupAdapter(RealmResults<U> dataItems) {
        listView = (ListView) appCompatActivity.findViewById(R.id.projects_sorted_list);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);
        populateList(dataItems, selectedSort);
    }

    public RealmResults<U> filterRealmOrderedCollection(OrderedRealmCollection<U> collection, @TrackingSort int sortBy) {

        switch (sortBy) {
            case SORT_BID_DATE:
                return collection.sort("bidDate", Sort.DESCENDING);
            case SORT_LAST_UPDATE:
                return collection.sort("lastPublishDate", Sort.DESCENDING);
            case SORT_DATE_ADDED:
                return collection.sort("firstPublishDate", Sort.DESCENDING);
            case SORT_VALUE_HIGH:
                return collection.sort("estLow", Sort.DESCENDING);
            case SORT_VALUE_LOW:
                return collection.sort("estLow", Sort.ASCENDING);
            case SORT_COMPANY_NAME:
                return collection.sort("name", Sort.DESCENDING);
            case SORT_COMPANY_UPDATED:
                return collection.sort("updatedAt", Sort.DESCENDING);
            case SORT_NONE:
                return collection.sort("");
            default:
                return collection.sort("bidDate", Sort.DESCENDING);
        }
    }

    private void populateList(RealmResults<U> realmList, @TrackingSort int sortBy) {

        dataItems = filterRealmOrderedCollection(realmList, sortBy);
        listView.setAdapter(getListAdapter(appCompatActivity, dataItems));
    }

    private void toogleMoveMenu(View view) {
        if (moveMenu == null) {
            createMoveMenu(view);
        } else {
            moveToAdapter = getMoveToListAdapter(appCompatActivity, appCompatActivity.getResources().getString(R.string.move_to), this, getUserTrackingListsExcludingCurrentList(trackingList));
            moveMenu.setAdapter(moveToAdapter);
        }
        moveMenu.show();
    }

    private void createMoveMenu(View anchor) {
        if (moveMenu == null) {
            moveMenu = new ListPopupWindow(appCompatActivity);

            moveToAdapter = getMoveToListAdapter(appCompatActivity, appCompatActivity.getResources().getString(R.string.move_to), this, getUserTrackingListsExcludingCurrentList(trackingList));

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
                    if (position > 0) {
                        mtmSortMenu.dismiss();
                        listView.clearChoices();
                        setObjectsSelected(null);
                        selectedSort = getSortBySelectedPosition(position - 1); // Excluding title
                        populateList(dataItems, selectedSort);
                    }
                }
            }); // the callback for when a list item is selected
        }
    }

    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);
        backButton = (ImageView) toolbar.findViewById(R.id.back_button);
        sortButton = (ImageView) toolbar.findViewById(R.id.sort_menu_button);

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
    public void onTrackingListClicked(T trackingList) {

        moveMenu.dismiss();
        handleMoveItemsClicked(getSelectedItems(), trackingList);
    }

    public void onMoveButtonClicked(View view) {
        toogleMoveMenu(view);
    }

    public void onRemoveButtonClicked(View view) {
        handleRemoveItemsClicked(getSelectedItems());
    }

    public void onCancelButtonClicked(View view) {
        listView.clearChoices();
        setObjectsSelected(null);
        finishWithResultCanceled();
    }

    public void onDoneButtonClicked(View view) {
        handleDoneClicked(getSelectedItems());
    }

    public void onBackButtonClick(View view) {
        appCompatActivity.onBackPressed();
    }

    public void onSortButtonClick(View view) {
        toogleMTMSortMenu(view);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        int selectedItemsCount = listView.getCheckedItemCount();
        if (selectedItemsCount == 0) {
            rfooter.setVisibility(View.GONE);
            setObjectsSelected(null);
        } else {
            rfooter.setVisibility(View.VISIBLE);
            setObjectsSelected(appCompatActivity.getString(R.string.x_selected, Integer.toString(selectedItemsCount)));
        }
    }

    public List<U> getSelectedItems() {
        SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
        List<U> items = new ArrayList<>();
        ModifyListAdapter<RealmResults<U>, U> adapter = (ModifyListAdapter) listView.getAdapter();
        for (int i = 0; i < checkedItems.size(); i++) {
            if (checkedItems.valueAt(i)) {
                items.add(adapter.getItem(i));
            }
        }
        return items;
    }

    public void showConfirmationDialog(String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {

        if (dialog == null) {
            dialog = getConfirmationDialog(message, positive, negative);
            dialog.show();
        } else {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            dialog = getConfirmationDialog(message, positive, negative);
            dialog.show();
        }
    }

    private Dialog getConfirmationDialog(String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {

        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
        builder.setTitle(appCompatActivity.getString(R.string.app_name));
        builder.setMessage(message);
        builder.setPositiveButton(appCompatActivity.getString(R.string.ok), positive);
        builder.setNegativeButton(appCompatActivity.getString(R.string.cancel), negative);

        return builder.create();
    }

    // Activity Results
    public void finishWithResultCanceled() {

        getAppCompatActivity().setResult(Activity.RESULT_CANCELED);
        getAppCompatActivity().finish();
    }

    public void finishWithEditedResult() {

        Intent resultIntent = new Intent();
        resultIntent.putExtra(TrackingListViewModel.RESULT_EXTRA_ITEMS_EDITED, true);
        resultIntent.putExtra(TrackingListViewModel.RESULT_EXTRA_SELECTED_SORT, selectedSort);

        getAppCompatActivity().setResult(Activity.RESULT_OK, resultIntent);
        getAppCompatActivity().finish();
    }

    // Dialogs
    public void showProgressDialog(String title, String message) {

        dismissProgressDialog();

        progressDialog = ProgressDialog.show(getAppCompatActivity(), title, message, true, false);
    }

    public void dismissProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    public void dismissAlertDialog() {

        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }

    public void showAlertDialog(String title, String message) {

        dismissAlertDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(getAppCompatActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(getAppCompatActivity().getString(R.string.ok), null);

        alertDialog = builder.show();
    }

    public void showDoneDialog(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dismissAlertDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(getAppCompatActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(getAppCompatActivity().getString(R.string.discard), negative);
        builder.setPositiveButton(getAppCompatActivity().getString(R.string.edit_upper), positive);

        alertDialog = builder.show();
    }
}
