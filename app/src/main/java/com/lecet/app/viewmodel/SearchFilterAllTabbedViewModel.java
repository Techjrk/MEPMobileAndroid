package com.lecet.app.viewmodel;

import android.app.Activity;
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
import com.lecet.app.data.models.UserFilterExtra;
import com.lecet.app.data.models.UserFilterSelect;
import com.lecet.app.utility.Log;

import io.realm.Realm;
import io.realm.RealmList;

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
    private Realm realm;

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

    /**
     * Note: This variable is used for viewing and hiding the search location field.
     * If the user is in the Project Near Me section, this search location field is hidden. If not, it is visible.
     */
    private boolean usingProjectNearMe;
    private UserFilterSelect saveFilter;
    private Bundle typeData, jurisdictionData, stageData;

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

        realm = Realm.getDefaultInstance();

        intent = activity.getIntent();
        setUsingProjectNearMe(intent.getBooleanExtra(activity.getString(R.string.using_project_near_me), false));
        saveFilter = realm.where(UserFilterSelect.class).findFirst();
        if (saveFilter != null) {
            getSavedFilterFromRealm();
            typeData = getTypeDataFromRealmList();
            jurisdictionData = getJurisdictionDataFromRealmList();
            stageData = getStageDataFromRealmList();
        } else {
            Log.d(TAG, "constructor: saveFilter is null");
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

    public Bundle getTypeDataFromRealmList() {
        Bundle b = null;
        if (saveFilter != null) {
            Log.d(TAG, "getTypeDataFromRealmList: saveFilter is NOT null");
            b = new Bundle();
            RealmList<UserFilterExtra> uList = saveFilter.getTypeKey();
            for (UserFilterExtra keyID : uList) {
                b.putString(keyID.getName(), keyID.getValue());
                Log.d(TAG, "getTypeDataFromRealmList: " + keyID.getName() + ":" + keyID.getValue());
            }
        } else {
            Log.d(TAG, "getTypeDataFromRealmList: saveFilter is null");
        }
        return b;
    }

    public Bundle getJurisdictionDataFromRealmList() {
        Bundle b = null;
        if (saveFilter != null) {
            Log.d(TAG, "getJurisdictionDataFromRealmList: saveFilter jurisdiction is NOT null");
            b = new Bundle();
            RealmList<UserFilterExtra> uList = saveFilter.getJurisdictionKey();
            if (uList == null || uList.size() == 0) return null;
            for (UserFilterExtra keyID : uList) {
                b.putString(keyID.getName(), keyID.getValue());
                Log.d(TAG, "getJurisdictionDataFromRealmList: " + keyID.getName() + ":" + keyID.getValue());
            }
        } else {
            Log.d(TAG, "getJurisdictionDataFromRealmList: saveFilter jurisdiction is null");
        }
        return b;
    }

    public Bundle getStageDataFromRealmList() {
        Bundle b = null;
        if (saveFilter != null) {
            Log.d(TAG, "getStageDataFromRealmList:: saveFilter stage is NOT null");
            RealmList<UserFilterExtra> uList = saveFilter.getStageKey();
            if (uList == null || uList.size() == 0) return null;
            b = new Bundle();
            for (UserFilterExtra uname : uList) {
                Bundle b2 = new Bundle();
                b2.putString(SearchFilterStageViewModel.BUNDLE_KEY_ID, uname.getName());
                b2.putString(SearchFilterStageViewModel.BUNDLE_KEY_NAME, uname.getValue());
                b2.putString(SearchFilterStageViewModel.BUNDLE_KEY_VIEW_TYPE, uname.getValueStage());
                b.putBundle(uname.getName(), b2);
                Log.d(TAG, "getStageDataFromRealmList: " + uname.getName() + ":" + uname.getValue());
            }
        } else {
            Log.d(TAG, "getStageDataFromRealmList: saveFilter stage is null");
        }

        return b;
    }

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
        Intent i;
        id = view.getId();
        int section;
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
                i.putExtra(activity.getString(R.string.FilterTypeData), typeData);
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
                i.putExtra(activity.getString(R.string.FilterJurisdictionData), jurisdictionData);
                break;

            case R.id.stage:
                section = STAGE;
                i = new Intent(activity, SearchFilterStageActivity.class);
                i.putExtra(SearchFilterAllTabbedViewModel.EXTRA_STAGE, getPersistedStage());
                i.putExtra(activity.getString(R.string.FilterStageData), stageData);
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
            Log.d(TAG, "onClickedProjectCompanyTab: project tab clicked");
            SearchViewModel.companyInstantSearch = false;
        } else {
            Log.d(TAG, "onClickedProjectCompanyTab: company tab clicked");
            //Focus in company. Search for the Company.
            SearchViewModel.companyInstantSearch = true;
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
    void getSavedFilterFromRealm() {

        /** Note: For displaying the project filter values to the project search field and project instant search tab fields (for instansearch)
         *  and for extracting the filter data needed for processing.
         */

        setLocation_select(saveFilter.getLocationSelect());
        setType_select(saveFilter.getTypeIdSelect());
        setPersistProjectTypeIdInt(saveFilter.getTypeIdInt());

        setUpdated_within_select(saveFilter.getUpdatedWithinSelect());
        setPersistedUpdatedWithin(saveFilter.getUpdatedWithin());
        Log.d(TAG, "getPref: updatedwithin select: " + saveFilter.getUpdatedWithinSelect());
        Log.d(TAG, "getPref: updatedwithin: " + saveFilter.getUpdatedWithin());

        setJurisdiction_select(saveFilter.getJurisdictionSelect());
        setStage_select(saveFilter.getStageSelect());
        setBidding_within_select(saveFilter.getBiddingWithinSelect());
        setPersistedBiddingWithin(saveFilter.getBiddingWithin());

        setBh_select(saveFilter.getBhSelect());

        if (persistedBuildingOrHighway == null ) persistedBuildingOrHighway = new String[2];
        persistedBuildingOrHighway[0] = saveFilter.getBhStr();
        persistedBuildingOrHighway[1] = saveFilter.getBh();

        setOwner_type_select(saveFilter.getOwnerTypeSelect());
        setPersistedOwnerType(saveFilter.getOwnerType());

        setWork_type_select(saveFilter.getWorkTypeSelect());
        setPersistedWorkType(saveFilter.getWorkType());

        setValue_select(saveFilter.getValueSelect());

        //Note: For displaying the company filter values to the company instant search tab fields and for extracting the filter data needed for processing.
        setClocationSelect(saveFilter.getcLocationSelect());
        setCvalueSelect(saveFilter.getcValueSelect());
        setCjurisdictionSelect(saveFilter.getcJurisdictionSelect());
        setCbiddingWithinSelect(saveFilter.getcBiddingWithinSelect());
        setCtypeSelect(saveFilter.getcTypeSelect());

        //Note: For Resultset of the url filter parameter values that will be passed to the web api.
        setResultProjectType(saveFilter.getTypeResult());
        setResultValue(saveFilter.getValueResult());
        setResultUpdateWithin(saveFilter.getUpdatedWithinResult());
        setResultJurisdiction(saveFilter.getJurisdictionResult());
        setResultStage(saveFilter.getStageResult());
        setResultBiddingWithin(saveFilter.getBiddingWithinResult());
        setResultBH(saveFilter.getBhResult());
        setResultOwnerType(saveFilter.getOwnerTypeResult());
        setResultWorkType(saveFilter.getWorkTypeResult());

        //Then set all the previous result to intent
        setAllFilterResults();
    }

    /**
     * Note: Used for setting all the previous url filter parameters extracted from the sharedpreferences
     * and place this filters to the intent where it will be used in  processing the search filter for web api in the MPSActivity class.
     */
    void setAllFilterResults() {
        setFilterResult(SearchViewModel.FILTER_PROJECT_UPDATED_IN_LAST, getResultUpdateWithin());
        setFilterResult(SearchViewModel.FILTER_PROJECT_BIDDING_WITHIN, getResultBiddingWithin());
        setFilterResult(SearchViewModel.FILTER_PROJECT_VALUE, getResultValue());
        setFilterResult(SearchViewModel.FILTER_PROJECT_TYPE, getResultProjectType());
        setFilterResult(SearchViewModel.FILTER_PROJECT_JURISDICTION, getResultJurisdiction());
        setFilterResult(SearchViewModel.FILTER_PROJECT_STAGE, getResultStage());
        setFilterResult(SearchViewModel.FILTER_PROJECT_WORK_TYPE, getResultWorkType());
        setFilterResult(SearchViewModel.FILTER_PROJECT_OWNER_TYPE, getResultOwnerType());
        setFilterResult(SearchViewModel.FILTER_PROJECT_BUILDING_OR_HIGHWAY, getResultBH());

    }

    // Note: Used for setting the extracted filter value with corresponding filter key name to the Intent object.
    void setFilterResult(String key, String value) {
        if (value != null && !value.isEmpty()) setSearchFilterResult(key, value);
        else setSearchFilterResult(key, "");
    }


    // Note: Used to save all previous selected filter items to the sharedpreferences named Filter.
    public void saveFilterToRealm() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                saveFilter = new UserFilterSelect();

                saveFilter.setLocationSelect(getLocation_select());
                saveFilter.setTypeIdSelect(getType_select());
                saveFilter.setTypeIdInt(getPersistProjectTypeIdInt());

                saveFilter.setValueSelect(getValue_select());
                saveFilter.setValueMin(persistedValueMin);
                saveFilter.setValueMax(persistedValueMax);

                saveFilter.setUpdatedWithinSelect(getUpdated_within_select());
                saveFilter.setUpdatedWithin(getPersistedUpdatedWithin());
                Log.d(TAG, "getPrefsave: updatedwithin select: " + saveFilter.getUpdatedWithinSelect());
                Log.d(TAG, "getPrefsave: updatedwithin: " + saveFilter.getUpdatedWithin());

                saveFilter.setJurisdictionSelect(getJurisdiction_select());
                saveFilter.setStageSelect(getStage_select());

                saveFilter.setBiddingWithinSelect(getBidding_within_select());
                saveFilter.setBiddingWithin(getPersistedBiddingWithin());

                saveFilter.setBhSelect(getBh_select());

                if (getPersistedBuildingOrHighway() != null && getPersistedBuildingOrHighway().length > 0) {
                    saveFilter.setBhStr(getPersistedBuildingOrHighway()[0]);
                    saveFilter.setBh(getPersistedBuildingOrHighway()[1]);
                }

                saveFilter.setOwnerTypeSelect(getOwner_type_select());
                saveFilter.setOwnerType(getPersistedOwnerType());
                saveFilter.setWorkTypeSelect(getWork_type_select());
                saveFilter.setWorkType(getPersistedWorkType());

                saveFilter.setcLocationSelect(getClocationSelect());
                saveFilter.setcValueSelect(getCvalueSelect());
                saveFilter.setcJurisdictionSelect(getCjurisdictionSelect());
                saveFilter.setcBiddingWithinSelect(getCbiddingWithinSelect());
                saveFilter.setcTypeSelect(getCtypeSelect());

                //Saving the Project/Company Filter Final Result values for Web api that could be used later

                saveFilter.setTypeResult(getResultProjectType());
                saveFilter.setJurisdictionResult(getResultJurisdiction());
                saveFilter.setStageResult(getResultStage());
                saveFilter.setBhResult(getResultBH());
                saveFilter.setOwnerTypeResult(getResultOwnerType());
                saveFilter.setWorkTypeResult(getResultWorkType());
                saveFilter.setUpdatedWithinResult(getResultUpdateWithin());
                saveFilter.setBiddingWithinResult(getResultBiddingWithin());
                saveFilter.setValueResult(getResultValue());

                //Note: For setting the Project Type list for realm
                if (typeData != null) {
                    RealmList<UserFilterExtra> userList = new RealmList<UserFilterExtra>();
                    for (String key : typeData.keySet()) {
                        UserFilterExtra uname = new UserFilterExtra();
                        uname.setName(key);
                        uname.setValue(typeData.getString(key));
                        userList.add(uname);
                    }
                    saveFilter.setTypeKey(userList);
                }

                //Note: For Jurisdiction list
                if (jurisdictionData != null) {
                    RealmList<UserFilterExtra> userList = new RealmList<UserFilterExtra>();
                    for (String key : jurisdictionData.keySet()) {
                        UserFilterExtra uname = new UserFilterExtra();
                        uname.setName(key);
                        uname.setValue(jurisdictionData.getString(key));
                        userList.add(uname);
                    }
                    saveFilter.setJurisdictionKey(userList);
                }

                //Note: For setting the Stage list to realm
                if (stageData != null) {
                    RealmList<UserFilterExtra> userList = new RealmList<UserFilterExtra>();
                    for (String key : stageData.keySet()) {
                        Bundle b = stageData.getBundle(key);
                        if (b == null) continue;

                        UserFilterExtra uname = new UserFilterExtra();
                        uname.setName(key);
                        uname.setValue(b.getString(SearchFilterStageViewModel.BUNDLE_KEY_NAME));
                        uname.setValueStage(b.getString(SearchFilterStageViewModel.BUNDLE_KEY_VIEW_TYPE));
                        userList.add(uname);
                    }
                    saveFilter.setStageKey(userList);
                }
                //Note: Save all user selected filters to realm
                realm.copyToRealmOrUpdate(saveFilter);
            }

        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "saveFilterToRealm: filter saved success");
            }
        }, new Realm.Transaction.OnError() {

            @Override
            public void onError(Throwable error) {
                Log.e(TAG, "saveFilterToRealm: filter saved error" + error.getMessage());
            }
        });

    }

    /**
     * Note: This method is used for saving the special filters data (Project types and Jurisdiction) to the sharedPreferences
     *
     * @param filterDataName - used as a filter key name for saving to sharedPreferences.
     * @param bundle         - hold the filter data to be saved to the sharedPreferences.
     */

    public void saveExtraFilterData(final String filterDataName, final Bundle bundle) {
        Log.d(TAG, "saveExtraFilterData: filter name: " + filterDataName);

        if (filterDataName == null) return;
        if (filterDataName.equalsIgnoreCase(activity.getString(R.string.FilterTypeData)))
            this.typeData = bundle;

        if (filterDataName.equalsIgnoreCase(activity.getString(R.string.FilterJurisdictionData)))
            this.jurisdictionData = bundle;

        if (filterDataName.equalsIgnoreCase(activity.getString(R.string.FilterStageData)))
            this.stageData = bundle;
    }


    /**
     * Note: The setter/getter methods for resultxxx.
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
        Log.d(TAG, "setResultBiddingWithin: result value of BiddingWithn: " + resultBiddingWithin);
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }
}
