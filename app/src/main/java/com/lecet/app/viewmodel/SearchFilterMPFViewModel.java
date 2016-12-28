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
        switch (view.getId()) {
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

            case R.id.option:
                setMoreOption(true); return;
            case R.id.feweroption:
                setMoreOption(false);return;
            default:
                activity.finish();
                return;
        }
        activity.startActivityForResult(i, 0);

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
