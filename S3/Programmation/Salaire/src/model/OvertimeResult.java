package model;

public class OvertimeResult {
    public final double nonTaxHours;
    public final double taxableHours;
    public final double ignoredHours;
    public final double nonTaxPay;
    public final double taxablePay;
    public final double totalPay;

    public OvertimeResult(double nonTaxHours, double taxableHours, double ignoredHours, double nonTaxPay, double taxablePay) {
        this.nonTaxHours = nonTaxHours;
        this.taxableHours = taxableHours;
        this.ignoredHours = ignoredHours;
        this.nonTaxPay = nonTaxPay;
        this.taxablePay = taxablePay;
        this.totalPay = nonTaxPay + taxablePay;
    }
}
