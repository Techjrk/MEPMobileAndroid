package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityLoginBinding;
import com.lecet.app.domain.UserDomain;
import com.lecet.app.viewmodel.LoginViewModel;

import io.realm.Realm;

public class LoginActivity extends LecetBaseActivity {

    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setupBinding();
        animateLoginLayout();
    }

    private void setupBinding() {
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        LoginViewModel viewModel = new LoginViewModel(this, new UserDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance()));
        binding.setViewModel(viewModel);
    }

    private void animateLoginLayout() {
        final View animView = findViewById(R.id.login_layout);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.login_slide_up);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });

        animView.startAnimation(animation);
    }

}
