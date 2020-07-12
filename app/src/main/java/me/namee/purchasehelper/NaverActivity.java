package me.namee.purchasehelper;

import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

public class NaverActivity extends AppCompatActivity {

    WebView view;
    NaverInterface naverInterface;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver);
        view = findViewById(R.id.naverWebView);
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
                if (naverInterface == null) return;
                naverInterface.url = url;
                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML, '" + url + "')");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                System.out.println(error.getDescription());
            }
        };
        view = new WebView(this);
        view.setWebViewClient(client);
        WebSettingUtil.config(view);
        naverInterface = new NaverInterface(view, PurchaseConfig.get(realm).copy());
        view.addJavascriptInterface(naverInterface, "Android");
        setContentView(view);
        view.loadUrl("https://m.naver.com");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        naverInterface.stop();
        naverInterface = null;
        view = null;
    }
}