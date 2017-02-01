package com.lecet.app.viewmodel;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.data.models.User;
import com.lecet.app.domain.UserDomain;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: ChangePasswordViewModel Created: 1/30/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class ChangePasswordViewModel extends BaseObservable {

    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private TextView saveButton;

    private ProgressDialog progressDialog;
    private Dialog alertDialog;

    private final AppCompatActivity context;
    private final UserDomain userDomain;

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;



    public ChangePasswordViewModel(AppCompatActivity context, UserDomain userDomain) {
        this.context = context;
        this.userDomain = userDomain;
    }

    @Bindable
    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
        notifyPropertyChanged(BR.currentPassword);
    }

    @Bindable
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        notifyPropertyChanged(BR.currentPassword);
    }

    @Bindable
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        notifyPropertyChanged(BR.currentPassword);
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

    /** Password Logic **/

    private boolean isCurrentPasswordValid() {
        return userDomain.isValidPassword(currentPassword);
    }

    private boolean isConfirmPasswordValid(String password, String confirm) {

        return confirm != null && password.equals(confirm);
    }

    private boolean isNewPasswordValid() {
        return userDomain.isValidPassword(newPassword);
    }

    /**
     * OnClick handlers
     **/

    @SuppressWarnings("unused")
    public void onSaveClicked(View view) {

        validateFormFields();
    }

    public void onBackButtonClick(View view) {

        context.onBackPressed();
    }

    public void validateFormFields() {

        dismissAlertDialog();

        boolean confirmedPassword = isConfirmPasswordValid(newPassword, currentPassword);

        if (isCurrentPasswordValid() && isNewPasswordValid() && confirmedPassword) {

            showTwoButtonAlertDialog(context.getString(R.string.title_activity_change_password), context.getString(R.string.password_confirm_message),
                    context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }, context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            postNewPassword();
                        }
                    });

        } else if (!isCurrentPasswordValid()) {

            showCancelAlertDialog(context.getString(R.string.invalid_password_title), context.getString(R.string.please_enter_current_password));

        } else if (!isNewPasswordValid()) {

            showCancelAlertDialog(context.getString(R.string.invalid_password_title), context.getString(R.string.invalid_password_message));

        } else if (!confirmedPassword) {

            showCancelAlertDialog(context.getString(R.string.invalid_password_title), context.getString(R.string.password_mismatch));
        }
    }

    /** Networking **/

    private void postNewPassword() {

        showProgressDialog(context.getString(R.string.app_name), context.getString(R.string.updating));

        userDomain.changePassword(currentPassword, newPassword, confirmPassword, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    User r = response.body();
                    userDomain.copyToRealmTransaction(r);

                    dismissProgressDialog();

                } else {

                    dismissProgressDialog();

                    showCancelAlertDialog(context.getString(R.string.error_network_title), response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                dismissProgressDialog();

                showCancelAlertDialog(context.getString(R.string.error_network_title), context.getString(R.string.error_network_message));
            }
        });
    }


    /** Dialogs **/

    private void showProgressDialog(String title, String message) {

        dismissProgressDialog();

        progressDialog = ProgressDialog.show(context, title, message, true, false);
    }

    private void dismissProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    private void dismissAlertDialog() {

        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();;
    }

    private void showCancelAlertDialog(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(context.getString(R.string.ok), null);

        alertDialog = builder.show();
    }

    private void showTwoButtonAlertDialog(String title, String message, String negativeTitle, DialogInterface.OnClickListener negativeListener,
                                          String positiveTitle, DialogInterface.OnClickListener positiveListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(negativeTitle, negativeListener);
        builder.setPositiveButton(positiveTitle, positiveListener);

        alertDialog = builder.show();
    }
}
