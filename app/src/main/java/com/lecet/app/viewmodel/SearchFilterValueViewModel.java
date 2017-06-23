package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.lecet.app.R;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterValueViewModel extends BaseObservable {
    private AppCompatActivity activity;
    private String min = "", max = "";
    private AlertDialog minMaxDialog = null;
    private final static int MAX_CHAR=8;

    /**
     * Constructor
     */
    public SearchFilterValueViewModel(AppCompatActivity activity) {
        this.activity = activity;
        Intent intent = activity.getIntent();
        setMin(intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_VALUE_MIN));
        setMax(intent.getStringExtra(SearchFilterAllTabbedViewModel.EXTRA_VALUE_MAX));
    }

    public void onApplyButtonClick(View view) {
    if (getMin() != null && getMax()!= null) {
        Log.d("maxchar", "maxchar" + getMin().length());

        if (getMax().length() > MAX_CHAR || getMin().length() > MAX_CHAR) {
            showCharMaxDialog();
            return;
        }
    }
        if ((getMin() == null || getMin().equals("")) && (getMax() == null || getMax().equals(""))) {
            min = "";
            max = "";
            Intent intent = activity.getIntent();
            intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, new String[]{min, max});
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }
        Integer minInt = 0;
        if (getMin() == null || getMin().equals("")) {
            minInt = 0;
            min = "0";
        } else {
            minInt = Integer.valueOf(getMin());
        }

        Integer maxInt = 0;
        if (getMax() == null || getMax().equals("")) {
            maxInt = SearchFilterAllTabbedViewModel.VALUE_MAX;
            max = String.valueOf(SearchFilterAllTabbedViewModel.VALUE_MAX);
        } else {
            maxInt = Integer.valueOf(getMax());
        }
        if (minInt <= maxInt) {
            Intent intent = activity.getIntent();
            intent.putExtra(SearchViewModel.FILTER_EXTRA_DATA, new String[]{min, max});
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        } else {
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
    /**
     * Show the min/max validation dialog
     */
    public void showCharMaxDialog() {
        if (minMaxDialog != null && minMaxDialog.isShowing()) {
            minMaxDialog.dismiss();
        }

        minMaxDialog = new AlertDialog.Builder(this.activity).create();
        minMaxDialog.setTitle(activity.getString(R.string.char_max_exceed_title));
        minMaxDialog.setMessage(activity.getString(R.string.char_max_message));
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
        notifyPropertyChanged(BR.min);
    }

    @Bindable
    public String getMax() {
        return max;
    }

    @Bindable
    public void setMax(String max) {
        this.max = max;
        notifyPropertyChanged(BR.max);
    }
}
