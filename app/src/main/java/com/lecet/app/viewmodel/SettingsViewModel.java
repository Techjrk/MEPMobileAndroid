package com.lecet.app.viewmodel;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.content.ChangePasswordActivity;
import com.lecet.app.content.LauncherActivity;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.UserDomain;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: SettingsViewModel Created: 8/22/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class SettingsViewModel extends BaseActivityViewModel {

    private final AppCompatActivity appCompatActivity;
    private final UserDomain userDomain;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;

    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private TextView saveButton;

    private boolean enableNotifications;

    public SettingsViewModel(AppCompatActivity appCompatActivity, UserDomain userDomain, LecetSharedPreferenceUtil sharedPreferenceUtil) {
        this.appCompatActivity = appCompatActivity;
        this.userDomain = userDomain;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        setEnableNotifications(sharedPreferenceUtil.getNotificationsSetting());
    }

    @Bindable
    public boolean isEnableNotifications() {
        return enableNotifications;
    }

    public void setEnableNotifications(boolean enableNotifications) {
        this.enableNotifications = enableNotifications;
        this.sharedPreferenceUtil.setNotificationSetting(enableNotifications);
        notifyPropertyChanged(BR.enableNotifications);
    }

    public void onChangePasswordSelected(View view) {

        Intent intent = new Intent(appCompatActivity, ChangePasswordActivity.class);
        appCompatActivity.startActivity(intent);
    }

    public void onLogoutSelected(View view) {

        showTwoButtonAlertDialog(appCompatActivity, appCompatActivity.getString(R.string.app_name), appCompatActivity.getString(R.string.logout_message),
                appCompatActivity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }, appCompatActivity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                logoutUser();
            }
        });
    }

    /** Networking **/

    private void logoutUser() {

        showProgressDialog(appCompatActivity, appCompatActivity.getString(R.string.app_name), appCompatActivity.getString(R.string.logging_out));

        userDomain.logout(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    LecetSharedPreferenceUtil.getInstance(appCompatActivity).clearPreferences();

                    dismissProgressDialog();

                    Intent i = new Intent(appCompatActivity, LauncherActivity.class);
                    // Clear activity stack
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    appCompatActivity.startActivity(i);
                    appCompatActivity.finish();

                } else {

                    dismissProgressDialog();

                    showCancelAlertDialog(appCompatActivity, appCompatActivity.getString(R.string.error_network_title), response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                dismissProgressDialog();

                showCancelAlertDialog(appCompatActivity, appCompatActivity.getString(R.string.error_network_title), appCompatActivity.getString(R.string.error_network_message));
            }
        });
    }

    /** Toolbar **/

    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);
        backButton = (ImageView) toolbar.findViewById(R.id.back_button);
        saveButton = (TextView) toolbar.findViewById(R.id.save_text_view);

        backButton.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        titleTextView.setText(title);
        subtitleTextView.setText(subtitle);
    }

}
