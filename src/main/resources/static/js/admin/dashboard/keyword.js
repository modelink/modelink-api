layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form;
    var laydate = layui.laydate;

    //时间选择器
    laydate.render({
        elem: '#chooseDate',
        range: true
    });

    insuranceEcharts.echartsMap["click-count-echart"] = echarts.init($("#click-count-echart")[0]);
    insuranceEcharts.echartsMap["reserve-click-echart"] = echarts.init($("#reserve-click-echart")[0]);
    insuranceEcharts.echartsMap["underwrite-amount-echart"] = echarts.init($("#underwrite-amount-echart")[0]);

    insuranceEcharts.echartsMap["click-rate-cost-echart"] = echarts.init($("#click-rate-cost-echart")[0]);
    insuranceEcharts.echartsMap["click-rate-cost-echart"].on("click", function (param) {
        var selectedPrefix = "click-rate-cost-keyword";
        var advertiseActive = param.name;
        $.ajax({
            url: "/admin/dashboard/keyword/getClickRateAndCostByKeyword",
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: advertiseActive
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawClickRateAndCostEchart(selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""],
                        [0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawClickRateAndCostEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList, response.rtnData.clickRateList, response.rtnData.clickCostList);
            }
        });
    });
    insuranceEcharts.echartsMap["click-rate-cost-keyword-echart"] = echarts.init($("#click-rate-cost-keyword-echart")[0]);

    insuranceEcharts.echartsMap["underwrite-summary-keyword-echart"] = echarts.init($("#underwrite-summary-keyword-echart")[0]);
    insuranceEcharts.echartsMap["transform-summary-keyword-echart"] = echarts.init($("#transform-summary-keyword-echart")[0]);

    insuranceEcharts.echartsMap["transform-rate-cycle-echart"] = echarts.init($("#transform-rate-cycle-echart")[0]);



    //搜索表单提交
    $("#search-btn").on("click", function () {

        insuranceEcharts.getDataJson2WordCloudsMap($, "click-count", "/admin/dashboard/keyword/getClickCount");
        insuranceEcharts.getDataJson2WordCloudsMap($, "reserve-click", "/admin/dashboard/keyword/getReserveCount");
        insuranceEcharts.getDataJson2WordCloudsMap($, "underwrite-amount", "/admin/dashboard/keyword/getUnderwriteAmount");

        insuranceEcharts.getDataJson2DrawClickRateAndCost($, "click-rate-cost", "/admin/dashboard/keyword/getClickRateAndCost");
        insuranceEcharts.getDataJson2DrawClickRateAndCost($, "click-rate-cost-keyword", "/admin/dashboard/keyword/getClickRateAndCostByKeyword");

        insuranceEcharts.getDataJson2DrawUnderwriteSummary($, "underwrite-summary-keyword", "/admin/dashboard/keyword/getUnderwriteSummaryByKeyword");
        insuranceEcharts.getDataJson2DrawCostSummary($, "transform-summary-keyword", "/admin/dashboard/keyword/getCostSummaryByKeyword");

        insuranceEcharts.getDataJson2DrawTransformRateAndCycle($, "transform-rate-cycle", "/admin/dashboard/keyword/getTransformCycleAndRate");
    });
    $("#search-btn").trigger("click");


    window.onresize = function () {
        for(var echartIdx in insuranceEcharts.echartsMap){
            insuranceEcharts.echartsMap[echartIdx].resize();
        }
    }

    exports('keyword', {});
});

