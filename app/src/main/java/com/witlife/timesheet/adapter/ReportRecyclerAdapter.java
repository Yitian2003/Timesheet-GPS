package com.witlife.timesheet.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.witlife.timesheet.R;
import com.witlife.timesheet.activity.MainActivity;
import com.witlife.timesheet.model.JobModel;
import com.witlife.timesheet.model.UserProfileModel;
import com.witlife.timesheet.util.DateUtil;
import com.witlife.timesheet.util.EmailUtil;
import com.witlife.timesheet.util.SPUtil;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by yitian on 24/05/2017.
 */

public class ReportRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Integer> reportItems;
    private Context context;

    public ReportRecyclerAdapter(Context context, List<Integer> reportItems){
        this.context = context;
        this.reportItems = reportItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position){
        int item = reportItems.get(position);
        ReportViewHolder holder = (ReportViewHolder) viewHolder;
        holder.tvReport.setText(DateUtil.getRangeOfWeek(item));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return reportItems.size();
    }

    private class ReportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvReport;

        ReportViewHolder(View view){
            super(view);
            tvReport = (TextView)view.findViewById(R.id.tvReport);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            sendEmail(reportItems.get(getAdapterPosition()));
        }
    }

    public void sendEmail(int weekNo){
        UserProfileModel user = SPUtil.readUserModel(context);

        String[] TO = {user.getEmail()};
        String[] CC = {""};
        String emailBody = EmailUtil.createEmailBody(); // create email body

        String userName = user.getFirstName() +
                " " + user.getLastName();
        EmailUtil.saveExcelFile(context, getWeeklyData(weekNo)); // create email attachment

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Weekly Timesheet from "+ userName + " ("+ DateUtil.getRangeOfWeek(weekNo) +")");
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        File file = new File(context.getExternalFilesDir(null), "timesheet.xls");
        if (!file.exists() || !file.canRead()) {
            //finish();
            return;
        }

        Uri uri = Uri.parse("file://" + file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }

    private TreeMap<Date, List<JobModel>> getWeeklyData(int weekOfYear){
        TreeMap<Date, List<JobModel>> jobMap = null;
        TreeMap<Date, List<JobModel>> weeklyJobMap = new TreeMap<>();
        List<JobModel> weeklyJobs;
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        jobMap = SPUtil.readFromSharePreferences(context);

        for(Date date : jobMap.keySet()){
            calendar.setTime(date);

            if (calendar.get(Calendar.WEEK_OF_YEAR) == weekOfYear){
                weeklyJobs = jobMap.get(date);
                weeklyJobMap.put(date, weeklyJobs);
            }
        }
        return weeklyJobMap;
    }
}

