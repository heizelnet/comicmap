package com.hazelnut.comicmap;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;

public class LoginSharedPreference {

    String VERIFICATION_TOKEN = "IS_VERIFICATED";
    private SharedPreferences pref;
    private Context mContext;
    public LoginSharedPreference() {
        mContext = MyApplication.getAppContext();
        pref = mContext.getSharedPreferences("LOG_INFO", AppCompatActivity.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return pref.getString(key, null);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) { return pref.getInt(key, 0); }

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
