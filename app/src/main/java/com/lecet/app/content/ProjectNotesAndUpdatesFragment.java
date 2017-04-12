package com.lecet.app.content;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.FragmentProjectNotesAndUpdatesBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectNotesAndUpdatesViewModel;

import java.util.List;

import io.realm.Realm;

import static com.lecet.app.content.ProjectDetailActivity.PROJECT_ID_EXTRA;

/**
 * Created by jasonm on 3/9/17.
 */

public class ProjectNotesAndUpdatesFragment extends Fragment {

    private static final String TAG = "ProjectNotesUpdatesFrag";

    private List<ProjectNote> notes;
    private FragmentProjectNotesAndUpdatesBinding binding;
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

        Log.d(TAG, "onCreate: projectId: " + projectId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //binding.getViewModel().fetchProjectNotes(/*binding.calendarView*/);   //TODO - enable
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = initDataBinding(inflater, container);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    private View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this.getContext()), Realm.getDefaultInstance());
        ProjectNotesAndUpdatesViewModel viewModel = new ProjectNotesAndUpdatesViewModel(this, projectId, projectDomain);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_notes_and_updates, container, false);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        return view;
    }
}