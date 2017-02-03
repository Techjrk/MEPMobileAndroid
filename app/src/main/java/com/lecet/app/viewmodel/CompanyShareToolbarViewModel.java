package com.lecet.app.viewmodel;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;
import com.lecet.app.adapters.MoveToAdapter;
import com.lecet.app.adapters.MoveToCompanyListAdapter;
import com.lecet.app.content.LecetConfirmDialogFragment;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MoveToListCallback;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: CompanyShareToolbarViewModel Created: 1/24/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class CompanyShareToolbarViewModel extends ShareToolbarViewModel<Company, CompanyTrackingList> implements LecetConfirmDialogFragment.ConfirmDialogListener {


    public CompanyShareToolbarViewModel(AppCompatActivity appCompatActivity, TrackingListDomain trackingListDomain, Company trackedObject) {
        super(appCompatActivity, trackingListDomain, trackedObject);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {


    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

        clearRadioGroup();
        dialog.dismiss();
    }

    @Override
    public void onDialogCancel(DialogFragment dialog) {

        clearRadioGroup();
    }

    @Override
    public MoveToAdapter getMoveToListAdapter(AppCompatActivity appCompatActivity, String title, MoveToListCallback callback, RealmResults<CompanyTrackingList> lists) {
        return new MoveToCompanyListAdapter(appCompatActivity, title, lists, callback);
    }

    @Override
    public CompanyTrackingList getAssociatedTrackingList(Company trackedObject) {

        RealmResults<CompanyTrackingList> results = getTrackingListDomain().fetchCompanyTrackingListsContainingCompany(trackedObject.getId());
        if (results.size() > 0) {

            return results.first();
        }

        return null;
    }

    @Override
    public RealmResults<CompanyTrackingList> getUserTrackingListsExcludingCurrentList(CompanyTrackingList currentTrackingList) {
        return getTrackingListDomain().fetchCompanyTrackingListsExcludingCurrentList(currentTrackingList.getId());
    }

    @Override
    public RealmResults<CompanyTrackingList> getAllUserTrackingLists() {
        return getTrackingListDomain().fetchUserCompanyTrackingList();
    }

    @Override
    public RealmList<Company> getTrackedItems(CompanyTrackingList trackingList) {
        return trackingList.getCompanies();
    }

    @Override
    public void removeTrackedObjectFromTrackingList(long trackingListId, List<Long> trackedIds) {

        showProgressDialog(getAppCompatActivity().getString(R.string.app_name), getAppCompatActivity().getString(R.string.updating));

        getTrackingListDomain().syncCompanyTrackingList(trackingListId, trackedIds, new Callback<CompanyTrackingList>() {
            @Override
            public void onResponse(Call<CompanyTrackingList> call, Response<CompanyTrackingList> response) {

                if (response.isSuccessful()) {

                    List<Long> selectedItems = new ArrayList<>();
                    selectedItems.add(getTrackedObject().getId());

                    // Remove items from Tracking list relationship
                    asyncDeleteCompanies(getTrackedObject().getId(), selectedItems);

                } else {

                    showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), response.message());
                }
            }

            @Override
            public void onFailure(Call<CompanyTrackingList> call, Throwable t) {

                showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), getAppCompatActivity().getString(R.string.error_network_message));
            }
        });
    }

    @Override
    public void addTrackedObjectToTrackingList(long trackingListId, List<Long> trackedIds) {

        showProgressDialog(getAppCompatActivity().getString(R.string.app_name), getAppCompatActivity().getString(R.string.updating));

        getTrackingListDomain().syncCompanyTrackingList(trackingListId, trackedIds, new Callback<CompanyTrackingList>() {
            @Override
            public void onResponse(Call<CompanyTrackingList> call, Response<CompanyTrackingList> response) {

                if (response.isSuccessful()) {

                    dismissProgressDialog();

                } else {

                    showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), response.message());
                }
            }

            @Override
            public void onFailure(Call<CompanyTrackingList> call, Throwable t) {

                showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), getAppCompatActivity().getString(R.string.error_network_message));
            }
        });
    }

    @Override
    public void onShareObjectSelected(Company trackedObject) {

        String projectUrl = LecetClient.ENDPOINT + "company/" + trackedObject.getId();

        StringBuilder sb = new StringBuilder();
        sb.append("COMPANY NAME : ");
        sb.append(trackedObject.getName());
        sb.append("/n");
        sb.append("WEB LINK : ");
        sb.append(projectUrl);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        sendIntent.setType("text/plain");
        getAppCompatActivity().startActivity(Intent.createChooser(sendIntent, getAppCompatActivity().getResources().getText(R.string.share_company)));
    }

    @Override
    public void onHideObjectSelected(Company trackedObject) {

    }


    /* Delete */
    private void asyncDeleteCompanies(long trackingListId, List<Long> toBeDeletedIds) {

        getTrackingListDomain().deleteCompaniesFromTrackingListAsync(trackingListId, toBeDeletedIds, new LecetCallback<CompanyTrackingList>() {
            @Override
            public void onSuccess(CompanyTrackingList result) {

                dismissProgressDialog();
            }

            @Override
            public void onFailure(int code, String message) {

                showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), message);
            }
        });
    }
}
