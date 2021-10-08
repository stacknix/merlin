package io.stacknix.merlin.db.android;

import android.util.Log;

import com.google.common.base.Joiner;


public class Logging {

    public static void e(String tag, Object... messages){
        Log.e(tag, Joiner.on(" ").join(messages));
    }

    public static void w(String tag, Object... messages){
        Log.w(tag, Joiner.on(" ").join(messages));
    }

    public static void d(String tag, Object... messages){
        Log.d(tag, Joiner.on(" ").join(messages));
    }

    public static void i(String tag, Object... messages){
        Log.i(tag, Joiner.on(" ").join(messages));
    }

}
