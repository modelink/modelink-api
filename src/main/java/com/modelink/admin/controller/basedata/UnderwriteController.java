package com.modelink.admin.controller.basedata;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.bean.ExceptionLogger;
import com.modelink.admin.service.ExceptionLoggerService;
import com.modelink.common.enums.AreaTypeEnum;
import com.modelink.common.enums.InsurancePayTypeEnum;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.excel.ExcelImportConfigation;
import com.modelink.common.excel.ExcelImportHelper;
import com.modelink.common.utils.DataUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.Flow;
import com.modelink.reservation.bean.FlowReserve;
import com.modelink.reservation.bean.Underwrite;
import com.modelink.reservation.service.FlowReserveService;
import com.modelink.reservation.service.UnderwriteService;
import com.modelink.reservation.vo.FlowReserveParamPagerVo;
import com.modelink.reservation.vo.UnderwriteParamPagerVo;
import com.modelink.reservation.vo.UnderwriteVo;
import com.modelink.usercenter.bean.Area;
import com.modelink.usercenter.bean.Merchant;
import com.modelink.usercenter.service.AreaService;
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
 * 承保效果数据Controller
 */
@Controller
@RequestMapping("/admin/underwrite")
public class UnderwriteController {

    public static Logger logger = LoggerFactory.getLogger(UnderwriteController.class);

    @Resource
    private AreaService areaService;
    @Resource
    private MerchantService merchantService;
    @Resource
    private UnderwriteService underwriteService;
    @Resource
    private FlowReserveService flowReserveService;
    @Resource
    private ExceptionLoggerService exceptionLoggerService;

    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/underwrite/list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/modify")
    public ResultVo modify() {
        FlowReserveParamPagerVo flowReserveParamPagerVo;
        List<FlowReserve> flowReserveList;
        UnderwriteParamPagerVo underwriteParamPagerVo = new UnderwriteParamPagerVo();
        List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);

        int difference;
        String reserveDate, finishDate;
        for(Underwrite underwrite : underwriteList){
            flowReserveParamPagerVo = new FlowReserveParamPagerVo();
            flowReserveParamPagerVo.setMobile(underwrite.getReserveMobile());
            flowReserveParamPagerVo.setSortField("date desc");
            flowReserveList = flowReserveService.findListByParam(flowReserveParamPagerVo);

            underwrite.setReserveDate("");
            underwrite.setKeyword("");
            if(flowReserveList.size() > 0) {
                for(FlowReserve flowReserve : flowReserveList){
                    reserveDate = flowReserve.getDate();
                    if(reserveDate.contains("/")){
                        reserveDate = DateUtils.dateFormatTransform(reserveDate, "yyyy/M/d", "yyyy-MM-dd");
                    }
                    finishDate = underwrite.getFinishDate();
                    if(finishDate.contains("/")){
                        finishDate = DateUtils.dateFormatTransform(finishDate, "yyyy/M/d", "yyyy-MM-dd");
                        underwrite.setFinishDate(finishDate);
                    }
                    difference = DateUtils.getDateDifference(reserveDate, finishDate);
                    if(difference > 0){
                        underwrite.setReserveDate(reserveDate);
                        underwrite.setKeyword(flowReserve.getAdvertiseDesc());
                        logger.info("[underwriteController|modify]承保数据更新成功。id={}, oldNum={}", underwrite.getId(), flowReserveList.size());
                        break;
                    }
                }
            }
            underwriteService.update(underwrite);
        }

