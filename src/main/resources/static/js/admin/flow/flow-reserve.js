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
        url: '/admin/flowReserve/importExcel',
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
                        table.reload('flowReserve-table-reload',{
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
                $("#upload-message").html("上传完成");
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
            $("#flowReserve-form").attr("action", "/admin/flowReserve/exportExcel").submit();
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

    exports('flow-reserve', {});
});

var FlowBase = {
    $: null,
    table: null,
    progressIndex: null,
    fieldList: [
        '',
        'id',
        'date',
        'time',
        'merchantName',
        'platformName',

        'reserveNo',
        'reserveMobile',
        'provinceName',
        'cityName',

        'createTime',
        'updateTime'
    ],
    column: [
        {field: 'id', title: 'ID', width: 100, sort: false, align: 'center', fixed: true},
        {field: 'date', title: '日期', minWidth: 120, align: 'center'},
        {field: 'time', title: '时间', minWidth: 100, align: 'center'},
        {field: 'merchantName', title: '合作商户', minWidth: 120, align: 'center'},
        {field: 'platformName', title: '渠道归属', minWidth: 120, align: 'center'},
        {field: 'reserveNo', title: '预约编号', minWidth: 120, align: 'center'},
        {field: 'reserveMobile', title: '联系电话', minWidth: 120, align: 'center'},

        {field: 'advertiseActive', title: '广告活动', minWidth: 120, align: 'center'},
        {field: 'advertiseMedia', title: '广告媒体', minWidth: 120, align: 'center'},
        {field: 'advertiseSeries', title: '广告系列', minWidth: 120, align: 'center'},
        {field: 'keyWordGroup', title: '关键词组', minWidth: 120, align: 'center'},
        {field: 'advertiseDesc', title: '广告描述', minWidth: 120, align: 'center'},
        {field: 'advertiseTime', title: '广告点击时间', minWidth: 120, align: 'center'},
        {field: 'advertiseTransformTime', title: '广告转化时间', minWidth: 120, align: 'center'},

        {field: 'firstAdvertiseActive', title: '首次点击广告活动', minWidth: 150, align: 'center'},
        {field: 'firstAdvertiseMedia', title: '首次点击广告媒体', minWidth: 150, align: 'center'},
        {field: 'firstAdvertiseDesc', title: '首次点击广告描述', minWidth: 150, align: 'center'},
        {field: 'firstAdvertiseTime', title: '首次广告点击时间', minWidth: 150, align: 'center'},

        {field: 'stationAdvertise', title: '站内广告', minWidth: 150, align: 'center'},
        {field: 'stationAdvertiseTime', title: '站内广告点击时间', minWidth: 150, align: 'center'},
        {field: 'stationAdvertiseTransformTime', title: '站内广告转化时间', minWidth: 150, align: 'center'},
        {field: 'transformType', title: '转化类型', minWidth: 150, align: 'center'},

        {field: 'ip', title: 'IP', minWidth: 120, align: 'center'},
        {field: 'provinceName', title: '地区名称', minWidth: 120, align: 'center'},
        {field: 'cityName', title: '城市名称', minWidth: 120, align: 'center'},
        {field: 'source', title: '访问来源', minWidth: 120, align: 'center'},
        {field: 'isAdvertise', title: '是否广告', minWidth: 120, align: 'center'},
        {field: 'website', title: '网站来源', minWidth: 120, align: 'center'},
        {field: 'searchWord', title: '搜索词', minWidth: 120, align: 'center'},
        {field: 'isNewVisitor', title: '是否为新访客', minWidth: 120, align: 'center'},

        {field: 'last2AdvertiseActive', title: '最后2次点击广告活动', minWidth: 150, align: 'center'},
        {field: 'last2AdvertiseMedia', title: '最后2次点击广告媒体', minWidth: 150, align: 'center'},
        {field: 'last2AdvertiseDesc', title: '最后2次点击广告描述', minWidth: 150, align: 'center'},
        {field: 'last2AdvertiseTime', title: '最后2次广告点击时间', minWidth: 150, align: 'center'},

        {field: 'last3AdvertiseActive', title: '最后3次点击广告活动', minWidth: 150, align: 'center'},
        {field: 'last3AdvertiseMedia', title: '最后3次点击广告媒体', minWidth: 150, align: 'center'},
        {field: 'last3AdvertiseDesc', title: '最后3次点击广告描述', minWidth: 150, align: 'center'},
        {field: 'last3AdvertiseTime', title: '最后3次广告点击时间', minWidth: 150, align: 'center'},

        {field: 'os', title: '操作系统', minWidth: 120, align: 'center'},
        {field: 'browser', title: '浏览器', minWidth: 120, align: 'center'},
        {field: 'resolutionRatio', title: '分辨率', minWidth: 120, align: 'center'},
        {field: 'deviceType', title: '设备类型', minWidth: 120, align: 'center'},

        {field: 'themePage', title: '专题页面', minWidth: 120, align: 'center'},
        {field: 'last2ThemePage', title: '最后2次专题页面', minWidth: 150, align: 'center'},
        {field: 'last2ThemePageNo', title: '最后2次专题页面明细ID', minWidth: 150, align: 'center'},
        {field: 'last2ThemeClickTime', title: '最后2次专题页面点击时间', minWidth: 150, align: 'center'},
        {field: 'last2ThemeTransformTime', title: '最后2次专题页面转化时间', minWidth: 150, align: 'center'},
        {field: 'isMakeUp', title: '是否充量', minWidth: 100, align: 'center'},

        {field: 'createTime', title: '创建时间', minWidth: 150, align: 'center'},
        {field: 'updateTime', title: '更新时间', minWidth: 150, align: 'center'}
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
        table.reload('flowReserve-table-reload',{
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
            id: 'flowReserve-table-reload',
            elem: '#flowReserve-table-grid',
            url: '/admin/flowReserve/list',
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