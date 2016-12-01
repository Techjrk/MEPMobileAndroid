package com.lecet.app.viewmodel;

import android.content.Intent;
import android.content.res.AssetManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Debug;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.adapters.DashboardRecyclerViewAdapter;
import com.lecet.app.adapters.Search1RecyclerViewAdapter;
import com.lecet.app.content.MainActivity;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Geocode;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.SearchList;
import com.lecet.app.data.models.SearchProject;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.data.models.User;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.SearchDomain;
import com.lecet.app.domain.UserDomain;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Noel Anonas on 11/21/16.
 */

public class SearchViewModel extends BaseObservable {

    private static final String TAG = "SearchViewModel";
    String sc="Search category";
    private StringBuilder sb = new StringBuilder(sc); //for testing only...
    @Bindable
    public String getSb() {
        return sb.toString();
    }
    ArrayList<Integer> PID = new ArrayList<Integer>();  //For Project ids storage
    private final AppCompatActivity activity;
    private final SearchDomain searchDomain;

    //**constructor without input query - For RecentlyViewed and SavedSearch API
    public SearchViewModel(AppCompatActivity activity, SearchDomain sd) {
        this.activity = activity;
        this.searchDomain = sd;

        LecetSharedPreferenceUtil sharedPreferenceUtil = LecetSharedPreferenceUtil.getInstance(activity.getApplication());


          getUserSavedSearches( sharedPreferenceUtil.getId());   //testing for getting savedsearches result data -noel
 //      new Thread(new IRecentlyViewed()).start(); //testing for getting recentlyviewed result data  synchronization with getting projectdetail -noel

        //       getUserRecentlyViewed( sharedPreferenceUtil.getId());   //testing for getting recentlyviewed result data -noel <-Dont need. Use IRecentViewed instead.
    }

  //constructor with input query data as last parameter - For Project and Company search
    public SearchViewModel(AppCompatActivity activity, SearchDomain sd, String query) {
        this.activity = activity;
        this.searchDomain = sd;

        LecetSharedPreferenceUtil sharedPreferenceUtil = LecetSharedPreferenceUtil.getInstance(activity.getApplication());
//        getUserRecentlyViewed( sharedPreferenceUtil.getId());   //testing for getting recentlyviewed result data -noel

 //***TESTING THE SEARCH FUNCTIONALITY ***
        getProjectInit(query);   //testing for search project result data -noel
    //    getCompany(query);   //testing for search company result data -noel
    }

    //Thread runnable for Retrofit synchronization
    class IRecentlyViewed implements Runnable{
        @Override
        public void run() {
            getUserRecentSync(LecetSharedPreferenceUtil.getInstance(activity.getApplication()).getId());
            getProjInfoSync();
        }
    }     //end of IRecentlyViewed

