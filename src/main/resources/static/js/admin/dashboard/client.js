layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload'], function (exports) {
    var $ = layui.jquery;
    var table = layui.table;
    var laydate = layui.laydate;

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

        insuranceEcharts.chooseItem = [];
        insuranceEcharts.chooseItem.push($(this).attr("data-value"));
        insuranceEcharts.getClientEchartJson($, table, "reserve-count", "/admin/dashboard/client/getReserveSummary");

    });
    $(".layui-label.reserve-item .layui-btn-group button").on("click", function (selected) {
        if($(this).hasClass("layui-btn-primary")){
            $(this).removeClass("layui-btn-primary");
            insuranceEcharts.chooseTableItem.push($(this).attr("data-value"));
        }else{
            $(this).addClass("layui-btn-primary");
            insuranceEcharts.chooseTableItem.pop($(this).attr("data-value"));
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
    chooseItem: ["browser"],
    chooseTableItem: ["browser"],
    echartsMap: {},

    getClientEchartJson: function ($, table, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                chooseItems: insuranceEcharts.chooseItem.join(","),
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
                data: titleList,
                selected: true
            },
            series : [
                {
                    name: '名称',
                    type: 'pie',
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
        if(insuranceEcharts.chooseItem.indexOf("browser") != -1){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'browser'}\">浏览器</th>";
        }else if(insuranceEcharts.chooseItem.indexOf("os") != -1){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'os'}\">操作系统</th>";
        }else if(insuranceEcharts.chooseItem.indexOf("resolutionRatio") != -1){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'resolutionRatio'}\">屏幕分辨率</th>";
        }else if(insuranceEcharts.chooseItem.indexOf("deviceType") != -1){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'deviceType'}\">设备类别</th>";
        }
        tableHtml += "<th lay-data=\"{align: 'center', field: 'value'}\">预约数量</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'proportion'}\">百分比</th>";
        tableHtml += "</tr>";
        $("#" + selectedId + "-table-head").html(tableHtml);

        tableHtml = "";
        if(!tableItemList || tableItemList.length <= 0){
            tableHtml += "<tr>";
            for(var i in insuranceEcharts.chooseItem){
                tableHtml += "<td></td>";
            }
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "</tr>";
        }
        for(var index in tableItemList){
            tableHtml += "<tr>";
            for(var i in insuranceEcharts.chooseItem){
                if(insuranceEcharts.chooseItem[i] == "browser"){
                    tableHtml += "<td>" + tableItemList[index].browser + "</td>";
                }else if(insuranceEcharts.chooseItem[i] == "os"){
                    tableHtml += "<td>" + tableItemList[index].os + "</td>";
                }else if(insuranceEcharts.chooseItem[i] == "resolutionRatio"){
                    tableHtml += "<td>" + tableItemList[index].resolutionRatio + "</td>";
                }else if(insuranceEcharts.chooseItem[i] == "deviceType"){
                    tableHtml += "<td>" + tableItemList[index].deviceType + "</td>";
                }
            }
            tableHtml += "<td>" + tableItemList[index].value + "</td>";
            tableHtml += "<td>" + tableItemList[index].proportion + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: 10,
            page: true
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
        tableHtml += "<th lay-data=\"{align: 'center', field: 'value'}\">预约数量</th>";
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
            limit: 10,
            page: true
        });
    }
};