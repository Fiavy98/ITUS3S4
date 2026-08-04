package model;
import java.util.Date;

public class Employee { 
   private int empno;
    private String ename;
    private String job;
    private double sal;
    private Date hiredate;
    private int deptno;
    
    public Employee() {}
    public Employee(int empno, String ename, String job, double sal, Date hiredate, int deptno) {
        this.empno = empno;
        this.ename = ename;
        this.job = job;
        this.sal = sal;
        this.hiredate = hiredate;
        this.deptno = deptno;
    }
    
    @Override
    public String toString() {
        return String.format("%d - %s", empno, ename);
    }
    // Getters and Setters
    public int getEmpno() {
        return empno;        
    }
    public void setEmpno(int empno) {
        this.empno = empno;
    }
    public String getEname() {
        return ename;
    }
    public void setEname(String ename) {
        this.ename = ename;
    }
    public String getJob() {
        return job;
    }
    public void setJob(String job) {
        this.job = job;
    }
    public double getSal() {
        return sal;
    }
    public void setSal(double sal) {
        this.sal = sal;
    }
    public Date getHiredate() {
        return hiredate;
    }
    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }
    public int getDeptno() {
        return deptno;
    }
    public void setDeptno(int deptno) {
        this.deptno = deptno;
    }


}
