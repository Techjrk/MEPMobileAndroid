package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by getdevsinc on 11/21/16.
 */

public class SearchList extends RealmObject {
    @SerializedName("robjects")
//    private String [] robjects;
    private RealmList<SearchObject> robjects;
    @Override
    public String toString() {
        return ".. SearchList Result.. {" +robjects+ '}';
    }
}
