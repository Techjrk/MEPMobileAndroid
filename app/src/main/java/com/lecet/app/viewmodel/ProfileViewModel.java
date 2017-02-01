package com.lecet.app.viewmodel;

import android.app.ProgressDialog;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.UiThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.databinding.library.baseAdapters.BR;
import com.lecet.app.R;
import com.lecet.app.data.models.User;
import com.lecet.app.domain.UserDomain;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: ProfileViewModel Created: 8/22/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class ProfileViewModel extends BaseObservable {

    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private TextView saveButton;


    private final AppCompatActivity context;
    private final UserDomain userDomain;
    private User user;

    private String firstName;
    private String lastName;
    private String email;
    private String title;
    private String organization;
    private String phone;
    private String fax;
    private String address;
    private String city;
    private String state;
    private String zip;

    private ProgressDialog progressDialog;

    public ProfileViewModel(AppCompatActivity context, UserDomain ud) {

        this.context = context;
        this.userDomain = ud;
        this.user = userDomain.fetchLoggedInUser();

        // Fetch current user info from backend.
        getUserProfile(user.getId());
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
        notifyPropertyChanged(BR.organization);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
        notifyPropertyChanged(BR.fax);
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        notifyPropertyChanged(BR.state);
    }

    @Bindable
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
        notifyPropertyChanged(BR.zip);
    }

    /** Toolbar **/

    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);
        backButton = (ImageView) toolbar.findViewById(R.id.back_button);
        saveButton = (TextView) toolbar.findViewById(R.id.save_text_view);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonClick(v);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveClicked(view);
            }
        });

        titleTextView.setText(title);
        subtitleTextView.setText(subtitle);
    }


    /**
     * OnClick handlers
     **/

    @SuppressWarnings("unused")
    public void onSaveClicked(View view) {

        updateUser(user);
    }

    public void onBackButtonClick(View view) {

        context.onBackPressed();
    }

    /**
     * Network Calls
     **/
    private void getUserProfile(final long userID) {

        showProgressDialog(context.getString(R.string.app_name), context.getString(R.string.updating));

        userDomain.getUser(userID, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    User r = response.body();
                    user = userDomain.copyToRealmTransaction(r);
                    updateUI(user);

                    dismissProgressDialog();

                } else {

                    dismissProgressDialog();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getString(R.string.error_network_title));
                    builder.setMessage(response.message());
                    builder.setNegativeButton(context.getString(R.string.ok), null);

                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                dismissProgressDialog();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.error_network_title));
                builder.setMessage(context.getString(R.string.error_network_message));
                builder.setNegativeButton(context.getString(R.string.ok), null);

                builder.show();
            }
        });
    }

    private void updateUser(User update) {

        showProgressDialog(context.getString(R.string.app_name), context.getString(R.string.updating));

        userDomain.updateUser(update, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    User r = response.body();
                    user = userDomain.copyToRealmTransaction(r);
                    updateUI(user);

                    dismissProgressDialog();

                } else {

                    dismissProgressDialog();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getString(R.string.error_network_title));
                    builder.setMessage(context.getString(R.string.error_login_message));
                    builder.setNegativeButton(context.getString(R.string.ok), null);

                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                dismissProgressDialog();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.error_network_title));
                builder.setMessage(context.getString(R.string.error_network_message));
                builder.setNegativeButton(context.getString(R.string.ok), null);

                builder.show();
            }
        });
    }

    /**
     * Update UI
     **/

    @UiThread
    private void updateUI(User user) {

        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setEmail(user.getEmail());
        setTitle(user.getTitle());
        setOrganization(user.getOrganization());
        setPhone(user.getPhoneNumber());
        setFax(user.getFax());
        setAddress(user.getAddress());
        setCity(user.getCity());
        setState(user.getState());
        setZip(user.getZip());
    }

    public void showProgressDialog(String title, String message) {

        dismissProgressDialog();

        progressDialog = ProgressDialog.show(context, title, message, true, false);
    }

    public void dismissProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }
}
