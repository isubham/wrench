package io.github.isubham.astra.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginPersistance {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String TOKEN = "token";
    public static final String GENERAL_USER_TOKEN = "general_user_token";
    public static final String EMAIL = "email";
    public static final String PROFILE_PIC = "profilePic";
    public static final String ID_FRONT = "idFront";
    public static final String ID_BACK = "idBack";

    public static void Save(String email, String token, Context context) {
        SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(TOKEN, token);
        editor.putString(EMAIL, email);
        editor.commit();
        editor.apply();
    }

    public static void Delete(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public static void update(String generalUserToken , String profilePic, String idFront, String idBack, Context context) {
        SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PROFILE_PIC, profilePic);
        editor.putString(ID_FRONT, idFront);
        editor.putString(ID_BACK, idBack);
        editor.putString(GENERAL_USER_TOKEN,generalUserToken);
        editor.commit();
        editor.apply();
    }

    public static String GetProfilePic(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(PROFILE_PIC, null);
    }

    public static String GetIdFront(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(ID_FRONT, null);
    }

    public static String GetIdBack(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(ID_BACK, null);
    }

    public static String GetToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(TOKEN, null);
    }

    public static String GetEmail(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(EMAIL, null);
    }

    public static String GetGeneralUserToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(GENERAL_USER_TOKEN, null);
    }


}
