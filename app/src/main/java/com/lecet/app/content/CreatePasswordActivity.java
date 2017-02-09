package com.lecet.app.content;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.data.api.request.CreateUserRequest;

public class CreatePasswordActivity extends AppCompatActivity {

    private final String TAG = "CreatePasswordActivity";

    public static final String EXTRA_CREATE_USER = "create_user_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_create_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // EXAMPLE of getting CreateUserRequest
        CreateUserRequest request = (CreateUserRequest)getIntent().getParcelableExtra(EXTRA_CREATE_USER);
    }

}
