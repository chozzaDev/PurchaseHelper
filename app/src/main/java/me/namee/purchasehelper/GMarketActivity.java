package me.namee.purchasehelper;

import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

public class GMarketActivity extends AppCompatActivity {

    WebView view;
    GMarketInterface gMarketInterface;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmarket);
        view = findViewById(R.id.gmarketWebView);
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
                if (gMarketInterface == null) return;
                gMarketInterface.url = url;
                if (gMarketInterface.isBuyPage()) {
                    gMarketInterface.selectBank();
                } else {
                    view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML, '" + url + "')");
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                System.out.println(error.getDescription());
            }
        };
        view = new WebView(this);
        view.setWebViewClient(client);
        WebSettingUtil.config(view);
        view.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        gMarketInterface = new GMarketInterface(view, PurchaseConfig.get(realm).copy());
        view.addJavascriptInterface(gMarketInterface, "Android");
        setContentView(view);
        view.loadUrl("https://m.gmarket.co.kr");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        gMarketInterface.stop();
        gMarketInterface = null;
        view = null;
    }
}