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
import com.modelink.reservation.bean.HuaxiaDataReport;
import com.modelink.reservation.service.HuaxiaDataReportService;
import com.modelink.reservation.vo.HuaxiaDataReportParamPagerVo;
import com.modelink.reservation.vo.HuaxiaDataReportVo;
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
@RequestMapping("/admin/huaxiaDataReport")
public class HuaxiaDataReportController {

    public static Logger logger = LoggerFactory.getLogger(HuaxiaDataReportController.class);

    @Resource
    private HuaxiaDataReportService huaxiaDataReportService;
    @Resource
    private ExceptionLoggerService exceptionLoggerService;

    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/huaxia-report/data-list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<HuaxiaDataReportVo> list(HuaxiaDataReportParamPagerVo paramPagerVo){
        LayuiResultPagerVo<HuaxiaDataReportVo> layuiResultPagerVo = new LayuiResultPagerVo<>();

        PageInfo<HuaxiaDataReport> pageInfo = huaxiaDataReportService.findPagerByParam(paramPagerVo);
        List<HuaxiaDataReport> huaxiaDataReportList = pageInfo.getList();
        List<HuaxiaDataReportVo> advertiseAnalyseVoList = transformBean2VoList(huaxiaDataReportList);

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
            if(StringUtils.isEmpty(fileName) || !fileName.startsWith("华夏日报-基础数量表")){
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("您导入表格不是华夏日报-基础数量表");
                return resultVo;
            }

            Map<Integer, String> fieldFormatMap = new HashMap<>();
            configation = new ExcelImportConfigation();
            configation.setFieldFormatMap(fieldFormatMap);
            configation.setStartRowNum(1);
            dataList = ExcelImportHelper.importExcel(configation, file.getInputStream());
        } catch (Exception e) {
            logger.error("[huaxiaDataReportController|importExcel]发生异常", e);
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
            if(dataItem.size() < 9){
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
        HuaxiaDataReport huaxiaDataReport;
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
                huaxiaDataReport = new HuaxiaDataReport();
                huaxiaDataReport.setDate(dataItem.get(1));
                huaxiaDataReport.setDataSource(dataItem.get(2));
                huaxiaDataReport = huaxiaDataReportService.findOneByParam(huaxiaDataReport);
                if(huaxiaDataReport == null){
                    exist = false;
                    huaxiaDataReport = new HuaxiaDataReport();
                }else{
                    logger.info("[huaxiaDataReportController|importExcel]重复数据{}", JSON.toJSONString(huaxiaDataReport));
                    ExceptionLogger exceptionLogger = new ExceptionLogger();
                    exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据重复");
                    exceptionLogger.setLoggerType("huaxiaDataReport");
                    exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                    exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                    exceptionLoggerService.save(exceptionLogger);
                }

                // 保存数据
                huaxiaDataReport.setDate(dataItem.get(1));
                huaxiaDataReport.setDataSource(dataItem.get(2));
                huaxiaDataReport.setPcCount(DataUtils.tranform2Integer(dataItem.get(3)));
                huaxiaDataReport.setWapCount(DataUtils.tranform2Integer(dataItem.get(4)));
                huaxiaDataReport.setWeixinCount(DataUtils.tranform2Integer(dataItem.get(5)));
                huaxiaDataReport.setXiaomiCount(DataUtils.tranform2Integer(dataItem.get(6)));
                huaxiaDataReport.setValidCount(DataUtils.tranform2Integer(dataItem.get(7)));
                huaxiaDataReport.setFlagCount(DataUtils.tranform2Integer(dataItem.get(8)));

                if(exist) {
                    huaxiaDataReportService.update(huaxiaDataReport);
                }else {
                    huaxiaDataReportService.insert(huaxiaDataReport);
                    totalCount ++;
                }

            } catch (Exception e) {
                logger.error("[huaxiaDataReportController|importExcel]保存数据发生异常。huaxiaDataReport={}", JSON.toJSONString(dataItem), e);
                ExceptionLogger exceptionLogger = new ExceptionLogger();
                exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据异常");
                exceptionLogger.setLoggerType("huaxiaDataReport");
                exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                exceptionLoggerService.save(exceptionLogger);
            }
        }
        resultVo.setRtnData(totalCount);
        return resultVo;
    }


    private List<HuaxiaDataReportVo> transformBean2VoList(List<HuaxiaDataReport> huaxiaDataReportList){
        HuaxiaDataReportVo huaxiaDataReportVo;
        List<HuaxiaDataReportVo> huaxiaDataReportVoList = new ArrayList<>();
        for(HuaxiaDataReport huaxiaDataReport : huaxiaDataReportList){
            huaxiaDataReportVo = new HuaxiaDataReportVo();
            BeanUtils.copyProperties(huaxiaDataReport, huaxiaDataReportVo);
            huaxiaDataReportVoList.add(huaxiaDataReportVo);
        }
        return huaxiaDataReportVoList;
    }
}
