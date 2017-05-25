package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.adapters.SearchFilterStageAdapter;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.SearchFilterProjectTypesMain;
import com.lecet.app.data.models.SearchFilterProjectTypesProjectCategory;
import com.lecet.app.data.models.SearchFilterStage;
import com.lecet.app.data.models.SearchFilterStagesMain;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityAddProjectBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.AddProjectActivityViewModel;
import com.lecet.app.viewmodel.SearchFilterAllTabbedViewModel;
import com.lecet.app.viewmodel.SearchFilterStageViewModel;
import com.lecet.app.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.realm.Realm;

import static com.lecet.app.content.ProjectsNearMeActivity.EXTRA_MARKER_ADDRESS;
import static com.lecet.app.content.ProjectsNearMeActivity.EXTRA_MARKER_LATITUDE;
import static com.lecet.app.content.ProjectsNearMeActivity.EXTRA_MARKER_LONGITUDE;

/**
 * Created by jasonm on 5/15/17.
 */

public class AddProjectActivity extends AppCompatActivity {

    private static final String TAG = "AddProjectActivity";

    private AddProjectActivityViewModel viewModel;
    private String address;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAddProjectBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_project);

        address = getIntent().getStringExtra(EXTRA_MARKER_ADDRESS);
        latitude = getIntent().getDoubleExtra(EXTRA_MARKER_LATITUDE, -1);
        longitude = getIntent().getDoubleExtra(EXTRA_MARKER_LONGITUDE, -1);

        Log.d(TAG, "onCreate: address: " + address);
        Log.d(TAG, "onCreate: latitude: " + latitude);
        Log.d(TAG, "onCreate: longitude: " + longitude);

        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        viewModel = new AddProjectActivityViewModel(this, address, latitude, longitude, projectDomain);

        binding.setViewModel(viewModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getBundleExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE);
        if (requestCode == SearchFilterAllTabbedViewModel.TYPE) {
            processProjectType(bundle);
        }
        if (requestCode == SearchFilterAllTabbedViewModel.STAGE) {
            processStage(bundle);
        }
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
                    Log.d(TAG, "processProjectType: " + key + ": " + value);
                    displayStr += value + ", ";

                    // check the grandchild-level (primary type) for a matching ID
                    PrimaryProjectType primaryType = realm.where(PrimaryProjectType.class).equalTo("id", Integer.valueOf(key)).findFirst();
                    if (primaryType != null) {
                        Log.d(TAG, "processProjectType: " + key + " is a Primary Type ID.");
                        idSet.add(primaryType.getId());
                    }

                    // if that's null, look for a matching child-level (subcategory) ID and if found, add all of its primary type IDs
                    else {
                        SearchFilterProjectTypesProjectCategory category = realm.where(SearchFilterProjectTypesProjectCategory.class).equalTo("id", Integer.valueOf(key)).findFirst();
                        if (category != null) {
                            Log.d(TAG, "processProjectType: " + key + " is a Category ID.");
                            for (PrimaryProjectType primaryProjectType : category.getProjectTypes()) {
                                idSet.add(primaryProjectType.getId());
                            }
                        }

                        // if that's null, look for a matching parent-level (Main) ID and if found, add all of its child categories' IDs
                        else {
                            SearchFilterProjectTypesMain mainType = realm.where(SearchFilterProjectTypesMain.class).equalTo("id", Integer.valueOf(key)).findFirst();
                            if (mainType != null) {
                                Log.d(TAG, "processProjectType: " +  key + " is a Main Type ID.");
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
                Log.d(TAG, "processProjectType: " +  "displayStr: " + displayStr);
                Log.d(TAG, "processProjectType: " + "ids: " + idList);
                int MAXCHARFIELD = 16;
                if (displayStr != null && displayStr.length() > MAXCHARFIELD)
                    displayStr = "\r\n" + displayStr;

                // viewModel.setPersistedProjectTypeId(displayStr);
                viewModel.setTypeSelect(displayStr);

                // types = "\"projectTypeId\":{\"inq\":" + idList + "}";
                // viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_TYPE, types);
            }  //end of public void execute()
        });
    }

    /**
     * Process the Stage input data based on the received list of Stages persisted in Realm
     * Ex: "projectStageId":{"inq":[208,209,210,211]}}
     */
    private void processStage(final Bundle bundle) {

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int viewType = -1;
                String stageStr;
                String stageId;
                String stages;

                try {
                    viewType = Integer.valueOf(bundle.getString(SearchFilterStageViewModel.BUNDLE_KEY_VIEW_TYPE));  // view type (parent, child)
                    stageStr = bundle.getString(SearchFilterStageViewModel.BUNDLE_KEY_NAME);                        // text display
                    stageId = bundle.getString(SearchFilterStageViewModel.BUNDLE_KEY_ID);                          // ID
                    stages = "";
                } catch (Exception e) {
                    Log.e(TAG, "processStage: Error parsing bundle.");
                    return;
                }

                // build the list of IDs for the query, which include the parent ID and any of its child IDs
                List<String> sList = new ArrayList<>();

                // GrandChild view type (2): should not occur since Stage does not have grandchild list view items
                if (viewType == SearchFilterStageAdapter.GRAND_CHILD_VIEW_TYPE) {
                    Log.w(TAG, "processStage: Warning: GrandChild Type Selected. Not Supported.");
                }
                // Child view type (1): just use the selected child's ID
                else if (viewType == SearchFilterStageAdapter.CHILD_VIEW_TYPE) {
                    Log.d(TAG, "processStage: Child Type Selected.");
                    sList.add(stageId);
                }
                // Parent view type (0): build a list of all child types under that parent ID
                else if (viewType == SearchFilterStageAdapter.PARENT_VIEW_TYPE) {
                    Log.d(TAG, "processStage: Parent Type Selected.");
                    SearchFilterStagesMain selectedParentStage = realm.where(SearchFilterStagesMain.class).equalTo("id", Integer.valueOf(stageId)).findFirst();
                    for (SearchFilterStage childStage : selectedParentStage.getStages()) {
                        sList.add(Integer.toString(childStage.getId()));
                    }
                } else {
                    Log.e(TAG, "processStage: Unsupported viewType: " + viewType);
                }

                if (stageStr != null && !stageStr.trim().equals("")) {

                    Log.d(TAG, "processStage: viewType: " + viewType);
                    Log.d(TAG, "processStage: input Stage name: " + stageStr);
                    Log.d(TAG, "processStage: IDs: " + sList);

                    stages = "\"projectStageId\":{\"inq\":" + sList.toString() + "}";
                }
                //  viewModel.setSearchFilterResult(SearchViewModel.FILTER_PROJECT_STAGE, stages);

                // display
                if (stageStr == null || stageStr.equals("")) stageStr = "Any";
                //   viewModel.setPersistedStage(stageStr);
                viewModel.setStageSelect(stageStr);
            }
        });
    }

}
