layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form;
    var table = layui.table;
    var laydate = layui.laydate;

    //时间选择器
    laydate.render({
        elem: '#chooseDate',
        range: true
    });

    insuranceEcharts.echartsMap["consume-trend-echart"] = echarts.init($("#consume-trend-echart")[0]);
    insuranceEcharts.echartsMap["consume-trend-echart"].on("click", function (param) {
        var selectedId;
        var advertiseActive = $("#advertiseActive").val();

        if(advertiseActive){
            selectedId = "consume-trend-table";
            var date = param.name;
            $.ajax({
                url: "/admin/dashboard/summary/getConsumeTableByDate",
                data: {
                    date: date,
                    merchantId: $("#merchant").val(),
                    platformName: $("#platformName").val(),
                    advertiseActive: $("#advertiseActive").val()
                },
                success: function (response) {
                    if(response && response.rtnCode === 200 && response.rtnData){
                        var tableHtml = "";
                        tableHtml += '<table class="layui-table" lay-size="sm" lay-filter="' + selectedId + '-table">';
                        tableHtml += '<thead id="' + selectedId + '-table-head">';
                        tableHtml += "<tr>";
                        tableHtml += "<th lay-data=\"{align: 'center', field: 'advertiseSeries'}\">广告系列</th>";
                        tableHtml += "<th lay-data=\"{align: 'center', field: 'stayTime'}\">关键词</th>";
                        tableHtml += "<th lay-data=\"{align: 'center', field: 'againRate', sort: true}\">消费（元）</th>";
                        tableHtml += "</tr>";
                        tableHtml += '</thead>';
                        tableHtml += '<tbody id="' + selectedId + '-table-body">';
                        tableHtml += '</tbody>';
                        tableHtml += '</table>';
                        layer.open({
                            type: 1,
                            content: tableHtml,
                            area: ['600px', '450px']
                        });
                        insuranceEcharts.drawConsumeTrendTable($, table, selectedId, response.rtnData);
                    }
                }
            });
        }else{
            selectedId = "consume-trend-pie-echart";
            var date = param.name;
            $.ajax({
                url: "/admin/dashboard/summary/getConsumePieByDate",
                data: {
                    date: date,
                    merchantId: $("#merchant").val(),
                    platformName: $("#platformName").val()
                },
                success: function (response) {
                    if(response && response.rtnCode === 200 && response.rtnData){
                        var tableHtml = '<div id="' + selectedId + '" style="height: 400px;"></div>';
                        layer.open({
                            type: 1,
                            content: tableHtml,
                            area: ['600px', '450px']
                        });
                        insuranceEcharts.echartsMap[selectedId] = echarts.init($("#" + selectedId)[0]);;
                        insuranceEcharts.drawPieEchart(selectedId, response.rtnData);
                    }
                }
            });
        }
    });
    insuranceEcharts.echartsMap["transform-summary-echart"] = echarts.init($("#transform-summary-echart")[0]);
    insuranceEcharts.echartsMap["transform-summary-echart"].on("click", function (param) {
        var selectedId;
        var date = param.name;
        var advertiseActive = $("#advertiseActive").val();

        if(advertiseActive){
            selectedId = "transform-summary-keyword";
        }else{
            selectedId = "transform-summary-advertiseActive";
        }
        $.ajax({
            url: "/admin/dashboard/summary/getTransformSummaryByDate",
            data: {
                date: date,
                merchantId: $("#merchant").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(response && response.rtnCode === 200 && response.rtnData){
                    var tableHtml = "";
                    tableHtml += '<table class="layui-table" lay-size="sm" lay-filter="' + selectedId + '-table">';
                    tableHtml += '<thead id="' + selectedId + '-table-head">';
                    tableHtml += '</thead>';
                    tableHtml += '<tbody id="' + selectedId + '-table-body">';
                    tableHtml += '</tbody>';
                    tableHtml += '</table>';
                    layer.open({
                        type: 1,
                        content: tableHtml,
                        area: ['600px', '450px']
                    });
                    insuranceEcharts.drawTransformSummaryTable($, table, selectedId, response.rtnData);
                }
            }
        });
    });
    insuranceEcharts.echartsMap["abnormal-count-echart"] = echarts.init($("#abnormal-count-echart")[0]);
    insuranceEcharts.echartsMap["repellent-amount-echart"] = echarts.init($("#repellent-amount-echart")[0]);

    insuranceEcharts.echartsMap["transform-cycle-echart"] = echarts.init($("#transform-cycle-echart")[0]);
    insuranceEcharts.echartsMap["transform-rate-echart"] = echarts.init($("#transform-rate-echart")[0]);
    insuranceEcharts.echartsMap["cost-summary-echart"] = echarts.init($("#cost-summary-echart")[0]);

    insuranceEcharts.echartsMap["transform-cost-rank-echart"] = echarts.init($("#transform-cost-rank-echart")[0]);
    insuranceEcharts.echartsMap["underwrite-count-rank-echart"] = echarts.init($("#underwrite-count-rank-echart")[0]);
    insuranceEcharts.echartsMap["underwrite-amount-rank-echart"] = echarts.init($("#underwrite-amount-rank-echart")[0]);


    //搜索表单提交
    $("#search-btn").on("click", function () {

        insuranceEcharts.getDataJson2DrawLine($, {name: '总消费（元）', cell: '元'}, "consume-trend", "/admin/dashboard/summary/getConsumeTrend");
        insuranceEcharts.getDataJson2DrawTransformSummary($, "transform-summary", "/admin/dashboard/summary/getTransformSummary");
        insuranceEcharts.getDataJson2DrawLine($, {name: '异常数量（个）', cell: '个'}, "abnormal-count", "/admin/dashboard/summary/getAbnormalCount");
        insuranceEcharts.getDataJson2DrawLine($, {name: '退保保费（元）', cell: '元'}, "repellent-amount", "/admin/dashboard/summary/getRepellentAmount");

        insuranceEcharts.getDataJson2DrawTransformCycle($, "transform-cycle", "/admin/dashboard/summary/getTransformCycle");
        insuranceEcharts.getDataJson2DrawTransformRate($, "transform-rate", "/admin/dashboard/summary/getTransformRate");
        insuranceEcharts.getDataJson2DrawCostSummary($, "cost-summary", "/admin/dashboard/summary/getCostSummary");

        insuranceEcharts.getDataJson2DrawBarRank($, {name: '转化成本（元）', cell: '元'}, "transform-cost-rank", "/admin/dashboard/summary/getTransformCostRank");
        insuranceEcharts.getDataJson2DrawBarRank($, {name: '承保件数（件）', cell: '件'}, "underwrite-count-rank", "/admin/dashboard/summary/getUnderwriteCountRank");
        insuranceEcharts.getDataJson2DrawBarRank($, {name: '保费金额（元）', cell: '元'}, "underwrite-amount-rank", "/admin/dashboard/summary/getUnderwriteAmountRank");
    });
    $("#search-btn").trigger("click");


    window.onresize = function () {
        for(var echartIdx in insuranceEcharts.echartsMap){
            insuranceEcharts.echartsMap[echartIdx].resize();
        }
    }

    exports('summary', {});
});

