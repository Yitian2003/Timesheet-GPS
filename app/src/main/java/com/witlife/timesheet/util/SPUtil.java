package com.witlife.timesheet.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.witlife.timesheet.R;
import com.witlife.timesheet.model.JobModel;
import com.witlife.timesheet.model.UserProfileModel;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by yitian on 19/05/2017.
 */

public class SPUtil {

    public static String SAVE_JOB_MODEL = "Job Model";
    public static String PROFILE_MODEL = "Profile Model";

    public static UserProfileModel readUserModel(Context context){
        SharedPreferences mPrefs = context.getSharedPreferences("Timesheet", Context.MODE_PRIVATE);
        String json = mPrefs.getString(PROFILE_MODEL, "");
        UserProfileModel userModel = null;

        if (json.isEmpty()) {
            userModel = new UserProfileModel();

        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                userModel = mapper.readValue(json,
                        new TypeReference<UserProfileModel>() {
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return userModel;
    }

    public static void writeUserModel(Context context, UserProfileModel userModel){
        SharedPreferences mPrefs = context.getSharedPreferences("Timesheet", Context.MODE_PRIVATE);
        String json = null;

        try {
            json = new ObjectMapper().writeValueAsString(userModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(PROFILE_MODEL, json);
        prefsEditor.commit();
    }

    public static TreeMap<Date, List<JobModel>> readFromSharePreferences(Context context){
        SharedPreferences mPrefs = context.getSharedPreferences("Timesheet", Context.MODE_PRIVATE);
        String json = mPrefs.getString(SAVE_JOB_MODEL, "");
        TreeMap<Date, List<JobModel>> jobMap = null;

        // read data to map
        if (json.isEmpty()) {
            jobMap = new TreeMap<>();

        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                jobMap = mapper.readValue(json,
                        new TypeReference<TreeMap<Date, List<JobModel>>>() {
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }

            TreeMap<Date, List<JobModel>> newMap = new TreeMap(Collections.reverseOrder());
            newMap.putAll(jobMap);
            jobMap = newMap;
        }

        return jobMap;
    }

    public static void writeToSharePreferences(Context context, TreeMap<Date, List<JobModel>> jobMap) {
        SharedPreferences mPrefs = context.getSharedPreferences("Timesheet", Context.MODE_PRIVATE);
        String json = null;

        try {
            json = new ObjectMapper().writeValueAsString(jobMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(SAVE_JOB_MODEL, json);
        prefsEditor.commit();
    }
}
