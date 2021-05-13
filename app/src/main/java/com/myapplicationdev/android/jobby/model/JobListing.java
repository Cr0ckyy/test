package com.myapplicationdev.android.jobby.model;

public class JobListing {

    String jobTitle;
    String jobDescription;
    String expectedMonthlySalary;
    String qualification;
    String postedDate;
    String id;

    public JobListing(String jobTitle, String jobDescription, String expectedMonthlySalary, String qualification, String postedDate, String id) {
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.expectedMonthlySalary = expectedMonthlySalary;
        this.qualification = qualification;
        this.postedDate = postedDate;
        this.id = id;
    }


    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getExpectedMonthlySalary() {
        return expectedMonthlySalary;
    }

    public void setExpectedMonthlySalary(String expectedMonthlySalary) {
        this.expectedMonthlySalary = expectedMonthlySalary;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getpostedDate() {
        return postedDate;
    }

    public void setpostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
