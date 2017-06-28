package com.lecet.app.viewmodel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.Bindable;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.lecet.app.R;
import com.lecet.app.content.LauncherActivity;
import com.lecet.app.data.api.request.UpdateUserProfileRequest;
import com.lecet.app.data.models.User;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.UserDomain;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: ProfileViewModel Created: 8/22/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class ProfileViewModel extends BaseActivityViewModel {

    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private TextView saveButton;


    private final AppCompatActivity appCompatActivity;
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

    public ProfileViewModel(AppCompatActivity appCompatActivity, UserDomain ud) {
        super(appCompatActivity);

        this.appCompatActivity = appCompatActivity;
        this.userDomain = ud;
        this.user = userDomain.fetchLoggedInUser();

        // Fetch current user info from backend.
        try {
            getUserProfile(user.getId());
        }

        // if user not found, return to login
        catch (NullPointerException e) {
            Toast.makeText(appCompatActivity, R.string.error_user_not_found, Toast.LENGTH_LONG).show();

            //TODO - the logout code has been borrowed from BaseActivityViewModel. These could be moved to inherited methods.
            logoutUser();
        }
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

    private boolean isFormsValid() {

        if (TextUtils.isEmpty(firstName)) {

            return false;
        }
        if (TextUtils.isEmpty(lastName)) {

            return false;
        }
        if (TextUtils.isEmpty(email)) {

            return false;
        }
        if (TextUtils.isEmpty(title)) {

            return false;
        }
        if (TextUtils.isEmpty(title)) {

            return false;
        }
        if (TextUtils.isEmpty(phone) || !phone.matches("[0-9]+")) {

            return false;
        }
        if (!TextUtils.isEmpty(fax) && !fax.matches("[0-9]+")) {

            return false;
        }
        if (TextUtils.isEmpty(address)) {

            return false;
        }
        if (TextUtils.isEmpty(city)) {

            return false;
        }
        if (TextUtils.isEmpty(state)) {

            return false;
        }
        if (TextUtils.isEmpty(zip) || !zip.matches("[0-9]+")) {

            return false;
        }

        return true;
    }

    private void validateFormFields() {

        if (TextUtils.isEmpty(firstName)) {

            String message = String.format(appCompatActivity.getString(R.string.profile_error_message), appCompatActivity.getString(R.string.first_name));
            showCancelAlertDialog( appCompatActivity.getString(R.string.app_name), message);

            return;
        }
        if (TextUtils.isEmpty(lastName)) {

            String message = String.format(appCompatActivity.getString(R.string.profile_error_message), appCompatActivity.getString(R.string.last_name));
            showCancelAlertDialog(appCompatActivity.getString(R.string.app_name), message);
            return;
        }
        if (TextUtils.isEmpty(email)) {

            String message = String.format(appCompatActivity.getString(R.string.profile_error_message), appCompatActivity.getString(R.string.email));
            showCancelAlertDialog(appCompatActivity.getString(R.string.app_name), message);
            return;
        }
        if (TextUtils.isEmpty(title)) {

            String message = String.format(appCompatActivity.getString(R.string.profile_error_message), appCompatActivity.getString(R.string.title));
            showCancelAlertDialog(appCompatActivity.getString(R.string.app_name), message);

            return;
        }
        if (TextUtils.isEmpty(phone) || !phone.matches("[0-9]+")) {

            String message = String.format(appCompatActivity.getString(R.string.profile_error_message), appCompatActivity.getString(R.string.phone));
            showCancelAlertDialog(appCompatActivity.getString(R.string.app_name), message);

            return;
        }
        if (!TextUtils.isEmpty(fax) && !fax.matches("[0-9]+")) {

            String message = String.format(appCompatActivity.getString(R.string.profile_error_message), appCompatActivity.getString(R.string.fax));
            showCancelAlertDialog( appCompatActivity.getString(R.string.app_name), message);

            return;
        }
        if (TextUtils.isEmpty(address)) {

            String message = String.format(appCompatActivity.getString(R.string.profile_error_message), appCompatActivity.getString(R.string.address));
            showCancelAlertDialog( appCompatActivity.getString(R.string.app_name), message);

            return;
        }
        if (TextUtils.isEmpty(city)) {

            String message = String.format(appCompatActivity.getString(R.string.profile_error_message), appCompatActivity.getString(R.string.city));
            showCancelAlertDialog(appCompatActivity.getString(R.string.app_name), message);

            return;
        }
        if (TextUtils.isEmpty(state)) {

            String message = String.format(appCompatActivity.getString(R.string.profile_error_message), appCompatActivity.getString(R.string.state));
            showCancelAlertDialog( appCompatActivity.getString(R.string.app_name), message);

            return;
        }
        if (TextUtils.isEmpty(zip) || !zip.matches("[0-9]+")) {

            String message = String.format(appCompatActivity.getString(R.string.profile_error_message), appCompatActivity.getString(R.string.zip));
            showCancelAlertDialog(appCompatActivity.getString(R.string.app_name), message);
            return;
        }
    }

    /**
     * Toolbar
     **/

    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);
        backButton = (ImageView) toolbar.findViewById(R.id.back_button);
        saveButton = (TextView) toolbar.findViewById(R.id.save_text_view);

        backButton.setVisibility(View.INVISIBLE);

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

        if (isFormsValid()) {

            updateUser();

        } else {

            validateFormFields();
        }
    }

    public void onBackButtonClick(View view) {

        appCompatActivity.onBackPressed();
    }

    /**
     * Network Calls
     **/
    private void getUserProfile(final long userID) {


        showProgressDialog(appCompatActivity.getString(R.string.app_name), appCompatActivity.getString(R.string.updating));


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
                    showCancelAlertDialog( appCompatActivity.getString(R.string.error_network_title), response.message());

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                dismissProgressDialog();

                showCancelAlertDialog(appCompatActivity.getString(R.string.error_network_title), appCompatActivity.getString(R.string.error_network_message));

            }
        });
    }

    private void updateUser() {

        showProgressDialog(appCompatActivity.getString(R.string.app_name), appCompatActivity.getString(R.string.updating));

        UpdateUserProfileRequest.Builder builder = new UpdateUserProfileRequest.Builder(user.getId())
                .firstName(getFirstName())
                .lastName(getLastName())
                .email(getEmail())
                .title(getTitle())
                .organization(getOrganization())
                .phoneNumber(getPhone())
                .fax(getFax())
                .address(getAddress())
                .city(getCity())
                .state(getState())
                .zip(getZip());

        userDomain.updateUser(builder.build(), new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    User r = response.body();
                    user = userDomain.copyToRealmTransaction(r);
                    updateUI(user);

                    dismissProgressDialog();

                    showCancelAlertDialog(appCompatActivity.getString(R.string.app_name), appCompatActivity.getString(R.string.successfully_updated));


                } else {

                    dismissProgressDialog();

                    showCancelAlertDialog(appCompatActivity.getString(R.string.error_network_title), response.message());

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                dismissProgressDialog();

                showCancelAlertDialog(appCompatActivity.getString(R.string.error_network_title), appCompatActivity.getString(R.string.error_network_message));

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

    private void logoutUser() {

        showProgressDialog(appCompatActivity.getString(R.string.app_name), appCompatActivity.getString(R.string.logging_out));

        userDomain.logout(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    clearSessionData();

                } else {

                    dismissProgressDialog();

                    showCancelAlertDialog( appCompatActivity.getString(R.string.error_network_title), response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                dismissProgressDialog();

                showCancelAlertDialog(appCompatActivity.getString(R.string.error_network_title), appCompatActivity.getString(R.string.error_network_message));
            }
        });
    }

    /** Session management **/
    @Override
    public void clearSessionData() {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all data in realm and clear shard preferences
                realm.deleteAll();
                LecetSharedPreferenceUtil.getInstance(appCompatActivity).clearPreferences();
                rerouteLoggedOutUser();
            }
        });
    }
/* Progress Dialog */
    public void showProgressDialog(String title, String message) {

        dismissProgressDialog();

        progressDialog = ProgressDialog.show(appCompatActivity, title, message, true, false);
    }

    public void dismissProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }
    private void rerouteLoggedOutUser() {

        dismissProgressDialog();

        Intent i = new Intent(appCompatActivity, LauncherActivity.class);
        // Clear activity stack
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        appCompatActivity.startActivity(i);
        appCompatActivity.finish();
    }

}
