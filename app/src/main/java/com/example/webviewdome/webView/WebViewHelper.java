package com.example.webviewdome.webView;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;


import com.example.webviewdome.utils.ThreadUtils;
import java.util.ArrayList;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class WebViewHelper {
    private static final String TAG = "mugai";
    private static volatile String thizHtml = null;

    private static volatile WebView webViewObj = null;

    public static void init(ClassLoader xcalssloader) {
        try {
            XposedBridge.hookAllMethods(
                    XposedHelpers.findClass("com.xuexiang.xuidemo.base.webview.XPageWebViewFragment$4",xcalssloader),
                    "onPageFinished",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            Log.e(TAG, "call on onPageFinished  param【0】:  "+ param.args[0].getClass().getName());
                            Log.e(TAG, "call on url "+ param.args[1]);
                            try {
//                                XposedHelpers.callMethod(param.args[0],
//                                        "loadUrl",
//                                        "javascript:window.Xposed.getSource(document.getElementsByTagName('html')[0].innerHTML);");
                                String jscode = "javascript:var realXhr = \"RealXMLHttpRequest\";\n" +
                                        "\n" +
                                        "function hookAjax(proxy) {\n" +
                                        "    window[realXhr] = window[realXhr] || XMLHttpRequest;\n" +
                                        "    XMLHttpRequest = function () {\n" +
                                        "        var xhr = new window[realXhr];\n" +
                                        "        for (var attr in xhr) {\n" +
                                        "            var type = \"\";\n" +
                                        "            try {\n" +
                                        "                type = typeof xhr[attr]\n" +
                                        "            } catch (e) {\n" +
                                        "            }\n" +
                                        "            if (type === \"function\") {\n" +
                                        "                this[attr] = hookFunction(attr);\n" +
                                        "            } else {\n" +
                                        "                Object.defineProperty(this, attr, {\n" +
                                        "                    get: getterFactory(attr),\n" +
                                        "                    set: setterFactory(attr),\n" +
                                        "                    enumerable: true\n" +
                                        "                })\n" +
                                        "            }\n" +
                                        "        }\n" +
                                        "        this.xhr = xhr;\n" +
                                        "    };\n" +
                                        "    function getterFactory(attr) {\n" +
                                        "        return function () {\n" +
                                        "            var v = this.hasOwnProperty(attr + \"_\") ? this[attr + \"_\"] : this.xhr[attr];\n" +
                                        "            var attrGetterHook = (proxy[attr] || {})[\"getter\"];\n" +
                                        "            return attrGetterHook && attrGetterHook(v, this) || v;\n" +
                                        "         };\n" +
                                        "    };\n" +
                                        "    function setterFactory(attr) {\n" +
                                        "        return function (v) {\n" +
                                        "            var xhr = this.xhr;\n" +
                                        "            var that = this;\n" +
                                        "            var hook = proxy[attr];\n" +
                                        "            if (typeof hook === \"function\") {\n" +
                                        "                xhr[attr] = function () {\n" +
                                        "                    proxy[attr](that) || v.apply(xhr, arguments);\n" +
                                        "                };\n" +
                                        "            } else {\n" +
                                        "                var attrSetterHook = (hook || {})[\"setter\"];\n" +
                                        "                v = attrSetterHook && attrSetterHook(v, that) || v;\n" +
                                        "                try {\n" +
                                        "                    xhr[attr] = v;\n" +
                                        "                } catch (e) {\n" +
                                        "                    this[attr + \"_\"] = v;\n" +
                                        "                }\n" +
                                        "            }\n" +
                                        "        };\n" +
                                        "    }\n" +
                                        "    function hookFunction(fun) {\n" +
                                        "        return function () {\n" +
                                        "            var args = [].slice.call(arguments);\n" +
                                        "            if (proxy[fun] && proxy[fun].call(this, args, this.xhr)) {\n" +
                                        "                return;\n" +
                                        "            }\n" +
                                        "            return this.xhr[fun].apply(this.xhr, args);\n" +
                                        "        }\n" +
                                        "    }\n" +
                                        "    return window[realXhr];\n" +
                                        "};\n" +
                                        "\n" +
                                        "\n" +
                                        "function tryParseJson2(v,xhr){\n" +
                                        "    var contentType=xhr.getResponseHeader(\"content-type\")||\"\";\n" +
                                        "    if(xhr.responseURL.startsWith(\"https://movie.douban.com/j/search_subjects\")){\n" +
                                        "       window.Xposed.getAjax(v);\n" +
                                        "    }\n" +
                                        "    return v;\n" +
                                        "}\n" +
                                        "\n" +
                                        "\n" +
                                        "hookAjax({\n" +
                                        "    responseText: {\n" +
                                        "        getter: tryParseJson2\n" +
                                        "    },\n" +
                                        "    response: {\n" +
                                        "        getter: tryParseJson2\n" +
                                        "    }\n" +
                                        "});" ;
                                XposedHelpers.callMethod(param.args[0],
                                        "loadUrl",
                                        jscode);
                            } catch (Throwable e) {
                                Log.e(TAG,"调用loadUrl error "+e.getMessage());
                            }

                        }
                    }
            );
            XposedBridge.hookAllMethods(
                    XposedHelpers.findClass("com.xuexiang.xuidemo.base.webview.XPageWebViewFragment",xcalssloader),
                    "a",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            Log.d(TAG, "url:"+(String) param.args[1]);
                            param.args[1]="https://movie.douban.com/explore#!type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=20&page_start=0";
                            Log.d(TAG, "url:"+(String) param.args[1]);
                            super.afterHookedMethod(param);


                        }
                    }
            );

