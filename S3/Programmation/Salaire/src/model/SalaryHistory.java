package model;
import java.util.Date;

public class SalaryHistory {
    private int empno;
    private double salary;
    private double netSalary;
    private Date startDate;
    private Date endDate;
    private String ename;

    public SalaryHistory() {}

    public SalaryHistory(int empno, double salary, Date startDate, Date endDate, String ename) {
        this.empno = empno;
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ename = ename;
    }

    public SalaryHistory(int empno, double salary, double netSalary, Date startDate, Date endDate, String ename) {
        this.empno = empno;
        this.salary = salary;
        this.netSalary = netSalary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ename = ename;
    }

    // Getters and Setters
    public int getEmpno() {
        return empno;
    }

    public void setEmpno(int empno) {
        this.empno = empno;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

}
