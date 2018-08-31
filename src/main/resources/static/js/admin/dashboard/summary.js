layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form;
    var laydate = layui.laydate;

    //时间选择器
    laydate.render({
        elem: '#chooseDate',
        range: true
    });

    insuranceEcharts.echartsMap["consume-trend-echart"] = echarts.init($("#consume-trend-echart")[0]);
    insuranceEcharts.echartsMap["transform-summary-echart"] = echarts.init($("#transform-summary-echart")[0]);
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

        insuranceEcharts.getDataJson2DrawLine($, "consume-trend", "/admin/dashboard/summary/getConsumeTrend");
        insuranceEcharts.getDataJson2DrawTransformSummary($, "transform-summary", "/admin/dashboard/summary/getTransformSummary");
        insuranceEcharts.getDataJson2DrawLine($, "abnormal-count", "/admin/dashboard/summary/getAbnormalCount");
        insuranceEcharts.getDataJson2DrawLine($, "repellent-amount", "/admin/dashboard/summary/getRepellentAmount");

        insuranceEcharts.getDataJson2DrawTransformCycle($, "transform-cycle", "/admin/dashboard/summary/getTransformCycle");
        insuranceEcharts.getDataJson2DrawTransformRate($, "transform-rate", "/admin/dashboard/summary/getTransformRate");
        insuranceEcharts.getDataJson2DrawCostSummary($, "cost-summary", "/admin/dashboard/summary/getCostSummary");

        insuranceEcharts.getDataJson2DrawBarRank($, "transform-cost-rank", "/admin/dashboard/summary/getTransformCostRank");
        insuranceEcharts.getDataJson2DrawBarRank($, "underwrite-count-rank", "/admin/dashboard/summary/getUnderwriteCountRank");
        insuranceEcharts.getDataJson2DrawBarRank($, "underwrite-amount-rank", "/admin/dashboard/summary/getUnderwriteAmountRank");
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
    getDataJson2DrawLine: function ($, selectedPrefix, dataUrl) {
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
                insuranceEcharts.drawLineEchart(selectedPrefix + "-echart", response.rtnData.titleList, response.rtnData.contentList);
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
    getDataJson2DrawBarRank: function ($, selectedPrefix, dataUrl) {
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
                    insuranceEcharts.drawBarRankEchart(selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""], [0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawBarRankEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList, response.rtnData.contentList);
            }
        });
    },

    // echarts 画图方法
    drawLineEchart: function (selectedId, xData, data) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            tooltip: {
                show: true
            },
            xAxis: {
                show: true,
                type: 'category',
                axisLabel: {
                    interval: 0,
                    rotate: "45"
                },
                data: xData
            },
            yAxis: {
                show: true
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
                    type: 'cross'
                }
            },
            grid: {
                left: '20%'
            },
            legend: {
                data:['预约数','承保件数','保费']
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
                    name: '预约数',
                    position: 'left',
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
                    name: '承保件数',
                    position: 'left',
                    offset: 80,
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
                    name: '保费',
                    position: 'right',
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
                    name: '预约数',
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
                    name: '保费',
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
            xAxis: {
                type: 'value'
            },
            yAxis: {
                type: 'category',
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
                    name: '转化率',
                    type: 'gauge',
                    detail: {
                        fontSize: '14',
                        formatter: function (value) {
                            value = value / 10;
                            value.toFixed(2);
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
            yAxis: [{
                type: 'value',
                name: '点击成本',
                position: 'left',
                axisLabel: {
                    formatter: '{value}元'
                }
            },
                {
                    type: 'value',
                    name: '转化成本',
                    position: 'right',
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
    drawBarRankEchart: function (selectedId, titleList, contentList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
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
                type: 'value'
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
    }
};