//            XposedBridge.hookAllMethods(
//                    XposedHelpers.findClass("com.xuexiang.xuidemo.base.webview.XPageWebViewFragment$4",xcalssloader),
//                    "onPageFinished",
//                    new XC_MethodHook() {
//                        @Override
//                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                            super.afterHookedMethod(param);
//                            Log.e(TAG, "call on onPageFinished  param【0】:  "+ param.args[0].getClass().getName());
//                            Log.e(TAG, "call on url "+ param.args[1]);
//                            try {
//                                String jsCode = "document.getElementsByTagName('html')[0].innerHTML";
////                                getHtmlString(param.args[0]);
//
////                                WebViewHelperzx wvhzx = new WebViewHelperzx((WebView) param.args[0]);
////
////                                wvhzx.executeJsCode("document.getElementById(\"index-kw\").value=1");
////                                wvhzx.clickByXpath("//button[@id=\"index-bn\"]");
////                                wvhzx.executeJsCode("jsCode",new ValueCallback<String>() {
////                                    @Override
////                                    public void onReceiveValue(String s) {
////                                        thizHtml = s;
////
////                                    }
////                                });
////                                Log.d(TAG, "thizHtml: "+thizHtml);
//
//
//
//
//
//                            } catch (Throwable e) {
//                                Log.e(TAG,"调用loadUrl error "+e.getMessage());
//                            }
//
//                        }
//                    }
//            );



            XposedBridge.hookAllMethods(
                    WebView.class,
                    "addJavascriptInterface",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            webViewObj = (WebView) param.thisObject;
                            initJavascriptInterface(param);


                        }
                    }
            );

            Log.e("mugai",">>>>>>>>>>>>>>>WebViewHelper->init finsh <<<<<<<<<<<<<<< ");

        } catch (Throwable e) {
            Log.e("mugai", e.toString());
        }
    }
    private static void getHtmlString(final Object webView) {
        XposedHelpers.callMethod(webView, "evaluateJavascript", "(function(){ return window.document.getElementsByTagName('html')[0].innerHTML})();", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(java.lang.String value) {
//                            Log.e(TAG, "[onReceiveValue] html:" + value);

                if (value.contains("加载中")) {
                    Log.e(TAG, "[onReceiveValue] html: 加载中");
                    value = value.replace("\\u003C", "<");
                    value = value.replace("\\&quot;", "");

                    int maxLogSize = 1000;
                    for (int i = 0; i <= value.length() / maxLogSize; i++) {
                        int start = i * maxLogSize;
                        int end = (i + 1) * maxLogSize;
                        end = end > value.length() ? value.length() : end;

                        Log.e(TAG, "[onReceiveValue] html:" + value.substring(start, end));
                    }
//                            getHtmlString(webView);
                } else {

                    value = value.replace("\\u003C", "<");
                    value = value.replace("\\&quot;", "");

                    int maxLogSize = 1000;
                    for (int i = 0; i <= value.length() / maxLogSize; i++) {
                        int start = i * maxLogSize;
                        int end = (i + 1) * maxLogSize;
                        end = end > value.length() ? value.length() : end;

                        Log.e(TAG, "[onReceiveValue] html:" + value.substring(start, end));
                    }

//                    AndroidAppHelper.currentApplication().getSharedPreferences("Demo", Context.MODE_PRIVATE).edit().putString("Remark",value).apply();
                }


            }
        });

    }
