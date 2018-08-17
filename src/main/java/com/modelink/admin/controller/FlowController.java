package com.modelink.admin.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.excel.ExcelImportConfigation;
import com.modelink.common.excel.ExcelImportHelper;
import com.modelink.common.utils.DataUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.Flow;
import com.modelink.reservation.service.FlowService;
import com.modelink.reservation.vo.FlowParamPagerVo;
import com.modelink.reservation.vo.FlowVo;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 承保效果数据Controller
 */
@Controller
@RequestMapping("/admin/flow")
public class FlowController {

    public static Logger logger = LoggerFactory.getLogger(FlowController.class);
    public static String yyyyMMddFormat = "yyyy-MM-dd";

    @Resource
    private AreaService areaService;
    @Resource
    private MerchantService merchantService;
    @Resource
    private FlowService flowService;

    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/flow/flow-list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<FlowVo> list(FlowParamPagerVo paramPagerVo){
        LayuiResultPagerVo<FlowVo> layuiResultPagerVo = new LayuiResultPagerVo<>();

        PageInfo<Flow> pageInfo = flowService.findPagerByParam(paramPagerVo);
        List<Flow> flowList = pageInfo.getList();
        List<FlowVo> advertiseAnalyseVoList = transformBean2VoList(flowList);

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
            if(StringUtils.isEmpty(fileName) || !fileName.startsWith("流量总表")){
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("您导入表格不是流量总表");
                return resultVo;
            }

            Map<Integer, String> fieldFormatMap = new HashMap<>();
            fieldFormatMap.put(10, "HH:mm:ss");

            configation = new ExcelImportConfigation();
            configation.setFieldFormatMap(fieldFormatMap);
            configation.setStartRowNum(1);
            dataList = ExcelImportHelper.importExcel(configation, file.getInputStream());
        } catch (Exception e) {
            logger.error("[flowController|importExcel]发生异常", e);
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
            if(dataItem.size() < 13){
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
        Flow flow;
        Merchant merchant;
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
                flow = new Flow();

                merchant = merchantService.findByName(dataItem.get(2));
                flow.setDate(dataItem.get(1));
                flow.setMerchantId(merchant.getId());
                flow.setPlatformName(dataItem.get(3));
                flow.setWebsite(dataItem.get(4));
                flow.setSource(dataItem.get(5));
                flow.setBrowseCount(DataUtils.tranform2Integer(dataItem.get(6)));
                flow = flowService.findOneByParam(flow);
                if(flow == null){
                    isExist = false;
                    flow = new Flow();
                }else{
                    logger.info("[flowController|importExcel]重复数据{}", JSON.toJSONString(flow));
                }


                // 保存数据
                flow.setDate(dataItem.get(1));
                flow.setMerchantId(merchant == null ? 0L : merchant.getId());
                // 渠道归属
                flow.setPlatformName(dataItem.get(3));

                flow.setWebsite(dataItem.get(4));
                flow.setSource(dataItem.get(5));
                flow.setBrowseCount(DataUtils.tranform2Integer(dataItem.get(6)));
                flow.setInflowCount(DataUtils.tranform2Integer(dataItem.get(7)));
                flow.setUserCount(DataUtils.tranform2Integer(dataItem.get(8)));
                flow.setAgainClickCount(DataUtils.tranform2Integer(dataItem.get(9)));
                flow.setAgainClickRate(dataItem.get(10));
                flow.setAverageStayTime(dataItem.get(11));
                flow.setAverageBrowsePageCount(dataItem.get(12));

                if(isExist) {
                    flowService.update(flow);
                }else{
                    flowService.insert(flow);
                }
                totalCount ++;
            } catch (Exception e) {
                logger.error("[flowController|importExcel]保存数据发生异常。flow={}", JSON.toJSONString(dataItem), e);
            }

        }
        resultVo.setRtnData(totalCount);
        return resultVo;
    }


    private List<FlowVo> transformBean2VoList(List<Flow> flowList){
        Merchant merchant;
        FlowVo flowVo;
        List<FlowVo> flowVoList = new ArrayList<>();
        for(Flow flow : flowList){
            merchant = merchantService.findById(flow.getMerchantId());
            flowVo = new FlowVo();
            BeanUtils.copyProperties(flow, flowVo);
            flowVo.setMerchantName(merchant == null ? "" : merchant.getName());
            flowVo.setCreateTime(DateUtils.formatDate(flow.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            flowVo.setUpdateTime(DateUtils.formatDate(flow.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            flowVoList.add(flowVo);
        }
        return flowVoList;
    }
}
