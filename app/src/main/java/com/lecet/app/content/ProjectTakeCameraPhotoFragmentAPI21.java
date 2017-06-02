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
import com.lecet.app.databinding.FragmentProjectTakeCameraPhotoApi21Binding;
import com.lecet.app.viewmodel.ProjectTakeCameraPhotoViewModelApi21;


/**
 * Created by jasonm on 3/29/17.
 */

public class ProjectTakeCameraPhotoFragmentAPI21 extends Fragment {

    private static final String TAG = "TakeCamPhotoFragAPI21";
    public ProjectTakeCameraPhotoViewModelApi21 viewModel;
    private FragmentProjectTakeCameraPhotoApi21Binding binding;


    public static ProjectTakeCameraPhotoFragmentAPI21 newInstance() {
        ProjectTakeCameraPhotoFragmentAPI21 fragmentInstance = new ProjectTakeCameraPhotoFragmentAPI21();
        Bundle args = new Bundle();
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    public ProjectTakeCameraPhotoFragmentAPI21() {
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

    private View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_take_camera_photo_api_21, container, false);

        ProjectTakeCameraPhotoViewModelApi21 viewModel = new ProjectTakeCameraPhotoViewModelApi21(this, binding.textureViewApi21);
        binding.setViewModel(viewModel);
        this.viewModel = viewModel;
        View view = binding.getRoot();
        return view;
    }



    public void onPause() {
        super.onPause();
        if(viewModel != null) {
            viewModel.releaseCamera();
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        if(viewModel != null) {
            Log.d(TAG, "onResume: Called");
            viewModel.resetCamera();
        }
    }


}