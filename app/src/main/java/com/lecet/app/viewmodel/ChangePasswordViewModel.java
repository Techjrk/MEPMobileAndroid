package com.lecet.app.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.lecet.app.BR;
import com.lecet.app.data.api.request.SetPasswordRequest;
import com.lecet.app.domain.SettingsDomain;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: ChangePasswordViewModel Created: 8/25/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class ChangePasswordViewModel extends BaseObservable {

    private static final String TAG = "ChangePasswordViewModel";

    private final Context context;
    private final SettingsDomain settingsDomain;

    private String newPasswordEntry;                  // first entry of new password
    private String newPasswordConfirm;                // confirmation entry of new password
    private boolean newPasswordEntryValid;            // first entry is valid?
    private boolean newPasswordConfirmValid;          // confirmation entry is valid?
    private boolean passwordsMatch;                   // first entry and confirmation passwords match?


    public ChangePasswordViewModel(Context context, SettingsDomain sd) {        //TODO - is this the correct domain? or UserDomain?

        this.context = context;
        this.settingsDomain = sd;

        Log.d(TAG, "Constructor");
    }

    @Bindable
    public String getNewPasswordEntry() {
        return newPasswordEntry;
    }

    public void setNewPasswordEntry(String newPasswordEntry) {
        this.newPasswordEntry = newPasswordEntry;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public boolean getNewPasswordEntryValid() {
        return newPasswordEntryValid;
    }

    public void setNewPasswordEntryValid(boolean newPasswordEntryValid) {
        this.newPasswordEntryValid = newPasswordEntryValid;
        notifyPropertyChanged(BR.emailValid);
    }

    @Bindable
    public boolean isNewPasswordConfirmValid() {
        return newPasswordConfirmValid;
    }

    public void setNewPasswordConfirmValid(boolean newPasswordConfirmValid) {
        this.newPasswordConfirmValid = newPasswordConfirmValid;
        notifyPropertyChanged(BR.emailValid);
    }

    @Bindable
    public boolean getPasswordsMatch() {
        return passwordsMatch;
    }

    public void setPasswordsMatch(boolean passwordsMatch) {
        this.passwordsMatch = passwordsMatch;
        notifyPropertyChanged(BR.passwordValid);
    }


    ///////////////////////////////////////////////////

    private void resetPasswordValues() {
        newPasswordEntry   = "";
        newPasswordConfirm = "";
    }

    private void sendPasswordChangeRequest(String password) {
        settingsDomain.setNewPassword(newPasswordEntry, new Callback<SetPasswordRequest>() {
            @Override
            public void onResponse(Call<SetPasswordRequest> call, Response<SetPasswordRequest> response) {

                if (response.isSuccessful()) {

                    //TODO - fill in
                } else {
                    //TODO - fill in

                }
            }

            @Override
            public void onFailure(Call<SetPasswordRequest> call, Throwable t) {
                //TODO - fill in
            }
        });
    }

    /** OnClick handlers **/

    @SuppressWarnings("unused")
    public void onPasswordEntryClicked(View view) {

        Log.d(TAG, "onPasswordEntryClicked **************");
        resetPasswordValues();

        //TODO show keyboard
        InputMethodManager imm = (InputMethodManager) this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    @SuppressWarnings("unused")
    public void onUpdatePasswordButtonClicked(View view) {

        Log.d(TAG, "onUpdatePasswordButtonClicked **************");

    }


}
