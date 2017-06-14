package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.content.SearchFilterBiddingWithinActivity;
import com.lecet.app.content.SearchFilterBuildingOrHighwayActivity;
import com.lecet.app.content.SearchFilterJurisdictionActivity;
import com.lecet.app.content.SearchFilterLocationActivity;
import com.lecet.app.content.SearchFilterOwnerTypeActivity;
import com.lecet.app.content.SearchFilterProjectTypeActivity;
import com.lecet.app.content.SearchFilterStageActivity;
import com.lecet.app.content.SearchFilterUpdatedWithinActivity;
import com.lecet.app.content.SearchFilterValueActivity;
import com.lecet.app.content.SearchFilterWorkTypeActivity;

/**
 * Created by DomandTom 2016.
 */

public class SearchFilterAllTabbedViewModel extends BaseObservable {
    private static final String TAG = "SearchFilterMPFVM";

    public static final int LOCATION = 0;
    public static final int TYPE = 1;
    public static final int VALUE = 2;
    public static final int UPDATED_WITHIN = 3;
    public static final int JURISDICTION = 4;
    public static final int STAGE = 5;
    public static final int BIDDING_WITHIN = 6;
    public static final int BH = 7;
    public static final int OWNER_TYPE = 8;
    public static final int WORK_TYPE = 9;

    public static int VALUE_MAX = 999999999;
    public static final String EXTRA_LOCATION_CITY = "persistedLocationCity";
    public static final String EXTRA_LOCATION_STATE = "persistedLocationState";
    public static final String EXTRA_LOCATION_COUNTY = "persistedLocationCounty";
    public static final String EXTRA_LOCATION_ZIP = "persistedLocationZip";
    public static final String EXTRA_PROJECT_TYPE_ID = "persistedProjectTypeId";
    public static final String EXTRA_VALUE_MIN = "persistedValueMin";
    public static final String EXTRA_VALUE_MAX = "persistedValueMax";
    //    public static final String EXTRA_UPDATED_WITHIN = "persistedUpdatedWithin";
    public static final String EXTRA_JURISDICTION = "persistedJurisdiction";
    public static final String EXTRA_STAGE = "persistedStage";
    public static final String EXTRA_BIDDING_WITHIN_DISPLAY_STR = "persistedBiddingWithinDisplayStr";
    public static final String EXTRA_BIDDING_WITHIN_DAYS_INT = "persistedBiddingWithinDaysInt";
    public static final String EXTRA_BUILDING_OR_HIGHWAY = "persistedBuildingOrHighway";
    public static final String EXTRA_UPDATED_WITHIN_DISPLAY_STR = "persistedUpdatedWithinDisplayStr";
    public static final String EXTRA_UPDATED_WITHIN_DAYS_INT = "persistedUpdatedWithinDaysInt";
    public static final String EXTRA_OWNER_TYPE = "persistedOwnerType";
    public static final String EXTRA_WORK_TYPE = "persistedWorkType";
    public static final String ANY = "Any";
    public static boolean userCreated;
    private AppCompatActivity activity;
    private int id;
    private Intent intent;
    private boolean isProjectViewVisible = true;
    private boolean moreOption;


    /**
     * User's selected filter item - values for Project display
     */
    private String locationSelect;
    private String typeSelect;
    private String valueSelect;
    private String updatedWithinSelect;
    private String jurisdictionSelect;
    private String stageSelect;
    private String biddingWithinSelect;
    private String bhSelect;
    private String ownerTypeSelect;
    private String workTypeSelect;

    /**
     * User's selected filter item - values for Company display
     */
    private String clocationSelect;
    private String cvalueSelect;
    private String cjurisdictionSelect;
    private String cbiddingWithinSelect;
    private String ctypeSelect;

    /**
     * User's selected filter item - Values for persistence between filter Activities
     */
    private String persistedLocationCity;
    private String persistedLocationState;
    private String persistedLocationCounty;
    private String persistedLocationZip;
    private String persistedProjectTypeId;
    private String persistedValueMin;
    private String persistedValueMax;
    private String persistedUpdatedWithin;
    private String persistedJurisdiction;
    private String persistedStage;
    private String persistedBiddingWithin;
    private String[] persistedBuildingOrHighway;
    private String persistedOwnerType;
    private String persistedWorkType;
    private boolean usingProjectNearMe;


