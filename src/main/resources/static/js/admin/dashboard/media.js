layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload', 'formSelects'], function (exports) {
    var $ = layui.jquery;
    var table = layui.table;
    var laydate = layui.laydate;
    var formSelects = layui.formSelects;

    var windowHeight = $(window).height();
    var formHeight = $(".layui-form").height();
    $(".content-fixed").height(windowHeight - formHeight - 100);

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
        range: true,
        elem: '#chooseDate'
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
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData){
                    insuranceEcharts.drawLineEchart(selectedPrefix + "-echart", [0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawLineTable($, table, selectedPrefix, response.rtnData.tableTitleList, response.rtnData.tableItemList);
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
            { name: '转化成本（总花费/承保件数）', max: 100}
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
            // tableHtml += "<td>" + tableItem.transformRate + "</td>";
            tableHtml += "</tr>";
        }

        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: tableItemList.length,
            page: false,
            height: 400
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
                bottom: '20%',
                right: '30%',
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
                axisLabel: {
                    interval: 0,
                    rotate: "45"
                },
                data: response.tableTitleList
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    data: response.underwriteAmountList,
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
                    data: response.operateCountList,
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
                    data: response.keywordCountList,
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
                    data: response.wordIdeaCountList,
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
                    data: response.imageIdeaCountList,
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
                    data: response.flowIdeaCountList,
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
                    data: response.flowPeopleCountList,
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
    drawLineTable: function ($, table, selectedId, tableTitleList, tableItemList) {
        var tableItem;
        var tableHtml;

        tableHtml += "<tr><th lay-data=\"{align: 'center'}\" colspan=\"" + (tableTitleList.length + 1) + "\">媒体渠道策略调整分析</th></tr>";
        tableHtml += "<tr><th lay-data=\"{align: 'center', field: 'operate', width: 150}\">日期</th>";
        for(var idx in tableTitleList){
            tableHtml += "<th lay-data=\"{align: 'center', field: '" + tableTitleList[idx] + "', width: 100}\">" + tableTitleList[idx] + "</th>";
        }
        tableHtml += "</tr>";
        $("#" + selectedId + "-table-head").html(tableHtml);
        // 月份
        tableHtml = "";
        for(var index in tableItemList){
            tableItem = tableItemList[index];
            if(tableItem.keyword){
                tableHtml += "<tr style='background-color: #eb7350'>";
            }else{
                tableHtml += "<tr>";
            }
            tableHtml += "<td>" + tableItem.operate + "</td>";
            for(var idx in tableTitleList){
                tableHtml += "<td>" + tableItem[tableTitleList[idx]] + "</td>";
            }
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: 30,
            height: 300
        });
    }
};