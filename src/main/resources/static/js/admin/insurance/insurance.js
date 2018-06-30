layui.define(['form', 'table', 'laydate', 'jquery'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form;
    var table = layui.table;
    var laydate = layui.laydate;

    Insurance.$ = $;

    /** 生成选择列属性的div **/
    var columnItem;
    var chooseColumnHtml = "";
    for(var index in Insurance.column){
        var checked = "";
        columnItem = Insurance.column[index];
        if($.inArray(columnItem.field, Insurance.fieldList) > 0){
            checked = "checked";
        }
        chooseColumnHtml += '<input type="checkbox" value="' + columnItem.field + '" title="' + columnItem.title + '" ' + checked + '>';
    }
    $("#columnField").html(chooseColumnHtml);
    form.render();

    Insurance.renderTable(table);
    form.on('checkbox', function(data){
        var isChecked = data.elem.checked;
        var fieldValue = data.elem.value;
        if(isChecked && $.inArray(fieldValue, Insurance.fieldList) < 0){
            Insurance.fieldList.push(fieldValue);
        }
        if(!isChecked && $.inArray(fieldValue, Insurance.fieldList)){
            Insurance.fieldList.splice($.inArray(fieldValue, Insurance.fieldList), 1);
        }
        console.log(Insurance.fieldList);
        Insurance.renderTable(table);
    });

    /** 设置表头事件处理逻辑 **/
    var active = {
        setColumnField: function(){ //获取选中数据
            if($("#columnField").is(':visible')) {
                $("#columnField").hide();
            }else{
                $("#columnField").show();
            }
        },
        importExcel: function(){ //获取选中数目
            var checkStatus = table.checkStatus('idTest')
                ,data = checkStatus.data;
            layer.msg('选中了：'+ data.length + ' 个');
        },
        exportExcel: function(){ //验证是否全选
            var checkStatus = table.checkStatus('idTest');
            layer.msg(checkStatus.isAll ? '全选': '未全选')
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
        $("#contactMobile").val("");
    });
    //搜索表单提交
    form.on('submit(search-btn)', function(data){
        console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
        console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
        console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
        table.reload('insurance-table-reload',{
            page: { pageNo: 1 },
            where: {
                chooseDate: data.field.chooseDate,
                mobile: data.field.mobile
            }});
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    //搜索表单提交
    form.on('submit(download-btn)', function(data){
        data.form.action = "/admin/insurance/download";
        data.form.submit();
    });
    //选择日期
    laydate.render({
        elem: '#chooseDate',
        range: true
    });

    exports('insurance', {});
});

var Insurance = {
    $: null,
    table: null,
    fieldList: [
        '',
        'id',
        'insuranceNo',
        'name',
        'mobile',
        'age',

        'contactTime',
        'merchantId',

        'callStatus',
        'problem',

        'payType',
        'insuranceAmount',
        'insuranceFee',
        'finishTime'
    ],
    column: [
        {field: 'id', title: 'ID', width: 50, sort: false, align: 'center', fixed: true},
        {field: 'insuranceNo', title: '保单编号', minWidth: 100, align: 'center'},
        {field: 'name', title: '投保人姓名', minWidth: 120, align: 'center'},
        {field: 'mobile', title: '投保人电话', minWidth: 120, align: 'center'},
        {field: 'gender', title: '投保人性别', minWidth: 100, align: 'center'},
        {field: 'age', title: '投保人年龄', minWidth: 100, align: 'center'},
        {field: 'birthday', title: '投保人生日', minWidth: 120, align: 'center'},
        {field: 'address', title: '投保人地址', minWidth: 150, align: 'center'},

        {field: 'contactTime', title: '预约时间', minWidth: 120, align: 'center'},
        {field: 'arrangeTime', title: '下发时间', minWidth: 120, align: 'center'},
        {field: 'merchantId', title: '合作商户', minWidth: 100, align: 'center'},
        {field: 'platform', title: '渠道归属', minWidth: 100, align: 'center'},
        {field: 'dataType', title: '渠道明细', minWidth: 100, align: 'center'},
        {field: 'sourceType', title: '入口类型', minWidth: 100, align: 'center'},

        {field: 'orgName', title: '机构名称', minWidth: 100, align: 'center'},
        {field: 'tsrName', title: 'TSR姓名', minWidth: 100, align: 'center'},
        {field: 'firstCall', title: '第1天拨打', minWidth: 100, align: 'center'},
        {field: 'secondCall', title: '第2天拨打', minWidth: 100, align: 'center'},
        {field: 'threeCall', title: '第3天拨打', minWidth: 100, align: 'center'},
        {field: 'callStatus', title: '拨打状态', minWidth: 100, align: 'center'},
        {field: 'problem', title: '问题数据', minWidth: 100, align: 'center'},

        {field: 'payType', title: '缴费类型', minWidth: 100, align: 'center'},
        {field: 'insuranceAmount', title: '保额', minWidth: 100, align: 'center'},
        {field: 'insuranceFee', title: '保费', minWidth: 100, align: 'center'},
        {field: 'finishTime', title: '成单日期', minWidth: 120, align: 'center'},
        {field: 'remark', title: '备注', minWidth: 100, align: 'center'},

        {field: 'createTime', title: '创建时间', minWidth: 180, align: 'center'},
        {field: 'updateTime', title: '更新时间', minWidth: 180, align: 'center'}
    ],
    renderTable: function (table) {
        var columnList = [];
        for(var index = 0; index < Insurance.column.length; index ++ ){
            var columnItem = Insurance.column[index];
            if(Insurance.$.inArray(columnItem.field, Insurance.fieldList) > 0) {
                columnList.push(columnItem);
            }
        }

        if(Insurance.table){
            table.render({
                cols: [ columnList ],
            })
            return;
        }
        Insurance.table = table.render({
            id: 'insurance-table-reload',
            elem: '#insurance-table-grid',
            url: '/admin/insurance/list',
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