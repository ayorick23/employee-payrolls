package com.payrollsystem.ui.utils;

import java.util.regex.Pattern;

public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return true; // Campo opcional
        // Patrón para teléfonos de El Salvador: ####-####
        Pattern phonePattern = Pattern.compile("^\\d{4}-\\d{4}$");
        return phonePattern.matcher(phone).matches();
    }
    
    public static boolean isPositiveNumber(String str) {
        try {
            double num = Double.parseDouble(str);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}