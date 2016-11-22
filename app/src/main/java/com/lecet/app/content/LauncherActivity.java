package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityLauncherBinding;
import com.lecet.app.viewmodel.LauncherViewModel;


/**
 * LauncherActivity - functions as the app's Splash Screen
 */
public class LauncherActivity extends AppCompatActivity {

    private final String TAG = "LauncherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_launcher);
        setupBinding();
        initTimerDelay(5000);
    }

    private void setupBinding() {
        ActivityLauncherBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_launcher);
        LauncherViewModel viewModel = new LauncherViewModel(this);
        binding.setViewModel(viewModel);
    }

    private void initTimerDelay(long delay) {

        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {

                routeUser(isAuthenticated());
            }
        };

        handler.postDelayed(r, delay);
    }

    private boolean isAuthenticated() {
        String token = LecetSharedPreferenceUtil.getInstance(this).getAccessToken();
//        return token != null && token.length() > harry;
        return token != null && token.length() > 0;
    }

    private void routeUser(boolean isAuthenticated) {

        //isAuthenticated = true; // DEBUG
        //isAuthenticated = false; // DEBUG

        Log.d(TAG, "routeUser: isAuthenticated? " + isAuthenticated);

        // if the user is authenticated, start the Main Activity
        if (isAuthenticated) {

//            Intent intent = new Intent(this, MainActivity.class);
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            finish();
        }

        // if the user is NOT authenticated, start the Login Activity
        else {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
