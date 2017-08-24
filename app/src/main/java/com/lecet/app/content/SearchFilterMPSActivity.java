package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lecet.app.R;
import com.lecet.app.adapters.SearchFilterJurisdictionAdapter;
import com.lecet.app.adapters.SearchFilterStageSingleSelectAdapter;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.SearchFilterJurisdictionDistrictCouncil;
import com.lecet.app.data.models.SearchFilterJurisdictionLocal;
import com.lecet.app.data.models.SearchFilterJurisdictionMain;
import com.lecet.app.data.models.SearchFilterJurisdictionNoDistrictCouncil;
import com.lecet.app.data.models.SearchFilterProjectTypesMain;
import com.lecet.app.data.models.SearchFilterProjectTypesProjectCategory;
import com.lecet.app.data.models.SearchFilterStage;
import com.lecet.app.data.models.SearchFilterStagesMain;
import com.lecet.app.databinding.ActivitySearchFiltersAllRefineBinding;
import com.lecet.app.databinding.ActivitySearchFiltersAllTabbedBinding;
import com.lecet.app.utility.Log;
import com.lecet.app.viewmodel.SearchFilterAllTabbedViewModel;
import com.lecet.app.viewmodel.SearchFilterBiddingWithinViewModel;
import com.lecet.app.viewmodel.SearchFilterBuildingOrHighwayViewModel;
import com.lecet.app.viewmodel.SearchFilterJurisdictionViewModel;
import com.lecet.app.viewmodel.SearchFilterStageViewModel;
import com.lecet.app.viewmodel.SearchFilterUpdatedWithinViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.realm.Realm;

import static com.lecet.app.viewmodel.SearchViewModel.FILTER_INSTANT_SEARCH;

public class SearchFilterMPSActivity extends AppCompatActivity {
    private static final String TAG = "SearchFilterMPSActivity";
    SearchFilterAllTabbedViewModel viewModel;
    boolean instantSearch;
    int MAXCHARFIELD = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new SearchFilterAllTabbedViewModel(this);
        Intent i = getIntent();
        instantSearch = i.getBooleanExtra(FILTER_INSTANT_SEARCH, true);

