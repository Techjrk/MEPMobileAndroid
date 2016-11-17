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

/**
 * Created by Josué Rodríguez on 16/11/2016.
 */

public class ModifyProjectListAdapter extends BaseAdapter {

    Context context;
    List<Project> projects;

    public ModifyProjectListAdapter(Context context, List<Project> projects) {
        this.context = context;
        this.projects = projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return projects.size();
    }

    @Override
    public Project getItem(int position) {
        return projects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
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

        Project project = getItem(position);

        holder.binding.projectName.setText(project.getTitle());
        holder.binding.location.setText(String.format("%s, %s", project.getCity(), project.getState()));

        return convertView;
    }

    private class Holder {
        ModifyListItemProjectBinding binding;

        public Holder(ModifyListItemProjectBinding binding) {
            this.binding = binding;
        }
    }
}
