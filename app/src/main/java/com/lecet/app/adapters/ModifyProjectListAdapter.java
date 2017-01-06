package com.lecet.app.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lecet.app.R;
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.ModifyListItemProjectBinding;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Josué Rodríguez on 16/11/2016.
 */

public class ModifyProjectListAdapter extends ModifyListAdapter<RealmResults<Project>, Project> {


    public ModifyProjectListAdapter(Context context, RealmResults<Project> projects) {
        super(context, projects);
    }

    @Override
    public long getObjectId(Project object) {
        return object.getId();
    }

    @Override
    public String getPrimaryDetail(Project object) {
        return object.getTitle();
    }

    @Override
    public String getSecondaryDetail(Project object) {
        return String.format("%s, %s", object.getCity(), object.getState());
    }
}
