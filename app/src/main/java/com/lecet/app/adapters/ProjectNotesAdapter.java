package com.lecet.app.adapters;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.data.models.ProjectPhoto;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ListItemProjectDetailImageBinding;
import com.lecet.app.databinding.ListItemProjectDetailNoteBinding;
import com.lecet.app.domain.UserDomain;
import com.lecet.app.interfaces.ProjectAdditionalData;
import com.lecet.app.viewmodel.ProjectNoteViewModel;
import com.lecet.app.viewmodel.ProjectPhotoViewModel;

import java.util.List;

import io.realm.Realm;

/**
 * Created by ludwigvondrake on 3/23/17.
 */

public class ProjectNotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "ProjectNotesAdapter";
    private static final int NOTE_VIEW_TYPE = 0;
    private static final int PHOTO_VIEW_TYPE = 1;
    private UserDomain userDomain;

    List<ProjectAdditionalData> data;

    public ProjectNotesAdapter(List<ProjectAdditionalData> data, Activity activity) {
        this.data = data;
        userDomain = new UserDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(activity), Realm.getDefaultInstance());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case NOTE_VIEW_TYPE: {
                ListItemProjectDetailNoteBinding binding = DataBindingUtil.inflate(inflater,
                        R.layout.list_item_project_detail_note, parent, false);
                return new ProjectNoteViewHolder(binding);
            }
            case PHOTO_VIEW_TYPE: {
                ListItemProjectDetailImageBinding binding = DataBindingUtil.inflate(inflater,
                        R.layout.list_item_project_detail_image, parent, false);
                return new ProjectImageViewHolder(binding);
            }
            default: {
                Log.e(TAG, "onBindViewHolder: unknown Type");
                ListItemProjectDetailNoteBinding binding = DataBindingUtil.inflate(inflater,
                        R.layout.list_item_project_detail_note, parent, false);
                return new ProjectNoteViewHolder(binding);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case PHOTO_VIEW_TYPE:
                ((ProjectImageViewHolder) holder).binding.setViewModel(
                        new ProjectPhotoViewModel((ProjectPhoto)data.get(position), userDomain)
                );
                break;
            case NOTE_VIEW_TYPE:
                ((ProjectNoteViewHolder) holder).binding.setViewModel(
                        new ProjectNoteViewModel((ProjectNote)data.get(position), userDomain)
                );
                break;
            default:
                Log.e(TAG, "onBindViewHolder: Not Type of View");
                break;

        }
    }

    @Override
    public int getItemViewType(int position){
        if(data.get(position) instanceof ProjectPhoto){
            return PHOTO_VIEW_TYPE;
        }
        if(data.get(position) instanceof ProjectNote) {
            return NOTE_VIEW_TYPE;
        }
        Log.e(TAG, "getImageViewType: Not Photo or Note");
        return -1;//if -1 then something snuck in that wasn't supposed to be here.
    }

    @Override
    public int getItemCount() {
        if(data == null) {
            return 0;
        }
        return data.size();
    }



    private class ProjectNoteViewHolder extends RecyclerView.ViewHolder{

        private final ListItemProjectDetailNoteBinding binding;

        public ProjectNoteViewHolder(ListItemProjectDetailNoteBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemProjectDetailNoteBinding getBinding() {return binding;}
    }

    private class ProjectImageViewHolder extends RecyclerView.ViewHolder{
        private final ListItemProjectDetailImageBinding binding;

        public ProjectImageViewHolder(ListItemProjectDetailImageBinding binding){
            super(binding.getRoot());

            this.binding = binding;
        }

        public ListItemProjectDetailImageBinding getBinding() {return binding;}
    }
}
