layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload'], function (exports) {
    var $ = layui.jquery;
    var table = layui.table;
    var layer = layui.layer;
    var laydate = layui.laydate;

    //时间选择器
    laydate.render({
        elem: '#chooseDate',
        range: true
    });

    insuranceEcharts.echartsMap["reserve-count-echart"] = echarts.init($("#reserve-count-echart")[0]);
    insuranceEcharts.echartsMap["reserve-count-echart"].on("click", function (param) {
        var selected = 'reserve-count-city-echart';
        var tableHtml = '<div id="' + selected + '" style="height: 450px;"></div>';
        layer.open({
            type: 1,
            content: tableHtml,
            area: ['600px', '500px']
        });

        $.ajax({
            url: "/admin/dashboard/area/getReserveCount",
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val(),
                provinceName: param.name
            },
            success: function (response) {
                insuranceEcharts.echartsMap[selected] = echarts.init($("#" + selected)[0]);

                var cellJson = {};
                cellJson.name = "承保件数（件）";
                cellJson.cell = "件";
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawBarEchart(selected, cellJson,
                        ["", "", "", "", "", "", "", "", "", ""], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawBarEchart(selected, cellJson, response.rtnData.titleList, response.rtnData.contentList);
            }
        })
    });
    insuranceEcharts.echartsMap["underwrite-count-echart"] = echarts.init($("#underwrite-count-echart")[0]);
    insuranceEcharts.echartsMap["underwrite-count-echart"].on("click", function (param) {
        var selected = 'underwrite-count-city-echart';
        var tableHtml = '<div id="' + selected + '" style="height: 450px;"></div>';
        layer.open({
            type: 1,
            content: tableHtml,
            area: ['600px', '500px']
        });

        $.ajax({
            url: "/admin/dashboard/area/getUnderwriteCount",
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val(),
                provinceName: param.name
            },
            success: function (response) {
                insuranceEcharts.echartsMap[selected] = echarts.init($("#" + selected)[0]);

                var cellJson = {};
                cellJson.name = "承保件数（件）";
                cellJson.cell = "件";
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawBarEchart(selected, cellJson,
                        ["", "", "", "", "", "", "", "", "", ""], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawBarEchart(selected, cellJson, response.rtnData.titleList, response.rtnData.contentList);
            }
        })
    });
    insuranceEcharts.echartsMap["underwrite-amount-echart"] = echarts.init($("#underwrite-amount-echart")[0]);
    insuranceEcharts.echartsMap["underwrite-amount-echart"].on("click", function (param) {
        var selected = 'underwrite-amount-city-echart';
        var tableHtml = '<div id="' + selected + '" style="height: 450px;"></div>';
        layer.open({
            type: 1,
            content: tableHtml,
            area: ['600px', '500px']
        });

        $.ajax({
            url: "/admin/dashboard/area/getUnderwriteAmount",
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val(),
                provinceName: param.name
            },
            success: function (response) {
                insuranceEcharts.echartsMap[selected] = echarts.init($("#" + selected)[0]);

                var cellJson = {};
                cellJson.name = "总保费（元）";
                cellJson.cell = "元";
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawBarEchart(selected, cellJson,
                        ["", "", "", "", "", "", "", "", "", ""], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawBarEchart(selected, cellJson, response.rtnData.titleList, response.rtnData.contentList);
            }
        })
    });

    insuranceEcharts.echartsMap["underwrite-map-echart"] = echarts.init($("#underwrite-map-echart")[0], "light");
    insuranceEcharts.echartsMap["again-rate-echart"] = echarts.init($("#again-rate-echart")[0]);
    insuranceEcharts.echartsMap["again-rate-echart"].on("click", function (param) {
        var selected = 'again-rate-city-echart';
        var tableHtml = '<div id="' + selected + '" style="height: 450px;"></div>';
        layer.open({
            type: 1,
            content: tableHtml,
            area: ['600px', '500px']
        });

        $.ajax({
            url: "/admin/dashboard/area/getAgainRate",
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val(),
                provinceName: param.name
            },
            success: function (response) {
                insuranceEcharts.echartsMap[selected] = echarts.init($("#" + selected)[0]);

                var cellJson = {};
                cellJson.name = "二跳率（%）";
                cellJson.cell = "%";
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawBarEchart(selected, cellJson,
                        ["", "", "", "", "", "", "", "", "", ""], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawBarEchart(selected, cellJson, response.rtnData.titleList, response.rtnData.contentList);
            }
        })
    });

    insuranceEcharts.echartsMap["user-count-echart"] = echarts.init($("#user-count-echart")[0]);
    insuranceEcharts.echartsMap["user-count-echart"].on("click", function (param) {
        var selected = 'user-count-city-echart';
        var tableHtml = '<div id="' + selected + '" style="height: 450px;"></div>';
        layer.open({
            type: 1,
            content: tableHtml,
            area: ['600px', '500px']
        });

        $.ajax({
            url: "/admin/dashboard/area/getUserCount",
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val(),
                provinceName: param.name
            },
            success: function (response) {
                insuranceEcharts.echartsMap[selected] = echarts.init($("#" + selected)[0]);

                var cellJson = {};
                cellJson.name = "用户数（个）";
                cellJson.cell = "个";
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawBarEchart(selected, cellJson,
                        ["", "", "", "", "", "", "", "", "", ""], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawBarEchart(selected, cellJson, response.rtnData.titleList, response.rtnData.contentList);
            }
        })
    });
    insuranceEcharts.echartsMap["user-stay-echart"] = echarts.init($("#user-stay-echart")[0]);
    insuranceEcharts.echartsMap["user-stay-echart"].on("click", function (param) {
        var selected = 'user-stay-city-echart';
        var tableHtml = '<div id="' + selected + '" style="height: 450px;"></div>';
        layer.open({
            type: 1,
            content: tableHtml,
            area: ['600px', '500px']
        });

        $.ajax({
            url: "/admin/dashboard/area/getUserStayTime",
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val(),
                provinceName: param.name
            },
            success: function (response) {
                insuranceEcharts.echartsMap[selected] = echarts.init($("#" + selected)[0]);

                var cellJson = {};
                cellJson.name = "平均停留时长（秒）";
                cellJson.cell = "秒";
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawBarEchart(selected, cellJson,
                        ["", "", "", "", "", "", "", "", "", ""], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawBarEchart(selected, cellJson, response.rtnData.titleList, response.rtnData.contentList);
            }
        })
    });
    insuranceEcharts.echartsMap["user-gender-echart"] = echarts.init($("#user-gender-echart")[0]);
    insuranceEcharts.echartsMap["user-gender-echart"].on("click", function (param) {
        var selected = 'user-gender-city-echart';
        var tableHtml = '<div id="' + selected + '" style="height: 450px;"></div>';
        layer.open({
            type: 1,
            content: tableHtml,
            area: ['600px', '500px']
        });

        $.ajax({
            url: "/admin/dashboard/area/getUserGender",
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                platformName: $("#platformName").val(),
                advertiseActive: $("#advertiseActive").val(),
                provinceName: param.name
            },
            success: function (response) {
                insuranceEcharts.echartsMap[selected] = echarts.init($("#" + selected)[0]);

                var cellJson = {};
                cellJson.name = "平均停留时长（秒）";
                cellJson.cell = "秒";
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawBarEchart(selected, cellJson,
                        ["", "", "", "", "", "", "", "", "", ""], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawBarEchart(selected, cellJson, response.rtnData.titleList, response.rtnData.contentList);
            }
        })
    });
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
                var cellJson = {};
                if(selectedPrefix == 'reserve-count'){
                    cellJson.name = "预约量（个）";
                    cellJson.cell = "个";
                }else if(selectedPrefix == 'underwrite-count'){
                    cellJson.name = "承保件数（件）";
                    cellJson.cell = "件";
                }else if(selectedPrefix == 'underwrite-amount'){
                    cellJson.name = "保费总额（元）";
                    cellJson.cell = "元";
                }else if(selectedPrefix == 'again-rate'){
                    cellJson.name = "二跳率（%）";
                    cellJson.cell = "%";
                }else if(selectedPrefix == 'user-count'){
                    cellJson.name = "用户数（个）";
                    cellJson.cell = "个";
                }else if(selectedPrefix == 'user-stay'){
                    cellJson.name = "平均停留时长（秒）";
                    cellJson.cell = "秒";
                }
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawBarEchart(selectedPrefix + "-echart", cellJson,
                        ["北京市", "上海市", "广东省", "山东省", "江苏省", "浙江省", "四川省", "重庆市", "河南省", "湖北省"],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawBarEchart(selectedPrefix + "-echart", cellJson, response.rtnData.titleList, response.rtnData.contentList);
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
                var cellJson = {};
                cellJson.name = "承保件数（件）";
                cellJson.cell = "件";
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.titleList.length <= 0){
                    insuranceEcharts.drawGenderBarEchart(selectedPrefix + "-echart", cellJson,
                        ["北京市", "上海市", "广东省", "山东省", "江苏省", "浙江省", "四川省", "重庆市", "河南省", "湖北省"],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawGenderBarEchart(selectedPrefix + "-echart", cellJson,
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
            backgroundColor: '#9ad0e6',
            tooltip: {
                trigger: 'item',
                formatter: function (param) {
                    var result = param.name + "<br />";
                    result += ("保费: " + param.value + "元<br />");
                    result += ("占比: " + param.data.ratio + "%");
                    return result;
                }
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
    drawBarEchart: function (selectedId, cellJson, titleList, contentList) {

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
                top: "40",
                left: "80",
                right: "20",
                bottom: "80",
                borderColor: "#c45455"
            },
            xAxis: {
                type: 'category',
                splitLine:{
                    show:false
                },
                axisLabel: {
                    interval: 0,
                    rotate: "45"
                },
                data: titleList
            },
            yAxis: [
                {
                    name: cellJson.name,
                    type: 'value',
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
                    yAxisIndex: 0,
                    barGap: '0',
                    type: 'bar',
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawGenderBarEchart: function (selectedId, cellJson, titleList, manList, womanList, unknowList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            legend: {
                data:['男','女','未知']
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                },
                formatter: function (param) {
                    var result;
                    var totalCount = 0;
                    for (var index in param) {
                        totalCount += param[index].value;
                        result = param[index].name;
                    }
                    if(totalCount == 0) totalCount = 1;
                    result += "<br/>";
                    for (var index in param) {
                        result += (param[index].seriesName + " : "
                            + param[index].value + "件 : "
                            + (param[index].value * 100 / totalCount).toFixed(2)
                            + "%" + "<br/>");
                    }
                    return result;
                }
            },
            grid: {
                show: true,
                top: "40",
                left: "80",
                right: "20",
                bottom: "80",
                borderColor: "#c45455"
            },
            xAxis: {
                type: 'category',
                splitLine:{
                    show:false
                },
                axisLabel: {
                    interval: 0,
                    rotate: "45"
                },
                data: titleList
            },
            yAxis: [
                {
                    name: cellJson.name,
                    type: 'value',
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
                    name: '男',
                    data: manList,
                    barWidth: '50%',
                    barGap: '0',
                    stack: '性别',
                    type: 'bar',
                },
                {
                    name: '女',
                    data: womanList,
                    barWidth: '50%',
                    barGap: '0',
                    stack: '性别',
                    type: 'bar',
                },
                {
                    name: '未知',
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
            page: false,
            height: 300,
            limit: gridDataList.length
        });
    }
};