layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload'], function (exports) {
    var $ = layui.jquery;
    var table = layui.table;
    var laydate = layui.laydate;

    //时间选择器
    laydate.render({
        range: true,
        elem: '#chooseDate',
        done: function(value, date, endDate){
            insuranceEcharts.getDataJson2DrawRadar($, table, "media-summary", "/admin/dashboard/media/getMediaSummary");
        }
    });
    //年份选择器
    laydate.render({
        range: true,
        type: 'year',
        elem: '#chooseYear',
        done: function(value, date, endDate){
            insuranceEcharts.getDataJson2DrawLine($, table, "media-tactics", "/admin/dashboard/media/getMediaTactics");
        }
    });

    insuranceEcharts.echartsMap["media-summary-echart"] = echarts.init($("#media-summary-echart")[0]);
    insuranceEcharts.echartsMap["media-tactics-echart"] = echarts.init($("#media-tactics-echart")[0]);
    //搜索表单提交
    $("#search-btn").on("click", function () {
        insuranceEcharts.getDataJson2DrawRadar($, table, "media-summary", "/admin/dashboard/media/getMediaSummary");
        insuranceEcharts.getDataJson2DrawLine($, table, "media-tactics", "/admin/dashboard/media/getMediaTactics");
    });
    $("#search-btn").trigger("click");


    window.onresize = function () {
        for(var echartIdx in insuranceEcharts.echartsMap){
            insuranceEcharts.echartsMap[echartIdx].resize();
        }
    }

    exports('media', {});
});

