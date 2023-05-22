package com.example.student.user.util;

public class PasswordValidator {

    public static boolean isValidPassword(String password) {
        if (password.length() < 7) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasSpecial = true;
            }else  if(!Character.isLetterOrDigit(c)){
                hasSpecial = true;
            }

            if (hasUppercase && hasLowercase && hasSpecial) {
                return true;
            }
        }

        return false; // if it hasn't returned true by now, it's invalid
    }
}