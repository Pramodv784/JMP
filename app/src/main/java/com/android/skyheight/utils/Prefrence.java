package com.android.skyheight.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefrence {

    private static Prefrence Prefrence;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefsEditor;

    public static Prefrence getInstance(Context context) {
        if (Prefrence == null) {
            Prefrence = new Prefrence(context);
        }
        return Prefrence;
    }

    private Prefrence(Context context) {
        sharedPreferences = context.getSharedPreferences("EShop",Context.MODE_PRIVATE);
    }

    public void saveData(String key,String value) {
        prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public void clear(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }


    public String getString(String my_lang, String s) {
        return my_lang;
    }
}