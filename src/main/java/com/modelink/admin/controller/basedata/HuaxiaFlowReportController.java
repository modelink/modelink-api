package com.modelink.admin.controller.basedata;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.bean.ExceptionLogger;
import com.modelink.admin.service.ExceptionLoggerService;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.excel.ExcelImportConfigation;
import com.modelink.common.excel.ExcelImportHelper;
import com.modelink.common.utils.DataUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.HuaxiaFlowReport;
import com.modelink.reservation.service.HuaxiaFlowReportService;
import com.modelink.reservation.vo.HuaxiaFlowReportParamPagerVo;
import com.modelink.reservation.vo.HuaxiaFlowReportVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("/admin/huaxiaFlowReport")
public class HuaxiaFlowReportController {

    public static Logger logger = LoggerFactory.getLogger(HuaxiaFlowReportController.class);

    @Resource
    private HuaxiaFlowReportService huaxiaFlowReportService;
    @Resource
    private ExceptionLoggerService exceptionLoggerService;

    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/huaxia-report/flow-list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<HuaxiaFlowReportVo> list(HuaxiaFlowReportParamPagerVo paramPagerVo){
        LayuiResultPagerVo<HuaxiaFlowReportVo> layuiResultPagerVo = new LayuiResultPagerVo<>();

        PageInfo<HuaxiaFlowReport> pageInfo = huaxiaFlowReportService.findPagerByParam(paramPagerVo);
        List<HuaxiaFlowReport> huaxiaFlowReportList = pageInfo.getList();
        List<HuaxiaFlowReportVo> advertiseAnalyseVoList = transformBean2VoList(huaxiaFlowReportList);

        layuiResultPagerVo.setTotalCount((int)pageInfo.getTotal());
        layuiResultPagerVo.setRtnList(advertiseAnalyseVoList);
        return layuiResultPagerVo;
    }

