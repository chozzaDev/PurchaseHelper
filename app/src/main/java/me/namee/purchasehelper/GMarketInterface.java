package me.namee.purchasehelper;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import io.realm.Realm;

public class GMarketInterface extends ScriptInterface {
    public GMarketInterface(WebView view, PurchaseConfig config) {
        super(view, config);
    }

    private boolean goingPayment = false;

    @JavascriptInterface
    @Override
    public void getHtml(String html, String url) {
        super.getHtml(html, url);

        if (!isMy() && isIndex()) {
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
            buy();
            goingPayment = true;
        }

        if (goingPayment) {
            if (isBuyPage()) {
                selectBank();
            } else {
                sleep();
                refresh();
            }
            return;
        }

    }

    public void sleep() {
        try {
            System.out.println((long) (config.getRefreshTime() * 3000));
            Thread.sleep((long) (config.getRefreshTime() * 3000));
        } catch (InterruptedException e) {
        }
    }

    public boolean isIndex() {
        return html.contains("href=\"//mmyg.gmarket.co.kr/v2\"");
    }

    public boolean isBuyPage() {
        return url.contains("/ko/order?orderIdx=");
    }

    public boolean isCart() {
        return url.contains("cart.gmarket.co.kr/ko-m/cart");
    }

    public boolean isMy() {
        return url.contains("mmyg.gmarket.co.kr");
    }

    public boolean isLogin() {
        return url.contains("Login/Login");
    }

    public boolean isClosed() {
        return html.contains("class=\"section item_soldout\"");
    }

    public void clickMy() {
        runScript("document.querySelector('.link__myg').click()");
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
        runScript("location.href='http://cart.gmarket.co.kr/ko-m/cart';");
    }

    public void buy() {
        runScript(
                "var chk = document.querySelector('input[type=checkbox]'); " +
                        "if(!chk.checked) chk.click();" +
                        "document.querySelector('.btn_submit').click();" +
                        "document.querySelectorAll('span').forEach((item) => {" +
                        "   if(item.innerHTML == '판매불가 상품이 포함되어 있습니다. 다시 확인해 주세요.') {" +
                        "       setTimeout(() => {" +
                        "           location.reload();" +
                        "       }, " + config.refreshTime + ");" +
                        "   }" +
                        "});");
    }

    public void selectBank() {
        runScript(
                "       document.getElementById('payChk2').focus();" +
                        "document.getElementById('payChk2').click();" +
                        "document.getElementById('chk_1_2').focus();" +
                        "document.getElementById('chk_1_2').click();" +
                        "document.querySelector('.ico_ibkbank').parentElement.focus();" +
                        "document.querySelector('.ico_ibkbank').parentElement.click();" +
                        "document.querySelector('#payment-button').click();");
//        runScript("document.querySelector('[data-ng-bind=paymentButtonTitle]').click();");
//        runScript(
//                "document.querySelectorAll('.box__payment-wrap ul li a')[1].click();" +
//                        "document.querySelector('.logo.logo_woori').parentElement.click();");
//                        "document.querySelector('[data-ng-bind=paymentButtonTitle]').click();");

    }
}
