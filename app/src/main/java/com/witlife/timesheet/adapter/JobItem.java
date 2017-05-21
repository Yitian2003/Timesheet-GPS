package com.witlife.timesheet.adapter;

import android.support.annotation.NonNull;

import com.witlife.timesheet.model.JobModel;

public class JobItem extends ListItem {

	@NonNull
	private JobModel job;

	public JobItem(@NonNull JobModel job) {
		this.job = job;
	}

	@NonNull
	public JobModel getJob() {
		return job;
	}

	// here getters and setters
	// for title and so on, built
	// using event

	@Override
	public int getType() {
		return TYPE_JOB;
	}

}