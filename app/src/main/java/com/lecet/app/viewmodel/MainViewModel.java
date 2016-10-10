package com.lecet.app.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.content.MainActivity;

/**
 * File: MainViewModel Created: 8/22/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class MainViewModel extends BaseObservable {

    private static final String TAG = "MainViewModel";

    private final Context context;

//    private String string1;
//    private String string2;
//    private boolean bool1;
//    private int int1;
//    private ImageView profileImage;


    public MainViewModel(Context context) {

        this.context = context;
    }

//    @Bindable
//    public String getString1() {
//        return string1;
//    }
//
//    public void setString1(String string1) {
//        this.string1 = string1;
//    }
//
//    @Bindable
//    public String getString2() {
//        return string2;
//    }
//
//    public void setString2(String string2) {
//        this.string2 = string2;
//    }
//
//    @Bindable
//    public boolean getBool1() {
//        return bool1;
//    }
//
//    public void setBool1(boolean bool1) {
//        this.bool1 = bool1;
//    }
//
//    @Bindable
//    public int getInt1() {
//        return int1;
//    }
//
//    public void setInt1(int int1) {
//        this.int1 = int1;
//    }
//
//    @BindingAdapter({"android:src"})
//    public static void setProfileImage(ImageView imageView, int resource) {
//        imageView.setImageResource(resource);
//    }


    /** OnClick handlers **/

    @SuppressWarnings("unused")
    public void onPageLeftClicked(View view) {

        //Log.d(TAG, "onPageLeftClicked");
        MainActivity activity = (MainActivity) this.context;
        activity.prevViewPage();
    }

    @SuppressWarnings("unused")
    public void onPageRightClicked(View view) {

        //Log.d(TAG, "onPageRightClicked");
        MainActivity activity = (MainActivity) this.context;
        activity.nextViewPage();
    }


}
