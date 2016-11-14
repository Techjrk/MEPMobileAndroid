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

import java.util.List;

/**
 * Created by Josué Rodríguez on 14/11/2016.
 */

public class MoveToAdapter extends BaseAdapter {

    private Context context;
    private String title;
    private List<ProjectTrackingList> projectTrackingList;
    private int size;
    private String projectFormat;
    private MTMMenuCallback callback;

    public MoveToAdapter(Context context, String title, List<ProjectTrackingList> items, MTMMenuCallback callback) {
        this.context = context;
        this.title = title;
        this.projectTrackingList = items;
        this.size = items.size() + 1;// + 1 title header
        this.projectFormat = context.getString(R.string.mtm_menu_number_projects);
        this.callback = callback;
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
            return projectTrackingList.get(position - 1);
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
        if (item instanceof ProjectTrackingList) {
            final ProjectTrackingList projectTracking = (ProjectTrackingList) item;
            holder.text1.setText(projectTracking.getName());
            holder.text2.setText(String.format(projectFormat, projectTracking.getProjects().size()));
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
//                    if (callback != null) {
//                        callback.onProjectTrackingListClicked(projectTracking);
//                    }
                }
            });
        } else {
            holder.text1.setText(item.toString());
        }
    }

    public void setProjectTrackingList(List<ProjectTrackingList> projectTrackingList) {
        this.projectTrackingList = projectTrackingList;
        this.size = this.projectTrackingList.size();
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
