package com.lecet.app.viewmodel;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.text.format.Time;
import android.util.Log;
import android.widget.ImageView;

import com.lecet.app.data.models.ProjectPhoto;
import com.lecet.app.data.models.User;
import com.lecet.app.domain.UserDomain;
import com.squareup.picasso.Picasso;

import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ludwigvondrake on 3/23/17.
 */

public class ListItemProjectImageViewModel extends BaseObservable {

    private static final String TAG = "ListItemProjectImageVM";

    private ProjectPhoto photo;
    private long authorId = -1;
    private String authorName = "Unknown Author";
    private long loggedInUserId = -1;
    private boolean canEdit;

    public ListItemProjectImageViewModel(ProjectPhoto photo, final UserDomain userDomain) {
        this.photo = photo;

        setLoggedInUserId(userDomain.fetchLoggedInUser().getId());
        setCanEdit(photo.getAuthorId() == getLoggedInUserId());
        fetchImageAuthor(photo, userDomain);
    }

    private void fetchImageAuthor(ProjectPhoto photo, final UserDomain userDomain) {
        final User imageAuthor = userDomain.fetchUser(photo.getAuthorId());
        if(imageAuthor == null){
            userDomain.getUser(photo.getAuthorId(), new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()) {
                        userDomain.copyToRealmTransaction(response.body());
                        setAuthorName(response.body().getFirstName() + " " + response.body().getLastName());
                        notifyChange();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
        else {
            setAuthorName(imageAuthor.getFirstName() + " " + imageAuthor.getLastName());
        }
    }



    @Bindable
    public boolean getCanEdit() {
        return this.canEdit;
    }

    private void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }


    public String getImageUrl() {
        //Log.d(TAG, "getImageUrl: photo url: " + photo.getUrl());
        return photo.getUrl();
    }

    public String getSrc() {
        Log.d(TAG, "getSrc: photo src: " + photo.getSrc());
        return photo.getSrc();
    }

    public String getAuthorName(){
        return authorName;
    }

    private void setAuthorName(String name){
        authorName = name;
    }

    public long getLoggedInUserId() {
        return loggedInUserId;
    }

    public void setLoggedInUserId(long id) {
        this.loggedInUserId = id;
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
