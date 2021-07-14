package com.cbslprojects.crm.CRM.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MyPrefences {

    private static MyPrefences mInstance;
    private static SharedPreferences sharedPreferences;

    public static final String PREFRENCE_USER_ID = "UserId";
    public static final String PREFRENCE_EMAIL_ID = "Emailid";
    public static final String PREFRENCE_USER_NAME = "username";
    public static final String PREFRENCE_MACHINE_ID = "MachineID";
    public static final String PREFRENCE_SOLE_ID = "soleID";
    public static final String PREFRENCE_BANK_ID = "bankID";
    public static final String PREFRENCE_REFERENCE_NO = "ReferenceNo";
    public static final String PREFRENCE_IMAGE_PATH = "Imagepath";
    public static final String PREFRENCE_NOTIFICATION = "resolveNotification";


    public static MyPrefences getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyPrefences();
            sharedPreferences = context.getSharedPreferences("MyPrefences_file", 0);
        }
        return mInstance;
    }

    public static MyPrefences getInstance(Context context,String file_name) {
        if (mInstance == null) {
            mInstance = new MyPrefences();
            sharedPreferences = context.getSharedPreferences(file_name, 0);
        }
        return mInstance;
    }

    public <T> void setList(String key, ArrayList<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        setString(key, json);
    }

    public  ArrayList getList(String key) {
        Gson gson = new Gson();
        String s = sharedPreferences.getString(key, null);
        ArrayList list = null;
        if (s != null)
            list = gson.fromJson(s, ArrayList.class);

        return list;

    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();

    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);

    }

    public void setInt(String key, int value) {
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();

    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);

    }

    public void setString(String key, String value) {
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);

    }

    public void setFloat(String key, float value) {
        Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();

    }

    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);

    }

    public void clearAllData() {
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
