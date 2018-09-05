package com.modelink.admin.controller.basedata;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.bean.ExceptionLogger;
import com.modelink.admin.service.ExceptionLoggerService;
import com.modelink.common.enums.AreaTypeEnum;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.excel.ExcelImportConfigation;
import com.modelink.common.excel.ExcelImportHelper;
import com.modelink.common.utils.DataUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.FlowArea;
import com.modelink.reservation.service.FlowAreaService;
import com.modelink.reservation.vo.FlowAreaParamPagerVo;
import com.modelink.reservation.vo.FlowAreaVo;
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
@RequestMapping("/admin/flowArea")
public class FlowAreaController {

    public static Logger logger = LoggerFactory.getLogger(FlowAreaController.class);
    public static String yyyyMMddFormat = "yyyy-MM-dd";

    @Resource
    private AreaService areaService;
    @Resource
    private MerchantService merchantService;
    @Resource
    private FlowAreaService flowAreaService;
    @Resource
    private ExceptionLoggerService exceptionLoggerService;

    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/flow/flow-area-list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<FlowAreaVo> list(FlowAreaParamPagerVo paramPagerVo){
        LayuiResultPagerVo<FlowAreaVo> layuiResultPagerVo = new LayuiResultPagerVo<>();

        PageInfo<FlowArea> pageInfo = flowAreaService.findPagerByParam(paramPagerVo);
        List<FlowArea> flowAreaList = pageInfo.getList();
        List<FlowAreaVo> advertiseAnalyseVoList = transformBean2VoList(flowAreaList);

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
            if(StringUtils.isEmpty(fileName) || !fileName.startsWith("地区流量表")){
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("您导入表格不是地区流量表");
                return resultVo;
            }

            Map<Integer, String> fieldFormatMap = new HashMap<>();
            fieldFormatMap.put(9, "HH:mm:ss");

            configation = new ExcelImportConfigation();
            configation.setFieldFormatMap(fieldFormatMap);
            configation.setStartRowNum(1);
            dataList = ExcelImportHelper.importExcel(configation, file.getInputStream());
        } catch (Exception e) {
            logger.error("[flowAreaController|importExcel]发生异常", e);
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
            if(dataItem.size() < 12){
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
        FlowArea flowArea;
        Merchant merchant;
        int provinceId, cityId;
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
                area = areaService.findByNameAndType(dataItem.get(4), AreaTypeEnum.省.getValue());
                if(area == null){
                    continue;
                }
                provinceId = area.getAreaId();
                area = areaService.findByNameAndType(dataItem.get(5), AreaTypeEnum.市.getValue());
                if(area == null){
                    area = areaService.findByNameAndType(dataItem.get(5), AreaTypeEnum.区.getValue());
                    if(area == null){
                        continue;
                    }
                }
                cityId = area.getAreaId();

                // 重复数据校验
                flowArea = new FlowArea();
                flowArea.setDate(dataItem.get(1));
                flowArea.setPlatformName(dataItem.get(3));
                flowArea.setProvinceId(provinceId);
                flowArea.setCityId(cityId);
                flowArea.setInflowCount(DataUtils.tranform2Integer(dataItem.get(7)));
                flowArea.setBrowseCount(DataUtils.tranform2Integer(dataItem.get(8)));
                flowArea.setUserCount(DataUtils.tranform2Integer(dataItem.get(9)));
                flowArea = flowAreaService.findOneByParam(flowArea);
                if(flowArea == null){
                    exist = false;
                    flowArea = new FlowArea();
                }else{
                    logger.info("[flowAreaController|importExcel]重复数据{}", JSON.toJSONString(dataItem));
                    ExceptionLogger exceptionLogger = new ExceptionLogger();
                    exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据重复");
                    exceptionLogger.setLoggerType("flow-area");
                    exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                    exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                    exceptionLoggerService.save(exceptionLogger);
                }

                merchant = merchantService.findByName(dataItem.get(2));
                // 保存数据
                flowArea.setDate(dataItem.get(1));
                flowArea.setMerchantId(merchant == null ? 0L : merchant.getId());
                // 渠道归属
                flowArea.setPlatformName(dataItem.get(3));

                flowArea.setProvinceId(provinceId);
                flowArea.setCityId(cityId);
                flowArea.setSource(dataItem.get(6));
                flowArea.setInflowCount(DataUtils.tranform2Integer(dataItem.get(7)));
                flowArea.setBrowseCount(DataUtils.tranform2Integer(dataItem.get(8)));
                flowArea.setUserCount(DataUtils.tranform2Integer(dataItem.get(9)));
                flowArea.setAverageStayTime(dataItem.get(10));
                flowArea.setAgainClickRate(dataItem.get(11));

                if(exist) {
                    flowAreaService.update(flowArea);
                }else {
                    flowAreaService.insert(flowArea);
                    totalCount ++;
                }
            } catch (Exception e) {
                logger.error("[flowAreaController|importExcel]保存数据发生异常。flowArea={}", JSON.toJSONString(dataItem), e);
                ExceptionLogger exceptionLogger = new ExceptionLogger();
                exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据异常");
                exceptionLogger.setLoggerType("flow-area");
                exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                exceptionLoggerService.save(exceptionLogger);
            }

        }
        resultVo.setRtnData(totalCount);
        return resultVo;
    }


    private List<FlowAreaVo> transformBean2VoList(List<FlowArea> flowAreaList){
        Area area;
        Merchant merchant;
        FlowAreaVo flowAreaVo;
        List<FlowAreaVo> flowAreaVoList = new ArrayList<>();
        for(FlowArea flowArea : flowAreaList){
            merchant = merchantService.findById(flowArea.getMerchantId());
            flowAreaVo = new FlowAreaVo();
            BeanUtils.copyProperties(flowArea, flowAreaVo);

            area = areaService.findById(flowArea.getProvinceId());
            flowAreaVo.setProvinceName(area == null ? "" : area.getAreaName());
            area = areaService.findById(flowArea.getCityId());
            flowAreaVo.setCityName(area == null ? "" : area.getAreaName());
            flowAreaVo.setMerchantName(merchant == null ? "" : merchant.getName());
            flowAreaVo.setCreateTime(DateUtils.formatDate(flowArea.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            flowAreaVo.setUpdateTime(DateUtils.formatDate(flowArea.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            flowAreaVoList.add(flowAreaVo);
        }
        return flowAreaVoList;
    }
}
