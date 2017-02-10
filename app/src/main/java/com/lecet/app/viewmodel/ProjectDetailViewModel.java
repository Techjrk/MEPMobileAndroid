package com.lecet.app.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.R;
import com.lecet.app.adapters.ProjectDetailAdapter;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.interfaces.ClickableMapInterface;
import com.lecet.app.utility.DateUtility;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: ProjectDetailViewModel Created: 11/9/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectDetailViewModel implements ClickableMapInterface {

    private static final String TAG = "ProjectDetailViewModel";

    private final long projectID;
    private final String mapsApiKey;

    private final WeakReference<ProjectDetailActivity> activityWeakReference;
    private final ProjectDomain projectDomain;

    private Project project;
    private AlertDialog networkAlertDialog;
    private ProjectDetailAdapter projectDetailAdapter;

    // Retrofit calls
    private Call<Project> projectDetailCall;

    public ProjectDetailViewModel(ProjectDetailActivity activity, long projectID, String mapsApiKey, ProjectDomain projectDomain) {

        this.activityWeakReference = new WeakReference<>(activity);
        this.projectID = projectID;
        this.mapsApiKey = mapsApiKey;
        this.projectDomain = projectDomain;
    }

    /**
     * Network
     **/

    public void cancelGetProjectDetailRequest() {

        if (projectDetailCall != null && !projectDetailCall.isCanceled()) {

            projectDetailCall.cancel();
        }
    }


    public void getProjectDetail() {

        getProjectDetail(projectID);
    }


    private void getProjectDetail(final long projectID) {

        projectDetailCall = projectDomain.getProjectDetail(projectID, new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {

                final ProjectDetailActivity activity = activityWeakReference.get();

                if (activity == null) return;

                if (response.isSuccessful()) {

                    Project responseProject = response.body();

                    projectDomain.asyncCopyToRealm(responseProject, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {

                            // Fetch updated project
                            project = projectDomain.fetchProjectById(projectID);

                            // Setup RecyclerView
                            initProjectDetailAdapter(activity, project);

                            // Setup paralax imageview
                            initMapImageView(activity, getMapUrl(project));

                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {

                            activity.hideNetworkAlert();

                            if (networkAlertDialog != null && networkAlertDialog.isShowing())
                                networkAlertDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle(activity.getString(R.string.error_network_title));
                            builder.setMessage(error.getMessage());
                            builder.setNegativeButton(activity.getString(R.string.ok), null);

                            networkAlertDialog = builder.show();
                        }
                    });



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
            public void onFailure(Call<Project> call, Throwable t) {

                ProjectDetailActivity activity = activityWeakReference.get();

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
    private void initProjectDetailAdapter(ProjectDetailActivity activity, Project project) {

        // Build Project Details
        List<ProjDetailItemViewModel> details = new ArrayList<>();

        details.add(new ProjDetailItemViewModel(activity.getString(R.string.county), project.getCounty()));
        details.add(new ProjDetailItemViewModel(activity.getString(R.string.project_id), project.getDodgeNumber()));
        details.add(new ProjDetailItemViewModel(activity.getString(R.string.address), project.getFullAddress()));
        details.add(new ProjDetailItemViewModel(activity.getString(R.string.project_type), project.getProjectTypes()));
        details.add(new ProjDetailItemViewModel(activity.getString(R.string.est_low), String.format("$ %,.0f", project.getEstLow())));
        details.add(new ProjDetailItemViewModel(activity.getString(R.string.est_high), String.format("$ %,.0f", project.getEstHigh())));
        details.add(new ProjDetailItemViewModel(activity.getString(R.string.stage), project.getProjectStage().getName()));
        details.add(new ProjDetailItemViewModel(activity.getString(R.string.date_added), DateUtility.formatDateForDisplay(project.getFirstPublishDate())));
        details.add(new ProjDetailItemViewModel(activity.getString(R.string.bid_date), project.getBidDate() != null ? DateUtility.formatDateForDisplay(project.getBidDate()) : ""));
        details.add(new ProjDetailItemViewModel(activity.getString(R.string.last_updated), DateUtility.formatDateForDisplay(project.getLastPublishDate())));
        details.add(new ProjDetailItemViewModel(activity.getString(R.string.value), "$ 0"));
        details.add(new ProjectDetailJurisdictionViewModel(new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(activity), Realm.getDefaultInstance()), projectID, activity.getString(R.string.jurisdiction)));
        details.add(new ProjDetailItemViewModel(activity.getString(R.string.b_h), project.getPrimaryProjectType().getBuildingOrHighway()));

        // Notes
        String notes = null;

        if (project.getProjectNotes() != null) notes = project.getProjectNotes();
        if (project.getStdIncludes() != null) notes = notes + " " + project.getStdIncludes();

        ProjDetailItemViewModel note = null;

        if (notes != null) {
            note = new ProjDetailItemViewModel(null, notes);
        }

        // Participants
        RealmResults<Contact> contacts = projectDomain.fetchProjectContacts(projectID);

        // Bidders
        RealmResults<Bid> bids = projectDomain.fetchProjectBids(projectID);


        projectDetailAdapter = new ProjectDetailAdapter(activity, project, details, note, bids, contacts, new ProjectDetailHeaderViewModel(project), projectDomain);
        initRecyclerView(activity, projectDetailAdapter);
    }


    private void initRecyclerView(ProjectDetailActivity activity, ProjectDetailAdapter adapter) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    private void initMapImageView(ProjectDetailActivity activity, String mapUrl) {

        ImageView imageView = (ImageView) activity.findViewById(R.id.parallax_image_view);
        Picasso.with(imageView.getContext()).load(mapUrl).fit().into(imageView);
    }


    public String getMapUrl(Project project) {

        if (project.getGeocode() == null) return null;

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=800x500&" +
                        "markers=color:blue|%.6f,%.6f&key=%s", project.getGeocode().getLat(), project.getGeocode().getLng(),
                project.getGeocode().getLat(), project.getGeocode().getLng(), mapsApiKey);
    }

    @Override
    public void onMapSelected(View view) {

        if (project.getGeocode() == null) return;

        ProjectDetailActivity activity = activityWeakReference.get();

        Uri gmmIntentUri = Uri.parse(String.format("geo:0,0?q=%f,%f", project.getGeocode().getLat(), project.getGeocode().getLng()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(mapIntent);
        }
    }
}
