package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.content.SearchFilterMPFBHActivity;
import com.lecet.app.content.SearchFilterMPFBiddingWithinActivity;
import com.lecet.app.content.SearchFilterMPFJurisdictionActivity;
import com.lecet.app.content.SearchFilterMPFLocationActivity;
import com.lecet.app.content.SearchFilterMPFOwnerTypeActivity;
import com.lecet.app.content.SearchFilterMPFStageActivity;
import com.lecet.app.content.SearchFilterMPFTypeActivity;
import com.lecet.app.content.SearchFilterMPFUpdatedWithinActivity;
import com.lecet.app.content.SearchFilterMPFValueActivity;
import com.lecet.app.content.SearchFilterMPFWorkTypeActivity;

/**
 * Created by DomandTom 2016.
 */

public class SearchFilterMPFViewModel extends BaseObservable {

    private static final String TAG = "SearchFilterMPFViewModel";

    public static final String EXTRA_LOCATION_CITY = "persistedLocationCity";
    public static final String EXTRA_LOCATION_STATE = "persistedLocationState";
    public static final String EXTRA_LOCATION_COUNTY = "persistedLocationCounty";
    public static final String EXTRA_LOCATION_ZIP = "persistedLocationZip";

    private AppCompatActivity activity;
    private static int id;
    private Intent intent;

    /**
     * User's selected filter item - values for display
     */
    private String locationSelect;
    private String typeSelect;
    private String valueSelect;
    private String updatedWithinSelect;
    private String jurisdictionSelect;
    private String stageSelect;
    private String biddingWithinSelect;
    private String bhSelect;
    private String ownerTypeSelect;
    private String workTypeSelect;

    public String getPersistedLocationCity() {
        return persistedLocationCity;
    }

    public void setPersistedLocationCity(String persistedLocationCity) {
        this.persistedLocationCity = persistedLocationCity;
    }

    /**
     * User's selected filter item - Values for persistence between filter Activities
     */
    private String persistedLocationCity;

    public String getPersistedLocationState() {
        return persistedLocationState;
    }

    public void setPersistedLocationState(String persistedLocationState) {
        this.persistedLocationState = persistedLocationState;
    }

    public String getPersistedLocationCounty() {
        return persistedLocationCounty;
    }

    public void setPersistedLocationCounty(String persistedLocationCounty) {
        this.persistedLocationCounty = persistedLocationCounty;
    }

    public String getPersistedLocationZip() {
        return persistedLocationZip;
    }

    public void setPersistedLocationZip(String persistedLocationZip) {
        this.persistedLocationZip = persistedLocationZip;
    }

    private String persistedLocationState;
    private String persistedLocationCounty;
    private String persistedLocationZip;



    static final String ANY = "Any";

    /*  public static final int LOCATION =0;
      public static final int TYPE =1;
      public static final int VALUE =2;
      public static final int UPDATED_WITHIN =3;
      public static final int JURISDICTION =4;
      public static final int STAGE =5;
      public static final int BIDDING_WITHIN =6;
      public static final int BH =7;
      public static final int OWNER_TYPE =8;
      public static final int WORK_TYPE=9;
     */
    @Bindable
    public String getLocation_select() {
        return locationSelect;
    }

    @Bindable
    public String getType_select() {
        return typeSelect;
    }

    @Bindable
    public String getValue_select() {
        return valueSelect;
    }

    @Bindable
    public String getUpdated_within_select() {
        return updatedWithinSelect;
    }

    @Bindable
    public String getJurisdiction_select() {
        return jurisdictionSelect;
    }

    @Bindable
    public String getStage_select() {
        return stageSelect;
    }

    @Bindable
    public String getBidding_within_select() {
        return biddingWithinSelect;
    }

    @Bindable
    public String getBh_select() {
        return bhSelect;
    }

    @Bindable
    public String getOwner_type_select() {
        return ownerTypeSelect;
    }

    @Bindable
    public String getWork_type_select() {
        return workTypeSelect;
    }

    public void setLocation_select(String locationSelect) {
        this.locationSelect = locationSelect;
        notifyPropertyChanged(BR.location_select);
    }

    public void setType_select(String typeSelect) {
        this.typeSelect = typeSelect;
        notifyPropertyChanged(BR.type_select);
    }

    public void setValue_select(String valueSelect) {
        this.valueSelect = valueSelect;
        notifyPropertyChanged(BR.value_select);

    }

    public void setUpdated_within_select(String updatedWithinSelect) {
        this.updatedWithinSelect = updatedWithinSelect;
        notifyPropertyChanged(BR.updated_within_select);

    }

