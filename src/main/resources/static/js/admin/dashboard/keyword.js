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


    //搜索表单提交
    $("#search-btn").on("click", function () {

        insuranceEcharts.getDataJson2DrawLine($, "consume-trend", "/admin/dashboard/summary/getConsumeTrend");
        insuranceEcharts.getDataJson2DrawTransformSummary($, "transform-summary", "/admin/dashboard/summary/getTransformSummary");
        insuranceEcharts.getDataJson2DrawLine($, "abnormal-count", "/admin/dashboard/summary/getAbnormalCount");
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
                            return 'rgb(' + [
                                Math.round(Math.random() * 160),
                                Math.round(Math.random() * 160),
                                Math.round(Math.random() * 160)
                            ].join(',') + ')';
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
};