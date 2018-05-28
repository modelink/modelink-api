layui.define(['table', 'laydate', 'jquery'], function (exports) {
    var $ = layui.jquery;
    var table = layui.table;
    var laydate = layui.laydate;

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
                {field: 'sourceType', title: '预约入口', align: 'center'},
                {field: 'status', title: '数据状态', align: 'center'},
                {field: 'createTime', title: '创建时间', sort: true, align: 'center'},
                {field: 'updateTime', title: '更新时间', sort: true, align: 'center'}
            ]
        ],
        id: 'reserve-table-reload',
        page: true,
        response: {
            statusName: 'rtnCode', //数据状态的字段名称，默认：code
            statusCode: 200, //成功的状态码，默认：0
            msgName: 'rtnMsg', //状态信息的字段名称，默认：msg
            countName: 'totalCount', //数据总数的字段名称，默认：count
            dataName: 'rtnList' //数据列表的字段名称，默认：data
        }
    });

    var active = {
        reload: function () {
            var demoReload = $('#demoReload');

            //执行重载
            table.reload('reserve-table-reload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    key: {
                        id: demoReload.val()
                    }
                }
            });
        }
    };

    $('.demoTable .layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});