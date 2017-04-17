package com.lecet.app.viewmodel;


import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import com.lecet.app.data.models.ProjectPhoto;

import java.util.TimeZone;

/**
 * Created by ludwigvondrake on 3/23/17.
 */

public class ProjectPhotoViewModel {
    private static String TAG = "ProjectNoteViewModel";
    private ProjectPhoto note;
    private String authorName = "Some Person";

    public ProjectPhotoViewModel(ProjectPhoto note){
        this.note = note;
    }

    public String getAuthorName(){

        return authorName;
    }

    public boolean canEdit(){
        /*if(note.getAuthorId() == WhateverYourIdIs){
            return true;
        }//*/
        return false;
    }

    public String getTitle(){return note.getTitle();}

    public String getText(){return note.getText();}

    public long getId(){return note.getId();}

    public String getTimeDifference(){
        long currentTime = System.currentTimeMillis();

        currentTime -= TimeZone.getTimeZone(Time.getCurrentTimezone()).getOffset(currentTime);

        long difference =  currentTime - note.getUpdatedAt().getTime();

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
