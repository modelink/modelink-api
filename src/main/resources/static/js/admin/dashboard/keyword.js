layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload', 'formSelects'], function (exports) {
    var $ = layui.jquery;
    var table = layui.table;
    var laydate = layui.laydate;
    var formSelects = layui.formSelects;

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
                    insuranceEcharts.drawClickRateAndCostEchart($, selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""],
                        [0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawClickRateAndCostEchart($, selectedPrefix + "-echart",
                    response.rtnData.titleList, response.rtnData.clickRateList, response.rtnData.clickCostList);
            }
        });
    });
    insuranceEcharts.echartsMap["click-rate-cost-keyword-echart"] = echarts.init($("#click-rate-cost-keyword-echart")[0]);

    insuranceEcharts.echartsMap["underwrite-summary-keyword-echart"] = echarts.init($("#underwrite-summary-keyword-echart")[0]);
    insuranceEcharts.echartsMap["transform-summary-keyword-echart"] = echarts.init($("#transform-summary-keyword-echart")[0]);

    insuranceEcharts.echartsMap["transform-rate-cycle-echart"] = echarts.init($("#transform-rate-cycle-echart")[0]);
    insuranceEcharts.echartsMap["reserve-underwrite-keyword-echart"] = echarts.init($("#reserve-underwrite-keyword-echart")[0]);


    //搜索表单提交
    $("#search-btn").on("click", function () {
        var platformName = $("#platformName").val();
        var advertiseActive = $("#advertiseActive").val();
        if(!platformName && advertiseActive){
            layer.open({
                type: 1,
                content: "请选择渠道归属"
            });
            return;
        }

        insuranceEcharts.getDataJson2WordCloudsMap($, "click-count", "/admin/dashboard/keyword/getClickCount");
        insuranceEcharts.getDataJson2WordCloudsMap($, "reserve-click", "/admin/dashboard/keyword/getReserveCount");
        insuranceEcharts.getDataJson2WordCloudsMap($, "underwrite-amount", "/admin/dashboard/keyword/getUnderwriteAmount");

        insuranceEcharts.getDataJson2DrawClickRateAndCost($, "click-rate-cost", "/admin/dashboard/keyword/getClickRateAndCost");
        insuranceEcharts.getDataJson2DrawClickRateAndCost($, "click-rate-cost-keyword", "/admin/dashboard/keyword/getClickRateAndCostByKeyword");

        insuranceEcharts.getDataJson2DrawUnderwriteSummary($, "underwrite-summary-keyword", "/admin/dashboard/keyword/getUnderwriteSummaryByKeyword");
        insuranceEcharts.getDataJson2DrawCostSummary($, "transform-summary-keyword", "/admin/dashboard/keyword/getCostSummaryByKeyword");

        insuranceEcharts.getDataJson2DrawTransformRateAndCycle2($, "transform-rate-cycle", "/admin/dashboard/keyword/getTransformCycleAndRate2");
        insuranceEcharts.getDataJson2DrawReserveUnderwriteKeyword($, "reserve-underwrite-keyword", "/admin/dashboard/keyword/getReserveUnderwriteKeyword");

        insuranceEcharts.getDataJson2DrawKeywordTableGrid($, table, "keyword-table-body", "/admin/dashboard/keyword/keywordTableGrid");
    });
    $("#search-btn").trigger("click");

    $("#transformRateCycle .layui-btn-group button").on("click", function (selected) {
        $("#transformRateCycle .layui-btn-group button").addClass("layui-btn-primary");
        $(this).removeClass("layui-btn-primary");

        insuranceEcharts.transformChooseItem = [];
        insuranceEcharts.transformChooseItem.push($(this).attr("data-value"));
        insuranceEcharts.getDataJson2DrawTransformRateAndCycle2($, "transform-rate-cycle", "/admin/dashboard/keyword/getTransformCycleAndRate2");
    });
    $("#reserveUnderwriteKeyword .layui-btn-group button").on("click", function (selected) {
        $("#reserveUnderwriteKeyword .layui-btn-group button").addClass("layui-btn-primary");
        $(this).removeClass("layui-btn-primary");

        insuranceEcharts.keywordChooseItem = [];
        insuranceEcharts.keywordChooseItem.push($(this).attr("data-value"));
        insuranceEcharts.getDataJson2DrawReserveUnderwriteKeyword($, "reserve-underwrite-keyword", "/admin/dashboard/keyword/getReserveUnderwriteKeyword");
    });


    window.onresize = function () {
        for(var echartIdx in insuranceEcharts.echartsMap){
            insuranceEcharts.echartsMap[echartIdx].resize();
        }
    }

    exports('keyword', {});
});

