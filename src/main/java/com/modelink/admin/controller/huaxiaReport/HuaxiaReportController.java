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
import com.modelink.reservation.enums.FeeTypeEnum;
import com.modelink.reservation.service.FlowReserveService;
import com.modelink.reservation.service.HuaxiaDataReportService;
import com.modelink.reservation.service.HuaxiaFlowReportService;
import com.modelink.reservation.service.MediaItemService;
import com.modelink.reservation.vo.HuaxiaDataReportParamPagerVo;
import com.modelink.reservation.vo.HuaxiaFlowReportParamPagerVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping("/admin/huaxiaReport")
public class HuaxiaReportController {

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
        List<HuaxiaReportDetailItemVo> summaryVoList = transformDetailItem2VoList(paramVo, dataReportList);

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
        columnNameList.add("华夏有效数据（下发）");
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

        ExcelSheetItem excelSheetItem;
        List<String> sheetNameList = new ArrayList<>();
        Map<String, ExcelSheetItem> sheetContentMap = new HashMap<>();
        for (String report : reportArray) {
            sheetNameList.add(report);

            paramPagerVo = new HuaxiaDataReportParamPagerVo();
            paramPagerVo.setChooseDate(paramVo.getChooseDate());
            paramPagerVo.setDataSource(report);
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
                rowValueList.add(summaryVo.getValidArrange() + "");
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

                rowValueList.add(summaryVo.getMediaShowCount() + "");
                rowValueList.add(summaryVo.getMediaClickCount() + "");
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
            sheetContentMap.put(report, excelSheetItem);
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
        String fileName = "华夏日报-明细表-" + DateUtils.formatDate(new Date(), "yyyy-MM-dd");
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
        ExcelSheetItem excelSheetItem;
        List<String> sheetNameList = new ArrayList<>();
        Map<String, ExcelSheetItem> sheetContentMap = new HashMap<>();
        for (String report : reportArray) {
            sheetNameList.add(report + "汇总");

            paramPagerVo = new HuaxiaFlowReportParamPagerVo();
            paramPagerVo.setChooseDate(paramVo.getChooseDate());

            arrays = report.split("-");
            paramPagerVo.setDataSource(arrays[0]);
            paramPagerVo.setPlatformName(arrays[1]);
            paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
            flowReportList = huaxiaFlowReportService.findListByParamGroup(paramPagerVo);
            huaxiaReportParamVo = new HuaxiaReportParamVo();
            BeanUtils.copyProperties(paramPagerVo, huaxiaReportParamVo);
            detailItemVoList = transformDetailItem2VoList(huaxiaReportParamVo, flowReportList);

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

                rowValueList.add(detailItemVo.getMediaShowCount() + "");
                rowValueList.add(detailItemVo.getMediaClickCount() + "");
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
            sheetContentMap.put(report + "汇总", excelSheetItem);
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
            String startDate = DateUtils.formatDate(new Date(), "yyyy-MM") + "-01";
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

        int directTransformCount;
        // 组装结果数据
        for(HuaxiaDataReport dataReport : dataReportList){
            summaryVo = new HuaxiaReportSummaryVo();
            BeanUtils.copyProperties(dataReport, summaryVo);
            summaryVo.setValidArrange(dataReport.getValidCount() + dataReport.getFlagCount());
            summaryVo.setTotalCount(dataReport.getPcCount() + dataReport.getWapCount());
            if (FeeTypeEnum.FEE_TYPE_RESERVE.getText().equals(paramVo.getDataSource())) {
                summaryVo.setMiniTotalCount(dataReport.getPcCount() + dataReport.getWapCount() - dataReport.getWeixinCount());
            } else if (FeeTypeEnum.FEE_TYPE_ESTIMATE.getText().equals(paramVo.getDataSource())) {
                summaryVo.setMiniTotalCount(dataReport.getPcCount() + dataReport.getWapCount() - dataReport.getXiaomiCount());
            }

            // 广告直接转化数
            directTransformCount = 0;
            reserve = reserveMap.get(dataReport.getDate());
            if (reserve != null) {
                directTransformCount = (int)reserve.get("reserveCount");
            }
            summaryVo.setDirectTransformCount(directTransformCount);
            // 广告直接转化数

            // 整合华夏日报-基础流量数据
            flowReport = flowReportMap.get(dataReport.getDate());
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
            mediaItem = mediaItemMap.get(dataReport.getDate());
            if (mediaItem != null) {
                summaryVo.setMediaShowCount(mediaItem.getShowCount());
                summaryVo.setMediaClickCount(mediaItem.getClickCount());
                summaryVo.setConsumeAmount(mediaItem.getSpeedCost());
                if (mediaItem.getClickCount() == null || mediaItem.getClickCount() == 0) {
                    summaryVo.setCpc(decimalFormat.format(0));
                } else {
                    summaryVo.setCpc(decimalFormat.format(Double.parseDouble(mediaItem.getSpeedCost()) / mediaItem.getClickCount()));
                }
                if (mediaItem.getShowCount() == null || mediaItem.getShowCount() == 0) {
                    summaryVo.setMediaClickRate(decimalFormat.format(0) + "%");
                    summaryVo.setCpm(decimalFormat.format(0));
                } else {
                    summaryVo.setMediaClickRate(decimalFormat.format(mediaItem.getClickCount() * 100.00d / mediaItem.getShowCount()) + "%");
                    summaryVo.setCpm(decimalFormat.format(Double.parseDouble(mediaItem.getSpeedCost()) / mediaItem.getShowCount()));
                }
            }
            // 整合媒体数据表中的数据
            if (directTransformCount != 0 && StringUtils.hasText(summaryVo.getConsumeAmount())) {
                summaryVo.setDirectTransformCost(decimalFormat.format(Double.parseDouble(summaryVo.getConsumeAmount()) / directTransformCount));
            }
            if (summaryVo.getMiniTotalCount() != null && summaryVo.getMiniTotalCount() != 0 && StringUtils.hasText(summaryVo.getConsumeAmount())) {
                summaryVo.setTotalTransformCost(decimalFormat.format(Double.parseDouble(summaryVo.getConsumeAmount()) / summaryVo.getMiniTotalCount()));
            }

            dataReportVoList.add(summaryVo);
        }
        return dataReportVoList;
    }
    private List<HuaxiaReportDetailItemVo> transformDetailItem2VoList(
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

        String unionKey;
        Long directTransformCount;
        // 组装结果数据
        for(HuaxiaFlowReport huaxiaFlowReport : dataReportList){
            detailItemVo = new HuaxiaReportDetailItemVo();
            BeanUtils.copyProperties(huaxiaFlowReport, detailItemVo);

            unionKey = huaxiaFlowReport.getDate() + "|"
                    + huaxiaFlowReport.getPlatformName()
                    + "|" + huaxiaFlowReport.getAdvertiseActive();
            // 广告直接转化数
            directTransformCount = 0L;
            reserve = reserveMap.get(unionKey);
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
            mediaItem = mediaItemMap.get(unionKey);
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
                    detailItemVo.setCpm(decimalFormat.format(Double.parseDouble(mediaItem.getSpeedCost()) / mediaItem.getShowCount()));
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
