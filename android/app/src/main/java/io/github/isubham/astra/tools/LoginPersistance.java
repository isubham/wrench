package io.github.isubham.astra.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginPersistance {

        public static final String MyPREFERENCES = "MyPrefs" ;

        public static void Save(String email, String token, Context context) {
            SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("token", token);
            editor.putString("email", email);
            editor.commit();
            editor.apply();
        }

        public static void Delete(Context context) {
            SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
        }

        public static String GetToken(Context context) {
            SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            return sp.getString("token", null);
        }

        public static String GetEmail(Context context) {
            SharedPreferences sp = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            return sp.getString("email", null);
        }


    }