    /**
     * Constructor
     */
    public SearchFilterAllTabbedViewModel(AppCompatActivity activity) {
        this.activity = activity;
        setLocation_select("");
        setType_select("");
        setValue_select("");
        setUpdated_within_select(ANY);
        setJurisdiction_select(ANY);
        setStage_select(ANY);
        setBidding_within_select(ANY);
        setBh_select(ANY);
        setOwner_type_select(ANY);
        setWork_type_select(ANY);
        setCjurisdictionSelect(ANY);
        setCbiddingWithinSelect(ANY);
        setCtypeSelect(ANY);

        intent = activity.getIntent();

        setUsingProjectNearMe(intent.getBooleanExtra(activity.getString(R.string.using_project_near_me),false));
    }
    @Bindable
    public boolean getUsingProjectNearMe() {
        return usingProjectNearMe;
    }

    public void setUsingProjectNearMe(boolean usingProjectNearMe) {
        this.usingProjectNearMe = usingProjectNearMe;
        notifyPropertyChanged(BR.usingProjectNearMe);
    }

    public String getPersistedLocationCity() {
        return persistedLocationCity;
    }

    public void setPersistedLocationCity(String persistedLocationCity) {
        this.persistedLocationCity = persistedLocationCity;
    }

    public String getPersistedLocationState() {
        return persistedLocationState;
    }

    public void setPersistedLocationState(String persistedLocationState) {
        this.persistedLocationState = persistedLocationState;
    }

    public String getPersistedLocationCounty() {
        return persistedLocationCounty;
    }

    public void setPersistedLocationCounty(String persistedLocationCounty) {
        this.persistedLocationCounty = persistedLocationCounty;
    }

    public String getPersistedLocationZip() {
        return persistedLocationZip;
    }

    public void setPersistedLocationZip(String persistedLocationZip) {
        this.persistedLocationZip = persistedLocationZip;
    }

    public String getPersistedProjectTypeId() {
        return persistedProjectTypeId;
    }

    public void setPersistedProjectTypeId(String persistedProjectTypeId) {
        this.persistedProjectTypeId = persistedProjectTypeId;
    }

    public String getPersistedValueMin() {
        return persistedValueMin;
    }

    public void setPersistedValueMin(String persistedValueMin) {
        this.persistedValueMin = persistedValueMin;
    }

    public String getPersistedValueMax() {
        return persistedValueMax;
    }

    public void setPersistedValueMax(String persistedValueMax) {
        this.persistedValueMax = persistedValueMax;
    }

    public String getPersistedUpdatedWithin() {
        return persistedUpdatedWithin;
    }

    public void setPersistedUpdatedWithin(String persistedUpdatedWithin) {
        this.persistedUpdatedWithin = persistedUpdatedWithin;
    }

    public String getPersistedJurisdiction() {
        return persistedJurisdiction;
    }

    public void setPersistedJurisdiction(String persistedJurisdiction) {
        this.persistedJurisdiction = persistedJurisdiction;
    }

    public String getPersistedStage() {
        return persistedStage;
    }

    public void setPersistedStage(String persistedStage) {
        this.persistedStage = persistedStage;
    }

    public String getPersistedBiddingWithin() {
        return persistedBiddingWithin;
    }

    public void setPersistedBiddingWithin(String persistedBiddingWithin) {
        this.persistedBiddingWithin = persistedBiddingWithin;
    }

    public String[] getPersistedBuildingOrHighway() {
        return persistedBuildingOrHighway;
    }

    //    public void setPersistedBuildingOrHighway(String[] persistedBuildingOrHighway) {
    public void setPersistedBuildingOrHighway(Bundle bundle) {
        String arr[] = {"", ""};
        arr[0] = bundle.getString(SearchFilterBuildingOrHighwayViewModel.BUNDLE_KEY_DISPLAY_STR);  //arr[0];      // could come in as "Both", "Any", "Building" or "Heavy-Highway", to be converted to array ["B"] or ["H"] or ["B","H"]
        arr[1] = bundle.getString(SearchFilterBuildingOrHighwayViewModel.BUNDLE_KEY_TAG);
        this.persistedBuildingOrHighway = arr;
    }

    public String getPersistedOwnerType() {
        return persistedOwnerType;
    }

    public void setPersistedOwnerType(String persistedOwnerType) {
        this.persistedOwnerType = persistedOwnerType;
    }

    public String getPersistedWorkType() {
        return persistedWorkType;
    }

    public void setPersistedWorkType(String persistedWorkType) {
        this.persistedWorkType = persistedWorkType;
    }

    /**
     * getter and setter
     *
     * @return
     */
    @Bindable
    public String getClocationSelect() {
        return clocationSelect;
    }

    public void setClocationSelect(String clocationSelect) {
        this.clocationSelect = clocationSelect;
        notifyPropertyChanged(BR.clocationSelect);
    }

