package com.lecet.app.domain;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.api.service.SearchService;
import com.lecet.app.data.models.SearchList;
import com.lecet.app.data.models.User;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by getdevsinc on 11/21/16.
 */

public class SearchDomain {
    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;

    public SearchDomain(LecetClient lecetClient, LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {
        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
    }
    public void getSearchRecentlyViewed(long userId, Callback<List<SearchList>> callback) {

        String token = sharedPreferenceUtil.getAccessToken();

        Call<List<SearchList>> call = lecetClient.getSearchService().getSearchRecentlyViewed(token, userId);
        call.enqueue(callback);
    }

   /* public SearchList copyToRealmTransaction(SearchList searchList) {

        realm.beginTransaction();
        SearchList persistedSearchList = realm.copyToRealmOrUpdate(searchList);
        realm.commitTransaction();

        return persistedSearchList;
    }*/
}

