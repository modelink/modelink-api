layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form;
    var laydate = layui.laydate;

    var windowHeight = $(window).height();
    var formHeight = $(".layui-form").height();
    $(".content-fixed").height(windowHeight - formHeight - 100);

    //时间选择器
    laydate.render({
        elem: '#chooseDate',
        range: true
    });

    insuranceEcharts.elementMap["consume-amount-echart"] = $("#consume-amount-echart");
    insuranceEcharts.elementMap["reserve-count-echart"] = $("#reserve-count-echart");
    insuranceEcharts.elementMap["underwrite-amount-echart"] = $("#underwrite-amount-echart");

    insuranceEcharts.elementMap["transform-cost-echart"] = $("#transform-cost-echart");
    insuranceEcharts.elementMap["transform-cycle-echart"] = $("#transform-cycle-echart");
    insuranceEcharts.elementMap["gender-age-echart"] = $("#gender-age-echart");
    insuranceEcharts.elementMap["insurance-amount-echart"] = $("#insurance-amount-echart");
    // insuranceEcharts.elementMap["repellent-amount-echart"] = $("#repellent-amount-echart");
    // insuranceEcharts.elementMap["abnormal-count-echart"] = $("#abnormal-count-echart");
    // insuranceEcharts.elementMap["reserve-click-echart"] = $("#reserve-click-echart");
    // insuranceEcharts.elementMap["transform-rate-echart"] = $("#transform-rate-echart");
    // insuranceEcharts.elementMap["word-clouds-echart"] = $("#word-clouds-echart");

    insuranceEcharts.echartsMap["consume-amount-echart"] = echarts.init($("#consume-amount-echart")[0]);
    insuranceEcharts.echartsMap["reserve-count-echart"] = echarts.init($("#reserve-count-echart")[0]);
    insuranceEcharts.echartsMap["underwrite-amount-echart"] = echarts.init($("#underwrite-amount-echart")[0]);

    insuranceEcharts.echartsMap["transform-cost-echart"] = echarts.init($("#transform-cost-echart")[0]);
    insuranceEcharts.echartsMap["transform-cycle-echart"] = echarts.init($("#transform-cycle-echart")[0]);
    insuranceEcharts.echartsMap["gender-age-echart"] = echarts.init($("#gender-age-echart")[0]);
    insuranceEcharts.echartsMap["insurance-amount-echart"] = echarts.init($("#insurance-amount-echart")[0]);
    // insuranceEcharts.echartsMap["repellent-amount-echart"] = echarts.init($("#repellent-amount-echart")[0]);
    // insuranceEcharts.echartsMap["abnormal-count-echart"] = echarts.init($("#abnormal-count-echart")[0]);
    // insuranceEcharts.echartsMap["reserve-click-echart"] = echarts.init($("#reserve-click-echart")[0]);
    // insuranceEcharts.echartsMap["transform-rate-echart"] = echarts.init($("#transform-rate-echart")[0]);
    // insuranceEcharts.echartsMap["word-clouds-echart"] = echarts.init($("#word-clouds-echart")[0]);


    //搜索表单提交
    $("#search-btn").on("click", function () {

        insuranceEcharts.getDataJson2DrawLine($, "consume-amount", "/admin/dashboard/getConsumeAmount");
        insuranceEcharts.getDataJson2DrawLine($, "reserve-count", "/admin/dashboard/getReserveCount");
        insuranceEcharts.getDataJson2DrawLine($, "underwrite-amount", "/admin/dashboard/getUnderwriteAmount");

        insuranceEcharts.getDataJson2DrawLine($, "transform-cost", "/admin/dashboard/getTransformCost");
        insuranceEcharts.getDataJson2DrawTransformCycle($, "transform-cycle", "/admin/dashboard/getTransformCycle");
        insuranceEcharts.getDataJson2DrawAgeBar($, "gender-age", "/admin/dashboard/getGenderAge");
        insuranceEcharts.getDataJson2DrawPie($, "insurance-amount", "/admin/dashboard/getInsuranceFeeDistribution");
        insuranceEcharts.getDataJson2FillData($, "/admin/dashboard/getReserveClick");
        // insuranceEcharts.getDataJson2DrawLine($, "repellent-amount", "/admin/dashboard/getRepellentAmount");
        // insuranceEcharts.getDataJson2DrawLine($, "abnormal-count", "/admin/dashboard/getAbnormalCount");
        // insuranceEcharts.getDataJson2DrawLine($, "transform-rate", "/admin/dashboard/getTransformRate");
        // insuranceEcharts.getDataJson2DrawLine($, "reserve-click", "/admin/dashboard/getReserveClick");
        // insuranceEcharts.getDataJson2WordCloudsMap($, "word-clouds", "/admin/dashboard/getWordClouds");

    });
    $("#search-btn").trigger("click");
    $("#word-clouds-echart").on("click", function () {
        window.href = "/admin/dashboard/keyword";
    });

    $("#word-clouds-label").on("click", function (event) {
        var url = "/admin/dashboard/keyword";
        insuranceEcharts.openTab($, url, event);
    });
    $("#insurance-map-label").on("click", function (event) {
        var url = "/admin/dashboard/area";
        insuranceEcharts.openTab($, url, event);
    });
    $("#gender-age-label").on("click", function (event) {
        var url = "/admin/dashboard/customer";
        insuranceEcharts.openTab($, url, event);
    });
    $("#underwrite-amount-label,#reserve-count-label,#underwrite-count-label," +
        "#repellent-amount-label,#transform-cost-label,#transform-cycle-label," +
        "#transform-rate-label,#reserve-click-label,#abnormal-count-label"
    ).on("click", function (event) {
        var url = "/admin/dashboard/summary";
        insuranceEcharts.openTab($, url, event);
    });

    window.onresize = function () {
        for(var echartIdx in insuranceEcharts.echartsMap){
            insuranceEcharts.echartsMap[echartIdx].resize();
        }
    }

    exports('dashboard', {});
});

