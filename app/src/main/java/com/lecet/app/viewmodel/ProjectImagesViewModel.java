package com.lecet.app.viewmodel;

import android.util.Log;

import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.data.models.ProjectPhoto;

/**
 * Created by ludwigvondrake on 3/17/17.
 * This is a temporary class until the ProjectNotesViewModel class is properly functioning.
 */

public class ProjectImagesViewModel {
    private static String TAG = "ProjectImagesViewModel";
    private ProjectPhoto note;
    private String authorName = "Some Person";

    public ProjectImagesViewModel(ProjectPhoto note){
        this.note = note;
        //authorName = someIdConversionLogic;
    }


    public ProjectPhoto getNote() {return note;}

    public String getAuthorName(){
        return authorName;
    }

    public boolean canEdit(){
        /*if(note.getAuthorId() == WhateverYourIdIs){
            return true;
        }//*/
        return false;
    }

    public String getTimeDifference(){

        long difference =  System.currentTimeMillis() - note.getUpdatedAt().getTime();
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
