package com.lecet.app.viewmodel;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.adapters.CompanyContactsAdapter;
import com.lecet.app.data.models.Contact;
import com.lecet.app.domain.CompanyDomain;

import io.realm.RealmList;

/**
 * File: CompanyContactsViewModel Created: 2/10/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyContactsViewModel extends BaseActivityViewModel {

    private final AppCompatActivity appCompatActivity;
    private final CompanyDomain companyDomain;
    private final long companyID;

    /**
     * Tool Bar
     **/
    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private ImageView sortButton;


    public CompanyContactsViewModel(AppCompatActivity appCompatActivity, CompanyDomain companyDomain, long companyID) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        this.companyDomain = companyDomain;
        this.companyID = companyID;
        initRecyclerView(appCompatActivity);
    }


    public void setToolbar(View toolbar, String title, String subtitle) {
        titleTextView = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);
        backButton = (ImageView) toolbar.findViewById(R.id.back_button);
        sortButton = (ImageView) toolbar.findViewById(R.id.sort_menu_button);
        sortButton.setVisibility(View.INVISIBLE);
//        backButton.setVisibility(View.INVISIBLE);

        titleTextView.setText(title);
        subtitleTextView.setText(subtitle);
    }


    private void initRecyclerView(AppCompatActivity activity) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        initAdapter(recyclerView);
    }

    private void initAdapter(RecyclerView recyclerView) {

        CompanyContactsAdapter adapter = new CompanyContactsAdapter(this.appCompatActivity, getCompanyContacts());
        recyclerView.setAdapter(adapter);
    }

    private RealmList<Contact> getCompanyContacts() {

        return companyDomain.fetchCompany(companyID).first().getContacts();
    }
}
