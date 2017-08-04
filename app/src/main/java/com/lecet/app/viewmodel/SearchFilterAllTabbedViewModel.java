package com.lecet.app.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.lecet.app.utility.Log;

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

    private static final String EXTRA_LOCATION_CITY = "persistedLocationCity";
    private static final String EXTRA_LOCATION_STATE = "persistedLocationState";
    private static final String EXTRA_LOCATION_COUNTY = "persistedLocationCounty";
    private static final String EXTRA_LOCATION_ZIP = "persistedLocationZip";
    private static final String EXTRA_PROJECT_TYPE_ID = "persistedProjectTypeId";
    private static final String EXTRA_PROJECT_TYPE_ID_INT = "persistedProjectTypeIdInt";

    public static final String EXTRA_VALUE_MIN = "persistedValueMin";
    public static final String EXTRA_VALUE_MAX = "persistedValueMax";
    public static final String EXTRA_VALUE = "persistedValue";
    public static final String EXTRA_JURISDICTION = "persistedJurisdiction";
    public static final String EXTRA_STAGE = "persistedStage";
    public static final String EXTRA_BIDDING_WITHIN_DISPLAY_STR = "persistedBiddingWithinDisplayStr";
    public static final String EXTRA_BIDDING_WITHIN_DAYS_INT = "persistedBiddingWithinDaysInt";
    public static final String EXTRA_BUILDING_OR_HIGHWAY = "persistedBuildingOrHighway";
    public static final String EXTRA_BUILDING_OR_HIGHWAY_TAG = "persistedBuildingOrHighwayTag";
    public static final String EXTRA_UPDATED_WITHIN_DISPLAY_STR = "persistedUpdatedWithinDisplayStr";
    public static final String EXTRA_UPDATED_WITHIN_DAYS_INT = "persistedUpdatedWithinDaysInt";
    public static final String EXTRA_OWNER_TYPE = "persistedOwnerType";
    public static final String EXTRA_OWNER_TYPE_ID = "persistedOwnerTypeId";
    public static final String EXTRA_WORK_TYPE = "persistedWorkType";
    public static final String EXTRA_WORK_TYPE_ID = "persistedWorkTypeId";
    public static final String ANY = "Any";

    //For Company
    private static final String EXTRA_CLOCATION_CITY = "CompanyLocationCity";
    private static final String EXTRA_CVALUE = "CompanyValue";
    private static final String EXTRA_CJURISDICTION = "CompanyJurisdiction";
    private static final String EXTRA_CBIDDING_WITHIN_DISPLAY_STR = "CompanyBiddingWithinDisplayStr";
    private static final String EXTRA_CBIDDING_WITHIN_DAYS_INT = "CompanyBiddingWithinDaysInt";
    private static final String EXTRA_CPROJECT_TYPE_ID = "CompanyProjectTypeId";

    private AppCompatActivity activity;
    private int id;
    private Intent intent;

    //Note: This static userCreated is needed because it used mostly from different parts of the code and needs to check whether the app is in the Project Near Me or in the Main Search section.
    public static boolean userCreated;

    //Note: This variable is used for instantSearch for determining if the user has selected the Project instant search tab against the Company instant search tab.
    private boolean isProjectViewVisible = true;

    //Note; This variable is used for determining if the OwnerType field view and WorkType view should be showed or hide.
    private boolean moreOption;

    /**
     * Note: This resultxxx variables is used for holding the url filter parameter to be passed for web api.
     * These setter/getter methods of these variables are also used for saving/restoring the previous  selected filter items to the sharedPreferences.
    */
    private String resultUpdateWithin;
    private String resultBiddingWithin;
    private String resultValue;
    private String resultProjectType;
    private String resultStage;
    private String resultJurisdiction;
    private String resultBH;
    private String resultOwnerType;
    private String resultWorkType;


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
    private String persistProjectTypeIdInt;

    /** Note: This variable is used for viewing and hiding the search location field.
     *      If the user is in the Project Near Me section, this search location field is hidden. If not, it is visible.
     */
    private boolean usingProjectNearMe;

    // SharedPreferences spref;

    /**
     * Constructor
     */
    public SearchFilterAllTabbedViewModel(AppCompatActivity activity) {
        this.activity = activity;
        //   spref = getActivity().getSharedPreferences(activity.getString(R.string.Filter), Context.MODE_PRIVATE);

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
        //Then set all the previous result to the intent
       // setAllFilterResults();
        setUsingProjectNearMe(intent.getBooleanExtra(activity.getString(R.string.using_project_near_me), false));
        SharedPreferences spref = getActivity().getSharedPreferences(activity.getString(R.string.Filter), Context.MODE_PRIVATE);
        if (spref != null) {
            getPrefFilterFieldValues(spref);
        } else {
            Log.d("sprefnull", "sprefnull");
        }
    }

    //Note: Temporarily not used. This could be used in the code if needed to reset the value of resultxxx.
 /*   void initResult(){
        setResultValue("");
        setResultBiddingWithin("");
        setResultUpdateWithin("");

        setResultStage("");
        setResultProjectType("");
        setResultJurisdiction("");

        setResultBH("");
        setResultOwnerType("");
        setResultWorkType("");
    }*/

    // Note: The setter/getter methods are used to determine if the location needed to be hide ( Yes for using the ProjectNearMe section)
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
        SearchFilterAllTabbedViewModel.userCreated = false;
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
                i.putExtra(EXTRA_PROJECT_TYPE_ID_INT, getPersistProjectTypeIdInt());
                break;

            case R.id.cvalue:
            case R.id.value:
                section = VALUE;
                i = new Intent(activity, SearchFilterValueActivity.class);
                String value = getValue_select();
                if (value != null && value.length() > 0) {
                    i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_VALUE_MIN, value.substring(1, value.indexOf('-')).trim());
                    i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_VALUE_MAX, value.substring(value.indexOf('-') + 3, value.length()).trim());
                }
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
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_OWNER_TYPE, getOwner_type_select());
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_OWNER_TYPE_ID, getPersistedOwnerType());
                break;

            case R.id.worktype:
                section = WORK_TYPE;
                i = new Intent(activity, SearchFilterWorkTypeActivity.class);
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_WORK_TYPE, getWork_type_select());
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_WORK_TYPE_ID, getPersistedWorkType());
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
            SearchViewModel.companyInstantSearch=false;
        } else {
            Log.d("SearchFilterMPFVM", "company tab clicked");
            //Focus in company. Search for the Company.
            SearchViewModel.companyInstantSearch=true;
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

    public String getPersistProjectTypeIdInt() {
        return persistProjectTypeIdInt;
    }

    public void setPersistProjectTypeIdInt(String persistProjectTypeIdInt) {
        this.persistProjectTypeIdInt = persistProjectTypeIdInt;
    }

    //Note: This method is used to extract all the filter saved from the SharedPreferences to be used to display again the previous selected item to the filter field section.
    void getPrefFilterFieldValues(SharedPreferences spref) {

        /** Note: For displaying the project filter values to the project search field and project instant search tab fields (for instansearch)
         *  and for extracting the filter data needed for processing.
         */
        setLocation_select(spref.getString(EXTRA_LOCATION_CITY, getLocation_select()));
        setType_select(spref.getString(EXTRA_PROJECT_TYPE_ID, getType_select()));
        setPersistProjectTypeIdInt(spref.getString(EXTRA_PROJECT_TYPE_ID_INT, getPersistProjectTypeIdInt()));

        setUpdated_within_select(spref.getString(EXTRA_UPDATED_WITHIN_DISPLAY_STR, getUpdated_within_select()));
        setPersistedUpdatedWithin(spref.getString(EXTRA_UPDATED_WITHIN_DAYS_INT, getPersistedUpdatedWithin()));

        setJurisdiction_select(spref.getString(EXTRA_JURISDICTION, getJurisdiction_select()));
        setStage_select(spref.getString(EXTRA_STAGE, getStage_select()));
        setBidding_within_select(spref.getString(EXTRA_BIDDING_WITHIN_DISPLAY_STR, getBidding_within_select()));
        setPersistedBiddingWithin(spref.getString(EXTRA_BIDDING_WITHIN_DAYS_INT, getPersistedBiddingWithin()));

        setBh_select(spref.getString(EXTRA_BUILDING_OR_HIGHWAY, getBh_select()));
        if (getPersistedBuildingOrHighway() != null && getPersistedBuildingOrHighway().length > 0)
            persistedBuildingOrHighway[1] = spref.getString(EXTRA_BUILDING_OR_HIGHWAY_TAG, getPersistedBuildingOrHighway()[1]);

        setOwner_type_select(spref.getString(EXTRA_OWNER_TYPE, getOwner_type_select()));
        setPersistedOwnerType(spref.getString(EXTRA_OWNER_TYPE_ID, getPersistedOwnerType()));

        setWork_type_select(spref.getString(EXTRA_WORK_TYPE, getWork_type_select()));
        setPersistedWorkType(spref.getString(EXTRA_WORK_TYPE_ID, getPersistedWorkType()));

        setValue_select(spref.getString(EXTRA_VALUE, getValue_select()));

        //Note: For displaying the company filter values to the company instant search tab fields and for extracting the filter data needed for processing.
        setClocationSelect(spref.getString(EXTRA_CLOCATION_CITY, getClocationSelect()));
        setCvalueSelect(spref.getString(EXTRA_CVALUE, getCvalueSelect()));
        setCjurisdictionSelect(spref.getString(EXTRA_CJURISDICTION, getCjurisdictionSelect()));
        setCbiddingWithinSelect(spref.getString(EXTRA_CBIDDING_WITHIN_DISPLAY_STR, getCbiddingWithinSelect()));
        setCtypeSelect(spref.getString(EXTRA_CPROJECT_TYPE_ID, getCtypeSelect()));

        //Note: For Resultset of the url filter parameter values that will be passed to the web api.
        setResultProjectType(spref.getString(activity.getString(R.string.FilterResultProjectType),""));
        setResultValue(spref.getString(activity.getString(R.string.FilterResultValue),""));
        setResultUpdateWithin(spref.getString(activity.getString(R.string.FilterResultUpdatedWithin),""));
        setResultJurisdiction(spref.getString(activity.getString(R.string.FilterResultJurisdiction),""));
        setResultStage(spref.getString(activity.getString(R.string.FilterResultStage),""));
        setResultBiddingWithin(spref.getString(activity.getString(R.string.FilterResultBiddingWithin),""));
        setResultBH(spref.getString(activity.getString(R.string.FilterResultBH),""));
        setResultOwnerType(spref.getString(activity.getString(R.string.FilterResultOwnerType),""));
        setResultWorkType(spref.getString(activity.getString(R.string.FilterResultWorkType),""));

        //Then set all the previous result to the intent
        setAllFilterResults();
    }


    /** Note: Used for setting all the previous url filter parameters extracted from the sharedpreferences
     *  and place this filters to the intent where it will be used in  processing the search filter for web api in the MPSActivity class.
     */
    void setAllFilterResults() {
        setFilterResult(SearchViewModel.FILTER_PROJECT_UPDATED_IN_LAST, getResultUpdateWithin());
        setFilterResult(SearchViewModel.FILTER_PROJECT_BIDDING_WITHIN, getResultBiddingWithin());
        setFilterResult(SearchViewModel.FILTER_PROJECT_VALUE, getResultValue());
        setFilterResult(SearchViewModel.FILTER_PROJECT_TYPE, getResultProjectType());
        setFilterResult(SearchViewModel.FILTER_PROJECT_JURISDICTION,getResultJurisdiction());
        setFilterResult(SearchViewModel.FILTER_PROJECT_STAGE,getResultStage());
        setFilterResult(SearchViewModel.FILTER_PROJECT_WORK_TYPE, getResultWorkType());
        setFilterResult(SearchViewModel.FILTER_PROJECT_OWNER_TYPE,getResultOwnerType());
        setFilterResult(SearchViewModel.FILTER_PROJECT_BUILDING_OR_HIGHWAY,getResultBH());

    }
    // Note: Used for setting the extracted filter value with corresponding filter key name to the Intent object.
    void setFilterResult(String key, String value) {
        if (value != null && !value.isEmpty()) setSearchFilterResult(key, value);
        else setSearchFilterResult(key, "");
    }

    // Note: Used for saving the filter data to the sharedPreferences.
    public void savePref(SharedPreferences.Editor edit, String key, String values) {
        // SharedPreferences.Editor edit = spref.edit();
        if (values != null && !values.isEmpty()) edit.putString(key, values);
    }

    // Note: Used to save all previous selected filter items to the sharedpreferences named Filter.
    public void savePrefFilterFieldValues() {
        // Note: The use of thread object here is not to hinder the processing of the filter to the web api while the filter data is saving to the sharedPreferences file.
        Thread t = new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        SharedPreferences spref = getActivity().getSharedPreferences(activity.getString(R.string.Filter), Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = spref.edit();
                        edit.clear();

                        //Saving the Project Filter display values that could be used later
                        savePref(edit, SearchViewModel.FILTER_PROJECT_UPDATED_IN_LAST, getResultUpdateWithin());
                        savePref(edit, SearchViewModel.FILTER_PROJECT_BIDDING_WITHIN, getResultBiddingWithin());
                        savePref(edit, SearchViewModel.FILTER_PROJECT_VALUE, getResultValue());
                        savePref(edit, EXTRA_LOCATION_CITY, getLocation_select());
                        savePref(edit, EXTRA_PROJECT_TYPE_ID, getType_select());
                        savePref(edit, EXTRA_PROJECT_TYPE_ID_INT, getPersistProjectTypeIdInt());

                        savePref(edit, EXTRA_VALUE, getValue_select());
                        savePref(edit, EXTRA_VALUE_MIN, persistedValueMin);
                        savePref(edit, EXTRA_VALUE_MAX, persistedValueMax);

                        savePref(edit, EXTRA_UPDATED_WITHIN_DISPLAY_STR, getUpdated_within_select());
                        savePref(edit, EXTRA_UPDATED_WITHIN_DAYS_INT, getPersistedUpdatedWithin());

                        savePref(edit, EXTRA_JURISDICTION, getJurisdiction_select());
                        savePref(edit, EXTRA_STAGE, getStage_select());

                        savePref(edit, EXTRA_BIDDING_WITHIN_DISPLAY_STR, getBidding_within_select());
                        savePref(edit, EXTRA_BIDDING_WITHIN_DAYS_INT, getPersistedBiddingWithin());

                        savePref(edit, EXTRA_BUILDING_OR_HIGHWAY, getBh_select());
                        if (getPersistedBuildingOrHighway() != null && getPersistedBuildingOrHighway().length > 0)
                            savePref(edit, EXTRA_BUILDING_OR_HIGHWAY_TAG, getPersistedBuildingOrHighway()[1]);

                        savePref(edit, EXTRA_OWNER_TYPE, getOwner_type_select());
                        savePref(edit, EXTRA_OWNER_TYPE_ID, getPersistedOwnerType());
                        savePref(edit, EXTRA_WORK_TYPE, getWork_type_select());
                        savePref(edit, EXTRA_WORK_TYPE_ID, getPersistedWorkType());

                        //Saving the Company Filter display values that could be used later
                        savePref(edit, EXTRA_CLOCATION_CITY, getClocationSelect());
                        savePref(edit, EXTRA_CVALUE, getCvalueSelect());
                        savePref(edit, EXTRA_CJURISDICTION, getCjurisdictionSelect());
                        savePref(edit, EXTRA_CBIDDING_WITHIN_DISPLAY_STR, getCbiddingWithinSelect());
                        savePref(edit, EXTRA_CPROJECT_TYPE_ID, getCtypeSelect());

                        //Saving the Project/Company Filter Final Result values for Web api that could be used later
                        savePref(edit,activity.getString(R.string.FilterResultProjectType),getResultProjectType());
                        savePref(edit,activity.getString(R.string.FilterResultValue),getResultValue());
                        savePref(edit,activity.getString(R.string.FilterResultUpdatedWithin),getResultUpdateWithin());
                        savePref(edit,activity.getString(R.string.FilterResultJurisdiction),getResultJurisdiction());
                        savePref(edit,activity.getString(R.string.FilterResultStage),getResultStage());
                        savePref(edit,activity.getString(R.string.FilterResultBiddingWithin),getResultBiddingWithin());
                        savePref(edit,activity.getString(R.string.FilterResultBH),getResultBH());
                        savePref(edit,activity.getString(R.string.FilterResultOwnerType),getResultOwnerType());
                        savePref(edit,activity.getString(R.string.FilterResultWorkType),getResultWorkType());
                        edit.apply();
                    }
                });
        t.start();
    }

    /** Note: This method is used for saving the special filters data (Project types and Jurisdiction) to the sharedPreferences
     *
     * @param filterDataName - used as a filter key name for saving to sharedPreferences.
     * @param bundle - hold the filter data to be saved to the sharedPreferences.
     */
    public void savePrefBundle(final String filterDataName, final Bundle bundle) {

        // Note: The use of thread object here is not to hinder the processing of the filter to the web api while the filter data is saving to the sharedPreferences file.
        Thread t = new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        SharedPreferences spref = getActivity().getSharedPreferences(filterDataName, Context.MODE_PRIVATE);
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

    /** Note: This method is used for saving the special filter data (Stage) to the sharedPreferences
     *
     * @param filterDataName - used as a filter key name for saving to sharedPreferences.
     * @param bundle - hold the filter data to be saved to the sharedPreferences.
     */
    public void savePrefBundleStageOnly(String filterDataName, final Bundle bundle) {
        final String fName = filterDataName + "name";

        // Note: The use of thread object here is not to hinder the processing of the filter to the web api while the filter data is saving to the sharedPreferences file.
        Thread t = new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        SharedPreferences spref = getActivity().getSharedPreferences(fName, Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = spref.edit();
                        edit.clear();
                        for (String key : bundle.keySet()) {
                            //  edit.putString(key,bundle.getString(key));
                            Bundle b = bundle.getBundle(key);

                            if (b == null) continue;
                            edit.putString(key, b.getString(SearchFilterStageViewModel.BUNDLE_KEY_NAME));
                        }
                        edit.apply();
                    }
                });
        t.start();

        savePrefBundleStageViewOnly(filterDataName, bundle);
    }

    /** Note: This method is used for saving the other special filter data (Stage) to the sharedPreferences
     *
     * @param filterDataName - used as a filter key name for saving to sharedPreferences.
     * @param bundle - hold the filter data to be saved to the sharedPreferences.
     */
    public void savePrefBundleStageViewOnly(String filterDataName, final Bundle bundle) {
        final String fViewType = filterDataName + "view";
        final SharedPreferences spref = getActivity().getSharedPreferences(fViewType, Context.MODE_PRIVATE);
        final SharedPreferences.Editor edit = spref.edit();

        // Note: The use of thread object here is not to hinder the processing of the filter to the web api while the filter data is saving to the sharedPreferences file.
        Thread t = new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        // SharedPreferences spref = getActivity().getSharedPreferences(fViewType, Context.MODE_PRIVATE);
                        // SharedPreferences.Editor edit = spref.edit();
                        edit.clear();
                        for (String key : bundle.keySet()) {
                            //  edit.putString(key,bundle.getString(key));
                            Bundle b = bundle.getBundle(key);
                            if (b == null) continue;
                            edit.putString(key, b.getString(SearchFilterStageViewModel.BUNDLE_KEY_VIEW_TYPE));
                        }
                        edit.apply();
                    }
                });
        t.start();
    }

    /** Note: The setter/getter methods for resultxxx.
     *
     * @return
     */
    public String getResultBH() {
        return resultBH;
    }

    public void setResultBH(String resultBH) {
        this.resultBH = resultBH;
    }

    public String getResultOwnerType() {
        return resultOwnerType;
    }

    public void setResultOwnerType(String resultOwnerType) {
        this.resultOwnerType = resultOwnerType;
    }

    public String getResultWorkType() {
        return resultWorkType;
    }

    public void setResultWorkType(String resultWorkType) {
        this.resultWorkType = resultWorkType;
    }

    public String getResultProjectType() {
        return resultProjectType;
    }

    public void setResultProjectType(String resultProjectType) {
        this.resultProjectType = resultProjectType;
    }

    public String getResultStage() {
        return resultStage;
    }

    public void setResultStage(String resultStage) {
        this.resultStage = resultStage;
    }

    public String getResultJurisdiction() {
        return resultJurisdiction;
    }

    public void setResultJurisdiction(String resultJurisdiction) {
        this.resultJurisdiction = resultJurisdiction;
    }

    public String getResultUpdateWithin() {
        return resultUpdateWithin;
    }

    public void setResultUpdateWithin(String resultUpdateWithin) {
        this.resultUpdateWithin = resultUpdateWithin;
    }

    public String getResultBiddingWithin() {
        return resultBiddingWithin;
    }

    public void setResultBiddingWithin(String resultBiddingWithin) {
        this.resultBiddingWithin = resultBiddingWithin;
        Log.d("resultBW", "resultBW" + resultBiddingWithin);
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }
}
