package io.stacknix.merlin.db.android;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

public class Preferences {

    private static final String PREF_FILE_KEY = "merlin.module";

    private final SharedPreferences sp;

    public Preferences(@NotNull Context context){
        this.sp = context.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE);
    }

    public String getString(String key, String defValue) {
        return this.sp.getString(key, defValue);
    }

    public void setString(String key, String defValue) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, defValue);
        editor.apply();
    }

    public int getInt(String key, int defValue) {
        return this.sp.getInt(key, defValue);
    }

    public void setInt(String key, int defValue) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, defValue);
        editor.apply();
    }
}
