package me.namee.purchasehelper;

import android.os.Bundle;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

public class PublicActivity extends AppCompatActivity {

    WebView view;
    PublicInterface publicInterface;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);
        view = findViewById(R.id.publicWebView);
        view.clearCache(true);
        view.clearHistory();
        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(view.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (publicInterface == null) return;
                publicInterface.url = url;
                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML, '" + url + "')");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                System.out.println(error.getDescription());
            }
        };
        WebChromeClient wcc = new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                System.out.println(consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Util.toast(view.getContext(), message);
                result.confirm();
                System.out.println(url);
                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML, '" + url + "')");
                return true;
            }
        };
        view = new WebView(this);
        view.setWebViewClient(client);
        view.setWebChromeClient(wcc);
        WebSettingUtil.config(view);
//        view.getSettings().setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
        view.getSettings().setLoadWithOverviewMode(true);
        view.getSettings().setUseWideViewPort(true);
        publicInterface = new PublicInterface(view, PurchaseConfig.get(realm).copy());
        view.addJavascriptInterface(publicInterface, "Android");
        setContentView(view);
        view.loadUrl("https://m.gongyoungshop.kr/");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        publicInterface.stop();
        publicInterface = null;
        view = null;
    }
}