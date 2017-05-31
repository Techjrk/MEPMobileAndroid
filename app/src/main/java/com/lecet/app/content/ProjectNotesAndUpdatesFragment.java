package com.lecet.app.content;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.databinding.FragmentProjectNotesAndUpdatesBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectNotesAndUpdatesViewModel;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;

/**
 * Created by jasonm on 3/9/17.
 */

public class ProjectNotesAndUpdatesFragment extends Fragment {

    private static final String TAG = "ProjectNotesUpdatesFrag";

    private ProjectNotesFragmentDataSource dataSource;
    private ProjectNotesFragmentListener listener;

    private ProjectNotesAndUpdatesViewModel viewModel;
    private long projectId;

    public static ProjectNotesAndUpdatesFragment newInstance(long projectId) {

        ProjectNotesAndUpdatesFragment fragmentInstance = new ProjectNotesAndUpdatesFragment();
        Bundle args = new Bundle();
        args.putLong(PROJECT_ID_EXTRA, projectId);
        fragmentInstance.setArguments(args);

        return fragmentInstance;
    }

    public ProjectNotesAndUpdatesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectId = getArguments().getLong(PROJECT_ID_EXTRA);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            dataSource = (ProjectNotesFragmentDataSource) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ProjectNotesFragmentDataSource");
        }

        try {
            listener = (ProjectNotesFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ProjectNotesFragmentListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewModel = new ProjectNotesAndUpdatesViewModel(this, projectId, dataSource.domainDataSource(), listener);

        FragmentProjectNotesAndUpdatesBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_notes_and_updates, container, false);
        binding.setViewModel(viewModel);

        viewModel.onCreateView(binding.getRoot());

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        viewModel.onActivityResult(requestCode, resultCode, data);
    }

    public interface ProjectNotesFragmentDataSource {

        ProjectDomain domainDataSource();
    }

    public interface ProjectNotesFragmentListener {

        void onPhotosAndNotesUpdated(int count);
    }
}