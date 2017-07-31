package com.lecet.app.content;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.databinding.FragmentProjectSelectLibraryPhotoBinding;
import com.lecet.app.utility.Log;
import com.lecet.app.viewmodel.ProjectSelectLibraryPhotoViewModel;


/**
 * Created by jasonm on 3/29/17.
 */

public class ProjectSelectLibraryPhotoFragment extends Fragment {

    private static final String TAG = "ProjectSelectPhotoFrag";

    private ProjectSelectLibraryPhotoViewModel viewModel;
    private FragmentProjectSelectLibraryPhotoBinding binding;

    public static ProjectSelectLibraryPhotoFragment newInstance() {
        ProjectSelectLibraryPhotoFragment fragmentInstance = new ProjectSelectLibraryPhotoFragment();
        Bundle args = new Bundle();
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    public ProjectSelectLibraryPhotoFragment() {
    }

    public void initImageChooser() {
        if(viewModel != null) {
            viewModel.initImageChooser();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
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
    
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "onHiddenChanged: " + hidden);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(this.getArguments());
        Log.d(TAG, "onViewStateRestored");
    }


    private View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        viewModel = new ProjectSelectLibraryPhotoViewModel(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_select_library_photo, container, false);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        return view;
    }
}