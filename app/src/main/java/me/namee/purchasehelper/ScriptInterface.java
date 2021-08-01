package me.namee.purchasehelper;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public abstract class ScriptInterface {
    WebView view;
    PurchaseConfig config;
    String html;
    String url;
    boolean isStop;

    @JavascriptInterface
    public void getHtml(String html, String url) {
        this.html = html;
        this.url = url;
        if (isStop) throw new RuntimeException("stop process");
    }

    public ScriptInterface(WebView view, PurchaseConfig config) {
        this.view = view;
        this.config = config;
    }

    protected void runScript(final String script) {
        view.post(new Runnable() {
            @Override
            public void run() {
                view.loadUrl("javascript:" +
                        "var findByInnerHTML = function (tag, text) {" +
                        "   var returnTag = null;" +
                        "   document.querySelectorAll(tag).forEach((node) => {" +
                        "       if(node.innerHTML == text) {" +
                        "           returnTag = node;" +
                        "           return false;" +
                        "       }" +
                        "   });" +
                        "   return returnTag;" +
                        "};" +
                        "var runbuy = function() { " +
                        "   try {" +
                        "   " + script + "" +
                        "   } catch(e) { " +
                        "       window.Android.error(e.message);" +
                        "       setTimeout(runbuy, 50); throw e;" +
                        "   }" +
                        "}; " +
                        "runbuy();");
            }
        });
    }

    public void stop() {
        isStop = true;
    }

    @JavascriptInterface
    public void error(String message) {
        System.out.println(message);
    }
}
