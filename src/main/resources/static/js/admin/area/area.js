layui.define(['form', 'table', 'layer', 'jquery'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form;
    var table = layui.table;
    var layer = layui.layer;

    $("#importAreaJson").on("click", function () {
        layer.prompt(function(value, index){
            if(value != "helloworld"){
                layer.msg('密码错误，该操作极其危险，请确认得到了授权');
                return;
            }
            layer.close(index);
            $.get("/static/json/area.json",function(response, status){
                if(status == 'success'){
                    // 获取省份数据
                    for(var provinceId in response){
                        var province = response[provinceId];
                        var provinceName = province.name;
                        $.ajax({
                            url: "/admin/area/save",
                            data: {
                                areaId: provinceId,
                                areaName: provinceName,
                                parentId: 0,
                                areaType: 1
                            }
                        });

                        // 获取城市数据
                        var cityList = province.child;
                        for(var cityId in cityList){
                            var city = cityList[cityId];
                            var cityName = city.name;
                            $.ajax({
                                url: "/admin/area/save",
                                data: {
                                    areaId: cityId,
                                    areaName: cityName,
                                    parentId: provinceId,
                                    areaType: 2
                                }
                            });

                            // 获得地区数据
                            var areaList = city.child;
                            for(var areaId in areaList){
                                var areaName = areaList[areaId];
                                $.ajax({
                                    url: "/admin/area/save",
                                    data: {
                                        areaId: areaId,
                                        areaName: areaName,
                                        parentId: cityId,
                                        areaType: 3
                                    }
                                })
                            }
                        }

                    }
                }
            });
        });

    });

    //重置事件
    $(".reset-btn").on("click", function () {
        $("#areaId").val("");
        $("#areaName").val("");
        $("#areaType").val("");
    });
    //搜索表单提交
    form.on('submit(search-btn)', function(data){
        table.reload('area-table-reload',{
            page: { curr: 1 },
            where: {
                areaId: data.field.areaId,
                areaName: data.field.areaName,
                areaType: data.field.areaType
            }});
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //方法级渲染
    table.render({
        elem: '#area-table-grid',
        url: '/admin/area/list',
        cols: [
            [
                {field: 'areaId', title: '地区ID', sort: false, align: 'center', fixed: true},
                {field: 'areaName', title: '地区名称', align: 'center'},
                {field: 'parentName', title: '父级地区', align: 'center'},
                {field: 'areaType', title: '地区类型', align: 'center'},
                {field: 'status', title: '数据状态', align: 'center'},
                {field: 'createTime', title: '创建时间', sort: true, align: 'center'},
                {field: 'updateTime', title: '更新时间', sort: true, align: 'center'}
            ]
        ],
        id: 'area-table-reload',
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


    exports('area', {});
});