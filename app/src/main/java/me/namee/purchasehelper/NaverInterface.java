package me.namee.purchasehelper;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class NaverInterface extends ScriptInterface {
    public NaverInterface(WebView view, PurchaseConfig config) {
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
        return html.contains("<a href=\"#\" class=\"mf_info_link MM_LOGINOUT\" data-fclk=\"fot.login\">로그인</a>");
    }

    public boolean isBuyPage() {
        return url.contains("m.pay.naver.com/o/orderSheet");
    }

    public boolean isCart() {
        return !isBuyPage() && url.contains("/products/");
    }

    public boolean isMy() {
        return !isCart() && html.contains("<a href=\"/na/\" class=\"shm_na_link\" data-clk=\"profileclick\">")
                && html.contains("<a href=\"#\" class=\"mf_info_link MM_LOGINOUT\" data-fclk=\"fot.logout\">로그아웃</a>");
    }

    public boolean isLogin() {
        return html.contains("<input type=\"text\" id=\"id\" name=\"id\"");
    }

    public boolean isClosed() {
        return !html.contains(">구매하기</button>");
    }

    public void clickMy() {
        Util.toast(view.getContext(), "로그인 클릭");
        runScript("document.querySelector('.mf_info_link.MM_LOGINOUT').click()");
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
        runScript("window.btnClicked = false;" +
                "var btns = [];" +
                "" +
                "document.querySelectorAll('button').forEach(" +
                "   function(btn) {" +
                "       if(btn.innerHTML.indexOf('구매하기') > -1) " +
                "           btns.push(btn)" +
                "   }" +
                ");" +
                "if (btns[0].clientHeight > 0 ) {" +
                "   btns[0].click();" +
                "} " +
                "if (btns[1].clientHeight > 0) {" +
                "   var opt = findByInnerHTML('div', '옵션을 먼저 선택해주세요.');" +
                "   if (opt && opt.clientHeight > 0 && document.querySelectorAll('a[role=option]')[0]) {" +
                "       document.querySelectorAll('a[role=option]').forEach((node) => {" +
                "           if(node.innerHTML.indexOf('품절') == -1) {" +
                "               node.click();" +
                "               return false;" +
                "           }" +
                "       })" +
                "   }" +
                "   btns[1].click();" +
                "   window.btnClicked = true;" +
                "}" +
                "if (!window.btnClicked) throw new Error('btn not clicked');");
        //checkBuyPage(url);
    }

    public void buy() {
        Util.toast(view.getContext(), "구매옵션 선택");
        runScript("document.querySelector('.paymethod > #basic').parentElement.children[1].click(); " +
                "document.querySelectorAll('strong').forEach((item)=>{if(item.innerHTML == '나중에 결제') item.parentElement.click();});" +
                "document.querySelector('#all_agree_btn').focus();" +
                "if (document.querySelector('#all_agree_btn') != 'on') {" +
                "   document.querySelector('#all_agree_btn').click(); " +
                "}" +
                "document.querySelector('._doPayText').parentElement.click();");
    }
}
