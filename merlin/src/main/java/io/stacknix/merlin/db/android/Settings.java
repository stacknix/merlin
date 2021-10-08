package io.stacknix.merlin.db.android;

import android.content.Context;

public class Settings {

    private static final String SETTINGS_DB_VERSION = "SETTINGS_DB_VERSION";

    public static int getDBVersion(Context context){
        return new Preferences(context).getInt(SETTINGS_DB_VERSION, 1);
    }

    public static void setDBVersion(Context context, int val){
        new Preferences(context).setInt(SETTINGS_DB_VERSION, val);
    }
}
