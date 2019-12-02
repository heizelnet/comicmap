package com.example.comicmap;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class DataSharedPreference {

    private SharedPreferences pref;
    private Context mContext;
    public final String CHECKLIST_KEY = "checklist";
    public final String SEARCH_BOX = "search_list";
    public final String FAVORITE_KEY = "favorite_list";

    public DataSharedPreference() {
        mContext = MyApplication.getAppContext();
        pref = mContext.getSharedPreferences("DataSheet", AppCompatActivity.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return pref.getString(key, null);
    }

    public void setStringArrayList(String key, ArrayList<String> values) {
        SharedPreferences.Editor editor = pref.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    public ArrayList<String> getStringArrayList(String key) {
        String json = pref.getString(key, null);
        ArrayList<String> list = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    list.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
