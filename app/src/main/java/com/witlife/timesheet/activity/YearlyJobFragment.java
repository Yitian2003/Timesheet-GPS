package com.witlife.timesheet.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.witlife.timesheet.R;
import com.witlife.timesheet.adapter.HeaderItem;
import com.witlife.timesheet.adapter.JobItem;
import com.witlife.timesheet.adapter.JobRecyclerAdapter;
import com.witlife.timesheet.adapter.ListItem;
import com.witlife.timesheet.model.JobModel;
import com.witlife.timesheet.util.SPUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by yitian on 27/05/2017.
 */

public class YearlyJobFragment extends Fragment {

    RecyclerView rvJobList;
    JobRecyclerAdapter recyclerAdapter;
    List<ListItem> items;
    TreeMap<Date, List<JobModel>> jobMap = null;
    private boolean IS_YEARLY = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancState){
        View view = inflater.inflate(R.layout.fragment_weekly_job, container, false);
        rvJobList = (RecyclerView) view.findViewById(R.id.rvJobList);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        jobMap = SPUtil.readFromSharePreferences(getContext());
        items = new ArrayList<ListItem>(){} ;

        // convert map to list
        for (Date date : jobMap.keySet()) {
            HeaderItem header = new HeaderItem(date);
            items.add(header);

            for (JobModel job : jobMap.get(date)) {
                JobItem item = new JobItem(job);
                items.add(item);
            }
        }

        // set recycler adapter
        if (items != null) {
            recyclerAdapter = new JobRecyclerAdapter(getContext(), items, IS_YEARLY);
            rvJobList.setAdapter(recyclerAdapter);
            rvJobList.setItemAnimator(new DefaultItemAnimator());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rvJobList.setLayoutManager(linearLayoutManager);
        }
    }
}
