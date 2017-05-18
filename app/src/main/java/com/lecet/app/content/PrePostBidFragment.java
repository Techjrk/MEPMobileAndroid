package com.lecet.app.content;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.FragmentPrePostBidBinding;
import com.lecet.app.domain.ProjectDomain;
import com.lecet.app.viewmodel.ProjectsNearMeViewModel;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrePostBidFragment extends Fragment {
static ProjectsNearMeViewModel viewModel;

    public static PrePostBidFragment newInstance(ProjectsNearMeViewModel vm) {
        PrePostBidFragment fragmentInstance = new PrePostBidFragment();
        viewModel = vm;
       // Bundle args = new Bundle();
       // args.putLong("BID_ID_EXTRA", bidId);
       // fragmentInstance.setArguments(args);
        return fragmentInstance;
    }
    public PrePostBidFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     //   TextView textView = new TextView(getActivity());
    //    textView.setText(R.string.hello_blank_fragment);
        View view = initDataBinding(inflater, container);
//        return textView;
          return view;
    }
    private FragmentPrePostBidBinding binding;
    private View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        ProjectDomain projectDomain = new ProjectDomain(LecetClient.getInstance(), LecetSharedPreferenceUtil.getInstance(this.getContext()), Realm.getDefaultInstance());
       // ProjectLocationViewModel viewModel = new ProjectLocationViewModel(this, 3, projectDomain);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pre_post_bid, container, false);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        //binding.getViewModel().init();
        return view;
    }
}
