package com.lecet.app.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import android.util.Log;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.UserDomain;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * File: LecetFirebaseInstanceIdService
 * Created: 5/15/17
 * Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class LecetFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "LecetFirebaseService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

        LecetSharedPreferenceUtil sharedPref = LecetSharedPreferenceUtil.getInstance(getApplicationContext());
        sharedPref.setFirebaseToken(token);
        if (sharedPref.getId() != -1) {

            // We can assume a new token has been created and we should update the server.

            UserDomain domain = new UserDomain(LecetClient.getInstance(), sharedPref, null);
            Call<ResponseBody> call = domain.registerFirebaseToken(token);
            try {
                Response<ResponseBody> response = call.execute();
                if (response.isSuccessful()) {

                    Log.d(TAG, "Firebase token successfully registered");
                } else {

                    Log.e(TAG, response.message());
                }

            } catch (IOException e) {
                Log.getStackTraceString(e);
            }
        }
    }
}
