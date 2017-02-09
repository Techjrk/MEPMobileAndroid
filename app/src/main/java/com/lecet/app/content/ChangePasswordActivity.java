package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.contentbase.LecetBaseActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityChangePasswordBinding;
import com.lecet.app.domain.UserDomain;
import com.lecet.app.viewmodel.ChangePasswordViewModel;

import io.realm.Realm;

public class ChangePasswordActivity extends LecetBaseActivity {

    private ChangePasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityChangePasswordBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        viewModel = new ChangePasswordViewModel(this, new UserDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance()));
        binding.setViewModel(viewModel);
        setupToolbar(viewModel, getString(R.string.title_activity_change_password), "");
    }

    private void setupToolbar(ChangePasswordViewModel viewModel, String title, String subTitle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = getLayoutInflater();

            View tb = inflater.inflate(R.layout.include_app_bar_layout_save, null);

            actionBar.setCustomView(tb);
            actionBar.setDisplayShowCustomEnabled(true);

            viewModel.setToolbar(tb, title, subTitle);
        }
    }

    @Override
    public void onKeyboardEditorActionSelected() {

        viewModel.validateFormFields();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, NetworkInfo networkInfo) {

    }
}
