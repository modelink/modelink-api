layui.define(['form', 'table', 'layer', 'laydate', 'jquery'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form;
    var layer = layui.layer;
    var table = layui.table;
    var laydate = layui.laydate;

    //重置事件
    $(".reset-btn").on("click", function () {
        $("#chooseDate").val("");
        $("#dataSource").val("");
    });
    //搜索表单提交
    form.on('submit(search-btn)', function(data){
        table.reload('summary-table-reload',{
            page: { curr: 1 },
            where: {
                chooseDate: data.field.chooseDate,
                dataSource: data.field.dataSource
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
        elem: '#summary-table-grid',
        url: '/admin/huaxiaReport/summaryList',
        cols: [
            [
                {checkbox: true, fixed: true},
                {field: 'date', title: '日期', minWidth: 120, align: 'center', fixed: true},
                {field: 'dataSource', title: '数据来源', minWidth: 120, align: 'center'},
                {field: 'validArrange', title: '华夏有效数据（下发）', minWidth: 160, align: 'center'},
                {field: 'totalCount', title: '总转化/测保转化（包含小米）', minWidth: 160, align: 'center'},
                {field: 'miniTotalCount', title: '总转化（不含微信）/测保转化（不含小米）', minWidth: 160, align: 'center'},
                {field: 'pcCount', title: 'PC', minWidth: 160, align: 'center'},
                {field: 'wapCount', title: 'WAP', minWidth: 160, align: 'center'},
                {field: 'weixinCount', title: '微信转化', minWidth: 120, align: 'center'},
                {field: 'directTransformCount', title: '广告直接转化数', minWidth: 100, align: 'center'},

                {field: 'browseCount', title: '浏览量', minWidth: 100, align: 'center'},
                {field: 'clickCount', title: '点击量', minWidth: 120, align: 'center'},
                {field: 'arriveCount', title: '到达量', minWidth: 120, align: 'center'},
                {field: 'arriveUserCount', title: '到达用户', minWidth: 120, align: 'center'},
                {field: 'arriveRate', title: '到达率（%）', minWidth: 120, align: 'center'},
                {field: 'againCount', title: '二跳量', minWidth: 120, align: 'center'},
                {field: 'againRate', title: '二跳率（%）', minWidth: 120, align: 'center'},
                {field: 'averageStayTime', title: '平均停留时间（S）', minWidth: 120, align: 'center'},

                {field: 'mediaShowCount', title: '展示数', minWidth: 120, align: 'center'},
                {field: 'mediaClickCount', title: '点击数', minWidth: 120, align: 'center'},
                {field: 'mediaClickRate', title: '点击率（%）', minWidth: 120, align: 'center'},
                {field: 'cpc', title: 'CPC（元）', minWidth: 120, align: 'center'},
                {field: 'cpm', title: 'CPM（元）', minWidth: 120, align: 'center'},
                {field: 'consumeAmount', title: '总花费（元）', minWidth: 120, align: 'center'},

                {field: 'directTransformCost', title: '直接转化成本', minWidth: 120, align: 'center'},
                {field: 'totalTransformCost', title: '总转化成本（不含微信）', minWidth: 120, align: 'center'},
                {field: 'insuranceAmount', title: '保费（元）', minWidth: 120, align: 'center'}
            ]
        ],
        id: 'summary-table-reload',
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
    table.on('toolbar(summary-table-grid)', function(element){
        switch(element.event){
            case 'download':

                var contentHtml = '';
                contentHtml += '<div style="margin: 30px;">';
                contentHtml += '<fieldset class="layui-elem-field">';
                contentHtml += '	<legend class="message-tip">选择下载项目</legend>';
                contentHtml += '	<div class="layui-field-box">';
                contentHtml += '	    <div class="layui-form">';
                contentHtml += '	    <table style="width: 100%">';
                contentHtml += '			<tr>';
                contentHtml += '			    <td><input type="checkbox" name="reports" title="预约" value="预约"></td>';
                contentHtml += '			    <td><input type="checkbox" name="reports" title="测保" value="测保"></td>';
                contentHtml += '			</tr>';
                contentHtml += '	    </table>';
                contentHtml += '	    </div>';
                contentHtml += '	</div>';
                contentHtml += '</fieldset>';
                contentHtml += '</div>';


                layer.open({
                    title: ['导出报表', 'text-align: center; padding-left: 80px;'],
                    content: contentHtml,
                    area: ['600px', '300px'],
                    btnAlign: 'c',
                    yes: function(index, layero){
                        var reports = [];
                        $("input:checkbox[name=reports]:checked").each(function () {
                            reports.push($(this).val());
                        });
                        if (reports.length <= 0) {
                            layer.tips("请选择下载项目", ".message-tip");
                            return false;
                        }
                        var chooseDate = $("#chooseDate").val() ? $("#chooseDate").val() : "";
                        window.location.href = ("/admin/huaxiaReport/summaryDownload?chooseDate=" + chooseDate + "&reports=" + reports.join(","));
                        layer.close(index);
                    }
                });
                form.render();
                form.on('submit(doDownload)', function(data){
                    var reports = [];
                    $("input:checkbox[name=reports]:checked").each(function () {
                        reports.push($(this).val());
                    });
                    console.log(reports);
                    layer.close(index);
                    return false;
                });
                break;
        };
    });

    exports('summary', {});
});