layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload', 'formSelects'], function (exports) {
    var $ = layui.jquery;
    var table = layui.table;
    var laydate = layui.laydate;
    var formSelects = layui.formSelects;

    var windowHeight = $(window).height();
    var formHeight = $(".layui-form").height();
    $(".content-fixed").height(windowHeight - formHeight - 100);

    //广告活动选择器
    formSelects.config('advertiseActive', {
        beforeSuccess: function(id, url, searchVal, result){
            //我要把数据外层的code, msg, data去掉
            result = result.rtnData;
            return result;
        }
    }).data('advertiseActive', 'server', {
        url: '/admin/flowReserve/advertiseActiveList'
    }).on('advertiseActive', function(id, vals, val, isAdd, isDisabled){
        var value = '';
        for (var index in vals) {
            value += (vals[index].name + ",")
        }
        $("#advertiseActive").val(value);
    }, true);
    //时间选择器
    laydate.render({
        elem: '#chooseDate',
        range: true
    });

    insuranceEcharts.echartsMap["reserve-count-echart"] = echarts.init($("#reserve-count-echart")[0]);

    //搜索表单提交
    $("#search-btn").on("click", function () {
        insuranceEcharts.getClientEchartJson($, table, "reserve-count", "/admin/dashboard/client/getReserveSummary");
        insuranceEcharts.getTableItemJson($, table, "reserve-item", "/admin/dashboard/client/getReserveItem");
    });
    $("#search-btn").trigger("click");

    $(".layui-label.reserve-count .layui-btn-group button").on("click", function (selected) {
        $(".layui-label.reserve-count .layui-btn-group button").addClass("layui-btn-primary");
        $(this).removeClass("layui-btn-primary");

        insuranceEcharts.transformChooseItem = [];
        insuranceEcharts.transformChooseItem.push($(this).attr("data-value"));
        insuranceEcharts.getClientEchartJson($, table, "reserve-count", "/admin/dashboard/client/getReserveSummary");

    });
    $(".layui-label.reserve-item .layui-btn-group button").on("click", function (selected) {
        if($(this).hasClass("layui-btn-primary")){
            $(this).removeClass("layui-btn-primary");
            insuranceEcharts.chooseTableItem.push($(this).attr("data-value"));
        }else{
            $(this).addClass("layui-btn-primary");
            insuranceEcharts.chooseTableItem.splice($.inArray($(this).attr("data-value"), insuranceEcharts.chooseTableItem),1);
        }
        insuranceEcharts.getTableItemJson($, table, "reserve-item", "/admin/dashboard/client/getReserveItem");

    });
    window.onresize = function () {
        for(var echartIdx in insuranceEcharts.echartsMap){
            insuranceEcharts.echartsMap[echartIdx].resize();
        }
    };

    exports('client', {});
});

