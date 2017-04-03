package com.lecet.app.viewmodel;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import com.lecet.app.R;
import com.lecet.app.adapters.ModifyListAdapter;
import com.lecet.app.adapters.ModifyProjectListAdapter;
import com.lecet.app.adapters.MoveToAdapter;
import com.lecet.app.adapters.MoveToProjectListAdapter;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectTrackingList;
import com.lecet.app.domain.TrackingListDomain;
import com.lecet.app.interfaces.LecetCallback;
import com.lecet.app.interfaces.MoveToListCallback;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Josué Rodríguez on 12/11/2016.
 */

public class ModifyProjectTrackingListViewModel extends ModifyTrackingListViewModel<ProjectTrackingList, Project> {

    private static final int TYPE_BID_DATE = 0;
    private static final int TYPE_LAST_UPDATE = 1;
    private static final int TYPE_DATE_ADDED = 2;
    private static final int TYPE_VALUE_HIGH = 3;
    private static final int TYPE_VALUE_LOW = 4;

    private final TrackingListDomain trackingListDomain;

    public ModifyProjectTrackingListViewModel(AppCompatActivity appCompatActivity, ProjectTrackingList trackingList, @TrackingSort int sortBy, final TrackingListDomain trackingListDomain) {
        super(appCompatActivity, trackingList, sortBy);

        this.trackingListDomain = trackingListDomain;
    }

    @Override
    public RealmResults<Project> getData(ProjectTrackingList trackingList, @TrackingSort int sortBy) {
        return filterRealmOrderedCollection(trackingList.getProjects(), sortBy);
    }

    @Override
    public int getSortBySelectedPosition(int position) {

        switch (position) {
            case TYPE_BID_DATE:
                return SORT_BID_DATE;
            case TYPE_LAST_UPDATE:
                return SORT_LAST_UPDATE;
            case TYPE_DATE_ADDED:
                return SORT_DATE_ADDED;
            case TYPE_VALUE_HIGH:
                return SORT_VALUE_HIGH;
            case TYPE_VALUE_LOW:
                return SORT_VALUE_LOW;
            default:
                return SORT_BID_DATE;
        }
    }

    @Override
    public ModifyListAdapter getListAdapter(AppCompatActivity appCompatActivity, RealmResults<Project> dataItems) {
        return new ModifyProjectListAdapter(appCompatActivity, dataItems);
    }

    @Override
    public MoveToAdapter getMoveToListAdapter(AppCompatActivity appCompatActivity, String title, MoveToListCallback callback, RealmResults<ProjectTrackingList> lists) {
        return new MoveToProjectListAdapter(appCompatActivity, title, lists, callback);
    }

    @Override
    public RealmResults<ProjectTrackingList> getUserTrackingListsExcludingCurrentList(ProjectTrackingList currentTrackingList) {
        return trackingListDomain.fetchProjectTrackingListsExcludingCurrentList(currentTrackingList.getId());
    }

    @Override
    public void handleDoneClicked(List<Project> selectedItems) {

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
    public void handleMoveItemsClicked(final List<Project> selectedItems, final ProjectTrackingList trackingList) {

        String message = String.format(getAppCompatActivity().getString(R.string.move_selected_item_from_list_message), getAppCompatActivity().getString(R.string.projects), trackingList.getName());
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
    public void handleRemoveItemsClicked(final List<Project> selectedItems) {

        String listName = getTrackingList().getName();
        String message = String.format(getAppCompatActivity().getString(R.string.remove_selected_item_from_list_message), getAppCompatActivity().getString(R.string.projects), listName);
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

    private List<Long> getSelectedIds(List<Project> selectedItems) {

        List<Long> ids = new ArrayList<>(selectedItems.size());

        for (Project project : selectedItems) {
            ids.add(project.getId());
        }

        return ids;
    }

    private List<Long> getRetainedIds(List<Project> selectedItems) {

        List<Long> currentIds = getSelectedIds(getTrackingList().getProjects());

        for (Project project : selectedItems) {

            currentIds.remove(project.getId());
        }

        return currentIds;
    }

    private List<Long> getAddedIds(List<Project> selectedItems, ProjectTrackingList trackingList) {
        RealmList<Project> projects = trackingListDomain.fetchProjectTrackingList(trackingList.getId()).getProjects();

        List<Long> currentIds = getSelectedIds(projects);

        for (Project project : selectedItems) {

            currentIds.add(project.getId());
        }

        return currentIds;
    }


    private void asyncDeleteProjects(List<Long> toBeDeletedIds) {

        trackingListDomain.deleteProjectsFromTrackingListAsync(getTrackingList().getId(), toBeDeletedIds, new LecetCallback<ProjectTrackingList>() {
            @Override
            public void onSuccess(ProjectTrackingList result) {

                dismissProgressDialog();
                setTrackingList(trackingListDomain.fetchProjectTrackingList(getTrackingList().getId()));
                updateDataItems((filterRealmOrderedCollection(getTrackingList().getProjects(), getSelectedSort())));
            }

            @Override
            public void onFailure(int code, String message) {

                showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), message);
            }
        });
    }

    private void moveItems(long source, final List<Long> toBeDeleted, long destination, List<Long> destinationItems, List<Long> sourceIds) {

        showProgressDialog(getAppCompatActivity().getString(R.string.app_name), getAppCompatActivity().getString(R.string.updating));

        trackingListDomain.moveProjectsToDestinationTrackingList(source, destination, destinationItems, sourceIds, new LecetCallback() {
            @Override
            public void onSuccess(Object result) {

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

        trackingListDomain.syncProjectTrackingList(source, retainedItems, new Callback<ProjectTrackingList>() {
            @Override
            public void onResponse(Call<ProjectTrackingList> call, Response<ProjectTrackingList> response) {

                if (response.isSuccessful()) {

                    // Remove items from Tracking list relationship
                    asyncDeleteProjects(selectedItems);

                } else {

                    showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), response.message());
                }
            }

            @Override
            public void onFailure(Call<ProjectTrackingList> call, Throwable t) {

                showAlertDialog(getAppCompatActivity().getString(R.string.error_network_title), getAppCompatActivity().getString(R.string.error_network_message));
            }
        });
    }
}
