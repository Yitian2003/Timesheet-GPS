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
import com.witlife.timesheet.util.DateUtil;
import com.witlife.timesheet.util.SPUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by yitian on 24/05/2017.
 */

public class WeeklyJobFragment extends Fragment {

    RecyclerView rvJobList;
    JobRecyclerAdapter recyclerAdapter;
    List<ListItem> items;
    TreeMap<Date, List<JobModel>> jobMap = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancState){
        View view = inflater.inflate(R.layout.fragment_weekly_job, container, false);
        rvJobList = (RecyclerView) view.findViewById(R.id.rvJobList);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);


        TreeMap<Date, List<JobModel>> weeklyJobMap = new TreeMap<>();
        List<JobModel> weeklyJobs;

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        jobMap = SPUtil.readFromSharePreferences(getContext());

        Date currentDate = Calendar.getInstance().getTime();
        int weekOfYear = DateUtil.getWeekNo(currentDate);

        for(Date date : jobMap.keySet()){
            calendar.setTime(date);
            if ((calendar.get(Calendar.WEEK_OF_YEAR)) == weekOfYear){
                weeklyJobs = jobMap.get(date);
                weeklyJobMap.put(date, weeklyJobs);
            }
        }
        TreeMap<Date, List<JobModel>> newMap = new TreeMap(Collections.reverseOrder());
        newMap.putAll(weeklyJobMap);
        weeklyJobMap = newMap;

        items = new ArrayList<ListItem>(){} ;
        // convert map to list
        for (Date date : weeklyJobMap.keySet()) {
            HeaderItem header = new HeaderItem(date);
            items.add(header);

            for (JobModel job : weeklyJobMap.get(date)) {
                JobItem item = new JobItem(job);
                items.add(item);
            }
        }

        // set recycler adapter
        if (items != null) {
            recyclerAdapter = new JobRecyclerAdapter(getContext(), items);
            rvJobList.setAdapter(recyclerAdapter);
            rvJobList.setItemAnimator(new DefaultItemAnimator());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rvJobList.setLayoutManager(linearLayoutManager);
        }
    }
}
