layui.define(['form', 'table', 'laydate', 'jquery'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form;
    var table = layui.table;
    var laydate = layui.laydate;

    //重置事件
    $(".reset-btn").on("click", function () {
        $("#chooseDate").val("");
        $("#loggerType").val("");
    });
    //搜索表单提交
    form.on('submit(search-btn)', function(data){
        table.reload('exceptionLogger-table-reload',{
            page: { curr: 1 },
            where: {
                chooseDate: data.field.chooseDate,
                loggerType: data.field.loggerType
            }});
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    form.on('submit(delete-btn)', function(data){
        $.ajax({
            url: "/admin/exceptionLogger/delete",
            success: function () {
                table.reload('exceptionLogger-table-reload',{
                    page: { curr: 1 },
                    where: {
                        chooseDate: data.field.chooseDate,
                        loggerType: data.field.loggerType
                    }});
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //选择日期
    laydate.render({
        elem: '#chooseDate',
        range: true
    });
    //方法级渲染
    table.render({
        elem: '#exceptionLogger-table-grid',
        url: '/admin/exceptionLogger/list',
        cols: [
            [
                {checkbox: true, fixed: true},
                {field: 'id', title: 'ID', width: 80, sort: false, align: 'center', fixed: true},
                {field: 'loggerKey', title: '错误内容', align: 'center'},
                {field: 'loggerDate', title: '日期', align: 'center'},
                {field: 'loggerType', title: '业务类型', align: 'center'},
                {field: 'loggerDesc', title: '错误详情', align: 'center'}
            ]
        ],
        id: 'exceptionLogger-table-reload',
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


    exports('exceptionLogger', {});
});