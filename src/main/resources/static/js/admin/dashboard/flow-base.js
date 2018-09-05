layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'layer'], function (exports) {
    var $ = layui.jquery;
    var layer = layui.layer;
    var table = layui.table;
    var laydate = layui.laydate;

    //时间选择器
    laydate.render({
        range: true,
        elem: '#chooseDate'
    });

    insuranceEcharts.echartsMap["flow-summary-echart"] = echarts.init($("#flow-summary-echart")[0]);
    insuranceEcharts.echartsMap["flow-user-echart"] = echarts.init($("#flow-user-echart")[0]);
    insuranceEcharts.echartsMap["inflow-source-echart"] = echarts.init($("#inflow-source-echart")[0]);
    insuranceEcharts.echartsMap["inflow-source-echart"].on("click", function (param) {
        var tableHtml = "";
        tableHtml += '<table class="layui-table" lay-size="sm" lay-filter="inflow-source-item-table">';
        tableHtml += '<thead id="inflow-source-item-table-head">';
        tableHtml += "<tr>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'areaName'}\">地区</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'inflowCount'}\">流入量</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'browseCount'}\">浏览量</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'userCount'}\">用户数</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'stayTime'}\">平均停留时间</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'againRate'}\">二跳率</th>";
        tableHtml += "</tr>";
        tableHtml += '</thead>';
        tableHtml += '<tbody id="inflow-source-item-table-body">';
        tableHtml += '</tbody>';
        tableHtml += '</table>';
        layer.open({
            type: 1,
            content: tableHtml,
            area: ['600px', '450px']
        });

        var tableItemList = [{areaName: "", inflowCount: 0, browseCount: 0, userCount: 0,  stayTime: 0, againRate: '0%'}];
        $.ajax({
            url: "/admin/dashboard/flow/getInflowSourceItem",
            data: {
                source: param.name,
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || response.rtnData.tableItemList.length <= 0){
                    insuranceEcharts.drawInflowSourceItemTable($, table, "inflow-source-item", tableItemList);
                }else{
                    insuranceEcharts.drawInflowSourceItemTable($, table, "inflow-source-item", response.rtnData.tableItemList);
                }
            },
            error: function () {
                insuranceEcharts.drawInflowSourceItemTable($, table, "inflow-source-item", tableItemList);
            }
        })
    });
    //搜索表单提交
    $("#search-btn").on("click", function () {
        insuranceEcharts.getDataJson2DrawFlowSummary($, "flow-summary", "/admin/dashboard/flow/getFlowSummary");
        insuranceEcharts.getDataJson2DrawFlowUser($, table, "flow-user", "/admin/dashboard/flow/getFlowUser");
        insuranceEcharts.getDataJson2DrawFlowSource($, table, "flow-source", "/admin/dashboard/flow/getFlowSource");
        insuranceEcharts.getDataJson2DrawInflowSource($, table, "inflow-source", "/admin/dashboard/flow/getInflowSource");
    });
    $("#search-btn").trigger("click");


    window.onresize = function () {
        for(var echartIdx in insuranceEcharts.echartsMap){
            insuranceEcharts.echartsMap[echartIdx].resize();
        }
    }

    exports('flow-base', {});
});

