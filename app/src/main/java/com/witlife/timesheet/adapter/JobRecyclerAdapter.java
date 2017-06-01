package com.witlife.timesheet.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.witlife.timesheet.R;
import com.witlife.timesheet.activity.AddJobActivity;
import com.witlife.timesheet.model.JobModel;

import java.util.List;

/**
 * Created by yitian on 5/05/2017.
 */

public class JobRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ListItem> items;
    public static String EDIT_JOB = "EDIT_JOB";
    public static String POSITION = "POSITION";
    private String IS_YEARLY = "IS_YEARLY";
    private boolean isYearly;

    public JobRecyclerAdapter (Context context, List<ListItem> items, boolean isYearly) {
        this.context = context;
        this.items = items;
        this.isYearly = isYearly;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ListItem.TYPE_HEADER: {
                View itemView = inflater.inflate(R.layout.item_header, parent, false);
                return new HeaderViewHolder(itemView);
            }
            case ListItem.TYPE_JOB: {
                View itemView = inflater.inflate(R.layout.item_job, parent, false);
                return new JobViewHolder(itemView);
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position){
        int viewType = getItemViewType(position);
        switch (viewType) {
            case ListItem.TYPE_HEADER: {
                HeaderItem header = (HeaderItem) items.get(position);
                HeaderViewHolder holder = (HeaderViewHolder) viewHolder;

                holder.tvDate.setText(header.getDate());
                break;
            }
            case ListItem.TYPE_JOB: {
                JobItem jobItem = (JobItem) items.get(position);
                JobModel job = jobItem.getJob();
                JobViewHolder holder = (JobViewHolder) viewHolder;

                holder.tvJobTime.setText(job.getStartTime() + "  " + job.getFinishTime());
                holder.tvLunch.setText(job.getLunch());
                holder.tvJobCode.setText(job.getJobCode());
                String locations[] = job.getLocation().split(", ");
                holder.tvLocation.setText(locations[0]);
                if (TextUtils.isEmpty(job.getFinishTime()) || TextUtils.isEmpty(job.getLocation())) {
                    holder.layoutJob.setBackgroundColor(context.getResources().getColor(R.color.unfinish_job));
                }
                break;
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public int getItemCount() {return items.size();}

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        }
    }

    private class JobViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvJobTime;
        TextView tvLunch;
        TextView tvJobCode;
        TextView tvLocation;
        LinearLayout layoutJob;

        JobViewHolder(View itemView) {
            super(itemView);
            tvJobTime = (TextView) itemView.findViewById(R.id.tvJobTime);
            tvLunch = (TextView) itemView.findViewById(R.id.tvLunch);
            tvJobCode = (TextView) itemView.findViewById(R.id.tvJobCode);
            tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);
            layoutJob = (LinearLayout) itemView.findViewById(R.id.layoutJob);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, AddJobActivity.class);
            int position = getAdapterPosition();

            JobItem jobItem = (JobItem) items.get(position);
            JobModel job = jobItem.getJob();
            intent.putExtra(POSITION, position);
            intent.putExtra(EDIT_JOB, job);
            intent.putExtra(IS_YEARLY, isYearly);
            context.startActivity(intent);
        }
    }
}


