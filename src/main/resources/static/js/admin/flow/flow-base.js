layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form;
    var table = layui.table;
    var laydate = layui.laydate;
    var upload = layui.upload;
    var element = layui.element;

    FlowBase.$ = $;

    /** 生成选择列属性的div **/
    var columnItem;
    var chooseColumnHtml = "";
    for(var index in FlowBase.column){
        var checked = "";
        columnItem = FlowBase.column[index];
        if($.inArray(columnItem.field, FlowBase.fieldList) > 0){
            checked = "checked";
        }
        chooseColumnHtml += '<input type="checkbox" value="' + columnItem.field + '" title="' + columnItem.title + '" ' + checked + '>';
    }
    $("#columnField").html(chooseColumnHtml);
    form.render();

    FlowBase.renderTable(table);
    form.on('checkbox', function(data){
        var isChecked = data.elem.checked;
        var fieldValue = data.elem.value;
        if(isChecked && $.inArray(fieldValue, FlowBase.fieldList) < 0){
            FlowBase.fieldList.push(fieldValue);
        }
        if(!isChecked && $.inArray(fieldValue, FlowBase.fieldList)){
            FlowBase.fieldList.splice($.inArray(fieldValue, FlowBase.fieldList), 1);
        }
        FlowBase.renderTable(table);
    });

    // 初始化上传组件
    upload.render({
        elem: '#importExcel',
        url: '/admin/flow/importExcel',
        accept: 'file',
        acceptMime: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel',
        exts: 'xls|xlsx',
        before: function () {
            var progressHtml = '<div id="upload-message" data-upload-status="no"></div>'
                + '<div class="layui-progress layui-progress-big" lay-filter="upload-progress">'
                + '<div class="layui-progress-bar layui-bg-green" lay-percent="0%"></div>'
                + '</div>';
            FlowBase.progressIndex = layer.open({
                title: ['上传文件', 'text-align: center; padding-left: 80px;'],
                content: progressHtml,
                btnAlign: 'c',
                area: '500px',
                closeBtn: 0,
                yes: function(index, layero){
                    var isUpload = $("#upload-message").attr("data-upload-status");
                    if(isUpload === "yes"){
                        layer.close(index);
                        table.reload('flow-table-reload',{
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
    /** 设置表头事件处理逻辑 **/
    var active = {
        setColumnField: function(){
            if($("#columnField").is(':visible')) {
                $("#columnField").hide();
            }else{
                $("#columnField").show();
            }
        },
        importExcel: function(){

        },
        exportExcel: function(){
            var columnFieldIds = "";
            for(var index = 0; index < FlowBase.fieldList.length; index ++ ){
                columnFieldIds += (FlowBase.fieldList[index] + ",")
            }
            $("#columnFieldIds").val(columnFieldIds);
            $("#flow-form").attr("action", "/admin/flow/exportExcel").submit();
        }
    };
    $('.table-toobar .layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
    /** 设置表头事件处理逻辑 **/


    //重置事件
    $(".reset-btn").on("click", function () {
        $("#chooseDate").val("");
    });
    //搜索表单提交
    form.on('submit(search-btn)', function(data){
        console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
        console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
        console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}

        FlowBase.queryTable(table, {
            chooseDate: data.field.chooseDate
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    //选择日期
    laydate.render({
        elem: '#chooseDate',
        range: true
    });

    exports('flow-base', {});
});

var FlowBase = {
    $: null,
    table: null,
    progressIndex: null,
    fieldList: [
        '',
        'id',
        'date',
        'merchantName',
        'platformName',
        'website',
        'source',

        'browseCount',
        'inflowCount',
        'userCount',
        'againClickCount',
        'againClickRate',
        'averageStayTime',
        'averageBrowsePageCount',

        'createTime',
        'updateTime'
    ],
    column: [
        {field: 'id', title: 'ID', width: 100, sort: false, align: 'center', fixed: true},
        {field: 'date', title: '日期', minWidth: 120, align: 'center'},
        {field: 'merchantName', title: '合作商户', minWidth: 120, align: 'center'},
        {field: 'platformName', title: '渠道归属', minWidth: 100, align: 'center'},
        {field: 'website', title: '网站来源', minWidth: 100, align: 'center'},
        {field: 'source', title: '来源类型', minWidth: 100, align: 'center'},

        {field: 'browseCount', title: '浏览量', minWidth: 120, align: 'center'},
        {field: 'inflowCount', title: '流入量', minWidth: 100, align: 'center'},
        {field: 'userCount', title: '用户数', minWidth: 100, align: 'center'},
        {field: 'againClickCount', title: '二跳量', minWidth: 150, align: 'center'},
        {field: 'againClickRate', title: '二跳率', minWidth: 120, align: 'center'},
        {field: 'averageStayTime', title: '平均停留时间', minWidth: 120, align: 'center'},
        {field: 'averageBrowsePageCount', title: '平均浏览页面', minWidth: 120, align: 'center'},

        {field: 'createTime', title: '创建时间', minWidth: 180, align: 'center'},
        {field: 'updateTime', title: '更新时间', minWidth: 180, align: 'center'}
    ],
    formColumn: function () {
        var columnList = [];
        for(var index = 0; index < FlowBase.column.length; index ++ ){
            var columnItem = FlowBase.column[index];
            if(FlowBase.$.inArray(columnItem.field, FlowBase.fieldList) > 0) {
                columnList.push(columnItem);
            }
        }
        console.log(columnList);
        return columnList;
    },
    queryTable: function (table, queryJson) {
        var columnList = FlowBase.formColumn();
        table.reload('flow-table-reload',{
            page: { curr: 1 },
            cols: [ columnList ],
            where: queryJson
        });
    },
    renderTable: function (table) {
        var columnList = [];
        for(var index = 0; index < FlowBase.column.length; index ++ ){
            var columnItem = FlowBase.column[index];
            if(FlowBase.$.inArray(columnItem.field, FlowBase.fieldList) > 0) {
                columnList.push(columnItem);
            }
        }
        console.log(columnList);
        if(FlowBase.table){
            FlowBase.table.reload({
                cols: [[{field: 'id', title: 'ID', width: 50, sort: false, align: 'center', fixed: true}]],
            });
            FlowBase.table.reload({
                cols: [ columnList ],
            });
            return;
        }
        FlowBase.table = table.render({
            id: 'flow-table-reload',
            elem: '#flow-table-grid',
            url: '/admin/flow/list',
            cols: [ columnList ],
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
    }
}