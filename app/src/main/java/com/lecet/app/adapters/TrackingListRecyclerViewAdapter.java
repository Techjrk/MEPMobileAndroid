package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ListItemTrackingBinding;
import com.lecet.app.databinding.ListItemTrackingTestBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ListItemCompanyTrackingViewModel;
import com.lecet.app.viewmodel.NewListItemProjectTrackingViewModel;

import java.lang.annotation.Retention;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * File: TrackingListRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class TrackingListRecyclerViewAdapter extends RecyclerView.Adapter<TrackingListRecyclerViewAdapter.TrackingListViewHolderNew> {

    @Retention(SOURCE)
    @IntDef({LIST_TYPE_COMPANY, LIST_TYPE_PROJECT})
    public @interface TrackingAdapterType {}

    public static final int LIST_TYPE_COMPANY = 0;
    public static final int LIST_TYPE_PROJECT = 1;

    @TrackingAdapterType
    private int listType;

    private List<RealmObject> data;
    private boolean showUpdates;
    private AppCompatActivity appCompatActivity;


    public TrackingListRecyclerViewAdapter(List<RealmObject> data, AppCompatActivity appCompatActivity, @TrackingAdapterType int listType) {

        this.data = data;
        this.listType = listType;
        this.appCompatActivity = appCompatActivity;
    }


    @Override
    public TrackingListViewHolderNew onCreateViewHolder(ViewGroup parent, int viewType) {

//        ListItemTrackingBinding projectBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_tracking, parent, false);
//        TrackingListViewHolder viewHolder = new TrackingListViewHolder(projectBinding);

        ListItemTrackingTestBinding projectBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_tracking_test, parent, false);
        TrackingListViewHolderNew viewHolder = new TrackingListViewHolderNew(projectBinding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrackingListViewHolderNew holder, int position) {

        final String mapsApiKey = "AIzaSyBP3MAIoz2P2layYXrWMRO6o1SgHR8dBWU";    //TODO - externalize?

        // Project List
        if(listType == LIST_TYPE_PROJECT) {

            NewListItemProjectTrackingViewModel viewModel = new NewListItemProjectTrackingViewModel(new ProjectDomain(LecetClient.getInstance(),
                    LecetSharedPreferenceUtil.getInstance(appCompatActivity),
                    Realm.getDefaultInstance()), (Project) data.get(position), mapsApiKey, showUpdates);

            holder.getBinding().setViewModel(viewModel);
        }

        // Company List
        else if(listType == LIST_TYPE_COMPANY) {

            holder.getBinding().setViewModel(new ListItemCompanyTrackingViewModel(new ProjectDomain(LecetClient.getInstance(),
                    LecetSharedPreferenceUtil.getInstance(appCompatActivity),
                    Realm.getDefaultInstance()), (Company) data.get(position), mapsApiKey, showUpdates));
        }
    }


    @Override
    public int getItemCount() {

        if (data == null) return 0;

        return data.size();
    }

    @Override
    public int getItemViewType(int position) {

        //return listType;
        return 1;   //TODO - check
    }

    public void setShowUpdates(boolean showUpdates) {
        this.showUpdates = showUpdates;
    }


    /**
     * View Holders
     **/

    public class TrackingListViewHolder extends RecyclerView.ViewHolder {

        private ListItemTrackingBinding binding;

        public TrackingListViewHolder(ListItemTrackingBinding binding) {

            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemTrackingBinding getBinding() {
            return binding;
        }
    }

    public class TrackingListViewHolderNew extends RecyclerView.ViewHolder {

        private ListItemTrackingTestBinding binding;

        public TrackingListViewHolderNew(ListItemTrackingTestBinding binding) {

            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemTrackingTestBinding getBinding() {
            return binding;
        }
    }
}
