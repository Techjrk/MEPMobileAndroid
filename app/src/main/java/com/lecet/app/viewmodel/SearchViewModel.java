package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.Search1RecyclerViewAdapter;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchProject;
import com.lecet.app.data.models.SearchResult;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.SearchDomain;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DomandTom 2016.
 */

public class SearchViewModel extends BaseObservable {

    private final AppCompatActivity activity;
    private final SearchDomain searchDomain;

    private static final String TAG = "SearchViewModel";

    private String query;

    @Bindable
    public String getQuery() {
        return query;
    }

    @Bindable
    public void setQuery(String query) {
        this.query = query;
    }


    //1 **constructor without input query - For RecentlyViewed and SavedSearch API
    public SearchViewModel(AppCompatActivity activity, SearchDomain sd) {
        this.activity = activity;
        this.searchDomain = sd;
        //TODO: 1 ***TESTING THE SEARCH FUNCTIONALITY FOR THIS CONSTRUCTOR ***
        getUserSavedSearches(LecetSharedPreferenceUtil.getInstance(activity.getApplication()).getId());   //testing for getting savedsearches result data
    }

    //function to get the list of recently viewed by the user
    public void getUserRecentlyViewed(long userId) {
        //Using the searchDomain to call the method to start retrofitting...
        searchDomain.getSearchRecentlyViewed(userId, new Callback<List<SearchResult>>() {
            @Override
            public void onResponse(Call<List<SearchResult>> call, Response<List<SearchResult>> response) {
                List<SearchResult> slist;
                if (response.isSuccessful()) {
                    slist = response.body();
                    sb += ("getUserRecentlyViewed\r\n");
                    int ctr = 0;
                    for (SearchResult s : slist) {
                        //TODO: testing for getting the result of  RecentlyViewed search
                        ctr++;
                        sb += ("\r\n" + ctr + " code1:" + s.getCode() + " id:" + s.getId() + " pid:" + s.getProjectId() + " cid:" + s.getCompanyId() + " createdAt:" + s.getCreatedAt() + "\r\n");
                    }
                    notifyPropertyChanged(BR.sb);

                } else {
                    errorDisplayMsg(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SearchResult>> call, Throwable t) {
                errorDisplayMsg(t.getLocalizedMessage());
            }
        }); //end of searchDomain.getRecentlyViewed;
    }

    //function to get the list of recently viewed by the user
    public void getUserSavedSearches(long userId) {
        //Using the searchDomain to call the method to start retrofitting...
        searchDomain.getSearchSaved(userId, new Callback<List<SearchSaved>>() {
            @Override
            public void onResponse(Call<List<SearchSaved>> call, Response<List<SearchSaved>> response) {
                List<SearchSaved> slist;
                if (response.isSuccessful()) {
                    slist = response.body();
                    int ctr = 0;
                    for (SearchSaved s : slist) {
                        //TODO: testing for getting the result of  RecentlyViewed search
                        ctr++;
                        sb += ("\r\n" + ctr + " title:" + s.getTitle() + " modelName:" + s.getModelName() + " id:" + s.getId() + " userid:" + s.getUserId() + " query:" + s.getQuery() + "\r\n");
                    }
                    notifyPropertyChanged(BR.sb);

                } else {
                    errorDisplayMsg(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SearchSaved>> call, Throwable t) {
                errorDisplayMsg(t.getLocalizedMessage());
            }
        });
    }

    //***==========
    //function to search the project based on query string
    public void getProjectInit(String q) {
        //Using the searchDomain to call the method to start retrofitting...
        searchDomain.getSearchProjectInit(q, new Callback<SearchProject>() {
            @Override
            public void onResponse(Call<SearchProject> call, Response<SearchProject> response) {
                SearchProject slist;
                if (response.isSuccessful()) {
                    slist = response.body();
                    //searchDomain.copyToRealmTransaction(slist);
                    sb += ("\r\n page:" + slist.getPage() + " pages:" + slist.getPages() + " total:" + slist.getTotal() + " returned:" + slist.getReturned() + " results:" + slist.getResults().toString() + "\r\n");
                    RealmList<Project> projects = slist.getResults();
                    int ctr = 0;
                    for (Project ps : projects) {
                        ctr++;
                        sb += ("\r\n" + ctr + " id:" + ps.getId() + " Title:" + ps.getTitle() + " Address:" + ps.getAddress1() + " GeoCode lat:" + ps.getGeocode().getLat() + " long:" + ps.getGeocode().getLng() + " BidDate:" + ps.getBidDate() + "\r\n");
                    }
                    notifyPropertyChanged(BR.sb);
                } else {
                    errorDisplayMsg(response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchProject> call, Throwable t) {
                errorDisplayMsg(t.getLocalizedMessage());
            }
        });
    }
    //***==========
    //function to search the project based on query string
    public void getProject(String q) {
        //Using the searchDomain to call the method to start retrofitting...
        searchDomain.getSearchProject(q, new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                List<Project> slist;
                if (response.isSuccessful()) {
                    slist = response.body();
                    //searchDomain.copyToRealmTransaction(slist);
                    int ctr = 0;
                    for (Project ps : slist) {
                        ctr++;
                        //   Log.d("Search ListCode: "+ctr,"search code:"+s.getCode()+ " id:"+s.getId()+" pid:"+s.getProjectId()+" cid:"+s.getCompanyId());
                        sb += ("\r\n" + ctr + " id:" + ps.getId() + " Title:" + ps.getTitle() + " Address:" + ps.getAddress1() + " GeoCode:" + ps.getGeocode() + " BidDate:" + ps.getBidDate() + "\r\n");
                    }
                    notifyPropertyChanged(BR.sb);
                } else {
                    errorDisplayMsg(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                errorDisplayMsg(t.getLocalizedMessage());
            }
        });
    }

    //***==========
    //function to search the company based on query string
    public void getCompany(String q) {
        //Using the searchDomain to call the method to start retrofitting...
        searchDomain.getSearchCompany(q, new Callback<List<Company>>() {
            @Override
            public void onResponse(Call<List<Company>> call, Response<List<Company>> response) {
                List<Company> slist;
                if (response.isSuccessful()) {
                    slist = response.body();
                    int ctr = 0;
                    for (Company cs : slist) {
                        ctr++;
                        sb += ("\r\n" + ctr + " id:" + cs.getId() + " Name:" + cs.getName() + " Address:" + cs.getAddress1() + " City:" + cs.getCity() + " Country:" + cs.getCountry() + "\r\n");
                    }
                    notifyPropertyChanged(BR.sb);
                } else {
                    errorDisplayMsg(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Company>> call, Throwable t) {
                errorDisplayMsg(t.getLocalizedMessage());
            }
        });
    }


    //***=============
    public void errorDisplayMsg(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.error_network_title) + "\r\n" + message);
        builder.setMessage(activity.getString(R.string.error_network_message));
        builder.setNegativeButton(activity.getString(R.string.ok), null);
        Log.e("onFailure", "onFailure: " + message);
        builder.show();
    }


    //*** setting up the recylerview
    private Search1RecyclerViewAdapter dashboardAdapter;
    private List<RealmObject> adapterData;

    private void initializeAdapter() {

        adapterData = new ArrayList<>();

        RecyclerView recyclerView = getProjectRecyclerView(R.id.recycler_view);
        setupRecyclerView(recyclerView);
        dashboardAdapter = new Search1RecyclerViewAdapter(adapterData);
        recyclerView.setAdapter(dashboardAdapter);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private RecyclerView getProjectRecyclerView(@IdRes int recyclerView) {

        return (RecyclerView) activity.findViewById(recyclerView);
    }

    //TODO: For checking the response result of the MSE APIs call using sb variable.
    private String sb = "";
    @Bindable
    public String getSb() {
        return sb;
    }

}
