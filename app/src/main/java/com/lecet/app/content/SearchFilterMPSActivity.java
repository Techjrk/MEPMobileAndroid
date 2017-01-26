package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.adapters.JurisdictionAdapter;
import com.lecet.app.data.models.ProjectStage;
import com.lecet.app.data.models.ProjectType;
import com.lecet.app.data.models.SearchFilterJurisdictionDistrictCouncil;
import com.lecet.app.data.models.SearchFilterJurisdictionLocal;
import com.lecet.app.data.models.SearchFilterJurisdictionMain;
import com.lecet.app.databinding.ActivitySearchFilterMps30Binding;
import com.lecet.app.databinding.ActivitySearchFilterMseBinding;
import com.lecet.app.viewmodel.SearchFilterMPFViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

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

        if (data == null) return;
        Bundle info2 = data.getBundleExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE);
        String[] info = null;
        if (info2 == null) {
          //  Log.d("nobundle","nobundle");
            info = data.getStringArrayExtra(SearchViewModel.FILTER_EXTRA_DATA);
        } //else             Log.d("hasbundle","hasbundle");

        if (info != null || info2 !=null)
            if (resultCode == RESULT_OK)
//            switch (resultCode) {
                switch (requestCode) {
                    // Location
//                case R.id.location & 0xfff:
                    case SearchFilterMPFViewModel.LOCATION:
                        processLocation(info);
                        break;

                    // Type
                    //               case R.id.type & 0xfff:
                    case SearchFilterMPFViewModel.TYPE:
                        //processPrimaryProjectType(info);

                        if (info2 != null){
                            processBundleProjectTypeId(info2);
                        } else
                         processProjectTypeId(info);
                        break;

                    // Dollar Value
                    //   case R.id.value & 0xfff:
                    case SearchFilterMPFViewModel.VALUE:
                        processValue(info);
                        break;

                    // Updated Within
//                case R.id.updated_within & 0xfff:
                    case SearchFilterMPFViewModel.UPDATED_WITHIN:
                        processUpdatedWithin(info);
                        break;

                    // Jurisdiction
//                case R.id.jurisdiction & 0xfff:
                    case SearchFilterMPFViewModel.JURISDICTION:
                        processJurisdiction(info);
                        break;

                    // Project Stage
//                case R.id.stage & 0xfff:
                    case SearchFilterMPFViewModel.STAGE:
                        processStage(info);
                        break;

                    // Bidding Within
                    //   case R.id.bidding_within & 0xfff:
                    case SearchFilterMPFViewModel.BIDDING_WITHIN:
                        processBiddingWithin(info);
                        break;

                    // Building / Highway
                    //  case R.id.bh & 0xfff:
                    case SearchFilterMPFViewModel.BH:
                        processBuildingOrHighway(info);
                        break;

                    // Owner Type
//                case R.id.ownertype & 0xfff:
                    case SearchFilterMPFViewModel.OWNER_TYPE:
                        processOwnerType(info);
                        break;

                    // Work Type
//                case R.id.worktype & 0xfff:
                    case SearchFilterMPFViewModel.WORK_TYPE:
                        processWorkType(info);
                        break;

                    default:
                        Log.w("SearchFilterMPSAct", "onActivityResult: WARNING: no case for request code " + requestCode);
                        break;
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
     * TODO: Use in conjunction with processProjectTypeId()
     */
    private void processPrimaryProjectType(String[] arr) {
        String typeStr = arr[0];
        String projectType = "";
        viewModel.setType_select(typeStr);
        if (typeStr != null && !typeStr.trim().equals("")) {
            projectType = "\"primaryProjectType\":{" + typeStr + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_TYPE, projectType);
    }

    /**
     * Bundle extra data
     */

    private void processBundleProjectTypeId(final Bundle arr) {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ProjectType> realmTypes;
                realmTypes = realm.where(ProjectType.class).equalTo("parentId", 0).findAll();     // parentId = 0 should be all parent ProjectTypes, which each contain a list of child ProjectTypes
                Log.d("processProjectTypeId: ", "realmTypes size: " + realmTypes.size());
                Log.d("processProjectTypeId: ", "realmTypes: " + realmTypes);

                String typeStr = "\r\n"; //arr[0];   // text display
                //    String typeId = arr[1];   // ID                   //TODO - use this ID for name/id lookup rather than name?
                String types = "";

                for (String key : arr.keySet()) {
                    typeStr += arr.get(key) + ", ";
                }
                typeStr = typeStr.substring(0, typeStr.length()-2);
                if (instantSearch && !viewModel.getIsProjectViewVisible()) {
                    viewModel.setCtypeSelect(typeStr);
                } else {
                    viewModel.setPersistedProjectTypeId(typeStr);
                    viewModel.setType_select(typeStr);
                }


                types = "\"projectTypeId\":{\"inq\":" + typeStr + "}";

                viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_TYPE, types);
            }
        });
    }

    /**
     * Process the Project Type Id code based on input data from list
     * TODO - HARD-CODED. Get from map of project categories mapped to type ID codes **********
     */
    private void processProjectTypeId(final String[] arr) {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ProjectType> realmTypes;
                realmTypes = realm.where(ProjectType.class).equalTo("parentId", 0).findAll();     // parentId = 0 should be all parent ProjectTypes, which each contain a list of child ProjectTypes
                Log.d("processProjectTypeId: ", "realmTypes size: " + realmTypes.size());
                Log.d("processProjectTypeId: ", "realmTypes: " + realmTypes);

                String typeStr = arr[0];   // text display
                String typeId = arr[1];   // ID                   //TODO - use this ID for name/id lookup rather than name?
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
    private void processJurisdiction(final String[] arr) {

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<SearchFilterJurisdictionMain> realmJurisdictions;
                realmJurisdictions = realm.where(SearchFilterJurisdictionMain.class).findAll();
                Log.d("jurisdiction: ", "realmJurisdictions size: " + realmJurisdictions.size());
                Log.d("jurisdiction: ", "realmJurisdictions: " + realmJurisdictions);

                int jurisdictionViewType    = Integer.valueOf(arr[0]);    // viewType
                String jurisdictionId       = arr[1];    // id
                String jurisdictionName     = arr[2];    // name
                String jurisdictions = "";

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

                    if(jurisdictionViewType == JurisdictionAdapter.PARENT_VIEW_TYPE) {
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
                    else if(jurisdictionViewType == JurisdictionAdapter.CHILD_VIEW_TYPE) {
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
                    else if(jurisdictionViewType == JurisdictionAdapter.GRAND_CHILD_VIEW_TYPE) {

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
     * Process the Stage input data based on the received list of Stages persisted in Realm
     * arr[0] - Stage name
     * arr[1] - Stage ID
     */
    private void processStage(final String[] arr) {

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ProjectStage> realmStages;
                realmStages = realm.where(ProjectStage.class).equalTo("parentId", 0).findAll();     // parentId = 0 should be all parent ProjectStages, which each contain a list of child ProjectStages
                Log.d("processStage: ", "realmStages size: " + realmStages.size());
                Log.d("processStage: ", "realmStages: " + realmStages);

                String stageStr = arr[0];   // text display
                String stageId = arr[1];   // ID                   //TODO - use this ID for name/id lookup rather than name?
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
