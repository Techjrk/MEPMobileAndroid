package com.lecet.app.viewmodel;

import android.content.Intent;
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
 * Created by getdevsinc on 11/21/16.
 */

public class SearchViewModel extends BaseObservable {
    private StringBuilder sb = new StringBuilder("Recently Viewed"); //for testing only...


    private static final String TAG = "SearchViewModel";
    @Bindable
    public String getSb() {
        return sb.toString();
    }

    private final AppCompatActivity activity;
   // private final UserDomain userDomain;
private final SearchDomain searchDomain;
private    List<SearchList> slist;
    public String getSlist() {
        return slist.toString();
    }

    public SearchViewModel(AppCompatActivity activity, SearchDomain sd) {
//        public SearchViewModel(AppCompatActivity activity, UserDomain ld) {
        this.activity = activity;
        this.searchDomain = sd;
//        this.userDomain = ld;
        LecetSharedPreferenceUtil sharedPreferenceUtil = LecetSharedPreferenceUtil.getInstance(activity.getApplication());
        getUserRecentlyViewed( sharedPreferenceUtil.getId());
    }

    public void getUserRecentlyViewed(long userId) {

//        userDomain.getUser(userId, new Callback<User>() {
   searchDomain.getSearchRecentlyViewed(userId, new Callback<List<SearchList>>() {
            @Override
            public void onResponse(Call<List<SearchList>> call, Response<List<SearchList>> response) {

                if (response.isSuccessful()) {
                      slist = response.body();
                    int ctr=0;
                    for (SearchList s:slist) {
                       ctr++;
                        Log.d("Search ListCode: "+ctr,"search code:"+s.getCode()+ " id:"+s.getId()+" pid:"+s.getProjectId()+" cid:"+s.getCompanyId());
                    sb.append("\r\n"+ctr+" code:"+s.getCode()+ " id:"+s.getId()+" pid:"+s.getProjectId()+" cid:"+s.getCompanyId()+" createdAt:"+s.getCreatedAt());
                    }
                    notifyPropertyChanged(BR._all);
                    //User r = response.body();
                    //userDomain.copyToRealmTransaction(r);

                  //  Intent intent = new Intent(activity, MainActivity.class);
                  //  activity.startActivity(intent);
                  //  activity.finish();

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
