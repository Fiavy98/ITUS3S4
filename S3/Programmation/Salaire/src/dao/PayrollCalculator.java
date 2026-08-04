package dao;

import model.OvertimeResult;

public class PayrollCalculator {

    public static class PayrollResult {
        public final double salaireBrut;
        public final double cnaps;
        public final double osti;
        public final double irsa;
        public final double bonus;
        public final double netSalary;

        public PayrollResult(double salaireBrut, double cnaps, double osti, double irsa, double bonus,
                double netSalary) {
            this.salaireBrut = salaireBrut;
            this.cnaps = cnaps;
            this.osti = osti;
            this.irsa = irsa;
            this.bonus = bonus;
            this.netSalary = netSalary;
        }
    }

    // OvertimeResult moved to model.OvertimeResult

 
    public static double calculateCNAPS(double salaireBrut, double percentage) {
        // Calculate CNAPS amount from percentage, then clamp to configured min/max
        double amount = salaireBrut * (percentage / 100.0);
        final double MIN_CNAPS = 10.0;
        final double MAX_CNAPS = 60.0;
        if (amount < MIN_CNAPS) return MIN_CNAPS;
        if (amount > MAX_CNAPS) return MAX_CNAPS;
        return amount;
    }

    /**
     * Compute overtime pay split according to rules:
     * - Overtime counted per week, max 40 hours counted
     * - First 20 hours are non-taxable, next 20 hours are taxable
     * - Hours above 40 are ignored (not paid)
     * - Hourly base = salaire / 175.33
     */
    public static OvertimeResult computeOvertimePay(double salaireBrut, double overtimeHours) {
        if (overtimeHours <= 0) return new OvertimeResult(0,0,0,0,0);

        double baseHourly = salaireBrut / 175.33;

        double counted = Math.max(0, Math.min(40, overtimeHours));
        double nonTaxHours = Math.min(20, counted);
        double taxableHours = Math.min(20, Math.max(0, counted - 20));
        double ignoredHours = Math.max(0, overtimeHours - 40);

        double nonTaxPay = nonTaxHours * baseHourly;
        double taxablePay = taxableHours * baseHourly;

        return new OvertimeResult(nonTaxHours, taxableHours, ignoredHours, nonTaxPay, taxablePay);
    }

    /**
     * Compute full payslip including overtime. Taxable overtime is included in the gross
     * for CNAPS/IRSA calculation. Non-taxable overtime is added to net after taxes.
     * Parameters mirror computePayslip plus overtimeHours (per week).
     */
    public static PayrollResult computePayslipWithOvertime(double salaireBrut,
            double cnapsValue,
            double ostiValue,
            boolean cnapsIsPercentage,
            boolean ostiIsPercentage,
            double bonus,
            String mode,
            double overtimeHours) {

        // compute overtime split
        OvertimeResult ot = computeOvertimePay(salaireBrut, overtimeHours);

        // Taxable overtime is added to gross for contributions and tax
        double grossForContrib = salaireBrut + ot.taxablePay;

        // compute CNAPS: use percentage on grossForContrib and clamp
        double cnapsAmount;
        if (cnapsIsPercentage) {
            cnapsAmount = calculateCNAPS(grossForContrib, cnapsValue);
        } else {
            // if caller passes absolute value, still clamp to min/max
            cnapsAmount = calculateCNAPS(grossForContrib, (cnapsValue / Math.max(1.0, grossForContrib)) * 100.0);
        }

        // compute OSTI (we treat as before; typically 0)
        double ostiAmount = 0.0;
        if (mode != null && mode.equalsIgnoreCase("fixe")) {
            // in fixe mode ostiValue is used as absolute
            ostiAmount = ostiValue;
        } else {
            if (ostiIsPercentage) {
                ostiAmount = calculateOSTI(grossForContrib, ostiValue);
            } else {
                ostiAmount = ostiValue;
            }
        }

        // compute IRSA on (grossForContrib - cnaps - osti)
        double irsa = calculateIRSA(grossForContrib, cnapsAmount, ostiAmount);

        // net salary = grossForContrib - cnaps - osti - irsa + bonus + nonTaxOvertime
        double netSalary = grossForContrib - cnapsAmount - ostiAmount - irsa + bonus + ot.nonTaxPay;

        return new PayrollResult(salaireBrut, cnapsAmount, ostiAmount, irsa, bonus, netSalary);
    }


    public static double calculateOSTI(double salaireBrut, double percentage) {
        return salaireBrut * (percentage / 100.0);
    }

  
    public static double calculateIRSA(double salaireBrut, double cnaps, double osti) {
        // New progressive scale as requested:
        // 0 - 400 : fixed 15
        // 400 - 800 : 5% on amount above 400
        // 800 - 1200 : 10% on amount above 800
        // 1200 - 1600 : 15% on amount above 1200
        // 1600+ : 20% on amount above 1600
        double baseImposable = salaireBrut - cnaps - osti;
        double irsa = 0.0;

        if (baseImposable <= 0) {
            irsa = 0.0;
        } else if (baseImposable <= 400.0) {
            irsa = 15.0;
        } else if (baseImposable <= 800.0) {
            irsa = 15.0 + (baseImposable - 400.0) * 0.05;
        } else if (baseImposable <= 1200.0) {
            irsa = 15.0 + (800.0 - 400.0) * 0.05 + (baseImposable - 800.0) * 0.10;
        } else if (baseImposable <= 1600.0) {
            irsa = 15.0 + (800.0 - 400.0) * 0.05 + (1200.0 - 800.0) * 0.10 + (baseImposable - 1200.0) * 0.15;
        } else {
            irsa = 15.0 + (800.0 - 400.0) * 0.05 + (1200.0 - 800.0) * 0.10 + (1600.0 - 1200.0) * 0.15
                    + (baseImposable - 1600.0) * 0.20;
        }

        return Math.max(0.0, irsa);
    }


    public static PayrollResult computePayslip(double salaireBrut,
            double cnapsValue,
            double ostiValue,
            boolean cnapsIsPercentage,
            boolean ostiIsPercentage,
            double bonus,
            String mode) {

        double cnapsAmount = 0.0;
        double ostiAmount = 0.0;
        double irsa = 0.0;

        if (mode != null && mode.equalsIgnoreCase("fixe")) {
            cnapsAmount = cnapsValue;
            ostiAmount = ostiValue;
            irsa = 0.0;
        } else {
           
            if (cnapsIsPercentage) {
                cnapsAmount = calculateCNAPS(salaireBrut, cnapsValue);
            } else {
                cnapsAmount = cnapsValue;
            }

            if (ostiIsPercentage) {
                ostiAmount = calculateOSTI(salaireBrut, ostiValue);
            } else {
                ostiAmount = ostiValue;
            }

     
            irsa = calculateIRSA(salaireBrut, cnapsAmount, ostiAmount);
        }

        double netSalary = salaireBrut - cnapsAmount - ostiAmount - irsa + bonus;

        return new PayrollResult(salaireBrut, cnapsAmount, ostiAmount, irsa, bonus, netSalary);
    }

}
