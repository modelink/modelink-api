layui.define(['form', 'table', 'laydate', 'jquery'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form;

    form.on('submit(reset-password-btn)', function(data){
        $.ajax({
            url: "/admin/doResetPassword",
            type: "post",
            data: {
                oldPassword: data.field.oldPassword,
                onePassword: data.field.onePassword,
                twoPassword: data.field.twoPassword
            },
            success: function (data) {
                if(data.rtnCode === 200){
                    parent.layer.closeAll();
                    parent.window.location.reload();
                }else{
                    layer.msg(data.rtnMsg, {offset: '50px'});
                }
            }
        })
        return false;
    });


    exports('resetPassword', {});
});