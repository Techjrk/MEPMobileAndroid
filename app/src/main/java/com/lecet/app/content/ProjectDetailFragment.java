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
import com.lecet.app.data.models.Project;
import com.lecet.app.databinding.FragmentProjectLocationBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectDetailFragmentViewModel;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;

/**
 * Created by jasonm on 3/9/17.
 */

public class ProjectDetailFragment extends Fragment {

    private static final String TAG = "ProjectLocationFragment";

    private FragmentProjectLocationBinding binding;
    private ProjectDetailFragmentViewModel viewModel;
    private long projectId;

    private ProjectDetailFragmentDataSource dataSource;
    private ProjectDetailFragmentListener listener;

    public static ProjectDetailFragment newInstance(long projectId) {
        ProjectDetailFragment fragmentInstance = new ProjectDetailFragment();
        Bundle args = new Bundle();
        args.putLong(PROJECT_ID_EXTRA, projectId);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    public ProjectDetailFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectId = getArguments().getLong(PROJECT_ID_EXTRA);
        } else {

            throw new IllegalStateException(this.toString() + " must have a PROJECT_ID_EXTRA");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            dataSource = (ProjectDetailFragmentDataSource) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ProjectDetailFragmentDataSource");
        }

        try {
            listener = (ProjectDetailFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ProjectDetailFragmentListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_location, container, false);
        binding.setViewModel(viewModel);

        viewModel = new ProjectDetailFragmentViewModel(this, projectId, dataSource.domainDataSource(), listener);
        viewModel.onCreateView(binding.getRoot());

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.onPause();
    }

    public interface ProjectDetailFragmentDataSource {

        ProjectDomain domainDataSource();
    }

    public interface ProjectDetailFragmentListener {

        void onProjectDetailsReceived(Project project);
    }
}