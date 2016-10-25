package com.lecet.app.interfaces;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Josué Rodríguez on 18/10/2016.
 */

public interface DashboardChart {

    void onAttach(Context context);

    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    View initDataBinding(LayoutInflater inflater, ViewGroup container);
}
