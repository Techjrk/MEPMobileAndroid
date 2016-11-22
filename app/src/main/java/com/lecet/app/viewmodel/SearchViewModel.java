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

    private StringBuilder sb = new StringBuilder("Recently Viewed"); //for testing only...
    @Bindable
    public String getSb() {
        return sb.toString();
    }

    private final AppCompatActivity activity;
private final SearchDomain searchDomain;
private    List<SearchList> slist;
    public String getSlist() {
        return slist.toString();
    }

    //**constructor
    public SearchViewModel(AppCompatActivity activity, SearchDomain sd) {
        this.activity = activity;
        this.searchDomain = sd;

        LecetSharedPreferenceUtil sharedPreferenceUtil = LecetSharedPreferenceUtil.getInstance(activity.getApplication());
        getUserRecentlyViewed( sharedPreferenceUtil.getId());
    }



    //function to get the list of recently viewed by the user
    public void getUserRecentlyViewed(long userId) {
      //Using the searchDomain to call the method to start retrofitting...
       searchDomain.getSearchRecentlyViewed(userId, new Callback<List<SearchList>>() {
            @Override
            public void onResponse(Call<List<SearchList>> call, Response<List<SearchList>> response) {

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
