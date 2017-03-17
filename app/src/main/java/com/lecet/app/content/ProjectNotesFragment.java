package com.lecet.app.content;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.adapters.ProjectNotesAdapter;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.ProjectAdditionalInfo;
import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.data.models.ProjectPhoto;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.FragmentProjectNotesBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectNotesViewModel;

import java.util.Date;
import java.util.List;

import io.realm.Realm;

/**
 * Created by jasonm on 3/9/17.
 */

public class ProjectNotesFragment extends Fragment {

    private static final String TAG = "ProjectNotesFragment";

    public static String PROJECT_ID = "com.lecet.app.content.ProjectNotesFragment.projectId";

    private List<ProjectAdditionalInfo> notes;
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
        getSomeData();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new ProjectNotesAdapter(notes);
        recyclerView.setAdapter(adapter);

        //TODO: Create A ProjectNotesAdapter, The Adapter will take a ProjectNotes or ProjectPhotos objects.
        // Have the two objects implement a custom interface "ProjectAdditionalInfo"
        // ProjectNotes implements ProjectAdditionalInfo
        // Adapter will have a List<ProjectAdditionalInfo> data

        return view;
    }

    private View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this.getContext()), Realm.getDefaultInstance());
        ProjectNotesViewModel viewModel = new ProjectNotesViewModel(this, projectId, projectDomain);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_notes, container, false);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        //binding.getViewModel().initiNoteView();
        return view;
    }

    private void getSomeData(){
        notes.add(new ProjectNote(1L,"Random House",
                "I Really Like this Project, ITS SO AWESOME!", false, 35L, 32L, 68L,
                new Date(291156831000L),new Date(291156831000L)));
        notes.add(new ProjectNote(2L, "I Hate Construction",
                "It takes forever, enough said", false, 35L, 32L, 69L, new Date(1490388831000L),
                new Date(1490388831000L)));
        notes.add(new ProjectPhoto(3L, "I Snagged A Picture",
                "The Project has been going, I really like the new windows they put in, and the toilet. Very toilety",
                35L, 32L, 70L, new Date(System.currentTimeMillis() - 30000L),
                "drawable://" + R.drawable.sample_construction_site));
    }
}