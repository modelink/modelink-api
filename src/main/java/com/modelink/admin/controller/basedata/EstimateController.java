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
import com.modelink.reservation.bean.Estimate;
import com.modelink.reservation.service.EstimateService;
import com.modelink.reservation.vo.EstimateParamPagerVo;
import com.modelink.reservation.vo.EstimateVo;
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

/**
 * 保费测算Controller
 */
@Controller
@RequestMapping("/admin/estimate")
public class EstimateController {

    public static Logger logger = LoggerFactory.getLogger(EstimateController.class);

    @Resource
    private EstimateService estimateService;
    @Resource
    private ExceptionLoggerService exceptionLoggerService;

    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/estimate/estimate-list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<EstimateVo> list(EstimateParamPagerVo paramPagerVo){
        LayuiResultPagerVo<EstimateVo> layuiResultPagerVo = new LayuiResultPagerVo<>();

        PageInfo<Estimate> pageInfo = estimateService.findPagerByParam(paramPagerVo);
        List<Estimate> estimateList = pageInfo.getList();
        List<EstimateVo> advertiseAnalyseVoList = transformBean2VoList(estimateList);

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
            if(StringUtils.isEmpty(fileName) || !fileName.startsWith("测保数据表")){
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("您导入表格不是测保数据表");
                return resultVo;
            }

            Map<Integer, String> fieldFormatMap = new HashMap<>();
            configation = new ExcelImportConfigation();
            configation.setFieldFormatMap(fieldFormatMap);
            configation.setStartRowNum(1);
            dataList = ExcelImportHelper.importExcel(configation, file.getInputStream());
        } catch (Exception e) {
            logger.error("[estimateController|importExcel]发生异常", e);
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
            if(dataItem.size() < 20){
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
        Estimate estimate;
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
                estimate = new Estimate();
                estimate.setDate(dataItem.get(1));
                estimate = estimateService.findOneByParam(estimate);
                if(estimate == null){
                    exist = false;
                    estimate = new Estimate();
                }else{
                    logger.info("[estimateController|importExcel]重复数据{}", JSON.toJSONString(estimate));
                    ExceptionLogger exceptionLogger = new ExceptionLogger();
                    exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据重复");
                    exceptionLogger.setLoggerType("estimate");
                    exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                    exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                    exceptionLoggerService.save(exceptionLogger);
                }

                // 保存数据
                estimate.setDate(dataItem.get(1));
                estimate.setPlatformName(dataItem.get(2).toUpperCase());
                estimate.setAdvertiseActive(dataItem.get(3).toUpperCase());
                estimate.setTransformCount(DataUtils.tranform2Integer(dataItem.get(4)));
                estimate.setWebBrowseCount(DataUtils.tranform2Integer(dataItem.get(5)));
                estimate.setWebClickCount(DataUtils.tranform2Integer(dataItem.get(6)));
                estimate.setArriveCount(DataUtils.tranform2Integer(dataItem.get(7)));
                estimate.setArriveUserCount(DataUtils.tranform2Integer(dataItem.get(8)));
                estimate.setArriveRate(dataItem.get(9));
                estimate.setAgainCount(DataUtils.tranform2Integer(dataItem.get(10)));
                estimate.setAgainRate(dataItem.get(11));
                estimate.setAverageStayTime(dataItem.get(12));
                estimate.setMediaShowCount(DataUtils.tranform2Integer(dataItem.get(13)));
                estimate.setMediaClickCount(DataUtils.tranform2Integer(dataItem.get(14)));
                estimate.setMediaClickRate(dataItem.get(15));
                estimate.setCpc(dataItem.get(16));
                estimate.setCpm(dataItem.get(17));
                estimate.setTotalAmount(dataItem.get(18));
                estimate.setDirectTransformCost(dataItem.get(19));

                if(exist) {
                    estimateService.update(estimate);
                }else {
                    estimateService.insert(estimate);
                    totalCount ++;
                }

            } catch (Exception e) {
                logger.error("[estimateController|importExcel]保存数据发生异常。estimate={}", JSON.toJSONString(dataItem), e);
                ExceptionLogger exceptionLogger = new ExceptionLogger();
                exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据异常");
                exceptionLogger.setLoggerType("estimate");
                exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                exceptionLoggerService.save(exceptionLogger);
            }
        }
        resultVo.setRtnData(totalCount);
        return resultVo;
    }


    private List<EstimateVo> transformBean2VoList(List<Estimate> estimateList){
        EstimateVo estimateVo;
        List<EstimateVo> estimateVoList = new ArrayList<>();
        for(Estimate estimate : estimateList){
            estimateVo = new EstimateVo();
            BeanUtils.copyProperties(estimate, estimateVo);
            estimateVo.setCreateTime(DateUtils.formatDate(estimate.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            estimateVo.setUpdateTime(DateUtils.formatDate(estimate.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            estimateVoList.add(estimateVo);
        }
        return estimateVoList;
    }
}