var insuranceEcharts = {
    echartsMap: {},

    // 获取后台JSON数据画折线图
    getDataJson2WordCloudsMap: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData){
                    return;
                }
                insuranceEcharts.drawWordCloudsEchart(selectedPrefix + "-echart", response.rtnData);
            }
        });
    },
    getDataJson2DrawClickRateAndCost: function ($, selectedPrefix, dataUrl) {
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
                    insuranceEcharts.drawClickRateAndCostEchart(selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""],
                        [0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawClickRateAndCostEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList, response.rtnData.clickRateList, response.rtnData.clickCostList);
            }
        });
    },
    getDataJson2DrawUnderwriteSummary: function ($, selectedPrefix, dataUrl) {
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
                    response.rtnData.underwriteCountList,
                    response.rtnData.underwriteAmountList);
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
            }
        });
    },
    getDataJson2DrawTransformRateAndCycle: function ($, selectedPrefix, dataUrl) {
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
                    insuranceEcharts.drawTransformRateAndCycleEchart(selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""],
                        [0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawTransformRateAndCycleEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList, response.rtnData.transformRateList, response.rtnData.transformCycleList);
            }
        });
    },

    // echarts 画图方法
    drawWordCloudsEchart: function (selectedId, wordCloudsData) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        var echartOption = {
            tooltip: {},
            series: [{
                type: 'wordCloud',
                gridSize: 20,
                sizeRange: [12, 50],
                rotationRange: [0, 0],
                shape: 'circle',
                textStyle: {
                    normal: {
                        color: function() {
                            var colors = ['#fda67e', '#81cacc', '#cca8ba', "#88cc81", "#82a0c5",
                                '#fddb7e', '#735ba1', '#bda29a', '#6e7074', '#546570', '#c4ccd3'];
                            return colors[parseInt(Math.random() * 10)];
                        }
                    },
                    emphasis: {
                        shadowBlur: 10,
                        shadowColor: '#333'
                    }
                },
                data: wordCloudsData
            }]
        };

        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawClickRateAndCostEchart: function (selectedId, titleList, clickRateList, clickCostList) {

        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        var echartOption = {
            grid: {
                show: true,
                top: "10%",
                right: "10%",
                borderColor: "#c45455",//网格的边框颜色
                bottom: "20%" //
            },
            legend: {
                data:['点击率','点击价格']
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
                    name: '点击价格',
                    position: 'left',
                    axisLabel: {
                        formatter: '{value}元'
                    }
                },
                {
                    type: 'value',
                    name: '点击率',
                    position: 'right',
                    axisLabel: {
                        formatter: '{value}%'
                    }
                }
            ],
            series: [
                {
                    data: clickCostList,
                    barWidth: '50%',
                    yAxisIndex: 0,
                    barGap: '0',
                    type: 'bar',
                    name: '点击价格'
                },
                {
                    data: clickRateList,
                    yAxisIndex: 1,
                    type: 'line',
                    name: '点击率'
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);


    },
    drawTransformSummaryEchart: function (selectedId, titleList, reserveCountList, insuranceCountList, insuranceFeeList) {
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
                bottom: '30%'
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
                    data: titleList
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
    drawCostSummaryEchart: function (selectedId, titleList, clickCostList, transformCostList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        var echartOption = {
            grid: {
                show: true,
                top: "10%",
                right: "10%",
                borderColor: "#c45455",//网格的边框颜色
                bottom: "30%" //
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
    drawTransformRateAndCycleEchart: function (selectedId, titleList, transformRateList, transformCycleList) {

        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        var echartOption = {
            grid: {
                show: true,
                top: "10%",
                right: "10%",
                borderColor: "#c45455",//网格的边框颜色
                bottom: "30%" //
            },
            legend: {
                data:['转化率','转化周期']
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
                    name: '转化周期',
                    position: 'left',
                    axisLabel: {
                        formatter: '{value}元'
                    }
                },
                {
                    type: 'value',
                    name: '转化率',
                    position: 'right',
                    axisLabel: {
                        formatter: '{value}%'
                    }
                }
            ],
            series: [
                {
                    data: transformCycleList,
                    barWidth: '50%',
                    yAxisIndex: 0,
                    barGap: '0',
                    type: 'bar',
                    name: '转化周期'
                },
                {
                    data: transformRateList,
                    yAxisIndex: 1,
                    type: 'line',
                    name: '转化率'
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);


    },
};