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
        if(insuranceEcharts.dateType == "6"){
            insuranceEcharts.getDataJson2DrawHour($, table, "reserve-count", "/admin/dashboard/time/getReserveCountByHour");
        }else{
            insuranceEcharts.getDataJson2DrawDate($, table, "reserve-count", "/admin/dashboard/time/getUnderwriteSummaryByDate");
        }
    });
    $("#search-btn").trigger("click");

    $(".layui-label .layui-btn-group button").on("click", function (selected) {
        $(".layui-label .layui-btn-group button").addClass("layui-btn-primary");
        $(this).removeClass("layui-btn-primary");
        insuranceEcharts.dateType = $(this).attr("data-value");

        if(insuranceEcharts.dateType == "6"){
            insuranceEcharts.getDataJson2DrawHour($, table, "reserve-count", "/admin/dashboard/time/getReserveCountByHour");
        }else{
            insuranceEcharts.getDataJson2DrawDate($, table, "reserve-count", "/admin/dashboard/time/getUnderwriteSummaryByDate");
        }

    });
    window.onresize = function () {
        for(var echartIdx in insuranceEcharts.echartsMap){
            insuranceEcharts.echartsMap[echartIdx].resize();
        }
    };

    exports('time', {});
});

var insuranceEcharts = {
    dateType: '6',
    echartsMap: {},

    getDataJson2DrawHour: function ($, table, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                dateType: insuranceEcharts.dateType,
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    return;
                }
                insuranceEcharts.drawHourTable($, table, selectedPrefix, response.rtnData.tableItemList);
                insuranceEcharts.drawHourEchart(selectedPrefix + "-echart", response.rtnData.titleList, response.rtnData.contentList);
            }
        });
    },
    getDataJson: function ($, table, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                dateType: insuranceEcharts.dateType,
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    return;
                }
                insuranceEcharts.drawDateTable($, table, selectedPrefix, response.rtnData.tableItemList);
                insuranceEcharts.drawDateEchart(selectedPrefix + "-echart", response.rtnData.titleList,
                    response.rtnData.reserveCountList, response.rtnData.underwriteCountList);
            }
        });
    },

    drawHourEchart: function (selectedId, titleList, contentList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            grid: {
                show: true,
                top: "10%",
                right: "10%",
                borderColor: "#c45455",//网格的边框颜色
                bottom: "20%" //
            },
            xAxis: {
                type: 'category',
                axisLabel: {
                    interval: 0,
                    rotate: "45"
                },
                data: titleList
            },
            yAxis: [
                {
                    type: 'value',
                    position: 'left',
                    axisLabel: {
                        formatter: '{value}'
                    }
                }
            ],
            series: [
                {
                    data: contentList,
                    barWidth: '50%',
                    barGap: '0',
                    type: 'bar',
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawHourTable: function ($, table, selectedId, tableItemList) {
        var tableHtml = "";
        tableHtml += "<tr>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'provinceName'}\">地域</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'underwriteCount'}\">承保数</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'proportion'}\">占比</th>";
        tableHtml += "</tr>";
        $("#" + selectedId + "-table-head").html(tableHtml);

        tableHtml = "";
        if(!tableItemList || tableItemList.length <= 0){
            tableHtml += "<tr>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "</tr>";
        }
        for(var index in tableItemList){
            tableHtml += "<tr>";
            tableHtml += "<td>" + tableItemList[index].time + "</td>";
            tableHtml += "<td>" + tableItemList[index].reserveCount + "</td>";
            tableHtml += "<td>" + tableItemList[index].proportion + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: 10,
            page: true
        });
    },

    drawEchart: function (selectedId, titleList, reserveCountList, underwriteCountList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            grid: {
                show: true,
                top: "10%",
                right: "10%",
                borderColor: "#c45455",//网格的边框颜色
                bottom: "20%" //
            },
            xAxis: {
                type: 'category',
                axisLabel: {
                    interval: 0,
                    rotate: "45"
                },
                data: titleList
            },
            yAxis: [
                {
                    type: 'value',
                    position: 'left',
                    axisLabel: {
                        formatter: '{value}'
                    }
                },{
                    type: 'value',
                    position: 'right',
                    axisLabel: {
                        formatter: '{value}'
                    }
                }
            ],
            series: [
                {
                    data: reserveCountList,
                    barWidth: '35%',
                    yAxisIndex: 0,
                    barGap: '0',
                    type: 'bar',
                },
                {
                    data: underwriteCountList,
                    barWidth: '35%',
                    yAxisIndex: 1,
                    barGap: '0',
                    type: 'bar',
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawTable: function ($, table, selectedId, tableItemList) {
        var tableHtml = "";
        tableHtml += "<tr>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'date'}\">日期</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'reserveCount'}\">预约数量</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'transformCost'}\">转化成本</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'transformCycle'}\">转化周期</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'underwriteCount'}\">承保件数</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'underwriteAmount'}\">保费</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'transformRate'}\">总转化率</th>";
        tableHtml += "</tr>";
        $("#" + selectedId + "-table-head").html(tableHtml);

        tableHtml = "";
        if(!tableItemList || tableItemList.length <= 0){
            tableHtml += "<tr>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "</tr>";
        }
        for(var index in tableItemList){
            tableHtml += "<tr>";
            tableHtml += "<td>" + tableItemList[index].date + "</td>";
            tableHtml += "<td>" + tableItemList[index].reserveCount + "</td>";
            tableHtml += "<td>" + tableItemList[index].transformCost + "</td>";
            tableHtml += "<td>" + tableItemList[index].transformCycle + "</td>";
            tableHtml += "<td>" + tableItemList[index].underwriteCount + "</td>";
            tableHtml += "<td>" + tableItemList[index].underwriteAmount + "</td>";
            tableHtml += "<td>" + tableItemList[index].transformRate + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: 10,
            page: true
        });
    }
};