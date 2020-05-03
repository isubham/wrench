package io.github.isubham.astra.tools;

import android.content.Context;

import java.util.regex.Pattern;

import io.github.isubham.astra.R;

public class validators {
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.equals("");
    }

    public static String emailHasErrors(Context ctx, String email) {
        if(isNullOrEmpty(email)) return ctx.getString(R.string.error_email_empty);
        else if(! Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
                .matcher(email).matches())
        {
            return ctx.getString(R.string.error_email_wrong_format);
        }
        return "";
    }

    public static String passwordHasErrors(Context ctx, String password) {
        if(isNullOrEmpty(password)) return ctx.getString(R.string.error_password_empty);
        if(password.length() < 8) return ctx.getString(R.string.error_password_length);
        return "";
    }

    public static String licenseHasErrors(Context ctx, String license) {
        if(isNullOrEmpty(license)) return ctx.getString(R.string.error_license_empty);
        return "";
    }

}
