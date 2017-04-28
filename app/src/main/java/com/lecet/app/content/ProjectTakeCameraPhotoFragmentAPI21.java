package com.lecet.app.content;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lecet.app.R;
import com.lecet.app.databinding.FragmentProjectTakeCameraPhotoApi21Binding;
import com.lecet.app.databinding.FragmentProjectTakeCameraPhotoBinding;
import com.lecet.app.viewmodel.ProjectTakeCameraPhotoViewModel;
import com.lecet.app.viewmodel.ProjectTakeCameraPhotoViewModelApi21;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;

/**
 * Created by jasonm on 3/29/17.
 */

public class ProjectTakeCameraPhotoFragmentAPI21 extends Fragment {

    private static final String TAG = "TakeCamPhotoFragAPI21";

    public static String IMAGE_PATH  = "com.lecet.app.content.ProjectTakeCameraPhotoFragment.imagePath";
    public static String FROM_CAMERA = "com.lecet.app.content.ProjectTakeCameraPhotoFragment.fromCamera";

    private FragmentProjectTakeCameraPhotoApi21Binding binding;
    private long projectId;


    public static ProjectTakeCameraPhotoFragmentAPI21 newInstance(long projectId) {
        ProjectTakeCameraPhotoFragmentAPI21 fragmentInstance = new ProjectTakeCameraPhotoFragmentAPI21();
        Bundle args = new Bundle();
        args.putLong(PROJECT_ID_EXTRA, projectId);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    public ProjectTakeCameraPhotoFragmentAPI21() {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_take_camera_photo_api_21, container, false);

        ProjectTakeCameraPhotoViewModelApi21 viewModel = new ProjectTakeCameraPhotoViewModelApi21(this, binding.textureViewApi21, projectId);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        return view;
    }



    public void onPause() {
        super.onPause();
        ProjectTakeCameraPhotoViewModelApi21.releaseCamera();

    }

    @Override
    public void onResume(){
        super.onResume();
    }


}