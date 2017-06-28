package com.lecet.app.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.adapters.ProjectDetailAdapter;
import com.lecet.app.adapters.ProjectNotesAdapter;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.content.ProjectDetailFragment;
import com.lecet.app.contentbase.BaseObservableViewModel;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.interfaces.ProjectAdditionalData;
import com.lecet.app.utility.DateUtility;

import java.util.ArrayList;
import java.util.IllegalFormatConversionException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jasonm on 3/9/17.
 */

public class ProjectDetailFragmentViewModel extends BaseObservableViewModel {

    private final Fragment fragment;

    private View parent;
    private long projectId;

    private ProjectDomain projectDomain;
    private ProjectDetailAdapter projectDetailAdapter;
    ProjectDetailFragment.ProjectDetailFragmentListener listener;

    private Call<Project> projectDetailCall;


    public ProjectDetailFragmentViewModel(Fragment fragment, long projectId, ProjectDomain projectDomain,
                                          ProjectDetailFragment.ProjectDetailFragmentListener listener) {
        super((AppCompatActivity) fragment.getActivity());
        this.fragment = fragment;
        this.projectId = projectId;
        this.projectDomain = projectDomain;
        this.listener = listener;
    }

    public void onCreateView(final View parent) {

        this.parent = parent;
    }

    public void onResume() {

        getProjectDetail();
    }

    public void onPause() {

        if (projectDetailCall != null && !projectDetailCall.isCanceled()) {
            projectDetailCall.cancel();
        }
    }

    /*
     * Networking
     */

    public void getProjectDetail() {

        getProjectDetail(parent, projectId);
    }


    private void getProjectDetail(final View parent, final long projectID) {

        AppCompatActivity activity = getActivityWeakReference().get();

        if (!isActivityAlive()) return;

        showProgressDialog();

        projectDetailCall = projectDomain.getProjectDetail(projectID, new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {

                final AppCompatActivity activity = getActivityWeakReference().get();

                if (!isActivityAlive()) return;

                if (response.isSuccessful()) {

                    dismissProgressDialog();

                    Project responseProject = response.body();

                    projectDomain.asyncCopyToRealm(responseProject, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {

                            // Fetch updated project
                            Project project = projectDomain.fetchProjectById(projectID);

                            // Setup RecyclerView
                            initProjectDetailAdapter(parent, project, projectDomain);

                            listener.onProjectDetailsReceived(project);

                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {

                            dismissProgressDialog();

                            showCancelAlertDialog(activity.getString(R.string.error_network_title),
                                    error.getMessage());
                        }
                    });


                } else {

                    dismissProgressDialog();

                    showCancelAlertDialog(activity.getString(R.string.error_network_title),
                            response.message());
                }
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {

                if (!isActivityAlive()) return;

                dismissProgressDialog();

                final AppCompatActivity activity = getActivityWeakReference().get();
                showCancelAlertDialog(activity.getString(R.string.error_network_title),
                        activity.getString(R.string.error_network_message));
            }
        });
    }

