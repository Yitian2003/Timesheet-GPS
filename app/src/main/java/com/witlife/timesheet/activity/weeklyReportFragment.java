package com.witlife.timesheet.activity;

import android.content.Intent;
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
import com.witlife.timesheet.adapter.ReportRecyclerAdapter;
import com.witlife.timesheet.model.JobModel;
import com.witlife.timesheet.util.DateUtil;
import com.witlife.timesheet.util.SPUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by yitian on 24/05/2017.
 */

public class WeeklyReportFragment extends Fragment {
    RecyclerView rvWeeklyReport;
    TreeMap<Date, List<JobModel>> jobMap = null;
    private List<Integer> reportItems = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_weekly_report, container, false);
        rvWeeklyReport = (RecyclerView) view.findViewById(R.id.rvWeeklyReport);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reportItems = new ArrayList<>();
        jobMap = SPUtil.readFromSharePreferences(getContext());
        int weekNo;
        int size;
        for (Date date : jobMap.keySet()) {
            weekNo = DateUtil.getWeekNo(date);
            if (reportItems.size() == 0) {
                reportItems.add(weekNo);
            } else {
                size = reportItems.size();
                int i;

                for (i = 0; i < size; i++) {

                    if (weekNo == reportItems.get(i)) {
                        break;
                    }
                }

                if (i==size) {
                    reportItems.add(weekNo);
                }
            }
        }

        ReportRecyclerAdapter reportRecyclerAdapter = new ReportRecyclerAdapter(getContext(), reportItems);
        rvWeeklyReport.setAdapter(reportRecyclerAdapter);
        rvWeeklyReport.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvWeeklyReport.setLayoutManager(linearLayoutManager);
    }
}
