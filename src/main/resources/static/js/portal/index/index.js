$(function () {

    $('.bannerWrap').unslider({
        speed: 400,
        delay: 2800,
        fluid: true,
        dots: true
    });

    $("#introsider li").each(function (index, selected) {
        console.log("li --> " + selected);
        $(selected).on("click", function () {
            $(selected).addClass("on");
            $(selected).siblings().removeClass("on");

            var offset = $(".intromain div[data-index=" + index + "]").attr("data-index", index).offset();
            $("html,body").animate({scrollTop: offset.top - 80}, 500);
        });
    });

    var index = BASE.getQueryString("index");
    if (index) {
        $("#introsider li[data-index=" + index + "]").addClass("on");
        $("#introsider li[data-index=" + index + "]").siblings().removeClass("on");

        var offset = $(".intromain div[data-index=" + index + "]").attr("data-index", index).offset();
        $("html,body").animate({scrollTop: offset.top - 80}, 500);
    }
    
});

var BASE = {
    getQueryString: function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }
}