    @ResponseBody
    @RequestMapping("/importExcel")
    public ResultVo importExcel(@RequestParam MultipartFile file){
        ResultVo resultVo = new ResultVo();
        List<List<String>> dataList;
        ExcelImportConfigation configation = new ExcelImportConfigation();
        try {
            String fileName = file.getOriginalFilename();
            if(StringUtils.isEmpty(fileName) || !fileName.startsWith("华夏日报-基础流量表")){
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("您导入表格不是华夏日报-基础流量表");
                return resultVo;
            }

            Map<Integer, String> fieldFormatMap = new HashMap<>();
            fieldFormatMap.put(10, "HH:mm:ss");
            configation = new ExcelImportConfigation();
            configation.setFieldFormatMap(fieldFormatMap);
            configation.setStartRowNum(1);
            dataList = ExcelImportHelper.importExcel(configation, file.getInputStream());
        } catch (Exception e) {
            logger.error("[huaxiaFlowReportController|importExcel]发生异常", e);
            resultVo.setRtnCode(RetStatus.Exception.getValue());
            resultVo.setRtnMsg(e.getMessage());
            dataList = null;
        }
        if(dataList == null){
            return resultVo;
        }

        if(dataList.size() <= 0){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("excel中没有符合条件的数据");
            return resultVo;
        }


        // 校验Excel数据是否符合规定
        boolean isFullNull;
        StringBuilder messageBuilder = new StringBuilder();
        int rowIndex = configation.getStartRowNum();
        for(List<String> dataItem : dataList){
            if(dataItem.size() < 11){
                messageBuilder.append("第").append(rowIndex).append("行：数据不足").append(";");
            }
            isFullNull = true;
            for(String dataString : dataItem){
                if(StringUtils.hasText(dataString)){
                    isFullNull = false;
                }
            }
            if(isFullNull){
                continue;
            }

            rowIndex ++;
        }
        if(StringUtils.hasText(messageBuilder.toString())){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(messageBuilder.toString());
            return resultVo;
        }

        // 数据入库
        boolean exist;
        HuaxiaFlowReport huaxiaFlowReport;
        int totalCount = 0;
        for(List<String> dataItem : dataList){

            // 跳过空行
            exist = true;
            isFullNull = true;
            for(String dataString : dataItem){
                if(StringUtils.hasText(dataString)){
                    isFullNull = false;
                }
            }
            if(isFullNull){
                continue;
            }
            try {
                // 重复数据校验
                huaxiaFlowReport = new HuaxiaFlowReport();
                huaxiaFlowReport.setDate(dataItem.get(1));
                huaxiaFlowReport.setDataSource(dataItem.get(2));
                huaxiaFlowReport.setPlatformName(dataItem.get(3).toUpperCase());
                huaxiaFlowReport.setAdvertiseActive(dataItem.get(4).toUpperCase());
                huaxiaFlowReport.setBrowseCount(DataUtils.tranform2Integer(dataItem.get(5)));
                huaxiaFlowReport.setClickCount(DataUtils.tranform2Integer(dataItem.get(6)));
                huaxiaFlowReport.setArriveCount(DataUtils.tranform2Integer(dataItem.get(7)));
                huaxiaFlowReport.setArriveUserCount(DataUtils.tranform2Integer(dataItem.get(8)));
                huaxiaFlowReport.setAgainCount(DataUtils.tranform2Integer(dataItem.get(9)));
                huaxiaFlowReport.setAverageStayTime(DataUtils.timeTranform2Second(dataItem.get(10)));
                huaxiaFlowReport = huaxiaFlowReportService.findOneByParam(huaxiaFlowReport);
                if(huaxiaFlowReport == null){
                    exist = false;
                    huaxiaFlowReport = new HuaxiaFlowReport();
                }else{
                    logger.info("[huaxiaFlowReportController|importExcel]重复数据{}", JSON.toJSONString(huaxiaFlowReport));
                    ExceptionLogger exceptionLogger = new ExceptionLogger();
                    exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据重复");
                    exceptionLogger.setLoggerType("huaxiaFlowReport");
                    exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                    exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                    exceptionLoggerService.save(exceptionLogger);
                }

                // 保存数据
                huaxiaFlowReport.setDate(dataItem.get(1));
                huaxiaFlowReport.setDataSource(dataItem.get(2));
                huaxiaFlowReport.setPlatformName(dataItem.get(3).toUpperCase());
                huaxiaFlowReport.setAdvertiseActive(dataItem.get(4).toUpperCase());
                huaxiaFlowReport.setBrowseCount(DataUtils.tranform2Integer(dataItem.get(5)));
                huaxiaFlowReport.setClickCount(DataUtils.tranform2Integer(dataItem.get(6)));
                huaxiaFlowReport.setArriveCount(DataUtils.tranform2Integer(dataItem.get(7)));
                huaxiaFlowReport.setArriveUserCount(DataUtils.tranform2Integer(dataItem.get(8)));
                huaxiaFlowReport.setAgainCount(DataUtils.tranform2Integer(dataItem.get(9)));
                huaxiaFlowReport.setAverageStayTime(DataUtils.timeTranform2Second(dataItem.get(10)));

                if(exist) {
                    huaxiaFlowReportService.update(huaxiaFlowReport);
                }else {
                    huaxiaFlowReportService.insert(huaxiaFlowReport);
                    totalCount ++;
                }

            } catch (Exception e) {
                logger.error("[huaxiaFlowReportController|importExcel]保存数据发生异常。huaxiaFlowReport={}", JSON.toJSONString(dataItem), e);
                ExceptionLogger exceptionLogger = new ExceptionLogger();
                exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据异常");
                exceptionLogger.setLoggerType("huaxiaFlowReport");
                exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                exceptionLoggerService.save(exceptionLogger);
            }
        }
        resultVo.setRtnData(totalCount);
        return resultVo;
    }


    private List<HuaxiaFlowReportVo> transformBean2VoList(List<HuaxiaFlowReport> huaxiaFlowReportList){
        HuaxiaFlowReportVo huaxiaFlowReportVo;
        List<HuaxiaFlowReportVo> huaxiaFlowReportVoList = new ArrayList<>();
        for(HuaxiaFlowReport huaxiaFlowReport : huaxiaFlowReportList){
            huaxiaFlowReportVo = new HuaxiaFlowReportVo();
            BeanUtils.copyProperties(huaxiaFlowReport, huaxiaFlowReportVo);
            huaxiaFlowReportVoList.add(huaxiaFlowReportVo);
        }
        return huaxiaFlowReportVoList;
    }
}
