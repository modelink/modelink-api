layui.define(['form', 'table', 'laydate', 'layer', 'jquery'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form;
    var table = layui.table;
    var layer = layui.layer;
    var laydate = layui.laydate;

    //重置事件
    $(".reset-btn").on("click", function () {
        $("#chooseDate").val("");
        $("#contactMobile").val("");
    });
    //搜索表单提交
    form.on('submit(search-btn)', function(data){
        table.reload('estimate-table-reload',{
            page: { curr: 1 },
            where: {
                chooseDate: data.field.chooseDate,
                contactMobile: data.field.contactMobile
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
        elem: '#estimate-table-grid',
        url: '/admin/estimation/list',
        cols: [
            [
                {checkbox: true, fixed: true},
                {field: 'id', title: 'ID', width: 80, align: 'center', fixed: true},
                {field: 'merchantName', title: '合作商户', width: 120, align: 'center', fixed: true},
                {field: 'name', title: '预约姓名', align: 'center'},
                {field: 'gender', title: '预约性别', align: 'center'},
                {field: 'mobile', title: '预约电话', align: 'center'},
                {field: 'birthday', title: '预约生日', align: 'center'},
                {field: 'sourceType', title: '预约产品', align: 'center'},
                {field: 'status', title: '数据状态', align: 'center', templet: function(value){
                    if(value.status == 0){ return "已预约" }
                    else if(value.status == 1){ return "沟通中" }
                    else if(value.status == 2){ return "沟通成功" }
                    else if(value.status == 3){ return "沟通失败" }
                }},
                {field: 'createTime', title: '创建时间', sort: true, align: 'center'},
                {field: 'updateTime', title: '更新时间', sort: true, align: 'center'}
            ]
        ],
        id: 'estimate-table-reload',
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
        }
    });
    //头工具栏事件
    table.on('toolbar(estimate-table-grid)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id);
        switch(obj.event){
            case 'download':
                var mobile = $("#mobile").val() ? $("#mobile").val() : "";
                var chooseDate = $("chooseDate").val() ? $("#mobile").val() : "";
                window.location.href = ("/admin/estimation/download?chooseDate=" + chooseDate + "&mobile=" + mobile);
                break;
        };
    });

    exports('estimate', {});
});