package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import com.lecet.app.R;

/**
 * Created by getdevsinc on 12/29/16.
 */

public class SearchFilterMPFOwnerTypeViewModel extends BaseObservable {
    private AppCompatActivity activity;

    private String[] ownertype = {"", ""};

    /**
     * Constructor
     */
    public SearchFilterMPFOwnerTypeViewModel(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onClicked(View view) {
        Intent intent = activity.getIntent();
        intent.putExtra("data", ownertype);
        activity.setResult(R.id.ownertype & 0xfff, intent);
        activity.finish();
    }

    public void onSelected(View view) {
        ownertype[0] = ((CheckBox) view).getText().toString();
//        time[1] = (String)view.getTag();
    }
}
