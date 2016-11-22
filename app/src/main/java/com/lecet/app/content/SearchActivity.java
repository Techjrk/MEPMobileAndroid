package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityLoginBinding;
import com.lecet.app.databinding.ActivitySearchBinding;
import com.lecet.app.domain.SearchDomain;
import com.lecet.app.domain.UserDomain;
import com.lecet.app.viewmodel.LoginViewModel;
import com.lecet.app.viewmodel.SearchViewModel;
import io.realm.Realm;

//userid: harry.camigla@domandtom.com
//password: 3nB72JTrRB7Uu4mFRpFppV6PN

public class SearchActivity extends AppCompatActivity {
    private final String TAG = "SearchActivity";
    TextView tvsearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_search);
        tvsearch = (TextView) findViewById(R.id.tvsearch);
        setupBinding();
    }
    private void setupBinding() {
        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
//        LoginViewModel viewModel = new LoginViewModel(this, new UserDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(getApplication()), Realm.getDefaultInstance()));
        SearchViewModel viewModel = new SearchViewModel(this, new SearchDomain(LecetClient.getInstance(),LecetSharedPreferenceUtil.getInstance(getApplication()),Realm.getDefaultInstance()));
        binding.setViewModel(viewModel);
     //   tvsearch.setText(viewModel.getSlist());
    }
}

