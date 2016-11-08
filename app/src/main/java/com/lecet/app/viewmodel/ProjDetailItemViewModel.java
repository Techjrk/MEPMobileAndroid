package com.lecet.app.viewmodel;

/**
 * File: ProjDetailItemViewModel Created: 11/7/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjDetailItemViewModel {

    private final String title;
    private final String info;

    public ProjDetailItemViewModel(String title, String info) {

        this.title = title;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public String getTitle() {
        return title;
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
