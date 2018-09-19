layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload'], function (exports) {
    var $ = layui.jquery;
    var table = layui.table;
    var laydate = layui.laydate;

    //时间选择器
    laydate.render({
        elem: '#chooseDate',
        range: true
    });

    insuranceEcharts.echartsMap["estimate-result-echart"] = echarts.init($("#estimate-result-echart")[0]);
    insuranceEcharts.echartsMap["estimate-transform-echart"] = echarts.init($("#estimate-transform-echart")[0]);
    insuranceEcharts.echartsMap["estimate-browse-echart"] = echarts.init($("#estimate-browse-echart")[0]);

    insuranceEcharts.echartsMap["estimate-keyword-echart"] = echarts.init($("#estimate-keyword-echart")[0]);
    insuranceEcharts.echartsMap["estimate-area-echart"] = echarts.init($("#estimate-area-echart")[0]);
    insuranceEcharts.echartsMap["estimate-area-echart"].on("click", function (param) {
        var tableHtml = '<div id="estimate-city-echart" style="height: 500px;"></div>';
        layer.open({
            type: 1,
            content: tableHtml,
            area: ['600px', '600px']
        });

        $.ajax({
            url: "/admin/dashboard/estimate/getEstimateArea",
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val(),
                source: insuranceEcharts.areaSelected,
                provinceName: param.name
            },
            success: function (response) {
                insuranceEcharts.echartsMap["estimate-city-echart"] = echarts.init($("#estimate-city-echart")[0]);
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawBarEchart("estimate-city-echart",
                        ["", "", "", "", "", "", ""],
                        ["0","0","0","0","0","0","0"]);
                    return;
                }
                insuranceEcharts.drawBarEchart("estimate-city-echart",
                    response.rtnData.titleList,
                    response.rtnData.contentList);
            }
        })
    });
    insuranceEcharts.echartsMap["estimate-client-echart"] = echarts.init($("#estimate-client-echart")[0]);

    insuranceEcharts.echartsMap["age-gender-echart"] = echarts.init($("#age-gender-echart")[0]);
    insuranceEcharts.echartsMap["transform-cycle-echart"] = echarts.init($("#transform-cycle-echart")[0]);

    $("#estimate-keyword .layui-btn-group button").on("click", function () {
        $("#estimate-keyword .layui-btn-group button").addClass("layui-btn-primary");
        $(this).removeClass("layui-btn-primary");
        insuranceEcharts.keywordSelected = $(this).attr("data-value");
        insuranceEcharts.getDataJson2EstimateKeyword($, "estimate-keyword", "/admin/dashboard/estimate/getEstimateKeyword");
    });
    $("#estimate-area .layui-btn-group button").on("click", function () {
        $("#estimate-area .layui-btn-group button").addClass("layui-btn-primary");
        $(this).removeClass("layui-btn-primary");
        insuranceEcharts.areaSelected = $(this).attr("data-value");
        insuranceEcharts.getDataJson2EstimateArea($, "estimate-area", "/admin/dashboard/estimate/getEstimateArea");
    });
    $("#estimate-client .layui-btn-group button").on("click", function () {
        $("#estimate-client .layui-btn-group button").addClass("layui-btn-primary");
        $(this).removeClass("layui-btn-primary");
        insuranceEcharts.clientSelected = $(this).attr("data-value");
        insuranceEcharts.getDataJson2EstimateClient($, "estimate-client", "/admin/dashboard/estimate/getEstimateClient");
    });

    //搜索表单提交
    $("#search-btn").on("click", function () {

        insuranceEcharts.getDataJson2EstimateResult($, "estimate-result", "/admin/dashboard/estimate/getEstimateResult");
        insuranceEcharts.getDataJson2EstimateTransform($, "estimate-transform", "/admin/dashboard/estimate/getEstimateTransform");
        insuranceEcharts.getDataJson2EstimateBrowse($, "estimate-browse", "/admin/dashboard/estimate/getEstimateBrowse");

        insuranceEcharts.getDataJson2EstimateKeyword($, "estimate-keyword", "/admin/dashboard/estimate/getEstimateKeyword");
        insuranceEcharts.getDataJson2EstimateArea($, "estimate-area", "/admin/dashboard/estimate/getEstimateArea");
        insuranceEcharts.getDataJson2EstimateClient($, "estimate-client", "/admin/dashboard/estimate/getEstimateClient");

        insuranceEcharts.getDataJson2DrawAgeBar($, "age-gender", "/admin/dashboard/estimate/getGenderAge");
        insuranceEcharts.getDataJson2DrawTransformCycle($, "transform-cycle", "/admin/dashboard/estimate/getTransformCycle");

    });
    $("#search-btn").trigger("click");


    window.onresize = function () {
        for(var echartIdx in insuranceEcharts.echartsMap){
            insuranceEcharts.echartsMap[echartIdx].resize();
        }
    }

    exports('estimate', {});
});

