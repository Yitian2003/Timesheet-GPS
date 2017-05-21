package com.witlife.timesheet.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.witlife.timesheet.R;
import com.witlife.timesheet.adapter.HeaderItem;
import com.witlife.timesheet.adapter.JobItem;
import com.witlife.timesheet.adapter.ListItem;
import com.witlife.timesheet.adapter.RecyclerAdapter;
import com.witlife.timesheet.model.JobModel;
import com.witlife.timesheet.util.DateUtil;
import com.witlife.timesheet.util.EmailUtil;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends BaseActivity {

    TabLayout tlUserProfileTabs;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    private List<JobModel> jobs;
    TreeMap<Date, List<JobModel>> jobMap;
    private List<ListItem> items;
    TextView tvHoursWeek;
    TextView tvHoursToday;
    TextView tvUsername;

    FloatingActionButton btnAdd;
    public static String SAVE_JOB_MODEL = "Job Model";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupTabs();
        initialView();
        setOnListener();
    }

    private void initialView() {
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        recyclerView = (RecyclerView) findViewById(R.id.rvJobList);
        tvHoursToday = (TextView) findViewById(R.id.tvHoursToday);
        tvHoursWeek = (TextView) findViewById(R.id.tvHoursWeek);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        List<ListItem> items = new ArrayList<>();

        SharedPreferences mPrefs = getApplicationContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        String json = mPrefs.getString(SAVE_JOB_MODEL, "");

        // read data to map
        if (json.isEmpty()) {
            jobMap = new TreeMap<>();
            jobs = new ArrayList<>();

        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                jobMap = mapper.readValue(json,
                        new TypeReference<TreeMap<Date, List<JobModel>>>() {
                        });

                } catch (Exception e) {
                    e.printStackTrace();
            }
        }

        sortedMap();

        // set today total hours
        tvHoursToday.setText(String.format("%.2f", calculateTodayHours()));
        // set weekly total hours
        tvHoursWeek.setText(String.format("%.2f", calculateWeekHours()));
        //tvHoursWeek.setText(String.valueOf(calculateWeekHours()));

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
            recyclerAdapter = new RecyclerAdapter(this, items);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    private void setOnListener() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddJobActivity.class);
                startActivity(intent);
            }
        });

        tlUserProfileTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               if(tab.getPosition() == 3) {
                   sendEmail();
               }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

    private void sortedMap(){
        TreeMap<Date, List<JobModel>> newMap = new TreeMap(Collections.reverseOrder());
        newMap.putAll(jobMap);
        jobMap = newMap;
    }

    private double calculateTodayHours(){
        Date currentTime = Calendar.getInstance().getTime();
        double total = 0.0;
        for (Date date: jobMap.keySet()) {
            if (DateUtil.isSameDay(currentTime, date)) {
                List<JobModel> hoursList = jobMap.get(date);

                for (int i = 0; i < hoursList.size(); i++) {
                    if (!TextUtils.isEmpty(hoursList.get(i).getTotalHours())) {
                        total += Double.parseDouble(hoursList.get(i).getContractHours());
                    }
                }
            }
        }
        return total;
    }

    private double calculateWeekHours(){
        Date currentDate = Calendar.getInstance().getTime();
        double total = 0.0;
        for (Date date: jobMap.keySet()){
            if(DateUtil.isSameWeek(currentDate, date)){
                List<JobModel> hoursList = jobMap.get(date);

                for (int i = 0;i < hoursList.size(); i++){
                    if (!TextUtils.isEmpty((hoursList.get(i).getTotalHours()))) {
                        total += Double.parseDouble(hoursList.get(i).getContractHours());
                    }
                }
            }
        }
        return total;
    }

    private TreeMap<Date, List<JobModel>> getWeeklyData(int weekOfYear){
        TreeMap<Date, List<JobModel>> weeklyJobMap = new TreeMap<>();
        List<JobModel> weeklyJobs;
        Calendar c1 = Calendar.getInstance();
        c1.setFirstDayOfWeek(Calendar.MONDAY);

        for(Date date : jobMap.keySet()){
            c1.setTime(date);

            if (c1.get(Calendar.WEEK_OF_YEAR) == weekOfYear){
                weeklyJobs = jobMap.get(date);
                weeklyJobMap.put(date, weeklyJobs);
            }
        }
        return weeklyJobMap;
    }

    public void sendEmail(){
        String[] TO = {"yitianchang@gmail.com"};
        String[] CC = {""};
        String emailBody = EmailUtil.createEmailBody(); // create email body
        EmailUtil.saveExcelFile(this, getWeeklyData(21)); // create email attachment

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Weekly Timesheet from " + tvUsername.getText());
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
        //emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        File file = new File(this.getExternalFilesDir(null), "timesheet.xls");
        if (!file.exists() || !file.canRead()) {
            finish();
            return;
        }

        Uri uri = Uri.parse("file://" + file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            //startActivity(emailIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


}