        return new ResultVo();
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<UnderwriteVo> list(UnderwriteParamPagerVo paramPagerVo){
        LayuiResultPagerVo<UnderwriteVo> layuiResultPagerVo = new LayuiResultPagerVo<>();

        PageInfo<Underwrite> pageInfo = underwriteService.findPagerByParam(paramPagerVo);
        List<Underwrite> underwriteList = pageInfo.getList();
        List<UnderwriteVo> advertiseAnalyseVoList = transformBean2VoList(underwriteList);

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
            if(StringUtils.isEmpty(fileName) || !fileName.startsWith("承保数据表")){
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("您导入表格不是承保数据表");
                return resultVo;
            }

            Map<Integer, String> fieldFormatMap = new HashMap<>();
            //fieldFormatMap.put(16, "HH:mm:ss");

            configation = new ExcelImportConfigation();
            configation.setFieldFormatMap(fieldFormatMap);
            configation.setStartRowNum(1);
            dataList = ExcelImportHelper.importExcel(configation, file.getInputStream());
        } catch (Exception e) {
            logger.error("[adminAdvertiseController|importExcel]发生异常", e);
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
        Area area;
        boolean exist;
        Underwrite underwrite;
        Set<String> mobileSet;
        List<FlowReserve> flowReserveList;
        Merchant merchant;
        String sourceDate;
        int index;
        int totalCount = 0;
        int provinceId, cityId;
        int difference;
        String reserveDate, finishDate;
        FlowReserveParamPagerVo flowReserveParamPagerVo;
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
                area = areaService.findByNameAndType(dataItem.get(18), AreaTypeEnum.省.getValue());
                if(area == null){
                    continue;
                }
                provinceId = area.getAreaId();
                area = areaService.findByNameAndType(dataItem.get(19), AreaTypeEnum.市.getValue());
                if(area == null){
                    area = areaService.findByNameAndType(dataItem.get(19), AreaTypeEnum.区.getValue());
                    if(area == null){
                        continue;
                    }
                }
                cityId = area.getAreaId();
                // 重复数据校验
                underwrite = new Underwrite();

                sourceDate = dataItem.get(9);
                underwrite.setSourceDate(sourceDate);
                merchant = merchantService.findByName(dataItem.get(1));
                underwrite.setMerchantId(merchant == null ? 0L : merchant.getId());
                underwrite.setInsuranceNo(dataItem.get(6));
                underwrite.setReserveMobile(dataItem.get(7));
                underwrite.setInsuranceAmount(dataItem.get(12));
                underwrite.setInsuranceFee(dataItem.get(13));
                underwrite = underwriteService.findOneByParam(underwrite);
                if(underwrite == null){
                    exist = false;
                    underwrite = new Underwrite();
                }else{
                    logger.info("[underwriteController|importExcel]重复数据{}", JSON.toJSONString(dataItem));
                    ExceptionLogger exceptionLogger = new ExceptionLogger();
                    exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据重复");
                    exceptionLogger.setLoggerType("underwrite");
                    exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                    exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                    exceptionLoggerService.save(exceptionLogger);
                }

                underwrite.setMerchantId(merchant == null ? 0L : merchant.getId());
                // 渠道归属
                underwrite.setPlatformName(dataItem.get(2).toUpperCase());
                // 渠道明细
                underwrite.setAdvertiseActive(dataItem.get(3).toUpperCase());

                underwrite.setProductName(dataItem.get(4));

                underwrite.setOrgName(dataItem.get(5));
                underwrite.setInsuranceNo(dataItem.get(6));
                underwrite.setReserveMobile(dataItem.get(7));
                underwrite.setSource(dataItem.get(8));
                if (dataItem.get(9).contains("/")) {
                    underwrite.setSourceDate(DateUtils.dateFormatTransform(dataItem.get(9), "yyyy/M/d", "yyyy-MM-dd"));
                } else {
                    underwrite.setSourceDate(dataItem.get(9));
                }
                if (dataItem.get(10).contains("/")) {
                    underwrite.setFinishDate(DateUtils.dateFormatTransform(dataItem.get(10), "yyyy/M/d", "yyyy-MM-dd"));
                } else {
                    underwrite.setFinishDate(dataItem.get(10));
                }
                underwrite.setPayType(InsurancePayTypeEnum.getValueByText(dataItem.get(11)));
                underwrite.setInsuranceAmount(dataItem.get(12));
                underwrite.setInsuranceFee(dataItem.get(13));
                underwrite.setGender(dataItem.get(14));
                underwrite.setBirthday(dataItem.get(15));
                if(StringUtils.hasText(dataItem.get(16)) && dataItem.get(16).contains(".")) {
                    index = dataItem.get(16).indexOf(".");
                    underwrite.setAge(DataUtils.tranform2Integer(dataItem.get(16).substring(0, index)));
                }else if(StringUtils.hasText(dataItem.get(16))){
                    underwrite.setAge(DataUtils.tranform2Integer(dataItem.get(16)));
                }
                underwrite.setAddress(dataItem.get(17));
                // 查找省份数据
                underwrite.setProvinceId(provinceId);
                // 查找城市数据
                underwrite.setCityId(cityId);

                // 插入预约日期
                flowReserveParamPagerVo = new FlowReserveParamPagerVo();
                flowReserveParamPagerVo.setMobile(underwrite.getReserveMobile());
                flowReserveParamPagerVo.setSortField("date desc");
                flowReserveList = flowReserveService.findListByParam(flowReserveParamPagerVo);

                underwrite.setReserveDate("");
                underwrite.setKeyword("");
                if(flowReserveList.size() > 0) {
                    for(FlowReserve flowReserve : flowReserveList){
                        reserveDate = flowReserve.getDate();
                        if(reserveDate.contains("/")){
                            reserveDate = DateUtils.dateFormatTransform(reserveDate, "yyyy/M/d", "yyyy-MM-dd");
                        }
                        finishDate = underwrite.getFinishDate();
                        if(finishDate.contains("/")){
                            finishDate = DateUtils.dateFormatTransform(finishDate, "yyyy/M/d", "yyyy-MM-dd");
                            underwrite.setFinishDate(finishDate);
                        }
                        difference = DateUtils.getDateDifference(reserveDate, finishDate);
                        if(difference > 0){
                            underwrite.setReserveDate(reserveDate);
                            underwrite.setKeyword(flowReserve.getAdvertiseDesc());
                            break;
                        }
                    }
                }

                if(exist) {
                    underwriteService.update(underwrite);
                }else{
                    underwriteService.insert(underwrite);
                    totalCount ++;
                }
            } catch (Exception e) {
                logger.error("[adminAdvertiseController|importExcel]保存数据发生异常。underwrite={}", JSON.toJSONString(dataItem), e);
                ExceptionLogger exceptionLogger = new ExceptionLogger();
                exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据异常");
                exceptionLogger.setLoggerType("underwrite");
                exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                exceptionLoggerService.save(exceptionLogger);
            }
        }
        resultVo.setRtnData(totalCount);
        return resultVo;
    }


    private List<UnderwriteVo> transformBean2VoList(List<Underwrite> underwriteList){
        Area area;
        Merchant merchant;
        UnderwriteVo underwriteVo;
        List<UnderwriteVo> underwriteVoList = new ArrayList<>();
        for(Underwrite underwrite : underwriteList){
            merchant = merchantService.findById(underwrite.getMerchantId());
            underwriteVo = new UnderwriteVo();
            BeanUtils.copyProperties(underwrite, underwriteVo);
            underwriteVo.setMerchantName(merchant == null ? "" : merchant.getName());
            underwriteVo.setPayType(InsurancePayTypeEnum.getTextByValue(underwrite.getPayType()));

            area = areaService.findById(underwrite.getProvinceId());
            underwriteVo.setProvinceName(area == null ? "" : area.getAreaName());
            area = areaService.findById(underwrite.getCityId());
            underwriteVo.setCityName(area == null ? "" : area.getAreaName());
            underwriteVo.setCreateTime(DateUtils.formatDate(underwrite.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            underwriteVo.setUpdateTime(DateUtils.formatDate(underwrite.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            underwriteVoList.add(underwriteVo);
        }
        return underwriteVoList;
    }
}
