package com.witlife.timesheet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import com.witlife.timesheet.R;
import com.witlife.timesheet.model.JobModel;

import java.util.List;

/**
 * Created by bruce on 5/05/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private List<JobModel> jobs;

    public RecyclerAdapter (Context context, List<JobModel> jobs) {
        this.context = context;
        this.jobs = jobs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return  new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        final JobModel job = jobs.get(position);
    }

    @Override
    public int getItemCount() {return jobs.size();}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ViewHolder(View view, final RecyclerAdapter recyclerAdapter) {
            super(view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}


