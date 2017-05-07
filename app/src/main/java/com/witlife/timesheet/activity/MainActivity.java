package com.witlife.timesheet.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.witlife.timesheet.R;
import com.witlife.timesheet.adapter.RecyclerAdapter;
import com.witlife.timesheet.model.JobModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    TabLayout tlUserProfileTabs;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    private List<JobModel> jobs;
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupTabs();
        initialView();
        setOnLinstener();
    }

    private void initialView(){

        jobs = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(this, jobs);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);

        recyclerView = (RecyclerView) findViewById(R.id.rvJobList);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void setOnLinstener(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddJobActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupTabs() {
        tlUserProfileTabs = (TabLayout) findViewById(R.id.tlUserProfileTabs);
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_location_on_white_24dp));
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_add));
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_today_white_24dp));
        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_access_time_white_24dp));
    }

}