    public void getUserRecentSync(long userId) {
        PID.clear();
        Response<List<SearchList>> response =    searchDomain.getSearchRecentlyViewedSync(userId);
        List<SearchList> slist;
        if (response.isSuccessful()) {
            slist = response.body();
            //searchDomain.copyToRealmTransaction(slist);
            sb.append("getUserRecentlyViewed\r\n");
            int ctr=0;
            for (SearchList s:slist) {
                ctr++;
                //   Log.d("Search ListCode: "+ctr,"search code:"+s.getCode()+ " id:"+s.getId()+" pid:"+s.getProjectId()+" cid:"+s.getCompanyId());
                sb.append("\r\n"+ctr+" code1:"+s.getCode()+ " id:"+s.getId()+" pid:"+s.getProjectId()+" cid:"+s.getCompanyId()+" createdAt:"+s.getCreatedAt()+"\r\n");
                PID.add(s.getProjectId());
            }

            notifyPropertyChanged(BR._all);
            //     getProjInfo();
        } else {

           errorDisplayMsg(new Exception(("Response Not Successful")));
        }
    }
    //****
    //function to get the list of recently viewed by the user
    public void getUserRecentlyViewed(long userId) {
        //Using the searchDomain to call the method to start retrofitting...
        searchDomain.getSearchRecentlyViewedWithFilter(userId, new Callback<List<SearchList>>() {

            @Override
            public void onResponse(Call<List<SearchList>> call, Response<List<SearchList>> response) {
                List<SearchList> slist;
                if (response.isSuccessful()) {
                    slist = response.body();
                    //searchDomain.copyToRealmTransaction(slist);
                    sb.append("getUserRecentlyViewed\r\n");
                    int ctr=0;
                    for (SearchList s:slist) {
                        ctr++;
                        //   Log.d("Search ListCode: "+ctr,"search code:"+s.getCode()+ " id:"+s.getId()+" pid:"+s.getProjectId()+" cid:"+s.getCompanyId());
                        sb.append("\r\n"+ctr+" code1:"+s.getCode()+ " id:"+s.getId()+" pid:"+s.getProjectId()+" cid:"+s.getCompanyId()+" createdAt:"+s.getCreatedAt()+"\r\n");
                        PID.add(s.getProjectId());
                    }

                    notifyPropertyChanged(BR._all);
                   //     getProjInfo();
                } else {
                    errorDisplayMsg(new Exception(("Response Not Successful")));
                }
            }

            @Override
            public void onFailure(Call<List<SearchList>> call, Throwable t) {
                errorDisplayMsg(t);
            }
        });


    }
    public void getProjInfoSync(){
        //----//       searchDomain.getProjectDetail(16995, new Callback<Project>() {
        Log.d("PID","PID size:"+PID.size());
        for (int i=0; i < PID.size(); i++) {
         Response<Project> response =   searchDomain.getProjectDetailSync(PID.get(i));
            Project p = response.body();
           if (p !=null && p.getGeocode() !=null) sb.append("noel\r\n lat:" + p.getGeocode().getLat() + " long:" + p.getGeocode().getLng() + "\r\n");
        }
           /* searchDomain.getProjectDetail(PID.get(i), new Callback<Project>() {
                @Override
                public void onResponse(Call<Project> call, Response<Project> response) {
                    if (response.isSuccessful()) {
                        Project p = response.body();
                        sb.append("noel\r\n lat:" + p.getGeocode().getLat() + " long:" + p.getGeocode().getLng() + "\r\n");
                    } else {
                        errorDisplayMsg(new Exception("not successful"));
                        Log.e("Error","error:");
                    }
                }
                @Override
                public void onFailure(Call<Project> call, Throwable t) {
                    Log.e("Error","error:"+t.getLocalizedMessage());
                    //    errorDisplayMsg(t);
                }
            });*/
        sb.append("\r\nNOTHING FOLLOWS");
        notifyPropertyChanged(BR._all);
    }  //end of getProjInfo()
    public void getProjInfo(){
        //----//       searchDomain.getProjectDetail(16995, new Callback<Project>() {
        for (int i=0; i < PID.size(); i++)
            searchDomain.getProjectDetail(PID.get(i), new Callback<Project>() {
                @Override
                public void onResponse(Call<Project> call, Response<Project> response) {
                    if (response.isSuccessful()) {
                        Project p = response.body();
                        sb.append("noel\r\n lat:" + p.getGeocode().getLat() + " long:" + p.getGeocode().getLng() + "\r\n");
                    } else {
                        errorDisplayMsg(new Exception("not successful"));
                      //  Log.e("Error","error:");
                    }
                }
                @Override
                public void onFailure(Call<Project> call, Throwable t) {
                //    Log.e("Error","error:"+t.getLocalizedMessage());
                        errorDisplayMsg(t);
                }
            });
        sb.append("\r\nNOTHING FOLLOWS");
        notifyPropertyChanged(BR._all);
    }  //end of getProjInfo()
    //*****
    //function to get the list of recently viewed by the user
    public void getUserSavedSearches(long userId) {
        //Using the searchDomain to call the method to start retrofitting...
        searchDomain.getSearchSaved(userId, new Callback<List<SearchSaved>>() {
            @Override
            public void onResponse(Call<List<SearchSaved>> call, Response<List<SearchSaved>> response) {
                List<SearchSaved> slist;
                if (response.isSuccessful()) {
                    slist = response.body();
                    //searchDomain.copyToRealmTransaction(slist);
                    int ctr=0;
                    for (SearchSaved s:slist) {
                        ctr++;
                        //   Log.d("Search ListCode: "+ctr,"search code:"+s.getCode()+ " id:"+s.getId()+" pid:"+s.getProjectId()+" cid:"+s.getCompanyId());
                        sb.append("\r\n"+ctr+" title:"+s.getTitle()+ " modelName:"+s.getModelName()+" id:"+s.getId()+" userid:"+s.getUserId()+" query:"+s.getQuery()+"\r\n");
                    }
                    notifyPropertyChanged(BR._all);

                } else {

                    errorDisplayMsg(new Exception(("Response Not Successful")));
                }
            }

            @Override
            public void onFailure(Call<List<SearchSaved>> call, Throwable t) {
                errorDisplayMsg(t);
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
//                List<Project> slist;
                 SearchProject slist;
                if (response.isSuccessful()) {
                    slist = response.body();
                    //searchDomain.copyToRealmTransaction(slist);

                    sb.append("\r\n page:"+slist.getPage()+ " pages:"+slist.getPages()+" total:"+slist.getTotal()+" returned:"+slist.getReturned()+" results:"+slist.getResults().toString()+"\r\n");

                    RealmList<Project> projects = slist.getResults();
                      int ctr=0;
                    for (Project ps:projects) {
                        ctr++;
                        //   Log.d("Search ListCode: "+ctr,"search code:"+s.getCode()+ " id:"+s.getId()+" pid:"+s.getProjectId()+" cid:"+s.getCompanyId());
                        sb.append("\r\n"+ctr+" id:"+ps.getId()+ " Title:"+ps.getTitle()+" Address:"+ps.getAddress1()+" GeoCode lat:"+ps.getGeocode().getLat()+" long:"+ps.getGeocode().getLng()+" BidDate:"+ps.getBidDate()+"\r\n");
                    }
                    notifyPropertyChanged(BR._all);

                } else {
                    errorDisplayMsg(new Exception(("Response Not Successful")));
                }
            }

            @Override
            public void onFailure(Call<SearchProject> call, Throwable t) {
                errorDisplayMsg(t);
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
                    int ctr=0;
                    for (Project ps:slist) {
                        ctr++;
                        //   Log.d("Search ListCode: "+ctr,"search code:"+s.getCode()+ " id:"+s.getId()+" pid:"+s.getProjectId()+" cid:"+s.getCompanyId());
                        sb.append("\r\n"+ctr+" id:"+ps.getId()+ " Title:"+ps.getTitle()+" Address:"+ps.getAddress1()+" GeoCode:"+ps.getGeocode()+" BidDate:"+ps.getBidDate()+"\r\n");
                    }
                    notifyPropertyChanged(BR._all);

                } else {
                    errorDisplayMsg(new Exception(("Response Not Successful")));
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                errorDisplayMsg(t);
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
                    //searchDomain.copyToRealmTransaction(slist);
                    int ctr=0;
                    for (Company cs:slist) {
                        ctr++;
                        //   Log.d("Search ListCode: "+ctr,"search code:"+s.getCode()+ " id:"+s.getId()+" pid:"+s.getProjectId()+" cid:"+s.getCompanyId());
                        sb.append("\r\n"+ctr+" id:"+cs.getId()+ " Name:"+cs.getName()+" Address:"+cs.getAddress1()+" City:"+cs.getCity()+" Country:"+cs.getCountry()+"\r\n");
                    }
                    notifyPropertyChanged(BR._all);

                } else {
                    errorDisplayMsg(new Exception(("Response Not Successful")));
                }
            }

            @Override
            public void onFailure(Call<List<Company>> call, Throwable t) {
                errorDisplayMsg(t);
            }
        });
    }

//***=============
public void errorDisplayMsg(Throwable t) {
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setTitle(activity.getString(R.string.error_network_title)+"\r\n"+t.getLocalizedMessage());
    builder.setMessage(activity.getString(R.string.error_network_message));
    builder.setNegativeButton(activity.getString(R.string.ok), null);
    Log.e("onFailure","onFailure: "+t.getMessage());
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
}
