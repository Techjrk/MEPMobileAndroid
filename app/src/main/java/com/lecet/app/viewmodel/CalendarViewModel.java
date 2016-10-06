package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Josué Rodríguez on 5/10/2016.
 */

public class CalendarViewModel extends BaseObservable {
    private final AppCompatActivity activity;

    public CalendarViewModel(AppCompatActivity activity) {
        this.activity = activity;
    }

}
