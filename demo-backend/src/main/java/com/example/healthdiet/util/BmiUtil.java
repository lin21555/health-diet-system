package com.example.healthdiet.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BmiUtil {

    private BmiUtil() {
    }

    public static BigDecimal calculate(BigDecimal heightCm, BigDecimal weightKg) {
        if (heightCm == null || weightKg == null || heightCm.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal heightM = heightCm.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        return weightKg.divide(heightM.multiply(heightM), 2, RoundingMode.HALF_UP);
    }

    public static String level(BigDecimal bmi) {
        if (bmi == null) {
            return "未知";
        }
        if (bmi.compareTo(new BigDecimal("18.5")) < 0) {
            return "偏瘦";
        }
        if (bmi.compareTo(new BigDecimal("24")) < 0) {
            return "正常";
        }
        if (bmi.compareTo(new BigDecimal("28")) < 0) {
            return "超重";
        }
        return "肥胖";
    }
}
