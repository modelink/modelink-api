package com.modelink.admin.controller.huaxiaReport;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.huaxiaReport.HuaxiaReportDetailItemVo;
import com.modelink.admin.vo.huaxiaReport.HuaxiaReportParamVo;
import com.modelink.admin.vo.huaxiaReport.HuaxiaReportSummaryVo;
import com.modelink.common.excel.ExcelExportConfigation;
import com.modelink.common.excel.ExcelExportHelper;
import com.modelink.common.excel.ExcelSheetItem;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.reservation.bean.HuaxiaDataReport;
import com.modelink.reservation.bean.HuaxiaFlowReport;
import com.modelink.reservation.bean.MediaItem;
import com.modelink.reservation.bean.Permiums;
import com.modelink.reservation.enums.FeeTypeEnum;
import com.modelink.reservation.service.*;
import com.modelink.reservation.vo.HuaxiaDataReportParamPagerVo;
import com.modelink.reservation.vo.HuaxiaFlowReportParamPagerVo;
import com.modelink.reservation.vo.PermiumsParamPagerVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping("/admin/huaxiaReport")
public class HuaxiaReportController {

    @Resource
    private PermiumsService permiumsService;
    @Resource
    private MediaItemService mediaItemService;
    @Resource
    private FlowReserveService flowReserveService;
    @Resource
    private HuaxiaDataReportService huaxiaDataReportService;
    @Resource
    private HuaxiaFlowReportService huaxiaFlowReportService;

