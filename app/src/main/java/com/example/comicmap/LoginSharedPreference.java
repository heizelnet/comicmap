package com.example.comicmap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

public class LoginSharedPreference {

    public static final String KEY_COOKIE = "com.dalgonakit.key.cookie";
    public static final String KEY_COOKIE2 = "com.dalgonakit.key.cookies";

///////////////////////////////////////////////////////////////////////////////////////////
/*
    private static LoginSharedPreference dsp = null;
    public static LoginSharedPreference getInstanceOf(Context c){
        if(dsp==null){
            dsp = new LoginSharedPreference(c);
        }
        return dsp;
    }
 */
///////////////////////////////////////////////////////////////////////////////////////////
    private SharedPreferences pref;
    private Context mContext;
    public LoginSharedPreference() {
        mContext = MyApplication.getAppContext();
        pref = mContext.getSharedPreferences("LOG_INFO", Activity.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return pref.getString(key, null);
    }

    public void putHashSet(String key, HashSet<String> set){
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, set);
        editor.apply();
    }

    public HashSet<String> getHashSet(String key, HashSet<String> dftValue){
        try {
            return (HashSet<String>)pref.getStringSet(key, dftValue);
        } catch (Exception e) {
            e.printStackTrace();
            return dftValue;
        }
    }

}
