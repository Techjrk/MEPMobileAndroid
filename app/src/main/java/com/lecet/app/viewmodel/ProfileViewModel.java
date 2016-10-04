package com.lecet.app.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.domain.UserDomain;

/**
 * File: ProfileViewModel Created: 8/22/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class ProfileViewModel extends BaseObservable {

    private final Context context;
    private final UserDomain userDomain;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address1;
    private String address2;
    private ImageView profileImage;


    public ProfileViewModel(Context context, UserDomain ud) {

        this.context = context;
        this.userDomain = ud;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Bindable
    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    @Bindable
    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    @BindingAdapter({"android:src"})
    public static void setProfileImage(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }


    /** OnClick handlers **/

    @SuppressWarnings("unused")
    public void onEditProfileClicked(View view) {

        //TODO - fill in
    }

}
