package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.content.ContactDetailActivity;
import com.lecet.app.content.ProfileActivity;
import com.lecet.app.content.ProjectAddNoteActivity;
import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.data.models.User;
import com.lecet.app.domain.UserDomain;
import com.lecet.app.utility.Log;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static com.lecet.app.content.ProjectAddNoteActivity.NOTE_BODY_EXTRA;
import static com.lecet.app.content.ProjectAddNoteActivity.NOTE_ID_EXTRA;
import static com.lecet.app.content.ProjectAddNoteActivity.NOTE_TITLE_EXTRA;

/**
 * Created by ludwigvondrake on 3/23/17.
 */

public class ListItemProjectNoteViewModel extends BaseObservable {

    private static String TAG = "ListItemProjectNoteVM";

    private ProjectNote note;
    private AppCompatActivity activity;
    //private long authorId = -1;
    private String authorName = "Unknown Author";
    private long loggedInUserId = -1;
    private boolean canEdit;

    public ListItemProjectNoteViewModel(ProjectNote note, AppCompatActivity activity, final UserDomain userDomain) {
        this.note = note;
        this.activity = activity;

        setAuthorNameFromUser(note);

        if(userDomain.fetchLoggedInUser() != null) {
            setLoggedInUserId(userDomain.fetchLoggedInUser().getId());
            setCanEdit(note.getAuthorId() == getLoggedInUserId());
        }
    }

    private void setAuthorNameFromUser(ProjectNote note) {
        final User author = note.getAuthor();

        if(author == null){
            Log.e(TAG, "fetchNoteAuthor: User Author is null");
        } else {
            Log.d(TAG, "fetchNoteAuthor: Set Author Name");
            setAuthorName(author.getFirstName() + " " + author.getLastName());
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
        return note.getFullAddress();
    }

    public void setFullAddress(String fullAddress) {
        this.note.setFullAddress(fullAddress);
        notifyPropertyChanged(BR.fullAddress);
    }

    public boolean showFullAddress() {
        return (getFullAddress() != null && !getFullAddress().isEmpty());
    }

    @Bindable
    public String getAuthorName(){
        return authorName;
    }

    private void setAuthorName(String name){
        authorName = name;
        notifyPropertyChanged(BR.authorName);
    }

    private long getLoggedInUserId() {
        return loggedInUserId;
    }

    private void setLoggedInUserId(long id) {
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

    public String getDateUpdatedForDisplay() {
        // not updated since created? no need to display updated at
        if(note.getUpdatedAt().getTime() - note.getCreatedAt().getTime() < 3000) {
            return "";
        }
        return activity.getString(R.string.last_updated) + ": " + getTimeDifference();
    }

    public String getDateCreatedForDisplay() {
        TimeZone localTimeZone = TimeZone.getDefault();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, yyyy h:mm a");
        simpleDateFormat.setTimeZone(localTimeZone);
        String displayDate = simpleDateFormat.format(note.getCreatedAt());
        return displayDate;
    }

    ///////////////////////////
    // Click Events

    public void onEditButtonClick(View view) {
        Log.d(TAG, "onEditButtonClick");

        Intent intent = new Intent(activity, ProjectAddNoteActivity.class);
        intent.putExtra(NOTE_ID_EXTRA, note.getId());
        intent.putExtra(NOTE_TITLE_EXTRA, note.getTitle());
        intent.putExtra(NOTE_BODY_EXTRA, note.getText());
        activity.startActivityForResult(intent, ProjectNotesAndUpdatesViewModel.NOTE_REQUEST_CODE);
    }

    public void onAuthorNameClick(View view) {

        if(note.getAuthorId() == getLoggedInUserId()) {
            Log.d(TAG, "onAuthorNameClick: using logged in user id: " + getLoggedInUserId());
            Intent intent = new Intent(activity, ProfileActivity.class);
            activity.startActivity(intent);
        }

        else if (note.getAuthorId() > -1) {
            Log.d(TAG, "onAuthorNameClick: using author id: " + note.getAuthorId());
            try {
                Intent intent = new Intent(activity, ContactDetailActivity.class);
                intent.putExtra(ContactDetailActivity.CONTACT_ID_EXTRA, note.getAuthorId());
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
    private String getTimeDifference(){
        long currentTime = System.currentTimeMillis();

        currentTime -= TimeZone.getTimeZone(Time.getCurrentTimezone()).getOffset(currentTime);

        long difference =  currentTime - note.getCreatedAt().getTime();

        if(difference < 0){
            Log.e(TAG, "getTimeDifference: Less then 0");
            return "";
        }
        if(difference < 30000L){//less then 30 seconds
            return activity.getString(R.string.just_now);
        }
        difference /= 1000L;//to seconds

        if(difference < 60L){//less then a minute
            return difference + " " + activity.getString(R.string.seconds_ago);
        }
        difference /= 60L;//to minutes

        if(difference < 60L){//less then an hour
            return difference + " " + activity.getString(R.string.minutes_ago);
        }

        difference /= 60L;//to hours

        if(difference < 60L){
            return difference + " " + activity.getString(R.string.hours_ago);
        }
        difference /= 24L;

        if(difference < 24L) {//less then a Day
            return difference + " " + activity.getString(R.string.days_ago);
        }
        difference /= 365L;//to Years
        return difference + " " + activity.getString(R.string.years_ago);
    }

}
