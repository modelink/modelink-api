layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload'], function (exports) {
    var $ = layui.jquery;
    var table = layui.table;
    var laydate = layui.laydate;

    //时间选择器
    laydate.render({
        elem: '#chooseDate',
        range: true
    });

    insuranceEcharts.echartsMap["underwrite-count-echart"] = echarts.init($("#underwrite-count-echart")[0]);
    insuranceEcharts.echartsMap["underwrite-amount-echart"] = echarts.init($("#underwrite-amount-echart")[0]);

    //搜索表单提交
    $("#search-btn").on("click", function () {
        insuranceEcharts.getDataJson2DrawArea2Count($, table, "underwrite-count", "/admin/dashboard/customer/getUnderwriteCount");
        insuranceEcharts.getDataJson2DrawAge2Amount($, table, "underwrite-amount", "/admin/dashboard/customer/getUnderwriteAmount");
        insuranceEcharts.getDataJson2DrawGender2Keyword($, table, "underwrite-keyword", "/admin/dashboard/customer/getUnderwriteKeyword");
        insuranceEcharts.getDataJson2DrawUnderwriteList($, table, "underwrite-list", "/admin/dashboard/customer/getUnderwriteList");
    });
    $("#search-btn").trigger("click");


    window.onresize = function () {
        for(var echartIdx in insuranceEcharts.echartsMap){
            insuranceEcharts.echartsMap[echartIdx].resize();
        }
    }

    exports('customer', {});
});

