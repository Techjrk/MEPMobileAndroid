package com.lecet.app.domain;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.ActivityUpdate;
import com.lecet.app.data.models.Company;
import com.lecet.app.data.models.Contact;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.interfaces.RealmFetchCallback;
import com.lecet.app.utility.Log;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * File: CompanyDomain Created: 12/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class CompanyDomain {

    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;

    public CompanyDomain(LecetClient lecetClient, final LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {

        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
    }

    /** Networking **/

    public Call<Company> getCompanyDetails(long companyId, Callback<Company> callback) {

        String token = sharedPreferenceUtil.getAccessToken();
        String filter = "{\"include\":[{\"contacts\":[\"company\"]},{\"projects\":{\"primaryProjectType\":{\"projectCategory\":\"projectGroup\"}}},{\"bids\":[\"company\",\"contact\",\"project\"]}]}";

        Call<Company> call = lecetClient.getCompanyService().company(token, companyId, filter);
        call.enqueue(callback);

        return call;
    }

    /** Persisted **/

    public RealmResults<Company> fetchCompany(long companyId) {

        RealmResults<Company> results = realm.where(Company.class).equalTo("id", companyId).findAll();
//        RealmResults<Company> results = realm.where(Company.class).equalTo("contactId", companyId).findAll();
        return results;
    }


    public void asyncFetchCompany(final long companyId, final RealmFetchCallback<Company> realmFetchCallback, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Log.d("Test", "execute: Company ID" + companyId);
                Company results = realm.where(Company.class).equalTo("id", companyId).findFirst();
                realmFetchCallback.fetchComplete(results);

            }
        }, onSuccess, onError);

    }

    public RealmResults<ActivityUpdate> fetchCompanyActivityUpdates(long projectId, Date updateMinDate, RealmChangeListener<RealmResults<ActivityUpdate>> listener) {

        RealmResults<ActivityUpdate> result = realm.where(ActivityUpdate.class).equalTo("companyId", projectId).greaterThanOrEqualTo("updatedAt", updateMinDate).findAllAsync();
        result.addChangeListener(listener);

        return result;
    }

    public Contact fetchCompanyContact(long contactID) {

        RealmResults<Contact> results = realm.where(Contact.class).equalTo("id", contactID).findAll();
        return results.first();
    }

    public Company copyToRealmTransaction(Company company) {

        realm.beginTransaction();
        Company persistedCompany = realm.copyToRealmOrUpdate(company);
        realm.commitTransaction();

        return persistedCompany;
    }

}
