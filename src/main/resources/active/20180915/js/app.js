var _hmt = _hmt || [];

$(function () {
	var hm = document.createElement("script");
	hm.src = "https://hm.baidu.com/hm.js?c9db76c5f4bbf98a2f7e54b7a207aa42";
	var s = document.getElementsByTagName("script")[0];
	s.parentNode.insertBefore(hm, s);
    
	customAlertDialog("", 1);	
    var url = window.location.href;
    //获取签名
    $.ajax({
        type: "GET",
        url: "/weixin/getSignature",
        data: {url: url},
        success: function (data) {
            setShareInfo({
                title: '塑料姐妹不可怕，谁穷谁尴尬', // 分享标题
                summary: '比包包，比老公，比孩子，现实比电视剧可精彩多了', // 分享内容
                pic: 'http://image.modelink.cn/common/poor.png', // 分享图片
                url: url, // 分享链接
                // 微信权限验证配置信息，若不在微信传播，可忽略
                WXconfig: {
                    swapTitleInWX: true, // 是否标题内容互换（仅朋友圈，因朋友圈内只显示标题）
                    appId: data.appid, // 公众号的唯一标识
                    timestamp: data.timestamp, // 生成签名的时间戳
                    nonceStr: data.nonceStr, // 生成签名的随机串
                    signature: data.signature // 签名
                }
            });
        }
    });

    //获取验证码
    $(".btn-getverifycode").click(function(){
        if($(this).hasClass("active")){
            return;
        }
        var name = $(".name").val()//姓名
        var phone = $(".phone").val();//手机号
        var verifycode =$(".verifycode").val();//验证码
        //验证手机号码
        if(!formatPhone(phone)){
            return;
        }
        //其他操作
        var curCount = 60;
        var inverval;

        $(".btn-getverifycode").addClass("active");
        $(".btn-getverifycode").html("正在获取中");
		_hmt.push(['_trackEvent', 'H5活动', 'click', '获取验证码', 1]);
        $.ajax({
            url: "/sms/sendCaptcha",
            data: {
                mobile: phone
            },
            success: function (response) {
                if(!response || response.rtnCode != 200){
                    var resultJson = JSON.parse(response.rtnMsg);
                    if(resultJson.code == "isv.BUSINESS_LIMIT_CONTROL"){
                        alert("短信发送太频繁");
                    }else{
                        alert(resultJson.message);
                    }
                    $(".btn-getverifycode").removeClass("active"); //启用按钮
                    $(".btn-getverifycode").html("获取验证码");
                    return;
                }else{
                    inverval = window.setInterval(function (args) {
                        if(curCount <= 1) {
                            curCount = 60;
                            window.clearInterval(inverval); //停止计时器
                            $(".btn-getverifycode").removeClass("active"); //启用按钮
                            $(".btn-getverifycode").html("获取验证码");
                            code = ""; //清除验证码。如果不清除，过时间后，输入收到的验证码依然有效
                        } else {
                            curCount --;
                            $(".btn-getverifycode").addClass("active");
                            $(".btn-getverifycode").html(curCount + "s后重试");
                        }
                    }, 1000);
                }
            },
            error: function () {
                alert("网络连接失败");
                $(".btn-getverifycode").removeClass("active"); //启用按钮
                $(".btn-getverifycode").html("获取验证码");
            }
        });
    });

    //提交预约信息
    $(".btn-appointment").click(function(){
        var name = $(".name").val()//姓名
        var phone = $(".phone").val();//手机号
        var verifycode =$(".verifycode").val();//验证码
        if (!name) {
            alert("请输入您的姓名（不超过10个字符）");
            return;
        }
        if (!phone) {
            alert("请输入您的手机号");
            return;
        }
        if (!verifycode) {
            alert("请输入验证码");
            return;
        }
		_hmt.push(['_trackEvent', 'H5活动', 'click', '点击预约', 1]);
        $.ajax({
            url: "/active/20180915/doReserve",
            data: {
                name: name,
                mobile: phone,
                captcha: verifycode
            },
            success: function (response) {
                if(!response || response.rtnCode != 200){
                    alert(response.rtnMsg);
                }else{
                    $(".tip-appointment").fadeOut(500);
                    $(".tip-share").fadeIn(500);

                    $(".name").val("")//姓名
                    $(".phone").val("");//手机号
                    $(".verifycode").val("");//验证码
					_hmt.push(['_trackEvent', 'H5活动', 'click', '预约成功', 1]);
                }
            }
        })
    });

    //分享
    $(".btn-share").click(function(){
        alert('请点击右上角分享给朋友');
    });

});

function formatPhone(phone){
    if(phone == ""){
        alert("请输入手机号码！");
        return false;
    }
    if(!/^1[34578]\d{9}$/.test(phone)){
        alert("请输入正确的手机号码！");
        return false;
    }
    return true;
}

//参数：custom--原url代替字符
//参数：linenum--换行数
function customAlertDialog(custom, lineNum) {
    window.alert = function(name) {
        var hh = '\r\n';
        var rowNum = hh.repeat(lineNum);
        iframe = document.createElement("IFRAME");
        iframe.style.display = "none";
        iframe.setAttribute("src", 'data:text/plain,');
        document.documentElement.appendChild(iframe);
        window.frames[0].window.alert(custom + rowNum + name);
        iframe.parentNode.removeChild(iframe);
    }
}
