package com.modelink.admin.controller.dashboard;

import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.vo.DashboardClientParamVo;
import com.modelink.admin.vo.DashboardParamVo;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.utils.DataUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.FlowReserve;
import com.modelink.reservation.bean.MediaItem;
import com.modelink.reservation.bean.Underwrite;
import com.modelink.reservation.enums.FeeTypeEnum;
import com.modelink.reservation.service.FlowReserveService;
import com.modelink.reservation.service.MediaItemService;
import com.modelink.reservation.service.UnderwriteService;
import com.modelink.reservation.vo.FlowReserveParamPagerVo;
import com.modelink.reservation.vo.MediaItemParamPagerVo;
import com.modelink.reservation.vo.UnderwriteParamPagerVo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping("/admin/dashboard/client")
public class DashboardClientController {

    @Resource
    private UnderwriteService underwriteService;
    @Resource
    private FlowReserveService flowReserveService;
    @Resource
    private MediaItemService mediaItemService;

    @RequestMapping
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/dashboard/client");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/getReserveSummary")
    public ResultVo getReserveSummary(DashboardClientParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("id,date,os,browser,resolutionRatio,deviceType");
        paramPagerVo.setFeeType(FeeTypeEnum.FEE_TYPE_RESERVE.getText());
        paramPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        String browser, os, resolutionRatio, deviceType;
        int reserveCount, totalCount = 0;
        StringBuilder chooseItems;
        Map<String, Integer> reserveCountMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            chooseItems = new StringBuilder();
            if(paramVo.getChooseItems().contains("browser")){
                browser = flowReserve.getBrowser();
                if(StringUtils.isEmpty(browser) || "-".equals(browser)){
                    browser = "-";
                }
                if(browser.indexOf(" ") > 0) {
                    browser = browser.substring(0, browser.indexOf(" "));
                }
                chooseItems.append(browser).append("|");
            }
            if(paramVo.getChooseItems().contains("os")){
                os = flowReserve.getOs();
                if(StringUtils.isEmpty(os)){
                    os = "-";
                }
                chooseItems.append(os).append("|");
            }
            if(paramVo.getChooseItems().contains("resolutionRatio")){
                resolutionRatio = flowReserve.getResolutionRatio();
                if(StringUtils.isEmpty(resolutionRatio)){
                    resolutionRatio = "-";
                }
                chooseItems.append(resolutionRatio).append("|");
            }
            if(paramVo.getChooseItems().contains("deviceType")){
                deviceType = flowReserve.getDeviceType();
                if(StringUtils.isEmpty(deviceType)){
                    deviceType = "-";
                }
                chooseItems.append(deviceType).append("|");
            }
            reserveCount = 0;
            if(reserveCountMap.get(chooseItems.toString()) != null){
                reserveCount = reserveCountMap.get(chooseItems.toString());
            }
            totalCount ++;
            reserveCount ++;
            reserveCountMap.put(chooseItems.toString(), reserveCount);
        }

        String chooseItem;
        String[] valueList;
        JSONObject tableItem;
        String proportion;
        List<String> titleList = new ArrayList<>();
        List<JSONObject> tableItemList = new ArrayList<>();
        List<Integer> contentList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        Iterator<String> iterator = reserveCountMap.keySet().iterator();
        String[] keyList = paramVo.getChooseItems().split(",");
        while (iterator.hasNext()) {
            chooseItem = iterator.next();
            valueList = chooseItem.split("\\|");
            titleList.add(valueList[0]);

            tableItem = new JSONObject();
            tableItem.put("name", valueList[0]);

            for(int i=0; i<valueList.length; i++) {
                tableItem.put(keyList[i], valueList[i]);
            }

            reserveCount = 0;
            if (reserveCountMap.get(chooseItem) != null) {
                reserveCount = reserveCountMap.get(chooseItem);
            }
            contentList.add(reserveCount);
            tableItem.put("value", reserveCount);

            proportion = "0.00";
            if(totalCount != 0){
                proportion = decimalFormat.format(reserveCount * 100.0d / totalCount);
            }
            tableItem.put("proportion", proportion + "%");

            tableItemList.add(tableItem);
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("contentList", contentList);
        resultJson.put("tableItemList", tableItemList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getReserveItem")
    public ResultVo getReserveItem(DashboardClientParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("id,date,os,browser,resolutionRatio,deviceType");
        paramPagerVo.setFeeType(FeeTypeEnum.FEE_TYPE_RESERVE.getText());
        paramPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        String browser, os, resolutionRatio, deviceType;
        int reserveCount, totalCount = 0;
        StringBuilder chooseItems;
        Map<String, Integer> reserveCountMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            chooseItems = new StringBuilder();
            if(paramVo.getChooseItems().contains("browser")){
                browser = flowReserve.getBrowser();
                if(StringUtils.isEmpty(browser) || "-".equals(browser)){
                    browser = "-";
                }
                if(browser.indexOf(" ") > 0) {
                    browser = browser.substring(0, browser.indexOf(" "));
                }
                chooseItems.append(browser).append("|");
            }
            if(paramVo.getChooseItems().contains("os")){
                os = flowReserve.getOs();
                if(StringUtils.isEmpty(os) || "-".equals(os)){
                    os = "-";
                }
                chooseItems.append(os).append("|");
            }
            if(paramVo.getChooseItems().contains("resolutionRatio")){
                resolutionRatio = flowReserve.getResolutionRatio();
                if(StringUtils.isEmpty(resolutionRatio) || "-".equals(resolutionRatio)){
                    resolutionRatio = "-";
                }
                chooseItems.append(resolutionRatio).append("|");
            }
            if(paramVo.getChooseItems().contains("deviceType")){
                deviceType = flowReserve.getDeviceType();
                if(StringUtils.isEmpty(deviceType) || "-".equals(deviceType)){
                    deviceType = "-";
                }
                chooseItems.append(deviceType).append("|");
            }
            reserveCount = 0;
            if(reserveCountMap.get(chooseItems.toString()) != null){
                reserveCount = reserveCountMap.get(chooseItems.toString());
            }
            totalCount ++;
            reserveCount ++;
            reserveCountMap.put(chooseItems.toString(), reserveCount);
        }

        String chooseItem;
        String[] keyList, valueList;
        JSONObject tableItem;
        String proportion;
        List<JSONObject> tableItemList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        Iterator<String> iterator = reserveCountMap.keySet().iterator();

        keyList = paramVo.getChooseItems().split(",");
        while (iterator.hasNext()) {
            chooseItem = iterator.next();
            valueList = chooseItem.split("\\|");

            tableItem = new JSONObject();
            for(int i=0; i<keyList.length; i++) {
                tableItem.put(keyList[i], valueList[i]);
            }

            reserveCount = 0;
            if (reserveCountMap.get(chooseItem) != null) {
                reserveCount = reserveCountMap.get(chooseItem);
            }
            tableItem.put("value", reserveCount);

            proportion = "0.00";
            if(totalCount != 0){
                proportion = decimalFormat.format(reserveCount * 100.0d / totalCount);
            }
            tableItem.put("proportion", proportion + "%");

            tableItemList.add(tableItem);
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("tableItemList", tableItemList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }



    private void initDashboardParam(DashboardClientParamVo dashboardParamVo){
        if(dashboardParamVo == null){
            dashboardParamVo = new DashboardClientParamVo();
        }
        if(StringUtils.isEmpty(dashboardParamVo.getChooseDate())){
            String endDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
            String startDate = DateUtils.formatDate(new Date(), "yyyy-MM") + "-01";
            dashboardParamVo.setChooseDate(startDate + " - " + endDate);
        }
        if(StringUtils.hasText(dashboardParamVo.getPlatformName())){
            dashboardParamVo.setPlatformName(dashboardParamVo.getPlatformName().toUpperCase());
        }
    }
}