var insuranceEcharts = {
    echartsMap: {},

    getDataJson2DrawArea2Count: function ($, table, selectedPrefix, dataUrl) {
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
                    insuranceEcharts.drawArea2CountEchart(selectedPrefix + "-echart",
                        ["北京市", "上海市", "广东省", "山东省", "江苏省", "浙江省", "四川省", "重庆市", "河南省", "湖北省"],
                        [
                            {name: "北京市", value: 0},{name: "上海市", value: 0},{name: "广东省", value: 0},
                            {name: "山东省", value: 0},{name: "江苏省", value: 0},{name: "浙江省", value: 0},
                            {name: "四川省", value: 0},{name: "重庆市", value: 0},{name: "河南省", value: 0},
                            {name: "湖北省", value: 0}
                        ]);
                    return;
                }
                insuranceEcharts.drawArea2CountTable($, table, selectedPrefix, response.rtnData.contentList);
                insuranceEcharts.drawArea2CountEchart(selectedPrefix + "-echart", response.rtnData.titleList, response.rtnData.contentList);
            }
        });
    },
    getDataJson2DrawAge2Amount: function ($, table, selectedPrefix, dataUrl) {
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
                    insuranceEcharts.drawAge2AmountEchart(selectedPrefix + "-echart",
                        ["北京市", "上海市", "广东省", "山东省", "江苏省", "浙江省", "四川省", "重庆市", "河南省", "湖北省"],
                        [
                            {name: "北京市", value: 0},{name: "上海市", value: 0},{name: "广东省", value: 0},
                            {name: "山东省", value: 0},{name: "江苏省", value: 0},{name: "浙江省", value: 0},
                            {name: "四川省", value: 0},{name: "重庆市", value: 0},{name: "河南省", value: 0},
                            {name: "湖北省", value: 0}
                        ]);
                    return;
                }
                insuranceEcharts.drawAge2AmountTable($, table, selectedPrefix, response.rtnData.tableItemList);
                insuranceEcharts.drawAge2AmountEchart(selectedPrefix + "-echart", response.rtnData.titleList,
                    response.rtnData.manAmountList, response.rtnData.womanAmountList);
            }
        });
    },
    getDataJson2DrawGender2Keyword: function ($, table, selectedPrefix, dataUrl) {
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
                insuranceEcharts.drawGender2KeywordTable($, table, selectedPrefix, response.rtnData.tableItemList);
            }
        });
    },
    getDataJson2DrawUnderwriteList: function ($, table, selectedPrefix, dataUrl) {
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
                insuranceEcharts.drawUnderwriteListTable($, table, selectedPrefix, response.rtnData.tableItemList);
            }
        });
    },


    drawArea2CountEchart: function (selectedId, titleList, contentList) {
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
                data: titleList.slice(0, 10)
            },
            series : [
                {
                    name: '地区',
                    type: 'pie',
                    radius : '55%',
                    center: ['40%', '50%'],
                    data: contentList.slice(0, 10),
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
    },

    drawAge2AmountEchart: function (selectedId, titleList, manAmountList, womanAmountList) {
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
                data:['男','女']
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
                    name: '男',
                    position: 'left',
                    axisLabel: {
                        formatter: '{value}元'
                    }
                },
                {
                    type: 'value',
                    name: '女',
                    position: 'right',
                    axisLabel: {
                        formatter: '{value}元'
                    }
                }
            ],
            series: [
                {
                    data: manAmountList,
                    barWidth: '35%',
                    yAxisIndex: 0,
                    barGap: '0',
                    type: 'bar',
                    name: '男'
                },
                {
                    data: womanAmountList,
                    barWidth: '35%',
                    yAxisIndex: 1,
                    barGap: '0',
                    type: 'bar',
                    name: '女'
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawAge2AmountTable: function ($, table, selectedId, tableItemList) {
        var tableHtml = "";
        if(!tableItemList || tableItemList.length <= 0){
            tableHtml += "<tr>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "</tr>";
        }
        for(var index in tableItemList){
            tableHtml += "<tr>";
            tableHtml += "<td>" + tableItemList[index].agePart + "</td>";
            tableHtml += "<td>" + tableItemList[index].manCount + "</td>";
            tableHtml += "<td>" + tableItemList[index].manAmount + "</td>";
            tableHtml += "<td>" + tableItemList[index].womanCount + "</td>";
            tableHtml += "<td>" + tableItemList[index].womanAmount + "</td>";
            tableHtml += "<td>" + tableItemList[index].proportion + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: 10,
            page: true
        });
    },

    drawGender2KeywordTable: function ($, table, selectedId, tableItemList) {
        var tableHtml = "";
        if(!tableItemList || tableItemList.length <= 0){
            tableHtml += "<tr>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "<td></td>";
            tableHtml += "</tr>";
        }
        for(var index in tableItemList){
            tableHtml += "<tr>";
            tableHtml += "<td>" + tableItemList[index].agePart + "</td>";
            tableHtml += "<td>" + tableItemList[index].manKeyword1 + "</td>";
            tableHtml += "<td>" + tableItemList[index].manKeyword2 + "</td>";
            tableHtml += "<td>" + tableItemList[index].manKeyword3 + "</td>";
            tableHtml += "<td>" + tableItemList[index].womanKeyword1 + "</td>";
            tableHtml += "<td>" + tableItemList[index].womanKeyword2 + "</td>";
            tableHtml += "<td>" + tableItemList[index].womanKeyword3 + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            limit: 10,
            page: true
        });
    },
    drawUnderwriteListTable: function ($, table, selectedId, tableItemList) {
        var tableHtml = "";
        if(!tableItemList || tableItemList.length <= 0){
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
            tableHtml += "</tr>";
        }
        for(var index in tableItemList){
            tableHtml += "<tr>";
            tableHtml += "<td>" + tableItemList[index].indexNo + "</td>";
            tableHtml += "<td>" + tableItemList[index].mobile + "</td>";
            tableHtml += "<td>" + tableItemList[index].merchantName + "</td>";
            tableHtml += "<td>" + tableItemList[index].platformName + "</td>";
            tableHtml += "<td>" + tableItemList[index].advertiseActive + "</td>";
            tableHtml += "<td>" + tableItemList[index].reserveDate + "</td>";
            tableHtml += "<td>" + tableItemList[index].finishDate + "</td>";
            tableHtml += "<td>" + tableItemList[index].insuranceFee + "</td>";
            tableHtml += "<td>" + tableItemList[index].isRepellent + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId + "-table-body").html(tableHtml);

        table.init(selectedId + "-table", {
            page: false,
            height: 300,
            limit: tableItemList.length
        });
    }
};