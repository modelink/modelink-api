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
    insuranceEcharts.echartsMap["underwrite-count-echart"] = echarts.init($("#underwrite-count-echart")[0]);
    insuranceEcharts.echartsMap["underwrite-amount-echart"] = echarts.init($("#underwrite-amount-echart")[0]);

    insuranceEcharts.echartsMap["underwrite-map-echart"] = echarts.init($("#underwrite-map-echart")[0]);
    insuranceEcharts.echartsMap["again-rate-echart"] = echarts.init($("#again-rate-echart")[0]);

    insuranceEcharts.echartsMap["user-count-echart"] = echarts.init($("#user-count-echart")[0]);
    insuranceEcharts.echartsMap["user-stay-echart"] = echarts.init($("#user-stay-echart")[0]);
    insuranceEcharts.echartsMap["user-gender-echart"] = echarts.init($("#user-gender-echart")[0]);
    //搜索表单提交
    $("#search-btn").on("click", function () {
        insuranceEcharts.getDataJson2DrawBar($, "reserve-count", "/admin/dashboard/area/getReserveCount");
        insuranceEcharts.getDataJson2DrawBar($, "underwrite-count", "/admin/dashboard/area/getUnderwriteCount");
        insuranceEcharts.getDataJson2DrawBar($, "underwrite-amount", "/admin/dashboard/area/getUnderwriteAmount");

        insuranceEcharts.getDataJson2DrawMap($, "underwrite-map", "/admin/dashboard/area/getUnderwriteMap");
        insuranceEcharts.getDataJson2DrawBar($, "again-rate", "/admin/dashboard/area/getAgainRate");

        insuranceEcharts.getDataJson2DrawBar($, "user-count", "/admin/dashboard/area/getUserCount");
        insuranceEcharts.getDataJson2DrawBar($, "user-stay", "/admin/dashboard/area/getUserStayTime");
        insuranceEcharts.getDataJson2DrawGenderBar($, "user-gender", "/admin/dashboard/area/getUserGender");

        insuranceEcharts.getDataJson2DrawAreaTableGrid($, table, "area-table-body", "/admin/dashboard/area/areaTableGrid");
    });
    $("#search-btn").trigger("click");


    window.onresize = function () {
        for(var echartIdx in insuranceEcharts.echartsMap){
            insuranceEcharts.echartsMap[echartIdx].resize();
        }
    }

    exports('area', {});
});

