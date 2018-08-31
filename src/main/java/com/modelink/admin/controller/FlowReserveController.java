package com.modelink.admin.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.bean.ExceptionLogger;
import com.modelink.admin.service.ExceptionLoggerService;
import com.modelink.common.enums.AreaTypeEnum;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.excel.ExcelImportConfigation;
import com.modelink.common.excel.ExcelImportHelper;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.FlowReserve;
import com.modelink.reservation.service.FlowReserveService;
import com.modelink.reservation.vo.FlowReserveParamPagerVo;
import com.modelink.reservation.vo.FlowReserveVo;
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
@RequestMapping("/admin/flowReserve")
public class FlowReserveController {

    public static Logger logger = LoggerFactory.getLogger(FlowReserveController.class);

    @Resource
    private AreaService areaService;
    @Resource
    private MerchantService merchantService;
    @Resource
    private FlowReserveService flowReserveService;
    @Resource
    private ExceptionLoggerService exceptionLoggerService;

    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/flow/flow-reserve-list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<FlowReserveVo> list(FlowReserveParamPagerVo paramPagerVo){
        LayuiResultPagerVo<FlowReserveVo> layuiResultPagerVo = new LayuiResultPagerVo<>();

        PageInfo<FlowReserve> pageInfo = flowReserveService.findPagerByParam(paramPagerVo);
        List<FlowReserve> flowReserveList = pageInfo.getList();
        List<FlowReserveVo> advertiseAnalyseVoList = transformBean2VoList(flowReserveList);

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
            if(StringUtils.isEmpty(fileName) || !fileName.startsWith("预约数据表")){
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("您导入表格不是预约数据表");
                return resultVo;
            }

            Map<Integer, String> fieldFormatMap = new HashMap<>();
            fieldFormatMap.put(3, "HH:mm:ss");

            configation = new ExcelImportConfigation();
            configation.setFieldFormatMap(fieldFormatMap);
            configation.setStartRowNum(1);
            dataList = ExcelImportHelper.importExcel(configation, file.getInputStream());
        } catch (Exception e) {
            logger.error("[flowReserveController|importExcel]发生异常", e);
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
            if(dataItem.size() < 49){
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
        FlowReserve flowReserve;
        Merchant merchant;
        int totalCount = 0;
        int provinceId, cityId;
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
                area = areaService.findByNameAndType(dataItem.get(23), AreaTypeEnum.省.getValue());
                if(area == null){
                    continue;
                }
                provinceId = area.getAreaId();
                area = areaService.findByNameAndType(dataItem.get(24), AreaTypeEnum.市.getValue());
                if(area == null){
                    area = areaService.findByNameAndType(dataItem.get(24), AreaTypeEnum.区.getValue());
                    if(area == null){
                        continue;
                    }
                }
                cityId = area.getAreaId();

                // 重复数据校验
                flowReserve = new FlowReserve();
                flowReserve.setDate(dataItem.get(2));
                flowReserve.setTime(dataItem.get(3));
                flowReserve.setReserveMobile(dataItem.get(5));
                flowReserve.setAdvertiseActive(dataItem.get(7));
                flowReserve = flowReserveService.findOneByParam(flowReserve);
                if(flowReserve == null){
                    isExist = false;
                    flowReserve = new FlowReserve();
                }else{
                    logger.info("[flowReserveController|importExcel]重复数据{}", JSON.toJSONString(flowReserve));
                    ExceptionLogger exceptionLogger = new ExceptionLogger();
                    exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据重复");
                    exceptionLogger.setLoggerType("flow-reserve");
                    exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                    exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                    exceptionLoggerService.save(exceptionLogger);
                }

                merchant = merchantService.findByName(dataItem.get(1));
                // 保存数据
                flowReserve.setDate(dataItem.get(2));
                flowReserve.setTime(dataItem.get(3));
                flowReserve.setMerchantId(merchant == null ? 0L : merchant.getId());
                // 渠道归属
                flowReserve.setPlatformName(dataItem.get(6));

                flowReserve.setProvinceId(area == null ? 0 : area.getAreaId());
                flowReserve.setCityId(area == null ? 0 : area.getAreaId());

                flowReserve.setReserveNo(dataItem.get(4));
                flowReserve.setReserveMobile(dataItem.get(5));
                flowReserve.setAdvertiseActive(dataItem.get(7));
                flowReserve.setAdvertiseMedia(dataItem.get(8));
                flowReserve.setAdvertiseSeries(dataItem.get(9));
                flowReserve.setKeyWordGroup(dataItem.get(10));
                flowReserve.setAdvertiseDesc(dataItem.get(11));
                flowReserve.setAdvertiseTime(dataItem.get(12));
                flowReserve.setAdvertiseTransformTime(dataItem.get(13));

                flowReserve.setFirstAdvertiseActive(dataItem.get(14));
                flowReserve.setFirstAdvertiseMedia(dataItem.get(15));
                flowReserve.setFirstAdvertiseDesc(dataItem.get(16));
                flowReserve.setFirstAdvertiseTime(dataItem.get(17));

                flowReserve.setStationAdvertise(dataItem.get(18));
                flowReserve.setStationAdvertiseTime(dataItem.get(19));
                flowReserve.setStationAdvertiseTransformTime(dataItem.get(20));

                flowReserve.setTransformType(dataItem.get(21));
                flowReserve.setIp(dataItem.get(22));
                flowReserve.setSource(dataItem.get(25));
                flowReserve.setIsAdvertise(dataItem.get(26));
                flowReserve.setWebsite(dataItem.get(27));
                flowReserve.setSearchWord(dataItem.get(28));
                flowReserve.setIsNewVisitor(dataItem.get(29));

                flowReserve.setLast2AdvertiseActive(dataItem.get(30));
                flowReserve.setLast2AdvertiseMedia(dataItem.get(31));
                flowReserve.setLast2AdvertiseDesc(dataItem.get(32));
                flowReserve.setLast2AdvertiseTime(dataItem.get(33));

                flowReserve.setLast3AdvertiseActive(dataItem.get(34));
                flowReserve.setLast3AdvertiseMedia(dataItem.get(35));
                flowReserve.setLast3AdvertiseDesc(dataItem.get(36));
                flowReserve.setLast3AdvertiseTime(dataItem.get(37));

                flowReserve.setBrowser(dataItem.get(38));
                flowReserve.setOs(dataItem.get(39));
                flowReserve.setResolutionRatio(dataItem.get(40));
                flowReserve.setDeviceType(dataItem.get(41));
                flowReserve.setFeeType(dataItem.get(48));

                flowReserve.setThemePage(dataItem.get(42));
                flowReserve.setLast2ThemePage(dataItem.get(43));
                flowReserve.setLast2ThemePageNo(dataItem.get(44));
                flowReserve.setLast2ThemeClickTime(dataItem.get(45));
                flowReserve.setLast2ThemeTransformTime(dataItem.get(46));

                flowReserve.setIsMakeUp(dataItem.get(47));

                if(isExist){
                    flowReserveService.update(flowReserve);
                }else {
                    flowReserveService.insert(flowReserve);
                    totalCount ++;
                }
            } catch (Exception e) {
                logger.error("[flowReserveController|importExcel]保存数据发生异常。flowReserve={}", JSON.toJSONString(dataItem), e);
                ExceptionLogger exceptionLogger = new ExceptionLogger();
                exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据异常");
                exceptionLogger.setLoggerType("flow-reserve");
                exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                exceptionLoggerService.save(exceptionLogger);
            }

        }
        resultVo.setRtnData(totalCount);
        return resultVo;
    }


    private List<FlowReserveVo> transformBean2VoList(List<FlowReserve> flowReserveList){
        Area area;
        Merchant merchant;
        FlowReserveVo flowReserveVo;
        List<FlowReserveVo> flowReserveVoList = new ArrayList<>();
        for(FlowReserve flowReserve : flowReserveList){
            merchant = merchantService.findById(flowReserve.getMerchantId());
            flowReserveVo = new FlowReserveVo();
            BeanUtils.copyProperties(flowReserve, flowReserveVo);

            area = areaService.findById(flowReserve.getProvinceId());
            flowReserveVo.setProvinceName(area == null ? "" : area.getAreaName());
            area = areaService.findById(flowReserve.getCityId());
            flowReserveVo.setCityName(area == null ? "" : area.getAreaName());
            flowReserveVo.setMerchantName(merchant == null ? "" : merchant.getName());
            flowReserveVo.setCreateTime(DateUtils.formatDate(flowReserve.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            flowReserveVo.setUpdateTime(DateUtils.formatDate(flowReserve.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            flowReserveVoList.add(flowReserveVo);
        }
        return flowReserveVoList;
    }
}
