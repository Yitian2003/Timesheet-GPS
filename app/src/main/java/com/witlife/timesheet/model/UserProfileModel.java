package com.witlife.timesheet.model;

/**
 * Created by yitian on 5/05/2017.
 */

public class UserProfileModel {
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String imageString;
    private String employeeNo;

    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public String getLastName() {return lastName;}
    public void setLastName(String firstName) {this.lastName = lastName;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}
    public String getImageString() {return imageString;}
    public void setImageString(String imageString) {this.imageString = imageString;}
    public String getEmployeeNo() {return employeeNo;}
    public void setEmployeeNo(String employeeNo) {this.employeeNo = employeeNo;}
}
