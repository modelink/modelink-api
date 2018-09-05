package com.modelink.admin.controller.basedata;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.bean.ExceptionLogger;
import com.modelink.admin.service.ExceptionLoggerService;
import com.modelink.common.enums.InsuranceChildStatusEnum;
import com.modelink.common.enums.InsurancePayTypeEnum;
import com.modelink.common.enums.InsuranceStatusEnum;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.excel.ExcelImportConfigation;
import com.modelink.common.excel.ExcelImportHelper;
import com.modelink.common.utils.DataUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.Repellent;
import com.modelink.reservation.service.RepellentService;
import com.modelink.reservation.vo.RepellentParamPagerVo;
import com.modelink.reservation.vo.RepellentVo;
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

/** 退保数据Controller **/
@Controller
@RequestMapping("/admin/repellent")
public class RepellentController {


    public static Logger logger = LoggerFactory.getLogger(RepellentController.class);

    @Resource
    private MerchantService merchantService;
    @Resource
    private RepellentService repellentService;
    @Resource
    private ExceptionLoggerService exceptionLoggerService;

    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/repellent/repellent-list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<RepellentVo> list(RepellentParamPagerVo paramPagerVo){
        LayuiResultPagerVo<RepellentVo> layuiResultPagerVo = new LayuiResultPagerVo<>();

        PageInfo<Repellent> pageInfo = repellentService.findPagerByParam(paramPagerVo);
        List<Repellent> repellentList = pageInfo.getList();
        List<RepellentVo> advertiseAnalyseVoList = transformBean2VoList(repellentList);

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
            if(StringUtils.isEmpty(fileName) || !fileName.startsWith("退保数据表")){
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("您导入表格不是退保数据表");
                return resultVo;
            }

            Map<Integer, String> fieldFormatMap = new HashMap<>();
            fieldFormatMap.put(0, "M月d日");

            configation = new ExcelImportConfigation();
            configation.setFieldFormatMap(fieldFormatMap);
            configation.setStartRowNum(1);
            dataList = ExcelImportHelper.importExcel(configation, file.getInputStream());
        } catch (Exception e) {
            logger.error("[repellentController|importExcel]发生异常", e);
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
        boolean isExist;
        boolean isFullNull;
        StringBuilder messageBuilder = new StringBuilder();
        int rowIndex = configation.getStartRowNum();
        for(List<String> dataItem : dataList){
            if(dataItem.size() < 26){
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
        Merchant merchant;
        Repellent repellent;
        int totalCount = 0;
        for(List<String> dataItem : dataList){

            // 跳过空行
            isExist = true;
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
                repellent = new Repellent();
                repellent.setInsuranceNo(dataItem.get(4));
                repellent.setStatus(InsuranceStatusEnum.getValueByText(dataItem.get(5)));
                repellent = repellentService.findOneByParam(repellent);
                if(repellent == null){
                    isExist = false;
                    repellent = new Repellent();
                }else{
                    logger.info("[repellentController|importExcel]重复数据{}", JSON.toJSONString(repellent));
                    ExceptionLogger exceptionLogger = new ExceptionLogger();
                    exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据重复");
                    exceptionLogger.setLoggerType("repellent");
                    exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                    exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                    exceptionLoggerService.save(exceptionLogger);
                }

                merchant = merchantService.findByName(dataItem.get(1));
                // 保存数据
                repellent.setMerchantId(merchant == null ? 0 : merchant.getId());
                repellent.setExportOrgName(dataItem.get(2));
                repellent.setRepellentNo(dataItem.get(3));
                repellent.setInsuranceNo(dataItem.get(4));
                repellent.setStatus(InsuranceStatusEnum.getValueByText(dataItem.get(5)));
                repellent.setChildStatus(InsuranceChildStatusEnum.getValueByText(dataItem.get(6)));
                repellent.setInsuranceName(dataItem.get(7));
                repellent.setProductName(dataItem.get(8));
                repellent.setExtraInsurance(dataItem.get(9));
                repellent.setInsuranceAmount(dataItem.get(10));
                repellent.setYearInsuranceFee(dataItem.get(11));
                repellent.setInsuranceFee(dataItem.get(12));
                repellent.setTsrNumber(dataItem.get(13));
                repellent.setTsrName(dataItem.get(14));
                repellent.setTlNumber(dataItem.get(15));
                repellent.setTlName(dataItem.get(16));
                repellent.setOrgName(dataItem.get(17));
                repellent.setDepartment(dataItem.get(18));
                repellent.setRegionName(dataItem.get(19));
                repellent.setGroupName(dataItem.get(20));
                repellent.setSpecialCaseName(dataItem.get(21));
                repellent.setInsuranceDate(dataItem.get(22));
                repellent.setPayType(InsurancePayTypeEnum.getValueByText(dataItem.get(23)));
                repellent.setHesitateDate(dataItem.get(24));
                if("-".equals(dataItem.get(25)) || "".equals(dataItem.get(25))){
                    repellent.setPayInterval(0);
                }else {
                    repellent.setPayInterval(DataUtils.tranform2Integer(dataItem.get(25)));
                }

                if(isExist){
                    repellentService.update(repellent);
                }else {
                    repellentService.insert(repellent);
                    totalCount ++;
                }
            } catch (Exception e) {
                logger.error("[repellentController|importExcel]保存数据发生异常。repellent={}", JSON.toJSONString(dataItem), e);
                ExceptionLogger exceptionLogger = new ExceptionLogger();
                exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据异常");
                exceptionLogger.setLoggerType("repellent");
                exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                exceptionLoggerService.save(exceptionLogger);
            }

        }
        resultVo.setRtnData(totalCount);
        return resultVo;
    }


    private List<RepellentVo> transformBean2VoList(List<Repellent> repellentList){
        Merchant merchant;
        RepellentVo repellentVo;
        List<RepellentVo> repellentVoList = new ArrayList<>();
        for(Repellent repellent : repellentList){
            repellentVo = new RepellentVo();
            BeanUtils.copyProperties(repellent, repellentVo);

            merchant = merchantService.findById(repellent.getMerchantId());
            repellentVo.setMerchantName(merchant == null ? "" : merchant.getName());
            repellentVo.setStatus(InsuranceStatusEnum.getTextByValue(repellent.getStatus()));
            repellentVo.setChildStatus(InsuranceChildStatusEnum.getTextByValue(repellent.getChildStatus()));
            repellentVo.setPayTypeName(InsurancePayTypeEnum.getTextByValue(repellent.getPayType()));
            repellentVo.setCreateTime(DateUtils.formatDate(repellent.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            repellentVo.setUpdateTime(DateUtils.formatDate(repellent.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            repellentVoList.add(repellentVo);
        }
        return repellentVoList;
    }
}
