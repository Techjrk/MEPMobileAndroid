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
import com.lecet.app.databinding.FragmentProjectNotesBinding;
import com.lecet.app.databinding.IncludeProjectDetailAddHeaderBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectDetailAddHeaderViewModel;
import com.lecet.app.viewmodel.ProjectNotesViewModel;

import java.util.List;

import io.realm.Realm;

/**
 * Created by jasonm on 3/9/17.
 */

public class ProjectNotesFragment extends Fragment {

    private static final String TAG = "ProjectNotesFragment";

    public static String PROJECT_ID = "com.lecet.app.content.ProjectNotesFragment.projectId";

    private List<ProjectNote> notes;
    private FragmentProjectNotesBinding binding;
    private long projectId;

    public static ProjectNotesFragment newInstance(long projectId) {
        ProjectNotesFragment fragmentInstance = new ProjectNotesFragment();
        Bundle args = new Bundle();
        args.putLong(ProjectNotesFragment.PROJECT_ID, projectId);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    public ProjectNotesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectId = getArguments().getLong(ProjectNotesFragment.PROJECT_ID);
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
        ProjectNotesViewModel viewModel = new ProjectNotesViewModel(this, projectId, projectDomain);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_notes, container, false);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();

        //IncludeProjectDetailAddHeaderBinding binding2 = DataBindingUtil.inflate(inflater,R.layout.include_project_detail_add_header, container, false);
        //binding2.setViewModel(new ProjectDetailAddHeaderViewModel());//TODO: Setup Button Functionality.
        //binding.getViewModel().initiNoteView();
        return view;
    }
}