    @Bindable
    public String getCvalueSelect() {
        return cvalueSelect;
    }

    public void setCvalueSelect(String cvalueSelect) {
        this.cvalueSelect = cvalueSelect;
        notifyPropertyChanged(BR.cvalueSelect);
    }

    @Bindable
    public String getCjurisdictionSelect() {
        return cjurisdictionSelect;
    }

    public void setCjurisdictionSelect(String cjurisdictionSelect) {
        this.cjurisdictionSelect = cjurisdictionSelect;
        notifyPropertyChanged(BR.cjurisdictionSelect);

    }

    @Bindable
    public String getCbiddingWithinSelect() {
        return cbiddingWithinSelect;
    }

    public void setCbiddingWithinSelect(String cbiddingWithinSelect) {
        this.cbiddingWithinSelect = cbiddingWithinSelect;
        notifyPropertyChanged(BR.cbiddingWithinSelect);

    }

    @Bindable
    public String getCtypeSelect() {
        return ctypeSelect;
    }

    public void setCtypeSelect(String ctypeSelect) {
        this.ctypeSelect = ctypeSelect;
        notifyPropertyChanged(BR.ctypeSelect);

    }

    @Bindable
    public String getLocation_select() {
        return locationSelect;
    }

    @Bindable
    public String getType_select() {
        return typeSelect;
    }

    @Bindable
    public String getValue_select() {
        return valueSelect;
    }

    @Bindable
    public String getUpdated_within_select() {
        return updatedWithinSelect;
    }

    @Bindable
    public String getJurisdiction_select() {
        return jurisdictionSelect;
    }

    @Bindable
    public String getStage_select() {
        return stageSelect;
    }

    @Bindable
    public String getBidding_within_select() {
        return biddingWithinSelect;
    }

    @Bindable
    public String getBh_select() {
        return bhSelect;
    }

    @Bindable
    public String getOwner_type_select() {
        return ownerTypeSelect;
    }

    @Bindable
    public String getWork_type_select() {
        return workTypeSelect;
    }

    public void setLocation_select(String locationSelect) {
        this.locationSelect = locationSelect;
        notifyPropertyChanged(BR.location_select);
    }

    public void setType_select(String typeSelect) {
        this.typeSelect = typeSelect;
        notifyPropertyChanged(BR.type_select);
    }

    public void setValue_select(String valueSelect) {
        this.valueSelect = valueSelect;
        notifyPropertyChanged(BR.value_select);

    }

    public void setUpdated_within_select(String updatedWithinSelect) {
        this.updatedWithinSelect = updatedWithinSelect;
        notifyPropertyChanged(BR.updated_within_select);

    }

    public void setJurisdiction_select(String jurisdictionSelect) {
        this.jurisdictionSelect = jurisdictionSelect;
        notifyPropertyChanged(BR.jurisdiction_select);

    }

    public void setStage_select(String stageSelect) {
        this.stageSelect = stageSelect;
        notifyPropertyChanged(BR.stage_select);

    }

    public void setBidding_within_select(String biddingWithinSelect) {
        this.biddingWithinSelect = biddingWithinSelect;
        notifyPropertyChanged(BR.bidding_within_select);

    }

    public void setBh_select(String bhSelect) {
        this.bhSelect = bhSelect;
        notifyPropertyChanged(BR.bh_select);

    }

    public void setOwner_type_select(String ownerTypeSelect) {
        this.ownerTypeSelect = ownerTypeSelect;
        notifyPropertyChanged(BR.owner_type_select);

    }

    public void setWork_type_select(String workTypeSelect) {
        this.workTypeSelect = workTypeSelect;
        notifyPropertyChanged(BR.work_type_select);

    }


    public AppCompatActivity getActivity() {
        return activity;
    }

    /**
     * OnClick handlers
     **/

