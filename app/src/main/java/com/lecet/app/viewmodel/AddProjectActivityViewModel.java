package com.lecet.app.viewmodel;

import com.google.android.gms.maps.model.LatLng;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.Bindable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.SearchFilterStageSingleSelectAdapter;
import com.lecet.app.content.AddProjectActivity;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.content.SearchFilterCountyActivity;
import com.lecet.app.content.SearchFilterProjectTypeActivity;
import com.lecet.app.content.SearchFilterStageActivity;
import com.lecet.app.contentbase.BaseObservableViewModel;
import com.lecet.app.data.api.request.GeocodeRequest;
import com.lecet.app.data.models.County;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectPost;
import com.lecet.app.data.models.SearchFilterStage;
import com.lecet.app.data.models.SearchFilterStagesMain;
import com.lecet.app.data.models.geocoding.AddressComponent;
import com.lecet.app.data.models.geocoding.GeocodeAddress;
import com.lecet.app.data.models.geocoding.GeocodeResult;
import com.lecet.app.domain.LocationDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.interfaces.ClickableMapInterface;
import com.lecet.app.utility.Log;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.lecet.app.R.string.google_api_key;

/**
 * Created by jasonm on 5/15/17.
 */

public class AddProjectActivityViewModel extends BaseObservableViewModel implements ClickableMapInterface {

    private static final String TAG = "AddProjectActivityVM";

    private AppCompatActivity activity;
    private final String mapsApiKey;
    private ProjectDomain projectDomain;
    private LocationDomain locationDomain;
    private AlertDialog alert;

    // values for posting to API or which can be displayed as native value
    private ProjectPost projectPost;
    private double latitude;
    private double longitude;

    // edit
    private long projectId;
    private Project project;

    private String targetStartDate;
    private boolean countyIsEditable;

    // values for display only
    private String typeSelect;
    private String stageSelect;

