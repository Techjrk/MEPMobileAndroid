package com.lecet.app.adapters;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lecet.app.R;

import com.lecet.app.content.MainActivity;
import com.lecet.app.content.ReturnToHomeActivity;
import com.lecet.app.databinding.ListItemTrackingBinding;
import com.lecet.app.viewmodel.TrackingListItem;

import io.realm.RealmResults;

import static com.lecet.app.R.string.google_api_key;

/**
 * File: TrackingListRecyclerViewAdapter Created: 10/21/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public abstract class TrackingListAdapter<T extends RealmResults> extends RecyclerView.Adapter<TrackingListAdapter<T>.TrackingListViewHolderNew> {

    private T data;
    private boolean showUpdates;
    private AppCompatActivity appCompatActivity;


    public TrackingListAdapter(T data, AppCompatActivity appCompatActivity) {

        this.data = data;
        this.appCompatActivity = appCompatActivity;
    }

    public abstract TrackingListItem viewModelForPosition(String apiKey, int position, boolean showUpdates);

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public T getData() {
        return data;
    }


    @Override
    public TrackingListViewHolderNew onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemTrackingBinding projectBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_tracking, parent, false);
        TrackingListViewHolderNew viewHolder = new TrackingListViewHolderNew(projectBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TrackingListViewHolderNew holder, int position) {

        final String mapsApiKey = appCompatActivity.getBaseContext().getResources().getString(google_api_key);

        holder.getBinding().setViewModel(viewModelForPosition(mapsApiKey, position, showUpdates));
        holder.linearTrackHolder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("longpressadapter","longpressadapter");
                Intent intent = new Intent(getAppCompatActivity(), ReturnToHomeActivity.class);
                getAppCompatActivity().startActivityForResult(intent, MainActivity.RETURN_TO_HOME);
                return false;
            }
        });

    }


    @Override
    public int getItemCount() {

        if (data == null) return 0;

        return data.size();
    }


    public void setShowUpdates(boolean showUpdates) {
        this.showUpdates = showUpdates;
    }


    /**
     * View Holders
     **/

    public class TrackingListViewHolderNew extends RecyclerView.ViewHolder {
        LinearLayout linearTrackHolder;
        private ListItemTrackingBinding binding;

        public TrackingListViewHolderNew(ListItemTrackingBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            linearTrackHolder = (LinearLayout) itemView.findViewById(R.id.track_list_item);
          //  linearTrackHolder = (LinearLayout) itemView.findViewById(R.id.content_track_list);
        }

        public ListItemTrackingBinding getBinding() {
            return binding;
        }
    }
}
