package com.lecet.app.content;

import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.data.api.LecetClient;
import com.lecet.app.data.storage.LecetSharedPreferenceUtil;
import com.lecet.app.databinding.ActivitySearchBinding;
import com.lecet.app.domain.SearchDomain;
import com.lecet.app.viewmodel.SearchViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.realm.Realm;

//userid: harry.camigla@domandtom.com
//password: 3nB72JTrRB7Uu4mFRpFppV6PN

public class SearchActivity extends AppCompatActivity {
    private final String TAG = "SearchActivity";
    TextView tvsearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
//        getFilterFromAsset();
        setContentView(R.layout.activity_search);
//        setContentView(R.layout.activity_search_1); //real UI coming from Sketch
  tvsearch = (TextView)findViewById(R.id.tvsearch);
        tvsearch.setText("Testing..");
        setupBindingSample4RecentView();
  //      setupBindingSample4Project();
    }
    private void setupBindingSample4Project() {
        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

//*** using data filter and input query     {for project search and company search}
        String sfilter="", squery="noel";
        sfilter = getFilterFromAsset("filters/filtersearch_project.txt"); //default filter for project
        //sfilter = "{}"; default filter for company

        //Testing for project search or company search;
        SearchViewModel viewModel =
                new SearchViewModel(this,
                        new SearchDomain(LecetClient.getInstance(),LecetSharedPreferenceUtil.getInstance(getApplication()),Realm.getDefaultInstance(),sfilter),
                    squery);
        binding.setViewModel(viewModel);

    } //end of setupBindingSample4Project

    private void setupBindingSample4RecentView() {
        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
//        SearchViewModel viewModel = new SearchViewModel(this, new SearchDomain(LecetClient.getInstance(),LecetSharedPreferenceUtil.getInstance(getApplication()),Realm.getDefaultInstance()));
//*** using data filter
String sfilter= getFilterFromAsset("filters/filtersearch_rviewed.txt");
        SearchViewModel viewModel = new SearchViewModel(this,
                new SearchDomain(LecetClient.getInstance(),LecetSharedPreferenceUtil.getInstance(getApplication()),Realm.getDefaultInstance(), sfilter));
        binding.setViewModel(viewModel);
    } //end of setupBindingSample4RecentView

    private String getFilterFromAsset(String sfile) {
        AssetManager am = getAssets();
        StringBuilder scontent = new StringBuilder();
        try {
            InputStream is =  am.open(sfile);
            BufferedReader br = new BufferedReader(  new InputStreamReader(is));

            String st="";
            while ( (st = br.readLine())!=null){
                scontent.append(st);
            }
        //    Log.d("FILTER","filter recently viewed:"+scontent.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return scontent.toString();
    } //end of getFilterFromAsset
}

