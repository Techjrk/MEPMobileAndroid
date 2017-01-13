package com.lecet.app.viewmodel;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.lecet.app.R;
import com.lecet.app.adapters.ProjectDetailAdapter;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.ProjectDomain;
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

public class ProjectDetailViewModel {

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

                ProjectDetailActivity activity = activityWeakReference.get();

                if (activity == null) return;

                if (response.isSuccessful()) {

                    Project responseProject = response.body();
                    projectDomain.copyToRealmTransaction(responseProject);

                    // Fetch updated project
                    project = projectDomain.fetchProjectById(projectID);

                    // Setup RecyclerView
                    initProjectDetailAdapter(activity, project);

                    // Setup paralax imageview
                    initMapImageView(activity, getMapUrl(project));

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

        projectDetailAdapter = new ProjectDetailAdapter(activity, project, buildDetails(project, activity), new ProjectDetailHeaderViewModel(project), projectDomain);
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

    private List<List<ProjDetailItemViewModel>> buildDetails(Project project, Context context) {

        List<List<ProjDetailItemViewModel>> data = new ArrayList<>();

        // First section will only have a header
        List<ProjDetailItemViewModel> section0 = new ArrayList<>();
        data.add(section0);

        // Build Project Details
        List<ProjDetailItemViewModel> section1 = new ArrayList<>();
        data.add(section1);

        section1.add(new ProjDetailItemViewModel(context.getString(R.string.county), project.getCounty()));
        section1.add(new ProjDetailItemViewModel(context.getString(R.string.project_id), project.getDodgeNumber()));
        section1.add(new ProjDetailItemViewModel(context.getString(R.string.address), project.getFullAddress()));
        section1.add(new ProjDetailItemViewModel(context.getString(R.string.project_type), project.getProjectTypes()));
        section1.add(new ProjDetailItemViewModel(context.getString(R.string.est_low), String.format("$ %,.0f", project.getEstLow())));

        //TODO: Handle view all and data set toggling
        section1.add(new ProjDetailItemViewModel(context.getString(R.string.est_high), String.format("$ %,.0f", project.getEstHigh())));
        section1.add(new ProjDetailItemViewModel(context.getString(R.string.stage), project.getProjectStage().getName()));
        section1.add(new ProjDetailItemViewModel(context.getString(R.string.date_added), DateUtility.formatDateForDisplay(project.getFirstPublishDate())));
        section1.add(new ProjDetailItemViewModel(context.getString(R.string.bid_date), DateUtility.formatDateForDisplay(project.getBidDate())));
        section1.add(new ProjDetailItemViewModel(context.getString(R.string.last_updated), DateUtility.formatDateForDisplay(project.getLastPublishDate())));
        section1.add(new ProjDetailItemViewModel(context.getString(R.string.value), "$ 0"));
        section1.add(new ProjectDetailJurisdictionViewModel(new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(context), Realm.getDefaultInstance()), projectID, context.getString(R.string.jurisdiction)));
        section1.add(new ProjDetailItemViewModel(context.getString(R.string.b_h), project.getPrimaryProjectType().getBuildingOrHighway()));

        // Notes
        List<ProjDetailItemViewModel> section2 = new ArrayList<>();
        data.add(section2);

        String notes = "";

        if (project.getProjectNotes() != null) notes = project.getProjectNotes();
        if (project.getStdIncludes() != null) notes = notes + " " + project.getStdIncludes();

        section2.add(new ProjDetailItemViewModel(null, notes));

        // Participants
        if (project.getContacts().size() > 0) {

            List<ProjDetailItemViewModel> section3 = new ArrayList<>();
            data.add(section3);

            RealmResults<Contact> contacts = projectDomain.fetchProjectContacts(projectID);
            for (Contact contact : contacts) {

                String contactType = contact.getContactType().getCategory();
                String detail = contact.getCompany().getName() + " \n " + contact.getCompany().getCity() + ", " + contact.getCompany().getState();

                section3.add(new ProjDetailItemViewModel(contactType, detail));
            }
        }

        // Bidders
        if (project.getBids().size() > 0) {

            List<ProjDetailItemViewModel> section4 = new ArrayList<>();
            data.add(section4);

            RealmResults<Bid> bids = projectDomain.fetchProjectBids(projectID);
            // Show only three max
            for (int i=0; i < 3; i++) {

                Bid bid = bids.get(i);

                Company company = bid.getCompany();
                if (company != null) {
                    String detail = company.getName() + " \n " + company.getCity() + ", " + company.getState();
                    String price = String.format("$ %,.0f", bid.getAmount());

                    section4.add(new ProjDetailItemViewModel(price, detail));
                }
            }

        }

        return data;
    }

}
