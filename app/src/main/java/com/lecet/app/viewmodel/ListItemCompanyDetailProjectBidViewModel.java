package com.lecet.app.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

/**
 * File: ListItemCompanyDetailProjectBidViewModel Created: 8/28/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class ListItemCompanyDetailProjectBidViewModel extends BaseObservable {

    private final Context context;

    private String projectName;
    private String location;
    private String keywords;
    private String date;
    private ImageView mapImageView;
    // TODO: add new bid field
    // TODO: add new note field

    public ListItemCompanyDetailProjectBidViewModel(Context context) {

        this.context = context;
    }

    @Bindable
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
        //notifyPropertyChanged(BR.projectName);
    }

    @Bindable
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        //notifyPropertyChanged(BR.location);
    }

    @Bindable
    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
        //notifyPropertyChanged(BR.keywords);
    }

    @Bindable
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        //notifyPropertyChanged(BR.date);
    }

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    /** OnClick handler for clicking the entire Pojo1 List Item view **/

    public void onClick(View view) {

        /*domain.handleItemClick(new Callback<Pojo1>() {
            @Override
            public void onResponse(Call<Pojo1> call, Response<Pojo1> response) {

                if (response.isSuccessful()) {


                } else {


                }
            }

            @Override
            public void onFailure(Call<Pojo1> call, Throwable t) {

            }
        });*/
    }

}
