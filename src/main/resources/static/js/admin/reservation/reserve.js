layui.define(['form', 'table', 'laydate', 'jquery'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form;
    var table = layui.table;
    var laydate = layui.laydate;

    //重置事件
    $(".reset-btn").on("click", function () {
        $("#chooseDate").val("");
        $("#contactMobile").val("");
    });
    //搜索表单提交
    form.on('submit(search-btn)', function(data){
        console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
        console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
        console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
        table.reload('reserve-table-reload',{
            page: { pageNo: 1 },
            where: {
                chooseDate: data.field.chooseDate,
                contactMobile: data.field.contactMobile
            }});
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    //搜索表单提交
    form.on('submit(download-btn)', function(data){
        data.form.action = "/admin/reservation/download";
        data.form.submit();
    });
    //选择日期
    laydate.render({
        elem: '#chooseDate',
        range: true
    });
    //方法级渲染
    table.render({
        elem: '#reserve-table-grid',
        url: '/admin/reservation/list',
        cols: [
            [{checkbox: true, fixed: true},
                {field: 'id', title: 'ID', width: 80, sort: false, align: 'center', fixed: true},
                {field: 'contactName', title: '预约姓名', align: 'center'},
                {field: 'contactTime', title: '预约时间', align: 'center'},
                {field: 'contactMobile', title: '预约电话', align: 'center'},
                {field: 'sourceType', title: '预约入口', align: 'center', templet: function(value){
                    if(value.sourceType == 0){ return "其他产品" }
                    else if(value.sourceType == 1){ return "专题首页" }
                    else if(value.sourceType == 2){ return "常青树" }
                    else if(value.sourceType == 3){ return "华夏福" }
                    else if(value.sourceType == 4){ return "常青藤" }
                    else if(value.sourceType == 5){ return "福临门" }
                }},
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
        id: 'reserve-table-reload',
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


    exports('reserve', {});
});