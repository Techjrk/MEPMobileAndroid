package com.lecet.app.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lecet.app.R;
import com.lecet.app.databinding.ModifyListItemProjectBinding;
import com.lecet.app.interfaces.TrackedObject;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * File: ModifyListAdapter Created: 12/7/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public abstract class ModifyListAdapter<T extends RealmResults<S>, S extends RealmObject & TrackedObject> extends BaseAdapter {

    Context context;
    T data;

    public ModifyListAdapter(Context context, T objects) {
        this.context = context;
        this.data = objects;
    }

    public abstract long getObjectId(S object);

    public abstract String getPrimaryDetail(S object);

    public abstract String getSecondaryDetail(S object);

    public void setData(T data) {
        this.data = data;
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public S getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getObjectId(data.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if (convertView == null) {
            ModifyListItemProjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.modify_list_item_project, parent, false);
            holder = new Holder(binding);
            convertView = binding.getRoot();
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        S object = getItem(position);

        holder.binding.projectName.setText(getPrimaryDetail(object));
        holder.binding.location.setText(getSecondaryDetail(object));

        return convertView;
    }

    public static class Holder {
        ModifyListItemProjectBinding binding;

        public Holder(ModifyListItemProjectBinding binding) {
            this.binding = binding;
        }
    }
}
