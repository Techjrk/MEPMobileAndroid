package com.lecet.app.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityContactDetailBinding;
import com.lecet.app.domain.CompanyDomain;
import com.lecet.app.utility.Log;
import com.lecet.app.viewmodel.ContactViewModel;

import io.realm.Realm;

public class ContactDetailActivity extends AppCompatActivity {

    public static final String CONTACT_ID_EXTRA = "com.lecet.app.content.ContactDetailActivity.contact.id.extra";

    private final String TAG = "ContactDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long contactID = getIntent().getLongExtra(CONTACT_ID_EXTRA, -1);
        Contact contact = (Contact) getIntent().getSerializableExtra("contact");

        Log.d(TAG, "onCreate: contactID: " + contactID);

        ActivityContactDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_detail);

        CompanyDomain companyDomain = new CompanyDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        ContactViewModel viewModel=null;
        if (contact == null) {
           // Log.d("contactnull","contactnull");
            //This is the old code. Will not be deleted because it might be used by other section
            viewModel = new ContactViewModel(this, companyDomain, contactID);
        }
        else {
           // Log.d("contactexist","contactexist");
            //This is the instance of ContactViewModel to pass the contact object in order
            //to provide the detail of Contacts to the ContactViewModel object.
            viewModel = new ContactViewModel(this, companyDomain, contact);
        }
        binding.setViewModel(viewModel);
        setupToolbar(viewModel);
    }

    private void setupToolbar(ContactViewModel viewModel) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflater = getLayoutInflater();

            View tb = inflater.inflate(R.layout.include_app_bar_layout_tracking_list, null);
            viewModel.setToolbar(tb);
            actionBar.setCustomView(tb);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }
}
