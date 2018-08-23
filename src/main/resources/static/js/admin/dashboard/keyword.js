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


    //搜索表单提交
    $("#search-btn").on("click", function () {

        insuranceEcharts.getDataJson2WordCloudsMap($, "click-count", "/admin/dashboard/keyword/getClickCount");
        insuranceEcharts.getDataJson2WordCloudsMap($, "reserve-click", "/admin/dashboard/keyword/getReserveCount");
        insuranceEcharts.getDataJson2WordCloudsMap($, "underwrite-amount", "/admin/dashboard/keyword/getUnderwriteAmount");

        insuranceEcharts.getDataJson2DrawClickRateAndCost($, "click-rate-cost", "/admin/dashboard/keyword/getClickRateAndCost");
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
                        formatter: '{value}'
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
};