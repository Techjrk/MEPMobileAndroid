package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.ProjectStage;
import com.lecet.app.data.models.SearchFilterJurisdictionDistrictCouncil;
import com.lecet.app.data.models.SearchFilterJurisdictionLocal;
import com.lecet.app.data.models.SearchFilterJurisdictionMain;
import com.lecet.app.data.models.SearchFilterProjectTypesMain;
import com.lecet.app.data.models.SearchFilterProjectTypesProjectCategory;
import com.lecet.app.databinding.ActivitySearchFiltersAllRefineBinding;
import com.lecet.app.databinding.ActivitySearchFiltersAllTabbedBinding;
import com.lecet.app.viewmodel.SearchFilterBiddingWithinViewModel;
import com.lecet.app.viewmodel.SearchFilterJurisdictionViewModel;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;
import com.lecet.app.viewmodel.SearchFilterStageViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.lecet.app.viewmodel.SearchViewModel.FILTER_INSTANT_SEARCH;

public class SearchFilterMPSActivity extends AppCompatActivity {
    SearchFilterMPFViewModel viewModel;
    boolean instantSearch;
    int MAXCHARFIELD = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new SearchFilterMPFViewModel(this);
        Intent i = getIntent();
        instantSearch = i.getBooleanExtra(FILTER_INSTANT_SEARCH, true);
        //Log.d("SearchFilterMPSActivity", "onCreate: instantSearch: " + instantSearch);

