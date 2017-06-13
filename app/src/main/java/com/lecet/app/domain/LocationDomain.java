package com.lecet.app.domain;

import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.geocoding.GeocodeAddress;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;

import io.realm.Realm;
import retrofit2.Call;

/**
 * Created by jasonm on 6/5/17.
 */

public class LocationDomain {

    private final LecetClient lecetClient;
    private final LecetSharedPreferenceUtil sharedPreferenceUtil;
    private final Realm realm;


    public LocationDomain(LecetClient lecetClient, final LecetSharedPreferenceUtil sharedPreferenceUtil, Realm realm) {

        this.lecetClient = lecetClient;
        this.sharedPreferenceUtil = sharedPreferenceUtil;
        this.realm = realm;
    }

    public Realm getRealm() {
        return realm;
    }

    public Call<GeocodeAddress> getAddressFromLocation(double lat, double lng, String resultType, String key) {
        String latlng = Double.toString(lat) + "," + Double.toString(lng);
        Call<GeocodeAddress> call = lecetClient.getLocationService().getAddressFromLocation(latlng, resultType, key);

        return call;
    }


}
