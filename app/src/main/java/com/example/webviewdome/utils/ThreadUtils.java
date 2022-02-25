package com.example.webviewdome.utils;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadUtils {
    public static Handler handler = new Handler(Looper.getMainLooper());
    private static final Executor executor = Executors.newSingleThreadExecutor();

    public ThreadUtils() {
    }

    public static void runOnNonUIThread(Runnable r) {
        executor.execute(r);
    }

    public static void runOnMainThread(Runnable r) {
        handler.post(r);
    }

    public static void runOnMainThread(Runnable r, long delayed) {
        handler.postDelayed(r, delayed);
    }
}