    private Calendar calendar;
    private String stageName = "", stageType = "";
    public AddProjectActivityViewModel(AppCompatActivity appCompatActivity, double latitude, double longitude, long projectId, ProjectDomain projectDomain, LocationDomain locationDomain) {
        super(appCompatActivity);

        Log.d(TAG, "Constructor: latitude: " + latitude);
        Log.d(TAG, "Constructor: longitude: " + longitude);

        this.activity = appCompatActivity;
        this.projectDomain = projectDomain;
        this.locationDomain = locationDomain;
        this.latitude = latitude;
        this.longitude = longitude;
        this.projectId = projectId;

        mapsApiKey = activity.getResources().getString(google_api_key);

        // create the new projectPost obj
        projectPost = new ProjectPost(latitude, longitude);
        // if creating a new project
        if (!isEditMode()) {

            //TODO: Is this always null? Seems so
            // add lat and long in the form of Geocode obj if they have been passed
            if (projectPost.getGeocode() == null) {
                GeocodeRequest geocode = new GeocodeRequest();
                geocode.setLat(latitude);
                geocode.setLng(longitude);
                projectPost.setGeocode(geocode);
            }

            getAddressFromLocation(latitude, longitude);
        }
        // if editing an existing project via a passed projectId
        else {
            Log.d(TAG, "AddProjectActivityViewModel: EDITING PROJECT: " + this.projectId);
            getEditableProject(this.projectId);
        }

        if (projectPost != null)
            initMapImageView((AddProjectActivity) appCompatActivity, getMapUrl(projectPost));
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Log.d(TAG, "getAddressFromLocation: lat, lng: " + latitude + ", " + longitude);

        Call<GeocodeAddress> call = locationDomain.getAddressFromLocation(latitude, longitude, "street_address", mapsApiKey);

        call.enqueue(new Callback<GeocodeAddress>() {
            @Override
            public void onResponse(Call<GeocodeAddress> call, Response<GeocodeAddress> response) {
                //Log.d(TAG, "getAddressFromLocation: onResponse: success? " + response.isSuccessful());
                //Log.d(TAG, "getAddressFromLocation: onResponse: response: " + response);
                Log.d(TAG, "getAddressFromLocation: onResponse: response.body: " + response.body());

                if (response.isSuccessful()) {
                    GeocodeAddress geocodeAddress = response.body();
                    if (geocodeAddress != null && geocodeAddress.getResults().size() > 0) {
                        GeocodeResult firstResult = geocodeAddress.getResults().get(0);
                        if (firstResult != null) {
                            String formattedAddress = firstResult.getFormattedAddress();    // the one-line formatted address for display
                            Log.d(TAG, "getAddressFromLocation: onResponse: address request successful. formattedAddress: " + formattedAddress);
                            List<AddressComponent> addressComponents = firstResult.getAddressComponents();
                            if (addressComponents != null && !addressComponents.isEmpty()) {
                                populateFieldsFromAddress(addressComponents);
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "getAddressFromLocation: onResponse: get address failed");
                }
            }

            @Override
            public void onFailure(Call<GeocodeAddress> call, Throwable t) {
                Log.e(TAG, "getAddressFromLocation: onFailure: get address failed");
            }
        });
    }

    /**
     * Maps nodes from a Google Maps response into the address vars we need
     */
    private void populateFieldsFromAddress(List<AddressComponent> address) {

        String streetNum = null;
        String addr1 = null;
        String addr2 = null;
        String neigb = null;
        String locality = null;
        String city = null;
        String county = null;
        String state = null;
        String country = null;
        String zip5 = null;

        //TODO - this loop could be optimized. Please retain formatting for readability.
        for (AddressComponent component : address) {
            if (component.getTypes().contains("street_number"))               streetNum = component.getShortName();
            if (component.getTypes().contains("route"))                       addr1 = component.getShortName();
            if (component.getTypes().contains("locality"))                    locality = component.getShortName();
            if (component.getTypes().contains("neighborhood"))                neigb = component.getShortName();
            if (component.getTypes().contains("sublocality_level_1"))         city = component.getShortName();
            if (component.getTypes().contains("administrative_area_level_2")) county = component.getShortName();
            if (component.getTypes().contains("administrative_area_level_1")) state = component.getShortName();
            if (component.getTypes().contains("country"))                     country = component.getShortName();
            if (component.getTypes().contains("postal_code"))                 zip5 = component.getShortName();
        }

        String fipsCounty = getFipsCounty(state, county);

        String streetAddr = "";
        if (streetNum != null & !streetNum.isEmpty()) streetAddr += streetNum;
        if (addr1 != null && !addr1.isEmpty()) streetAddr += (" " + addr1);

        if (streetAddr != null)  getProjectPost().setAddress1(streetAddr);   // address line 1
        //if(addr2 != null)      getProjectPost().setAddress2(addr2);        // address line 2, prob not avail from the response (like Apt / Floor#)
        if (city != null)        getProjectPost().setCity(city);             // city
        if (state != null)       getProjectPost().setState(state);           // state
        if (zip5 != null)        getProjectPost().setZip5(zip5);             // zip
        if (county != null)      getProjectPost().setCounty(county);         // county
        if (fipsCounty != null)  getProjectPost().setFipsCounty(fipsCounty); // FIPS county
        if (country != null)     getProjectPost().setCountry(country);       // country

        // use locality as alternative to city if sublocality_level_1 is null
        if (city == null && locality != null) getProjectPost().setCity(locality);

        // set the edit mode of the County field
        if (county != null && !county.isEmpty()) {
            countyIsEditable = false;
        } else countyIsEditable = true;
    }

    private String getFipsCounty(final String projectState, final String projectCounty) {
        Log.d(TAG, "getFipsCounty: " + projectCounty);
        if (projectState == null || projectCounty == null) return null;

        final String[] fipsCounty = new String[1];

        try {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<County> realmCounties = realm.where(County.class).findAll();
                    Log.d(TAG, "getFipsCounty: realmCounties size: " + realmCounties.size());
                    if (realmCounties != null) {
                        for (County realmCounty : realmCounties) {
                            if (realmCounty.getState().equals(projectState)) {
                                if (projectCounty.toUpperCase().contains(realmCounty.getCountyName())) {
                                    fipsCounty[0] = realmCounty.getFipsCountyId();
                                    Log.d(TAG, "getFipsCounty: found match: project county:" + projectCounty.toUpperCase() + ", realm county:" + realmCounty.getCountyName() + ", fipsCounty:" + fipsCounty[0]);
                                }
                            }
                        }
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "getFipsCounty: EXCEPTION: " + e);
        }

        return fipsCounty[0];   // could be valid string or null
    }


    public String getMapUrl(ProjectPost projectPost) {

        if (projectPost.getGeocode() == null) {

            String mapStr;

            String generatedAddress = generateCenterPointAddress(projectPost);

            StringBuilder sb2 = new StringBuilder();
            sb2.append("https://maps.googleapis.com/maps/api/staticmap");
            sb2.append("?center=");
            sb2.append(generatedAddress);
            sb2.append("&zoom=16");
            sb2.append("&size=200x200");
            sb2.append("&markers=color:blue|");
            sb2.append(generatedAddress);
            sb2.append("&key=" + mapsApiKey);

            mapStr = String.format((sb2.toString().replace(' ', '+')), (Object) null);

            return mapStr;
        }

        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=16&size=800x500&" +
                        "markers=color:blue|%.6f,%.6f&key=%s", projectPost.getGeocode().getLat(), projectPost.getGeocode().getLng(),
                projectPost.getGeocode().getLat(), projectPost.getGeocode().getLng(), mapsApiKey);
    }

    private void initMapImageView(AddProjectActivity activity, String mapUrl) {

        ImageView imageView = (ImageView) activity.findViewById(R.id.parallax_image_view);
        Picasso.with(imageView.getContext()).load(mapUrl).fit().into(imageView);
    }

    private String generateCenterPointAddress(ProjectPost projectPost) {

        StringBuilder stringBuilder = new StringBuilder();

        if (projectPost.getAddress1() != null) {
            stringBuilder.append(projectPost.getAddress1());
            stringBuilder.append(",");
        }

        if (projectPost.getAddress2() != null) {
            stringBuilder.append(projectPost.getAddress2());
            stringBuilder.append(",");
        }

        if (projectPost.getCity() != null) {
            stringBuilder.append(projectPost.getCity());
            stringBuilder.append(",");
        }

        if (projectPost.getState() != null) {
            stringBuilder.append(projectPost.getState());
        }

        if (projectPost.getZipPlus4() != null) {
            stringBuilder.append(",");
            stringBuilder.append(projectPost.getZipPlus4());
        }


        return stringBuilder.toString();
    }

    private void getEditableProject(long projectId) {

        project = projectDomain.fetchProjectById(projectId);

        if (project != null) {
            Log.d(TAG, "getEditableProject: FOUND PROJECT: " + project);
            projectPost.setGeocode(new GeocodeRequest(project.getGeocode().getLat(), project.getGeocode().getLng()));
            projectPost.setTitle(project.getTitle());
            projectPost.setAddress1(project.getAddress1());
            projectPost.setAddress2(project.getAddress2());
            projectPost.setCity(project.getCity());
            projectPost.setState(project.getState());
            projectPost.setZip5(project.getZip5());
            projectPost.setCounty(project.getCounty());
            projectPost.setFipsCounty(project.getFipsCounty());
            projectPost.setCountry(project.getCountry());
            if (project.getProjectStageId() != null) projectPost.setProjectStageId(Integer.parseInt(project.getProjectStageId()));
            if (project.getBidDate() != null) projectPost.setBidDate(project.getBidDate().toString());
            projectPost.setPrimaryProjectTypeId(project.getPrimaryProjectTypeId());
            projectPost.setEstLow(project.getEstLow());
            if (project.getTargetStartDate() != null) {
                projectPost.setTargetStartDate(new SimpleDateFormat("MM/dd/yy").format(project.getTargetStartDate()));
                setTargetStartDate(projectPost.getTargetStartDate());
            }

            // special cases for display purposes of Type and Stage etc
            //Note: Since the rule in selecting the project type in the custom project is just a single item, and the parent items are not allowed,
            // the code below is no longer applicable
            //            if (project.getProjectTypes() != null) setTypeSelect(project.getProjectTypes());
            //            if (project.getProjectStage() != null && project.getProjectStage().getName() != null)
            //                setStageSelect(project.getProjectStage().getName());

            // Note: With the the stageId from the project, the code below is used for bringing back the previous stage item selected by the user and
            // put the stage value to respected stage field and also to
            // save the extracted stage data to the sharedPreferences to be used later by the Stage Filter section to set the checkbox selection for this extracted stage data.
            //  if (getStageSelect() == null || getTypeSelect().isEmpty()) //* commented this to check if this is no longer needed.
            if (project.getProjectStageId() != null) searchBackStageName(String.valueOf(project.getProjectStageId()));

            // Note: With the the primaryProjectTypeId from the project, the code below is used for bringing back the previous project type item selected by the user and
            // put the project type value to respected project type field and also to save
            // the extracted project type data to the sharedPreferences to be used later by the Project Type Filter section to set the checkbox selection for this extracted project type data.
            //    if (getTypeSelect() == null || getTypeSelect().isEmpty()) //* commented this to check if this is no longer needed.
            searchBackProjectTypeName(String.valueOf(project.getPrimaryProjectTypeId()));

        }  //if project !=null
    }

    private void postProject() {
        SearchFilterAllTabbedViewModel.userCreated=true;
        Log.d(TAG, "postProject: Project Post: " + projectPost);
        if(TextUtils.isEmpty(projectPost.getCounty()) || TextUtils.isEmpty(projectPost.getFipsCounty())){
            showCancelAlertDialog("",activity.getResources().getString(R.string.county_not_set_message));
            return;
        }
        Call<Project> call;
        Calendar c = Calendar.getInstance();
        Log.d(TAG,"postProject: Current time => " + c.getTime());
        /** Getting the date today for firstPublishDate for Recently Project Added and
         * for lastPublishDat for Recently Project Updated
         * Note: The date format should be in UTC and in the format of "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ"
         */
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");
        //new SimpleDateFormat("MM/dd/yy"); // new SimpleDateFormat("dd-MMM-yyyy");
        String today = df.format(c.getTime());

        if(isEditMode()){

            if (projectPost.getAddress1() != project.getAddress1() ||
                projectPost.getState() != project.getState() ||
                projectPost.getCity() != project.getCity() ||
                projectPost.getAddress2() != project.getAddress2()) {

//              Give the projectPost a new Lat and lng that accurately tracks its location.
//              resetLngAndLat();
            }
            //Note:
            //The saved edited project cannot be viewed immediately from the Recently Updated screen according to Harry.
            //He said that when he discussed this with Devo, there's still some processing on the web side on the dodge system or something.
            //Anyways, This code below is used for displaying the custom project in the RecentlyUpdated view.
            projectPost.setLastPublishDate(today);
            projectPost.setPriorPublishDate(today); //This is just in case it's needed.
            call = projectDomain.updateProject(projectId, projectPost);

        } else {
            /**Note:
             * This is used for displaying the custom project in the RecentlyAdded view.
             * This could be view immediately from the Recently Added screen.
             * */
            projectPost.setFirstPublishDate(today);
            call = projectDomain.postProject(projectPost);
        }

        call.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {

                if (response.isSuccessful()) {
                    Project createdProject = response.body();
                    //TODO: Save returned project to realm
                    Log.d(TAG, "postProject: onResponse: projectPost post successful. Created project: " + createdProject);

                    // view the project in Project Detail
                    if (createdProject != null && createdProject.getId() > 0) {
                        if(!isEditMode()){
                            Intent intent = new Intent(activity, ProjectDetailActivity.class);
                            intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, createdProject.getId());
                            activity.startActivity(intent);

                        }
                    }
                    activity.setResult(RESULT_OK);
                    activity.finish();
                } else {
                    Log.e(TAG, "postProject: onResponse: projectPost post failed");
                    showPostProjectFailedDialog();
                }
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {
                Log.e(TAG, "postProject: onFailure: projectPost post failed"+t.getMessage());
                showPostProjectFailedDialog();
            }

        });

    }

    private void showPostProjectAlertDialog(View view, DialogInterface.OnClickListener onClick) {

        alert = new AlertDialog.Builder(view.getContext()).create();

        String message = null;
        String confirmButtonText;

        // Alert if any required fields are not filled in
        if (projectPost.getGeocode() == null)
            message = activity.getString(R.string.save_project_location_required);

        else if (projectPost.getTitle() == null || projectPost.getTitle().isEmpty())
            message = activity.getString(R.string.save_project_title_required);

        else if (projectPost.getAddress1() == null || projectPost.getAddress1().isEmpty())
            message = activity.getString(R.string.save_project_address_required);

        else if (projectPost.getCity() == null || projectPost.getCity().isEmpty())
            message = activity.getString(R.string.save_project_city_required);

        else if (projectPost.getState() == null || projectPost.getState().isEmpty())
            message = activity.getString(R.string.save_project_state_required);

        // Required content of project post
        if (message != null) {
            alert.setButton(DialogInterface.BUTTON_NEUTRAL, activity.getString(android.R.string.ok), onClick);
            alert.setMessage(message);
            alert.show();
        }

        // Confirmation, if all fields are correct
        else {
            if (!isEditMode()) {
                message = activity.getString(R.string.save_project_confirm);
                confirmButtonText = activity.getString(R.string.save_project);
            } else {
                message = activity.getString(R.string.update_project_confirm);
                confirmButtonText = activity.getString(R.string.update_project);
            }

            alert.setMessage(message);
            alert.setButton(DialogInterface.BUTTON_POSITIVE, confirmButtonText, onClick);
            alert.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getString(android.R.string.cancel), onClick);
            alert.show();
        }
    }

    private void showPostProjectFailedDialog() {
        String title = activity.getString(R.string.error_project_update_failed_title);
        String message = activity.getString(R.string.error_project_update_failed);
        showCancelAlertDialog(title, message);
    }


    /*
     * Clicks 
     */
    public void onClicked(View view) {
        Log.d(TAG, "onClicked: " + view.getContext().getResources().getResourceEntryName(view.getId()));
        Intent i = null;
        int id = view.getId();
        int section = 0;
        //Note: Setting the userCreated to true to let the search functionality that we are going to use only a single-item selection for Project Type and Stage.
        SearchFilterAllTabbedViewModel.userCreated = true;

        switch (id) {
            case R.id.add_project_type:
                section = SearchFilterAllTabbedViewModel.TYPE;
                i = new Intent(activity, SearchFilterProjectTypeActivity.class);
                break;
            case R.id.stage:
                section = SearchFilterAllTabbedViewModel.STAGE;
                i = new Intent(activity, SearchFilterStageActivity.class);
                break;
            case R.id.target_set_date:
                calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                };
                new DatePickerDialog(activity, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.county:
                i = new Intent(activity , SearchFilterCountyActivity.class);
                i.putExtra(SearchFilterCountyActivity.REQUEST_STATE_EXTRA , projectPost.getState());
                i.putExtra(SearchFilterCountyActivity.REQUEST_FIPSCOUNTY_EXTRA , projectPost.getFipsCounty());
                section =  SearchFilterCountyActivity.REQUEST_COUNTY;

                break;
            default:
                Log.w(TAG, "onClicked: Warning: Unsupported view id clicked: " + id);
                return;
        }
        if(i != null){
            activity.startActivityForResult(i, section);
        }

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        setTargetStartDate(sdf.format(calendar.getTime()));
    }

    public void onClickCancel(View view) {
        Log.d(TAG, "onClickCancel");
        activity.setResult(RESULT_CANCELED);
        activity.finish();
    }

    public void onClickSave(View view) {
        Log.d(TAG, "onClickSave");
        //Note: this method below is used to delete all the SharedPref file that is no longer needed when exiting this Custom Project Activity.
        // But if the mode is in editMode, the sharedPref for Type and Data data values will not be deleted because it migh be used later by this CustomProject Edit functionality,
        // which will be called under the ProjectDetailActivity activity class.
        deletePrefFilterFieldValues();

        DialogInterface.OnClickListener onClickAddListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        postProject();
                        //dialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEUTRAL:
                        dialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        showPostProjectAlertDialog(view, onClickAddListener);
    }

    public void onClickCounty(View view) {
        Log.d(TAG, "onClickCounty");
    }


    @Override
    public void onMapSelected(View view) {
        Log.d(TAG, "onMapSelected");
    }

    private boolean isEditMode() {
        return this.projectId > 0;
    }

    /*
     * Bindings for values to be posted to the API or which can be displayed as native values
     */

    @Bindable
    public String getActivityTitle() {
        if (!isEditMode()) {
            return activity.getString(R.string.new_project);
        } else return activity.getString(R.string.update_project);
    }

    @Bindable
    public String getSaveButtonText() {
        if (!isEditMode()) {
            return activity.getString(R.string.save);
        } else return activity.getString(R.string.update);
    }

    @Bindable
    public ProjectPost getProjectPost() {
        return projectPost;
    }

    @Bindable
    public String getTargetStartDate() {
        return targetStartDate;
    }

    public void setTargetStartDate(String targetStartDate) {
        projectPost.setTargetStartDate(targetStartDate);
        this.targetStartDate = targetStartDate;
        notifyPropertyChanged(BR.targetStartDate);
    }


    /*
     * Bindings for values to be used for visual display only
     */

    @Bindable
    public String getStageSelect() {
        return stageSelect;
    }

    public void setStageSelect(String stageSelect) {
        this.stageSelect = stageSelect;
        notifyPropertyChanged(BR.stageSelect);
    }

    @Bindable
    public String getTypeSelect() {
        return typeSelect;
    }

    public void setTypeSelect(String typeSelect) {
        this.typeSelect = typeSelect;
        notifyPropertyChanged(BR.typeSelect);
    }

    public boolean getCountyIsEditable() {
        return countyIsEditable;
    }


    //Finds the Lng and Lat for the current text address.
    private void resetLngAndLat() {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            String strAddress = "";
            if (projectPost.getAddress1().isEmpty()) {
                strAddress += projectPost.getAddress1();
            }
            if (projectPost.getCity().isEmpty()) {
                strAddress += " " + projectPost.getCity();
            }
            if (projectPost.getState().isEmpty()) {
                strAddress += " " + projectPost.getState();
            }

            List<Address> addresses = geocoder.getFromLocationName(strAddress, 5);
            if (addresses.size() > 0) {
                projectPost.getGeocode().setLat(addresses.get(0).getLatitude());
                projectPost.getGeocode().setLng(addresses.get(0).getLongitude());
            }
        } catch (IOException e) {
            Log.e(TAG, "getAddressFromString: " + e.getMessage());
        }
    }

    private LatLng getLocationFromAddress(String strAddress) {
        Log.d(TAG, "getLocationFromAddress: " + strAddress);

        Geocoder coder = new Geocoder(getActivityWeakReference().get(), Locale.US);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null || address.size() == 0) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException e) {
            Log.e(TAG, "getLocationFromAddress: Error. " + e.getMessage());
        }
        return p1;
    }

    //Note: This method is used for saving the previous selected Stage item for later use if needed.
    public void savePrefBundleStageOnly(final String filterDataName, final Bundle bundle) {

        Thread t = new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        SharedPreferences spref = activity.getSharedPreferences(filterDataName + "name", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = spref.edit();
                        edit.clear();
                        for (String key : bundle.keySet()) {
                            //  edit.putString(key,bundle.getString(key));
                            Bundle b = bundle.getBundle(key);

                            if (b == null) continue;
                            edit.putString(key, b.getString(SearchFilterStageViewModel.BUNDLE_KEY_NAME));
                        }
                        edit.apply();
                        savePrefBundleStageViewOnly(filterDataName, bundle);

                    }
                });
        t.start();
    }

    //Note: Continuation of saving the other part of the previous stage item to the sharedPreferences.
    public void savePrefBundleStageViewOnly(String filterDataName, Bundle bundle) {
        SharedPreferences spref = activity.getSharedPreferences(filterDataName + "view", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spref.edit();
        edit.clear();
        for (String key : bundle.keySet()) {
            //  edit.putString(key,bundle.getString(key));
            Bundle b = bundle.getBundle(key);
            if (b == null) continue;
            edit.putString(key, b.getString(SearchFilterStageViewModel.BUNDLE_KEY_VIEW_TYPE));
        }
        edit.apply();
    }

    //Note: This is used for deleting the filter data stored in the SharedPreferences.
    public void deletePrefFilterFieldValues() {
        clearSharedPref("lastcheckedStageItems");
        clearSharedPref("lastcheckedTypeItems");
        clearSharedPref(activity.getString(R.string.Filter));
        if (!isEditMode()) clearSharedPref(activity.getString(R.string.FilterTypeData));
        if (!isEditMode()) clearSharedPref(activity.getString(R.string.FilterStageData) + "name");
        if (!isEditMode()) clearSharedPref(activity.getString(R.string.FilterStageData) + "view");
    }

    //Note: This is used for processing the deletion of the filter data stored in the SharedPreferences.
    private void clearSharedPref(String dataName) {
        SharedPreferences spref = activity.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        if (spref == null) return;
        SharedPreferences.Editor editData = spref.edit();
        //  if (editData == null) return;
        editData.clear();
        editData.commit();
    }

    //Note: This is used to save the filter data to the SharedPreferences.
    public void savePrefBundle(final String filterDataName, final Bundle bundle) {
        Thread t = new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        SharedPreferences spref = activity.getSharedPreferences(filterDataName, Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = spref.edit();
                        edit.clear();
                        for (String key : bundle.keySet()) {
                            edit.putString(key, bundle.getString(key));
                        }
                        edit.apply();
                    }
                });

        t.start();
    }

    //Note: This is used of getting all the project type data needed based on the Project type id.
    public void searchBackProjectTypeName(String key) {

        Realm realm = Realm.getDefaultInstance();
        String projectName = "";
        // check the grandchild-level (primary type) for a matching ID
        PrimaryProjectType primaryType = realm.where(PrimaryProjectType.class).equalTo("id", Integer.valueOf(key.trim())).findFirst();
        if (primaryType != null) {
            Log.d(TAG, "searchBackProjectTypeName: mainTitle" + primaryType.getTitle());
            projectName = primaryType.getTitle();
        }   // if that's null, look for a matching child-level (subcategory) ID and if found, add all of its primary type IDs

        if (!projectName.trim().isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putString(key, projectName);
            setTypeSelect(projectName);

            //Saving all the extracted project type data to the sharedprefences. This will be used when The Search ProjectType View model section is called (getPref).
            savePrefBundle(activity.getString(R.string.FilterTypeData), bundle);
        }
    }

    //Note: This is used of getting all the Stage type data needed based on the Stage type id.
    public void searchBackStageName(final String key) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                SearchFilterStagesMain selectedParentStage = realm.where(SearchFilterStagesMain.class).equalTo("id", Integer.valueOf(key)).findFirst();
                if (selectedParentStage != null) {
                    stageName = selectedParentStage.getName();
                    stageType = "" + SearchFilterStageSingleSelectAdapter.PARENT_VIEW_TYPE;
                } else {
                    SearchFilterStage childStage = realm.where(SearchFilterStage.class).equalTo("id", Integer.valueOf(key)).findFirst();
                    if (childStage != null) {
                        stageName = childStage.getName();
                        stageType = "" + SearchFilterStageSingleSelectAdapter.CHILD_VIEW_TYPE;
                    }
                }


            }
        });

        if (!stageName.trim().isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putString(SearchFilterStageViewModel.BUNDLE_KEY_ID, key);
            bundle.putString(SearchFilterStageViewModel.BUNDLE_KEY_NAME, stageName);
            bundle.putString(SearchFilterStageViewModel.BUNDLE_KEY_VIEW_TYPE, stageType);
            Bundle bStage = new Bundle();
            bStage.putBundle(key, bundle);
            setStageSelect(stageName);

            //Saving all the extracted stage data to the sharedprefences. This will be used when The Search Stage View model section is called (getPref).
            savePrefBundleStageOnly(activity.getString(R.string.FilterStageData), bStage);
        }
    }

    public void setCounty(String id, String county) {
        projectPost.setCounty(county);
        projectPost.setFipsCounty(id);
    }

}
