package me.namee.purchasehelper;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class NaverInterface {
    WebView view;
    PurchaseConfig config;
    String html;
    String url;
    boolean isStop;

    public NaverInterface(WebView view, PurchaseConfig config) {
        this.view = view;
        this.config = config;
    }

    @JavascriptInterface
    public void getHtml(String html, String url) {
        this.html = html;
        this.url = url;
        if (isStop) return;
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
                clickBuy();
            }
            return;
        }
        if (isBuyPage()) {
            checkBuyPage(url);
            return;
        }
        refresh();
    }
    @JavascriptInterface
    public void checkBuyPage(String url) {
        if (isBuyPage()) {
            buy();
        }
        sleep(200);
        runScript("javascript:window.Android.checkBuyPage(location.href)");
    }

    public void sleep() {
        System.out.println((long) (config.getRefreshTime() * 1000));
        sleep((long) (config.getRefreshTime() * 1000));
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    public boolean isIndex() {
        return html.contains("<a href=\"#\" class=\"info MM_LOGINOUT\" onclick=\"nclk(this,'fot.login','','');\">로그인</a>");
    }

    public boolean isBuyPage() {
        return url.contains("m.pay.naver.com/o/orderSheet");
    }

    public boolean isCart() {
        return url.contains("/products/");
    }

    public boolean isMy() {
        return !isCart() && html.contains("<div class=\"MM_MYSPACE_THUMB lg_user_thumb\">")
                && html.contains("<a href=\"#\" class=\"info MM_LOGINOUT\" onclick=\"nclk(this,'fot.logout','','');\">로그아웃</a>");
    }

    public boolean isLogin() {
        return html.contains("<input type=\"text\" id=\"id\" name=\"id\"");
    }

    public boolean isClosed() {
        return !html.contains(">구매하기</button>");
    }

    public void clickMy() {
        Util.toast(view.getContext(), "로그인 클릭");
        runScript("document.querySelector('.info.MM_LOGINOUT').click()");
    }

    public void login() {
        Util.toast(view.getContext(), "로그인");
        runScript(
                "document.getElementById('id').value = '" + config.getNaverId() + "';" +
                        "document.getElementById('pw').value = '" + config.getNaverPw() + "';" +
                        "document.getElementById('log.login').click()");
    }

    public void refresh() {
        Util.toast(view.getContext(), "새로고침");
        runScript("location.reload()");
    }

    public void goCart() {
        Util.toast(view.getContext(), "구매페이지 이동");
        view.post(new Runnable() {
            @Override
            public void run() {
                view.loadUrl(config.naverUrl);
            }
        });
    }

    public void clickBuy() {
        Util.toast(view.getContext(), "구매버튼 클릭");
        runScript("var btns = [];document.querySelectorAll('button').forEach(function(btn) {if(btn.innerHTML.indexOf('구매하기') > -1) btns.push(btn)});" +
                "if(btns[0].clientHeight > 0 ) btns[0].click(); if(btns[1].clientHeight > 0) btns[1].click();");
        checkBuyPage(url);
//        sleep(500);
//        runScript("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML, location.href)");
    }

    public void buy() {
        Util.toast(view.getContext(), "구매옵션 선택");
        runScript("document.querySelector('.paymethod > #basic').parentElement.children[1].click(); " +
                "document.querySelectorAll('strong').forEach((item)=>{if(item.innerHTML == '나중에 결제') item.parentElement.click();});" +
                "document.querySelector('#all_agree_btn').focus();document.querySelector('#all_agree_btn').click(); " +
                "document.querySelectorAll('span').forEach((item)=>{if(item.innerHTML == '주문하기') item.parentElement.click();});");
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
