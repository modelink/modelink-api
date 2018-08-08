layui.define(['form', 'table', 'element', 'laydate', 'jquery', 'upload'], function (exports) {
    var $ = layui.jquery;
    var form = layui.form;
    var laydate = layui.laydate;

    //年选择器
    laydate.render({
        elem: '#chooseYear',
        type: 'year',
        value: new Date(),
        isInitValue: true
    });
    //年月选择器
    laydate.render({
        elem: '#chooseMonth',
        type: 'month',
        value: new Date(),
        isInitValue: true
    });
    //时间选择器
    laydate.render({
        elem: '#chooseDate',
        type: 'date',
        value: new Date(),
        isInitValue: true
    });

    $(".choose-date").hide();
    $(".choose-date.date").show();
    form.on('select(dateType)', function(selected){
        $(".choose-date").hide();
        if(selected.value == 1){
            $(".choose-date.date").show();
        }else if(selected.value == 2){
            $(".choose-date.month").show();
        }else if(selected.value == 3){
            $(".choose-date.year").show();
        }
    });

    insuranceEcharts.elementMap["reserve-count-echart"] = $("#reserve-count-echart");
    insuranceEcharts.elementMap["insurance-count-echart"] = $("#reserve-count-echart");
    insuranceEcharts.elementMap["insurance-fee-echart"] = $("#insurance-fee-echart");
    insuranceEcharts.elementMap["insurance-cost-echart"] = $("#insurance-cost-echart");
    insuranceEcharts.elementMap["exception-count-echart"] = $("#exception-count-echart");
    insuranceEcharts.elementMap["transform-cycle-echart"] = $("#transform-cycle-echart");
    insuranceEcharts.elementMap["gender-age-echart"] = $("#gender-age-echart");
    insuranceEcharts.elementMap["transform-rate-echart"] = $("#transform-rate-echart");

    insuranceEcharts.echartsMap["reserve-count-echart"] = echarts.init($("#reserve-count-echart")[0]);
    insuranceEcharts.echartsMap["insurance-count-echart"] = echarts.init($("#insurance-count-echart")[0]);
    insuranceEcharts.echartsMap["insurance-fee-echart"] = echarts.init($("#insurance-fee-echart")[0]);
    insuranceEcharts.echartsMap["insurance-cost-echart"] = echarts.init($("#insurance-cost-echart")[0]);
    insuranceEcharts.echartsMap["exception-count-echart"] = echarts.init($("#exception-count-echart")[0]);
    insuranceEcharts.echartsMap["transform-cycle-echart"] = echarts.init($("#transform-cycle-echart")[0]);
    insuranceEcharts.echartsMap["gender-age-echart"] = echarts.init($("#gender-age-echart")[0]);
    insuranceEcharts.echartsMap["transform-rate-echart"] = echarts.init($("#transform-rate-echart")[0]);


    //搜索表单提交
    $("#search-btn").on("click", function () {
        $.ajax({
            url: "",
            data: {
                merchant: $("#merchant").val(),
                dateType: $("#dateType").val(),
                chooseYear: $("#chooseYear").val(),
                chooseMonth: $("#chooseMonth").val(),
                chooseDate: $("#chooseDate").val()
            },
            success: function (response) {
                insuranceEcharts.drawLineEchart("reserve-count-echart", ["","","","",""], ["5","6","7","6","5"]);
                insuranceEcharts.drawLineEchart("insurance-count-echart", ["","","","",""], ["5","6","7","6","5"]);
                insuranceEcharts.drawLineEchart("insurance-fee-echart", ["","","","",""], ["5","6","7","6","5"]);
                insuranceEcharts.drawLineEchart("insurance-cost-echart", ["","","","",""], ["5","6","7","6","5"]);
                insuranceEcharts.drawLineEchart("exception-count-echart", ["","","","",""], ["5","6","7","6","5"]);
                insuranceEcharts.drawLineEchart("transform-cycle-echart", ["","","","",""], ["5","6","7","6","5"]);
                insuranceEcharts.drawAgeBarEchart("gender-age-echart", ["","","","",""], ["5","6","7","6","5"]);
                insuranceEcharts.drawLineEchart("transform-rate-echart", ["","","","",""], ["5","6","7","6","5"]);
            }
        })
    });
    $("#search-btn").trigger("click");


    window.onresize = function () {
        for(var echartIdx in insuranceEcharts.echartsMap){
            insuranceEcharts.echartsMap[echartIdx].resize();
        }
    }

    exports('insurance-echarts', {});
});

var insuranceEcharts = {
    elementMap: {},
    echartsMap: {},
    drawLineEchart: function (selectedId, xData, data) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        // 指定图表的配置项和数据
        var echartOption = {
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
    drawAgeBarEchart: function (selectedId, xData, data) {
        var selectedEchart = insuranceEcharts.echartsMap[selectedId];
        var echartOption = {
            xAxis: [
                {
                    type: 'value',
                    max: 500,
                    min: -500,
                    show: false,
                    axisLabel: {
                        formatter: function (data) {
                            return (Math.abs(data));
                        }
                    }
                }
            ],
            yAxis: [
                {
                    type: 'category',
                    show: false,
                    axisTick: {show: false},
                    data: ['0-18岁', '18-30岁', '30-50岁', '50-60岁', '60-70岁', '70-80岁', '80岁以上']
                }
            ],
            series: [
                {
                    name: '收入',
                    type: 'bar',
                    stack: '总量',
                    label: {
                        normal: {
                            show: false
                        }
                    },
                    data: [320, 302, 341, 374, 390, 450, 420]
                },
                {
                    name: '支出',
                    type: 'bar',
                    stack: '总量',
                    label: {
                        normal: {
                            show: false,
                            position: 'left',
                            formatter: function (v) {
                                return Math.abs(v.data)
                            }
                        }
                    },
                    data: [-320, -132, -101, -134, -190, -420, -440],
                    formatter: function (v) {
                        return Math.abs(v)
                    }
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        selectedEchart.setOption(echartOption);
    }
};