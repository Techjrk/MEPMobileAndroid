package com.lecet.app.viewmodel;

import android.content.Intent;
import android.content.res.AssetManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Debug;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.lecet.app.BR;
import com.lecet.app.R;
import com.lecet.app.content.MainActivity;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.SearchList;
import com.lecet.app.data.models.SearchSaved;
import com.lecet.app.data.models.User;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.domain.SearchDomain;
import com.lecet.app.domain.UserDomain;

import java.util.List;

import io.realm.RealmList;
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

    private final AppCompatActivity activity;
private final SearchDomain searchDomain;
/*
    private List result;
    public String getSlist() {
        return result.toString();
    }
*/

    //**constructor
    public SearchViewModel(AppCompatActivity activity, SearchDomain sd) {
        this.activity = activity;
        this.searchDomain = sd;

        LecetSharedPreferenceUtil sharedPreferenceUtil = LecetSharedPreferenceUtil.getInstance(activity.getApplication());
//        getUserRecentlyViewed( sharedPreferenceUtil.getId());   //testing for getting recentlyviewed result data -noel
          getUserSavedSearches( sharedPreferenceUtil.getId());   //testing for getting savedsearches result data -noel
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
                    //searchDomain.copyToRealmTransaction(slist);
                    int ctr=0;
                    for (SearchSaved s:slist) {
                        ctr++;
                        //   Log.d("Search ListCode: "+ctr,"search code:"+s.getCode()+ " id:"+s.getId()+" pid:"+s.getProjectId()+" cid:"+s.getCompanyId());
                        sb.append("\r\n"+ctr+" title:"+s.getTitle()+ " modelName:"+s.getModelName()+" id:"+s.getId()+" userid:"+s.getUserId()+" query:"+s.getQuery()+"\r\n");
                    }
                    notifyPropertyChanged(BR._all);

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(activity.getString(R.string.error_login_title));
                    builder.setMessage(activity.getString(R.string.error_login_message));
                    builder.setNegativeButton(activity.getString(R.string.ok), null);

                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<List<SearchSaved>> call, Throwable t) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(activity.getString(R.string.error_network_title)+"\r\n"+t.getLocalizedMessage());
                builder.setMessage(activity.getString(R.string.error_network_message));
                builder.setNegativeButton(activity.getString(R.string.ok), null);
                Log.e("onFailure","onFailure: "+t.getMessage());
                builder.show();
            }
        });
    }
//***=============
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
                    int ctr=0;
                    for (SearchList s:slist) {
                       ctr++;
                     //   Log.d("Search ListCode: "+ctr,"search code:"+s.getCode()+ " id:"+s.getId()+" pid:"+s.getProjectId()+" cid:"+s.getCompanyId());
                    sb.append("\r\n"+ctr+" code:"+s.getCode()+ " id:"+s.getId()+" pid:"+s.getProjectId()+" cid:"+s.getCompanyId()+" createdAt:"+s.getCreatedAt()+"\r\n");
                    }
                    notifyPropertyChanged(BR._all);

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(activity.getString(R.string.error_login_title));
                    builder.setMessage(activity.getString(R.string.error_login_message));
                    builder.setNegativeButton(activity.getString(R.string.ok), null);

                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<List<SearchList>> call, Throwable t) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(activity.getString(R.string.error_network_title)+"\r\n"+t.getLocalizedMessage());
                builder.setMessage(activity.getString(R.string.error_network_message));
                builder.setNegativeButton(activity.getString(R.string.ok), null);
                Log.e("onFailure","onFailure: "+t.getMessage());
                builder.show();
            }
        });
    }
}
