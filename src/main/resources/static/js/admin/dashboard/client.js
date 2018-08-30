layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload'], function (exports) {
    var $ = layui.jquery;
    var table = layui.table;
    var laydate = layui.laydate;

    insuranceEcharts.dateType = "hour";
    //时间选择器
    laydate.render({
        elem: '#chooseDate',
        range: true
    });

    insuranceEcharts.echartsMap["reserve-count-echart"] = echarts.init($("#reserve-count-echart")[0]);

    //搜索表单提交
    $("#search-btn").on("click", function () {
        insuranceEcharts.getDataJson2DrawHour($, table, "reserve-count", "/admin/dashboard/client/getUnderwriteCount");
    });
    $("#search-btn").trigger("click");

    $(".layui-label .layui-btn-group button").on("click", function (selected) {
        $(".layui-label .layui-btn-group button").addClass("layui-btn-primary");
        $(this).removeClass("layui-btn-primary");
        insuranceEcharts.dateType = $(this).attr("data-value");

        insuranceEcharts.getDataJson2DrawHour($, table, "reserve-count", "/admin/dashboard/client/getUnderwriteCount");
    });
    window.onresize = function () {
        for(var echartIdx in insuranceEcharts.echartsMap){
            insuranceEcharts.echartsMap[echartIdx].resize();
        }
    }

    exports('client', {});
});

var insuranceEcharts = {
    dateType: '',
    echartsMap: {},

    getDataJson2DrawHour: function ($, table, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                dateType: insuranceEcharts.dateType,
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    return;
                }
                insuranceEcharts.drawArea2CountTable($, table, selectedPrefix, response.rtnData.contentList);
                insuranceEcharts.selectedEchart(selectedPrefix + "-echart", response.rtnData.titleList, response.rtnData.contentList);
            }
        });
    },

    drawHourEchart: function (selectedId, titleList, contentList) {
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
                    position: 'left',
                    axisLabel: {
                        formatter: '{value}'
                    }
                }
            ],
            series: [
                {
                    data: contentList,
                    barWidth: '50%',
                    yAxisIndex: 0,
                    barGap: '0',
                    type: 'bar',
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawArea2CountTable: function ($, table, selectedId, contentList) {
        var tableHtml = "";
        if(!contentList || contentList.length <= 0){
            tableHtml += "<tr>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "</tr>";
        }
        for(var index in contentList){
            tableHtml += "<tr>";
            tableHtml += "<td>" + contentList[index].name + "</td>";
            tableHtml += "<td>" + contentList[index].value + "</td>";
            tableHtml += "<td>" + contentList[index].proportion + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: 10,
            page: true
        });
    }
};