var insuranceEcharts = {
    elementMap: {},
    echartsMap: {},

    openTab: function($, url, event) {
        var allMenus = $('.left-nav #nav li a', window.top.document);
        var selected = $('.left-nav #nav li a[_href="' + url + '"]', window.top.document);
        var title = selected.children('cite').html();
        var index = allMenus.index(selected);
        var id = index + 1;
        window.parent.layui.element.tabAdd('xbs_tab', {
            title: title,
            content: '<iframe tab-id="' + id + '" frameborder="0" src="' + url + '" scrolling="yes" class="x-iframe"></iframe>',
            id: id
        });
        window.parent.layui.element.tabChange('xbs_tab', id);
        event.stopPropagation();
    },

    getDataJson2FillData: function ($, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                dateType: $("#dateType").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData){
                    return;
                }
                $("#capacity").html(response.rtnData.capacity);
                $("#reserveRate").html(response.rtnData.reserveRate);
                $("#underwriteRate").html(response.rtnData.underwriteRate);
            }
        });
    },
    // 获取后台JSON数据画折线图
    getDataJson2DrawLine: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                dateType: $("#dateType").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData || response.rtnData.contentList.length <= 0){
                    insuranceEcharts.drawLineEchart(selectedPrefix + "-echart", ["","","","",""], ["0","0","0","0","0"]);
                    return;
                }
                insuranceEcharts.drawLineEchart(selectedPrefix + "-echart", response.rtnData.titleList, response.rtnData.contentList);
                if (selectedPrefix == "transform-cost") {
                    $("#" + selectedPrefix + " label").html(response.rtnData.totalTransformCost);
                    return;
                }

                $("#" + selectedPrefix + " label").html(response.rtnData.totalValue);
                $("#" + selectedPrefix + " em").html(response.rtnData.trendRate + "%");
                if(response.rtnData.trendRate == 0){
                    $("#" + selectedPrefix + " i").removeClass().addClass("iconfont icon-arrow-equal").attr("style", "color:gray;")
                }else if(response.rtnData.trendRate > 0){
                    $("#" + selectedPrefix + " i").removeClass().addClass("iconfont icon-arrow-up").attr("style", "color:green;")
                }else if(response.rtnData.trendRate < 0){
                    $("#" + selectedPrefix + " i").removeClass().addClass("iconfont icon-arrow-down").attr("style", "color:red;")
                }
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
                dateType: $("#dateType").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData){
                    insuranceEcharts.drawAgeBarEchart(selectedPrefix + "-echart",
                        ['0-5', '5-18', '18-25', '25-30', '30-35', '35-40', '40-50', '50-55', '55以上'],
                        [0, 0, 0, 0, 0, 0, 0, 0, 0]);
                    return;
                }
                insuranceEcharts.drawAgeBarEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList,
                    response.rtnData.contentList);
                $("#" + selectedPrefix + " #man p").html(response.rtnData.manValue);
                $("#" + selectedPrefix + " #woman p").html(response.rtnData.womanValue);
            }
        });
    },
    // 获取后台JSON数据画地图
    getDataJson2DrawMap: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                dateType: $("#dateType").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData){
                    insuranceEcharts.drawMapEchart("insurance-map-echart", [
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
                insuranceEcharts.drawMapEchart("insurance-map-echart", response.rtnData);
            }
        });
    },
    getDataJson2DrawPie: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                dateType: $("#dateType").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData){
                    insuranceEcharts.drawPieEchart("insurance-amount-echart", [
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
                insuranceEcharts.drawPieEchart("insurance-amount-echart", response.rtnData);
            }
        });
    },
    // 获取后台JSON数据画词云图
    getDataJson2WordCloudsMap: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                dateType: $("#dateType").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData){
                    return;
                }
                insuranceEcharts.drawWordCloudsEchart(selectedPrefix + "-echart", response.rtnData);
            }
        });
    },
    getDataJson2DrawTransformCycle: function ($, selectedPrefix, dataUrl) {
        $.ajax({
            url: dataUrl,
            data: {
                merchantId: $("#merchant").val(),
                chooseDate: $("#chooseDate").val(),
                dateType: $("#dateType").val()
            },
            success: function (response) {
                if(!response || response.rtnCode != 200 || !response.rtnData){
                    return;
                }
                insuranceEcharts.drawTransformCycleEchart(selectedPrefix + "-echart",
                    response.rtnData.titleList,
                    response.rtnData.contentList);
            }
        });
    },

    // echarts 画图方法
    drawLineEchart: function (selectedId, xData, data) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            grid: {
                top: 10,
                left: 10,
                right: 10,
                bottom: 10
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            xAxis: {
                show: false,
                type: 'category',
                data: xData
            },
            yAxis: {
                show: false
            },
            series: [{
                type: 'line',
                data: data
            }]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawAgeBarEchart: function (selectedId, titleList, contentList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        selectedEchart.clear();
        var echartOption = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                },
                formatter: function (selected) {
                    return (selected[0].value * 100).toFixed(2) + "%";
                }
            },
            grid: {
                show: true,
                top: 10,
                left: 10,
                right: 10,
                bottom: 50,
                borderWidth: 0  // 去掉外边框
            },
            xAxis: {
                type: 'category',
                splitLine: {
                    show: false
                },
                axisLabel: {
                    interval: 0,
                    rotate: "45"
                },
                data: titleList
            },
            yAxis: {
                show: false
            },
            series: [
                {
                    data: contentList,
                    barWidth: '50%',
                    yAxisIndex: 0,
                    barGap: '0',
                    type: 'bar',
                    itemStyle: {
                        normal: {
                            label: {
                                formatter: function(selected){
                                    return (selected.value * 100).toFixed(2) + "%";
                                }
                            }
                        }
                    }
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawMapEchart: function (selectedId, provinceData) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            grid: {
                left: 50
            },
            backgroundColor: '#b6deff',
            tooltip : {
                trigger: 'item',
                formatter: '{a}<br />{b}: {c}%'
            },
            visualMap: {
                min: 0,
                max: 50,
                left: 'left',
                top: 'bottom',
                text:['高','低'],           // 文本，默认为数值文本
                calculable : true
            },
            //配置属性
            series: [{
                name: '保费分布',
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
                data: provinceData  //数据
            }]
        };

        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    },
    drawPieEchart: function (selectedId, tableItemList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
        var echartOption = {
            tooltip : {
                trigger: 'item',
                formatter: "{b} <br/>保费: {c}元<br/>占比: {d}%"
            },
            series : [
                {
                    type: 'pie',
                    radius : '80%',
                    center: ['50%', '50%'],
                    data: tableItemList,
                    label: {
                        show: false
                    },
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
    drawWordCloudsEchart: function (selectedId, wordCloudsData) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        selectedEchart.clear();
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
    drawTransformCycleEchart: function (selectedId, titleList, contentList) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        selectedEchart.clear();

        var echartOption = {
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                top: '3%',
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis:  {
                type: 'value',
                splitLine: {
                    show: false
                },
            },
            yAxis: {
                type: 'category',
                data: titleList
            },
            series: [{
                type: 'bar',
                data: contentList
            }]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    }
};