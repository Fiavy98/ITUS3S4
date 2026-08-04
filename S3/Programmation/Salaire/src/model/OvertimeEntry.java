package model;

import java.util.Date;

public class OvertimeEntry {
    private int id;
    private int employeeId;
    private Date periodStart; // week start or month start depending on periodType
    private String periodType; // WEEKLY, MONTHLY, YEARLY
    private int year;
    private int month; // 1-12 for monthly/weekly
    private double hours;
    private Date createdAt;

    public OvertimeEntry() {}

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public Date getPeriodStart() { return periodStart; }
    public void setPeriodStart(Date periodStart) { this.periodStart = periodStart; }
    public String getPeriodType() { return periodType; }
    public void setPeriodType(String periodType) { this.periodType = periodType; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public double getHours() { return hours; }
    public void setHours(double hours) { this.hours = hours; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
