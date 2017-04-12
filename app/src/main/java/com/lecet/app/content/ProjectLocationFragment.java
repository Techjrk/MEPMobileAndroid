package com.lecet.app.content;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.FragmentProjectLocationBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectLocationViewModel;


import io.realm.Realm;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;

/**
 * Created by jasonm on 3/9/17.
 */

public class ProjectLocationFragment extends Fragment {

    private static final String TAG = "ProjectLocationFragment";

    private FragmentProjectLocationBinding binding;
    private long projectId;

    public static ProjectLocationFragment newInstance(long projectId) {
        ProjectLocationFragment fragmentInstance = new ProjectLocationFragment();
        Bundle args = new Bundle();
        args.putLong(PROJECT_ID_EXTRA, projectId);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    public ProjectLocationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectId = getArguments().getLong(PROJECT_ID_EXTRA);
        }

        Log.d(TAG, "onCreate: projectId: " + projectId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initDataBinding(inflater, container);
        return view;
    }

    private View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this.getContext()), Realm.getDefaultInstance());
        ProjectLocationViewModel viewModel = new ProjectLocationViewModel(this, projectId, projectDomain);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_location, container, false);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        //binding.getViewModel().init();
        return view;
    }
}