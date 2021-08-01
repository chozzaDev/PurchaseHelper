package me.namee.purchasehelper;

import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class PublicInterface extends ScriptInterface {
    public PublicInterface(WebView view, PurchaseConfig config) {
        super(view, config);
    }

    @JavascriptInterface
    @Override
    public void getHtml(String html, String url) {
        super.getHtml(html, url);
        if (isIndex()) {
            clickMy();
            return;
        }
        if (isLoginPage()) {
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
    }

    @JavascriptInterface
    public void checkBuyPage(String url) {
        if (isBuyPage()) {
            buy();
            return;
        }
        sleep(100);
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
        return url.contains("/main.do");
    }

    public boolean isBuyPage() {
        return url.contains("selectOrderPay.do");
    }

    public boolean isCart() {
        return !isBuyPage() && url.contains("/goods/");
    }

    public boolean isMy() {
        return url.contains("mypage/selectMyPageIndex.do");
    }

    public boolean isLoginPage() {
        return html.contains("<span class=\"a-btn__text\">로그인</span>");
    }

    public boolean isClosed() {
        return !html.contains("id=\"detailBuyingBtn\"");
    }

    public void clickMy() {
        Util.toast(view.getContext(), "로그인 클릭");
        //class="a-icon__mypage"
        runScript("document.querySelector('.a-icon__mypage').click()");
    }

    public void login() {
        Util.toast(view.getContext(), "로그인");
        runScript(
                "document.getElementById('searchWebMemNo').value = '" + config.getPublicId() + "';" +
                        "document.getElementById('searchWebMemSecrtNo').value = '" + config.getPublicPw() + "';" +
                        "login.callMbrLogin();");
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
                view.loadUrl(config.publicUrl);
//                view.loadUrl("https://www.gongyoungshop.kr/goods/selectGoodsDetail.do?prdId=11453301");
            }
        });
    }

    public void clickBuy() {
        Util.toast(view.getContext(), "구매버튼 클릭");
        runScript("document.getElementById('detailBuyingBtn').click()");
//        runScript("document.querySelector('#prdUntList > li > label').click()");

        runScript("document.getElementById('btnBuyProduct').click()");
    }

    public void buy() {
        Util.toast(view.getContext(), "구매옵션 선택");
        runScript("document.querySelector('#btnBank').click(); " +
                "document.querySelector('#bankSelect').value = '020';" +
                "document.querySelector('#receiptNo').click();" +
                "document.querySelector('#agreeCheck').click();" +
                "doPaySubmitWithLoading();");
//        sleep(10000);
        stop();
    }
}
