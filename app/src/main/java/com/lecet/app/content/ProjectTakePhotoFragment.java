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
import com.lecet.app.databinding.FragmentProjectTakePhotoBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectTakePhotoViewModel;

import io.realm.Realm;

/**
 * Created by jasonm on 3/29/17.
 */

public class ProjectTakePhotoFragment extends Fragment {

    private static final String TAG = "ProjectTakePhotoFrag";

    public static String PROJECT_ID = "com.lecet.app.content.ProjectTakePhotoFragment.projectId";

    private FragmentProjectTakePhotoBinding binding;
    private long projectId;

    public static ProjectTakePhotoFragment newInstance(long projectId) {
        ProjectTakePhotoFragment fragmentInstance = new ProjectTakePhotoFragment();
        Bundle args = new Bundle();
        args.putLong(ProjectTakePhotoFragment.PROJECT_ID, projectId);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    public ProjectTakePhotoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectId = getArguments().getLong(ProjectTakePhotoFragment.PROJECT_ID);
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

          //TODO - fill in
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initDataBinding(inflater, container);
        return view;
    }

    private View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        ProjectTakePhotoViewModel viewModel = new ProjectTakePhotoViewModel(this, projectId);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_take_photo, container, false);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        return view;
    }
}