    public void setJurisdiction_select(String jurisdictionSelect) {
        this.jurisdictionSelect = jurisdictionSelect;
        notifyPropertyChanged(BR.jurisdiction_select);

    }

    public void setStage_select(String stageSelect) {
        this.stageSelect = stageSelect;
        notifyPropertyChanged(BR.stage_select);

    }

    public void setBidding_within_select(String biddingWithinSelect) {
        this.biddingWithinSelect = biddingWithinSelect;
        notifyPropertyChanged(BR.bidding_within_select);

    }

    public void setBh_select(String bhSelect) {
        this.bhSelect = bhSelect;
        notifyPropertyChanged(BR.bh_select);

    }

    public void setOwner_type_select(String ownerTypeSelect) {
        this.ownerTypeSelect = ownerTypeSelect;
        notifyPropertyChanged(BR.owner_type_select);

    }

    public void setWork_type_select(String workTypeSelect) {
        this.workTypeSelect = workTypeSelect;
        notifyPropertyChanged(BR.work_type_select);

    }

    /**
     * Constructor
     */
    public SearchFilterMPFViewModel(AppCompatActivity activity) {
        this.activity = activity;
        setLocation_select("");
        setType_select("");
        setValue_select("");
        setUpdated_within_select(ANY);
        setJurisdiction_select(ANY);
        setStage_select(ANY);
        setBidding_within_select(ANY);
        setBh_select(ANY);
        setOwner_type_select(ANY);
        setWork_type_select(ANY);
        intent = activity.getIntent();
    }

    /**
     * Display error message
     */
    public void errorDisplayMsg(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.error_network_title) + "\r\n" + message + "\r\n");
        Log.e("Error:", "Error " + message);
        builder.setMessage(message);
        builder.setNegativeButton(activity.getString(R.string.ok), null);
        Log.e("onFailure", "onFailure: " + message);
        builder.show();
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    /**
     * OnClick handlers
     **/


    public void onClicked(View view) {
        Intent i = null;
        id = view.getId();
        switch (id) {
            case R.id.location:
                i = new Intent(activity, SearchFilterMPFLocationActivity.class);
                i.putExtra(SearchFilterMPFViewModel.EXTRA_LOCATION_CITY, getPersistedLocationCity());
                i.putExtra(SearchFilterMPFViewModel.EXTRA_LOCATION_STATE, getPersistedLocationState());
                i.putExtra(SearchFilterMPFViewModel.EXTRA_LOCATION_COUNTY, getPersistedLocationCounty());
                i.putExtra(SearchFilterMPFViewModel.EXTRA_LOCATION_ZIP, getPersistedLocationZip());
                break;

            case R.id.type:
                i = new Intent(activity, SearchFilterMPFTypeActivity.class);
                break;
            case R.id.value:
                i = new Intent(activity, SearchFilterMPFValueActivity.class);
                break;
            case R.id.updated_within:
                i = new Intent(activity, SearchFilterMPFUpdatedWithinActivity.class);
                break;
            case R.id.jurisdiction:
                i = new Intent(activity, SearchFilterMPFJurisdictionActivity.class);
                break;
            case R.id.stage:
                i = new Intent(activity, SearchFilterMPFStageActivity.class);
                break;
            case R.id.bidding_within:
                i = new Intent(activity, SearchFilterMPFBiddingWithinActivity.class);
                break;
            case R.id.bh:
                i = new Intent(activity, SearchFilterMPFBHActivity.class);
                break;
            case R.id.ownertype:
                i = new Intent(activity, SearchFilterMPFOwnerTypeActivity.class);
                break;
            case R.id.worktype:
                i = new Intent(activity, SearchFilterMPFWorkTypeActivity.class);
                break;

            case R.id.option:
                setMoreOption(true);
                return;
            case R.id.feweroption:
                setMoreOption(false);
                return;
            default:
                //TODO: return all filter data in Intent back to MSE
                saveResult();
                activity.finish();  // includes Cancel and Apply buttons
                return;
        }
        activity.startActivityForResult(i, id & 0xfff); //mark
    }

    /**
     * Put the passed filter name and String value into an Extra to be returned with the Intent
     */
    public void setSearchFilterResult(String name, String content) {
        intent.putExtra(name, content);
    }

    public void saveResult() {
        activity.setResult(1, intent);
    }

    private boolean moreOption; //TODO - move to top of class

    @Bindable
    public boolean getMoreOption() {
        return moreOption;
    }

    @Bindable
    public void setMoreOption(boolean moreOption) {
        this.moreOption = moreOption;
        notifyPropertyChanged(BR.moreOption);
    }
}
