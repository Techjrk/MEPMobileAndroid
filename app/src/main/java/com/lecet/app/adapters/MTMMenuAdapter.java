package com.lecet.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.data.models.ProjectTrackingList;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Josué Rodríguez on 3/11/2016.
 */

public class MTMMenuAdapter extends BaseAdapter {


    private Context context;
    private String[] titles;
    private int size;
    private boolean isProjectListVisible;
    private boolean isCompanyListVisible;
    private int projectTitleIndex;
    private int companyTitleIndex;
    private List<CompanyTrackingList> companyTrackingList;
    private List<ProjectTrackingList> projectTrackingList;
    private String companyFormat;
    private String projectFormat;

    public MTMMenuAdapter(Context context, String[] titles, List<ProjectTrackingList> projectTrackingList
            , List<CompanyTrackingList> companyTrackingList) {
        this.isProjectListVisible = false;
        this.isCompanyListVisible = false;
        this.projectTitleIndex = 0;
        this.companyTitleIndex = 1;
        this.context = context;
        this.titles = titles;
        this.size = titles.length;
        this.companyTrackingList = companyTrackingList;
        this.projectTrackingList = projectTrackingList;
        this.projectFormat = context.getString(R.string.mtm_menu_number_projects);
        this.companyFormat = context.getString(R.string.mtm_menu_number_company);
    }

    public void setCompanyTrackingList(List<CompanyTrackingList> companyTrackingList) {
        this.companyTrackingList = companyTrackingList;
        //TODO if visible then update size
    }

    public void setProjectTrackingList(List<ProjectTrackingList> projectTrackingList) {
        this.projectTrackingList = projectTrackingList;
        //TODO if visible then update size
    }

    public void setProjectListVisible(boolean projectListVisible) {
        boolean oldValue = isProjectListVisible;
        isProjectListVisible = projectListVisible;
        if (oldValue != isProjectListVisible) { //means the value changed and we need to update the size
            int displacement = isProjectListVisible ? projectTrackingList.size() : -projectTrackingList.size();
            size += displacement;
            //the company title is moved from its place, so we have to update the index
            companyTitleIndex += displacement;

            notifyDataSetChanged();
        }
    }

    public void setCompanyListVisible(boolean companyListVisible) {
        boolean oldValue = isCompanyListVisible;
        isCompanyListVisible = companyListVisible;

        if (oldValue != isCompanyListVisible) { //means the value changed and we need to update the size
            size += isCompanyListVisible ? companyTrackingList.size() : -companyTrackingList.size();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        if (position == projectTitleIndex) {
            return titles[0];
        } else if (position == companyTitleIndex) {
            return titles[1];
        } else if (position < companyTitleIndex) {
            return projectTrackingList.get(position - 1); // removing the project title index
        } else {
            return companyTrackingList.get(position - companyTitleIndex - 1); // removing the company title index;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MTMMenuAdapter.Holder holder;

        LayoutInflater inflater = LayoutInflater.from(context);
        if (position == projectTitleIndex || position == companyTitleIndex) { //Inflate the custom top item
            convertView = inflater.inflate(R.layout.list_item_mtm_title, parent, false);
        } else {
            convertView = inflater.inflate(R.layout.list_item_mtm_project_or_company, parent, false);
        }
        holder = new MTMMenuAdapter.Holder(convertView);
        convertView.setTag(holder);

        Object item = getItem(position);
        if (item instanceof String) { //title
            final int auxIndex = position;
            holder.text1.setText(item.toString());
            if (auxIndex == projectTitleIndex) {
                if (isProjectListVisible) {
                    Picasso.with(context).load(R.drawable.ic_up_triangle).into(holder.imageView1);
                } else {
                    Picasso.with(context).load(R.drawable.ic_down_triangle).into(holder.imageView1);
                }
            } else {
                if (isCompanyListVisible) {
                    Picasso.with(context).load(R.drawable.ic_up_triangle).into(holder.imageView1);
                } else {
                    Picasso.with(context).load(R.drawable.ic_down_triangle).into(holder.imageView1);
                }
            }

            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (auxIndex == projectTitleIndex) {
                        setProjectListVisible(!isProjectListVisible);
                    } else {
                        setCompanyListVisible(!isCompanyListVisible);
                    }
                }
            });
        } else if (item instanceof ProjectTrackingList) {
            ProjectTrackingList projectTracking = (ProjectTrackingList) item;
            holder.text1.setText(projectTracking.getName());
            holder.text2.setText(String.format(projectFormat, projectTracking.getProjects().size()));
        } else { //company tracking list
            CompanyTrackingList projectTracking = (CompanyTrackingList) item;
            holder.text1.setText(projectTracking.getName());
            holder.text2.setText(String.format(companyFormat, projectTracking.getCompanies().size()));
        }

        return convertView;
    }

    private class Holder {
        private View root;
        private TextView text1;
        private TextView text2;
        private ImageView imageView1;

        Holder(View view) {
            root = view;
            text1 = (TextView) view.findViewById(R.id.text1);
            text2 = (TextView) view.findViewById(R.id.text2);
            imageView1 = (ImageView) view.findViewById(R.id.imageView1);
        }
    }
}
