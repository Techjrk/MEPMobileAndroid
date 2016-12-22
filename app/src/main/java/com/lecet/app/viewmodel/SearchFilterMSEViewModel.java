package com.lecet.app.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.SearchAllCompanyRecyclerViewAdapter;
import com.lecet.app.adapters.SearchAllContactRecyclerViewAdapter;
import com.lecet.app.adapters.SearchAllProjectRecyclerViewAdapter;
import com.lecet.app.adapters.SearchCompanyRecyclerViewAdapter;
import com.lecet.app.adapters.SearchProjectRecyclerViewAdapter;
import com.lecet.app.adapters.SearchRecentRecyclerViewAdapter;
import com.lecet.app.adapters.SearchSummaryCompanyRecyclerViewAdapter;
import com.lecet.app.adapters.SearchSummaryContactRecyclerViewAdapter;
import com.lecet.app.adapters.SearchSummaryProjectRecyclerViewAdapter;
import com.lecet.app.content.widget.SearchFilterMSEActivity;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchCompany;
import com.lecet.app.data.models.SearchContact;
import com.lecet.app.data.models.SearchProject;
import com.lecet.app.data.models.SearchResult;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.SearchDomain;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DomandTom 2016.
 */

public class SearchFilterMSEViewModel extends BaseObservable {

    private static final String TAG = "SearchFilterMSEViewModel";
    private AppCompatActivity activity;

    /**
     * Constructor
     */
    public SearchFilterMSEViewModel(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * Display error message
     */
    public void errorDisplayMsg(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.error_network_title) + "\r\n" + message + "\r\n");
        Log.e("Error:", "Error " + message);
        builder.setMessage(message);
        builder.setNegativeButton(activity.getString(R.string.ok), null);
        Log.e("onFailure", "onFailure: " + message);
        builder.show();
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    /**
     * OnClick handlers
     **/

    public void onClicked(View view) {
        Toast.makeText(activity, "Clicked.", Toast.LENGTH_SHORT).show();
    }

    public void onFilterClicked(View view) {
//        Toast.makeText(activity, "Filter clicked.", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(activity, SearchFilterMSEActivity.class);
        activity.startActivityForResult(i, 0);
    }
}
