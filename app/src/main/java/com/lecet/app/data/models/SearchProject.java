package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by getdevsinc on 11/28/16.
 */

public class SearchProject extends RealmObject {
    @SerializedName("total")
    private int total;
    private int returned;
    private int page;
    private int pages;
    private RealmList<Project> results;

}