        // if coming from Search Activity when no text has been input or Saved Search used, use the tabbed Projects/Companies filter layout
        if (SearchViewModel.usingInstantSearch) {
            ActivitySearchFiltersAllTabbedBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filters_all_tabbed);       //Projects/Companies
            sfilter.setViewModel(viewModel);
        }

        // if coming from Search Activity in a state where search text is entered (via user input or a Saved Search), use the non-tabbed Refine Search filter layout
        else {
            ActivitySearchFiltersAllRefineBinding sfilter = DataBindingUtil.setContentView(this, R.layout.activity_search_filters_all_refine);     //Refine Search
            sfilter.setViewModel(viewModel);
        }
        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.saveFilterToRealm();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(false);
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

        // Activities which return result in String[] format
        if (resultCode == RESULT_OK) {

            Bundle bundle = data.getBundleExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE);
            String[] extrasArr = null;
            if (bundle == null) {
                extrasArr = data.getStringArrayExtra(SearchViewModel.FILTER_EXTRA_DATA);
                if (extrasArr == null || extrasArr.length == 0) {
                    Log.w(TAG, "onActivityResult: WARNING: no valid Bundle or String extras was received from the exiting activity.");
                }
            }

            if (extrasArr != null) {
                switch (requestCode) {

                    // Location
                    case SearchFilterAllTabbedViewModel.LOCATION:
                        processLocation(extrasArr);
                        break;

                    // Dollar Value
                    case SearchFilterAllTabbedViewModel.VALUE:
                        processValue(extrasArr);
                        break;

                    // Owner Type
                    case SearchFilterAllTabbedViewModel.OWNER_TYPE:
                        processOwnerType(extrasArr);
                        break;

                    // Work Type
                    case SearchFilterAllTabbedViewModel.WORK_TYPE:
                        processWorkType(extrasArr);
                        break;

                    default:
                        Log.w(TAG, "onActivityResult: using String[] extras: WARNING: no case for request code: " + requestCode);
                        break;
                }
            }

            // Activities which return result in Bundle format
            else if (bundle != null) {
                //Log.d(TAG, "onActivityResult: using Bundle");
                switch (requestCode) {

                    // Project Type
                    case SearchFilterAllTabbedViewModel.TYPE:
                        processProjectType(bundle);
                        break;

                    // Jurisdiction
                    case SearchFilterAllTabbedViewModel.JURISDICTION:
                        processJurisdiction(bundle);
                        break;

                    // Project Stage
                    case SearchFilterAllTabbedViewModel.STAGE:
                        processStage(bundle);
                        break;

                    // Bidding Within
                    case SearchFilterAllTabbedViewModel.BIDDING_WITHIN:
                        processBiddingWithin(bundle);
                        break;

                    // Updated Within
                    case SearchFilterAllTabbedViewModel.UPDATED_WITHIN:
                        processUpdatedWithin(bundle);
                        break;

                    // Building / Highway
                    case SearchFilterAllTabbedViewModel.BH:
                        processBuildingOrHighway(bundle);
                        break;

                    default:
                        Log.w(TAG, "onActivityResult: using Bundle: WARNING: no case for request code: " + requestCode);
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
        String stateStr = !state.equals("") ? (!city.equals("") ? "," + state : " " + state) : "";       //NOTE - no validation
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
       // Log.d(TAG, "location: " + locationText);


        // StringBuilder used for generating query with commas as appropriate
        StringBuilder sb = new StringBuilder();
        String projectLocation = "";
        String companyLocation = "";

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
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_COMPANY_LOCATION, null);
        if (!viewModel.getIsProjectViewVisible()) {
            companyLocation = sb.toString();
            if (!companyLocation.trim().equals("")) {
                companyLocation = "\"companyLocation\":{" + companyLocation + "}";
            }

            viewModel.setSearchFilterResult(SearchViewModel.FILTER_COMPANY_LOCATION, companyLocation);  // this should work whether or not companyLocation is empty
           // Log.d("companyresult", "companyresult:" + companyLocation);
        }
        projectLocation = sb.toString();

        if (!projectLocation.trim().equals("")) {
            projectLocation = "\"projectLocation\":{" + projectLocation + "}";
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_LOCATION, projectLocation);  // this should work whether or not projectLocation is empty
    }

    /**
     * Process the Project Type Bundle extra data
     * Single type grandchild selection (500-level): projectTypeId":{"inq":[503]}
     * Multiple type grandchild selection (500-level) in different categories: projectTypeId":{"inq":[503,508]}
     * Multiple type grandchild selection (500-level) in single category: projectTypeId":{"inq":[503,504,505]}
     * Single type child subcategory selection Engineering:Dams: projectTypeId":{"inq":[503,504,505]}
     * Single type parent category selection Engineering: projectTypeId":{"inq":[501,502,503,504,505,506,507,508,509,510,511,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,528,529,530]}
     */
    private void processProjectType(final Bundle bundle) {
        viewModel.saveExtraFilterData(getString(R.string.FilterTypeData), bundle); //saved the selected project type items to process later when needed.
        if (bundle.isEmpty()) {
            viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_TYPE, "");
            viewModel.setType_select("");
            viewModel.setResultProjectType("");
            return;
        }
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String displayStr = ""; // = "\r\n";          // text display - removed line break as it was unnecessary
                String types = "";
                Set<Integer> idSet = new TreeSet<Integer>();        // using a Set to prevent dupes and maintain ascending order
                List<Integer> idList;

                // add each parent type ID
                for (String key : bundle.keySet()) {

                    Object value = bundle.get(key);
                   // Log.d("processProjectType: ", key + ": " + value);
                    displayStr += value + ", ";

                    // check the grandchild-level (primary type) for a matching ID
                    PrimaryProjectType primaryType = realm.where(PrimaryProjectType.class).equalTo("id", Integer.valueOf(key)).findFirst();
                    if (primaryType != null) {
                    //    Log.d("processProjectType: ", key + " is a Primary Type ID.");
                        idSet.add(primaryType.getId());
                    }

                    // if that's null, look for a matching child-level (subcategory) ID and if found, add all of its primary type IDs
                    else {
                        SearchFilterProjectTypesProjectCategory category = realm.where(SearchFilterProjectTypesProjectCategory.class).equalTo("id", Integer.valueOf(key)).findFirst();
                        if (category != null) {
                        //    Log.d("processProjectType: ", key + " is a Category ID.");
                            for (PrimaryProjectType primaryProjectType : category.getProjectTypes()) {
                                idSet.add(primaryProjectType.getId());
                            }
                        }

                        // if that's null, look for a matching parent-level (Main) ID and if found, add all of its child categories' IDs
                        else {
                            SearchFilterProjectTypesMain mainType = realm.where(SearchFilterProjectTypesMain.class).equalTo("id", Integer.valueOf(key)).findFirst();
                            if (mainType != null) {
                             //   Log.d("processProjectType: ", key + " is a Main Type ID.");
                                for (SearchFilterProjectTypesProjectCategory projectCategory : mainType.getProjectCategories()) {
                                    for (PrimaryProjectType primary : projectCategory.getProjectTypes()) {
                                        idSet.add(primary.getId());
                                    }
                                }
                            }
                        }
                    }
                }

                if (displayStr.length() > 2) {
                    displayStr = displayStr.substring(0, displayStr.length() - 2);         //trim trailing ", "
                }
                idList = new ArrayList<>(idSet);
               // Log.d("processProjectType", "displayStr: " + displayStr);
               // Log.d("processProjectType", "ids: " + idList);

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
                if (!idList.isEmpty()) {
                    types = "\"projectTypeId\":{\"inq\":" + idList + "}";
                    viewModel.setResultProjectType(types);
                  //  Log.d("type3","type3"+types);
                    viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_TYPE, types);
                } else {
                    viewModel.setResultProjectType("");
                    viewModel.setType_select("");
                }
            }
        });
    }


    /**
     * Process the dollar Value from input data
     */
    private void processValue(String[] arr) {
        String min = arr[0];                          // int for query
        String max = arr[1];                          // int for query

        if (max.trim().equals(String.valueOf(viewModel.VALUE_MAX))) {
            max = getString(R.string.MAX);
        }
        String valueStr = "$" + min + " - $" + max;   // text for display
        String projectValue = "";
        if ((min == null || min.equals("")) && (max == null || max.equals(""))) {
            valueStr = "";
        }
        if (valueStr != null && !valueStr.trim().equals("")) {
            projectValue = "\"projectValue\":{" + "\"min\":" + min + ",\"max\":" + max + "}";
            viewModel.setResultValue(projectValue);
        } else {
            viewModel.setResultValue("");
        }

        if (instantSearch && !viewModel.getIsProjectViewVisible()) {
            viewModel.setCvalueSelect(valueStr);
        } else {
            viewModel.setPersistedValueMin(min);
            if (!max.equals(getString(R.string.MAX))) viewModel.setPersistedValueMax(max);
            else viewModel.setPersistedValueMax("");
            viewModel.setValue_select(valueStr);
        }

        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_VALUE, projectValue);
    }

    /**
     * Process the Updated Within input data
     */
    private void processUpdatedWithin(final Bundle bundle) {
        String updatedWithinStr = bundle.getString(SearchFilterUpdatedWithinViewModel.BUNDLE_KEY_DISPLAY_STR); // text for display
        String updatedWithinInt = bundle.getString(SearchFilterUpdatedWithinViewModel.BUNDLE_KEY_DAYS_INT);    // int for query
        String projectUpdatedWithin = "";
        if (instantSearch && !viewModel.getIsProjectViewVisible()) {
            viewModel.setUpdated_within_select(updatedWithinStr);
        } else {
            viewModel.setPersistedUpdatedWithin(updatedWithinInt);
            viewModel.setUpdated_within_select(updatedWithinStr);
        }
        if (updatedWithinInt != null && !updatedWithinInt.trim().equals("") && !updatedWithinInt.equalsIgnoreCase(getString(R.string.any))) {
            projectUpdatedWithin = "\"updatedInLast\":" + updatedWithinInt;
            viewModel.setResultUpdateWithin(projectUpdatedWithin);
        } else {
            viewModel.setResultUpdateWithin("");
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_UPDATED_IN_LAST, projectUpdatedWithin);
    }

    /**
     * Process the Jurisdiction input data
     * Local (GrandChild) selection ex: "jurisdictions": { "inq": [3] }
     * District Council (Child) selection ex: "jurisdictions": { "inq": [3,4] }
     * Orphan Local (Child) selection ex: "jurisdictions": { "inq": [8] }
     * Region (Parent) selection ex with IDs of Locals with and without District Councils: "jurisdictions": { "inq": [8, 9, 10, 11, 12, 13, 14, 15, 16, 18, 19, 1, 2, 3, 4, 5, 6] }
     */
    private void processJurisdiction(final Bundle bundle) {
        viewModel.saveExtraFilterData(getString(R.string.FilterJurisdictionData), bundle); //saved the selected project type items to process later when needed.
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int jurisdictionViewType = -1;
                String jurisdictionId = null;
                String jurisdictionRegionId = null;
                String jurisdictionName = null;
                String jurisdictionAbbrev = null;
                String jurisdictionLongName = null;
                String jurisdictions = "";

                try {
                    jurisdictionViewType = Integer.valueOf(bundle.getString(SearchFilterJurisdictionViewModel.BUNDLE_KEY_VIEW_TYPE));
                    jurisdictionId = bundle.getString(SearchFilterJurisdictionViewModel.BUNDLE_KEY_ID);
                    jurisdictionRegionId = bundle.getString(SearchFilterJurisdictionViewModel.BUNDLE_KEY_REGION_ID);
                    jurisdictionName = bundle.getString(SearchFilterJurisdictionViewModel.BUNDLE_KEY_NAME);
                    jurisdictionAbbrev = bundle.getString(SearchFilterJurisdictionViewModel.BUNDLE_KEY_ABBREVIATION);
                    jurisdictionLongName = bundle.getString(SearchFilterJurisdictionViewModel.BUNDLE_KEY_LONG_NAME);
                } catch (Exception e) {
                    Log.e("processJurisdiction: ", "Error parsing bundle. bundle is empty");
                   // return;
                }

                // Build a single-element List based on the Jurisdiction ID and Name passed in the Bundle.
                List<String> jList = new ArrayList<>();

                // for a grandchild (Local) selection type (0), just use the ID
                if (jurisdictionName != null && jurisdictionName.length() > 0) {

                    // for a Local within a District Council which was selected directly
                    if (jurisdictionViewType == SearchFilterJurisdictionAdapter.GRAND_CHILD_VIEW_TYPE) {
                       // Log.d(TAG, "processJurisdiction: GrandChild (Local) Type Selected.");
                        jList.add(jurisdictionId);
                    }
                    // for a child (District Council) selection type (1), get the District Council which name matches the jurisdictionName and only add Local IDs within that DC
                    else if (jurisdictionViewType == SearchFilterJurisdictionAdapter.CHILD_VIEW_TYPE) {
                        SearchFilterJurisdictionDistrictCouncil selectedDistrictCouncil = realm.where(SearchFilterJurisdictionDistrictCouncil.class).equalTo("name", jurisdictionName).findFirst();
                        if (selectedDistrictCouncil != null) {
                          //  Log.d(TAG, "processJurisdiction: Child Type (District Council) Selected: " + selectedDistrictCouncil);
                            for (SearchFilterJurisdictionLocal local : selectedDistrictCouncil.getLocals()) {
                                jList.add(Integer.toString(local.getId()));
                            }
                        }
                        // or if an orphaned Local with no District Council was selected
                        else {
                         //   Log.d(TAG, "processJurisdiction: Child Type (Orphan Local with no District Council) Selected: " + jurisdictionName);
                            SearchFilterJurisdictionLocal selectedOrphanLocal = realm.where(SearchFilterJurisdictionLocal.class).equalTo("name", jurisdictionName).findFirst();
                            jList.add(Integer.toString(selectedOrphanLocal.getId()));
                        }
                    }
                    // for a parent (Region) selection type (2), search though Main Jurisdictions until a name matches the jurisdictionName and use the main's ID
                    else if (jurisdictionViewType == SearchFilterJurisdictionAdapter.PARENT_VIEW_TYPE) {
                        SearchFilterJurisdictionMain mainJurisdiction = realm.where(SearchFilterJurisdictionMain.class).equalTo("name", jurisdictionName).findFirst();
                        if (mainJurisdiction != null) {
                          //  Log.d(TAG, "processJurisdiction: Parent Type (Region) Selected: " + mainJurisdiction);

                            // first add any orphan Locals which are part of this parent Region but have no District Council
                            for (SearchFilterJurisdictionNoDistrictCouncil orphanLocal : mainJurisdiction.getLocalsWithNoDistrict()) {
                                jList.add(Integer.toString(orphanLocal.getId()));
                            }

                            // then look through all of the Region's District Councils and get any Local IDs belonging to those DCs
                            for (SearchFilterJurisdictionDistrictCouncil districtCouncil : mainJurisdiction.getDistrictCouncils()) {
                                for (SearchFilterJurisdictionLocal local : districtCouncil.getLocals()) {
                                    jList.add(Integer.toString(local.getId()));
                                }
                            }
                        }
                    }
                }
                if (jurisdictionName != null && !jurisdictionName.trim().equals("")) {

                    String js = jList.toString();
                    jurisdictions = "\"jurisdictions\":{\"inq\":" + js + "}";

                   /* Log.d(TAG, "processJurisdiction: jurisdictionViewType: " + jurisdictionViewType);
                    Log.d(TAG, "processJurisdiction: jurisdictionId: " + jurisdictionId);
                    Log.d(TAG, "processJurisdiction: jurisdictionRegionId: " + jurisdictionRegionId);
                    Log.d(TAG, "processJurisdiction: jurisdictionName: " + jurisdictionName);
                    Log.d(TAG, "processJurisdiction: jurisdictionAbbrev: " + jurisdictionAbbrev);
                    Log.d(TAG, "processJurisdiction: jurisdictionLongName: " + jurisdictionLongName);
                    Log.d(TAG, "processJurisdiction: jList for filter: " + jList);
                    Log.d(TAG, "processJurisdiction: final filter for jurisdictions: " + jurisdictions);*/

                    viewModel.setResultJurisdiction(jurisdictions);
                } else {
                    viewModel.setResultJurisdiction("");
                }
                // Log.d("jurisdictiondata","jurisdictiondata"+jurisdictions);
                viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_JURISDICTION, jurisdictions);

                // display
                if (jurisdictionName == null || jurisdictionName.equals("")) {
                    jurisdictionName = "Any";
                } else if (jurisdictionName.length() > MAXCHARFIELD)    //moved this below the filter construction so as not to check against the altered jurisdictionName
                    jurisdictionName = "\r\n" + jurisdictionName;

                if (instantSearch && !viewModel.getIsProjectViewVisible()) {
                    viewModel.setCjurisdictionSelect(jurisdictionName);
                } else {
                    viewModel.setPersistedJurisdiction(jurisdictionName);
                    viewModel.setJurisdiction_select(jurisdictionName);
                }
            }
        });

    }


    /**
     * Process the Stage input data based on the received list of Stages persisted in Realm
     * Ex: "projectStageId":{"inq":[208,209,210,211]}}
     */
    private void processStage(final Bundle b) {
        viewModel.saveExtraFilterData(getString(R.string.FilterStageData), b); //saved the selected project type items to process later when needed.
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int viewType = -1;
                String stageStr = "";
                // String stageStrId="";
                String stageId = "";
                String stages = "";

                // build the list of IDs for the query, which include the parent ID and any of its child IDs
                List<String> sList = new ArrayList<>();

                // add each parent type ID
                for (String key : b.keySet()) {
                    try {
                        Bundle bundle = b.getBundle(key);

                        viewType = Integer.valueOf(bundle.getString(SearchFilterStageViewModel.BUNDLE_KEY_VIEW_TYPE));  // view type (parent, child)
                        stageStr += bundle.getString(SearchFilterStageViewModel.BUNDLE_KEY_NAME) + ", ";                        // text display
                        stageId = bundle.getString(SearchFilterStageViewModel.BUNDLE_KEY_ID);
                        //   stageStrId += String.valueOf(stageId)+", ";
                        // ID
                        stages = "";
                    } catch (Exception e) {
                        Log.e("processStage: ", "Error parsing bundle. str=" + stageStr + " error:" + e.getLocalizedMessage());
                        return;
                    }

                    // build the list of IDs for the query, which include the parent ID and any of its child IDs
                    //List<String> sList = new ArrayList<>();

                    // GrandChild view type (2): should not occur since Stage does not have grandchild list view items
                    if (viewType == SearchFilterStageSingleSelectAdapter.GRAND_CHILD_VIEW_TYPE) {
                        Log.w(TAG, "processStage: Warning: GrandChild Type Selected. Not Supported.");
                    }
                    // Child view type (1): just use the selected child's ID
                    else if (viewType == SearchFilterStageSingleSelectAdapter.CHILD_VIEW_TYPE) {
                        Log.d(TAG, "processStage: Child Type Selected.");
                        sList.add(stageId);
                    }
                    // Parent view type (0): build a list of all child types under that parent ID
                    else if (viewType == SearchFilterStageSingleSelectAdapter.PARENT_VIEW_TYPE) {
                        Log.d(TAG, "processStage: Parent Type Selected.");
                        SearchFilterStagesMain selectedParentStage = realm.where(SearchFilterStagesMain.class).equalTo("id", Integer.valueOf(stageId)).findFirst();
                        for (SearchFilterStage childStage : selectedParentStage.getStages()) {
                            sList.add(Integer.toString(childStage.getId()));
                        }
                    } else {
                        Log.e("processStage: ", "Unsupported viewType: " + viewType);
                    }
                } // end of for-loop

                if (stageStr != null && !stageStr.trim().equals("")) {

                   /* Log.d(TAG, "processStage: viewType: " + viewType);
                    Log.d(TAG, "processStage: input Stage name: " + stageStr);
                    Log.d(TAG, "processStage: IDs: " + sList);*/

                    stages = "\"projectStageId\":{\"inq\":" + sList.toString() + "}";
                    viewModel.setResultStage(stages);
                } else viewModel.setResultStage("");
                viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_STAGE, stages);
                if (stageStr.length() > 2) {
                    stageStr = stageStr.substring(0, stageStr.length() - 2);         //trim trailing ", "
                }
                // display
                if (stageStr == null || stageStr.equals("")) stageStr = "Any";
                viewModel.setPersistedStage(stageStr);
                viewModel.setStage_select(stageStr);
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

        if (biddingWithinInt != null && !biddingWithinInt.trim().equals("") && !biddingWithinInt.equalsIgnoreCase(getString(R.string.any))) {
            projectBiddingWithin = "\"biddingInNext\":" + biddingWithinInt;
            viewModel.setResultBiddingWithin(projectBiddingWithin);
        } else {
            viewModel.setResultBiddingWithin("");
        }
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_BIDDING_WITHIN, projectBiddingWithin);
    }

    /**
     * Process the Building-or-Highway input data, which is an array of one or two elements
     */
    private void processBuildingOrHighway(Bundle bundle) {

        final String BUILDING = getApplicationContext().getResources().getString(R.string.building);
        final String HEAVY_HIGHWAY = getApplicationContext().getResources().getString(R.string.heavy_highway);

        String bhDisplayStr = bundle.getString(SearchFilterBuildingOrHighwayViewModel.BUNDLE_KEY_DISPLAY_STR);  //arr[0];      // could come in as "Both", "Any", "Building" or "Heavy-Highway", to be converted to array ["B"] or ["H"] or ["B","H"]
        String bhChar = bundle.getString(SearchFilterBuildingOrHighwayViewModel.BUNDLE_KEY_TAG); // arr[1];
        viewModel.setPersistedBuildingOrHighway(bundle);
        if (bhDisplayStr == null || bhDisplayStr.equals("")) bhDisplayStr = "Any";
        viewModel.setBh_select(bhDisplayStr);
        if (bhDisplayStr != null && !bhDisplayStr.trim().equals("")) {
            List<String> bhList = new ArrayList<>();
            if (bhDisplayStr.equals(BUILDING)) bhList.add("\"B\"");
            else if (bhDisplayStr.equals(HEAVY_HIGHWAY)) bhList.add("\"H\"");
            else {
                bhList.add("\"B\"");
                bhList.add("\"H\"");
                //** Using this to improve searching in bh.
                bhList.clear();
                viewModel.setResultBH("");
                viewModel.setBh_select("Any");
                bhChar="";
                viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_BUILDING_OR_HIGHWAY, bhChar);
                return;
            }
            bhChar = "\"buildingOrHighway\":{\"inq\":" + bhList.toString() + "}";
            viewModel.setResultBH(bhChar);
        } else viewModel.setResultBH("");
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_BUILDING_OR_HIGHWAY, bhChar);

    }

    /**
     * Process the Owner Type input data, which is a String array with one element
     */
    private void processOwnerType(String[] arr) {
        String ownerTypeStr = arr[0];
        String ownerTypeId = arr[1];
        String ownerType = "";
        if (ownerTypeStr == null || ownerTypeStr.equals("")) {
            ownerTypeStr = "Any";
        }
        viewModel.setPersistedOwnerType(ownerTypeId);
        viewModel.setOwner_type_select(ownerTypeStr);
        if (ownerTypeStr != null && !ownerTypeStr.trim().equals("") && !ownerTypeStr.equalsIgnoreCase(getString(R.string.any))) {
            List<String> oList = new ArrayList<>();
            oList.add("\"" + ownerTypeStr + "\"");
            ownerType = "\"ownerType\":{\"inq\":" + oList.toString() + "}";
            viewModel.setResultOwnerType(ownerType);
        } else  viewModel.setResultOwnerType("");
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_OWNER_TYPE, ownerType);

    }

    /**
     * Process the Work Type input data, which is a String array with one element
     */
    private void processWorkType(final String[] arr) {
        String workTypeStr = arr[0];
        String workTypeInt = arr[1];
        String workTypeCBId = arr[2]; //checkbox id
        String workType = "";
        if (workTypeStr == null || workTypeStr.equals("")) {
            workTypeStr = "Any";
        }
        viewModel.setWork_type_select(workTypeStr);
        viewModel.setPersistedWorkType(workTypeCBId);
        List<String> wList = new ArrayList<>();
        if (workTypeInt != null && !workTypeInt.trim().equals("") && !workTypeStr.equalsIgnoreCase(getString(R.string.any))) {
            wList.add(workTypeInt);
            workType = "\"workTypeId\":{\"inq\":" + wList.toString() + "}";
            viewModel.setResultWorkType(workType);
        } else viewModel.setResultWorkType("");
        viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_WORK_TYPE, workType);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //store the search filter result data to be used by the Search activity. 
        viewModel.saveResult();
    }
}
