package com.witlife.timesheet.adapter;

import android.support.annotation.NonNull;

import com.witlife.timesheet.util.DateUtil;

import java.util.Date;

public class HeaderItem extends ListItem {

	@NonNull
	private Date date;

	public HeaderItem(@NonNull Date date) {
		this.date = date;
	}

	@NonNull
	public String getDate() {
		return DateUtil.getDateAndDayOfWeek(date);
	}

	// here getters and setters
	// for title and so on, built
	// using date

	@Override
	public int getType() {
		return TYPE_HEADER;
	}

}