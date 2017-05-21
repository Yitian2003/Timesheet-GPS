package com.witlife.timesheet.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yitian on 5/05/2017.
 */

public class JobModel implements Serializable {
    private Date date;
    private String dateString;
    private String dayInWeek;
    private String startTime;
    private String finishTime;
    private String lunch;
    private String totalHours;
    private String contractHours;
    private String serviceHours;
    private String jobCode;
    private String location;

    public Date getDate() {return date;}
    public void setDate(Date date) {this.date = date;}
    public String getDateString() {return dateString;}
    public void setDateString(String dateString) {this.dateString = dateString;}
    public String getDayInWeek() {return dayInWeek;}
    public void setDayInWeek(String dayInWeek) {this.dayInWeek = dayInWeek;}
    public String getStartTime() {return startTime;}
    public void setStartTime(String startTime) {this.startTime = startTime;}
    public String getFinishTime() {return finishTime;}
    public void setFinishTime(String finishTime) {this.finishTime = finishTime;}
    public String getLunch() {return lunch;}
    public void setLunch(String lunch) {this.lunch = lunch;}
    public String getTotalHours() {return totalHours;}
    public void setTotalHours(String totalHours) {this.totalHours = totalHours;}
    public String getContractHours() {return contractHours;}
    public void setContractHours(String contractHours) {this.contractHours = contractHours;}
    public String getServiceHours() {return serviceHours;}
    public void setServiceHours(String serviceHours) {this.serviceHours = serviceHours;}
    public String getLocation() {return location;}
    public void setLocation(String location) {this.location = location;}
    public String getJobCode() {return jobCode;}
    public void setJobCode(String jobCode) {this.jobCode = jobCode;}
}
