var open = window.open('https://smartstore.naver.com/kimmaster/products/4983495987');
var checkBuyBtn = function () {
    console.log('checkBuyBtn');
    var btn = open.document.querySelector('._buy_button');
    if( open.document.querySelector('._buy_button')) {
        btn.click();
        setTimeout(waitBuy, 500);
    } else {
        open.location.reload();
        setTimeout(checkBuyBtn, 500);
    }
}
var waitBuy = function() {
    if(open.document.querySelector("[for=generalPayments]")) {
        open.document.querySelector("[for=generalPayments]").click();
        open.document.querySelector("[for=pay19]").click();
        open.document.querySelector("#allAgree").click();
        open.document.querySelector(".btn_payment").click();
    } else {
        waitBuy(checkBuyBtn, 500);
    }
}
checkBuyBtn();
