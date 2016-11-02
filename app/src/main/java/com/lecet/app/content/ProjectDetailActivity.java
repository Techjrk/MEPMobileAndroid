package com.lecet.app.content;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lecet.app.R;
import com.lecet.app.adapters.ProjectDetailAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailActivity extends AppCompatActivity {

    // TODO: Test Adapter
    private ProjectDetailAdapter detailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        // TODO: Testing Only
        List<String> section1 = new ArrayList<>();
        section1.add("0");
        section1.add("1");
        section1.add("2");
        section1.add("3");
        section1.add("4");
        section1.add("5");
        section1.add("6");
        section1.add("7");
        section1.add("8");
        section1.add("9");

        List<String> section2 = new ArrayList<>();
        section2.add("10");
        section2.add("11");
        section2.add("12");
        section2.add("13");
        section2.add("14");
        section2.add("15");

        List<String> section3 = new ArrayList<>();
        section3.add("16");
        section3.add("17");

        List<String> section4 = new ArrayList<>();
        section4.add("18");

        List<List<String>> data = new ArrayList<>();
        data.add(section1);
        data.add(section2);
        data.add(section3);
        data.add(section4);

        detailAdapter = new ProjectDetailAdapter(data);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(detailAdapter);
    }


}
