package com.lecet.app.viewmodel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lecet.app.R;
import com.lecet.app.adapters.ProjectDetailAdapter;
import com.lecet.app.adapters.ProjectNotesAdapter;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.contentbase.BaseMapObservableViewModel;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.Bid;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.data.models.ProjectPhoto;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.interfaces.ClickableMapInterface;
import com.lecet.app.interfaces.ProjectAdditionalData;
import com.lecet.app.utility.DateUtility;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File: ProjectDetailViewModel Created: 11/9/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectDetailViewModel extends BaseMapObservableViewModel implements ClickableMapInterface, BaseMapObservableViewModel.OnBaseViewModelMapReady {

    private static final String TAG = "ProjectDetailViewModel";

    private final long projectID;
    private final String mapsApiKey;

    private final WeakReference<ProjectDetailActivity> activityWeakReference;
    private final ProjectDomain projectDomain;
    private List<ProjectAdditionalData> additionalNotes;
    private boolean mapReady;
    //private List<ProjectAdditionalData> additionalImages;

    private Project project;
    private AlertDialog networkAlertDialog;
    private ProjectDetailAdapter projectDetailAdapter;
    private ProjectNotesAdapter projectNotesAdapter;
    //private ProjectImagesAdapter projectImagesAdapter;

    // Retrofit calls
    private Call<Project> projectDetailCall;
    private Call<List<ProjectNote>> additonalNotesCall;
    private Call<List<ProjectPhoto>> additonalImagesCall;


    public ProjectDetailViewModel(ProjectDetailActivity activity, long projectID, double bidAmount, String mapsApiKey, ProjectDomain projectDomain) {

        super(activity, new GoogleMapOptions().rotateGesturesEnabled(false)
                        .rotateGesturesEnabled(false)
                        .scrollGesturesEnabled(false)
                        .tiltGesturesEnabled(false)
                        .zoomControlsEnabled(false)
                        .zoomGesturesEnabled(false),
                R.id.map_container);

        setListener(this);

        this.activityWeakReference = new WeakReference<>(activity);
        this.projectID = projectID;
        this.mapsApiKey = mapsApiKey;
        this.projectDomain = projectDomain;

        Log.d(TAG, "Constructor: projectId: " + projectID);
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

        ProjectDetailActivity activity = activityWeakReference.get();

        if (activity == null) return;

        showProgressDialog(activity.getString(R.string.updating), "");

        projectDetailCall = projectDomain.getProjectDetail(projectID, new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {

                final ProjectDetailActivity activity = activityWeakReference.get();

                if (activity == null) return;

                if (response.isSuccessful()) {

                    dismissProgressDialog();

                    Project responseProject = response.body();

                    projectDomain.asyncCopyToRealm(responseProject, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {

                            // Fetch updated project
                            project = projectDomain.fetchProjectById(projectID);

                            // Setup RecyclerView
                            initProjectDetailAdapter(activity, project);

                            // Setup paralax imageview
                            //initMapImageView(activity, getMapUrl(project));
                            if (mapReady) {
                                addMarker(R.drawable.ic_yellow_marker, project.getGeocode().toLatLng(), 16);
                            }

                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {

                            dismissProgressDialog();

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

                    dismissProgressDialog();

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

                dismissProgressDialog();

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
        details.add(new ProjDetailItemViewModel(activity.getString(R.string.stage_normal), project.getProjectStage() != null ? project.getProjectStage().getName() : ""));
        details.add(new ProjDetailItemViewModel(activity.getString(R.string.date_added), DateUtility.formatDateForDisplay(project.getFirstPublishDate())));
        details.add(new ProjDetailItemViewModel(activity.getString(R.string.bid_date), project.getBidDate() != null ? DateUtility.formatDateForDisplay(project.getBidDate()) : ""));
        details.add(new ProjDetailItemViewModel(activity.getString(R.string.last_updated), DateUtility.formatDateForDisplay(project.getLastPublishDate())));

        // Project Value
        if (project.getProjectStage() != null) {

            // The project is in Bidding/Participating Stage
            if (project.getProjectStage().getParentId() == 102) {

                // If the project is in the "Bid Results" stage, we will use the
                // EstLow value
                if (project.getProjectStage().getName() != null && project.getProjectStage().getName().equals("Bid Results")) {

                    details.add(new ProjDetailItemViewModel(activity.getString(R.string.value), String.format("$ %,.0f", project.getEstLow())));

                } else {

                    // Else we will use the average of the EstLow & EstHigh
                    double average = (project.getEstLow() + project.getEstHigh()) / 2;
                    details.add(new ProjDetailItemViewModel(activity.getString(R.string.value), String.format("$ %,.0f", average)));
                }
            }

        } else {

            // Not in Bidding/Participating Stage so we will display $0
            details.add(new ProjDetailItemViewModel(activity.getString(R.string.value), String.format("$ %,.0f", 0)));
        }

        // Remaining details
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
        RealmList<Bid> bids = getResortedBids(projectID);

        projectDetailAdapter = new ProjectDetailAdapter(activity, project, details, note, bids, contacts, new ProjectDetailHeaderViewModel(project), projectDomain);
        initLocationRecyclerView(activity, projectDetailAdapter);
        additionalNotes = new ArrayList<ProjectAdditionalData>();
        projectNotesAdapter = new ProjectNotesAdapter(additionalNotes, activity);
        initNotesRecyclerView(activity, projectNotesAdapter);
        getAdditionalNotes(false);
    }

    private RealmList<Bid> getResortedBids(long projectID) {
        RealmResults<Bid> bids = projectDomain.fetchProjectBids(projectID);
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

    private void initLocationRecyclerView(ProjectDetailActivity activity, ProjectDetailAdapter adapter) {

        //Scope Limit the Location Recycler View
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view_location_detail);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void initNotesRecyclerView(ProjectDetailActivity activity, ProjectNotesAdapter adapter) {

        //Scope Limit the Location Recycler View
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view_project_notes);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void initMapImageView(ProjectDetailActivity activity, String mapUrl) {

        ImageView imageView = (ImageView) activity.findViewById(R.id.parallax_image_view);
        Picasso.with(imageView.getContext()).load(mapUrl).fit().into(imageView);
    }

    public void getAdditionalNotes(final boolean refresh) {
        if (refresh) {
            additionalNotes.clear();
        }

        additonalNotesCall = projectDomain.fetchProjectNotes(projectID, new Callback<List<ProjectNote>>() {
            @Override
            public void onResponse(Call<List<ProjectNote>> call, Response<List<ProjectNote>> response) {
                List<ProjectNote> responseBody = response.body();

                if (responseBody != null && additionalNotes != null) {
                    additionalNotes.addAll(responseBody);
                    projectNotesAdapter.notifyDataSetChanged();
                }

                if (additionalNotes != null) {
                    Collections.sort(additionalNotes);
                    Collections.reverse(additionalNotes);
                    projectNotesAdapter.notifyDataSetChanged();
                }
                long[] ids = new long[additionalNotes.size()];
                for (int i = 0; i < additionalNotes.size(); i++) {
                    if (additionalNotes.get(i) instanceof ProjectNote) {
                        ids[i] = ((ProjectNote) additionalNotes.get(i)).getId();
                    } else {
                        ids[i] = ((ProjectPhoto) additionalNotes.get(i)).getId();
                    }
                }
                Log.d(TAG, "getAdditionalNotes: IdList: \n" + Arrays.toString(ids));
            }

            @Override
            public void onFailure(Call<List<ProjectNote>> call, Throwable t) {
                ProjectDetailActivity activity = activityWeakReference.get();

                if (activity == null) return;

                if (networkAlertDialog != null && networkAlertDialog.isShowing())
                    networkAlertDialog.dismiss();

                activity.showNetworkAlert();
            }
        });
        additonalImagesCall = projectDomain.fetchProjectImages(projectID, new Callback<List<ProjectPhoto>>() {
            @Override
            public void onResponse(Call<List<ProjectPhoto>> call, Response<List<ProjectPhoto>> response) {
                Log.d(TAG, "getAdditionalImages: onResponse");

                List<ProjectPhoto> responseBody = response.body();

                if (responseBody != null && additionalNotes != null) {
                    additionalNotes.addAll(responseBody);
                    projectNotesAdapter.notifyDataSetChanged();
                }

                if (additionalNotes != null) {
                    Collections.sort(additionalNotes);
                    Collections.reverse(additionalNotes);
                    projectNotesAdapter.notifyDataSetChanged();
                }
                long[] ids = new long[additionalNotes.size()];
                for (int i = 0; i < additionalNotes.size(); i++) {
                    if (additionalNotes.get(i) instanceof ProjectNote) {
                        ids[i] = ((ProjectNote) additionalNotes.get(i)).getId();
                    } else {
                        ids[i] = ((ProjectPhoto) additionalNotes.get(i)).getId();
                    }
                }
                Log.d(TAG, "getAdditionalNotes: IdList: \n" + Arrays.toString(ids));
            }

            @Override
            public void onFailure(Call<List<ProjectPhoto>> call, Throwable t) {
                Log.e(TAG, "getAdditionalImages: onFailure");
                ProjectDetailActivity activity = activityWeakReference.get();

                if (activity == null) return;

                if (networkAlertDialog != null && networkAlertDialog.isShowing())
                    networkAlertDialog.dismiss();

                activity.showNetworkAlert();
            }
        });


    }


    public void getAdditionalImages(final boolean refresh) {
        additonalImagesCall = projectDomain.fetchProjectImages(projectID, new Callback<List<ProjectPhoto>>() {
            @Override
            public void onResponse(Call<List<ProjectPhoto>> call, Response<List<ProjectPhoto>> response) {
                Log.d(TAG, "getAdditionalImages: onResponse");

                List<ProjectPhoto> responseBody = response.body();

                if (refresh) {
                    additionalNotes.clear();
                }

                if (responseBody != null && additionalNotes != null) {
                    additionalNotes.addAll(responseBody);
                    projectNotesAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<ProjectPhoto>> call, Throwable t) {
                Log.e(TAG, "getAdditionalImages: onFailure");
                ProjectDetailActivity activity = activityWeakReference.get();

                if (activity == null) return;

                if (networkAlertDialog != null && networkAlertDialog.isShowing())
                    networkAlertDialog.dismiss();

                activity.showNetworkAlert();
            }
        });
    }

    /**
     * MAPS
     **/

    public String getMapUrl(Project project) {

        if (project.getGeocode() == null) {

            String mapStr;

            String generatedAddress = generateCenterPointAddress(project);

            StringBuilder sb2 = new StringBuilder();
            sb2.append("https://maps.googleapis.com/maps/api/staticmap");
            sb2.append("?center=");
            sb2.append(generatedAddress);
            sb2.append("&zoom=16");
            sb2.append("&size=200x200");
            sb2.append("&markers=color:blue|");
            sb2.append(generatedAddress);
            sb2.append("&key=" + mapsApiKey);
            mapStr = String.format((sb2.toString().replace(' ', '+')), null);

            return mapStr;
        }

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

    private String generateCenterPointAddress(Project project) {

        StringBuilder stringBuilder = new StringBuilder();

        if (project.getAddress1() != null) {
            stringBuilder.append(project.getAddress1());
            stringBuilder.append(",");
        }

        if (project.getAddress2() != null) {
            stringBuilder.append(project.getAddress2());
            stringBuilder.append(",");
        }

        if (project.getCity() != null) {
            stringBuilder.append(project.getCity());
            stringBuilder.append(",");
        }

        if (project.getState() != null) {
            stringBuilder.append(project.getState());
        }

        if (project.getZipPlus4() != null) {
            stringBuilder.append(",");
            stringBuilder.append(project.getZipPlus4());
        }


        return stringBuilder.toString();
    }

    @Override
    public void onMapSetup(GoogleMap googleMap) {

        mapReady = true;
    }
}