var insuranceEcharts = {
    transformChooseItem: ["browser"],
    chooseTableItem: ["browser"],
    echartsMap: {},

    getClientEchartJson: function ($, table, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                chooseItems: insuranceEcharts.transformChooseItem.join(","),
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.tableItemList.length <= 0){
                    return;
                }
                insuranceEcharts.drawClientTable($, table, selectedPrefix, response.rtnData.tableItemList);
                insuranceEcharts.drawClientEchart(selectedPrefix + "-echart", response.rtnData.titleList, response.rtnData.tableItemList);

            }
        });
    },
    getTableItemJson: function ($, table, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                chooseItems: insuranceEcharts.chooseTableItem.join(","),
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.tableItemList.length <= 0){
                    return;
                }
                insuranceEcharts.drawItemTable($, table, selectedPrefix, response.rtnData.tableItemList);
            }
        });
    },

    drawClientEchart: function (selectedId, titleList, contentList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                type: 'scroll',
                orient: 'vertical',
                top: '20%',
                right: '20%',
                data: titleList
            },
            series : [
                {
                    name: '名称',
                    type: 'pie',
                    label: {
                        show: false
                    },
                    radius : '55%',
                    center: ['40%', '50%'],
                    data: contentList,
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawClientTable: function ($, table, selectedId, tableItemList) {
        var tableHtml = "";
        tableHtml += "<tr>";
        if(insuranceEcharts.transformChooseItem.indexOf("browser") != -1){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'browser'}\">浏览器</th>";
        }else if(insuranceEcharts.transformChooseItem.indexOf("os") != -1){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'os'}\">操作系统</th>";
        }else if(insuranceEcharts.transformChooseItem.indexOf("resolutionRatio") != -1){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'resolutionRatio'}\">屏幕分辨率</th>";
        }else if(insuranceEcharts.transformChooseItem.indexOf("deviceType") != -1){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'deviceType'}\">设备类别</th>";
        }
        tableHtml += "<th lay-data=\"{align: 'center', field: 'value', sort: true}\">预约数量（个）</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'proportion'}\">百分比</th>";
        tableHtml += "</tr>";
        $("#" + selectedId + "-table-head").html(tableHtml);

        tableHtml = "";
        if(!tableItemList || tableItemList.length <= 0){
            tableHtml += "<tr>";
            for(var i in insuranceEcharts.transformChooseItem){
                tableHtml += "<td></td>";
            }
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "</tr>";
        }
        for(var index in tableItemList){
            tableHtml += "<tr>";
            for(var i in insuranceEcharts.transformChooseItem){
                if(insuranceEcharts.transformChooseItem[i] == "browser"){
                    tableHtml += "<td>" + tableItemList[index].browser + "</td>";
                }else if(insuranceEcharts.transformChooseItem[i] == "os"){
                    tableHtml += "<td>" + tableItemList[index].os + "</td>";
                }else if(insuranceEcharts.transformChooseItem[i] == "resolutionRatio"){
                    tableHtml += "<td>" + tableItemList[index].resolutionRatio + "</td>";
                }else if(insuranceEcharts.transformChooseItem[i] == "deviceType"){
                    tableHtml += "<td>" + tableItemList[index].deviceType + "</td>";
                }
            }
            tableHtml += "<td>" + tableItemList[index].value + "</td>";
            tableHtml += "<td>" + tableItemList[index].proportion + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: tableItemList.length,
            page: false,
            height: 385
        });
    },

    drawItemTable: function ($, table, selectedId, tableItemList) {
        var tableHtml = "";
        tableHtml += "<tr>";
        if(insuranceEcharts.chooseTableItem.indexOf("browser") != -1){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'browser'}\">浏览器</th>";
        }
        if(insuranceEcharts.chooseTableItem.indexOf("os") != -1){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'os'}\">操作系统</th>";
        }
        if(insuranceEcharts.chooseTableItem.indexOf("resolutionRatio") != -1){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'resolutionRatio'}\">屏幕分辨率</th>";
        }
        if(insuranceEcharts.chooseTableItem.indexOf("deviceType") != -1){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'deviceType'}\">设备类别</th>";
        }
        tableHtml += "<th lay-data=\"{align: 'center', field: 'value', sort: true}\">预约数量（个）</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'proportion'}\">百分比</th>";
        tableHtml += "</tr>";
        $("#" + selectedId + "-table-head").html(tableHtml);

        tableHtml = "";
        if(!tableItemList || tableItemList.length <= 0){
            tableHtml += "<tr>";
            for(var i in insuranceEcharts.chooseTableItem){
                tableHtml += "<td></td>";
            }
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "</tr>";
        }
        for(var index in tableItemList){
            tableHtml += "<tr>";
            for(var i in insuranceEcharts.chooseTableItem){
                if(insuranceEcharts.chooseTableItem[i].indexOf("browser") != -1){
                    tableHtml += "<td>" + tableItemList[index].browser + "</td>";
                }
                if(insuranceEcharts.chooseTableItem[i].indexOf("os") != -1){
                    tableHtml += "<td>" + tableItemList[index].os + "</td>";
                }
                if(insuranceEcharts.chooseTableItem[i].indexOf("resolutionRatio") != -1){
                    tableHtml += "<td>" + tableItemList[index].resolutionRatio + "</td>";
                }
                if(insuranceEcharts.chooseTableItem[i].indexOf("deviceType") != -1){
                    tableHtml += "<td>" + tableItemList[index].deviceType + "</td>";
                }
            }
            tableHtml += "<td>" + tableItemList[index].value + "</td>";
            tableHtml += "<td>" + tableItemList[index].proportion + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: tableItemList.length,
            page: false,
            height: 385
        });
    }
};