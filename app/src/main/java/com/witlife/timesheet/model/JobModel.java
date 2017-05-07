package com.witlife.timesheet.model;

import java.util.Date;

/**
 * Created by yitian on 5/05/2017.
 */

public class JobModel {
    private Date date;
    private String dateInWeek;
    private Date startTime;
    private Date finishTime;
    private int lunch;
    private double totalHours;
    private double contractHours;
    private double serviceHours;
    private String jobCode;
    private String location;

    public Date getDate() {return date;}
    public void setDate(Date date) {this.date = date;}
    public String getDateInWeek() {return dateInWeek;}
    public void setDateInWeek(String dateInWeek) {this.dateInWeek = dateInWeek;}
    public Date getStartTime() {return startTime;}
    public void setStartTime(Date startTime) {this.startTime = startTime;}
    public Date getFinishTime() {return finishTime;}
    public void setFinishTime(Date finishTime) {this.finishTime = finishTime;}
    public int getLunch() {return lunch;}
    public void setLunch(int lunch) {this.lunch = lunch;}
    public double getTotalHours() {return totalHours;}
    public void setTotalHours(double totalHours) {this.totalHours = totalHours;}
    public double getContractHours() {return contractHours;}
    public void setContractHours(double contractHours) {this.contractHours = contractHours;}
    public double getServiceHours() {return serviceHours;}
    public void setServiceHours(double serviceHours) {this.serviceHours = serviceHours;}
    public String getLocation() {return location;}
    public void setLocation(String location) {this.location = location;}
    public String getJobCode() {return jobCode;}
    public void setJobCode(String jobCode) {this.jobCode = jobCode;}
}
