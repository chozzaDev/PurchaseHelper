package me.namee.purchasehelper;

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
        if (isIndex() && !isLogin()) {
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
        return html.contains("<a id=\"btnHdrLogin\" class=\"login\">로그인</a>");
    }

    public boolean isBuyPage() {
        return url.contains("selectOrderPay.do");
    }

    public boolean isCart() {
        return !isBuyPage() && url.contains("/goods/");
    }

    public boolean isMy() {
        return !isBuyPage() && !isCart() && html.contains("<a id=\"btnHdrLogout\" class=\"logout\">로그아웃</a>");
    }

    public boolean isLogin() {
        return html.contains("<h4 class=\"h4 noPadding none\">로그인</h4>");
    }

    public boolean isClosed() {
        return !html.contains("<a id=\"btnBuyProduct\"");
    }

    public void clickMy() {
        Util.toast(view.getContext(), "로그인 클릭");
        runScript("document.querySelector('#btnHdrLogin').click()");
    }

    public void login() {
        Util.toast(view.getContext(), "로그인");
        runScript(
                "document.getElementById('searchWebMemNo').value = '" + config.getPublicId() + "';" +
                        "document.getElementById('searchWebMemSecrtNo').value = '" + config.getPublicPw() + "';" +
                        "document.getElementById('loginBtn').click();");
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
        runScript("document.getElementById('btnBuyProduct').click()");
    }

    public void buy() {
        Util.toast(view.getContext(), "구매옵션 선택");
        runScript("document.querySelector('#paymentCheck3').click(); " +
                "document.querySelector('#bankSelect').value = '020';" +
                "document.querySelector('#rdoCashReceipt3').click();" +
                "document.querySelector('#agreeCheck').click();" +
                "document.querySelector('[prop=btnPayment]').click();");
        stop();
    }
}