    /*
     * View management
     */
    private void initProjectDetailAdapter(View parent, Project project, ProjectDomain projectDomain) {

        Context context = parent.getContext();

        // Build Project Details
        List<ProjDetailItemViewModel> details = new ArrayList<>();

        details.add(new ProjDetailItemViewModel(context.getString(R.string.county), project.getCounty()));
        details.add(new ProjDetailItemViewModel(context.getString(R.string.project_ids), project.getDodgeNumber()));
        details.add(new ProjDetailItemViewModel(context.getString(R.string.address), project.getFullAddress()));
        details.add(new ProjDetailItemViewModel(context.getString(R.string.project_type), project.getProjectTypes()));

        // We will no longer display EstLow and EstHigh and simply display valuation
        if (project.getEstLow() > 0) {

            details.add(new ProjDetailItemViewModel(context.getString(R.string.valuation), String.format("$ %,.0f", project.getEstLow())));
        }

        details.add(new ProjDetailItemViewModel(context.getString(R.string.stage_normal), project.getProjectStage() != null ? project.getProjectStage().getName() : ""));
        details.add(new ProjDetailItemViewModel(context.getString(R.string.date_added), DateUtility.formatDateForDisplay(project.getFirstPublishDate())));
        details.add(new ProjDetailItemViewModel(context.getString(R.string.bid_date), project.getBidDate() != null ? DateUtility.formatDateForDisplay(project.getBidDate()) : ""));
        details.add(new ProjDetailItemViewModel(context.getString(R.string.last_updated), DateUtility.formatDateForDisplay(project.getLastPublishDate())));

        // Project Value
        if (project.getProjectStage() != null) {

            // The project is in Bidding/Participating Stage
            if (project.getProjectStage().getParentId() == 102) {

                // If the project is in the "Bid Results" stage, we will use the
                // EstLow value
                if (project.getProjectStage().getName() != null && project.getProjectStage().getName().equals("Bid Results")) {

                    details.add(new ProjDetailItemViewModel(context.getString(R.string.value), String.format("$ %,.0f", project.getEstLow())));

                } else {

                    // Else we will use the average of the EstLow & EstHigh
                    double average = (project.getEstLow() + project.getEstHigh()) / 2;
                    details.add(new ProjDetailItemViewModel(context.getString(R.string.value), String.format("$ %,.0f", average)));
                }
            }

        } else {

            // Not in Bidding/Participating Stage so we will display $0
            String str = "0";
            try {
                str = String.format("$ %,.0f", 0);
            } catch (IllegalFormatConversionException e) {
                Log.e("ProjectDetailFragmentVM", "initProjectDetailAdapter: FORMAT CONVERSION ERROR");
            }
            details.add(new ProjDetailItemViewModel(context.getString(R.string.value), str));
        }
        // Remaining details
        details.add(new ProjectDetailJurisdictionViewModel(new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(context),
                Realm.getDefaultInstance()), project.getId(), context.getString(R.string.jurisdiction)));
        if(project.getPrimaryProjectType() != null) {
            details.add(new ProjDetailItemViewModel(context.getString(R.string.b_h), project.getPrimaryProjectType().getBuildingOrHighway()));
        }

        // Notes
        String notes = null;

        if (project.getProjectNotes() != null) notes = project.getProjectNotes();
        if (project.getStdIncludes() != null) notes = notes + " " + project.getStdIncludes();

        ProjDetailItemViewModel note = null;

        if (notes != null) {
            note = new ProjDetailItemViewModel(null, notes);
        }

        // Participants
        RealmResults<Contact> contacts = projectDomain.fetchProjectContacts(project.getId());

        // Bidders
        RealmList<Bid> bids = getResortedBids(project.getId(), projectDomain);

        projectDetailAdapter = new ProjectDetailAdapter((AppCompatActivity)fragment.getActivity(), project, details, note, bids, contacts, new ProjectDetailHeaderViewModel(project), projectDomain);
        initRecyclerView(parent, projectDetailAdapter);
    }

    private RealmList<Bid> getResortedBids(long projectId, ProjectDomain projectDomain) {
        RealmResults<Bid> bids = projectDomain.fetchProjectBids(projectId);
        RealmList<Bid> bidsCopy = new RealmList<>();
        RealmList<Bid> resortedBids = new RealmList<>();
        bidsCopy.addAll(bids);

        // add sorted Bids with value more than zero first
        for (Bid bid : bidsCopy) {
            if (bid.getAmount() > 0) {
                resortedBids.add(bid);
            }
        }

        // add Bids with value of 0 last
        for (Bid bid : bidsCopy) {
            if (bid.getAmount() == 0) {
                resortedBids.add(bid);
            }
        }

        return resortedBids;
    }

    private void initRecyclerView(View parent, ProjectDetailAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(parent.getContext());

        RecyclerView recyclerView = (RecyclerView) parent.findViewById(R.id.recycler_view_location_detail);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}

