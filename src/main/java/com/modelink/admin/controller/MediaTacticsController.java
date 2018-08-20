package com.modelink.admin.controller;

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
import com.modelink.reservation.bean.MediaTactics;
import com.modelink.reservation.service.MediaTacticsService;
import com.modelink.reservation.vo.MediaTacticsParamPagerVo;
import com.modelink.reservation.vo.MediaTacticsVo;
import com.modelink.usercenter.bean.Merchant;
import com.modelink.usercenter.service.MerchantService;
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
 * 媒体策略数据Controller
 */
@Controller
@RequestMapping("/admin/mediaTactics")
public class MediaTacticsController {

    public static Logger logger = LoggerFactory.getLogger(MediaTacticsController.class);

    @Resource
    private MediaTacticsService mediaTacticsService;
    @Resource
    private MerchantService merchantService;
    @Resource
    private ExceptionLoggerService exceptionLoggerService;

    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/media/media-tactics-list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<MediaTacticsVo> list(MediaTacticsParamPagerVo paramPagerVo){
        LayuiResultPagerVo<MediaTacticsVo> layuiResultPagerVo = new LayuiResultPagerVo<>();

        PageInfo<MediaTactics> pageInfo = mediaTacticsService.findPagerByParam(paramPagerVo);
        List<MediaTactics> mediaTacticsList = pageInfo.getList();
        List<MediaTacticsVo> advertiseAnalyseVoList = transformBean2VoList(mediaTacticsList);

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
            if(StringUtils.isEmpty(fileName) || !fileName.startsWith("媒体策略调整数据表")){
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("您导入表格不是媒体策略调整数据表");
                return resultVo;
            }

            Map<Integer, String> fieldFormatMap = new HashMap<>();
            configation = new ExcelImportConfigation();
            configation.setFieldFormatMap(fieldFormatMap);
            configation.setStartRowNum(1);
            dataList = ExcelImportHelper.importExcel(configation, file.getInputStream());
        } catch (Exception e) {
            logger.error("[mediaTacticsController|importExcel]发生异常", e);
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
            if(dataItem.size() < 29){
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
        MediaTactics mediaTactics;
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

                Merchant merchant = merchantService.findByName(dataItem.get(1));
                // 重复数据校验
                mediaTactics = new MediaTactics();
                mediaTactics.setMerchantId(merchant == null ? 0 : merchant.getId());
                mediaTactics.setMonth(dataItem.get(2));
                mediaTactics.setAdvertiseActive(dataItem.get(4));
                mediaTactics.setSpeedCost(dataItem.get(5));
                mediaTactics.setReserveCount(DataUtils.tranform2Integer(dataItem.get(6)));
                mediaTactics = mediaTacticsService.findOneByParam(mediaTactics);
                if(mediaTactics == null){
                    exist = false;
                    mediaTactics = new MediaTactics();
                }else{
                    logger.info("[mediaTacticsController|importExcel]重复数据{}", JSON.toJSONString(mediaTactics));
                    ExceptionLogger exceptionLogger = new ExceptionLogger();
                    exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据重复");
                    exceptionLogger.setLoggerType("mediaTactics");
                    exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                    exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                    exceptionLoggerService.save(exceptionLogger);
                }


                // 保存数据
                mediaTactics.setMerchantId(merchant == null ? 0 : merchant.getId());
                mediaTactics.setMonth(dataItem.get(2));
                mediaTactics.setPlatformName(dataItem.get(3));
                mediaTactics.setAdvertiseActive(dataItem.get(4));
                mediaTactics.setSpeedCost(dataItem.get(5));
                mediaTactics.setReserveCount(DataUtils.tranform2Integer(dataItem.get(6)));
                mediaTactics.setInsuranceCount(DataUtils.tranform2Integer(dataItem.get(7)));
                mediaTactics.setInsuranceFee(dataItem.get(8));
                mediaTactics.setOperateCount(DataUtils.tranform2Integer(dataItem.get(9)));
                mediaTactics.setOptimizeKeyWord(DataUtils.tranform2Integer(dataItem.get(10)));
                mediaTactics.setAddBid(DataUtils.tranform2Integer(dataItem.get(11)));
                mediaTactics.setReduceBid(DataUtils.tranform2Integer(dataItem.get(12)));
                mediaTactics.setAddPatten(DataUtils.tranform2Integer(dataItem.get(13)));
                mediaTactics.setReducePatten(DataUtils.tranform2Integer(dataItem.get(14)));
                mediaTactics.setAddKeyWord(DataUtils.tranform2Integer(dataItem.get(15)));
                mediaTactics.setReduceKeyWord(DataUtils.tranform2Integer(dataItem.get(16)));
                mediaTactics.setFilteKeyWord(DataUtils.tranform2Integer(dataItem.get(17)));
                mediaTactics.setOptimizeWordIdea(DataUtils.tranform2Integer(dataItem.get(18)));
                mediaTactics.setAddStyle(DataUtils.tranform2Integer(dataItem.get(19)));
                mediaTactics.setAddWordIdea(DataUtils.tranform2Integer(dataItem.get(20)));
                mediaTactics.setOptimizeImageIdea(DataUtils.tranform2Integer(dataItem.get(21)));
                mediaTactics.setAddImageIdea(DataUtils.tranform2Integer(dataItem.get(22)));
                mediaTactics.setReduceImageIdea(DataUtils.tranform2Integer(dataItem.get(23)));
                mediaTactics.setModifyImageBid(DataUtils.tranform2Integer(dataItem.get(24)));
                mediaTactics.setOptimizeFlowIdea(DataUtils.tranform2Integer(dataItem.get(25)));
                mediaTactics.setModifyCopywrite(DataUtils.tranform2Integer(dataItem.get(26)));
                mediaTactics.setOptimizeFlowPeople(DataUtils.tranform2Integer(dataItem.get(27)));
                mediaTactics.setModifyKeyWord(DataUtils.tranform2Integer(dataItem.get(28)));

                if(exist) {
                    mediaTacticsService.update(mediaTactics);
                }else {
                    mediaTacticsService.insert(mediaTactics);
                    totalCount ++;
                }

            } catch (Exception e) {
                logger.error("[mediaTacticsController|importExcel]保存数据发生异常。mediaTactics={}", JSON.toJSONString(dataItem), e);
                ExceptionLogger exceptionLogger = new ExceptionLogger();
                exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据异常");
                exceptionLogger.setLoggerType("media-tactics");
                exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                exceptionLoggerService.save(exceptionLogger);
            }
        }
        resultVo.setRtnData(totalCount);
        return resultVo;
    }


    private List<MediaTacticsVo> transformBean2VoList(List<MediaTactics> mediaTacticsList){
        Merchant merchant;
        MediaTacticsVo mediaTacticsVo;
        List<MediaTacticsVo> mediaTacticsVoList = new ArrayList<>();
        for(MediaTactics mediaTactics : mediaTacticsList){
            mediaTacticsVo = new MediaTacticsVo();
            BeanUtils.copyProperties(mediaTactics, mediaTacticsVo);
            merchant = merchantService.findById(mediaTactics.getMerchantId());
            mediaTacticsVo.setMerchantName(merchant == null ? "" : merchant.getName());
            mediaTacticsVo.setCreateTime(DateUtils.formatDate(mediaTactics.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            mediaTacticsVo.setUpdateTime(DateUtils.formatDate(mediaTactics.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            mediaTacticsVoList.add(mediaTacticsVo);
        }
        return mediaTacticsVoList;
    }
}
