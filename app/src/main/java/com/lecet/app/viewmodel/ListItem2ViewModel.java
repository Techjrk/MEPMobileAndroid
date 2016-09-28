package com.lecet.app.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

/**
 * File: ListItem2ViewModel Created: 8/28/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class ListItem2ViewModel extends BaseObservable {

    private final Context context;

    private String string1;
    private String string2;
    private String string3;
    private ImageView imageView;

    public ListItem2ViewModel(Context context) {

        this.context = context;
    }

    @Bindable
    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
        //notifyPropertyChanged(BR.string1);
    }

    @Bindable
    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
        //notifyPropertyChanged(BR.string2);
    }

    @Bindable
    public String getString3() {
        return string3;
    }

    public void setString3(String string3) {
        this.string3 = string3;
        //notifyPropertyChanged(BR.string3);
    }

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    /** OnClick handler for clicking the entire Pojo1 List Item view **/

    public void onClick(View view) {

        /*domain.handleItemClick(new Callback<Pojo1>() {
            @Override
            public void onResponse(Call<Pojo1> call, Response<Pojo1> response) {

                if (response.isSuccessful()) {


                } else {


                }
            }

            @Override
            public void onFailure(Call<Pojo1> call, Throwable t) {

            }
        });*/
    }

}
