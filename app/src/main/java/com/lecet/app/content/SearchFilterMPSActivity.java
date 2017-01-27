package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.adapters.SearchFilterJurisdictionAdapter;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.ProjectStage;
import com.lecet.app.data.models.SearchFilterJurisdictionDistrictCouncil;
import com.lecet.app.data.models.SearchFilterJurisdictionLocal;
import com.lecet.app.data.models.SearchFilterJurisdictionMain;
import com.lecet.app.data.models.SearchFilterProjectTypesMain;
import com.lecet.app.data.models.SearchFilterProjectTypesProjectCategory;
import com.lecet.app.databinding.ActivitySearchFilterMps30Binding;
import com.lecet.app.databinding.ActivitySearchFilterMseBinding;
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

public class SearchFilterMPSActivity extends AppCompatActivity {
    SearchFilterMPFViewModel viewModel;
    boolean instantSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //     setContentView(R.layout.activity_search_filter_mse);
        // setContentView(R.layout.activity_search_filter_mps30);
        viewModel = new SearchFilterMPFViewModel(this);
        Intent i = getIntent();
        instantSearch = i.getBooleanExtra("instantSearch", true);
        if (instantSearch) {
            ActivitySearchFilterMseBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mse);
            sfilter.setViewModel(viewModel);
        } else {
            ActivitySearchFilterMps30Binding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filter_mps30);
            sfilter.setViewModel(viewModel);
        }
    }

    /**
     * Get data back from user input from a Filter Activity
     * and put that data into the View Model
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;   //TODO - handle case of no String[] data for Activities which pass it

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

                    // Bidding Within
                    case SearchFilterMPFViewModel.BIDDING_WITHIN:
                        processBiddingWithin(extrasArr);
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
                //Log.d("processProjectType", "realmTypes size: " + realmTypes.size());
                //Log.d("processProjectType", "realmTypes: " + realmTypes);

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

                                    for(PrimaryProjectType primaryType : category.getProjectTypes()) {
                                        if(primaryType != null) {
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

                if(displayStr.length()> 2) {
                    displayStr = displayStr.substring(0, displayStr.length() - 2);         //trim trailing ", "
                }
                idList = new ArrayList<>(idSet);
                Log.d("processProjectType", "displayStr: " + displayStr);
                Log.d("processProjectType", "ids: " + idList);

                if (instantSearch && !viewModel.getIsProjectViewVisible()) {
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
     * Process the Project Type Id code based on input data from list
     */
    /*private void processProjectTypeId(final String[] arr) {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ProjectType> realmTypes;
                realmTypes = realm.where(ProjectType.class).equalTo("parentId", 0).findAll();     // parentId = 0 should be all parent ProjectTypes, which each contain a list of child ProjectTypes
                Log.d("processProjectTypeId: ", "realmTypes size: " + realmTypes.size());
                Log.d("processProjectTypeId: ", "realmTypes: " + realmTypes);

                String typeStr = arr[0];   // text display
                String typeId = arr[1];   // ID
                String types = "";


                // build the list of IDs for the query, which include the parent ID and any of its child IDs
                List<String> tList = new ArrayList<>();
                tList.add(typeId);
                if (typeStr != null && !typeStr.trim().equals("")) {
                    // add each child Type ID
                    for (ProjectType parentType : realmTypes) {
                        if (typeStr.equals(parentType.getName())) {
                            List<ProjectType> childTypes = parentType.getChildTypes();
                            for (ProjectType childType : childTypes) {
                                if (childType != null) {
                                    tList.add(Long.toString(childType.getId()));
                                }
                            }
                            break;
                        }
                    }
                    if (instantSearch && !viewModel.getIsProjectViewVisible()) {
                        viewModel.setCtypeSelect(typeStr);
                    } else {
                        viewModel.setPersistedProjectTypeId(typeStr);
                        viewModel.setType_select(typeStr);
                    }
                    Log.d("SearchFilterMPSAct", "processType: input Type name: " + typeStr);
                    Log.d("SearchFilterMPSAct", "processType: parent and child Type IDs: " + tList);

//                    String ids = idList.toString();
//                    projectTypeId = "\"projectTypeId\":{\"inq\":" + ids + "}";         // square brackets [ ] come for free when the list is converted to a String


                    types = "\"projectTypeId\":{\"inq\":" + tList.toString() + "}";
                }
                viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_TYPE, types);
            }
        });
    }*/

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
        viewModel.setPersistedUpdatedWithin(updatedWithinArr);
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
                Log.d("processJurisdiction: ", "realmJurisdictions size: " + realmJurisdictions.size());
                Log.d("processJurisdiction: ", "realmJurisdictions: " + realmJurisdictions);

                int jurisdictionViewType = -1;
                String jurisdictionId = null;
                String jurisdictionName = null;
                String jurisdictions = "";

                try {
                    jurisdictionViewType = Integer.valueOf(bundle.getString(SearchFilterJurisdictionViewModel.BUNDLE_KEY_VIEW_TYPE));
                    jurisdictionId       = bundle.getString(SearchFilterJurisdictionViewModel.BUNDLE_KEY_ID);
                    jurisdictionName     = bundle.getString(SearchFilterJurisdictionViewModel.BUNDLE_KEY_NAME);  //TODO: ABBREVIATION AND LONGNAME ARE ALSO AVAILABLE. USEFUL?
                }
                catch (Exception e) {
                    Log.e("processJurisdiction: ", "Error parsing bundle.");
                }

                if (instantSearch && !viewModel.getIsProjectViewVisible()) {
                    viewModel.setCjurisdictionSelect(jurisdictionName);
                } else {
                    viewModel.setPersistedJurisdiction(jurisdictionName);
                    viewModel.setJurisdiction_select(jurisdictionName);
                }

                // build the list of IDs for the query, which include ... ?
                List<String> jList = new ArrayList<>();

                List<SearchFilterJurisdictionDistrictCouncil> districtCouncils;
                List<SearchFilterJurisdictionLocal> locals;

                // add the highest-level Jurisdiction ID
                jList.add(jurisdictionName);
                if (jurisdictionName != null && !jurisdictionName.trim().equals("")) {

                    if(jurisdictionViewType == SearchFilterJurisdictionAdapter.PARENT_VIEW_TYPE) {
                        // add each District Council ID
                        for (SearchFilterJurisdictionMain j : realmJurisdictions) {
                            if (jurisdictionName.equals(j.getName())) {
                                jList.add(j.getName());
                                districtCouncils = j.getDistrictCouncils();

                                // add each Local ID
                                for (SearchFilterJurisdictionDistrictCouncil dc : districtCouncils) {
                                    if (dc != null) {
                                        jList.add(dc.getName());
                                        locals = dc.getLocals();
                                        for (SearchFilterJurisdictionLocal local : locals) {
                                            if (local != null) {
                                                jList.add(local.getName());
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }

                    //TODO - correct this section
                    else if(jurisdictionViewType == SearchFilterJurisdictionAdapter.CHILD_VIEW_TYPE) {
                        // look for matching District Council ID
                        for (SearchFilterJurisdictionMain j : realmJurisdictions) {
                            districtCouncils = j.getDistrictCouncils();
                            for (SearchFilterJurisdictionDistrictCouncil dc : districtCouncils) {
                                if (dc != null) {
                                    jList.add(dc.getName());
                                    if(jurisdictionName.equals(dc.getName())) {
                                        locals = dc.getLocals();
                                        for (SearchFilterJurisdictionLocal local : locals) {
                                            if (local != null) {
                                                jList.add(local.getName());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //TODO - implement this section
                    else if(jurisdictionViewType == SearchFilterJurisdictionAdapter.GRAND_CHILD_VIEW_TYPE) {

                    }

                    //Log.d("SearchFilterMPSAct", "processJurisdiction: input Jurisdiction id: " + jurisdictionId);
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
     * Process the Stage input data based on the received list of Stages persisted in Realm     //TODO - NO LONGER VALID
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

                String stageStr = bundle.getString(SearchFilterStageViewModel.BUNDLE_KEY_NAME);     // text display
                String stageId = SearchFilterStageViewModel.BUNDLE_KEY_ID;                          // ID                   //TODO - use this ID for name/id lookup rather than name?
                String stages = "";
                viewModel.setPersistedStage(stageStr);
                viewModel.setStage_select(stageStr);

                // build the list of IDs for the query, which include the parent ID and any of its child IDs
                List<String> sList = new ArrayList<>();
                sList.add(stageId);
                if (stageStr != null && !stageStr.trim().equals("")) {

                    // add each child Stage ID
                    for (ProjectStage parentStage : realmStages) {
                        if (stageStr.equals(parentStage.getName())) {
                            List<ProjectStage> childStages = parentStage.getChildStages();
                            for (ProjectStage childStage : childStages) {
                                if (childStage != null) {
                                    sList.add(Long.toString(childStage.getId()));
                                }
                            }
                            break;
                        }
                    }
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
    private void processBiddingWithin(String[] arr) {
        String biddingWithinStr = arr[0];   // text for display
        String biddingWithinInt = arr[1];   // int for query
        String[] updatedWithinArr = {biddingWithinStr, biddingWithinInt};
        String projectBiddingWithin = "";
        if (instantSearch && !viewModel.getIsProjectViewVisible()) {
            viewModel.setCbiddingWithinSelect(biddingWithinStr);
        } else {
            viewModel.setPersistedBiddingWithin(updatedWithinArr);
            viewModel.setBidding_within_select(biddingWithinStr);
        }

        if (biddingWithinStr != null && !biddingWithinStr.trim().equals("")) {
            projectBiddingWithin = "\"biddingInNext\":" + biddingWithinInt;
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_BIDDING_WITHIN, projectBiddingWithin);
    }

    /**
     * Process the Building-or-Highway input data, which is an array of one or two elements
     */
    private void processBuildingOrHighway(String[] arr) {
        String bhStr = arr[0];      // could come in as "Both", "Building" or "Heavy-Highway", to be converted to array ["B"] or ["H"] or ["B","H"]
        String bh = "";
        viewModel.setPersistedBuildingOrHighway(bhStr);
        viewModel.setBh_select(bhStr);
        if (bhStr != null && !bhStr.trim().equals("")) {
            List<String> bhList = new ArrayList<>();
            if (bhStr.equals("Building")) bhList.add("\"B\"");
            else if (bhStr.equals("Heavy-Highway")) bhList.add("\"H\"");
            else {
                bhList.add("\"B\"");
                bhList.add("\"H\"");
            }
            bh = "\"buildingOrHighway\":{\"inq\":" + bhList.toString() + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_BUILDING_OR_HIGHWAY, bh);
    }

    /**
     * Process the Owner Type input data, which is a String array with one element
     * TODO - make sure UI only allows single selection, as per iOS
     */
    private void processOwnerType(String[] arr) {
        String ownerTypeStr = arr[0];
        String ownerType = "";
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