var insuranceEcharts = {
    echartsMap: {},

    // 获取后台JSON数据画图
    getDataJson2DrawMap: function ($, selectedPrefix, dataUrl) {
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
                    insuranceEcharts.drawMapEchart(selectedPrefix + "-echart", [
                        {name: '北京市',value: 0 },
                        {name: '天津市',value: 0 },
                        {name: '上海市',value: 0 },
                        {name: '重庆市',value: 0 },
                        {name: '河北省',value: 0 },
                        {name: '河南省',value: 0 },
                        {name: '云南省',value: 0 },
                        {name: '辽宁省',value: 0 },
                        {name: '黑龙江省',value: 0 },
                        {name: '湖南省',value: 0 },
                        {name: '安徽省',value: 0 },
                        {name: '山东省',value: 0 },
                        {name: '新疆维吾尔自治区',value: 0 },
                        {name: '江苏省',value: 0 },
                        {name: '浙江省',value: 0 },
                        {name: '江西省',value: 0 },
                        {name: '湖北省',value: 0 },
                        {name: '广西壮族自治区',value: 0 },
                        {name: '甘肃省',value: 0 },
                        {name: '山西省',value: 0 },
                        {name: '内蒙古自治区',value: 0 },
                        {name: '陕西省',value: 0 },
                        {name: '吉林省',value: 0 },
                        {name: '福建省',value: 0 },
                        {name: '贵州省',value: 0 },
                        {name: '广东省',value: 0 },
                        {name: '青海省',value: 0 },
                        {name: '西藏自治区',value: 0 },
                        {name: '四川省',value: 0 },
                        {name: '宁夏回族自治区',value: 0 },
                        {name: '海南省',value: 0 },
                        {name: '台湾省',value: 0 },
                        {name: '香港特别行政区',value: 0 },
                        {name: '澳门特别行政区',value: 0 }
                    ]);
                    return;
                }
                insuranceEcharts.drawMapEchart(selectedPrefix + "-echart", response.rtnData);
            }
        });
    },
    getDataJson2DrawBar: function ($, selectedPrefix, dataUrl) {
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
                    insuranceEcharts.drawBarEchart(selectedPrefix + "-echart",
                        ["北京市", "上海市", "广东省", "山东省", "江苏省", "浙江省", "四川省", "重庆市", "河南省", "湖北省"],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawBarEchart(selectedPrefix + "-echart", response.rtnData.titleList, response.rtnData.contentList);
            }
        });
    },
    getDataJson2DrawGenderBar: function ($, selectedPrefix, dataUrl) {
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
                    insuranceEcharts.drawGenderBarEchart(selectedPrefix + "-echart",
                        ["北京市", "上海市", "广东省", "山东省", "江苏省", "浙江省", "四川省", "重庆市", "河南省", "湖北省"],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawGenderBarEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList, response.rtnData.manList, response.rtnData.womanList, response.rtnData.unknowList);
            }
        });
    },
    getDataJson2DrawAreaTableGrid: function($, table, selectedPrefix, dataUrl) {
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
                    insuranceEcharts.drawAreaTableGridTable($, table, selectedPrefix, []);
                    return;
                }
                insuranceEcharts.drawAreaTableGridTable($, table, selectedPrefix, response.rtnData);
            },
            error: function () {
                insuranceEcharts.drawAreaTableGridTable($, table, selectedPrefix, []);
            }
        });
    },

    // echarts 画图方法
    drawMapEchart: function (selectedId, contentList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            backgroundColor: '#FFFFFF',
            tooltip : {
                trigger: 'item'
            },

            //配置属性
            series: [{
                name: '保费',
                type: 'map',
                mapType: 'china',
                roam: true,
                label: {
                    normal: {
                        show: false  //省份名称
                    },
                    emphasis: {
                        show: false
                    }
                },
                data: contentList  //数据
            }]
        };

        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawBarEchart: function (selectedId, titleList, contentList) {

        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
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
    drawGenderBarEchart: function (selectedId, titleList, manList, womanList, unknowList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
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
                    data: manList,
                    barWidth: '50%',
                    barGap: '0',
                    stack: '性别',
                    type: 'bar',
                },
                {
                    data: womanList,
                    barWidth: '50%',
                    barGap: '0',
                    stack: '性别',
                    type: 'bar',
                },
                {
                    data: unknowList,
                    barWidth: '50%',
                    barGap: '0',
                    stack: '性别',
                    type: 'bar',
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawAreaTableGridTable: function ($, table, selectedId, gridDataList) {
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
            tableHtml += "</tr>";
        }
        for(var index in gridDataList){
            tableHtml += "<tr>";
            tableHtml += "<td>" + gridDataList[index].indexNo + "</td>";
            tableHtml += "<td>" + gridDataList[index].merchantName + "</td>";
            tableHtml += "<td>" + gridDataList[index].platformName + "</td>";
            tableHtml += "<td>" + gridDataList[index].advertiseActive + "</td>";
            tableHtml += "<td>" + gridDataList[index].provinceName + "</td>";
            tableHtml += "<td>" + gridDataList[index].cityName + "</td>";
            tableHtml += "<td>" + gridDataList[index].browseCount + "</td>";
            tableHtml += "<td>" + gridDataList[index].reserveCount + "</td>";
            tableHtml += "<td>" + gridDataList[index].underwriteCount + "</td>";
            tableHtml += "<td>" + gridDataList[index].underwriteAmount + "</td>";
            tableHtml += "<td>" + gridDataList[index].transformCycle + "</td>";
            tableHtml += "</tr>";
        }
        $("#" + selectedId).html(tableHtml);

        table.init("area-table", {
            limit: 10,
            page: true
        });
    }
};