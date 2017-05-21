package com.witlife.timesheet.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.witlife.timesheet.model.JobModel;


/**
 * Created by yitian on 8/05/2017.
 */

public class FileUtil {

    public static void writeToFile(Context context, JobModel jobModel){

        String fileName = "timesheet";
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(jobModel);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public static ArrayList<JobModel> readFromFile(Context context){
        FileInputStream fileInputStream = null;
        ArrayList<JobModel> jobs = null;

            File file = context.getFileStreamPath("timesheet");
            if(file.exists()) {
                try {
                    fileInputStream = context.openFileInput("timesheet");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    jobs = (ArrayList<JobModel>) objectInputStream.readObject();
                    objectInputStream.close();
                    fileInputStream.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                return jobs;
            } else {
                return null;
            }


    }
}