var insuranceEcharts = {
    echartsMap: {},
    keywordSelected: "estimate",
    areaSelected: "estimate",
    clientSelected: "browser",

    // 获取后台JSON数据画折线图
    getDataJson2EstimateResult: function ($, selectedPrefix, dataUrl) {
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
                    return;
                }
                insuranceEcharts.drawEstimateResultEchart(selectedPrefix + "-echart", response.rtnData.titleList,
                    response.rtnData.transformCountList, response.rtnData.underwriteCountList, response.rtnData.underwriteAmountList);
            }
        });
    },
    getDataJson2EstimateTransform: function ($, selectedPrefix, dataUrl) {
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
                    insuranceEcharts.drawEstimateTransformEchart(selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""],
                        [0, 0, 0, 0, 0, 0, 0],
                        [0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawEstimateTransformEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList, response.rtnData.transformCostList, response.rtnData.transformAmountList);
            }
        });
    },
    getDataJson2EstimateBrowse: function ($, selectedPrefix, dataUrl) {
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
                    insuranceEcharts.drawEstimateBrowseEchart(selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""],
                        ["0","0","0","0","0","0","0"],
                        ["0","0","0","0","0","0","0"],
                        ["0","0","0","0","0","0","0"]);
                    return;
                }
                insuranceEcharts.drawEstimateBrowseEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList,
                    response.rtnData.arriveRateList,
                    response.rtnData.againRateList,
                    response.rtnData.clickRateList);
            }
        });
    },
    // 承保-测保对比
    getDataJson2EstimateKeyword: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val(),
                source: insuranceEcharts.keywordSelected
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawBarEchart(selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""],
                        ["0","0","0","0","0","0","0"]);
                    return;
                }
                insuranceEcharts.drawBarEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList,
                    response.rtnData.contentList);
            }
        });
    },
    getDataJson2EstimateArea: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val(),
                source: insuranceEcharts.areaSelected
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawBarEchart(selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""],
                        ["0","0","0","0","0","0","0"]);
                    return;
                }
                insuranceEcharts.drawBarEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList,
                    response.rtnData.contentList);
            }
        });
    },
    getDataJson2EstimateClient: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val(),
                source: insuranceEcharts.clientSelected
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawBarEchart(selectedPrefix + "-echart",
                        ["", "", "", "", "", "", ""],
                        ["0","0","0","0","0","0","0"]);
                    return;
                }
                insuranceEcharts.drawBarEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList,
                    response.rtnData.contentList);
            }
        });
    },
    // 获取后台JSON数据画年龄分布图
    getDataJson2DrawAgeBar: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val(),
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData){
                    insuranceEcharts.drawAgeBarEchart(selectedPrefix + "-echart",
                        ['0-5', '5-18', '18-25', '25-30', '30-35', '35-40', '40-50', '50-55', '55以上'],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawAgeBarEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList,
                    response.rtnData.manList,
                    response.rtnData.womanList,
                    response.rtnData.maxAmount);
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
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData){
                    return;
                }
                insuranceEcharts.drawTransformCycleEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList,
                    response.rtnData.labelList,
                    response.rtnData.contentList);
            }
        });
    },

    // echarts 画图方法
    drawEstimateResultEchart: function (selectedId, titleList, transformCountList, underwriteCountList, underwriteAmountList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        var colors = ['#5793f3', '#675bba', '#d14a61'];
        selectedEchart.clear();
        var echartOption =  {
            color: colors,

            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                left: '120',
                bottom: '20%'
            },
            legend: {
                data:['测保转化数','承保件数','保费']
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
                    name: '测保转化数',
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
                    name: '测保转化数',
                    type: 'line',
                    data: transformCountList
                },
                {
                    name: '承保件数',
                    type: 'line',
                    yAxisIndex: 1,
                    data: underwriteCountList
                },
                {
                    name: '保费',
                    type: 'bar',
                    barWidth: '50%',
                    yAxisIndex: 2,
                    data: underwriteAmountList
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawEstimateTransformEchart: function (selectedId, titleList, transformCostList, transformAmountList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        var colors = ['#5793f3', '#d14a61'];
        var echartOption =  {
            color: colors,

            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                bottom: '20%'
            },
            legend: {
                data:['直接转化成本', '总花费']
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
                    name: '直接转化成本',
                    position: 'left',
                    axisLine: {
                        lineStyle: {
                            color: colors[0]
                        }
                    },
                    axisLabel: {
                        formatter: '{value}元'
                    }
                },
                {
                    type: 'value',
                    name: '总花费',
                    position: 'right',
                    axisLine: {
                        lineStyle: {
                            color: colors[1]
                        }
                    },
                    axisLabel: {
                        formatter: '{value}元'
                    }
                }
            ],
            series: [
                {
                    name: '直接转化成本',
                    type: 'line',
                    yAxisIndex: 0,
                    data: transformCostList
                },
                {
                    name: '总花费',
                    type: 'bar',
                    barWidth: '50%',
                    yAxisIndex: 1,
                    data: transformAmountList
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawEstimateBrowseEchart: function (selectedId, titleList, arriveRateList, againRateList, clickRateList) {
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
                data:['到达率','二跳率','点击率']
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
                    name: '百分比',
                    position: 'left',
                    axisLabel: {
                        formatter: '{value}%'
                    }
                }
            ],
            series: [
                {
                    data: arriveRateList,
                    type: 'line',
                    name: '到达率'
                },
                {
                    data: againRateList,
                    type: 'line',
                    name: '二跳率'
                }
                ,
                {
                    data: clickRateList,
                    type: 'line',
                    name: '点击率'
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    
    drawBarEchart: function (selectedId, titleList, contentList) {
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
                left: "20%",
                right: "10%",
                borderColor: "#c45455",//网格的边框颜色
                bottom: "10%" //
            },
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
    drawAgeBarEchart: function (selectedId, titleList, manList, womanList, maxAmount) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        selectedEchart.clear();
        var echartOption = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            legend: {
                data: ["男性", "女性"]
            },
            grid: {
                top: '10%',
                bottom: '10%'
            },
            xAxis: [
                {
                    max: maxAmount,
                    min: -maxAmount,
                    type: 'value',
                    name: '总保费（元）',
                    show: true,
                    axisLabel: {
                        formatter: function (data) {
                            return Math.abs(data) + "元";
                        }
                    }
                }
            ],
            yAxis: [
                {
                    type: 'category',
                    show: true,
                    axisTick: {show: false},
                    data: titleList
                }
            ],
            series: [
                {
                    name: '男性',
                    type: 'bar',
                    stack: '总量',
                    label: {
                        normal: {
                            show: false,
                            position: 'left',
                            formatter: function (v) {
                                return Math.abs(v.data)
                            }
                        }
                    },
                    data: manList
                },
                {
                    name: '女性',
                    type: 'bar',
                    stack: '总量',
                    label: {
                        normal: {
                            show: false,
                            position: 'right'
                        }
                    },
                    data: womanList,
                    formatter: function (v) {
                        return Math.abs(v)
                    }
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawTransformCycleEchart: function (selectedId, titleList, labelList, contentList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        selectedEchart.clear();

        var series;
        var seriesList = [];
        for (var index in titleList) {
            series = {};
            series.name = titleList[index];
            series.type = 'bar';
            series.stack = '总量';
            series.data = contentList[index];
            seriesList.push(series);
        }
        var echartOption = {
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data: titleList
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis:  {
                type: 'value'
            },
            yAxis: {
                type: 'category',
                data: labelList
            },
            series: seriesList
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    }
};