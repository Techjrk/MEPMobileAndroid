package com.lecet.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.interfaces.MTMMenuCallback;
import com.lecet.app.interfaces.MoveToListCallback;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Josué Rodríguez on 14/11/2016.
 */

public abstract class MoveToAdapter<T extends RealmObject> extends BaseAdapter {

    private Context context;
    private String title;
    private RealmResults<T> trackingLists;
    private int size;
    private MoveToListCallback callback;

    public MoveToAdapter(Context context, String title, RealmResults<T> trackingLists, MoveToListCallback callback) {
        this.context = context;
        this.title = title;
        this.trackingLists = trackingLists;
        this.size = trackingLists.size() + 1;// + 1 title header
        this.callback = callback;
    }

    public abstract String itemPrimaryDetail(T object);
    public abstract String itemSecondaryDetail(T object);

    public Context getContext() {
        return context;
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return title;
        } else {
            return trackingLists.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        if (position == 0) { //Inflate the custom top item
            convertView = inflater.inflate(R.layout.list_item_mtm_title, parent, false);
        } else {
            convertView = inflater.inflate(R.layout.list_item_mtm_project_or_company, parent, false);
        }

        Holder holder = new Holder(convertView);

        if (position == 0) {
            holder.imageView1.setVisibility(View.GONE);
        }

        populateView(holder, getItem(position));
        return convertView;
    }


    protected void populateView(Holder holder, Object item) {
        if (item instanceof String) {

            holder.text1.setText(item.toString());

        } else {

            final T trackingList = (T)item;
            holder.text1.setText(itemPrimaryDetail(trackingList));
            holder.text2.setText(itemSecondaryDetail(trackingList));
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onTrackingListClicked(trackingList);
                    }
                }
            });
        }
    }

    public void setTrackingLists(RealmResults<T> trackingLists) {
        this.trackingLists = trackingLists;
        this.size = this.trackingLists.size();
        notifyDataSetInvalidated();
    }

    private class Holder {
        TextView text1;
        TextView text2;
        ImageView imageView1;
        View root;

        Holder(View view) {
            root = view;
            text1 = (TextView) view.findViewById(R.id.text1);
            text2 = (TextView) view.findViewById(R.id.text2);
            imageView1 = (ImageView) view.findViewById(R.id.imageView1);
        }
    }
}
