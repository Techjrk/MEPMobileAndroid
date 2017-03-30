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
import com.lecet.app.databinding.FragmentProjectSelectPhotoBinding;
import com.lecet.app.viewmodel.ProjectSelectPhotoViewModel;

/**
 * Created by jasonm on 3/29/17.
 */

public class ProjectSelectPhotoFragment extends Fragment {

    private static final String TAG = "ProjectSelectPhotoFrag";

    public static String PROJECT_ID = "com.lecet.app.content.ProjectSelectPhotoFragment.projectId";

    private FragmentProjectSelectPhotoBinding binding;
    private long projectId;

    public static ProjectSelectPhotoFragment newInstance(long projectId) {
        ProjectSelectPhotoFragment fragmentInstance = new ProjectSelectPhotoFragment();
        Bundle args = new Bundle();
        args.putLong(ProjectSelectPhotoFragment.PROJECT_ID, projectId);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    public ProjectSelectPhotoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectId = getArguments().getLong(ProjectSelectPhotoFragment.PROJECT_ID);
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
        ProjectSelectPhotoViewModel viewModel = new ProjectSelectPhotoViewModel(this, projectId);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_select_photo, container, false);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        return view;
    }
}