        // if coming from Search Activity when no text has been input or Saved Search used, use the tabbed Projects/Companies filter layout
        if (instantSearch) {
            ActivitySearchFiltersAllTabbedBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filters_all_tabbed);       //Projects/Companies
            sfilter.setViewModel(viewModel);
        }

        // if coming from Search Activity in a state where search text is entered (via user input or a Saved Search), use the non-tabbed Refine Search filter layout
        else {
            ActivitySearchFiltersAllRefineBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filters_all_refine);     //Refine Search
            sfilter.setViewModel(viewModel);
        }
    }

    /**
     * Get data back from user input from a Filter Activity
     * and put that data into the View Model
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        Bundle bundle = data.getBundleExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE);      //TODO - handle case of no Bundle for Activities which pass it
        String[] extrasArr = null;

        if (bundle == null) {
            extrasArr = data.getStringArrayExtra(SearchViewModel.FILTER_EXTRA_DATA);
        }

        // Activities which return result in String[] format
        if (resultCode == RESULT_OK) {
            if (extrasArr != null) {
                switch (requestCode) {

                    // Location
                    case SearchFilterMPFViewModel.LOCATION:
                        processLocation(extrasArr);
                        break;

                    // Dollar Value
                    case SearchFilterMPFViewModel.VALUE:
                        processValue(extrasArr);
                        break;

                    // Updated Within
                    case SearchFilterMPFViewModel.UPDATED_WITHIN:
                        processUpdatedWithin(extrasArr);
                        break;

                    // Building / Highway
                    case SearchFilterMPFViewModel.BH:
                        processBuildingOrHighway(extrasArr);
                        break;

                    // Owner Type
                    case SearchFilterMPFViewModel.OWNER_TYPE:
                        processOwnerType(extrasArr);
                        break;

                    // Work Type
                    case SearchFilterMPFViewModel.WORK_TYPE:
                        processWorkType(extrasArr);
                        break;

                    default:
                        Log.w("SearchFilterMPSAct", "onActivityResult: WARNING: no case for request code " + requestCode);
                        break;
                }
            }

            // Activities which return result in Bundle format
            else if (bundle != null) {
                switch (requestCode) {

                    // Project Type
                    case SearchFilterMPFViewModel.TYPE:
                        processProjectType(bundle);
                        break;

                    // Jurisdiction
                    case SearchFilterMPFViewModel.JURISDICTION:
                        processJurisdiction(bundle);
                        break;

                    // Project Stage
                    case SearchFilterMPFViewModel.STAGE:
                        processStage(bundle);
                        break;

                    // Bidding Within
                    case SearchFilterMPFViewModel.BIDDING_WITHIN:
                        processBiddingWithin(bundle);
                        break;

                    default:
                        Log.w("SearchFilterMPSAct", "onActivityResult: WARNING: no case for request code " + requestCode);
                        break;
                }
            }
        }
    }

    /**
     * Process the Location input data
     */
    private void processLocation(String[] arr) {

        String city = "";
        String state = "";
        String county = "";
        String zip = "";

        // initial vars from raw data
        city = arr[0];
        state = arr[1];
        county = arr[2];
        zip = arr[3];

        // comma-separated constructed String for UI display
        String cityStr = city;
        String stateStr = !state.equals("") ? (!city.equals("") ? "," + state : " " + state) : "";       //TODO - any validation here? compare iOS version
        String countyStr = !county.equals("") ? (!state.equals("") ? "," + county : " " + county) : "";
        String zipStr = !zip.equals("") ? (!zip.equals("") ? "," + zip : " " + zip) : "";

        String locationText = cityStr + stateStr + countyStr + zipStr;


        if (instantSearch && !viewModel.getIsProjectViewVisible()) {
            viewModel.setClocationSelect(locationText);
        } else {
            viewModel.setPersistedLocationCity(city);   //mark
            viewModel.setPersistedLocationState(state);
            viewModel.setPersistedLocationCounty(county);
            viewModel.setPersistedLocationZip(zip);
            viewModel.setLocation_select(locationText);
        }
        Log.d("SearchFilterMPSAct", "location: " + locationText);


        // StringBuilder used for generating query with commas as appropriate
        StringBuilder sb = new StringBuilder();
        String projectLocation = "";

        if (city.length() > 0) {
            sb.append("\"city\":\"" + city + "\"");
        }
        if (state.length() > 0) {
            if (sb.length() > 0) sb.append(",");
            sb.append("\"state\":\"" + state + "\"");
        }
        if (county.length() > 0) {
            if (sb.length() > 0) sb.append(",");
            sb.append("\"county\":\"" + county + "\"");
        }
        if (zip.length() > 0) {
            if (sb.length() > 0) sb.append(",");
            sb.append("\"zip5\":\"" + zip + "\"");
        }

        projectLocation = sb.toString();

        if (!projectLocation.trim().equals("")) {
            projectLocation = "\"projectLocation\":{" + projectLocation + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_LOCATION, projectLocation);  // this should work whether or not projectLocation is empty
    }

    /**
     * Process the Primary Project Type input data
     * TODO: Use in conjunction with processProjectTypeId() ?
     */
    /*private void processPrimaryProjectType(String[] arr) {
        String typeStr = arr[0];
        String projectType = "";
        viewModel.setType_select(typeStr);
        if (typeStr != null && !typeStr.trim().equals("")) {
            projectType = "\"primaryProjectType\":{" + typeStr + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_TYPE, projectType);
    }*/

    /**
     * Process the Project Type Bundle extra data
     */
    private void processProjectType(final Bundle bundle) {

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<SearchFilterProjectTypesMain> realmTypes;
                realmTypes = realm.where(SearchFilterProjectTypesMain.class).findAll();
                String displayStr = ""; // = "\r\n"; //bundle[0];   // text display     //removed line break as it was unnecessary
                //    String typeId = bundle[1];   // ID
                String types = "";
                Set<Integer> idSet = new HashSet<Integer>();        // using a Set to prevent dupes
                List<Integer> idList;

                //TODO - VALIDITY INCOMPLETE. The final ID list should be all 500-level IDs, meaning only Primary Project Type IDs. Currently that works only if the top-level category is selected.

                // add each parent type ID
                for (String key : bundle.keySet()) {

                    Object value = bundle.get(key);
                    Log.d("processProjectType", key + "=" + value.toString());
                    displayStr += value + ", ";

                    // add each child Type ID (District Council)
                    for (SearchFilterProjectTypesMain mainType : realmTypes) {
                        if (value.equals(mainType.getTitle())) {
                            //idSet.add(Integer.valueOf(mainType.getId()));

                            // add each grandchild Type ID (Primary Type)
                            for (SearchFilterProjectTypesProjectCategory category : mainType.getProjectCategories()) {
                                if (category != null) {
                                    //idSet.add(Integer.valueOf(category.getId()));
                                    //displayStr += category.getTitle() + ", ";

                                    for (PrimaryProjectType primaryType : category.getProjectTypes()) {
                                        if (primaryType != null) {
                                            idSet.add(Integer.valueOf(primaryType.getId()));
                                            //displayStr += primaryType.getTitle() + ", ";
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }

                if (displayStr.length() > 2) {
                    displayStr = displayStr.substring(0, displayStr.length() - 2);         //trim trailing ", "
                }
                idList = new ArrayList<>(idSet);
                Log.d("processProjectType", "displayStr: " + displayStr);
                Log.d("processProjectType", "ids: " + idList);

                if (displayStr != null && displayStr.length() > MAXCHARFIELD)
                    displayStr = "\r\n" + displayStr;
                if (instantSearch && !viewModel.getIsProjectViewVisible()) {
                    if (displayStr == null || displayStr.equals(""))
                        displayStr = "Any";  //default value in Companies project type field
                    viewModel.setCtypeSelect(displayStr);
                } else {
                    viewModel.setPersistedProjectTypeId(displayStr);
                    viewModel.setType_select(displayStr);
                }

                types = "\"projectTypeId\":{\"inq\":" + idList + "}";

                viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_TYPE, types);
            }
        });
    }


    /**
     * Process the dollar Value from input data
     */
    private void processValue(String[] arr) {
        String min = arr[0];                          // int for query
        String max = arr[1];                          // int for query
        String valueStr = "$" + min + " - $" + max;   // text for display
        String projectValue = "";

        if (valueStr != null && !valueStr.trim().equals("")) {
            projectValue = "\"projectValue\":{" + "\"min\":" + min + ",\"max\":" + max + "}";
        }
        if (min == null || max == null) {
            valueStr = "";
        }

        if (instantSearch && !viewModel.getIsProjectViewVisible()) {
            viewModel.setCvalueSelect(valueStr);
        } else {
            viewModel.setPersistedValueMin(min);
            viewModel.setPersistedValueMax(max);
            viewModel.setValue_select(valueStr);
        }

        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_VALUE, projectValue);
    }

    /**
     * Process the Updated Within input data
     */
    private void processUpdatedWithin(String[] arr) {
        String updatedWithinStr = arr[0];   // text for display
        String updatedWithinInt = arr[1];   // int for query
        String[] updatedWithinArr = {updatedWithinStr, updatedWithinInt};
        String projectUpdatedWithin = "";
        viewModel.setPersistedUpdatedWithin(updatedWithinStr);
        viewModel.setUpdated_within_select(updatedWithinStr);
        if (updatedWithinStr != null && !updatedWithinStr.trim().equals("")) {
            projectUpdatedWithin = "\"updatedInLast\":" + updatedWithinInt;
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_UPDATED_IN_LAST, projectUpdatedWithin);
    }

    /**
     * Process the Jurisdiction input data
     * TODO - jurisdiction search may require "jurisdiction":true as well as "jurisdictions":{"inq":[array]} and "deepJurisdictionId":[ids]
     * TODO - also we need to map input to jurisdiction codes
     * ex:
     * "jurisdiction": true
     * "jurisdictions": { "inq": [3] }
     * "deepJurisdictionId": ["3-3_2-1_1"]
     */
    private void processJurisdiction(final Bundle bundle) {          //TODO - NO LONGER VALID

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<SearchFilterJurisdictionMain> realmJurisdictions;
                realmJurisdictions = realm.where(SearchFilterJurisdictionMain.class).findAll();
                int jurisdictionViewType = -1;
                String jurisdictionId = null;
                String jurisdictionRegionId = null;
                String jurisdictionName = null;
                String jurisdictions = "";

                try {
                    jurisdictionViewType = Integer.valueOf(bundle.getString(SearchFilterJurisdictionViewModel.BUNDLE_KEY_VIEW_TYPE));
                    jurisdictionId = bundle.getString(SearchFilterJurisdictionViewModel.BUNDLE_KEY_ID);
                    jurisdictionRegionId = bundle.getString(SearchFilterJurisdictionViewModel.BUNDLE_KEY_REGION_ID);
                    jurisdictionName = bundle.getString(SearchFilterJurisdictionViewModel.BUNDLE_KEY_NAME);  //TODO: ABBREVIATION AND LONGNAME ARE ALSO AVAILABLE. USEFUL?
                } catch (Exception e) {
                    Log.e("processJurisdiction: ", "Error parsing bundle.");
                }
                if (jurisdictionName == null || jurisdictionName.equals("")) {
                    jurisdictionName = "Any";
                } else if (jurisdictionName.length() > MAXCHARFIELD)
                    jurisdictionName = "\r\n" + jurisdictionName;

                if (instantSearch && !viewModel.getIsProjectViewVisible()) {
                    viewModel.setCjurisdictionSelect(jurisdictionName);
                } else {
                    viewModel.setPersistedJurisdiction(jurisdictionName);
                    viewModel.setJurisdiction_select(jurisdictionName);
                }

                // Build a single-element List based on the Jurisdiction ID passed in the Bundle.
                List<String> jList = new ArrayList<>();

                List<SearchFilterJurisdictionDistrictCouncil> districtCouncils;
                List<SearchFilterJurisdictionLocal> locals;

                if (jurisdictionName != null && !jurisdictionName.trim().equals("")) {

                    Log.d("SearchFilterMPSAct", "processJurisdiction: input Jurisdiction id: " + jurisdictionId);
                    Log.d("SearchFilterMPSAct", "processJurisdiction: input Jurisdiction name: " + jurisdictionName);
                    Log.d("SearchFilterMPSAct", "processJurisdiction: list: " + jList);

                    String js = jList.toString();
                    jurisdictions = "\"jurisdictions\":{\"inq\":" + js + "}";
                }
                viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_JURISDICTION, jurisdictions);
            }
        });

    }


    /**
     * Process the Stage input data based on the received list of Stages persisted in Realm
     */
    private void processStage(final Bundle bundle) {

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ProjectStage> realmStages;
                realmStages = realm.where(ProjectStage.class).equalTo("parentId", 0).findAll();     // parentId = 0 should be all parent ProjectStages, which each contain a list of child ProjectStages
                Log.d("processStage: ", "realmStages size: " + realmStages.size());
                Log.d("processStage: ", "realmStages: " + realmStages);

                String viewType = bundle.getString(SearchFilterStageViewModel.BUNDLE_KEY_VIEW_TYPE);  // view type (parent, child, grandchild)
                String stageStr = bundle.getString(SearchFilterStageViewModel.BUNDLE_KEY_NAME);       // text display
                String stageId = bundle.getString(SearchFilterStageViewModel.BUNDLE_KEY_ID);         // ID                   //TODO - use this ID for name/id lookup rather than name?
                String stages = "";
                if (stageStr == null || stageStr.equals("")) stageStr = "Any";
                viewModel.setPersistedStage(stageStr);
                viewModel.setStage_select(stageStr);

                // build the list of IDs for the query, which include the parent ID and any of its child IDs
                List<String> sList = new ArrayList<>();
                sList.add(stageId);
                if (stageStr != null && !stageStr.trim().equals("")) {

                    Log.d("SearchFilterMPSAct", "processStage: input Stage name: " + stageStr);
                    Log.d("SearchFilterMPSAct", "processStage: parent and child Stage IDs: " + sList);

                    stages = "\"projectStageId\":{\"inq\":" + sList.toString() + "}";
                }
                viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_STAGE, stages);
            }
        });
    }

    /**
     * Process the Bidding Within input data, which is an int, # of days
     */
    private void processBiddingWithin(final Bundle bundle) {
        String biddingWithinStr = bundle.getString(SearchFilterBiddingWithinViewModel.BUNDLE_KEY_DISPLAY_STR); // text for display
        String biddingWithinInt = bundle.getString(SearchFilterBiddingWithinViewModel.BUNDLE_KEY_DAYS_INT);    // int for query
        String projectBiddingWithin = "";
        if (instantSearch && !viewModel.getIsProjectViewVisible()) {
            viewModel.setCbiddingWithinSelect(biddingWithinStr);
        } else {
            viewModel.setPersistedBiddingWithin(biddingWithinInt);
            viewModel.setBidding_within_select(biddingWithinStr);
        }

        if (biddingWithinInt != null && !biddingWithinInt.trim().equals("")) {
            projectBiddingWithin = "\"biddingInNext\":" + biddingWithinInt;
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_BIDDING_WITHIN, projectBiddingWithin);
    }

    /**
     * Process the Building-or-Highway input data, which is an array of one or two elements
     */
    private void processBuildingOrHighway(String[] arr) {

        final String BUILDING      = getApplicationContext().getResources().getString(R.string.building);
        final String HEAVY_HIGHWAY = getApplicationContext().getResources().getString(R.string.heavy_highway);

        String bhDisplayStr = arr[0];      // could come in as "Both", "Any", "Building" or "Heavy-Highway", to be converted to array ["B"] or ["H"] or ["B","H"]
        String bhChar = arr[1];
        viewModel.setPersistedBuildingOrHighway(arr);
        viewModel.setBh_select(bhDisplayStr);
        if (bhDisplayStr != null && !bhDisplayStr.trim().equals("")) {
            List<String> bhList = new ArrayList<>();
            if (bhDisplayStr.equals(BUILDING)) bhList.add("\"B\"");
            else if (bhDisplayStr.equals(HEAVY_HIGHWAY)) bhList.add("\"H\"");
            else {
                bhList.add("\"B\"");
                bhList.add("\"H\"");
            }
            bhChar = "\"buildingOrHighway\":{\"inq\":" + bhList.toString() + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_BUILDING_OR_HIGHWAY, bhChar);
    }

    /**
     * Process the Owner Type input data, which is a String array with one element
     * TODO - make sure UI only allows single selection, as per iOS
     */
    private void processOwnerType(String[] arr) {
        String ownerTypeStr = arr[0];
        String ownerType = "";
        if (ownerTypeStr == null || ownerTypeStr.equals("")) {
            ownerTypeStr = "Any";
        }
        viewModel.setPersistedOwnerType(ownerTypeStr);
        viewModel.setOwner_type_select(ownerTypeStr);
        if (ownerTypeStr != null && !ownerTypeStr.trim().equals("")) {
            List<String> oList = new ArrayList<>();
            oList.add("\"" + ownerTypeStr + "\"");
            ownerType = "\"ownerType\":{\"inq\":" + oList.toString() + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_OWNER_TYPE, ownerType);
    }

    /**
     * Process the Work Type input data, which is a String array with one element
     * TODO - make sure UI only allows single selection, as per iOS
     * TODO - work types need to be mapped to integer IDs
     */
    private void processWorkType(String[] arr) {
        String workTypeStr = arr[0];
        String workType = "";
        if (workTypeStr == null || workTypeStr.equals("")) {
            workTypeStr = "Any";
        }
        viewModel.setPersistedWorkType(workTypeStr);
        viewModel.setWork_type_select(workTypeStr);
        if (workTypeStr != null && !workTypeStr.trim().equals("")) {
            List<String> wList = new ArrayList<>();
            wList.add("\"" + workTypeStr + "\"");
            workType = "\"workTypeId\":{\"inq\":" + wList.toString() + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_WORK_TYPE, workType);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //store the search filter result data to be used by the Search activity. 
        viewModel.saveResult();
    }
}
