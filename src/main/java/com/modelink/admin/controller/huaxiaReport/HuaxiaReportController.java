package com.modelink.admin.controller.huaxiaReport;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.huaxiaReport.HuaxiaReportDetailItemVo;
import com.modelink.admin.vo.huaxiaReport.HuaxiaReportParamVo;
import com.modelink.admin.vo.huaxiaReport.HuaxiaReportSummaryVo;
import com.modelink.common.excel.*;
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
import org.apache.poi.ss.usermodel.IndexedColors;
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
        List<ColumnDetail> columnDetailList = new ArrayList<>();
        columnDetailList.add(new ColumnDetail("日期", 0, "string"));
        columnDetailList.add(new ColumnDetail("数据来源", 0, "string"));
        columnDetailList.add(new ColumnDetail("有效数据（下发）", 0, "integer"));
        columnDetailList.add(new ColumnDetail("营销标记", 0, "integer"));
        columnDetailList.add(new ColumnDetail("总转化/测保转化（包含小米）", 3000, "integer"));
        columnDetailList.add(new ColumnDetail("总转化（不含微信）/测保转化（不含小米）", 3000, "integer"));
        columnDetailList.add(new ColumnDetail("PC", 0, "integer"));
        columnDetailList.add(new ColumnDetail("WAP", 0, "integer"));
        columnDetailList.add(new ColumnDetail("微信转化", 0, "integer"));
        columnDetailList.add(new ColumnDetail("直接转化数", 0, "integer"));

        columnDetailList.add(new ColumnDetail("浏览量", 0, "integer"));
        columnDetailList.add(new ColumnDetail("点击量", 0, "integer"));
        columnDetailList.add(new ColumnDetail("到达量", 0, "integer"));
        columnDetailList.add(new ColumnDetail("到达用户", 0, "integer"));
        columnDetailList.add(new ColumnDetail("到达率（%）", 0, "percent"));
        columnDetailList.add(new ColumnDetail("二跳量", 0, "integer"));
        columnDetailList.add(new ColumnDetail("二跳率（%）", 0, "percent"));
        columnDetailList.add(new ColumnDetail("平均停留时间", 0, "double"));

        columnDetailList.add(new ColumnDetail("展示数", 0, "integer"));
        columnDetailList.add(new ColumnDetail("点击数", 0, "integer"));
        columnDetailList.add(new ColumnDetail("点击率（%）", 0, "percent"));
        columnDetailList.add(new ColumnDetail("CPC（元）", 0, "double"));
        columnDetailList.add(new ColumnDetail("CPM（元）", 0, "double"));
        columnDetailList.add(new ColumnDetail("总花费（元）", 0, "double"));

        columnDetailList.add(new ColumnDetail("直接转化成本", 0, "double"));
        columnDetailList.add(new ColumnDetail("总转化成本（不含微信）", 0, "double"));
        columnDetailList.add(new ColumnDetail("保费（元）", 0, "double"));

        // 创建Excel 数据
        List<CellDetail> rowValueList;
        List<List<CellDetail>> dataList;

        HuaxiaReportParamVo huaxiaReportParamVo;
        HuaxiaDataReportParamPagerVo paramPagerVo;
        List<HuaxiaReportSummaryVo> summaryVoList;
        List<HuaxiaDataReport> dataReportList;
        String[] reportArray = reports.split(",");

        String sheetName;
        SheetDetail sheetDetail;
        ExcelSheetItem excelSheetItem;
        List<ExcelSheetItem> sheetItemList;
        List<SheetDetail> sheetDetailList = new ArrayList<>();
        Map<String, List<ExcelSheetItem>> sheetContentMap = new HashMap<>();

        int validCount, flagCount, totalCount, miniTotalCount;
        int pcCount, wapCount, weixinCount, directTransformCount;
        int browseCount, clickCount, arriveCount, arriveUserCount, againCount;
        double arriveRate, againRate, mediaClickRate, averageStayTime;
        int mediaShowCount, mediaClickCount;
        double cpc, cpm, consumeAmount, insuranceAmount;
        for (String report : reportArray) {
            sheetItemList = new ArrayList<>();


            paramPagerVo = new HuaxiaDataReportParamPagerVo();
            paramPagerVo.setChooseDate(paramVo.getChooseDate());
            paramPagerVo.setDataSource(report);

            // 汇总月报
            sheetName = report;
            sheetDetail = new SheetDetail();
            sheetDetail.setName(sheetName);
            sheetDetail.setFreezePane("3,1,3,1");
            sheetDetailList.add(sheetDetail);
            dataReportList = huaxiaDataReportService.findListByMonthGroup(paramPagerVo);
            huaxiaReportParamVo = new HuaxiaReportParamVo();
            BeanUtils.copyProperties(paramPagerVo, huaxiaReportParamVo);
            summaryVoList = transformSummary2VoListByMonth(huaxiaReportParamVo, dataReportList);

            validCount = 0;
            flagCount = 0;
            totalCount = 0;
            miniTotalCount = 0;
            pcCount = 0;
            wapCount = 0;
            weixinCount = 0;
            directTransformCount = 0;
            browseCount = 0;
            clickCount = 0;
            arriveCount = 0;
            arriveUserCount = 0;
            againCount = 0;
            averageStayTime = 0;
            mediaShowCount = 0;
            mediaClickCount = 0;
            consumeAmount = 0d;
            insuranceAmount = 0d;

            dataList = new ArrayList<>();
            for (HuaxiaReportSummaryVo summaryVo : summaryVoList) {


                if (summaryVo.getValidCount() != null) {
                    validCount += summaryVo.getValidCount();
                }
                if (summaryVo.getFlagCount() != null) {
                    flagCount += summaryVo.getFlagCount();
                }
                if (summaryVo.getTotalCount() != null) {
                    totalCount += summaryVo.getTotalCount();
                }
                if (summaryVo.getMiniTotalCount() != null) {
                    miniTotalCount += summaryVo.getMiniTotalCount();
                }
                if (summaryVo.getPcCount() != null) {
                    pcCount += summaryVo.getPcCount();
                }
                if (summaryVo.getWapCount() != null) {
                    wapCount += summaryVo.getWapCount();
                }
                if (summaryVo.getWeixinCount() != null) {
                    weixinCount += summaryVo.getWeixinCount();
                }
                if (summaryVo.getDirectTransformCount() != null) {
                    directTransformCount += summaryVo.getDirectTransformCount();
                }
                if (summaryVo.getBrowseCount() != null) {
                    browseCount += summaryVo.getBrowseCount();
                }
                if (summaryVo.getClickCount() != null) {
                    clickCount += summaryVo.getClickCount();
                }
                if (summaryVo.getArriveCount() != null) {
                    arriveCount += summaryVo.getArriveCount();
                }
                if (summaryVo.getArriveUserCount() != null) {
                    arriveUserCount += summaryVo.getArriveUserCount();
                }
                if (summaryVo.getAgainCount() != null) {
                    againCount += summaryVo.getAgainCount();
                }
                if (summaryVo.getAverageStayTime() != null) {
                    averageStayTime += Double.parseDouble(summaryVo.getAverageStayTime());
                }
                if (summaryVo.getMediaShowCount() != null) {
                    mediaShowCount += summaryVo.getMediaShowCount();
                }
                if (summaryVo.getMediaClickCount() != null) {
                    mediaClickCount += summaryVo.getMediaClickCount();
                }
                if (summaryVo.getConsumeAmount() != null) {
                    consumeAmount += Double.parseDouble(summaryVo.getConsumeAmount());
                }
                if (summaryVo.getInsuranceAmount() != null) {
                    insuranceAmount += Double.parseDouble(summaryVo.getInsuranceAmount());
                }

                rowValueList = new ArrayList<>();
                // to-do
                rowValueList.add(new CellDetail(summaryVo.getDate()));
                rowValueList.add(new CellDetail(summaryVo.getDataSource()));
                rowValueList.add(new CellDetail(summaryVo.getValidCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getFlagCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getTotalCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getMiniTotalCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getPcCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getWapCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getWeixinCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getDirectTransformCount() + ""));

                rowValueList.add(new CellDetail(summaryVo.getBrowseCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getClickCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getArriveCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getArriveUserCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getArriveRate()));
                rowValueList.add(new CellDetail(summaryVo.getAgainCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getAgainRate()));
                rowValueList.add(new CellDetail(summaryVo.getAverageStayTime()));

                rowValueList.add(new CellDetail(summaryVo.getMediaShowCount() == null ? "" : summaryVo.getMediaShowCount().toString()));
                rowValueList.add(new CellDetail(summaryVo.getMediaClickCount() == null ? "" : summaryVo.getMediaClickCount().toString()));
                rowValueList.add(new CellDetail(summaryVo.getMediaClickRate()));
                rowValueList.add(new CellDetail(summaryVo.getCpc()));
                rowValueList.add(new CellDetail(summaryVo.getCpm()));
                rowValueList.add(new CellDetail(summaryVo.getConsumeAmount()));

                rowValueList.add(new CellDetail(summaryVo.getDirectTransformCost()));
                rowValueList.add(new CellDetail(summaryVo.getTotalTransformCost()));
                rowValueList.add(new CellDetail(summaryVo.getInsuranceAmount()));
                dataList.add(rowValueList);
            }
            // 增加合计行
            rowValueList = new ArrayList<>();
            // to-do
            rowValueList.add(new CellDetail("合计"));
            rowValueList.add(new CellDetail(report));
            rowValueList.add(new CellDetail(validCount + ""));
            rowValueList.add(new CellDetail(flagCount + ""));
            rowValueList.add(new CellDetail(totalCount + ""));
            rowValueList.add(new CellDetail(miniTotalCount + ""));
            rowValueList.add(new CellDetail(pcCount + ""));
            rowValueList.add(new CellDetail(wapCount + ""));
            rowValueList.add(new CellDetail(weixinCount + ""));
            rowValueList.add(new CellDetail(directTransformCount + ""));

            rowValueList.add(new CellDetail(browseCount + ""));
            rowValueList.add(new CellDetail(clickCount + ""));
            rowValueList.add(new CellDetail(arriveCount + ""));
            rowValueList.add(new CellDetail(arriveUserCount + ""));
            rowValueList.add(new CellDetail(clickCount == 0 ? "0" : arriveCount * 1d / clickCount + ""));
            rowValueList.add(new CellDetail(againCount + ""));
            rowValueList.add(new CellDetail(arriveCount == 0 ? "0" : againCount * 1d / arriveCount + ""));
            rowValueList.add(new CellDetail(averageStayTime / summaryVoList.size() + ""));


            rowValueList.add(new CellDetail(mediaShowCount + ""));
            rowValueList.add(new CellDetail(mediaClickCount + ""));
            rowValueList.add(new CellDetail(mediaShowCount == 0 ? "0" : mediaClickCount * 1d / mediaShowCount + ""));
            rowValueList.add(new CellDetail(mediaClickCount == 0 ? "0" : consumeAmount * 1d / mediaClickCount + ""));
            rowValueList.add(new CellDetail(mediaShowCount == 0 ? "0" : consumeAmount * 1000d / mediaShowCount + ""));
            rowValueList.add(new CellDetail(consumeAmount + ""));

            rowValueList.add(new CellDetail(directTransformCount == 0 ? "0" : consumeAmount * 1d / directTransformCount + ""));
            rowValueList.add(new CellDetail(miniTotalCount == 0 ? "0" : consumeAmount * 1d / miniTotalCount + ""));
            rowValueList.add(new CellDetail(insuranceAmount + ""));
            dataList.add(rowValueList);

            excelSheetItem = new ExcelSheetItem();
            excelSheetItem.setColumnDetailList(columnDetailList);
            excelSheetItem.setCellValueList(dataList);
            sheetItemList.add(excelSheetItem);

            // 汇总日报
            dataReportList = huaxiaDataReportService.findListByParam(paramPagerVo);
            huaxiaReportParamVo = new HuaxiaReportParamVo();
            BeanUtils.copyProperties(paramPagerVo, huaxiaReportParamVo);
            summaryVoList = transformSummary2VoList(huaxiaReportParamVo, dataReportList);

            validCount = 0;
            flagCount = 0;
            totalCount = 0;
            miniTotalCount = 0;
            pcCount = 0;
            wapCount = 0;
            weixinCount = 0;
            directTransformCount = 0;
            browseCount = 0;
            clickCount = 0;
            arriveCount = 0;
            arriveUserCount = 0;
            againCount = 0;
            averageStayTime = 0;
            mediaShowCount = 0;
            mediaClickCount = 0;
            cpc = 0d;
            cpm = 0d;
            consumeAmount = 0d;
            insuranceAmount = 0d;
            dataList = new ArrayList<>();
            for (HuaxiaReportSummaryVo summaryVo : summaryVoList) {

                if (summaryVo.getValidCount() != null) {
                    validCount += summaryVo.getValidCount();
                }
                if (summaryVo.getFlagCount() != null) {
                    flagCount += summaryVo.getFlagCount();
                }
                if (summaryVo.getTotalCount() != null) {
                    totalCount += summaryVo.getTotalCount();
                }
                if (summaryVo.getMiniTotalCount() != null) {
                    miniTotalCount += summaryVo.getMiniTotalCount();
                }
                if (summaryVo.getPcCount() != null) {
                    pcCount += summaryVo.getPcCount();
                }
                if (summaryVo.getWapCount() != null) {
                    wapCount += summaryVo.getWapCount();
                }
                if (summaryVo.getWeixinCount() != null) {
                    weixinCount += summaryVo.getWeixinCount();
                }
                if (summaryVo.getDirectTransformCount() != null) {
                    directTransformCount += summaryVo.getDirectTransformCount();
                }
                if (summaryVo.getBrowseCount() != null) {
                    browseCount += summaryVo.getBrowseCount();
                }
                if (summaryVo.getClickCount() != null) {
                    clickCount += summaryVo.getClickCount();
                }
                if (summaryVo.getArriveCount() != null) {
                    arriveCount += summaryVo.getArriveCount();
                }
                if (summaryVo.getArriveUserCount() != null) {
                    arriveUserCount += summaryVo.getArriveUserCount();
                }
                if (summaryVo.getAgainCount() != null) {
                    againCount += summaryVo.getAgainCount();
                }
                if (summaryVo.getAverageStayTime() != null) {
                    averageStayTime += Double.parseDouble(summaryVo.getAverageStayTime());
                }
                if (summaryVo.getMediaShowCount() != null) {
                    mediaShowCount += summaryVo.getMediaShowCount();
                }
                if (summaryVo.getMediaClickCount() != null) {
                    mediaClickCount += summaryVo.getMediaClickCount();
                }
                if (summaryVo.getConsumeAmount() != null) {
                    consumeAmount += Double.parseDouble(summaryVo.getConsumeAmount());
                }
                if (summaryVo.getCpc() != null) {
                    cpc += Double.parseDouble(summaryVo.getCpc());
                }
                if (summaryVo.getCpm() != null) {
                    cpm += Double.parseDouble(summaryVo.getCpm());
                }

                rowValueList = new ArrayList<>();
                // to-do
                if ("星期六".equals(DateUtils.printWeekValue(summaryVo.getDate(), "yyyy-MM-dd"))
                || "星期日".equals(DateUtils.printWeekValue(summaryVo.getDate(), "yyyy-MM-dd"))) {
                    rowValueList.add(new CellDetail(IndexedColors.RED.index, summaryVo.getDate()));
                }else {
                    rowValueList.add(new CellDetail(summaryVo.getDate()));
                }
                rowValueList.add(new CellDetail(summaryVo.getDataSource()));
                rowValueList.add(new CellDetail(summaryVo.getValidCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getFlagCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getTotalCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getMiniTotalCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getPcCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getWapCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getWeixinCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getDirectTransformCount() + ""));

                rowValueList.add(new CellDetail(summaryVo.getBrowseCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getClickCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getArriveCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getArriveUserCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getArriveRate()));
                rowValueList.add(new CellDetail(summaryVo.getAgainCount() + ""));
                rowValueList.add(new CellDetail(summaryVo.getAgainRate()));
                rowValueList.add(new CellDetail(summaryVo.getAverageStayTime()));

                rowValueList.add(new CellDetail(summaryVo.getMediaShowCount() == null ? "" : summaryVo.getMediaShowCount().toString()));
                rowValueList.add(new CellDetail(summaryVo.getMediaClickCount() == null ? "" : summaryVo.getMediaClickCount().toString()));
                rowValueList.add(new CellDetail(summaryVo.getMediaClickRate()));
                rowValueList.add(new CellDetail(summaryVo.getCpc()));
                rowValueList.add(new CellDetail(summaryVo.getCpm()));
                rowValueList.add(new CellDetail(summaryVo.getConsumeAmount()));

                rowValueList.add(new CellDetail(summaryVo.getDirectTransformCost()));
                rowValueList.add(new CellDetail(summaryVo.getTotalTransformCost()));
                rowValueList.add(new CellDetail(summaryVo.getInsuranceAmount()));
                dataList.add(rowValueList);
            }
            // 增加合计行
            rowValueList = new ArrayList<>();
            // to-do
            rowValueList.add(new CellDetail("合计"));
            rowValueList.add(new CellDetail(report));
            rowValueList.add(new CellDetail(validCount + ""));
            rowValueList.add(new CellDetail(flagCount + ""));
            rowValueList.add(new CellDetail(totalCount + ""));
            rowValueList.add(new CellDetail(miniTotalCount + ""));
            rowValueList.add(new CellDetail(pcCount + ""));
            rowValueList.add(new CellDetail(wapCount + ""));
            rowValueList.add(new CellDetail(weixinCount + ""));
            rowValueList.add(new CellDetail(directTransformCount + ""));

            rowValueList.add(new CellDetail(browseCount + ""));
            rowValueList.add(new CellDetail(clickCount + ""));
            rowValueList.add(new CellDetail(arriveCount + ""));
            rowValueList.add(new CellDetail(arriveUserCount + ""));
            rowValueList.add(new CellDetail(clickCount == 0 ? "0" : arriveCount * 1d / clickCount + ""));
            rowValueList.add(new CellDetail(againCount + ""));
            rowValueList.add(new CellDetail(arriveCount == 0 ? "0" : againCount * 1d / arriveCount + ""));
            rowValueList.add(new CellDetail(averageStayTime / summaryVoList.size() + ""));

            rowValueList.add(new CellDetail(mediaShowCount + ""));
            rowValueList.add(new CellDetail(mediaClickCount + ""));
            rowValueList.add(new CellDetail(mediaShowCount == 0 ? "0" : mediaClickCount * 1d / mediaShowCount + ""));
            rowValueList.add(new CellDetail(mediaClickCount == 0 ? "0" : consumeAmount * 1d / mediaClickCount + ""));
            rowValueList.add(new CellDetail(mediaShowCount == 0 ? "0" : consumeAmount * 1000d / mediaShowCount + ""));
            rowValueList.add(new CellDetail(consumeAmount + ""));

            rowValueList.add(new CellDetail(directTransformCount == 0 ? "0" : consumeAmount * 1d / directTransformCount + ""));
            rowValueList.add(new CellDetail(miniTotalCount == 0 ? "0" : consumeAmount * 1d / miniTotalCount + ""));
            rowValueList.add(new CellDetail(insuranceAmount + ""));
            dataList.add(rowValueList);

            excelSheetItem = new ExcelSheetItem();
            excelSheetItem.setColumnDetailList(columnDetailList);
            excelSheetItem.setCellValueList(dataList);
            sheetItemList.add(excelSheetItem);
            sheetContentMap.put(sheetName, sheetItemList);
        }

        ExcelExportConfigation excelConfigation = new ExcelExportConfigation();
        excelConfigation.setFileName(fileName);
        excelConfigation.setSheetDetailList(sheetDetailList);
        excelConfigation.setSheetContentMap(sheetContentMap);
        ExcelExportHelper.exportExcel2Response(excelConfigation, response);
    }

    @RequestMapping("/detailItemDownload")
    public void detailItemDownload(HttpServletResponse response, HuaxiaReportParamVo paramVo, String reports) throws Exception {
        initDashboardParam(paramVo);
        // 创建文件名称
        String fileName = "华夏日报-" + DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        // 创建Excel表格列名称
        List<ColumnDetail> columnDetailList = new ArrayList<>();
        columnDetailList.add(new ColumnDetail("日期", 0, "string"));
        columnDetailList.add(new ColumnDetail("数据来源", 0, "string"));
        columnDetailList.add(new ColumnDetail("渠道归属", 0, "string"));
        columnDetailList.add(new ColumnDetail("广告活动", 0, "string"));
        columnDetailList.add(new ColumnDetail("直接转化数", 0, "integer"));

        columnDetailList.add(new ColumnDetail("浏览量", 0, "integer"));
        columnDetailList.add(new ColumnDetail("点击量", 0, "integer"));
        columnDetailList.add(new ColumnDetail("到达量", 0, "integer"));
        columnDetailList.add(new ColumnDetail("到达用户", 0, "integer"));
        columnDetailList.add(new ColumnDetail("到达率（%）", 0, "percent"));
        columnDetailList.add(new ColumnDetail("二跳量", 0, "integer"));
        columnDetailList.add(new ColumnDetail("二跳率（%）", 0, "percent"));
        columnDetailList.add(new ColumnDetail("平均停留时间", 0, "double"));

        columnDetailList.add(new ColumnDetail("展示数", 0, "integer"));
        columnDetailList.add(new ColumnDetail("点击数", 0, "integer"));
        columnDetailList.add(new ColumnDetail("点击率（%）", 0, "percent"));
        columnDetailList.add(new ColumnDetail("CPC（元）", 0, "double"));
        columnDetailList.add(new ColumnDetail("CPM（元）", 0, "double"));
        columnDetailList.add(new ColumnDetail("总花费（元）", 0, "double"));

        columnDetailList.add(new ColumnDetail("直接转化成本", 0, "double"));

        // 创建Excel 数据
        List<CellDetail> rowValueList;
        List<List<CellDetail>> dataList;

        HuaxiaReportParamVo huaxiaReportParamVo;
        HuaxiaFlowReportParamPagerVo paramPagerVo;
        List<HuaxiaReportDetailItemVo> detailItemVoList;
        List<HuaxiaFlowReport> flowReportList;
        String[] reportArray = reports.split(",");

        String[] arrays;
        String sheetName;
        SheetDetail sheetDetail;
        ExcelSheetItem excelSheetItem;
        List<ExcelSheetItem> sheetItemList;
        List<SheetDetail> sheetDetailList = new ArrayList<>();
        Map<String, List<ExcelSheetItem>> sheetContentMap = new HashMap<>();

        int directTransformCount;
        double averageStayTime, consumeAmount;
        int browseCount, clickCount, arriveCount, arriveUserCount, againCount;
        int mediaShowCount, mediaClickCount;
        // 输出汇总的EXCEL数据
        for (String report : reportArray) {
            sheetItemList = new ArrayList<>();

            paramPagerVo = new HuaxiaFlowReportParamPagerVo();
            paramPagerVo.setChooseDate(paramVo.getChooseDate());

            arrays = report.split("-");
            paramPagerVo.setDataSource(arrays[0]);
            paramPagerVo.setPlatformName(arrays[1]);

            sheetName =  report;
            sheetDetail = new SheetDetail();
            sheetDetail.setName(sheetName);
            sheetDetail.setFreezePane("5,1,5,1");
            sheetDetailList.add(sheetDetail);
            flowReportList = huaxiaFlowReportService.findListByMonthGroup(paramPagerVo);
            huaxiaReportParamVo = new HuaxiaReportParamVo();
            BeanUtils.copyProperties(paramPagerVo, huaxiaReportParamVo);
            detailItemVoList = transformDetailItem2DownloadVoListByMonth(huaxiaReportParamVo, flowReportList);

            directTransformCount = 0;
            browseCount = 0;
            clickCount = 0;
            arriveCount = 0;
            arriveUserCount = 0;
            againCount = 0;
            averageStayTime = 0;
            mediaShowCount = 0;
            mediaClickCount = 0;
            consumeAmount = 0d;

            dataList = new ArrayList<>();
            for (HuaxiaReportDetailItemVo detailItemVo : detailItemVoList) {

                if (detailItemVo.getDirectTransformCount() != null) {
                    directTransformCount += detailItemVo.getDirectTransformCount();
                }
                if (detailItemVo.getBrowseCount() != null) {
                    browseCount += detailItemVo.getBrowseCount();
                }
                if (detailItemVo.getClickCount() != null) {
                    clickCount += detailItemVo.getClickCount();
                }
                if (detailItemVo.getArriveCount() != null) {
                    arriveCount += detailItemVo.getArriveCount();
                }
                if (detailItemVo.getArriveUserCount() != null) {
                    arriveUserCount += detailItemVo.getArriveUserCount();
                }
                if (detailItemVo.getAgainCount() != null) {
                    againCount += detailItemVo.getAgainCount();
                }
                if (detailItemVo.getAverageStayTime() != null) {
                    averageStayTime += detailItemVo.getAverageStayTime();
                }
                if (detailItemVo.getMediaShowCount() != null) {
                    mediaShowCount += detailItemVo.getMediaShowCount();
                }
                if (detailItemVo.getMediaClickCount() != null) {
                    mediaClickCount += detailItemVo.getMediaClickCount();
                }
                if (detailItemVo.getConsumeAmount() != null) {
                    consumeAmount += Double.parseDouble(detailItemVo.getConsumeAmount());
                }

                rowValueList = new ArrayList<>();
                // to-do
                rowValueList.add(new CellDetail(detailItemVo.getDate()));
                rowValueList.add(new CellDetail(detailItemVo.getDataSource()));
                rowValueList.add(new CellDetail(detailItemVo.getPlatformName()));
                rowValueList.add(new CellDetail(detailItemVo.getAdvertiseActive()));
                rowValueList.add(new CellDetail(detailItemVo.getDirectTransformCount() + ""));

                rowValueList.add(new CellDetail(detailItemVo.getBrowseCount() + ""));
                rowValueList.add(new CellDetail(detailItemVo.getClickCount() + ""));
                rowValueList.add(new CellDetail(detailItemVo.getArriveCount() + ""));
                rowValueList.add(new CellDetail(detailItemVo.getArriveUserCount() + ""));
                rowValueList.add(new CellDetail(detailItemVo.getArriveRate()));
                rowValueList.add(new CellDetail(detailItemVo.getAgainCount() + ""));
                rowValueList.add(new CellDetail(detailItemVo.getAgainRate()));
                rowValueList.add(new CellDetail(detailItemVo.getAverageStayTime() + ""));

                rowValueList.add(new CellDetail(detailItemVo.getMediaShowCount() == null ? "" : detailItemVo.getMediaShowCount().toString()));
                rowValueList.add(new CellDetail(detailItemVo.getMediaClickCount() == null ? "" : detailItemVo.getMediaClickCount().toString()));
                rowValueList.add(new CellDetail(detailItemVo.getMediaClickRate()));
                rowValueList.add(new CellDetail(detailItemVo.getCpc()));
                rowValueList.add(new CellDetail(detailItemVo.getCpm()));
                rowValueList.add(new CellDetail(detailItemVo.getConsumeAmount()));

                rowValueList.add(new CellDetail(detailItemVo.getDirectTransformCost()));
                dataList.add(rowValueList);
            }
            rowValueList = new ArrayList<>();
            // to-do
            rowValueList.add(new CellDetail("合计"));
            rowValueList.add(new CellDetail(arrays[0]));
            rowValueList.add(new CellDetail(arrays[1]));
            rowValueList.add(new CellDetail(""));
            rowValueList.add(new CellDetail(directTransformCount + ""));
            rowValueList.add(new CellDetail(browseCount + ""));
            rowValueList.add(new CellDetail(clickCount + ""));
            rowValueList.add(new CellDetail(arriveCount + ""));
            rowValueList.add(new CellDetail(arriveUserCount + ""));
            rowValueList.add(new CellDetail(clickCount == 0 ? "0" : arriveCount * 1d / clickCount + ""));
            rowValueList.add(new CellDetail(againCount + ""));
            rowValueList.add(new CellDetail(arriveCount == 0 ? "0" : againCount * 1d / arriveCount + ""));
            rowValueList.add(new CellDetail(averageStayTime * 1d / detailItemVoList.size() + ""));
            rowValueList.add(new CellDetail(mediaShowCount + ""));
            rowValueList.add(new CellDetail(mediaClickCount + ""));
            rowValueList.add(new CellDetail(mediaShowCount == 0 ? "0" : mediaClickCount * 1d / mediaShowCount + ""));
            rowValueList.add(new CellDetail(mediaClickCount == 0 ? "0" : consumeAmount * 1d / mediaClickCount + ""));
            rowValueList.add(new CellDetail(mediaShowCount == 0 ? "0" : consumeAmount * 1000d / mediaShowCount + ""));
            rowValueList.add(new CellDetail(consumeAmount + ""));
            rowValueList.add(new CellDetail(directTransformCount == 0 ? "0" : consumeAmount * 1d / directTransformCount + ""));
            dataList.add(rowValueList);

            excelSheetItem = new ExcelSheetItem();
            excelSheetItem.setColumnDetailList(columnDetailList);
            excelSheetItem.setCellValueList(dataList);
            sheetItemList.add(excelSheetItem);

            flowReportList = huaxiaFlowReportService.findListByParamGroup(paramPagerVo);
            huaxiaReportParamVo = new HuaxiaReportParamVo();
            BeanUtils.copyProperties(paramPagerVo, huaxiaReportParamVo);
            detailItemVoList = transformDetailItem2DownloadVoListByDate(huaxiaReportParamVo, flowReportList);

            dataList = new ArrayList<>();
            for (HuaxiaReportDetailItemVo detailItemVo : detailItemVoList) {

                if (detailItemVo.getDirectTransformCount() != null) {
                    directTransformCount += detailItemVo.getDirectTransformCount();
                }
                if (detailItemVo.getBrowseCount() != null) {
                    browseCount += detailItemVo.getBrowseCount();
                }
                if (detailItemVo.getClickCount() != null) {
                    clickCount += detailItemVo.getClickCount();
                }
                if (detailItemVo.getArriveCount() != null) {
                    arriveCount += detailItemVo.getArriveCount();
                }
                if (detailItemVo.getArriveUserCount() != null) {
                    arriveUserCount += detailItemVo.getArriveUserCount();
                }
                if (detailItemVo.getAgainCount() != null) {
                    againCount += detailItemVo.getAgainCount();
                }
                if (detailItemVo.getAverageStayTime() != null) {
                    averageStayTime += detailItemVo.getAverageStayTime();
                }
                if (detailItemVo.getMediaShowCount() != null) {
                    mediaShowCount += detailItemVo.getMediaShowCount();
                }
                if (detailItemVo.getMediaClickCount() != null) {
                    mediaClickCount += detailItemVo.getMediaClickCount();
                }
                if (detailItemVo.getConsumeAmount() != null) {
                    consumeAmount += Double.parseDouble(detailItemVo.getConsumeAmount());
                }

                rowValueList = new ArrayList<>();
                if ("星期六".equals(DateUtils.printWeekValue(detailItemVo.getDate(), "yyyy-MM-dd"))
                        || "星期日".equals(DateUtils.printWeekValue(detailItemVo.getDate(), "yyyy-MM-dd"))) {
                    rowValueList.add(new CellDetail(IndexedColors.RED.index, detailItemVo.getDate()));
                }else {
                    rowValueList.add(new CellDetail(detailItemVo.getDate()));
                }
                // to-do
                rowValueList.add(new CellDetail(detailItemVo.getDataSource()));
                rowValueList.add(new CellDetail(detailItemVo.getPlatformName()));
                rowValueList.add(new CellDetail(detailItemVo.getAdvertiseActive()));
                rowValueList.add(new CellDetail(detailItemVo.getDirectTransformCount() + ""));

                rowValueList.add(new CellDetail(detailItemVo.getBrowseCount() + ""));
                rowValueList.add(new CellDetail(detailItemVo.getClickCount() + ""));
                rowValueList.add(new CellDetail(detailItemVo.getArriveCount() + ""));
                rowValueList.add(new CellDetail(detailItemVo.getArriveUserCount() + ""));
                rowValueList.add(new CellDetail(detailItemVo.getArriveRate()));
                rowValueList.add(new CellDetail(detailItemVo.getAgainCount() + ""));
                rowValueList.add(new CellDetail(detailItemVo.getAgainRate()));
                rowValueList.add(new CellDetail(detailItemVo.getAverageStayTime() + ""));

                rowValueList.add(new CellDetail(detailItemVo.getMediaShowCount() == null ? "" : detailItemVo.getMediaShowCount().toString()));
                rowValueList.add(new CellDetail(detailItemVo.getMediaClickCount() == null ? "" : detailItemVo.getMediaClickCount().toString()));
                rowValueList.add(new CellDetail(detailItemVo.getMediaClickRate()));
                rowValueList.add(new CellDetail(detailItemVo.getCpc()));
                rowValueList.add(new CellDetail(detailItemVo.getCpm()));
                rowValueList.add(new CellDetail(detailItemVo.getConsumeAmount()));

                rowValueList.add(new CellDetail(detailItemVo.getDirectTransformCost()));
                dataList.add(rowValueList);
            }
            rowValueList = new ArrayList<>();
            // to-do
            rowValueList.add(new CellDetail("合计"));
            rowValueList.add(new CellDetail(arrays[0]));
            rowValueList.add(new CellDetail(arrays[1]));
            rowValueList.add(new CellDetail(""));
            rowValueList.add(new CellDetail(directTransformCount + ""));
            rowValueList.add(new CellDetail(browseCount + ""));
            rowValueList.add(new CellDetail(clickCount + ""));
            rowValueList.add(new CellDetail(arriveCount + ""));
            rowValueList.add(new CellDetail(arriveUserCount + ""));
            rowValueList.add(new CellDetail(clickCount == 0 ? "0" : arriveCount * 1d / clickCount + ""));
            rowValueList.add(new CellDetail(againCount + ""));
            rowValueList.add(new CellDetail(arriveCount == 0 ? "0" : againCount * 1d / arriveCount + ""));
            rowValueList.add(new CellDetail(averageStayTime * 1d / detailItemVoList.size() + ""));
            rowValueList.add(new CellDetail(mediaShowCount + ""));
            rowValueList.add(new CellDetail(mediaClickCount + ""));
            rowValueList.add(new CellDetail(mediaShowCount == 0 ? "0" : mediaClickCount * 1d / mediaShowCount + ""));
            rowValueList.add(new CellDetail(mediaClickCount == 0 ? "0" : consumeAmount * 1d / mediaClickCount + ""));
            rowValueList.add(new CellDetail(mediaShowCount == 0 ? "0" : consumeAmount * 1000d / mediaShowCount + ""));
            rowValueList.add(new CellDetail(consumeAmount + ""));
            rowValueList.add(new CellDetail(directTransformCount == 0 ? "0" : consumeAmount * 1d / directTransformCount + ""));
            dataList.add(rowValueList);

            excelSheetItem = new ExcelSheetItem();
            excelSheetItem.setColumnDetailList(columnDetailList);
            excelSheetItem.setCellValueList(dataList);
            sheetItemList.add(excelSheetItem);

            sheetContentMap.put(sheetName, sheetItemList);
        }
        Map<String, String> advertiseActiveMap = new HashMap<>();
        advertiseActiveMap.put("预约-PC", "360SEM,360品专,百度SEM,百度SEM-同台展现,垂直媒体,搜狗SEM,搜狗品专");
        advertiseActiveMap.put("预约-WAP", "360SEM,360品专,99DSP,百度SEM,百度SEM-同台展现,百度原生,垂直媒体,广点通,今日头条APP,头条建站,神马搜索,搜狗SEM,搜狗品专,一点资讯");
        advertiseActiveMap.put("测保-PC", "百度SEM");
        advertiseActiveMap.put("测保-WAP", "FY,ZH,百度原生,垂直媒体,广点通,今日头条APP,小米");
        // 输出广告活动明细
        String[] advertiseActiveList;
        for (String report : reportArray) {
            advertiseActiveList = advertiseActiveMap.get(report).split(",");
            for (String advertiseActive : advertiseActiveList) {
                sheetItemList = new ArrayList<>();

                paramPagerVo = new HuaxiaFlowReportParamPagerVo();
                paramPagerVo.setChooseDate(paramVo.getChooseDate());

                arrays = report.split("-");
                paramPagerVo.setDataSource(arrays[0]);
                paramPagerVo.setPlatformName(arrays[1]);
                paramPagerVo.setAdvertiseActive(advertiseActive);
                if ("百度SEM".equals(advertiseActive)) {
                    paramPagerVo.setAdvertiseActive("百度SEM,百度表单");
                }

                sheetName = report + "-" + advertiseActive;
                sheetDetail = new SheetDetail();
                sheetDetail.setName(sheetName);
                sheetDetail.setFreezePane("5,1,5,1");
                sheetDetailList.add(sheetDetail);
                flowReportList = huaxiaFlowReportService.findListByMonthGroup(paramPagerVo);
                huaxiaReportParamVo = new HuaxiaReportParamVo();
                BeanUtils.copyProperties(paramPagerVo, huaxiaReportParamVo);
                detailItemVoList = transformDetailItem2DownloadVoListByMonth(huaxiaReportParamVo, flowReportList);

                dataList = new ArrayList<>();
                for (HuaxiaReportDetailItemVo detailItemVo : detailItemVoList) {
                    rowValueList = new ArrayList<>();
                    // to-do
                    rowValueList.add(new CellDetail(detailItemVo.getDate()));
                    rowValueList.add(new CellDetail(detailItemVo.getDataSource()));
                    rowValueList.add(new CellDetail(detailItemVo.getPlatformName()));
                    rowValueList.add(new CellDetail(detailItemVo.getAdvertiseActive()));
                    rowValueList.add(new CellDetail(detailItemVo.getDirectTransformCount() + ""));

                    rowValueList.add(new CellDetail(detailItemVo.getBrowseCount() + ""));
                    rowValueList.add(new CellDetail(detailItemVo.getClickCount() + ""));
                    rowValueList.add(new CellDetail(detailItemVo.getArriveCount() + ""));
                    rowValueList.add(new CellDetail(detailItemVo.getArriveUserCount() + ""));
                    rowValueList.add(new CellDetail(detailItemVo.getArriveRate()));
                    rowValueList.add(new CellDetail(detailItemVo.getAgainCount() + ""));
                    rowValueList.add(new CellDetail(detailItemVo.getAgainRate()));
                    rowValueList.add(new CellDetail(detailItemVo.getAverageStayTime() + ""));

                    rowValueList.add(new CellDetail(detailItemVo.getMediaShowCount() == null ? "" : detailItemVo.getMediaShowCount().toString()));
                    rowValueList.add(new CellDetail(detailItemVo.getMediaClickCount() == null ? "" : detailItemVo.getMediaClickCount().toString()));
                    rowValueList.add(new CellDetail(detailItemVo.getMediaClickRate()));
                    rowValueList.add(new CellDetail(detailItemVo.getCpc()));
                    rowValueList.add(new CellDetail(detailItemVo.getCpm()));
                    rowValueList.add(new CellDetail(detailItemVo.getConsumeAmount()));

                    rowValueList.add(new CellDetail(detailItemVo.getDirectTransformCost()));
                    dataList.add(rowValueList);
                }

                excelSheetItem = new ExcelSheetItem();
                excelSheetItem.setColumnDetailList(columnDetailList);
                excelSheetItem.setCellValueList(dataList);
                sheetItemList.add(excelSheetItem);

                flowReportList = huaxiaFlowReportService.findListByParamGroup(paramPagerVo);
                huaxiaReportParamVo = new HuaxiaReportParamVo();
                BeanUtils.copyProperties(paramPagerVo, huaxiaReportParamVo);
                detailItemVoList = transformDetailItem2DownloadVoListByDate(huaxiaReportParamVo, flowReportList);

                dataList = new ArrayList<>();
                for (HuaxiaReportDetailItemVo detailItemVo : detailItemVoList) {
                    rowValueList = new ArrayList<>();
                    // to-do
                    if ("星期六".equals(DateUtils.printWeekValue(detailItemVo.getDate(), "yyyy-MM-dd"))
                            || "星期日".equals(DateUtils.printWeekValue(detailItemVo.getDate(), "yyyy-MM-dd"))) {
                        rowValueList.add(new CellDetail(IndexedColors.RED.index, detailItemVo.getDate()));
                    }else {
                        rowValueList.add(new CellDetail(detailItemVo.getDate()));
                    }
                    rowValueList.add(new CellDetail(detailItemVo.getDataSource()));
                    rowValueList.add(new CellDetail(detailItemVo.getPlatformName()));
                    rowValueList.add(new CellDetail(detailItemVo.getAdvertiseActive()));
                    rowValueList.add(new CellDetail(detailItemVo.getDirectTransformCount() + ""));

                    rowValueList.add(new CellDetail(detailItemVo.getBrowseCount() + ""));
                    rowValueList.add(new CellDetail(detailItemVo.getClickCount() + ""));
                    rowValueList.add(new CellDetail(detailItemVo.getArriveCount() + ""));
                    rowValueList.add(new CellDetail(detailItemVo.getArriveUserCount() + ""));
                    rowValueList.add(new CellDetail(detailItemVo.getArriveRate()));
                    rowValueList.add(new CellDetail(detailItemVo.getAgainCount() + ""));
                    rowValueList.add(new CellDetail(detailItemVo.getAgainRate()));
                    rowValueList.add(new CellDetail(detailItemVo.getAverageStayTime() + ""));

                    rowValueList.add(new CellDetail(detailItemVo.getMediaShowCount() == null ? "" : detailItemVo.getMediaShowCount().toString()));
                    rowValueList.add(new CellDetail(detailItemVo.getMediaClickCount() == null ? "" : detailItemVo.getMediaClickCount().toString()));
                    rowValueList.add(new CellDetail(detailItemVo.getMediaClickRate()));
                    rowValueList.add(new CellDetail(detailItemVo.getCpc()));
                    rowValueList.add(new CellDetail(detailItemVo.getCpm()));
                    rowValueList.add(new CellDetail(detailItemVo.getConsumeAmount()));

                    rowValueList.add(new CellDetail(detailItemVo.getDirectTransformCost()));
                    dataList.add(rowValueList);
                }

                excelSheetItem = new ExcelSheetItem();
                excelSheetItem.setColumnDetailList(columnDetailList);
                excelSheetItem.setCellValueList(dataList);
                sheetItemList.add(excelSheetItem);
                sheetContentMap.put(sheetName, sheetItemList);
            }
        }

        ExcelExportConfigation excelConfigation = new ExcelExportConfigation();
        excelConfigation.setFileName(fileName);
        excelConfigation.setSheetDetailList(sheetDetailList);
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
        DecimalFormat decimalFormatPercent = new DecimalFormat("#0.0000");
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
                    summaryVo.setArriveRate(decimalFormatPercent.format(0));
                } else {
                    summaryVo.setArriveRate(decimalFormatPercent.format(flowReport.getArriveCount() * 1d / flowReport.getClickCount()));
                }
                if (flowReport.getArriveCount() == null || flowReport.getArriveCount() == 0) {
                    summaryVo.setAgainRate(decimalFormatPercent.format(0));
                } else {
                    summaryVo.setAgainRate(decimalFormatPercent.format(flowReport.getAgainCount() * 1d / flowReport.getArriveCount()));
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
                    summaryVo.setMediaClickRate(decimalFormatPercent.format(0));
                    summaryVo.setCpm(decimalFormat.format(0));
                } else {
                    summaryVo.setMediaClickRate(decimalFormatPercent.format(mediaItem.getClickCount() * 1d / mediaItem.getShowCount()));
                    summaryVo.setCpm(decimalFormat.format(Double.parseDouble(speedCost) * 1000d / mediaItem.getShowCount()));
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
        DecimalFormat decimalFormatPercent = new DecimalFormat("#0.0000");
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
        Double insuranceFee;
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
                    summaryVo.setArriveRate(decimalFormatPercent.format(0));
                } else {
                    summaryVo.setArriveRate(decimalFormatPercent.format(arriveCount * 1d / clickCount));
                }
                if (arriveCount == 0) {
                    summaryVo.setAgainRate(decimalFormatPercent.format(0));
                } else {
                    summaryVo.setAgainRate(decimalFormatPercent.format(againCount * 1d / arriveCount));
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
                    summaryVo.setCpc(decimalFormat.format(speedCost * 1d / mediaClickCount));
                }
                if (mediaShowCount == 0) {
                    summaryVo.setMediaShowCount(0);
                    summaryVo.setMediaClickRate("0");
                    summaryVo.setCpm(decimalFormat.format(0));
                } else {
                    summaryVo.setMediaShowCount(mediaShowCount);
                    summaryVo.setMediaClickRate(decimalFormatPercent.format(mediaClickCount * 1.00d / mediaShowCount));
                    summaryVo.setCpm(decimalFormat.format(speedCost * 1000d / mediaShowCount));
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
        DecimalFormat decimalFormatPercent = new DecimalFormat("#0.0000");

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
                detailItemVo.setArriveRate(decimalFormatPercent.format(0));
            } else {
                detailItemVo.setArriveRate(decimalFormatPercent.format(detailItemVo.getArriveCount() * 1d / detailItemVo.getClickCount()));
            }
            if (detailItemVo.getArriveCount() == null || detailItemVo.getArriveCount() == 0) {
                detailItemVo.setAgainRate(decimalFormatPercent.format(0));
            } else {
                detailItemVo.setAgainRate(decimalFormatPercent.format(detailItemVo.getAgainCount() * 1d / detailItemVo.getArriveCount()));
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
                    detailItemVo.setMediaClickRate(decimalFormatPercent.format(0));
                    detailItemVo.setCpm(decimalFormat.format(0));
                } else {
                    detailItemVo.setMediaClickRate(decimalFormatPercent.format(mediaItem.getClickCount() * 100.00d / mediaItem.getShowCount()));
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
        DecimalFormat decimalFormatPercent = new DecimalFormat("#0.0000");

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
                detailItemVo.setArriveRate(decimalFormatPercent.format(0));
            } else {
                detailItemVo.setArriveRate(decimalFormatPercent.format(detailItemVo.getArriveCount() * 1d / detailItemVo.getClickCount()));
            }
            if (detailItemVo.getArriveCount() == null || detailItemVo.getArriveCount() == 0) {
                detailItemVo.setAgainRate(decimalFormatPercent.format(0));
            } else {
                detailItemVo.setAgainRate(decimalFormatPercent.format(detailItemVo.getAgainCount() * 1d / detailItemVo.getArriveCount()));
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
                    detailItemVo.setMediaClickRate(decimalFormatPercent.format(0));
                    detailItemVo.setCpm(decimalFormat.format(0));
                } else {
                    detailItemVo.setMediaClickRate(decimalFormatPercent.format(mediaItem.getClickCount() * 1d / mediaItem.getShowCount()));
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
        DecimalFormat decimalFormatPercent = new DecimalFormat("#0.0000");

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
                detailItemVo.setArriveRate(decimalFormatPercent.format(0));
            } else {
                detailItemVo.setArriveRate(decimalFormatPercent.format(detailItemVo.getArriveCount() * 1d / detailItemVo.getClickCount()));
            }
            if (detailItemVo.getArriveCount() == null || detailItemVo.getArriveCount() == 0) {
                detailItemVo.setAgainRate(decimalFormatPercent.format(0));
            } else {
                detailItemVo.setAgainRate(decimalFormatPercent.format(detailItemVo.getAgainCount() * 1d / detailItemVo.getArriveCount()));
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
                    detailItemVo.setMediaClickRate("0.00");
                    detailItemVo.setCpm(decimalFormat.format(0));
                } else {
                    detailItemVo.setMediaShowCount(showCount);
                    detailItemVo.setMediaClickRate(decimalFormatPercent.format(clickCount * 1d / showCount));
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