var insuranceEcharts = {
    echartsMap: {},

    // 获取后台JSON数据画图
    getDataJson2DrawFlowSummary: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawFlowSummaryLineEchart(selectedPrefix + "-echart",
                        ["日期", "日期", "日期", "日期", "日期", "日期"],
                        [0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawFlowSummaryLineEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList,
                    response.rtnData.browseCountList,
                    response.rtnData.userCountList);

                $("#browseTotalCount").val(response.rtnData.flowSummary.browseTotalCount);
                $("#inflowTotalCount").val(response.rtnData.flowSummary.inflowTotalCount);
                $("#userTotalCount").val(response.rtnData.flowSummary.userTotalCount);
                $("#againTotalCount").val(response.rtnData.flowSummary.againTotalCount);
                $("#againTotalRate").val(response.rtnData.flowSummary.againTotalRate);
                $("#averageStayTime").val(response.rtnData.flowSummary.averageStayTime);
                $("#averageBrowsePage").val(response.rtnData.flowSummary.averageBrowsePage);
            }
        });
    },
    getDataJson2DrawFlowUser: function ($, table, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || response.rtnData.titleList.length <= 0){
                    var tableItemList = [{name: "省份1", value: 0, proportion: '0%'},
                        {name: "省份2", value: 0, proportion: '0%'},
                        {name: "省份3", value: 0, proportion: '0%'},
                        {name: "省份4", value: 0, proportion: '0%'},
                        {name: "省份5", value: 0, proportion: '0%'},
                        {name: "省份6", value: 0, proportion: '0%'},
                        {name: "省份7", value: 0, proportion: '0%'}];
                    insuranceEcharts.drawFlowUserPieEchart(selectedPrefix + "-echart",
                        ["省份1", "省份2", "省份3", "省份4", "省份5", "省份6", "省份7"], tableItemList);
                    insuranceEcharts.drawFlowUserTable($, table, selectedPrefix, tableItemList);
                    return;
                }
                insuranceEcharts.drawFlowUserPieEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList,
                    response.rtnData.tableItemList);
                insuranceEcharts.drawFlowUserTable($, table, selectedPrefix, response.rtnData.tableItemList);
            }
        });
    },
    getDataJson2DrawFlowSource: function ($, table, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                source: $("#source").val(),
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData){
                    var tableItemList = [{name: "省份1", value: 0, proportion: '0%'},
                        {name: "省份2", value: 0, proportion: '0%'},
                        {name: "省份3", value: 0, proportion: '0%'},
                        {name: "省份4", value: 0, proportion: '0%'},
                        {name: "省份5", value: 0, proportion: '0%'},
                        {name: "省份6", value: 0, proportion: '0%'},
                        {name: "省份7", value: 0, proportion: '0%'}];
                    insuranceEcharts.drawFlowSourceTable($, table, selectedPrefix,
                        ["省份1", "省份2", "省份3", "省份4", "省份5", "省份6", "省份7"], tableItemList);
                    return;
                }
                insuranceEcharts.drawFlowSourceTable($, table, selectedPrefix, response.rtnData.tableItemList);
            }
        });
    },
    getDataJson2DrawInflowSource: function ($, table, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || response.rtnData.tableItemList.length <= 0){
                    var tableItemList = [{name: "访问来源1", value: 0, proportion: '0%'},
                        {name: "访问来源2", value: 0, proportion: '0%'},
                        {name: "访问来源3", value: 0, proportion: '0%'},
                        {name: "访问来源4", value: 0, proportion: '0%'},
                        {name: "访问来源5", value: 0, proportion: '0%'},
                        {name: "访问来源6", value: 0, proportion: '0%'},
                        {name: "访问来源7", value: 0, proportion: '0%'}];
                    insuranceEcharts.drawInflowSourcePieEchart(selectedPrefix + "-echart", tableItemList);
                    insuranceEcharts.drawInflowSourceTable($, table, selectedPrefix, tableItemList);
                    return;
                }
                insuranceEcharts.drawInflowSourcePieEchart(selectedPrefix + "-echart", response.rtnData.tableItemList);
                insuranceEcharts.drawInflowSourceTable($, table, selectedPrefix, response.rtnData.tableItemList);
            }
        });
    },

    drawFlowSummaryLineEchart: function (selectedId, titleList, browseCountList, userCountList) {

        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            grid: {
                top: '10%',
                bottom: '10%',
                left: '10%'
            },
            legend: {
                data: [
                    '浏览量',
                    '用户数'
                ]
            },
            xAxis: {
                type: 'category',
                data: titleList
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    data: browseCountList,
                    name: '浏览量',
                    type: 'line',
                    symbol: 'circle',
                    smooth: '0.5',
                    lineStyle: {
                        normal: {
                            color: 'red',
                            type: 'solid'
                        }
                    }
                },
                {
                    data: userCountList,
                    name: '用户数',
                    type: 'line',
                    symbol: 'circle',
                    smooth: '0.5',
                    lineStyle: {
                        normal: {
                            color: 'green',
                            type: 'solid'
                        }
                    }
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawFlowUserPieEchart: function (selectedId, titleList, tableItemList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            series : [
                {
                    name: '名称',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '50%'],
                    data: tableItemList,
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
    drawFlowUserTable: function ($, table, selectedId, tableItemList) {
        var tableHtml = "";
        tableHtml += "<tr>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'name'}\">地区</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'value'}\">用户数</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'proportion'}\">百分比</th>";
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
            tableHtml += "<td>" + tableItemList[index].name + "</td>";
            tableHtml += "<td>" + tableItemList[index].value + "</td>";
            tableHtml += "<td>" + tableItemList[index].proportion + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: 10
        });
    },

    drawFlowSourceTable: function ($, table, selectedId, tableItemList) {
        var tableHtml = "";
        tableHtml += "<tr>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'website'}\">来源网站</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'inflowCount'}\">流入数</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'againRate'}\">二跳率</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'stayTime'}\">平均停留时间(S)</th>";
        tableHtml += "</tr>";
        $("#" + selectedId + "-table-head").html(tableHtml);

        tableHtml = "";
        if(!tableItemList || tableItemList.length <= 0){
            tableHtml += "<tr>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "</tr>";
        }
        for(var index in tableItemList){
            tableHtml += "<tr>";
            tableHtml += "<td>" + tableItemList[index].website + "</td>";
            tableHtml += "<td>" + tableItemList[index].inflowCount + "</td>";
            tableHtml += "<td>" + tableItemList[index].againRate + "</td>";
            tableHtml += "<td>" + tableItemList[index].stayTime + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: 10,
            page: true
        });
    },

    drawInflowSourcePieEchart: function (selectedId, tableItemList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            series : [
                {
                    name: '名称',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '50%'],
                    data: tableItemList,
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
    drawInflowSourceTable: function ($, table, selectedId, tableItemList) {
        var tableHtml = "";
        tableHtml += "<tr>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'name'}\">访问来源</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'value'}\">用户数</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'proportion'}\">百分比</th>";
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
            tableHtml += "<td>" + tableItemList[index].name + "</td>";
            tableHtml += "<td>" + tableItemList[index].value + "</td>";
            tableHtml += "<td>" + tableItemList[index].proportion + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: 10
        });
    },
    drawInflowSourceItemTable: function ($, table, selectedId, tableItemList) {
        var tableHtml = "";
        if(!tableItemList || tableItemList.length <= 0){
            tableHtml += "<tr>";
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
            tableHtml += "<td>" + tableItemList[index].areaName + "</td>";
            tableHtml += "<td>" + tableItemList[index].inflowCount + "</td>";
            tableHtml += "<td>" + tableItemList[index].browseCount + "</td>";
            tableHtml += "<td>" + tableItemList[index].userCount + "</td>";
            tableHtml += "<td>" + tableItemList[index].stayTime + "</td>";
            tableHtml += "<td>" + tableItemList[index].againRate + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: 10,
            page: true
        });
    }
};