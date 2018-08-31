layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload'], function (exports) {
    var $ = layui.jquery;
    var table = layui.table;
    var laydate = layui.laydate;

    //时间选择器
    laydate.render({
        elem: '#chooseDate',
        range: true
    });

    insuranceEcharts.echartsMap["reserve-count-echart"] = echarts.init($("#reserve-count-echart")[0]);

    //搜索表单提交
    $("#search-btn").on("click", function () {
        insuranceEcharts.getDataJson($, table, "reserve-count", "/admin/dashboard/client/getReserveSummary");
    });
    $("#search-btn").trigger("click");

    $(".layui-label .layui-btn-group button").on("click", function (selected) {
        $(".layui-label .layui-btn-group button").addClass("layui-btn-primary");
        $(this).removeClass("layui-btn-primary");

        insuranceEcharts.getDataJson($, table, "reserve-count", "/admin/dashboard/client/getReserveSummary");

    });
    window.onresize = function () {
        for(var echartIdx in insuranceEcharts.echartsMap){
            insuranceEcharts.echartsMap[echartIdx].resize();
        }
    };

    exports('client', {});
});

var insuranceEcharts = {
    chooseItem: "browser",
    echartsMap: {},

    getDataJson: function ($, table, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                chooseItems: insuranceEcharts.chooseItem,
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    return;
                }
                insuranceEcharts.drawTable($, table, selectedPrefix, response.rtnData.tableItemList);
                insuranceEcharts.drawEchart(selectedPrefix + "-echart", response.rtnData.titleList, response.rtnData.contentList);
            }
        });
    },

    drawEchart: function (selectedId, titleList, contentList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                type: 'scroll',
                orient: 'vertical',
                top: '20%',
                right: '20%',
                data: titleList,
                selected: true
            },
            series : [
                {
                    name: '地区',
                    type: 'pie',
                    radius : '55%',
                    center: ['40%', '50%'],
                    data: contentList,
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
    drawTable: function ($, table, selectedId, tableItemList) {
        var tableHtml = "";
        tableHtml += "<tr>";
        if(insuranceEcharts.chooseItem == "browser"){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'date'}\">浏览器</th>";
        }else if(insuranceEcharts.chooseItem == "os"){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'date'}\">操作系统</th>";
        }else if(insuranceEcharts.chooseItem == "resolutionRatio"){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'date'}\">屏幕分辨率</th>";
        }else if(insuranceEcharts.chooseItem == "deviceType"){
            tableHtml += "<th lay-data=\"{align: 'center', field: 'date'}\">设备类别</th>";
        }
        tableHtml += "<th lay-data=\"{align: 'center', field: 'reserveCount'}\">预约数量</th>";
        tableHtml += "<th lay-data=\"{align: 'center', field: 'proportion'}\">百分比</th>";
        tableHtml += "</tr>";
        $("#" + selectedId + "-table-head").html(tableHtml);

        tableHtml = "";
        if(!tableItemList || tableItemList.length <= 0){
            tableHtml += "<tr>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "</tr>";
        }
        for(var index in tableItemList){
            tableHtml += "<tr>";
            tableHtml += "<td>" + tableItemList[index].chooseItem + "</td>";
            tableHtml += "<td>" + tableItemList[index].reserveCount + "</td>";
            tableHtml += "<td>" + tableItemList[index].proportion + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: 10,
            page: true
        });
    }
};