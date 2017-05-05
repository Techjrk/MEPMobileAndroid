package com.lecet.app.content;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lecet.app.R;
import com.lecet.app.databinding.FragmentProjectTakeCameraPhotoBinding;
import com.lecet.app.viewmodel.ProjectTakeCameraPhotoViewModel;


/**
 * Created by jasonm on 3/29/17.
 */

public class ProjectTakeCameraPhotoFragment extends Fragment {

    private static final String TAG = "ProjTakeCameraPhotoFrag";

    public static String IMAGE_PATH  = "com.lecet.app.content.ProjectTakeCameraPhotoFragment.imagePath";
    public static String FROM_CAMERA = "com.lecet.app.content.ProjectTakeCameraPhotoFragment.fromCamera";

    private FragmentProjectTakeCameraPhotoBinding binding;
    private FrameLayout frameLayout;


    public static ProjectTakeCameraPhotoFragment newInstance() {
        ProjectTakeCameraPhotoFragment fragmentInstance = new ProjectTakeCameraPhotoFragment();
        Bundle args = new Bundle();
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    public ProjectTakeCameraPhotoFragment() {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_take_camera_photo, container, false);
        frameLayout = (FrameLayout) binding.getRoot().findViewById(R.id.camera_preview);
        ProjectTakeCameraPhotoViewModel viewModel = new ProjectTakeCameraPhotoViewModel(this, frameLayout);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        return view;
    }



    public void onPause() {
        super.onPause();
        ProjectTakeCameraPhotoViewModel.releaseCamera();

    }

    @Override
    public void onResume(){
        super.onResume();
        ProjectTakeCameraPhotoViewModel.getCameraInstance();
    }


}