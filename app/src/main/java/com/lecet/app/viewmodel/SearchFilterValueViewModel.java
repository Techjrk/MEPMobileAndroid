package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lecet.app.R;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterValueViewModel extends BaseObservable {
    private AppCompatActivity activity;
    private String min = "", max = "";
    private AlertDialog minMaxDialog = null;

    /**
     * Constructor
     */
    public SearchFilterValueViewModel(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onApplyButtonClick(View view) {

        // min/max validation
        Integer minInt = Integer.valueOf(getMin());
        Integer maxInt = Integer.valueOf(getMax());
        if (minInt <= maxInt) {
            Intent intent = activity.getIntent();
            intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, new String[]{min, max});
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }
        else {
            showMinMaxDialog();
        }
    }

    /**
     * Show the min/max validation dialog
     */
    public void showMinMaxDialog() {
        if (minMaxDialog != null && minMaxDialog.isShowing()) {
            minMaxDialog.dismiss();
        }

        minMaxDialog = new AlertDialog.Builder(this.activity).create();
        minMaxDialog.setTitle(this.activity.getResources().getString(R.string.value_min_max_dialog_title));
        minMaxDialog.setMessage(this.activity.getResources().getString(R.string.value_min_max_dialog_msg));

        minMaxDialog.setButton(AlertDialog.BUTTON_POSITIVE, this.activity.getResources().getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        minMaxDialog.show();
    }

    public void onCancelButtonClick(View view) {
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }


    @Bindable
    public String getMin() {
        return min;
    }

    @Bindable
    public void setMin(String min) {
        this.min = min;
    }

    @Bindable
    public String getMax() {
        return max;
    }

    @Bindable
    public void setMax(String max) {
        this.max = max;
    }
}