//    public void executeJsCode(final String jsCode, ValueCallback<String> valueCallback) {
//        if (valueCallback == null) {
//            valueCallback = new ValueCallback<String>() {
//                @Override
//                public void onReceiveValue(String s) {
//
//                }
//            };
//        }
//        Log.i("mugai", "execute js code: " + jsCode);
//        //android.webkit.WebView#evaluateJavascript(String script, @RecentlyNullable ValueCallback<String> resultCallback) {
//        XposedHelpers.callMethod(webView, "evaluateJavascript", jsCode, valueCallback);
//    }


    /**
     * 添加我们自己的 JavascriptInterface
     * <p>
     * https://www.androidos.net.cn/android/9.0.0_r8/xref/frameworks/base/core/java/android/webkit/WebView.java
     */
    private static void initJavascriptInterface(XC_MethodHook.MethodHookParam param) {
        if (isAddJavascriptInterface(param.thisObject.hashCode())) {
            Log.e(TAG, "添加我们自己的 JavascriptInterface  " + param.thisObject.getClass().getName());
            //not found XposedAppiumWebViewClient
            ThreadUtils.runOnMainThread(() ->
                    addJavascriptInterface(param.thisObject, new XposedJavaScriptLocalObj())
            );

        }
    }

    /**
     * add my JavascriptInterface
     */
    private static void addJavascriptInterface(Object thiz, Object bean) {
        Log.i(TAG, "start add my JavascriptInterface ");
        try {
            XposedHelpers.callMethod(thiz, "addJavascriptInterface", bean, "Xposed");
            Log.i(TAG, "add my JavascriptInterface sucess! ");
        } catch (Throwable e) {
            Log.i(TAG, "add my JavascriptInterface error! ");
        }

    }

    /**
     * 存放App全部的WebView HashCode
     * 用来标识是否添加过自己的 JavascriptInterface
     */
    private static final ArrayList<Integer> WebViewHashCode = new ArrayList<>();

    public static boolean isAddJavascriptInterface(int thizhashcode) {
        for (int hashcode : WebViewHashCode) {
            if (thizhashcode == hashcode) {
                return false;
            }
        }
        WebViewHashCode.add(thizhashcode);
        //没查找到则 return true;
        return true;
    }


    /**
     * 获取最后一次调用 onPageFinished 的WebView的内容
     */
    public static String getHtmlContent() {
        return thizHtml;
    }

    public static WebView getWebView() {
        return webViewObj;
    }


    static final class XposedJavaScriptLocalObj {
        @JavascriptInterface
        public void getSource(String html) {
            Log.i(TAG, "WebViewHelper替换html成功  ");
            Log.i(TAG, html);
            //替换最新更新的html内容
            thizHtml = html;
        }

        @JavascriptInterface
        public void getAjax(String json) {
            Log.i(TAG, "返回json成功");
            Log.i(TAG, json);
        }

    }

}
