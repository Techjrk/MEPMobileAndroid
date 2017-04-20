package com.lecet.app.viewmodel;


import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.text.format.Time;
import android.util.Log;
import android.widget.ImageView;

import com.lecet.app.data.models.ProjectPhoto;
import com.squareup.picasso.Picasso;

import java.util.TimeZone;

/**
 * Created by ludwigvondrake on 3/23/17.
 */

public class ProjectPhotoViewModel extends BaseObservable {

    private static final String TAG = "ProjectPhotoViewModel";

    private ProjectPhoto photo;
    private String authorName = "Some Photo Taker";

    public ProjectPhotoViewModel(ProjectPhoto photo) {
        this.photo = photo;
    }

    public String getImageUrl() {
        //Log.d(TAG, "getImageUrl: photo url: " + photo.getUrl());
        return photo.getUrl();
    }

    public String getSrc() {
        Log.d(TAG, "getSrc: photo src: " + photo.getSrc());
        return photo.getSrc();
    }

    public String getAuthorName() {
        return authorName;
    }

    public boolean canEdit() {
        /*if(note.getAuthorId() == WhateverYourIdIs){   //TODO - check editing
            return true;
        }//*/
        return false;
    }

    public String getTitle() {
        return photo.getTitle();
    }

    public String getText() {
        return photo.getText();
    }

    public long getId() {
        return photo.getId();
    }

    @BindingAdapter("bind:projectImageUrl")
    public static void loadImage(ImageView view, String url) {
        Log.d(TAG, "loadImage: url: " + url);

        Picasso.with(view.getContext())
                .load(url)
                .placeholder(null)  //TODO - use any placeholder image during load?
                .into(view);
    }


    /*
     * Helpers
     */

    public String getTimeDifference(){  //TODO - move to a utils class as static method
        long currentTime = System.currentTimeMillis();

        currentTime -= TimeZone.getTimeZone(Time.getCurrentTimezone()).getOffset(currentTime);

        long difference =  currentTime - photo.getUpdatedAt().getTime();

        if(difference < 0){
            Log.e(TAG, "getTimeDifference: Less then 0");
        }
        if(difference < 30000L){//less then 30 seconds
            return "Just Now";
        }
        difference /= 1000L;//to seconds

        if(difference < 60L){//less then a minute
            return difference + " Seconds Ago";
        }
        difference /= 60L;//to minutes

        if(difference < 60L){//less then an hour
            return difference + " Minute(s) Ago";
        }

        difference /= 60L;//to hours

        if(difference < 60L){
            return difference + " Hour(s) Ago";
        }
        difference /= 60L;

        if(difference < 24L) {//less then a Day
            return difference + " Days(s) Ago";
        }
        difference /= 365L;//to Years
        return difference + " Year(s) Ago";

    }

}
