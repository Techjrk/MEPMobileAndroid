package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.MenuTitleListAdapter;
import com.lecet.app.adapters.ModifyListAdapter;
import com.lecet.app.adapters.ModifyProjectListAdapter;
import com.lecet.app.adapters.MoveToAdapter;
import com.lecet.app.adapters.MoveToProjectListAdapter;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.enums.SortBy;
import com.lecet.app.interfaces.MTMMenuCallback;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Josué Rodríguez on 12/11/2016.
 */

public class ModifyProjectTrackingListViewModel extends ModifyTrackingListViewModel<ProjectTrackingList, Project> {

    private static final int TYPE_BID_DATE = 0;
    private static final int TYPE_LAST_UPDATE = 1;
    private static final int TYPE_DATE_ADDED = 2;
    private static final int TYPE_VALUE_HIGH = 3;
    private static final int TYPE_VALUE_LOW = 4;

    private TrackingListDomain trackingListDomain;

    public ModifyProjectTrackingListViewModel(AppCompatActivity appCompatActivity, ProjectTrackingList trackingList, @TrackingSort int sortBy, TrackingListDomain trackingListDomain) {
        super(appCompatActivity, trackingList, sortBy);

        this.trackingListDomain = trackingListDomain;
    }

    @Override
    public RealmResults<Project> getData(ProjectTrackingList trackingList, @TrackingSort int sortBy) {
        return filterRealmOrderedCollection(trackingList.getProjects(), sortBy);
    }

    @Override
    public int getSortBySelectedPosition(int position) {

        switch (position) {
            case TYPE_BID_DATE:
                return SORT_BID_DATE;
            case TYPE_LAST_UPDATE:
                return SORT_LAST_UPDATE;
            case TYPE_DATE_ADDED:
                return SORT_DATE_ADDED;
            case TYPE_VALUE_HIGH:
                return SORT_VALUE_HIGH;
            case TYPE_VALUE_LOW:
                return SORT_VALUE_LOW;
            default:
                return SORT_BID_DATE;
        }
    }

    @Override
    public ModifyListAdapter getListAdapter(AppCompatActivity appCompatActivity, RealmResults<Project> dataItems) {
        return new ModifyProjectListAdapter(appCompatActivity, dataItems);
    }

    @Override
    public MoveToAdapter getMoveToListAdapter(AppCompatActivity appCompatActivity, String title, MTMMenuCallback callback, RealmResults<ProjectTrackingList> lists) {
        return new MoveToProjectListAdapter(appCompatActivity, title, lists, callback);
    }

    @Override
    public RealmResults<ProjectTrackingList> getUserTrackingListsExcludingCurrentList(ProjectTrackingList currentTrackingList) {
        return trackingListDomain.fetchProjectTrackingListsExcludingCurrentList(currentTrackingList.getId());
    }
}