    @RequestMapping("/summary")
    public ModelAndView summary() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/admin/huaxia-report/summary");
        return modelAndView;
    }
    @RequestMapping("/detailItem")
    public ModelAndView detailItem() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/admin/huaxia-report/detail-item");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/summaryList")
    public LayuiResultPagerVo<HuaxiaReportSummaryVo> summaryList(HuaxiaReportParamVo paramVo) {
        LayuiResultPagerVo<HuaxiaReportSummaryVo> layuiResultPagerVo = new LayuiResultPagerVo<>();

        initDashboardParam(paramVo);

        HuaxiaDataReportParamPagerVo paramPagerVo = new HuaxiaDataReportParamPagerVo();
        BeanUtils.copyProperties(paramVo, paramPagerVo);
        PageInfo<HuaxiaDataReport> pageInfo = huaxiaDataReportService.findPagerByParam(paramPagerVo);
        List<HuaxiaDataReport> dataReportList = pageInfo.getList();
        List<HuaxiaReportSummaryVo> summaryVoList = transformSummary2VoList(paramVo, dataReportList);

        layuiResultPagerVo.setTotalCount((int)pageInfo.getTotal());
        layuiResultPagerVo.setRtnList(summaryVoList);
        return layuiResultPagerVo;
    }

    @ResponseBody
    @RequestMapping("/detailItemList")
    public LayuiResultPagerVo<HuaxiaReportDetailItemVo> detailItemList(HuaxiaReportParamVo paramVo) {
        LayuiResultPagerVo<HuaxiaReportDetailItemVo> layuiResultPagerVo = new LayuiResultPagerVo<>();

        initDashboardParam(paramVo);

        HuaxiaFlowReportParamPagerVo paramPagerVo = new HuaxiaFlowReportParamPagerVo();
        BeanUtils.copyProperties(paramVo, paramPagerVo);
        PageInfo<HuaxiaFlowReport> pageInfo = huaxiaFlowReportService.findPagerByParam(paramPagerVo);
        List<HuaxiaFlowReport> dataReportList = pageInfo.getList();
        List<HuaxiaReportDetailItemVo> summaryVoList = transformDetailItem2PageViewVoList(paramVo, dataReportList);

        layuiResultPagerVo.setTotalCount((int)pageInfo.getTotal());
        layuiResultPagerVo.setRtnList(summaryVoList);
        return layuiResultPagerVo;
    }

    @RequestMapping("/summaryDownload")
    public void summaryDownload(HttpServletResponse response, HuaxiaReportParamVo paramVo, String reports) throws Exception {
        initDashboardParam(paramVo);
        // 创建文件名称
        String fileName = "华夏日报-汇总表-" + DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        // 创建Excel表格列名称
        List<String> columnNameList = new ArrayList<>();
        columnNameList.add("日期");
        columnNameList.add("数据来源");
        columnNameList.add("有效数据（下发）");
        columnNameList.add("营销标记");
        columnNameList.add("总转化/测保转化（包含小米）");
        columnNameList.add("总转化（不含微信）/测保转化（不含小米）");
        columnNameList.add("PC");
        columnNameList.add("WAP");
        columnNameList.add("微信转化");
        columnNameList.add("广告直接转化数");

        columnNameList.add("浏览量");
        columnNameList.add("点击量");
        columnNameList.add("到达量");
        columnNameList.add("到达用户");
        columnNameList.add("到达率（%）");
        columnNameList.add("二跳量");
        columnNameList.add("二跳率（%）");
        columnNameList.add("平均停留时间（S）");

        columnNameList.add("展示数");
        columnNameList.add("点击数");
        columnNameList.add("点击率（%）");
        columnNameList.add("CPC（元）");
        columnNameList.add("CPM（元）");
        columnNameList.add("总花费（元）");

        columnNameList.add("直接转化成本");
        columnNameList.add("总转化成本（不含微信）");
        columnNameList.add("保费（元）");

        // 创建Excel 数据
        List<String> rowValueList;
        List<List<String>> dataList;

        HuaxiaReportParamVo huaxiaReportParamVo;
        HuaxiaDataReportParamPagerVo paramPagerVo;
        List<HuaxiaReportSummaryVo> summaryVoList;
        List<HuaxiaDataReport> dataReportList;
        String[] reportArray = reports.split(",");

        String sheetName;
        ExcelSheetItem excelSheetItem;
        List<String> sheetNameList = new ArrayList<>();
        Map<String, ExcelSheetItem> sheetContentMap = new HashMap<>();
        for (String report : reportArray) {


            paramPagerVo = new HuaxiaDataReportParamPagerVo();
            paramPagerVo.setChooseDate(paramVo.getChooseDate());
            paramPagerVo.setDataSource(report);

            // 汇总月报
            sheetName = "月汇总-" + report;
            sheetNameList.add(sheetName);
            dataReportList = huaxiaDataReportService.findListByMonthGroup(paramPagerVo);
            huaxiaReportParamVo = new HuaxiaReportParamVo();
            BeanUtils.copyProperties(paramPagerVo, huaxiaReportParamVo);
            summaryVoList = transformSummary2VoListByMonth(huaxiaReportParamVo, dataReportList);

            dataList = new ArrayList<>();
            for (HuaxiaReportSummaryVo summaryVo : summaryVoList) {
                rowValueList = new ArrayList<>();
                // to-do
                rowValueList.add(summaryVo.getDate());
                rowValueList.add(summaryVo.getDataSource());
                rowValueList.add(summaryVo.getValidCount() + "");
                rowValueList.add(summaryVo.getFlagCount() + "");
                rowValueList.add(summaryVo.getTotalCount() + "");
                rowValueList.add(summaryVo.getMiniTotalCount() + "");
                rowValueList.add(summaryVo.getPcCount() + "");
                rowValueList.add(summaryVo.getWapCount() + "");
                rowValueList.add(summaryVo.getWeixinCount() + "");
                rowValueList.add(summaryVo.getDirectTransformCount() + "");

                rowValueList.add(summaryVo.getBrowseCount() + "");
                rowValueList.add(summaryVo.getClickCount() + "");
                rowValueList.add(summaryVo.getArriveCount() + "");
                rowValueList.add(summaryVo.getArriveUserCount() + "");
                rowValueList.add(summaryVo.getArriveRate());
                rowValueList.add(summaryVo.getAgainCount() + "");
                rowValueList.add(summaryVo.getAgainRate());
                rowValueList.add(summaryVo.getAverageStayTime());

                rowValueList.add(summaryVo.getMediaShowCount() == null ? "" : summaryVo.getMediaShowCount().toString());
                rowValueList.add(summaryVo.getMediaClickCount() == null ? "" : summaryVo.getMediaClickCount().toString());
                rowValueList.add(summaryVo.getMediaClickRate());
                rowValueList.add(summaryVo.getCpc());
                rowValueList.add(summaryVo.getCpm());
                rowValueList.add(summaryVo.getConsumeAmount());

                rowValueList.add(summaryVo.getDirectTransformCost());
                rowValueList.add(summaryVo.getTotalTransformCost());
                rowValueList.add(summaryVo.getInsuranceAmount());
                dataList.add(rowValueList);
            }

            excelSheetItem = new ExcelSheetItem();
            excelSheetItem.setColumnNameList(columnNameList);
            excelSheetItem.setCellValueList(dataList);
            sheetContentMap.put(sheetName, excelSheetItem);

            // 汇总日报
            sheetName = "日汇总-" + report;
            sheetNameList.add(sheetName);
            dataReportList = huaxiaDataReportService.findListByParam(paramPagerVo);
            huaxiaReportParamVo = new HuaxiaReportParamVo();
            BeanUtils.copyProperties(paramPagerVo, huaxiaReportParamVo);
            summaryVoList = transformSummary2VoList(huaxiaReportParamVo, dataReportList);

            dataList = new ArrayList<>();
            for (HuaxiaReportSummaryVo summaryVo : summaryVoList) {
                rowValueList = new ArrayList<>();
                // to-do
                rowValueList.add(summaryVo.getDate());
                rowValueList.add(summaryVo.getDataSource());
                rowValueList.add(summaryVo.getValidCount() + "");
                rowValueList.add(summaryVo.getFlagCount() + "");
                rowValueList.add(summaryVo.getTotalCount() + "");
                rowValueList.add(summaryVo.getMiniTotalCount() + "");
                rowValueList.add(summaryVo.getPcCount() + "");
                rowValueList.add(summaryVo.getWapCount() + "");
                rowValueList.add(summaryVo.getWeixinCount() + "");
                rowValueList.add(summaryVo.getDirectTransformCount() + "");

                rowValueList.add(summaryVo.getBrowseCount() + "");
                rowValueList.add(summaryVo.getClickCount() + "");
                rowValueList.add(summaryVo.getArriveCount() + "");
                rowValueList.add(summaryVo.getArriveUserCount() + "");
                rowValueList.add(summaryVo.getArriveRate());
                rowValueList.add(summaryVo.getAgainCount() + "");
                rowValueList.add(summaryVo.getAgainRate());
                rowValueList.add(summaryVo.getAverageStayTime());

                rowValueList.add(summaryVo.getMediaShowCount() == null ? "" : summaryVo.getMediaShowCount().toString());
                rowValueList.add(summaryVo.getMediaClickCount() == null ? "" : summaryVo.getMediaClickCount().toString());
                rowValueList.add(summaryVo.getMediaClickRate());
                rowValueList.add(summaryVo.getCpc());
                rowValueList.add(summaryVo.getCpm());
                rowValueList.add(summaryVo.getConsumeAmount());

                rowValueList.add(summaryVo.getDirectTransformCost());
                rowValueList.add(summaryVo.getTotalTransformCost());
                rowValueList.add(summaryVo.getInsuranceAmount());
                dataList.add(rowValueList);
            }

            excelSheetItem = new ExcelSheetItem();
            excelSheetItem.setColumnNameList(columnNameList);
            excelSheetItem.setCellValueList(dataList);
            sheetContentMap.put(sheetName, excelSheetItem);
        }

        ExcelExportConfigation excelConfigation = new ExcelExportConfigation();
        excelConfigation.setFileName(fileName);
        excelConfigation.setSheetNameList(sheetNameList);
        excelConfigation.setSheetContentMap(sheetContentMap);
        ExcelExportHelper.exportExcel2Response(excelConfigation, response);
    }

    @RequestMapping("/detailItemDownload")
    public void detailItemDownload(HttpServletResponse response, HuaxiaReportParamVo paramVo, String reports) throws Exception {
        initDashboardParam(paramVo);
        // 创建文件名称
        String fileName = "华夏日报-" + DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        // 创建Excel表格列名称
        List<String> columnNameList = new ArrayList<>();
        columnNameList.add("日期");
        columnNameList.add("数据来源");
        columnNameList.add("渠道归属");
        columnNameList.add("广告活动");
        columnNameList.add("广告直接转化数");

        columnNameList.add("浏览量");
        columnNameList.add("点击量");
        columnNameList.add("到达量");
        columnNameList.add("到达用户");
        columnNameList.add("到达率（%）");
        columnNameList.add("二跳量");
        columnNameList.add("二跳率（%）");
        columnNameList.add("平均停留时间（S）");

        columnNameList.add("展示数");
        columnNameList.add("点击数");
        columnNameList.add("点击率（%）");
        columnNameList.add("CPC（元）");
        columnNameList.add("CPM（元）");
        columnNameList.add("总花费（元）");

        columnNameList.add("直接转化成本");

        // 创建Excel 数据
        List<String> rowValueList;
        List<List<String>> dataList;

        HuaxiaReportParamVo huaxiaReportParamVo;
        HuaxiaFlowReportParamPagerVo paramPagerVo;
        List<HuaxiaReportDetailItemVo> detailItemVoList;
        List<HuaxiaFlowReport> flowReportList;
        String[] reportArray = reports.split(",");

        String[] arrays;
        String sheetName;
        ExcelSheetItem excelSheetItem;
        List<String> sheetNameList = new ArrayList<>();
        Map<String, ExcelSheetItem> sheetContentMap = new HashMap<>();
        // 输出汇总的EXCEL数据
        for (String report : reportArray) {
            paramPagerVo = new HuaxiaFlowReportParamPagerVo();
            paramPagerVo.setChooseDate(paramVo.getChooseDate());

            arrays = report.split("-");
            paramPagerVo.setDataSource(arrays[0]);
            paramPagerVo.setPlatformName(arrays[1]);

            sheetName =  "月汇总-" + report;
            sheetNameList.add(sheetName);
            flowReportList = huaxiaFlowReportService.findListByMonthGroup(paramPagerVo);
            huaxiaReportParamVo = new HuaxiaReportParamVo();
            BeanUtils.copyProperties(paramPagerVo, huaxiaReportParamVo);
            detailItemVoList = transformDetailItem2DownloadVoListByMonth(huaxiaReportParamVo, flowReportList);

            dataList = new ArrayList<>();
            for (HuaxiaReportDetailItemVo detailItemVo : detailItemVoList) {
                rowValueList = new ArrayList<>();
                // to-do
                rowValueList.add(detailItemVo.getDate());
                rowValueList.add(detailItemVo.getDataSource());
                rowValueList.add(detailItemVo.getPlatformName());
                rowValueList.add(detailItemVo.getAdvertiseActive());
                rowValueList.add(detailItemVo.getDirectTransformCount() + "");

                rowValueList.add(detailItemVo.getBrowseCount() + "");
                rowValueList.add(detailItemVo.getClickCount() + "");
                rowValueList.add(detailItemVo.getArriveCount() + "");
                rowValueList.add(detailItemVo.getArriveUserCount() + "");
                rowValueList.add(detailItemVo.getArriveRate());
                rowValueList.add(detailItemVo.getAgainCount() + "");
                rowValueList.add(detailItemVo.getAgainRate());
                rowValueList.add(detailItemVo.getAverageStayTime() + "");

                rowValueList.add(detailItemVo.getMediaShowCount() == null ? "" : detailItemVo.getMediaShowCount().toString());
                rowValueList.add(detailItemVo.getMediaClickCount() == null ? "" : detailItemVo.getMediaClickCount().toString());
                rowValueList.add(detailItemVo.getMediaClickRate());
                rowValueList.add(detailItemVo.getCpc());
                rowValueList.add(detailItemVo.getCpm());
                rowValueList.add(detailItemVo.getConsumeAmount());

                rowValueList.add(detailItemVo.getDirectTransformCost());
                dataList.add(rowValueList);
            }

            excelSheetItem = new ExcelSheetItem();
            excelSheetItem.setColumnNameList(columnNameList);
            excelSheetItem.setCellValueList(dataList);
            sheetContentMap.put(sheetName, excelSheetItem);

            sheetName = "日汇总-" + report;
            sheetNameList.add(sheetName);
            flowReportList = huaxiaFlowReportService.findListByParamGroup(paramPagerVo);
            huaxiaReportParamVo = new HuaxiaReportParamVo();
            BeanUtils.copyProperties(paramPagerVo, huaxiaReportParamVo);
            detailItemVoList = transformDetailItem2DownloadVoListByDate(huaxiaReportParamVo, flowReportList);

            dataList = new ArrayList<>();
            for (HuaxiaReportDetailItemVo detailItemVo : detailItemVoList) {
                rowValueList = new ArrayList<>();
                // to-do
                rowValueList.add(detailItemVo.getDate());
                rowValueList.add(detailItemVo.getDataSource());
                rowValueList.add(detailItemVo.getPlatformName());
                rowValueList.add(detailItemVo.getAdvertiseActive());
                rowValueList.add(detailItemVo.getDirectTransformCount() + "");

                rowValueList.add(detailItemVo.getBrowseCount() + "");
                rowValueList.add(detailItemVo.getClickCount() + "");
                rowValueList.add(detailItemVo.getArriveCount() + "");
                rowValueList.add(detailItemVo.getArriveUserCount() + "");
                rowValueList.add(detailItemVo.getArriveRate());
                rowValueList.add(detailItemVo.getAgainCount() + "");
                rowValueList.add(detailItemVo.getAgainRate());
                rowValueList.add(detailItemVo.getAverageStayTime() + "");

                rowValueList.add(detailItemVo.getMediaShowCount() == null ? "" : detailItemVo.getMediaShowCount().toString());
                rowValueList.add(detailItemVo.getMediaClickCount() == null ? "" : detailItemVo.getMediaClickCount().toString());
                rowValueList.add(detailItemVo.getMediaClickRate());
                rowValueList.add(detailItemVo.getCpc());
                rowValueList.add(detailItemVo.getCpm());
                rowValueList.add(detailItemVo.getConsumeAmount());

                rowValueList.add(detailItemVo.getDirectTransformCost());
                dataList.add(rowValueList);
            }

            excelSheetItem = new ExcelSheetItem();
            excelSheetItem.setColumnNameList(columnNameList);
            excelSheetItem.setCellValueList(dataList);
            sheetContentMap.put(sheetName, excelSheetItem);
        }
        Map<String, String> advertiseActiveMap = new HashMap<>();
        advertiseActiveMap.put("预约-PC", "360SEM,360品专,百度SEM,百度SEM-同台展现,垂直媒体,搜狗SEM,搜狗品专");
        advertiseActiveMap.put("预约-WAP", "360SEM,360品专,99DSP,百度SEM,百度SEM-同台展现,百度原生,垂直媒体,广点通,今日头条APP,神马搜索,搜狗SEM,搜狗品专,一点资讯");
        advertiseActiveMap.put("测保-PC", "百度SEM");
        advertiseActiveMap.put("测保-WAP", "FY,ZH,百度原生,垂直媒体,广点通,今日头条APP,小米");
        // 输出广告活动明细
        String[] advertiseActiveList;
        for (String report : reportArray) {
            advertiseActiveList = advertiseActiveMap.get(report).split(",");
            for (String advertiseActive : advertiseActiveList) {


                paramPagerVo = new HuaxiaFlowReportParamPagerVo();
                paramPagerVo.setChooseDate(paramVo.getChooseDate());

                arrays = report.split("-");
                paramPagerVo.setDataSource(arrays[0]);
                paramPagerVo.setPlatformName(arrays[1]);
                paramPagerVo.setAdvertiseActive(advertiseActive);
                if ("百度SEM".equals(advertiseActive)) {
                    paramPagerVo.setAdvertiseActive("百度SEM,百度表单");
                }

                sheetName = "月汇总-" + report + "-" + advertiseActive;
                sheetNameList.add(sheetName);
                flowReportList = huaxiaFlowReportService.findListByMonthGroup(paramPagerVo);
                huaxiaReportParamVo = new HuaxiaReportParamVo();
                BeanUtils.copyProperties(paramPagerVo, huaxiaReportParamVo);
                detailItemVoList = transformDetailItem2DownloadVoListByMonth(huaxiaReportParamVo, flowReportList);

                dataList = new ArrayList<>();
                for (HuaxiaReportDetailItemVo detailItemVo : detailItemVoList) {
                    rowValueList = new ArrayList<>();
                    // to-do
                    rowValueList.add(detailItemVo.getDate());
                    rowValueList.add(detailItemVo.getDataSource());
                    rowValueList.add(detailItemVo.getPlatformName());
                    rowValueList.add(detailItemVo.getAdvertiseActive());
                    rowValueList.add(detailItemVo.getDirectTransformCount() + "");

                    rowValueList.add(detailItemVo.getBrowseCount() + "");
                    rowValueList.add(detailItemVo.getClickCount() + "");
                    rowValueList.add(detailItemVo.getArriveCount() + "");
                    rowValueList.add(detailItemVo.getArriveUserCount() + "");
                    rowValueList.add(detailItemVo.getArriveRate());
                    rowValueList.add(detailItemVo.getAgainCount() + "");
                    rowValueList.add(detailItemVo.getAgainRate());
                    rowValueList.add(detailItemVo.getAverageStayTime() + "");

                    rowValueList.add(detailItemVo.getMediaShowCount() == null ? "" : detailItemVo.getMediaShowCount().toString());
                    rowValueList.add(detailItemVo.getMediaClickCount() == null ? "" : detailItemVo.getMediaClickCount().toString());
                    rowValueList.add(detailItemVo.getMediaClickRate());
                    rowValueList.add(detailItemVo.getCpc());
                    rowValueList.add(detailItemVo.getCpm());
                    rowValueList.add(detailItemVo.getConsumeAmount());

                    rowValueList.add(detailItemVo.getDirectTransformCost());
                    dataList.add(rowValueList);
                }

                excelSheetItem = new ExcelSheetItem();
                excelSheetItem.setColumnNameList(columnNameList);
                excelSheetItem.setCellValueList(dataList);
                sheetContentMap.put(sheetName, excelSheetItem);


                sheetName = "日汇总-" + report + "-" + advertiseActive;
                sheetNameList.add(sheetName);
                flowReportList = huaxiaFlowReportService.findListByParamGroup(paramPagerVo);
                huaxiaReportParamVo = new HuaxiaReportParamVo();
                BeanUtils.copyProperties(paramPagerVo, huaxiaReportParamVo);
                detailItemVoList = transformDetailItem2DownloadVoListByDate(huaxiaReportParamVo, flowReportList);

                dataList = new ArrayList<>();
                for (HuaxiaReportDetailItemVo detailItemVo : detailItemVoList) {
                    rowValueList = new ArrayList<>();
                    // to-do
                    rowValueList.add(detailItemVo.getDate());
                    rowValueList.add(detailItemVo.getDataSource());
                    rowValueList.add(detailItemVo.getPlatformName());
                    rowValueList.add(detailItemVo.getAdvertiseActive());
                    rowValueList.add(detailItemVo.getDirectTransformCount() + "");

                    rowValueList.add(detailItemVo.getBrowseCount() + "");
                    rowValueList.add(detailItemVo.getClickCount() + "");
                    rowValueList.add(detailItemVo.getArriveCount() + "");
                    rowValueList.add(detailItemVo.getArriveUserCount() + "");
                    rowValueList.add(detailItemVo.getArriveRate());
                    rowValueList.add(detailItemVo.getAgainCount() + "");
                    rowValueList.add(detailItemVo.getAgainRate());
                    rowValueList.add(detailItemVo.getAverageStayTime() + "");

                    rowValueList.add(detailItemVo.getMediaShowCount() == null ? "" : detailItemVo.getMediaShowCount().toString());
                    rowValueList.add(detailItemVo.getMediaClickCount() == null ? "" : detailItemVo.getMediaClickCount().toString());
                    rowValueList.add(detailItemVo.getMediaClickRate());
                    rowValueList.add(detailItemVo.getCpc());
                    rowValueList.add(detailItemVo.getCpm());
                    rowValueList.add(detailItemVo.getConsumeAmount());

                    rowValueList.add(detailItemVo.getDirectTransformCost());
                    dataList.add(rowValueList);
                }

                excelSheetItem = new ExcelSheetItem();
                excelSheetItem.setColumnNameList(columnNameList);
                excelSheetItem.setCellValueList(dataList);
                sheetContentMap.put(sheetName, excelSheetItem);
            }
        }

        ExcelExportConfigation excelConfigation = new ExcelExportConfigation();
        excelConfigation.setFileName(fileName);
        excelConfigation.setSheetNameList(sheetNameList);
        excelConfigation.setSheetContentMap(sheetContentMap);
        ExcelExportHelper.exportExcel2Response(excelConfigation, response);
    }




    private void initDashboardParam(HuaxiaReportParamVo paramVo){
        if(paramVo == null){
            paramVo = new HuaxiaReportParamVo();
        }
        if(StringUtils.isEmpty(paramVo.getChooseDate())){
            String endDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
            String startDate = DateUtils.calculateDate(new Date(), Calendar.YEAR, -1, "yyyy-MM") + "01";
            paramVo.setChooseDate(startDate + " - " + endDate);
        }
        if(StringUtils.isEmpty(paramVo.getDataSource())){
            paramVo.setDataSource(FeeTypeEnum.FEE_TYPE_RESERVE.getText());
        }
    }
    private List<HuaxiaReportSummaryVo> transformSummary2VoList(
            HuaxiaReportParamVo paramVo, List<HuaxiaDataReport> dataReportList){
        MediaItem mediaItem;
        HuaxiaFlowReport flowReport;
        HuaxiaReportSummaryVo summaryVo;
        Map<String, Object> reserve;
        List<HuaxiaReportSummaryVo> dataReportVoList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        // 获取华夏日报-基础流量数据汇总数据
        Map<String, HuaxiaFlowReport> flowReportMap = huaxiaFlowReportService.findMapByParamGroup(paramVo);
        // 获取媒体数据表中的数据汇总数据
        Map<String, MediaItem> mediaItemMap = mediaItemService.findMapByParamGroup(paramVo);
        // 获取广告直接转化数
        Map<String, Map<String, Object>> reserveMap = flowReserveService.findMapByParamGroup(paramVo);
        // 获取合作方提供的保费数据
        Map<String, Double> insuranceFeeMap = new HashMap<>();
        PermiumsParamPagerVo paramPagerVo = new PermiumsParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setColumnFieldIds("date,insuranceFee");
        paramPagerVo.setDateField("date");
        List<Permiums> permiumsList = permiumsService.findListByParam(paramPagerVo);
        for (Permiums permiums : permiumsList) {
            if (StringUtils.hasText(permiums.getInsuranceFee()) && !"-".equals(permiums.getInsuranceFee())) {
                insuranceFeeMap.put(permiums.getDate(), Double.parseDouble(permiums.getInsuranceFee()));
            } else {
                insuranceFeeMap.put(permiums.getDate(), 0.00d);
            }
        }

        String speedCost;
        Long directTransformCount;
        // 组装结果数据
        for(HuaxiaDataReport dataReport : dataReportList){
            summaryVo = new HuaxiaReportSummaryVo();
            BeanUtils.copyProperties(dataReport, summaryVo);
            summaryVo.setWapCount(dataReport.getWapCount() - dataReport.getWeixinCount());
            summaryVo.setValidArrange(dataReport.getValidCount() + dataReport.getFlagCount());
            summaryVo.setTotalCount(dataReport.getPcCount() + dataReport.getWapCount());
            if (FeeTypeEnum.FEE_TYPE_RESERVE.getText().equals(paramVo.getDataSource())) {
                summaryVo.setMiniTotalCount(dataReport.getPcCount() + dataReport.getWapCount() - dataReport.getWeixinCount());
            } else if (FeeTypeEnum.FEE_TYPE_ESTIMATE.getText().equals(paramVo.getDataSource())) {
                summaryVo.setMiniTotalCount(dataReport.getPcCount() + dataReport.getWapCount() - dataReport.getXiaomiCount());
            }

            // 广告直接转化数
            directTransformCount = 0L;
            reserve = reserveMap.get(dataReport.getDate() + "|" + paramVo.getDataSource());
            if (reserve != null) {
                directTransformCount = (Long)reserve.get("reserveCount");
            }
            summaryVo.setDirectTransformCount(directTransformCount.intValue());
            // 广告直接转化数

            // 整合华夏日报-基础流量数据
            flowReport = flowReportMap.get(dataReport.getDate() + "|" + paramVo.getDataSource());
            if (flowReport != null) {
                summaryVo.setBrowseCount(flowReport.getBrowseCount());
                summaryVo.setClickCount(flowReport.getClickCount());
                summaryVo.setArriveCount(flowReport.getArriveCount());
                summaryVo.setArriveUserCount(flowReport.getArriveUserCount());
                summaryVo.setAgainCount(flowReport.getAgainCount());
                summaryVo.setAverageStayTime(String.valueOf(flowReport.getAverageStayTime()));
                if (flowReport.getClickCount() == null || flowReport.getClickCount() == 0) {
                    summaryVo.setArriveRate(decimalFormat.format(0) + "%");
                } else {
                    summaryVo.setArriveRate(decimalFormat.format(flowReport.getArriveCount() * 100d / flowReport.getClickCount()) + "%");
                }
                if (flowReport.getArriveCount() == null || flowReport.getArriveCount() == 0) {
                    summaryVo.setAgainRate(decimalFormat.format(0) + "%");
                } else {
                    summaryVo.setAgainRate(decimalFormat.format(flowReport.getAgainCount() * 100d / flowReport.getArriveCount()) + "%");
                }
            }
            // 整合华夏日报-基础流量数据

            // 整合媒体数据表中的数据

            mediaItem = mediaItemMap.get(dataReport.getDate() + "|" + paramVo.getDataSource());
            if (mediaItem != null) {
                summaryVo.setMediaShowCount(mediaItem.getShowCount());
                summaryVo.setMediaClickCount(mediaItem.getClickCount());
                speedCost = mediaItem.getSpeedCost();
                if (StringUtils.isEmpty(speedCost) || "-".equals(speedCost)) {
                    speedCost = "0";
                }

                summaryVo.setConsumeAmount(decimalFormat.format(Double.parseDouble(speedCost)));
                if (mediaItem.getClickCount() == null || mediaItem.getClickCount() == 0) {
                    summaryVo.setCpc(decimalFormat.format(0));
                } else {
                    summaryVo.setCpc(decimalFormat.format(Double.parseDouble(speedCost) / mediaItem.getClickCount()));
                }
                if (mediaItem.getShowCount() == null || mediaItem.getShowCount() == 0) {
                    summaryVo.setMediaClickRate(decimalFormat.format(0) + "%");
                    summaryVo.setCpm(decimalFormat.format(0));
                } else {
                    summaryVo.setMediaClickRate(decimalFormat.format(mediaItem.getClickCount() * 100.00d / mediaItem.getShowCount()) + "%");
                    summaryVo.setCpm(decimalFormat.format(Double.parseDouble(speedCost) * 1000 / mediaItem.getShowCount()));
                }
            }
            // 整合媒体数据表中的数据
            if (directTransformCount != 0 && StringUtils.hasText(summaryVo.getConsumeAmount())) {
                summaryVo.setDirectTransformCost(decimalFormat.format(Double.parseDouble(summaryVo.getConsumeAmount()) / directTransformCount));
            }
            if (summaryVo.getMiniTotalCount() != null && summaryVo.getMiniTotalCount() != 0 && StringUtils.hasText(summaryVo.getConsumeAmount())) {
                summaryVo.setTotalTransformCost(decimalFormat.format(Double.parseDouble(summaryVo.getConsumeAmount()) / summaryVo.getMiniTotalCount()));
            }
            if (insuranceFeeMap.get(dataReport.getDate()) != null && FeeTypeEnum.FEE_TYPE_RESERVE.getText().equals(paramVo.getDataSource())) {
                summaryVo.setInsuranceAmount(decimalFormat.format(insuranceFeeMap.get(dataReport.getDate())));
            }

            dataReportVoList.add(summaryVo);
        }
        return dataReportVoList;
    }
    private List<HuaxiaReportSummaryVo> transformSummary2VoListByMonth(
            HuaxiaReportParamVo paramVo, List<HuaxiaDataReport> dataReportList){
        Map<String, Object> mediaItem;
        Map<String, Object> flowReport;
        HuaxiaReportSummaryVo summaryVo;
        Map<String, Object> reserve;
        List<HuaxiaReportSummaryVo> dataReportVoList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        // 获取华夏日报-基础流量数据汇总数据
        Map<String, Map<String, Object>> flowReportMap = huaxiaFlowReportService.findMapByMonthGroup(paramVo);
        // 获取媒体数据表中的数据汇总数据
        Map<String, Map<String, Object>> mediaItemMap = mediaItemService.findMapByMonthGroup(paramVo);
        // 获取广告直接转化数
        Map<String, Map<String, Object>> reserveMap = flowReserveService.findMapByMonthGroup(paramVo);
        // 获取合作方提供的保费数据
        Map<String, Double> insuranceFeeMap = new HashMap<>();
        PermiumsParamPagerVo paramPagerVo = new PermiumsParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setColumnFieldIds("date,insuranceFee");
        paramPagerVo.setDateField("date");
        List<Permiums> permiumsList = permiumsService.findListByParam(paramPagerVo);
        String dateKey;
        Double insuranceFee = 0.00d;
        for (Permiums permiums : permiumsList) {
            dateKey = DateUtils.dateFormatTransform(permiums.getDate(), "yyyy-MM-dd", "yyyy-MM");

            insuranceFee = 0.00d;
            if (insuranceFeeMap.get(dateKey) != null) {
                insuranceFee = insuranceFeeMap.get(dateKey);
            }
            if (StringUtils.hasText(permiums.getInsuranceFee()) && !"-".equals(permiums.getInsuranceFee())) {
                insuranceFee += Double.parseDouble(permiums.getInsuranceFee());
            }
            insuranceFeeMap.put(dateKey, insuranceFee);
        }

        Long directTransformCount;
        Integer clickCount, arriveCount, againCount;
        Integer mediaShowCount, mediaClickCount;
        Double speedCost;
        // 组装结果数据
        for(HuaxiaDataReport dataReport : dataReportList){
            summaryVo = new HuaxiaReportSummaryVo();
            BeanUtils.copyProperties(dataReport, summaryVo);
            summaryVo.setWapCount(dataReport.getWapCount() - dataReport.getWeixinCount());
            summaryVo.setValidArrange(dataReport.getValidCount() + dataReport.getFlagCount());
            summaryVo.setTotalCount(dataReport.getPcCount() + dataReport.getWapCount());
            if (FeeTypeEnum.FEE_TYPE_RESERVE.getText().equals(paramVo.getDataSource())) {
                summaryVo.setMiniTotalCount(dataReport.getPcCount() + dataReport.getWapCount() - dataReport.getWeixinCount());
            } else if (FeeTypeEnum.FEE_TYPE_ESTIMATE.getText().equals(paramVo.getDataSource())) {
                summaryVo.setMiniTotalCount(dataReport.getPcCount() + dataReport.getWapCount() - dataReport.getXiaomiCount());
            }

            // 广告直接转化数
            directTransformCount = 0L;
            reserve = reserveMap.get(dataReport.getDate() + "|" + paramVo.getDataSource());
            if (reserve != null) {
                directTransformCount = (Long)reserve.get("reserveCount");
            }
            summaryVo.setDirectTransformCount(directTransformCount.intValue());
            // 广告直接转化数

            // 整合华夏日报-基础流量数据
            flowReport = flowReportMap.get(dataReport.getDate() + "|" + paramVo.getDataSource());
            if (flowReport != null) {
                if (flowReport.get("clickCount") == null) {
                    clickCount = 0;
                } else {
                    clickCount = ((BigDecimal)flowReport.get("clickCount")).intValue();
                }
                if (flowReport.get("arriveCount") == null) {
                    arriveCount = 0;
                } else {
                    arriveCount = ((BigDecimal)flowReport.get("arriveCount")).intValue();
                }
                if (flowReport.get("againCount") == null) {
                    againCount = 0;
                } else {
                    againCount = ((BigDecimal)flowReport.get("againCount")).intValue();
                }
                summaryVo.setClickCount(clickCount);
                summaryVo.setArriveCount(arriveCount);
                summaryVo.setAgainCount(againCount);
                summaryVo.setBrowseCount(((BigDecimal)flowReport.get("browseCount")).intValue());
                summaryVo.setArriveUserCount(((BigDecimal)flowReport.get("arriveUserCount")).intValue());
                summaryVo.setAverageStayTime(((BigDecimal)flowReport.get("averageStayTime")).setScale(2,BigDecimal.ROUND_HALF_DOWN).toPlainString());
                if (clickCount == 0) {
                    summaryVo.setArriveRate(decimalFormat.format(0) + "%");
                } else {
                    summaryVo.setArriveRate(decimalFormat.format(arriveCount * 100d / clickCount) + "%");
                }
                if (arriveCount == 0) {
                    summaryVo.setAgainRate(decimalFormat.format(0) + "%");
                } else {
                    summaryVo.setAgainRate(decimalFormat.format(againCount * 100d / arriveCount) + "%");
                }
            }
            // 整合华夏日报-基础流量数据

            // 整合媒体数据表中的数据
            mediaItem = mediaItemMap.get(dataReport.getDate() + "|" + paramVo.getDataSource());
            if (mediaItem != null) {
                if (mediaItem.get("showCount") == null) {
                    mediaShowCount = 0;
                } else {
                    mediaShowCount = ((BigDecimal)mediaItem.get("showCount")).intValue();
                }
                if (mediaItem.get("clickCount") == null) {
                    mediaClickCount = 0;
                } else {
                    mediaClickCount = ((BigDecimal)mediaItem.get("clickCount")).intValue();
                }
                if (mediaItem.get("speedCost") == null) {
                    speedCost = 0d;
                } else {
                    speedCost = (Double)mediaItem.get("speedCost");
                }

                summaryVo.setConsumeAmount(decimalFormat.format(speedCost));
                if (mediaClickCount == 0) {
                    summaryVo.setMediaClickCount(0);
                    summaryVo.setCpc(decimalFormat.format(0));
                } else {
                    summaryVo.setMediaClickCount(mediaClickCount);
                    summaryVo.setCpc(decimalFormat.format(speedCost / mediaClickCount));
                }
                if (mediaShowCount == 0) {
                    summaryVo.setMediaShowCount(0);
                    summaryVo.setMediaClickRate(mediaClickCount + "%");
                    summaryVo.setCpm(decimalFormat.format(0));
                } else {
                    summaryVo.setMediaShowCount(mediaShowCount);
                    summaryVo.setMediaClickRate(decimalFormat.format(mediaClickCount * 100.00d / mediaShowCount) + "%");
                    summaryVo.setCpm(decimalFormat.format(speedCost * 1000 / mediaShowCount));
                }
            }
            // 整合媒体数据表中的数据
            if (directTransformCount != 0 && StringUtils.hasText(summaryVo.getConsumeAmount())) {
                summaryVo.setDirectTransformCost(decimalFormat.format(Double.parseDouble(summaryVo.getConsumeAmount()) / directTransformCount));
            }
            if (summaryVo.getMiniTotalCount() != null && summaryVo.getMiniTotalCount() != 0 && StringUtils.hasText(summaryVo.getConsumeAmount())) {
                summaryVo.setTotalTransformCost(decimalFormat.format(Double.parseDouble(summaryVo.getConsumeAmount()) / summaryVo.getMiniTotalCount()));
            }
            if (insuranceFeeMap.get(dataReport.getDate()) != null && FeeTypeEnum.FEE_TYPE_RESERVE.getText().equals(paramVo.getDataSource())) {
                summaryVo.setInsuranceAmount(decimalFormat.format(insuranceFeeMap.get(dataReport.getDate())));
            }

            dataReportVoList.add(summaryVo);
        }
        return dataReportVoList;
    }
    private List<HuaxiaReportDetailItemVo> transformDetailItem2PageViewVoList(
            HuaxiaReportParamVo paramVo, List<HuaxiaFlowReport> dataReportList){
        MediaItem mediaItem;
        HuaxiaReportDetailItemVo detailItemVo;
        Map<String, Object> reserve;
        List<HuaxiaReportDetailItemVo> dataReportVoList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        // 获取媒体数据表中的数据汇总数据
        Map<String, MediaItem> mediaItemMap;
        // 获取广告直接转化数
        Map<String, Map<String, Object>> reserveMap;

        StringBuilder unionKey;
        Long directTransformCount;
        // 组装结果数据
        for(HuaxiaFlowReport huaxiaFlowReport : dataReportList){
            detailItemVo = new HuaxiaReportDetailItemVo();
            BeanUtils.copyProperties(huaxiaFlowReport, detailItemVo);

            paramVo = new HuaxiaReportParamVo();
            paramVo.setChooseDate(huaxiaFlowReport.getDate() + " - " + huaxiaFlowReport.getDate());
            paramVo.setDataSource(huaxiaFlowReport.getDataSource());
            paramVo.setPlatformName(huaxiaFlowReport.getPlatformName());
            paramVo.setAdvertiseActive(huaxiaFlowReport.getAdvertiseActive());
            mediaItemMap = mediaItemService.findMapByParamGroup(paramVo);
            reserveMap = flowReserveService.findMapByParamGroup(paramVo);

            unionKey = new StringBuilder("");
            unionKey.append(huaxiaFlowReport.getDate()).append("|");
            unionKey.append(huaxiaFlowReport.getDataSource());
            if (StringUtils.hasText(huaxiaFlowReport.getPlatformName())) {
                unionKey.append("|").append(huaxiaFlowReport.getPlatformName());
            }
            if (StringUtils.hasText(huaxiaFlowReport.getAdvertiseActive()) && !"百度SEM".equals(huaxiaFlowReport.getAdvertiseActive())) {
                unionKey.append("|").append(huaxiaFlowReport.getAdvertiseActive());
            }

            // 广告直接转化数
            directTransformCount = 0L;
            reserve = reserveMap.get(unionKey.toString());
            if (reserve != null) {
                directTransformCount = (Long)reserve.get("reserveCount");
            }
            detailItemVo.setDirectTransformCount(directTransformCount.intValue());
            // 广告直接转化数

            // 整合华夏日报-基础流量数据
            if (detailItemVo.getClickCount() == null || detailItemVo.getClickCount() == 0) {
                detailItemVo.setArriveRate(decimalFormat.format(0) + "%");
            } else {
                detailItemVo.setArriveRate(decimalFormat.format(detailItemVo.getArriveCount() * 100d / detailItemVo.getClickCount()) + "%");
            }
            if (detailItemVo.getArriveCount() == null || detailItemVo.getArriveCount() == 0) {
                detailItemVo.setAgainRate(decimalFormat.format(0) + "%");
            } else {
                detailItemVo.setAgainRate(decimalFormat.format(detailItemVo.getAgainCount() * 100d / detailItemVo.getArriveCount()) + "%");
            }
            // 整合华夏日报-基础流量数据

            // 整合媒体数据表中的数据
            mediaItem = mediaItemMap.get(unionKey.toString());
            if (mediaItem != null) {
                detailItemVo.setMediaShowCount(mediaItem.getShowCount());
                detailItemVo.setMediaClickCount(mediaItem.getClickCount());
                detailItemVo.setConsumeAmount(decimalFormat.format(Double.parseDouble(mediaItem.getSpeedCost())));
                if (mediaItem.getClickCount() == null || mediaItem.getClickCount() == 0) {
                    detailItemVo.setCpc(decimalFormat.format(0));
                } else {
                    detailItemVo.setCpc(decimalFormat.format(Double.parseDouble(mediaItem.getSpeedCost()) / mediaItem.getClickCount()));
                }
                if (mediaItem.getShowCount() == null || mediaItem.getShowCount() == 0) {
                    detailItemVo.setMediaClickRate(decimalFormat.format(0) + "%");
                    detailItemVo.setCpm(decimalFormat.format(0));
                } else {
                    detailItemVo.setMediaClickRate(decimalFormat.format(mediaItem.getClickCount() * 100.00d / mediaItem.getShowCount()) + "%");
                    detailItemVo.setCpm(decimalFormat.format(Double.parseDouble(mediaItem.getSpeedCost()) * 1000 / mediaItem.getShowCount()));
                }
            }
            // 整合媒体数据表中的数据
            if (directTransformCount != 0 && StringUtils.hasText(detailItemVo.getConsumeAmount())) {
                detailItemVo.setDirectTransformCost(decimalFormat.format(Double.parseDouble(detailItemVo.getConsumeAmount()) / directTransformCount));
            }

            dataReportVoList.add(detailItemVo);
        }
        return dataReportVoList;
    }
    private List<HuaxiaReportDetailItemVo> transformDetailItem2DownloadVoListByDate(
            HuaxiaReportParamVo paramVo, List<HuaxiaFlowReport> dataReportList){
        MediaItem mediaItem;
        HuaxiaReportDetailItemVo detailItemVo;
        Map<String, Object> reserve;
        List<HuaxiaReportDetailItemVo> dataReportVoList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        // 获取媒体数据表中的数据汇总数据
        Map<String, MediaItem> mediaItemMap = mediaItemService.findMapByParamGroup(paramVo);
        // 获取广告直接转化数
        Map<String, Map<String, Object>> reserveMap = flowReserveService.findMapByParamGroup(paramVo);

        StringBuilder unionKey;
        Long directTransformCount;
        // 组装结果数据
        for(HuaxiaFlowReport huaxiaFlowReport : dataReportList){
            detailItemVo = new HuaxiaReportDetailItemVo();
            BeanUtils.copyProperties(huaxiaFlowReport, detailItemVo);
            detailItemVo.setAdvertiseActive(paramVo.getAdvertiseActive());

            unionKey = new StringBuilder("");
            unionKey.append(huaxiaFlowReport.getDate()).append("|");
            unionKey.append(huaxiaFlowReport.getDataSource());
            if (StringUtils.hasText(huaxiaFlowReport.getPlatformName())) {
                unionKey.append("|").append(huaxiaFlowReport.getPlatformName());
            }
            if (StringUtils.hasText(huaxiaFlowReport.getAdvertiseActive()) && !"百度SEM".equals(huaxiaFlowReport.getAdvertiseActive())) {
                unionKey.append("|").append(huaxiaFlowReport.getAdvertiseActive());
            }

            // 广告直接转化数
            directTransformCount = 0L;
            reserve = reserveMap.get(unionKey.toString());
            if (reserve != null) {
                directTransformCount = (Long)reserve.get("reserveCount");
            }
            detailItemVo.setDirectTransformCount(directTransformCount.intValue());
            // 广告直接转化数

            // 整合华夏日报-基础流量数据
            if (detailItemVo.getClickCount() == null || detailItemVo.getClickCount() == 0) {
                detailItemVo.setArriveRate(decimalFormat.format(0) + "%");
            } else {
                detailItemVo.setArriveRate(decimalFormat.format(detailItemVo.getArriveCount() * 100d / detailItemVo.getClickCount()) + "%");
            }
            if (detailItemVo.getArriveCount() == null || detailItemVo.getArriveCount() == 0) {
                detailItemVo.setAgainRate(decimalFormat.format(0) + "%");
            } else {
                detailItemVo.setAgainRate(decimalFormat.format(detailItemVo.getAgainCount() * 100d / detailItemVo.getArriveCount()) + "%");
            }
            // 整合华夏日报-基础流量数据

            // 整合媒体数据表中的数据
            mediaItem = mediaItemMap.get(unionKey.toString());
            if (mediaItem != null) {
                detailItemVo.setMediaShowCount(mediaItem.getShowCount());
                detailItemVo.setMediaClickCount(mediaItem.getClickCount());
                detailItemVo.setConsumeAmount(decimalFormat.format(Double.parseDouble(mediaItem.getSpeedCost())));
                if (mediaItem.getClickCount() == null || mediaItem.getClickCount() == 0) {
                    detailItemVo.setCpc(decimalFormat.format(0));
                } else {
                    detailItemVo.setCpc(decimalFormat.format(Double.parseDouble(mediaItem.getSpeedCost()) / mediaItem.getClickCount()));
                }
                if (mediaItem.getShowCount() == null || mediaItem.getShowCount() == 0) {
                    detailItemVo.setMediaClickRate(decimalFormat.format(0) + "%");
                    detailItemVo.setCpm(decimalFormat.format(0));
                } else {
                    detailItemVo.setMediaClickRate(decimalFormat.format(mediaItem.getClickCount() * 100.00d / mediaItem.getShowCount()) + "%");
                    detailItemVo.setCpm(decimalFormat.format(Double.parseDouble(mediaItem.getSpeedCost()) * 1000 / mediaItem.getShowCount()));
                }
            }
            // 整合媒体数据表中的数据
            if (directTransformCount != 0 && StringUtils.hasText(detailItemVo.getConsumeAmount())) {
                detailItemVo.setDirectTransformCost(decimalFormat.format(Double.parseDouble(detailItemVo.getConsumeAmount()) / directTransformCount));
            }

            dataReportVoList.add(detailItemVo);
        }
        return dataReportVoList;
    }
    private List<HuaxiaReportDetailItemVo> transformDetailItem2DownloadVoListByMonth(
            HuaxiaReportParamVo paramVo, List<HuaxiaFlowReport> dataReportList){
        Map<String, Object> mediaItem;
        HuaxiaReportDetailItemVo detailItemVo;
        Map<String, Object> reserve;
        List<HuaxiaReportDetailItemVo> dataReportVoList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        // 获取媒体数据表中的数据汇总数据
        Map<String, Map<String, Object>> mediaItemMap = mediaItemService.findMapByMonthGroup(paramVo);
        // 获取广告直接转化数
        Map<String, Map<String, Object>> reserveMap = flowReserveService.findMapByMonthGroup(paramVo);

        StringBuilder unionKey;
        Long directTransformCount;
        Integer showCount, clickCount;
        Double speedCost;
        // 组装结果数据
        for(HuaxiaFlowReport huaxiaFlowReport : dataReportList){
            detailItemVo = new HuaxiaReportDetailItemVo();
            BeanUtils.copyProperties(huaxiaFlowReport, detailItemVo);
            detailItemVo.setAdvertiseActive(paramVo.getAdvertiseActive());

            unionKey = new StringBuilder("");
            unionKey.append(huaxiaFlowReport.getDate());
            unionKey.append("|");
            unionKey.append(huaxiaFlowReport.getDataSource());
            if (StringUtils.hasText(huaxiaFlowReport.getPlatformName())) {
                unionKey.append("|").append(huaxiaFlowReport.getPlatformName());
            }
            if (StringUtils.hasText(huaxiaFlowReport.getAdvertiseActive())) {
                unionKey.append("|").append(huaxiaFlowReport.getAdvertiseActive());
            }

            // 广告直接转化数
            directTransformCount = 0L;
            reserve = reserveMap.get(unionKey.toString());
            if (reserve != null) {
                directTransformCount = (Long)reserve.get("reserveCount");
            }
            detailItemVo.setDirectTransformCount(directTransformCount.intValue());
            // 广告直接转化数

            // 整合华夏日报-基础流量数据
            if (detailItemVo.getClickCount() == null || detailItemVo.getClickCount() == 0) {
                detailItemVo.setArriveRate(decimalFormat.format(0) + "%");
            } else {
                detailItemVo.setArriveRate(decimalFormat.format(detailItemVo.getArriveCount() * 100d / detailItemVo.getClickCount()) + "%");
            }
            if (detailItemVo.getArriveCount() == null || detailItemVo.getArriveCount() == 0) {
                detailItemVo.setAgainRate(decimalFormat.format(0) + "%");
            } else {
                detailItemVo.setAgainRate(decimalFormat.format(detailItemVo.getAgainCount() * 100d / detailItemVo.getArriveCount()) + "%");
            }
            // 整合华夏日报-基础流量数据

            // 整合媒体数据表中的数据
            mediaItem = mediaItemMap.get(unionKey.toString());
            if (mediaItem != null) {
                if (mediaItem.get("showCount") == null) {
                    showCount = 0;
                } else {
                    showCount = ((BigDecimal)mediaItem.get("showCount")).intValue();
                }
                if (mediaItem.get("clickCount") == null) {
                    clickCount = 0;
                } else {
                    clickCount = ((BigDecimal)mediaItem.get("clickCount")).intValue();
                }
                if (mediaItem.get("speedCost") == null) {
                    speedCost = 0d;
                } else {
                    speedCost = (Double)mediaItem.get("speedCost");
                }

                detailItemVo.setConsumeAmount(decimalFormat.format(speedCost));
                if (clickCount == 0) {
                    detailItemVo.setMediaClickCount(0);
                    detailItemVo.setCpc(decimalFormat.format(0));
                } else {
                    detailItemVo.setMediaClickCount(clickCount);
                    detailItemVo.setCpc(decimalFormat.format(speedCost / clickCount));
                }
                if (showCount == 0) {
                    detailItemVo.setMediaShowCount(0);
                    detailItemVo.setMediaClickRate(clickCount + "%");
                    detailItemVo.setCpm(decimalFormat.format(0));
                } else {
                    detailItemVo.setMediaShowCount(showCount);
                    detailItemVo.setMediaClickRate(decimalFormat.format(clickCount * 100.00d / showCount) + "%");
                    detailItemVo.setCpm(decimalFormat.format(speedCost * 1000 / showCount));
                }
            }
            // 整合媒体数据表中的数据
            if (directTransformCount != 0 && StringUtils.hasText(detailItemVo.getConsumeAmount())) {
                detailItemVo.setDirectTransformCost(decimalFormat.format(Double.parseDouble(detailItemVo.getConsumeAmount()) / directTransformCount));
            }

            dataReportVoList.add(detailItemVo);
        }
        return dataReportVoList;
    }
}
