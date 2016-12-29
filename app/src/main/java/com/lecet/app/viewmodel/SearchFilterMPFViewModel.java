package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.BoolRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.lecet.app.content.SearchFilterMPSActivity;

/**
 * Created by DomandTom 2016.
 */

public class SearchFilterMPFViewModel extends BaseObservable {

    private static final String TAG = "SearchFilterMPFViewModel";
    private AppCompatActivity activity;
    private static int id;

    private String location_select = "";
    private String type_select = "";
    private String value_select = "";
    private String updated_within_select = "Any";
    private String jurisdiction_select = "Any";
    private String stage_select = "Any";
    private String bidding_within_select = "Any";
    private String bh_select = "Any";
    private String owner_type_select = "Any";
    private String work_type_select = "Any";

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
        return location_select;
    }

    @Bindable
    public String getType_select() {
        return type_select;
    }

    @Bindable
    public String getValue_select() {
        return value_select;
    }

    @Bindable
    public String getUpdated_within_select() {
        return updated_within_select;
    }

    @Bindable
    public String getJurisdiction_select() {
        return jurisdiction_select;
    }

    @Bindable
    public String getStage_select() {
        return stage_select;
    }

    @Bindable
    public String getBidding_within_select() {
        return bidding_within_select;
    }

    @Bindable
    public String getBh_select() {
        return bh_select;
    }

    @Bindable
    public String getOwner_type_select() {
        return owner_type_select;
    }

    @Bindable
    public String getWork_type_select() {
        return work_type_select;
    }

    public void setLocation_select(String location_select) {
        this.location_select = location_select;
        notifyPropertyChanged(BR.location_select);
    }

    public void setType_select(String type_select) {
        this.type_select = type_select;
        notifyPropertyChanged(BR.type_select);
    }

    public void setValue_select(String value_select) {
        this.value_select = value_select;
        notifyPropertyChanged(BR.value_select);

    }

    public void setUpdated_within_select(String updated_within_select) {
        this.updated_within_select = updated_within_select;
        notifyPropertyChanged(BR.updated_within_select);

    }

    public void setJurisdiction_select(String jurisdiction_select) {
        this.jurisdiction_select = jurisdiction_select;
        notifyPropertyChanged(BR.jurisdiction_select);

    }

    public void setStage_select(String stage_select) {
        this.stage_select = stage_select;
        notifyPropertyChanged(BR.stage_select);

    }

    public void setBidding_within_select(String bidding_within_select) {
        this.bidding_within_select = bidding_within_select;
        notifyPropertyChanged(BR.bidding_within_select);

    }

    public void setBh_select(String bh_select) {
        this.bh_select = bh_select;
        notifyPropertyChanged(BR.bh_select);

    }

    public void setOwner_type_select(String owner_type_select) {
        this.owner_type_select = owner_type_select;
        notifyPropertyChanged(BR.owner_type_select);

    }

    public void setWork_type_select(String work_type_select) {
        this.work_type_select = work_type_select;
        notifyPropertyChanged(BR.work_type_select);

    }

    /**
     * Constructor
     */
    public SearchFilterMPFViewModel(AppCompatActivity activity) {
        this.activity = activity;
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
                activity.finish();
                return;
        }
        activity.startActivityForResult(i, id & 0xfff);
    }

    private boolean moreOption;

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
