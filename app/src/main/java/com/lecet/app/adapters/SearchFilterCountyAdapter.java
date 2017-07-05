package com.lecet.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.lecet.app.R;
import com.lecet.app.data.models.County;
import com.lecet.app.data.models.SearchFilter;
import com.lecet.app.databinding.ListItemCountyBinding;
import com.lecet.app.viewmodel.SearchCountyViewModel;

import java.util.List;

/**
 * Created by getdevs on 05/07/2017.
 */

public class SearchFilterCountyAdapter extends RecyclerView.Adapter<SearchFilterCountyAdapter.SearchFilterCountyHolder> {
    private List<County> counties;
    private County selectedCounty;
    public SearchFilterCountyAdapter(List<County> counties){
        this.counties = counties;
    }
    @Override
    public SearchFilterCountyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemCountyBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()) , R.layout.list_item_county , parent, false);
        SearchFilterCountyHolder holder = new SearchFilterCountyHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(final SearchFilterCountyHolder holder, int position) {
        final County county = counties.get(position);
        SearchCountyViewModel searchCountyViewModel = new SearchCountyViewModel(county);
        holder.getBinding().setViewModel(searchCountyViewModel);
        holder.getBinding().cbCounty.setOnCheckedChangeListener(null);
        holder.getBinding().cbCounty.setChecked(county.isSelected());
        holder.getBinding().cbCounty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(selectedCounty != null){
                    selectedCounty.setSelected(false);
                }
                county.setSelected(isChecked);
                if(isChecked){
                    setSelectedCounty(county);
                }
                else{
                    setSelectedCounty(null);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return counties.size();
    }

    public void setSelectedCounty(County county){
        this.selectedCounty = county;
    }
    public County getSelectedCounty(){
        return selectedCounty;
    }
    public class SearchFilterCountyHolder extends RecyclerView.ViewHolder{
        private ListItemCountyBinding binding;
        public SearchFilterCountyHolder(ListItemCountyBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
        public ListItemCountyBinding getBinding(){
            return binding;
        }
    }
}
