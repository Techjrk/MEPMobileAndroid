package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.BR;
import com.lecet.app.content.ContactDetailActivity;
import com.lecet.app.content.ProfileActivity;
import com.lecet.app.content.ProjectAddImageActivity;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.content.ProjectViewFullscreenImageActivity;
import com.lecet.app.content.ProjectViewImageActivity;
import com.lecet.app.data.models.ProjectPhoto;
import com.lecet.app.data.models.User;
import com.lecet.app.domain.UserDomain;
import com.lecet.app.utility.Log;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static com.lecet.app.content.ProjectAddImageActivity.IMAGE_BODY_EXTRA;
import static com.lecet.app.content.ProjectAddImageActivity.IMAGE_ID_EXTRA;
import static com.lecet.app.content.ProjectAddImageActivity.IMAGE_TITLE_EXTRA;
import static com.lecet.app.content.ProjectAddImageActivity.IMAGE_URL_EXTRA;
import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;
import static com.lecet.app.content.ProjectImageChooserActivity.PROJECT_REPLACE_IMAGE_EXTRA;

/**
 * Created by ludwigvondrake on 3/23/17.
 */

public class ListItemProjectImageViewModel extends BaseObservable {

    private static final String TAG = "ListItemProjectImageVM";

    private ProjectPhoto photo;
    private AppCompatActivity activity;
    private String authorName = "Unknown Author";
    private long loggedInUserId = -1;
    private boolean canEdit;
    private ImageView imageView;

    public ListItemProjectImageViewModel(ProjectPhoto photo, AppCompatActivity activity, final UserDomain userDomain, ImageView imageView) {
        this.photo = photo;
        this.activity = activity;
        this.imageView = imageView;

        setAuthorNameFromUser(photo);
        if(userDomain.fetchLoggedInUser() != null) {
            setLoggedInUserId(userDomain.fetchLoggedInUser().getId());
            setCanEdit(photo.getAuthorId() == getLoggedInUserId());
        }
    }

    private void setAuthorNameFromUser(ProjectPhoto photo) {
        final User author = photo.getAuthor();

        if(author == null){
            Log.e(TAG, "fetchImageAuthor: No Author Attached");
        }
        else {
            setAuthorName(author.getFirstName() + " " + author.getLastName());
            notifyChange();
        }
    }


    @Bindable
    public boolean getCanEdit() {
        return this.canEdit;
    }

    private void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
        notifyPropertyChanged(BR.canEdit);
    }

    @Bindable
    public String getFullAddress() {
        return photo.getFullAddress();
    }

    public void setFullAddress(String fullAddress) {
        this.photo.setFullAddress(fullAddress);
        notifyPropertyChanged(BR.fullAddress);
    }

    public String getImageUrl() {
        //Log.d(TAG, "getImageUrl: photo url: " + photo.getUrl());
        return photo.getUrl();
    }

    public String getSrc() {
        Log.d(TAG, "getSrc: photo src: " + photo.getSrc());
        return photo.getSrc();
    }

    @Bindable
    public String getAuthorName(){
        return authorName;
    }

    private void setAuthorName(String name){
        authorName = name;
        notifyPropertyChanged(BR.authorName);
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

    public String getDateUpdatedForDisplay() {
        TimeZone localTimeZone = TimeZone.getDefault();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d yyyy, hh:mm aaa");
        simpleDateFormat.setTimeZone(localTimeZone);
        String displayDate = simpleDateFormat.format(photo.getUpdatedAt());
        return displayDate;
    }


    ///////////////////////////
    // Click Events

    public void onImageClick(View view) {
        Log.d(TAG, "onImageClick");
        Intent intent;
        if(imageView.getDrawable().getIntrinsicWidth() > imageView.getDrawable().getIntrinsicHeight()){
            intent = new Intent(activity, ProjectViewImageActivity.class);
        }else{
            intent = new Intent(activity, ProjectViewFullscreenImageActivity.class);
        }
        intent.putExtra(PROJECT_ID_EXTRA, photo.getProjectId());
        intent.putExtra(IMAGE_ID_EXTRA, photo.getId());
        intent.putExtra(IMAGE_TITLE_EXTRA, photo.getTitle());
        intent.putExtra(IMAGE_BODY_EXTRA, photo.getText());
        intent.putExtra(IMAGE_URL_EXTRA, photo.getUrl());
        activity.startActivityForResult(intent, ProjectDetailActivity.REQUEST_CODE_HOME);
    }

    public void onEditButtonClick(View view) {
        Log.d(TAG, "onEditButtonClick");

        Intent intent = new Intent(activity, ProjectAddImageActivity.class);
        intent.putExtra(PROJECT_ID_EXTRA, photo.getProjectId());
        intent.putExtra(IMAGE_ID_EXTRA, photo.getId());
        intent.putExtra(IMAGE_TITLE_EXTRA, photo.getTitle());
        intent.putExtra(IMAGE_BODY_EXTRA, photo.getText());
        intent.putExtra(IMAGE_URL_EXTRA, photo.getUrl());
        intent.putExtra(PROJECT_REPLACE_IMAGE_EXTRA, true);
        activity.startActivityForResult(intent, ProjectNotesAndUpdatesViewModel.NOTE_REQUEST_CODE);
    }

    public void onAuthorNameClick(View view) {
        Log.d(TAG, "onAuthorNameClick");
        //Log.d(TAG, "onAuthorNameClick: logged in user id: " + getLoggedInUserId());
        //Log.d(TAG, "onAuthorNameClick: image author id: " + photo.getAuthorId());

        if(photo.getAuthorId() == getLoggedInUserId()) {
            Log.d(TAG, "onAuthorNameClick: using logged in user id: " + getLoggedInUserId());
            Intent intent = new Intent(activity, ProfileActivity.class);
            activity.startActivity(intent);
        }

        else if (photo.getAuthorId() > -1) {
            Log.d(TAG, "onAuthorNameClick: using author id: " + photo.getAuthorId());
            try {
                Intent intent = new Intent(activity, ContactDetailActivity.class);
                intent.putExtra(ContactDetailActivity.CONTACT_ID_EXTRA, photo.getAuthorId());
                activity.startActivity(intent);
            }
            catch (IndexOutOfBoundsException e) {
                Log.e(TAG, "onAuthorNameClick: " + e.getMessage() );
            }
        }
    }


    /*
     * Helpers
     */

    //TODO - move to utils class?
    public String getTimeDifference(){
        long currentTime = System.currentTimeMillis();

        currentTime -= TimeZone.getTimeZone(Time.getCurrentTimezone()).getOffset(currentTime);

        long difference =  currentTime - photo.getCreatedAt().getTime();

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
        difference /= 24L;

        if(difference < 24L) {//less then a Day
            return difference + " Days(s) Ago";
        }
        difference /= 365L;//to Years
        return difference + " Year(s) Ago";

    }

}
