package com.lecet.app.content;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.PrimaryProjectType;
import com.lecet.app.data.models.SearchFilter;
import com.lecet.app.data.models.SearchFilterProjectTypesMain;
import com.lecet.app.data.models.SearchFilterProjectTypesProjectCategory;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivityAddProjectBinding;
import com.lecet.app.domain.LocationDomain;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.AddProjectActivityViewModel;
import com.lecet.app.viewmodel.SearchFilterAllTabbedViewModel;
import com.lecet.app.viewmodel.SearchFilterStageViewModel;
import com.lecet.app.viewmodel.SearchViewModel;
import io.realm.Realm;

import static com.lecet.app.content.ProjectsNearMeActivity.EXTRA_MARKER_LATITUDE;
import static com.lecet.app.content.ProjectsNearMeActivity.EXTRA_MARKER_LONGITUDE;

/**
 * Created by jasonm on 5/15/17.
 */

public class AddProjectActivity extends AppCompatActivity {

    private static final String TAG = "AddProjectActivity";

    private AddProjectActivityViewModel viewModel;
    private double latitude;
    private double longitude;
    private long projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAddProjectBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_project);

        latitude  = getIntent().getDoubleExtra(EXTRA_MARKER_LATITUDE, -1);
        longitude = getIntent().getDoubleExtra(EXTRA_MARKER_LONGITUDE, -1);
        projectId = getIntent().getLongExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, -1);

        Log.d(TAG, "onCreate: latitude: " + latitude);
        Log.d(TAG, "onCreate: longitude: " + longitude);
        Log.d(TAG, "onCreate: projectId: " + projectId);

        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        LocationDomain locationDomain = new LocationDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this), Realm.getDefaultInstance());
        viewModel = new AddProjectActivityViewModel(this, latitude, longitude, projectId, projectDomain, locationDomain);

        binding.setViewModel(viewModel);

        //Note: Delete all the saved filter from the SharedPreferences before processing the filter data.
        viewModel.deletePrefFilterFieldValues();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED || data == null ) {
            return;
        }
        Bundle bundle = data.getBundleExtra(SearchViewModel.FILTER_EXTRA_DATA_BUNDLE);
        if (bundle == null || bundle.isEmpty()) return;
        if (requestCode == SearchFilterAllTabbedViewModel.TYPE) {
            processProjectType(bundle);
        }
        if (requestCode == SearchFilterAllTabbedViewModel.STAGE) {
            processStage(bundle);
        }
        if(requestCode == SearchFilterCountyActivity.REQUEST_COUNTY){
            processCounty(bundle);
        }
    }

    private void processCounty(Bundle bundle) {
        String id = bundle.getString("id" , "None");
        String display = bundle.getString("display" , "None");
        viewModel.setCounty(id , display);

    }

    /**
     * Process the Project Type Bundle extra data
     * Single type selection: "primaryProjectTypeId":503
     */
    private void processProjectType(final Bundle bundle) {
        //Note: Saving the previous selected project type item to the sharedPreferences for later use if needed for bringing back again the Project Type item View.
        viewModel.savePrefBundle(getString(R.string.FilterTypeData), bundle); //saved the selected project type items to process later when needed.
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String displayStr = "";   // text display
                int primaryProjectTypeId = -1;

                // find the relevant project type ID
                for (String key : bundle.keySet()) {

                    Object value = bundle.get(key);
                    Log.d(TAG, "processProjectType: " + key + ": " + value);
                    displayStr = ""+value;
                 //   displayStr += value + ", "; //In case 9588 - It's present here in this case. This was deleted n bringing it back again for case 9929.
                    // check the grandchild-level (primary type) for a matching ID
                    PrimaryProjectType primaryType = realm.where(PrimaryProjectType.class).equalTo("id", Integer.valueOf(key)).findFirst();
                    if (primaryType != null) {
                        Log.d(TAG, "processProjectType: " + key + " is a Primary Type ID.");
                        primaryProjectTypeId = primaryType.getId();
                        break;
                    }
                }

                Log.d(TAG, "processProjectType: " + "displayStr: " + displayStr);
                Log.d(TAG, "processProjectType: " + "primaryProjectTypeId: " + primaryProjectTypeId);

                // value for API
                viewModel.getProjectPost().setPrimaryProjectTypeId(primaryProjectTypeId);

                // values for display
                viewModel.setTypeSelect(displayStr);
            }
        });
    }

    /**
     * Process the Stage input data
     * Ex: "projectStageId":208
     */
    private void processStage(final Bundle bundle) {

        //Note: Saving the previous selected stage item to the sharedPreferences for later use if needed for bringing back again the Stage item View.
        viewModel.savePrefBundleStageOnly(getString(R.string.FilterStageData), bundle); //saved the selected project type items to process later when needed.
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String stageStr;
                int stageId;

                if (bundle != null) {
                    for (String key : bundle.keySet()) {
                        Object value = bundle.get(key);
                        if(value instanceof Bundle){
                            Bundle stageBundle = (Bundle)value;
                            try {
                                stageStr = stageBundle.getString(SearchFilterStageViewModel.BUNDLE_KEY_NAME);                        // text display
                                stageId = Integer.parseInt(stageBundle.getString(SearchFilterStageViewModel.BUNDLE_KEY_ID));         // ID
                            }
                            catch (Exception e) {
                                Log.e(TAG, "processStage: Error parsing bundle.");
                                return;
                            }

                            if (stageStr != null && !stageStr.trim().equals("")) {
                                Log.d(TAG, "processStage: input Stage name: " + stageStr);
                                Log.d(TAG, "processStage: ID: " + stageId);
                            }

                            // API value
                            viewModel.getProjectPost().setProjectStageId(stageId);

                            // display value
                            if (stageStr == null || stageStr.equals("")) stageStr = "None";
                            viewModel.setStageSelect(stageStr);
                        }
                    }
                }

            }
        });
    }
}