var insuranceEcharts = {
    echartsMap: {},

    // 获取后台JSON数据画图
    getDataJson2DrawRadar: function ($, table, selectedPrefix, dataUrl) {
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
                    insuranceEcharts.drawRadarEchart(selectedPrefix + "-echart", [0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawRadarEchart(selectedPrefix + "-echart", response.rtnData.contentList);
                insuranceEcharts.drawRadarTable($, table, selectedPrefix, response.rtnData.tableItemList);
            }
        });
    },
    getDataJson2DrawLine: function ($, table, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseYear").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData){
                    insuranceEcharts.drawLineEchart(selectedPrefix + "-echart", [0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawLineTable($, table, selectedPrefix, response.rtnData.tableItemList);
                insuranceEcharts.drawLineEchart(selectedPrefix + "-echart", response.rtnData);
            }
        });
        },

    drawRadarEchart: function (selectedId, contentList) {

        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var labelList = [
            { name: '点击率（点击量/曝光量）', max: 100},
            { name: '预约率（预约数量/点击量）', max: 100},
            { name: '承保率（承保件数/预约数量）', max: 100},
            { name: '转化周期（转化时间/承保件数）', max: 100},
            { name: '转化成本（总花费/承保件数）', max: 100},
            { name: '总转化率（承保件数/总花费）', max: 100}
        ];
        var echartOption = {
            tooltip: {
                formatter: function (param) {
                    var html = param.name + "<br/>";
                    for (var index in param.value) {
                        html += (labelList[index].name + ":" + param.value[index] + "%<br/>");
                    }
                    return html;
                }
            },
            radar: {
                name: {
                    textStyle: {
                        color: '#fff',
                        backgroundColor: '#999',
                        borderRadius: 3,
                        padding: [3, 5]
                    }
                },
                indicator: labelList
            },
            series: [{
                type: 'radar',
                data : [
                    {
                        name: '媒体渠道关键指标分析',
                        value : contentList,
                    }
                ]
            }]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawRadarTable: function ($, table, selectedId, tableItemList) {
        var tableItem;
        var tableHtml = "";

        // 月份
        for(var index in tableItemList){
            tableItem = tableItemList[index];

            tableHtml += "<tr>";
            tableHtml += "<td>" + tableItem.platformName + "</td>";
            tableHtml += "<td>" + tableItem.advertiseActive + "</td>";
            tableHtml += "<td>" + tableItem.clickRate + "</td>";
            tableHtml += "<td>" + tableItem.reserveRate + "</td>";
            tableHtml += "<td>" + tableItem.underwriteRate + "</td>";
            tableHtml += "<td>" + tableItem.transformCycle + "</td>";
            tableHtml += "<td>" + tableItem.transformCost + "</td>";
            tableHtml += "<td>" + tableItem.transformRate + "</td>";
            tableHtml += "</tr>";
        }

        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            height: 600
        });
    },
    drawLineEchart: function (selectedId, response) {

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
                top: '10%',
                bottom: '10%',
                right: '20%',
                left: '10%'
            },
            legend: {
                type: 'scroll',
                align: 'right',
                orient: 'vertical',
                x: 'right',
                y: 'center',
                right: '15',
                data: [
                    '保费',
                    '操作数（次）',
                    '关键词优化（次）',
                    '文字创意优化（次）',
                    '展示类图片创意优化（次）',
                    '信息流文字创意优化（次）',
                    '信息流人群优化（次）'
                ]
            },
            xAxis: {
                type: 'category',
                data: response.monthList
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    data: [120, 200, 150, 80, 70, 110, 130, 130, 130, 130, 130, 130],
                    name: '保费',
                    type: 'line',
                    symbol: 'circle',
                    smooth: '0.5',
                    lineStyle: {
                        normal: {
                            color: 'green',
                            type: 'solid'
                        }
                    }
                },
                {
                    data: [110, 220, 140, 81, 75, 100, 123, 130, 130, 130, 130, 130],
                    name: '操作数（次）',
                    type: 'line',
                    symbol: 'circle',
                    smooth: '0.5',
                    lineStyle: {
                        normal: {
                            color: 'red',
                            type: 'dotted'
                        }
                    }
                },
                {
                    data: [110, 220, 140, 81, 75, 100, 123, 130, 130, 130, 130, 130],
                    name: '关键词优化（次）',
                    type: 'line',
                    symbol: 'circle',
                    smooth: '0.5',
                    lineStyle: {
                        normal: {
                            type: 'dotted'
                        }
                    }
                },
                {
                    data: [110, 220, 140, 81, 75, 100, 123, 130, 130, 130, 130, 130],
                    name: '文字创意优化（次）',
                    type: 'line',
                    symbol: 'circle',
                    smooth: '0.5',
                    lineStyle: {
                        normal: {
                            type: 'dotted'
                        }
                    }
                },
                {
                    data: [110, 220, 140, 81, 75, 100, 123, 130, 130, 130, 130, 130],
                    name: '展示类图片创意优化（次）',
                    type: 'line',
                    symbol: 'circle',
                    smooth: '0.5',
                    lineStyle: {
                        normal: {
                            type: 'dotted'
                        }
                    }
                },
                {
                    data: [110, 220, 140, 81, 75, 100, 123, 130, 130, 130, 130, 130],
                    name: '信息流文字创意优化（次）',
                    type: 'line',
                    symbol: 'circle',
                    smooth: '0.5',
                    lineStyle: {
                        normal: {
                            type: 'dotted'
                        }
                    }
                },
                {
                    data: [110, 220, 140, 81, 75, 100, 123, 130, 130, 130, 130, 130],
                    name: '信息流人群优化（次）',
                    type: 'line',
                    symbol: 'circle',
                    smooth: '0.5',
                    lineStyle: {
                        normal: {
                            type: 'dotted'
                        }
                    }
                }

            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawLineTable: function ($, table, selectedId, tableItemList) {
        var tableItem;
        var tableHtml = "";

        // 月份
        for(var index in tableItemList){
            tableItem = tableItemList[index];
            if(tableItem.keyword){
                tableHtml += "<tr style='background-color: #eb7350'>";
            }else{
                tableHtml += "<tr>";
            }
            tableHtml += "<td>" + tableItem.operate + "</td>";
            tableHtml += "<td>" + tableItem.month1 + "</td>";
            tableHtml += "<td>" + tableItem.month2 + "</td>";
            tableHtml += "<td>" + tableItem.month3 + "</td>";
            tableHtml += "<td>" + tableItem.month4 + "</td>";
            tableHtml += "<td>" + tableItem.month5 + "</td>";
            tableHtml += "<td>" + tableItem.month6 + "</td>";
            tableHtml += "<td>" + tableItem.month7 + "</td>";
            tableHtml += "<td>" + tableItem.month8 + "</td>";
            tableHtml += "<td>" + tableItem.month9 + "</td>";
            tableHtml += "<td>" + tableItem.month10 + "</td>";
            tableHtml += "<td>" + tableItem.month11 + "</td>";
            tableHtml += "<td>" + tableItem.month12 + "</td>";
            tableHtml += "</tr>";
        }

        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: 30,
            height: 300
        });
    }
};