    public void onClicked(View view) {
        Intent i = null;
        id = view.getId();
        int section = 0;
        switch (id) {

            case R.id.clocation:
                section = LOCATION;

                i = new Intent(activity, SearchFilterLocationActivity.class);
                break;

            case R.id.location:
                section = LOCATION;
                i = new Intent(activity, SearchFilterLocationActivity.class);
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_LOCATION_CITY, getPersistedLocationCity());
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_LOCATION_STATE, getPersistedLocationState());
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_LOCATION_COUNTY, getPersistedLocationCounty());
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_LOCATION_ZIP, getPersistedLocationZip());
                break;

            case R.id.ctype:
            case R.id.type:
                section = TYPE;
                i = new Intent(activity, SearchFilterProjectTypeActivity.class);
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_PROJECT_TYPE_ID, getPersistedProjectTypeId());
                break;

            case R.id.cvalue:
            case R.id.value:
                section = VALUE;
                i = new Intent(activity, SearchFilterValueActivity.class);
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_VALUE_MIN, getPersistedValueMin());
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_VALUE_MAX, getPersistedValueMax());
                break;

            case R.id.updated_within:
                section = UPDATED_WITHIN;
                i = new Intent(activity, SearchFilterUpdatedWithinActivity.class);
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_UPDATED_WITHIN_DISPLAY_STR, getUpdated_within_select());
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_UPDATED_WITHIN_DAYS_INT, getPersistedUpdatedWithin());

                break;

            case R.id.cjurisdiction:
            case R.id.jurisdiction:
                section = JURISDICTION;
                i = new Intent(activity, SearchFilterJurisdictionActivity.class);
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_JURISDICTION, getPersistedJurisdiction());
                break;

            case R.id.stage:
                section = STAGE;
                i = new Intent(activity, SearchFilterStageActivity.class);
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_STAGE, getPersistedStage());
                break;

            case R.id.cbidding_within:
            case R.id.bidding_within:
                section = BIDDING_WITHIN;
                i = new Intent(activity, SearchFilterBiddingWithinActivity.class);
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_BIDDING_WITHIN_DISPLAY_STR, getBidding_within_select());
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_BIDDING_WITHIN_DAYS_INT, getPersistedBiddingWithin());
                break;

            case R.id.bh:
                section = BH;
                i = new Intent(activity, SearchFilterBuildingOrHighwayActivity.class);
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_BUILDING_OR_HIGHWAY, getPersistedBuildingOrHighway());
                break;

            case R.id.ownertype:
                section = OWNER_TYPE;
                i = new Intent(activity, SearchFilterOwnerTypeActivity.class);
                setPersistedOwnerType(getOwner_type_select());
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_OWNER_TYPE, getPersistedOwnerType());
                break;

            case R.id.worktype:
                section = WORK_TYPE;
                i = new Intent(activity, SearchFilterWorkTypeActivity.class);
                setPersistedWorkType(getWork_type_select());
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_WORK_TYPE, getPersistedWorkType());
                break;

            case R.id.option:
                setMoreOption(true);
                return;

            case R.id.feweroption:
                setMoreOption(false);
                return;

            case R.id.apply_button:
                saveResult();
                activity.finish();
                return;

            case R.id.cancel_button:
                intent.putExtra(SearchViewModel.SAVE_SEARCH_CATEGORY, "");
                activity.finish();
                return;

            default:
                Log.w(TAG, "onClicked: Warning: Unsupported view id clicked: " + id);
                return;
        }

        activity.startActivityForResult(i, section);
    }

    public void onClickedProjectCompanyTab(View view) {
        isProjectViewVisible = view.getId() == R.id.btn_project;
        if (getIsProjectViewVisible()) {
            Log.d("SearchFilterMPFVM", "project tab clicked");
            //   intent.putExtra(SearchViewModel.SAVE_SEARCH_CATEGORY, SearchViewModel.SAVE_SEARCH_CATEGORY_PROJECT);
        } else {
            Log.d("SearchFilterMPFVM", "company tab clicked");
            //   intent.putExtra(SearchViewModel.SAVE_SEARCH_CATEGORY, SearchViewModel.SAVE_SEARCH_CATEGORY_COMPANY);
        }
        notifyPropertyChanged(BR.isProjectViewVisible);
    }

    @Bindable
    public boolean getIsProjectViewVisible() {
        return isProjectViewVisible;
    }

    /**
     * Put the passed filter name and String value into an Extra to be returned with the Intent
     */
    public void setSearchFilterResult(String name, String content) {
        intent.putExtra(name, content);
    }

    public void saveResult() {
        if (getIsProjectViewVisible()) {
            intent.putExtra(SearchViewModel.SAVE_SEARCH_CATEGORY, SearchViewModel.SAVE_SEARCH_CATEGORY_PROJECT);
        } else {
            intent.putExtra(SearchViewModel.SAVE_SEARCH_CATEGORY, SearchViewModel.SAVE_SEARCH_CATEGORY_COMPANY);
        }

        activity.setResult(Activity.RESULT_OK, intent);
    }

    @Bindable
    public boolean getMoreOption() {
        return moreOption;
    }

    @Bindable
    public void setMoreOption(boolean moreOption) {
        this.moreOption = moreOption;
        notifyPropertyChanged(BR.moreOption);
    }
}
