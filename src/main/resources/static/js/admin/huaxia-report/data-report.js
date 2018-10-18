layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form;
    var table = layui.table;
    var upload = layui.upload;
    var laydate = layui.laydate;
    var element = layui.element;

    //重置事件
    $(".reset-btn").on("click", function () {
        $("#chooseDate").val("");
    });
    //搜索表单提交
    form.on('submit(search-btn)', function(data){
        table.reload('data-report-table-reload',{
            page: { curr: 1 },
            where: {
                chooseDate: data.field.chooseDate,
                platformName: data.field.platformName,
                advertiseActive: data.field.advertiseActive
            }});
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    //选择日期
    laydate.render({
        elem: '#chooseDate',
        range: true
    });

    //方法级渲染
    table.render({
        elem: '#data-report-table-grid',
        url: '/admin/huaxiaDataReport/list',
        cols: [
            [
                {checkbox: true, fixed: true},
                {field: 'id', title: 'ID', minWidth: 80, align: 'center', fixed: true},
                {field: 'date', title: '日期', minWidth: 120, align: 'center', fixed: true},
                {field: 'dataSource', title: '数据来源', minWidth: 120, align: 'center'},
                {field: 'pcCount', title: '总转化-PC（去重后）', minWidth: 160, align: 'center'},
                {field: 'wapCount', title: '总转化-移动（去重后）', minWidth: 160, align: 'center'},
                {field: 'weixinCount', title: '微信端（去重后）', minWidth: 120, align: 'center'},
                {field: 'xiaomiCount', title: '小米', minWidth: 100, align: 'center'},
                {field: 'validCount', title: '有效', minWidth: 100, align: 'center'},
                {field: 'flagCount', title: '营销标记电话', minWidth: 120, align: 'center'},
                {field: 'createTime', title: '创建时间', minWidth: 120, align: 'center'},
                {field: 'updateTime', title: '更新时间', minWidth: 120, align: 'center'}
            ]
        ],
        id: 'data-report-table-reload',
        defaultToolbar: ['filter'],
        toolbar: '#toolbar',
        page: true,
        request: {
            pageName: 'pageNo', //页码的参数名称，默认：page
            limitName: 'pageSize' //每页数据量的参数名，默认：limit
        },
        response: {
            statusName: 'rtnCode', //数据状态的字段名称，默认：code
            statusCode: 200, //成功的状态码，默认：0
            msgName: 'rtnMsg', //状态信息的字段名称，默认：msg
            countName: 'totalCount', //数据总数的字段名称，默认：count
            dataName: 'rtnList' //数据列表的字段名称，默认：data
        },
        done: function (response, curr, count) {
            // 初始化上传组件
            upload.render({
                elem: '#importExcel',
                url: '/admin/huaxiaDataReport/importExcel',
                accept: 'file',
                acceptMime: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel',
                exts: 'xls|xlsx',
                before: function () {
                    var progressHtml = '<div id="upload-message" data-upload-status="no"></div>'
                        + '<div class="layui-progress layui-progress-big" lay-filter="upload-progress">'
                        + '<div class="layui-progress-bar layui-bg-green" lay-percent="0%"></div>'
                        + '</div>';
                    layer.open({
                        title: ['上传文件', 'text-align: center; padding-left: 80px;'],
                        content: progressHtml,
                        btnAlign: 'c',
                        area: '500px',
                        closeBtn: 0,
                        yes: function(index, layero){
                            var isUpload = $("#upload-message").attr("data-upload-status");
                            if(isUpload === "yes"){
                                layer.close(index);
                                table.reload('data-report-table-reload',{
                                    page: { curr: 1 },
                                    where: {
                                        chooseDate: $("#chooseDate").val()
                                    }
                                });
                            }else{
                                $("#upload-message").html("<p color='red'>正在上传文件……</p>");
                            }
                        }
                    });
                    $("#upload-message").html("正在上传文件……");
                    element.progress("upload-progress", 0 + "%");
                },
                done: function(response){
                    if(response.rtnCode == 200){
                        $("#upload-message").html("上传完成，共导入数据：" + response.rtnData);
                        $("#upload-message").attr("data-upload-status", "yes");
                        element.progress("upload-progress", 100 + "%");
                    }else{
                        $("#upload-message").html(response.rtnMsg);
                        $("#upload-message").attr("data-upload-status", "yes");
                        element.progress("upload-progress", 100 + "%");
                    }
                }
            });

        }
    });

    exports('data-report', {});
});