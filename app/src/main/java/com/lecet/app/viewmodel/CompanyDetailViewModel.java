package com.lecet.app.viewmodel;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.lecet.app.R;
import com.lecet.app.adapters.CompanyDetailAdapter;
import com.lecet.app.content.CompanyDetailActivity;
import com.lecet.app.data.models.Company;
import com.lecet.app.domain.CompanyDomain;
import com.lecet.app.domain.ProjectDomain;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: CompanyDetailViewModel Created: 1/24/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyDetailViewModel {

    private static final String TAG = "CompanyDetailViewModel";

    private final long companyID;

    private final WeakReference<CompanyDetailActivity> activityWeakReference;
    private final CompanyDomain companyDomain;
    private final ProjectDomain projectDomain;

    private Company company;
    private AlertDialog networkAlertDialog;
    private CompanyDetailAdapter companyDetailAdapter;

    // Retrofit calls
    private Call<Company> companyDetailCall;


    public CompanyDetailViewModel(CompanyDetailActivity activity, long companyID, CompanyDomain companyDomain, ProjectDomain projectDomain) {

        this.activityWeakReference = new WeakReference<>(activity);
        this.companyID = companyID;
        this.companyDomain = companyDomain;
        this.projectDomain = projectDomain;
    }

    /**
     * Network
     **/

    public void cancelGetCompanyDetailRequest() {

        if (companyDetailCall != null && !companyDetailCall.isCanceled()) {

            companyDetailCall.cancel();
        }
    }


    public void getCompanyDetail() {

        getCompanyDetail(companyID);
    }


    private void getCompanyDetail(final long companyID) {

        companyDetailCall = companyDomain.getCompanyDetails(companyID, new Callback<Company>() {
            @Override
            public void onResponse(Call<Company> call, Response<Company> response) {

                CompanyDetailActivity activity = activityWeakReference.get();

                if (activity == null) return;

                if (response.isSuccessful()) {

                    Company responseCompany = response.body();
                    companyDomain.copyToRealmTransaction(responseCompany);

                    // Fetch updated project
                    company = companyDomain.fetchCompany(companyID).first();

                    // Setup RecyclerView
                    initCompanyDetailAdapter(activity, company);

                    // Setup paralax imageview
                    initMapImageView(activity, getMapUrl(company));

                } else {

                    if (activity.isDisplayingNetworkAlert()) {

                        activity.hideNetworkAlert();

                        if (networkAlertDialog != null && networkAlertDialog.isShowing())
                            networkAlertDialog.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle(activity.getString(R.string.error_network_title));
                        builder.setMessage(response.message());
                        builder.setNegativeButton(activity.getString(R.string.ok), null);

                        networkAlertDialog = builder.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {

                CompanyDetailActivity activity = activityWeakReference.get();

                if (activity == null) return;

                if (networkAlertDialog != null && networkAlertDialog.isShowing())
                    networkAlertDialog.dismiss();

                activity.showNetworkAlert();
            }
        });
    }


    /**
     * View management
     **/
    private void initCompanyDetailAdapter(CompanyDetailActivity activity, Company company) {

        companyDetailAdapter = new CompanyDetailAdapter(activity, company, projectDomain);
        initRecyclerView(activity, companyDetailAdapter);
    }


    private void initRecyclerView(CompanyDetailActivity activity, CompanyDetailAdapter adapter) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    private void initMapImageView(CompanyDetailActivity activity, String mapUrl) {

        ImageView imageView = (ImageView) activity.findViewById(R.id.parallax_image_view);
        Picasso.with(imageView.getContext()).load(mapUrl).fit().into(imageView);
    }


    public String getMapUrl(Company company) {

        CompanyDetailActivity activity = activityWeakReference.get();

        String mapStr;

        StringBuilder sb2 = new StringBuilder();
        sb2.append("https://maps.googleapis.com/maps/api/staticmap");
        sb2.append("?center=");
        sb2.append(company.getAddress1() + ",");
        sb2.append(company.getAddress2() + ",");
        sb2.append(company.getCity() + ",");
        sb2.append(company.getState());
        sb2.append("&zoom=16");
        sb2.append("&size=200x200");
        sb2.append("&markers=color:blue|");
        sb2.append("&key=" + activity.getString(R.string.google_maps_key));
        mapStr = String.format((sb2.toString().replace(' ', '+')), null);

        return mapStr;
    }
}
