package me.namee.purchasehelper;

import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebSettingUtil {
    public static void config(WebView view) {
        WebSettings settings = view.getSettings();
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Mobile Safari/537.36");
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportMultipleWindows(true);
    }
}
