package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.databinding.ActivityLoginBinding;
import com.lecet.app.domain.LoginDomain;
import com.lecet.app.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setupBinding();
        initViews();
        setupBinding();
    }

    private void initViews() {

        TextView logoTV = (TextView) findViewById(R.id.logo_text_view);
        Spannable ws = new SpannableString(getString(R.string.app_name));
        ws.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorTuatara)), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ws.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorChristie)), 6, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        logoTV.setText(ws);

        TextView pTV = (TextView) findViewById(R.id.powered_by_text_view);
        pTV.setTextColor(ContextCompat.getColor(this, R.color.colorTuatara));
    }

    private void setupBinding() {
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        LoginViewModel viewModel = new LoginViewModel(this, new LoginDomain(LecetClient.getInstance()));
        binding.setViewModel(viewModel);
    }
}