var insuranceEcharts = {

    echartsMap: {},
    transformChooseItem: ["transformRate"],
    keywordChooseItem: ["reserveKeyword"],

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
                    insuranceEcharts.drawClickRateAndCostEchart($, selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""],
                        [0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawClickRateAndCostEchart($, selectedPrefix + "-echart",
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
                    insuranceEcharts.drawTransformSummaryEchart($, selectedPrefix + "-echart",
                        ["0","0","0","0","0","0","0"],
                        ["0","0","0","0","0","0","0"],
                        ["0","0","0","0","0","0","0"]);
                    return;
                }
                insuranceEcharts.drawTransformSummaryEchart($, selectedPrefix + "-echart",
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
                    insuranceEcharts.drawCostSummaryEchart($, selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""],
                        [0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawCostSummaryEchart($, selectedPrefix + "-echart",
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
                    insuranceEcharts.drawTransformRateAndCycleEchart($, selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""],
                        [0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawTransformRateAndCycleEchart($, selectedPrefix + "-echart",
                    response.rtnData.titleList, response.rtnData.transformRateList, response.rtnData.transformCycleList);
            }
        });
    },
    getDataJson2DrawTransformRateAndCycle2: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val(),
                chooseItems: insuranceEcharts.transformChooseItem.join(",")
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawTransformRateAndCycleEchart2($, response.rtnData.cellJson, selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""],
                        [0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawTransformRateAndCycleEchart2($, response.rtnData.cellJson, selectedPrefix + "-echart",
                    response.rtnData.titleList, response.rtnData.contentList);
            }
        });
    },
    getDataJson2DrawReserveUnderwriteKeyword: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val(),
                chooseItems: insuranceEcharts.keywordChooseItem.join(",")
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawReserveUnderwriteKeywordEchart($, response.rtnData.cellJson, selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""],
                        [0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawReserveUnderwriteKeywordEchart($, response.rtnData.cellJson, selectedPrefix + "-echart",
                    response.rtnData.titleList, response.rtnData.contentList);
            }
        });
    },
    getDataJson2DrawKeywordTableGrid: function($, table, selectedPrefix, dataUrl) {
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
                    insuranceEcharts.drawKeywordTableGridTable($, table, selectedPrefix, []);
                    return;
                }
                insuranceEcharts.drawKeywordTableGridTable($, table, selectedPrefix, response.rtnData);
            },
            error: function () {
                insuranceEcharts.drawKeywordTableGridTable($, table, selectedPrefix, []);
            }
        });
    },

    // echarts 画图方法
    drawWordCloudsEchart: function (selectedId, wordCloudsData) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
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
    drawClickRateAndCostEchart: function ($, selectedId, titleList, clickRateList, clickCostList) {

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
                bottom: "30%",
                borderColor: "#c45455"  //网格的边框颜色
            },
            legend: {
                data:['点击率','点击价格']
            },
            xAxis: {
                triggerEvent: true,
                type: 'category',
                splitLine:{
                    show:false
                },
                axisLabel: {
                    interval: 0,
                    rotate: "45",
                    formatter: function(value) {
                        var result = value;
                        if(result.length > 8) {
                            result = result.substring(0, 7) + "…";
                        }
                        return result;
                    }
                },
                data: titleList
            },
            yAxis: [
                {
                    type: 'value',
                    name: '点击价格（元）',
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
                    name: '点击率（%）',
                    position: 'right',
                    splitLine:{
                        show:false
                    },
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
        extension($, selectedEchart);
    },
    drawTransformSummaryEchart: function ($, selectedId, titleList, reserveCountList, insuranceCountList, insuranceFeeList) {
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
                left: '20%',
                bottom: '30%'
            },
            legend: {
                data:['预约数量','承保件数','总保费']
            },
            xAxis: [
                {
                    triggerEvent: true,
                    type: 'category',
                    splitLine:{
                        show:false
                    },
                    axisLabel: {
                        interval: 0,
                        rotate: "45",
                        formatter: function(value) {
                            var result = value;
                            if(result.length > 8) {
                                result = result.substring(0, 7) + "…";
                            }
                            return result;
                        }
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
        extension($, selectedEchart);
    },
    drawCostSummaryEchart: function ($, selectedId, titleList, clickCostList, transformCostList) {
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
                bottom: "30%" //
            },
            legend: {
                data:['点击成本','转化成本']
            },
            xAxis: {
                triggerEvent: true,
                type: 'category',
                splitLine:{
                    show:false
                },
                axisLabel: {
                    interval: 0,
                    rotate: "45",
                    formatter: function(value) {
                        var result = value;
                        if(result.length > 8) {
                            result = result.substring(0, 7) + "…";
                        }
                        return result;
                    }
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
        extension($, selectedEchart);
    },
    drawTransformRateAndCycleEchart: function ($, selectedId, titleList, transformRateList, transformCycleList) {

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
                bottom: "30%" //
            },
            legend: {
                data:['转化率','转化周期']
            },
            xAxis: {
                triggerEvent: true,
                type: 'category',
                splitLine:{
                    show:false
                },
                axisLabel: {
                    interval: 0,
                    rotate: "45",
                    formatter: function(value) {
                        var result = value;
                        if(result.length > 8) {
                            result = result.substring(0, 7) + "…";
                        }
                        return result;
                    }
                },
                data: titleList
            },
            yAxis: [
                {
                    type: 'value',
                    name: '转化周期（天）',
                    position: 'left',
                    splitLine:{
                        show:false
                    },
                    axisLabel: {
                        formatter: '{value}天'
                    }
                },
                {
                    type: 'value',
                    name: '转化率（%）',
                    position: 'right',
                    splitLine:{
                        show:false
                    },
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
        extension($, selectedEchart);
    },
    drawTransformRateAndCycleEchart2: function ($, cellJson, selectedId, titleList, contentList) {
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
                bottom: "30%" //
            },
            xAxis: {
                triggerEvent: true,
                type: 'category',
                splitLine:{
                    show:false
                },
                axisLabel: {
                    interval: 0,
                    rotate: "45",
                    formatter: function(value) {
                        var result = value;
                        if(result.length > 8) {
                            result = result.substring(0, 7) + "…";
                        }
                        return result;
                    }
                },
                data: titleList
            },
            yAxis: [
                {
                    type: 'value',
                    name: cellJson.name,
                    position: 'left',
                    splitLine:{
                        show:false
                    },
                    axisLabel: {
                        formatter: '{value}' + cellJson.cell
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
        extension($, selectedEchart);
    },
    drawReserveUnderwriteKeywordEchart: function ($, cellJson, selectedId, titleList, contentList) {
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
                bottom: "30%" //
            },
            xAxis: {
                triggerEvent: true,
                type: 'category',
                splitLine:{
                    show:false
                },
                axisLabel: {
                    interval: 0,
                    rotate: "45",
                    formatter: function(value) {
                        var result = value;
                        if(result.length > 8) {
                            result = result.substring(0, 7) + "…";
                        }
                        return result;
                    }
                },
                data: titleList
            },
            yAxis: [
                {
                    type: 'value',
                    name: cellJson.name,
                    position: 'left',
                    splitLine:{
                        show:false
                    },
                    axisLabel: {
                        formatter: '{value}' + cellJson.cell
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
        extension($, selectedEchart);
    },
    drawKeywordTableGridTable: function ($, table, selectedId, gridDataList) {
        var tableHtml = "";
        if(!gridDataList || gridDataList.length <= 0){
            tableHtml += "<tr>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "</tr>";
        }
        for(var index in gridDataList){
            tableHtml += "<tr>";
            tableHtml += "<td>" + gridDataList[index].indexNo + "</td>";
            tableHtml += "<td>" + gridDataList[index].merchantName + "</td>";
            tableHtml += "<td>" + gridDataList[index].platformName + "</td>";
            tableHtml += "<td>" + gridDataList[index].advertiseActive + "</td>";
            tableHtml += "<td>" + gridDataList[index].keyword + "</td>";
            tableHtml += "<td>" + gridDataList[index].clickCount + "</td>";
            tableHtml += "<td>" + gridDataList[index].reserveCount + "</td>";
            tableHtml += "<td>" + gridDataList[index].underwriteCount + "</td>";
            tableHtml += "<td>" + gridDataList[index].underwriteAmount + "</td>";
            tableHtml += "<td>" + gridDataList[index].transformCost + "</td>";
            tableHtml += "<td>" + gridDataList[index].clickCost + "</td>";
            tableHtml += "<td>" + gridDataList[index].transformCycle + "</td>";
            tableHtml += "<td>" + gridDataList[index].transformRate + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId).html(tableHtml);

        table.init("keyword-table", {
            page: false,
            height: 300,
            limit: gridDataList.length
        });
    }
};

function extension($, mychart) {
    //判断是否创建过div框,如果创建过就不再创建了
    var id = document.getElementById("extension");
    if(!id) {
        var div = "<div id = 'extension' sytle=\"display:none\"></div>"
        $('html').append(div);
    }


    mychart.on('mouseover', function(params) {
        if(params.componentType == "xAxis") {
            $('#extension').css({
                "position": "absolute",
                "background-color" : "black",
                "color": "white",
                "font-family": "Arial",
                "font-size": "16px",
                "padding": "5px",
                "display": "inline"
            }).text(params.value);


            $("html").mousemove(function(event) {
                var xx = event.pageX - 30;
                var yy = event.pageY + 20;
                $('#extension').css('top', yy).css('left', xx);
            });
        }
    });


    mychart.on('mouseout', function(params) {
        if(params.componentType == "xAxis") {
            $('#extension').css('display', 'none');
        }
    });


};