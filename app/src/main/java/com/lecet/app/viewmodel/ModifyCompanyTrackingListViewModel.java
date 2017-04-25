package com.lecet.app.viewmodel;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;
import com.lecet.app.adapters.ModifyCompanyListAdapter;
import com.lecet.app.adapters.ModifyListAdapter;
import com.lecet.app.adapters.MoveToAdapter;
import com.lecet.app.adapters.MoveToCompanyListAdapter;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.CompanyTrackingList;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MTMMenuCallback;
import com.lecet.app.interfaces.MoveToListCallback;
import com.lecet.app.interfaces.TrackingListObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: ModifyCompanyTrackingListViewModel Created: 12/7/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ModifyCompanyTrackingListViewModel extends ModifyTrackingListViewModel<CompanyTrackingList, Company> {

    private static final int TYPE_LAST_UPDATE = 0;
    private static final int TYPE_ALPHABETICAL = 1;

    private TrackingListDomain trackingListDomain;

    public ModifyCompanyTrackingListViewModel(AppCompatActivity appCompatActivity, CompanyTrackingList trackingList, @TrackingSort int sortBy, TrackingListDomain trackingListDomain) {
        super(appCompatActivity, trackingList, sortBy);

        this.trackingListDomain = trackingListDomain;
    }

    @Override
    public RealmResults<Company> getData(CompanyTrackingList trackingList, @TrackingSort int sortBy) {
        return filterRealmOrderedCollection(trackingList.getCompanies(), sortBy);
    }

    @Override
    public int getSortBySelectedPosition(int position) {

        switch (position) {
            case TYPE_LAST_UPDATE:
                return SORT_COMPANY_UPDATED;
            case TYPE_ALPHABETICAL:
                return SORT_COMPANY_NAME;
            default:
                return SORT_COMPANY_UPDATED;
        }
    }

    @Override
    public ModifyListAdapter getListAdapter(AppCompatActivity appCompatActivity, RealmResults<Company> dataItems) {
        return new ModifyCompanyListAdapter(appCompatActivity, dataItems);
    }

    @Override
    public MoveToAdapter getMoveToListAdapter(AppCompatActivity appCompatActivity, String title, MoveToListCallback callback, RealmResults<CompanyTrackingList> lists) {
        return new MoveToCompanyListAdapter(appCompatActivity, title, lists, callback);
    }

    @Override
    public RealmResults<CompanyTrackingList> getUserTrackingListsExcludingCurrentList(CompanyTrackingList currentTrackingList) {
        return trackingListDomain.fetchCompanyTrackingListsExcludingCurrentList(currentTrackingList.getId());
    }

    @Override
    public void handleDoneClicked(List<Company> selectedItems) {

        if (selectedItems != null && selectedItems.size() > 0) {

            showDoneDialog(getTrackingList().getName(), getAppCompatActivity().getString(R.string.pending_changes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finishWithEditedResult();
                }
            });

        } else {

            finishWithEditedResult();
        }
    }

    @Override
    public void handleMoveItemsClicked(final List<Company> selectedItems, final CompanyTrackingList trackingList) {

        String message = String.format(getAppCompatActivity().getString(R.string.move_selected_item_from_list_message), getAppCompatActivity().getString(R.string.companies), trackingList.getName());
        showConfirmationDialog(message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                moveItems(getTrackingList().getId(), getSelectedIds(selectedItems), trackingList.getId(), getAddedIds(selectedItems, trackingList), getRetainedIds(selectedItems));

            } // Positive Interface
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            } // Negative Interface
        });
    }

    @Override
    public void handleRemoveItemsClicked(final List<Company> selectedItems) {

        String listName = getTrackingList().getName();
        String message = String.format(getAppCompatActivity().getString(R.string.move_selected_item_from_list_message), getAppCompatActivity().getString(R.string.companies), listName);
        showConfirmationDialog(message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                removeItems(getTrackingList().getId(), getSelectedIds(selectedItems), getRetainedIds(selectedItems));

            } // Positive Interface
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            } // Negative Interface
        });
    }

    private List<Long> getSelectedIds(List<Company> selectedItems) {

        List<Long> ids = new ArrayList<>(selectedItems.size());

        for (Company company : selectedItems) {
            ids.add(company.getId());
        }

        return ids;
    }

    private List<Long> getRetainedIds(List<Company> selectedItems) {

        List<Long> currentIds = getSelectedIds(getTrackingList().getCompanies());

        for (Company company : selectedItems) {

            currentIds.remove(company.getId());
        }

        return currentIds;
    }

    private List<Long> getAddedIds(List<Company> selectedItems, CompanyTrackingList trackingList) {
        RealmList<Company> projects = trackingListDomain.fetchCompanyTrackingList(trackingList.getId()).getCompanies();

        List<Long> currentIds = getSelectedIds(projects);

        for (Company company : selectedItems) {

            currentIds.add(company .getId());
        }

        return currentIds;
    }


    private void asyncDeleteProjects(List<Long> toBeDeletedIds) {

        trackingListDomain.deleteCompaniesFromTrackingListAsync(getTrackingList().getId(), toBeDeletedIds, new LecetCallback<CompanyTrackingList>() {
            @Override
            public void onSuccess(CompanyTrackingList result) {

                dismissProgressDialog();
                setTrackingList(trackingListDomain.fetchCompanyTrackingList(getTrackingList().getId()));
                updateDataItems((filterRealmOrderedCollection(getTrackingList().getCompanies(), getSelectedSort())));
            }

            @Override
            public void onFailure(int code, String message) {

                showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), message);
            }
        });
    }

    private void moveItems(long source, final List<Long> toBeDeleted, long destination, List<Long> destinationItems, final List<Long> sourceIds) {

        showProgressDialog(getAppCompatActivity().getString(R.string.app_name), getAppCompatActivity().getString(R.string.updating));

        trackingListDomain.moveCompaniesToDestinationTrackingList(source, destination, destinationItems, sourceIds, new LecetCallback() {
            @Override
            public void onSuccess(Object result) {

                if (sourceIds.size() != 1) {
                    updateToolbarSubTitle(sourceIds.size(), getAppCompatActivity().getResources().getString(R.string.companies));
                } else {
                    updateToolbarSubTitle(sourceIds.size(), getAppCompatActivity().getResources().getString(R.string.company));
                }

                // Remove items from Tracking list relationship
                asyncDeleteProjects(toBeDeleted);
            }

            @Override
            public void onFailure(int code, String message) {

                showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), message);
            }
        });
    }

    private void removeItems(long source, final List<Long> selectedItems, final List<Long> retainedItems) {

        showProgressDialog(getAppCompatActivity().getString(R.string.app_name), getAppCompatActivity().getString(R.string.updating));

        trackingListDomain.syncCompanyTrackingList(source, retainedItems, new Callback<CompanyTrackingList>() {
            @Override
            public void onResponse(Call<CompanyTrackingList> call, Response<CompanyTrackingList> response) {

                if (response.isSuccessful()) {

                    if (retainedItems.size() != 1) {
                        updateToolbarSubTitle(retainedItems.size(), getAppCompatActivity().getResources().getString(R.string.companies));
                    } else {
                        updateToolbarSubTitle(retainedItems.size(), getAppCompatActivity().getResources().getString(R.string.company));
                    }

                    // Remove items from Tracking list relationship
                    asyncDeleteProjects(selectedItems);

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
}
