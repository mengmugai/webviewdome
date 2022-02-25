package com.example.webviewdome;

import android.app.Application;
import android.util.Log;
import com.example.webviewdome.webView.WebViewHelper;

import java.util.ArrayList;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookEntry implements IXposedHookLoadPackage {
    private static final String TAG = "mugai";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        if (loadPackageParam.packageName.equals("com.xuexiang.xuidemo")) {
            try {
                hookAttach(loadPackageParam.classLoader);
            } catch (Throwable e) {
                Log.e("handleLoadPackage error ", e.toString());
            }
        }
    }

    private void hookAttach(ClassLoader xclassloader) {
        XposedHelpers.findAndHookMethod(Application.class,
                "onCreate",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Log.d(TAG, "onCreate");
                        WebViewHelper.init(xclassloader);
                    }
                });
    }


}


