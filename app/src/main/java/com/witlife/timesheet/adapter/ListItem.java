package com.witlife.timesheet.adapter;

public abstract class ListItem {

    public static final int TYPE_HEADER = 0;
	public static final int TYPE_JOB = 1;

    abstract public int getType();

} 