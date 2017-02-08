package com.lecet.app.viewmodel;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.content.CompanyDetailActivity;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Contact;
import com.lecet.app.domain.CompanyDomain;

/**
 * File: ContactViewModel Created: 1/26/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class ContactViewModel {
    /**
     * Tool Bar
     **/
    private TextView titleTextView;
    private TextView subtitleTextView;
    private ImageView backButton;
    private ImageView sortButton;


    private final AppCompatActivity appCompatActivity;
    private final CompanyDomain companyDomain;
    private Contact contact;
    private Company company;

    public ContactViewModel(AppCompatActivity appCompatActivity, CompanyDomain companyDomain, long contactID) {
        this.appCompatActivity = appCompatActivity;
        this.companyDomain = companyDomain;

        this.contact = companyDomain.fetchCompanyContact(contactID);
        this.company = contact.getCompany();

        if (this.company == null) {

            this.company = companyDomain.fetchCompany(contact.getCompanyId()).first();
        }
    }

    public void setToolbar(View toolbar) {
        titleTextView = (TextView) toolbar.findViewById(R.id.title_text_view);
        subtitleTextView = (TextView) toolbar.findViewById(R.id.subtitle_text_view);
        backButton = (ImageView) toolbar.findViewById(R.id.back_button);
        sortButton = (ImageView) toolbar.findViewById(R.id.sort_menu_button);
        sortButton.setVisibility(View.INVISIBLE);

        //TODO - check the binding in the layout, which is not triggering the button clicks in this VM
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonClick(v);
            }
        });

        titleTextView.setText(contact.getName());
    }

    public void onBackButtonClick(View view) {

        appCompatActivity.onBackPressed();
    }

    /** Text Values **/

    public Spannable getJobTitleSpannable() {

        String preCompany = (contact.getTitle() != null ? contact.getTitle() : "[ not given ]") + " " + appCompatActivity.getString(R.string.at) + " ";
        String companyName = company.getName();
        Spannable spannable = new SpannableString(preCompany + companyName);
        int companyNameLength =  companyName.length();
        if (preCompany.length() > companyNameLength) {
            companyNameLength = preCompany.length() +1;
        }
        spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(appCompatActivity, R.color.lecetSpannableBlue)), preCompany.length(), companyName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(appCompatActivity, R.color.lecetSpannableBlue)), preCompany.length(), companyName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }

    public String getAddress() {

        String address = company.getAddress1() + " " + (company.getAddress2() != null ? company.getAddress2() : "") + "\n" + company.getCity() + " " + company.getState() + " " + company.getZipPlus4();
        return address;
    }

    public String getPhoneNumber() {

        return contact.getPhoneNumber();
    }

    public String getEmail() {

        return contact.getEmail();
    }

    /** OnClicks **/

    public void onJobTitleSelected(View view) {

        Intent intent = new Intent(view.getContext(), CompanyDetailActivity.class);
        intent.putExtra(CompanyDetailActivity.COMPANY_ID_EXTRA, contact.getCompanyId());
        view.getContext().startActivity(intent);
    }

    public void onAddressSelected(View View) {

        String mapPoint = generateCenterPointAddress(company, "+");

        Uri gmmIntentUri = Uri.parse(String.format("geo:0,0?q=%s", mapPoint));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(appCompatActivity.getPackageManager()) != null) {
            appCompatActivity.startActivity(mapIntent);
        }
    }

    /** Maps **/


    private String generateCenterPointAddress(Company company, String bindingCharacter) {

        StringBuilder stringBuilder = new StringBuilder();

        if (company.getAddress1() != null) {
            stringBuilder.append(company.getAddress1());
            stringBuilder.append(bindingCharacter);
        }

        if (company.getAddress2() != null) {
            stringBuilder.append(company.getAddress2());
            stringBuilder.append(bindingCharacter);
        }

        if (company.getCity() != null) {
            stringBuilder.append(company.getCity());
            stringBuilder.append(bindingCharacter);
        }

        if (company.getState() != null) {
            stringBuilder.append(company.getState());
        }

        if (company.getZip5() != null) {
            stringBuilder.append(bindingCharacter);
            stringBuilder.append(company.getZipPlus4());
        }


        return stringBuilder.toString();
    }


}
