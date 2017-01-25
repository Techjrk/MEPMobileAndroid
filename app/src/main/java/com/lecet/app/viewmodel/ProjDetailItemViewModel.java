package com.lecet.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.lecet.app.BR;

/**
 * File: ProjDetailItemViewModel Created: 11/7/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjDetailItemViewModel extends BaseObservable {

    private final String title;
    private String info;

    public ProjDetailItemViewModel(String title, String info) {

        this.title = title;
        this.info = info;
    }

    @Bindable
    public String getInfo() {
        return info;
    }

    public String getTitle() {
        return title;
    }

    public void setInfo(String info) {
        this.info = info;
        notifyPropertyChanged(BR.info);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjDetailItemViewModel)) return false;

        ProjDetailItemViewModel that = (ProjDetailItemViewModel) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return info != null ? info.equals(that.info) : that.info == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (info != null ? info.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProjDetailItemViewModel{" +
                "title='" + title + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
