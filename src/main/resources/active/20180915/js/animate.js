$(function () {
    function r() {
        $(".s1-04, .s1-05, .s1-06, .s1-07, .s1-08").removeClass("animated pulse cycle Rotation");
        setTimeout(function () {
            $(".s1-04").addClass("animated bounceOutUp")
        }, 100);
        setTimeout(function () {
            $(".s1-03").addClass("animated bounceOutDown")
        }, 100);
        setTimeout(function () {
            $(".s1-04").addClass("animated bounceOutUp")
        }, 200);
        setTimeout(function () {
            $(".s1-05").addClass("animated bounceOutUp")
        }, 300);
        setTimeout(function () {
            $(".s1-06").addClass("animated bounceOutUp")
        }, 400);
        setTimeout(function () {
                $(".s1-07").addClass("animated bounceOutUp")
            },
            500);
        setTimeout(function () {
            $(".s1-08").addClass("animated bounceOutUp")
        }, 600);
        setTimeout(function () {
            $(".s1-02").addClass("animated bounceOutLeft")
        }, 500);
        setTimeout(function () {
            $(".s1-09").addClass("animated bounceOutRight")
        }, 500)
    }

    function t() {
        $(".full-canvas, .step-2").fadeIn(300);
        $(".step-1").html("");
        setTimeout(function () {
            $(".step-2 img").eq(0).removeClass("hide fadeOut").addClass("animated bounceInUp")
        }, 100);
        setTimeout(function () {
                $(".step-2 img").eq(1).removeClass("hide fadeOut").addClass("animated bounceInRight")
            },
            300);
        setTimeout(function () {
            $(".step-2 img").eq(2).removeClass("hide fadeOut").addClass("animated bounceInRight")
        }, 400);
        setTimeout(function () {
            $(".step-2 img").eq(3).removeClass("hide fadeOut").addClass("animated bounceInDown")
        }, 500);
        setTimeout(function () {
            $(".step-2 img").eq(4).removeClass("hide fadeOut").addClass("animated bounceInRight")
        }, 600);
        setTimeout(function () {
            $(".step-2 img").eq(5).removeClass("hide fadeOut").addClass("animated bounceInLeft")
        }, 700);
        setTimeout(function () {
                $(".step-2 img").eq(6).removeClass("hide fadeOut").addClass("animated bounceInRight")
            },
            800);
        setTimeout(function () {
            $(".step-2 img").eq(7).removeClass("hide fadeOut").addClass("animated bounceInRight")
        }, 800);
        setTimeout(function () {
            $(".step-2 img").eq(8).removeClass("hide fadeOut").addClass("animated bounceInRight")
        }, 1200);
        setTimeout(function () {
            $(".step-2 img").eq(9).removeClass("hide fadeOut").addClass("animated bounceInRight")
        }, 1200);
        setTimeout(function () {
            e.show()
        }, 2E3)
    }

    function f() {
        for (var d = [0, 1, 2, 3, 4, 5, 6, 7], b = "bounceOutUp bounceOutUp bounceOutDown zoomOutDown bounceOutRight bounceOutLeft bounceOutUp bounceOutLeft".split(" "),
                 e = "bounceInUp bounceInUp bounceInDown zoomInDown bounceInRight bounceInLeft bounceInUp bounceInLeft".split(" "), a = 0; 8 > a; a++) $(".step-2 img").eq(d[a]).addClass(b[a]), $(".step-2 img").eq(d[a]).removeClass("bounceInLeft bounceInRight bounceInUp bounceInDown"), $(".step-2 img").eq(d[a]).addClass(e[a]);
        setTimeout(function () {
            for (var a = 0; 8 > a; a++) $(".step-2 img").eq(d[a]).removeClass(b[a]), $(".step-2 img").eq(d[a]).addClass(e[a])
        }, 500);
        setTimeout(function () {
                for (var a = 0; 8 > a; a++) $(".step-2 img").eq(d[a]).removeClass(e[a])
            },
            1E3);
        setTimeout(function () {
            $(".step-2 img").eq(3).addClass("pulse cycleslow")
        }, 1500);
        $(".chat").fadeOut(500);
        $(".pagenumber").html(c - 1 + "/8");
        10 < c && $(".pagenumber").hide();
        setTimeout(function () {
            11 > c && $(".pagenumber").show()
        }, 2E3)
    }

    function y() {
        setTimeout(function () {
            $(".step-2 img").eq(8).removeClass("hide").addClass("animated bounceOutRight")
        }, 100);
        setTimeout(function () {
            $(".step-2 img").eq(9).removeClass("hide").addClass("animated bounceOutLeft")
        }, 100)
    }

    function p(d, c) {
        function u() {
            v(b.data[a].type,
                b.data[a].bearing);
            var d = ".st-" + b.data[a].type + "-" + b.data[a].bearing,
                l = ".st-" + b.data[a].type + "-" + b.data[a].bearing + "-t",
                k = ".st-" + b.data[a].type + "-" + b.data[a].bearing + "-n", f = b.data[a].speed;
            $(l).html("");
            $(d).show();
            $(l).css("font-size", b.data[a].fontsize);
            $(l).css("line-height", b.data[a].lineheight);
            $(l).css("margin", b.data[a].margin);
            $(l).css("text-align", b.data[a].textalign);
            $(l).html(b.data[a].text).show();
            $(k).show();
            45 != a && 25 != a || $(".s2-06").addClass("animated tada cycleslowfast");
            "l" == b.data[a].bearing &&
            26 != a ? $(".step-2 img").eq(5).addClass("animated tada cycleslowfast") : $(".step-2 img").eq(4).addClass("animated tada cycleslowfast");
            setTimeout(function () {
                $(".s2-07, .s2-06").removeClass("tada cycleslowfast")
            }, f);
            console.log(a + "__" + f);
            a == z ? clearInterval(g) : a == c ? (clearInterval(g), a++) : 14 == a && 0 == m ? (clearInterval(g), setTimeout(function () {
                0 == m && (m = 1, clearInterval(g), $(".s2-07, .s2-06").removeClass("tada cycleslowfast"), $(".st-dh-l-t, .st-dh-l-n, .st-dh-l, .st-nx-r, .st-nx-r-t").hide(), $(".st-dh-r-t, .st-dh-r-n, .st-dh-r, .st-nx-l, .st-nx-r-l").hide(),
                    $(".misicmask").hide(), $(".tip-toend").show(), n.hide(), e.hide(), h = 1)
            }, f)) : (clearInterval(g), g = setInterval(u, f), a++)
        }

        var a = d;
        g = setInterval(u, 1E3)
    }

    function v(d, b) {
        "dh" == d && ("l" == b ? ($(".st-dh-r-t, .st-dh-r").hide(), $(".st-nx-r, .st-nx-r-t").hide(), $(".st-dh-r-n").hide()) : ($(".st-dh-l-t, .st-dh-l").hide(), $(".st-nx-l, .st-nx-l-t").hide(), $(".st-dh-l-n").hide()));
        "nx" == d && ("l" == b ? $(".st-nx-r, .st-nx-r-t").hide() : $(".st-nx-l, .st-nx-l-t").hide())
    }

    var w, q = $(".s1-03"), n = $(".btn-down"), e = $(".btn-up"), c = 1,
        m = 0, g = 0, h = 0;
    $(".step-2, .tip-toend, .tip-appointment, .tip-share, .btn-down, .btn-up, .full-canvas").hide();
    (new Image).src = "i/musicstop.png";
    var b = function () {
        var b;
        $.ajax({
            type: "get", url: "js/data.json", dataType: "json", async: !1, success: function (d) {
                b = d
            }
        });
        return b
    }(), z = function (b) {
        var d = 0, c;
        for (c in b) d++;
        return d
    }(b.data), x = navigator.userAgent, audio = document.getElementById("audio");
    document.addEventListener("WeixinJSBridgeReady", function () {
        0 < x.indexOf("iPhone") ? audio.play() : 0 < x.indexOf("Android") && (audio.play(),
        0 === audio.currentTime && audio.play())
    });
    audio.play && $(".music").addClass("animated Rotation cycle");
    timer1 = setInterval(function () {
        q.html("\u5851\u6599\u59d0\u59b9\u89c1\u9762\uff0c\u683c\u5916\u7cbe\u5f69\uff01\u597d\u620f\uff0c\u6b63\u8981\u5f00\u573a".substring(0, w));
        w++;
        if ("\u5851\u6599\u59d0\u59b9\u89c1\u9762\uff0c\u683c\u5916\u7cbe\u5f69\uff01\u597d\u620f\uff0c\u6b63\u8981\u5f00\u573a" == q.html()) {
            clearInterval(timer1);
            for (var b = 0; 5 >= b; b++) (function (b) {
                setTimeout(function () {
                    q.html("\u5851\u6599\u59d0\u59b9\u89c1\u9762\uff0c\u683c\u5916\u7cbe\u5f69\uff01\u597d\u620f\uff0c\u6b63\u8981\u5f00\u573a" +
                        ".....".substring(0, b))
                }, 600 * b)
            })(b);
            timer2 = setInterval(function () {
                "\u5851\u6599\u59d0\u59b9\u89c1\u9762\uff0c\u683c\u5916\u7cbe\u5f69\uff01\u597d\u620f\uff0c\u6b63\u8981\u5f00\u573a....." == $(".s1-03").html() && (clearInterval(timer2), r(), t(), setTimeout(function () {
                    e.show();
                    c = 2
                }, 3E3))
            }, 200)
        }
    }, 200);
    $("body").on("touchstart", function (b) {
        startX = b.originalEvent.changedTouches[0].pageX
    });
    $("body").on("touchend", function (d) {
        moveEndX = d.originalEvent.changedTouches[0].pageX;
        X = moveEndX - startX;
        0 < X && 0 == h && (5 ==
        c && (m = 1), $(".s2-07, .s2-06").removeClass("tada cycleslowfast"), $(".pagenumber").hide(), setTimeout(function () {
            clearInterval(g)
        }, 100), 10 == c && (f(), n.hide(), e.hide(), h = 1, setTimeout(function () {
            $(".st-dh-l-t, .st-dh-l-n, .st-dh-l, .st-nx-r, .st-nx-r-t").hide();
            $(".st-dh-r-t, .st-dh-r-n, .st-dh-r, .st-nx-l, .st-nx-r-l").hide();
            $(".tip-appointment").show();
            $(".pagenumber").hide();
            c++;
            return !1
        }, 1500)), 10 > c && 2 <= c && 0 == h ? (3 <= c ? (n.hide(), $(".tip-toend").hide()) : 2 == c && (n.hide(), y()), h = 0, $(".tip-toend").hide(),
            v(b.data[c].type, b.data[c].bearing), setTimeout(function () {
            p(b.scene[c].start, b.scene[c].end);
            console.log("\u4e0a\u6ed1_" + c);
            c++;
            e.show()
        }, 500), f()) : e.hide())
    });
    $(".btn-reload").click(function () {
        $(".step-2 img").addClass("fadeOut");
        h = 0;
        $(".step-2 img").eq(3).removeClass("pulse cycleslow");
        $(".step-2 img").eq(8).removeClass("bounceOutRight");
        $(".step-2 img").eq(9).removeClass("bounceOutLeft");
        $(".tip-share").hide();
        r();
        t();
        setTimeout(function () {
                e.show();
                c = 2;
                $(".step-2 img").removeClass("fadeOut")
            },
            2E3)
    });
    $(".btn-continue").click(function () {
        $(".pagenumber").hide();
        e.show();
        f();
        $(".pagenumber").html("3/8");
        $(".tip-toend").hide();
        setTimeout(function () {
            p(15, 17)
        }, 500);
        h = 0
    });
    $(".btn-end").click(function () {
        $(".pagenumber").hide();
        e.show();
        f();
        $(".tip-toend").fadeOut(1500);
        p(25, 32);
        c = 7;
        h = 0;
        setTimeout(function () {
            $(".pagenumber").html("5/8")
        }, 500)
    });
    $(".misicmask").click(function () {
        document.getElementById("audio").paused && ($("#audio").trigger("play"), $(".music").addClass("animated Rotation cycle"), $(".music").attr("src",
            "i/music.png"));
        $(".misicmask").hide()
    });
    $(".music").click(function () {
        document.getElementById("audio").paused ? ($("#audio").trigger("play"), $(".music").addClass("animated Rotation cycle"), $(".music").attr("src", "i/music.png")) : ($("#audio").trigger("pause"), $(".music").removeClass("animated Rotation cycle"), $(".music").attr("src", "i/musicstop.png"))
    })
});