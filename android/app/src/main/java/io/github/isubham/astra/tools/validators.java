package io.github.isubham.astra.tools;

import android.content.Context;

import java.util.regex.Pattern;

import io.github.isubham.astra.R;

public class validators {
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.equals("");
    }

    public static boolean isNumberPattern(String value) {
        return !Pattern.compile(".*[^0-9].*").matcher(value).matches();
    }

    public static String emailHasErrors(Context ctx, String email) {
        if (isNullOrEmpty(email)) return ctx.getString(R.string.error_email_empty);
        else if (!Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
                .matcher(email).matches()) {
            return ctx.getString(R.string.error_email_wrong_format);
        }
        return "";
    }

    public static String passwordHasErrors(Context ctx, String password) {
        if (isNullOrEmpty(password)) return ctx.getString(R.string.error_password_empty);
        if (password.length() < 8) return ctx.getString(R.string.error_password_length);
        return "";
    }

    public static String licenseHasErrors(Context ctx, String license) {
        if (isNullOrEmpty(license)) return ctx.getString(R.string.error_license_empty);
        return "";
    }

    public static String contactHasErrors(Context ctx, String contact) {
        if (isNullOrEmpty(contact)) return ctx.getString(R.string.error_contact_empty);
        else if (!isNumberPattern(contact) || contact.length() != 10 || Integer.parseInt(String.valueOf(contact.charAt(0))) < 6) {
            return ctx.getString(R.string.invalid_contact);
        }
        return Constants.EMPTY_STRING;
    }

    public static String aadharHasErrors(Context ctx, String aadhar) {
        if (isNullOrEmpty(aadhar)) return ctx.getString(R.string.error_aadhar_empty);
        else if (!isNumberPattern(aadhar) || aadhar.length() != 12) {
            return ctx.getString(R.string.invalid_aadhar);
        }
        return Constants.EMPTY_STRING;
    }

    public static String pincodeHasErrors(Context ctx, String pincode) {
        if (isNullOrEmpty(pincode)) return ctx.getString(R.string.error_pincode_empty);
        else if (!isNumberPattern(pincode) || pincode.length() != 6) {
            return ctx.getString(R.string.invalid_pincode);
        }
        return Constants.EMPTY_STRING;
    }

    public static String dateFormatErrors(Context ctx, String dob) {
        if (isNullOrEmpty(dob)) return ctx.getString(R.string.error_dob_empty);
        else if (!correctDateFormatPattern(dob)) {
            return ctx.getString(R.string.invalid_date);
        }
        return Constants.EMPTY_STRING;
    }

    private static boolean correctDateFormatPattern(String dob) {
        return Pattern.compile("\\d{2}-\\d{2}-\\d{4}").matcher(dob).matches();
    }

}