var insuranceEcharts = {
    echartsMap: {},

    // 获取后台JSON数据画折线图
    getDataJson2DrawLine: function ($, cellJson, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.contentList.length <= 0){
                    insuranceEcharts.drawLineEchart(selectedPrefix + "-echart", ["","","","","","",""], ["0","0","0","0","0","0","0"]);
                    return;
                }
                insuranceEcharts.drawLineEchart(selectedPrefix + "-echart", cellJson, response.rtnData.titleList, response.rtnData.contentList);
                if(response.rtnData.hasOwnProperty("consumeTotalAmount")){
                    $("#consumeTotalAmount").val(response.rtnData.consumeTotalAmount);
                }else if(response.rtnData.hasOwnProperty("abnormalTotalCount")){
                    $("#abnormalTotalCount").val(response.rtnData.abnormalTotalCount);
                }else if(response.rtnData.hasOwnProperty("repellentTotalAmount")){
                    $("#repellentTotalAmount").val(response.rtnData.repellentTotalAmount);
                }
            }
        });
    },
    getDataJson2DrawTransformSummary: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawTransformSummaryEchart(selectedPrefix + "-echart",
                        ["0","0","0","0","0","0","0"],
                        ["0","0","0","0","0","0","0"],
                        ["0","0","0","0","0","0","0"]);
                    return;
                }
                insuranceEcharts.drawTransformSummaryEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList,
                    response.rtnData.reserveCountList,
                    response.rtnData.insuranceCountList,
                    response.rtnData.insuranceFeeList);
                if(response.rtnData.hasOwnProperty("reserveTotalCount")){
                    $("#reserveTotalCount").val(response.rtnData.reserveTotalCount);
                }
                if(response.rtnData.hasOwnProperty("underwriteTotalCount")){
                    $("#underwriteTotalCount").val(response.rtnData.underwriteTotalCount);
                }
                if(response.rtnData.hasOwnProperty("underwriteTotalAmount")){
                    $("#underwriteTotalAmount").val(response.rtnData.underwriteTotalAmount);
                }
            }
        });
    },
    getDataJson2DrawTransformCycle: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.contentList.length <= 0){
                    insuranceEcharts.drawTransformCycleEchart(selectedPrefix + "-echart",
                        ["1天", "2天", "3天", "4天", "5天", "6天", "7天-14天", "15天-30天", "31天-60天", "61天-90天", "90天以上"],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawTransformCycleEchart(selectedPrefix + "-echart", response.rtnData.titleList, response.rtnData.contentList);
                if(response.rtnData.hasOwnProperty("transformCycle")){
                    $("#transformCycle").val(response.rtnData.transformCycle);
                }
            }
        });
    },
    getDataJson2DrawTransformRate: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.contentList.length <= 0){
                    insuranceEcharts.drawTransformRateEchart(selectedPrefix + "-echart", [{value: 0, name: '完成率'}]);
                    return;
                }
                insuranceEcharts.drawTransformRateEchart(selectedPrefix + "-echart", response.rtnData.contentList);
            }
        });
    },
    getDataJson2DrawCostSummary: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawCostSummaryEchart(selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""],
                        [0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawCostSummaryEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList, response.rtnData.clickCostList, response.rtnData.transformCostList);
                if(response.rtnData.hasOwnProperty("transformCost")){
                    $("#transformCost").val(response.rtnData.transformCost);
                }
            }
        });
    },
    getDataJson2DrawBarRank: function ($, cellJson, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawBarRankEchart(selectedPrefix + "-echart", cellJson,
                        ["", "", "", "", "", "", ""], [0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawBarRankEchart(selectedPrefix + "-echart", cellJson,
                    response.rtnData.titleList, response.rtnData.contentList);
            }
        });
    },

    // echarts 画图方法
    drawLineEchart: function (selectedId, cellJson, xData, data) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            xAxis: {
                show: true,
                type: 'category',
                splitLine:{
                    show:false
                },
                axisLabel: {
                    interval: 0,
                    rotate: "45"
                },
                data: xData
            },
            yAxis: {
                show: true,
                name: cellJson.name,
                splitLine:{
                    show:false
                },
                axisLabel: {
                    formatter: '{value}' + cellJson.cell
                }
            },
            series: [{
                type: 'line',
                data: data
            }]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawTransformSummaryEchart: function (selectedId, dateList, reserveCountList, insuranceCountList, insuranceFeeList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        var colors = ['#5793f3', '#675bba', '#d14a61'];
        var echartOption =  {
            color: colors,

            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                left: '20%'
            },
            legend: {
                data:['预约数量','承保件数','总保费']
            },
            xAxis: [
                {
                    type: 'category',
                    axisLabel: {
                        interval: 0,
                        rotate: "45"
                    },
                    axisTick: {
                        alignWithLabel: true
                    },
                    data: dateList
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '预约数量（个）',
                    position: 'left',
                    splitLine:{
                        show:false
                    },
                    axisLine: {
                        lineStyle: {
                            color: colors[0]
                        }
                    },
                    axisLabel: {
                        formatter: '{value}个'
                    }
                },
                {
                    type: 'value',
                    name: '承保件数（件）',
                    position: 'left',
                    offset: 80,
                    splitLine:{
                        show:false
                    },
                    axisLine: {
                        lineStyle: {
                            color: colors[1]
                        }
                    },
                    axisLabel: {
                        formatter: '{value}件'
                    }
                },
                {
                    type: 'value',
                    name: '总保费（元）',
                    position: 'right',
                    splitLine:{
                        show:false
                    },
                    axisLine: {
                        lineStyle: {
                            color: colors[2]
                        }
                    },
                    axisLabel: {
                        formatter: '{value}元'
                    }
                }
            ],
            series: [
                {
                    name: '预约数量',
                    type: 'line',
                    data: reserveCountList
                },
                {
                    name: '承保件数',
                    type: 'line',
                    yAxisIndex: 1,
                    data: insuranceCountList
                },
                {
                    name: '总保费',
                    type: 'bar',
                    barWidth: '50%',
                    yAxisIndex: 2,
                    data: insuranceFeeList
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawTransformCycleEchart: function (selectedId, titleList, contentList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            xAxis: {
                type: 'value',
                splitLine:{
                    show:false
                },
            },
            yAxis: {
                type: 'category',
                splitLine:{
                    show:false
                },
                data: titleList
            },
            series: [
                {
                    type: 'bar',
                    barWidth: '50%',
                    data: contentList
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawTransformRateEchart: function (selectedId, contentList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            tooltip : {
                formatter: "{a} <br/>{b} : {c}‰"
            },
            series: [
                {
                    name: '总转化率',
                    type: 'gauge',
                    min: 0,
                    max: 10,
                    detail: {
                        fontSize: '14',
                        formatter: function (value) {
                            return value + '‰';
                        }
                    },
                    data: contentList
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawCostSummaryEchart: function (selectedId, titleList, clickCostList, transformCostList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                show: true,
                top: "10%",
                right: "10%",
                borderColor: "#c45455",//网格的边框颜色
                bottom: "20%" //
            },
            legend: {
                data:['点击成本','转化成本']
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
                    name: '点击成本（元）',
                    position: 'left',
                    splitLine:{
                        show:false
                    },
                    axisLabel: {
                        formatter: '{value}元'
                    }
                },
                {
                    type: 'value',
                    name: '转化成本（元）',
                    position: 'right',
                    splitLine:{
                        show:false
                    },
                    axisLabel: {
                        formatter: '{value}元'
                    }
                }
            ],
            series: [
                {
                    data: clickCostList,
                    barWidth: '35%',
                    yAxisIndex: 0,
                    barGap: '0',
                    type: 'bar',
                    name: '点击成本'
                },
                {
                    data: transformCostList,
                    barWidth: '35%',
                    yAxisIndex: 1,
                    barGap: '0',
                    type: 'bar',
                    name: '转化成本'
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawBarRankEchart: function (selectedId, cellJson, titleList, contentList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                show: true,//是否显示直角坐标系网格。[ default: false ]
                top: "10%",
                right: "0px",
                borderColor: "#c45455",//网格的边框颜色
                bottom: "30%" //
            },
            xAxis: {
                type: 'category',
                axisLabel: {//坐标轴刻度标签的相关设置。
                    interval: 0,
                    rotate: "45"
                },
                data: titleList
            },
            yAxis: {
                name: cellJson.name,
                type: 'value',
                splitLine:{
                    show:false
                },
                axisLabel: {
                    formatter: '{value}' + cellJson.cell
                }
            },
            series: [
                {
                    data: contentList,
                    barWidth: '50%',
                    type: 'bar'
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },

    drawPieEchart: function (selectedId, tableItemList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            tooltip : {
                trigger: 'item',
                formatter: "{b} <br/>消费: {c}<br/>占比: {d}%"
            },
            series : [
                {
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '50%'],
                    data: tableItemList,
                    label: {
                        show: false
                    },
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
    drawConsumeTrendTable: function ($, table, selectedId, tableItemList) {
        var tableHtml = "";
        if(!tableItemList || tableItemList.length <= 0){
            tableHtml += "<tr>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "</tr>";
        }
        for(var index in tableItemList){
            tableHtml += "<tr>";
            tableHtml += "<td>" + tableItemList[index].advertiseSeries + "</td>";
            tableHtml += "<td>" + tableItemList[index].keyword + "</td>";
            tableHtml += "<td>" + tableItemList[index].speedCost + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: tableItemList.length,
            page: false,
            height: 360
        });
    },
    drawTransformSummaryTable: function ($, table, selectedId, tableItemList) {
        if (tableItemList[0]) {
            var headHtml = "<tr>";
            if(tableItemList[0].hasOwnProperty("advertiseActive")){
                headHtml += "<th lay-data=\"{align: 'center', field: 'advertiseActive'}\">广告活动</th>";
            }
            if(tableItemList[0].hasOwnProperty("advertiseSeries")){
                headHtml += "<th lay-data=\"{align: 'center', field: 'advertiseSeries'}\">广告系列</th>";
            }
            if(tableItemList[0].hasOwnProperty("keyword")){
                headHtml += "<th lay-data=\"{align: 'center', field: 'keyword'}\">关键词</th>";
            }
            headHtml += "<th lay-data=\"{align: 'center', field: 'reserveCount'}\">预约数量（个）</th>";
            headHtml += "<th lay-data=\"{align: 'center', field: 'underwriteCount'}\">承保件数（件）</th>";
            headHtml += "<th lay-data=\"{align: 'center', field: 'underwriteAmount', sort: true}\">总保费（元）</th>";
            headHtml += "</tr>";
            $("#" + selectedId + "-table-head").html(headHtml);
        }

        var tableHtml = "";
        for(var index in tableItemList){
            tableHtml += "<tr>";
            if(tableItemList[index].hasOwnProperty("advertiseActive")){
                tableHtml += "<td>" + tableItemList[index].advertiseActive + "</td>";
            }
            if(tableItemList[index].hasOwnProperty("advertiseSeries")){
                tableHtml += "<td>" + tableItemList[index].advertiseSeries + "</td>";
            }
            if(tableItemList[index].hasOwnProperty("keyword")){
                tableHtml += "<td>" + tableItemList[index].keyword + "</td>";
            }
            tableHtml += "<td>" + tableItemList[index].reserveCount + "</td>";
            tableHtml += "<td>" + tableItemList[index].underwriteCount + "</td>";
            tableHtml += "<td>" + tableItemList[index].underwriteAmount + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: tableItemList.length,
            page: false,
            height: 360
        });
    }
};