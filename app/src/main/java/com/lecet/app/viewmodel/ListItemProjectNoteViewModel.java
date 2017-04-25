package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.format.Time;
import android.util.Log;

import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.data.models.User;
import com.lecet.app.domain.UserDomain;

import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ludwigvondrake on 3/23/17.
 */

public class ListItemProjectNoteViewModel extends BaseObservable {

    private static String TAG = "ListItemProjectNoteVM";

    private ProjectNote note;
    private long authorId = -1;
    private String authorName = "Unknown Author";
    private long loggedInUserId = -1;
    private boolean canEdit;

    public ListItemProjectNoteViewModel(ProjectNote note, final UserDomain userDomain) {
        this.note = note;

        setLoggedInUserId(userDomain.fetchLoggedInUser().getId());
        setCanEdit(note.getAuthorId() == getLoggedInUserId());
        fetchNoteAuthor(note, userDomain);
    }

    private void fetchNoteAuthor(final ProjectNote note, final UserDomain userDomain) {
        final User noteAuthor = userDomain.fetchUser(note.getAuthorId());
        if(noteAuthor == null){
            userDomain.getUser(note.getAuthorId(), new Callback<User>() {
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
            setAuthorName(noteAuthor.getFirstName() + " " + noteAuthor.getLastName());
        }
    }


    @Bindable
    public boolean getCanEdit() {
        return this.canEdit;
    }

    private void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }


    /*public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }*/

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
        return note.getTitle();
    }

    public String getText() {
        return note.getText();
    }

    public long getId() {
        return note.getId();
    }

    //TODO - move to utils class?
    public String getTimeDifference() {
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
