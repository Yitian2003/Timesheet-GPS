package com.witlife.timesheet.util;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.witlife.timesheet.model.JobModel;
import com.witlife.timesheet.model.UserProfileModel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by yitian on 21/05/2017.
 */

public class EmailUtil {

    @NonNull
    public static String createEmailBody() {
        StringBuilder builder = new StringBuilder();
        NumberFormat format = NumberFormat.getCurrencyInstance();
        //builder.append(DateUtil.getRangeOfWeek(weekNo) + "\n");
        builder.append("The excel file is attached");

        return builder.toString();
    }

    public static boolean saveExcelFile(Context context, TreeMap<Date, List<JobModel>> weeklyMap) {

        UserProfileModel user = SPUtil.readUserModel(context);

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return false;
        }

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Job Sheet");

        // Employee Name
        Row row = sheet1.createRow(0);
        c = row.createCell(0);
        c.setCellValue("Employee Name");
        c = row.createCell(1);
        c.setCellValue(user.getFirstName() + " "+ user.getLastName());

        // Employee No.
        row = sheet1.createRow(1);
        c = row.createCell(0);
        c.setCellValue("Employee No.");
        c = row.createCell(1);
        c.setCellValue(user.getEmployeeNo());

        // Week Ending
        row = sheet1.createRow(2);
        c = row.createCell(0);
        c.setCellValue("Week Ending");
        c = row.createCell(1);
        c.setCellValue(DateUtil.getDate(weeklyMap.firstKey()));
        c = row.createCell(2);
        c.setCellValue("To");
        c = row.createCell(3);
        c.setCellValue(DateUtil.getDate(weeklyMap.lastKey()));

        row = sheet1.createRow(3);

        // header
        row = sheet1.createRow(4);
        c = row.createCell(0);
        c.setCellValue("Day");
        c.setCellStyle(cs);
        c = row.createCell(1);
        c.setCellValue("Start");
        c.setCellStyle(cs);
        c = row.createCell(2);
        c.setCellValue("Finish");
        c.setCellStyle(cs);
        c = row.createCell(3);
        c.setCellValue("Lunch Mins");
        c.setCellStyle(cs);
        c = row.createCell(4);
        c.setCellValue("Total Hours");
        c.setCellStyle(cs);
        c = row.createCell(5);
        c.setCellValue("Contract Hours");
        c.setCellStyle(cs);
        c = row.createCell(6);
        c.setCellValue("Service");
        c.setCellStyle(cs);
        c = row.createCell(7);
        c.setCellValue("Job Code");
        c.setCellStyle(cs);
        c = row.createCell(8);
        c.setCellValue("Job Name");
        c.setCellStyle(cs);

        row = sheet1.createRow(5);

        // Job content
        int j = 0;
        for (Date date : weeklyMap.keySet()) {

            List<JobModel> jobModelList;
            jobModelList = weeklyMap.get(date);

            for (int i = 0; i < jobModelList.size(); i++) {
                JobModel jobModel = jobModelList.get(i);
                row = sheet1.createRow(6 + i + j);
                c = row.createCell(0);
                c.setCellValue(jobModel.getDayInWeek());
                c = row.createCell(1);
                c.setCellValue(jobModel.getStartTime());
                c = row.createCell(2);
                c.setCellValue(jobModel.getFinishTime());
                c = row.createCell(3);
                c.setCellValue(jobModel.getLunch());
                c = row.createCell(4);
                c.setCellValue(jobModel.getTotalHours());
                c = row.createCell(5);
                c.setCellValue(jobModel.getContractHours());
                c = row.createCell(6);
                c.setCellValue(jobModel.getServiceHours());
                c = row.createCell(7);
                c.setCellValue(jobModel.getJobCode());
                c = row.createCell(8);
                c.setCellValue(jobModel.getLocation());
            }
            j += jobModelList.size();
        }

        sheet1.setColumnWidth(0, (15 * 300));
        sheet1.setColumnWidth(1, (15 * 200));
        sheet1.setColumnWidth(2, (15 * 200));
        sheet1.setColumnWidth(3, (15 * 200));
        sheet1.setColumnWidth(4, (15 * 200));
        sheet1.setColumnWidth(5, (15 * 200));
        sheet1.setColumnWidth(6, (15 * 200));
        sheet1.setColumnWidth(7, (15 * 200));
        sheet1.setColumnWidth(8, (15 * 600));

        // Create a path where we will place our List of objects on external storage
        File file = new File(context.getExternalFilesDir(null), "timesheet.xls");
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        return success;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
