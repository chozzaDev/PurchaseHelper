package me.namee.purchasehelper;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import io.realm.Realm;

public class GMarketInterface {
    WebView view;
    PurchaseConfig config;
    String html;
    String url;
    boolean isStop;

    public GMarketInterface(WebView view, PurchaseConfig config) {
        this.view = view;
        this.config = config;
    }

    @JavascriptInterface
    public void getHtml(String html, String url) {
        this.html = html;
        this.url = url;
        if(isStop) return;
        if (isIndex()) {
            clickMy();
            return;
        }
        if (isLogin()) {
            login();
            return;
        }
        if (isMy()) {
            goCart();
            return;
        }
        if (isCart()) {
            if (isClosed()) {
                sleep();
                refresh();
            } else {
                buy();
            }
            return;
        }
        refresh();
    }

    public void sleep() {
        try {
            System.out.println((long) (config.getRefreshTime() * 1000));
            Thread.sleep((long) (config.getRefreshTime() * 1000));
        } catch (InterruptedException e) {
        }
    }

    public boolean isIndex() {
        return html.contains("<span class=\"for-a11y\">마이페이지 바로가기</span>");
    }

    public boolean isBuyPage() {
        return url.contains("/ko/order?orderIdx=");
    }

    public boolean isCart() {
        return !isBuyPage() && html.contains("<h2 class=\"h_page\" id=\"simple_header_bar_title\">장바구니</h2>");
    }

    public boolean isMy() {
        return !isCart() && html.contains("<em class=\"cart_num\" id=\"simple_cartIcon\"");
    }

    public boolean isLogin() {
        return html.contains("id=\"id\" placeholder=\"아이디\"");
    }

    public boolean isClosed() {
        return html.contains("class=\"section item_soldout\"");
    }

    public void clickMy() {
        runScript("document.querySelectorAll('.list-item__menu > a')[3].click()");
    }

    public void login() {
        runScript(
                "document.getElementById('id').value = '" + config.getGmarketId() + "';" +
                        "document.getElementById('pwd').value = '" + config.getGmarketPw() + "';" +
                        "document.getElementById('btnLogin').click()");
    }

    public void refresh() {
        runScript("location.reload()");
    }

    public void goCart() {
        runScript("document.querySelector('[id=simple_cartIcon]').parentElement.click()");
    }

    public void buy() {
        runScript(
                "var chk = document.querySelector('input[type=checkbox]'); " +
                        "if(!chk.checked) chk.click();" +
                        "document.querySelector('.btn_submit').click();");
    }

    public void selectBank() {
        runScript(
                "       document.getElementById('payChk2').focus();" +
                        "document.getElementById('payChk2').click();" +
                        "document.getElementById('chk_1_2').focus();" +
                        "document.getElementById('chk_1_2').click();" +
                        "document.querySelector('.sp_cart.ico_ibkbank').parentElement.focus();" +
                        "document.querySelector('.sp_cart.ico_ibkbank').parentElement.click();" +
                        "document.querySelector('[data-ng-bind=paymentButtonTitle]').click();");
//        runScript("document.querySelector('[data-ng-bind=paymentButtonTitle]').click();");
//        runScript(
//                "document.querySelectorAll('.box__payment-wrap ul li a')[1].click();" +
//                        "document.querySelector('.logo.logo_woori').parentElement.click();");
//                        "document.querySelector('[data-ng-bind=paymentButtonTitle]').click();");

    }

    private void runScript(final String script) {
        view.post(new Runnable() {
            @Override
            public void run() {
                view.loadUrl("javascript:var runbuy = function() { try {" + script + "} catch(e) { setTimeout(runbuy, 200); }}; runbuy();");
            }
        });
    }

    public void stop() {
        isStop = true;
    }
}
