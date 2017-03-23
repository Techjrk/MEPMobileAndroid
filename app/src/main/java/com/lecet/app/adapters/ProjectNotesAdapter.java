package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.data.models.ProjectPhoto;
import com.lecet.app.databinding.ListItemProjectDetailImageBinding;
import com.lecet.app.databinding.ListItemProjectDetailNoteBinding;
import com.lecet.app.interfaces.ProjectAdditionalData;
import com.lecet.app.viewmodel.ProjectNoteViewModel;
import com.lecet.app.viewmodel.ProjectPhotoViewModel;

import java.util.List;

/**
 * Created by ludwigvondrake on 3/23/17.
 */

public class ProjectNotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "ProjectNotesAdapter";
    private static final int NOTE_VIEW_TYPE = 0;
    private static final int PHOTO_VIEW_TYPE = 1;

    List<ProjectAdditionalData> data;

    public ProjectNotesAdapter(List<ProjectAdditionalData> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
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
        switch (holder.getItemViewType()){
            case NOTE_VIEW_TYPE:
                ((ProjectNoteViewHolder) holder).binding.setViewModel(
                        new ProjectNoteViewModel((ProjectNote)data.get(position))
                );
                break;
            case PHOTO_VIEW_TYPE:
                ((ProjectImageViewHolder) holder).binding.setViewModel(
                        new ProjectPhotoViewModel((ProjectPhoto)data.get(position))
                );
                break;
            default:
                Log.e(TAG, "onBindViewHolder: Not Type of View");

        }
    }


    public int getImageViewType(int position){
        if(data.get(position) instanceof ProjectPhoto){
            return PHOTO_VIEW_TYPE;
        }
        if(data.get(position) instanceof ProjectNote) {
            return NOTE_VIEW_TYPE;
        }
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
