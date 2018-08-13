layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form;
    var table = layui.table;
    var laydate = layui.laydate;
    var upload = layui.upload;
    var element = layui.element;

    Repellent.$ = $;

    /** 生成选择列属性的div **/
    var columnItem;
    var chooseColumnHtml = "";
    for(var index in Repellent.column){
        var checked = "";
        columnItem = Repellent.column[index];
        if($.inArray(columnItem.field, Repellent.fieldList) > 0){
            checked = "checked";
        }
        chooseColumnHtml += '<input type="checkbox" value="' + columnItem.field + '" title="' + columnItem.title + '" ' + checked + '>';
    }
    $("#columnField").html(chooseColumnHtml);
    form.render();

    Repellent.renderTable(table);
    form.on('checkbox', function(data){
        var isChecked = data.elem.checked;
        var fieldValue = data.elem.value;
        if(isChecked && $.inArray(fieldValue, Repellent.fieldList) < 0){
            Repellent.fieldList.push(fieldValue);
        }
        if(!isChecked && $.inArray(fieldValue, Repellent.fieldList)){
            Repellent.fieldList.splice($.inArray(fieldValue, Repellent.fieldList), 1);
        }
        Repellent.renderTable(table);
    });

    // 初始化上传组件
    upload.render({
        elem: '#importExcel',
        url: '/admin/repellent/importExcel',
        accept: 'file',
        acceptMime: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel',
        exts: 'xls|xlsx',
        before: function () {
            var progressHtml = '<div id="upload-message" data-upload-status="no"></div>'
                + '<div class="layui-progress layui-progress-big" lay-filter="upload-progress">'
                + '<div class="layui-progress-bar layui-bg-green" lay-percent="0%"></div>'
                + '</div>';
            Repellent.progressIndex = layer.open({
                title: ['上传文件', 'text-align: center; padding-left: 80px;'],
                content: progressHtml,
                btnAlign: 'c',
                area: '500px',
                closeBtn: 0,
                yes: function(index, layero){
                    var isUpload = $("#upload-message").attr("data-upload-status");
                    if(isUpload === "yes"){
                        layer.close(index);
                        table.reload('repellent-table-reload',{
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
            for(var index = 0; index < Repellent.fieldList.length; index ++ ){
                columnFieldIds += (Repellent.fieldList[index] + ",")
            }
            $("#columnFieldIds").val(columnFieldIds);
            $("#repellent-form").attr("action", "/admin/repellent/exportExcel").submit();
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

        Repellent.queryTable(table, {
            chooseDate: data.field.chooseDate
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    //选择日期
    laydate.render({
        elem: '#chooseDate',
        range: true
    });

    exports('repellent', {});
});

var Repellent = {
    $: null,
    table: null,
    progressIndex: null,
    fieldList: [
        '',
        'id',
        'merchantName',
        'repellentNo',
        'insuranceNo',

        'insuranceName',
        'productName',
        'extraInsurance',
        'insuranceAmount',
        'yearInsuranceFee',
        'insuranceFee',

        'tsrNumber',
        'tsrName',

        'createTime',
        'updateTime'
    ],
    column: [
        {field: 'id', title: 'ID', width: 100, sort: false, align: 'center', fixed: true},
        {field: 'merchantName', title: '合作商户', minWidth: 120, align: 'center'},
        {field: 'exportOrgName', title: '数据导出机构', minWidth: 120, align: 'center'},
        {field: 'repellentNo', title: '投保单号', minWidth: 100, align: 'center'},
        {field: 'insuranceNo', title: '保单号', minWidth: 100, align: 'center'},
        {field: 'status', title: '保单状态', minWidth: 120, align: 'center'},
        {field: 'childStatus', title: '保单子状态', minWidth: 100, align: 'center'},
        {field: 'insuranceName', title: '被保人姓名', minWidth: 120, align: 'center'},
        {field: 'productName', title: '产品名称', minWidth: 150, align: 'center'},
        {field: 'extraInsurance', title: '可附加险种', minWidth: 150, align: 'center'},
        {field: 'insuranceAmount', title: '保额', minWidth: 150, align: 'center'},
        {field: 'yearInsuranceFee', title: '年华保费', minWidth: 150, align: 'center'},
        {field: 'insuranceFee', title: '标准保费', minWidth: 150, align: 'center'},

        {field: 'tsrNumber', title: 'TSR工号', minWidth: 150, align: 'center'},
        {field: 'tsrName', title: 'TSR姓名', minWidth: 150, align: 'center'},
        {field: 'tlNumber', title: 'TL工号', minWidth: 150, align: 'center'},
        {field: 'tlName', title: 'TL姓名', minWidth: 150, align: 'center'},
        {field: 'orgName', title: '管理机构', minWidth: 150, align: 'center'},
        {field: 'department', title: '部', minWidth: 150, align: 'center'},
        {field: 'regionName', title: '区', minWidth: 150, align: 'center'},
        {field: 'groupName', title: '组', minWidth: 150, align: 'center'},

        {field: 'specialCaseName', title: '专案名称', minWidth: 150, align: 'center'},
        {field: 'insuranceDate', title: '承保日期', minWidth: 150, align: 'center'},
        {field: 'payTypeName', title: '交费频率', minWidth: 150, align: 'center'},
        {field: 'hesitateDate', title: '犹豫期退保日期', minWidth: 150, align: 'center'},
        {field: 'payInterval', title: '交费期间', minWidth: 150, align: 'center'},

        {field: 'createTime', title: '创建时间', minWidth: 150, align: 'center'},
        {field: 'updateTime', title: '更新时间', minWidth: 150, align: 'center'}
    ],
    formColumn: function () {
        var columnList = [];
        for(var index = 0; index < Repellent.column.length; index ++ ){
            var columnItem = Repellent.column[index];
            if(Repellent.$.inArray(columnItem.field, Repellent.fieldList) > 0) {
                columnList.push(columnItem);
            }
        }
        console.log(columnList);
        return columnList;
    },
    queryTable: function (table, queryJson) {
        var columnList = Repellent.formColumn();
        table.reload('repellent-table-reload',{
            page: { curr: 1 },
            cols: [ columnList ],
            where: queryJson
        });
    },
    renderTable: function (table) {
        var columnList = [];
        for(var index = 0; index < Repellent.column.length; index ++ ){
            var columnItem = Repellent.column[index];
            if(Repellent.$.inArray(columnItem.field, Repellent.fieldList) > 0) {
                columnList.push(columnItem);
            }
        }
        console.log(columnList);
        if(Repellent.table){
            Repellent.table.reload({
                cols: [[{field: 'id', title: 'ID', width: 50, sort: false, align: 'center', fixed: true}]],
            });
            Repellent.table.reload({
                cols: [ columnList ],
            });
            return;
        }
        Repellent.table = table.render({
            id: 'repellent-table-reload',
            elem: '#repellent-table-grid',
            url: '/admin/repellent/list',
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