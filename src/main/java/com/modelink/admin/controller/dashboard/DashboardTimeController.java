package com.modelink.admin.controller.dashboard;

import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.vo.DashboardParamVo;
import com.modelink.common.enums.AgePartEnum;
import com.modelink.common.enums.DateTypeEnum;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.utils.DataUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.FlowReserve;
import com.modelink.reservation.bean.MediaItem;
import com.modelink.reservation.bean.Repellent;
import com.modelink.reservation.bean.Underwrite;
import com.modelink.reservation.service.FlowReserveService;
import com.modelink.reservation.service.MediaItemService;
import com.modelink.reservation.service.RepellentService;
import com.modelink.reservation.service.UnderwriteService;
import com.modelink.reservation.vo.FlowReserveParamPagerVo;
import com.modelink.reservation.vo.MediaItemParamPagerVo;
import com.modelink.reservation.vo.UnderwriteParamPagerVo;
import com.modelink.usercenter.bean.Area;
import com.modelink.usercenter.service.AreaService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping("/admin/dashboard/time")
public class DashboardTimeController {

    @Resource
    private UnderwriteService underwriteService;
    @Resource
    private FlowReserveService flowReserveService;
    @Resource
    private MediaItemService mediaItemService;

    @RequestMapping
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/dashboard/time");
        return modelAndView;
    }
    @ResponseBody
    @RequestMapping("/getReserveCountByHour")
    public ResultVo getReserveCountByHour(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("id,date,time");
        paramPagerVo.setFeeType(FlowReserve.FEE_TYPE_RESERVE);
        paramPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        String dateKey;
        int reserveCount;
        int totalCount = 0;
        Map<Integer, Integer> statCountMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            dateKey = DataUtils.getDateKeyByDateType(flowReserve.getDate() + " " + flowReserve.getTime(), paramVo.getDateType());
            reserveCount = 0;
            if(statCountMap.get(Integer.parseInt(dateKey)) != null){
                reserveCount = statCountMap.get(Integer.parseInt(dateKey));
            }
            totalCount ++;
            reserveCount ++;
            statCountMap.put(Integer.parseInt(dateKey), reserveCount);
        }

        JSONObject tableItem;
        List<String> titleList = new ArrayList<>();
        List<Integer> contentList = new ArrayList<>();
        List<JSONObject> tableItemList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        for (int i=0; i<24; i++) {
            titleList.add(i + ":00-" + (i+1) + ":00");
            reserveCount = 0;
            if(statCountMap.get(i) != null) {
                reserveCount = statCountMap.get(i);
            }
            contentList.add(statCountMap.get(i));
            tableItem = new JSONObject();
            tableItem.put("time", i + ":00-" + (i+1) + ":00");
            tableItem.put("reserveCount", reserveCount);
            if(totalCount == 0){
                tableItem.put("proportion", "0%");
            }else {
                tableItem.put("proportion", decimalFormat.format((reserveCount * 100.0d) / totalCount) + "%");
            }
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
    @RequestMapping("/getUnderwriteSummaryByDate")
    public ResultVo getUnderwriteSummaryByDate(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("id,date,time");
        paramPagerVo.setFeeType(FlowReserve.FEE_TYPE_RESERVE);
        paramPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        String dateKey;
        int reserveCount;
        Map<String, Integer> reserveCountMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            dateKey = DataUtils.getDateKeyByDateType(flowReserve.getDate(), paramVo.getDateType());
            reserveCount = 0;
            if(reserveCountMap.get(dateKey) != null){
                reserveCount = reserveCountMap.get(dateKey);
            }
            reserveCount ++;
            reserveCountMap.put(dateKey, reserveCount);
        }

        /** 转化周期计算 **/
        UnderwriteParamPagerVo underwriteParamPagerVo = new UnderwriteParamPagerVo();
        underwriteParamPagerVo.setChooseDate(paramVo.getChooseDate());
        underwriteParamPagerVo.setMerchantId(paramVo.getMerchantId());
        underwriteParamPagerVo.setColumnFieldIds("id,reserveDate,finishDate,insuranceFee");
        underwriteParamPagerVo.setPlatformName(paramVo.getPlatformName());
        underwriteParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        underwriteParamPagerVo.setSource("!产品测保");
        underwriteParamPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);

        List<Underwrite> tempUnderwriteList;
        Map<String, List<Underwrite>> underwriteListMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            dateKey = DataUtils.getDateKeyByDateType(underwrite.getReserveDate(), paramVo.getDateType());
            tempUnderwriteList = new ArrayList<>();
            if(underwriteListMap.get(dateKey) != null){
                tempUnderwriteList = underwriteListMap.get(dateKey);
            }
            tempUnderwriteList.add(underwrite);
            underwriteListMap.put(dateKey, tempUnderwriteList);
        }
        String transformCycle;
        double insuranceAmount;
        int difference, totalDifference;
        String reserveDate, finishDate;

        Map<String, Double> underwriteAmountMap = new HashMap<>();
        Map<String, Integer> underwriteCountMap = new HashMap<>();
        Map<String, String> transformCycleMap = new HashMap<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        Iterator<String> iterator = underwriteListMap.keySet().iterator();
        while(iterator.hasNext()){
            dateKey = iterator.next();
            tempUnderwriteList = underwriteListMap.get(dateKey);
            if(tempUnderwriteList == null || tempUnderwriteList.size() <= 0){
                continue;
            }

            totalDifference = 0;
            insuranceAmount = 0.0d;
            for(Underwrite underwrite : tempUnderwriteList){
                finishDate = underwrite.getFinishDate();
                reserveDate = underwrite.getReserveDate();
                difference = DateUtils.getDateDifference(reserveDate, finishDate);
                totalDifference += difference;
                if(StringUtils.hasText(underwrite.getInsuranceFee()) && !"-".equals(underwrite.getInsuranceFee())) {
                    insuranceAmount += Double.parseDouble(underwrite.getInsuranceFee());
                }
            }
            transformCycle = decimalFormat.format(totalDifference / tempUnderwriteList.size());
            transformCycleMap.put(dateKey, transformCycle);
            underwriteCountMap.put(dateKey, tempUnderwriteList.size());
            underwriteAmountMap.put(dateKey, insuranceAmount);
        }
        /** 转化周期计算 **/



        /** 计算转化成本与点击成本 **/
        int clickCount;
        double consumeAmount;
        MediaItemParamPagerVo mediaItemParamPagerVo = new MediaItemParamPagerVo();
        mediaItemParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaItemParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaItemParamPagerVo.setPlatformName(paramVo.getPlatformName());
        mediaItemParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        mediaItemParamPagerVo.setColumnFieldIds("date,merchantId,platformName,advertiseActive,clickCount,speedCost");
        mediaItemParamPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);
        Map<String, Double> consumeAmountMap = new HashMap<>();
        Map<String, Integer> clickCountMap = new HashMap<>();
        for (MediaItem mediaItem : mediaItemList) {
            dateKey = DataUtils.getDateKeyByDateType(mediaItem.getDate(), paramVo.getDateType());

            clickCount = 0;
            if(clickCountMap.get(dateKey) != null) {
                clickCount = clickCountMap.get(dateKey);
            }
            if(mediaItem.getClickCount() != null) {
                clickCount += mediaItem.getClickCount();
            }
            clickCountMap.put(dateKey, clickCount);

            consumeAmount = 0.0d;
            if(consumeAmountMap.get(dateKey) != null) {
                consumeAmount = consumeAmountMap.get(dateKey);
            }
            if(StringUtils.hasText(mediaItem.getSpeedCost()) && !"-".equals(mediaItem.getSpeedCost())) {
                consumeAmount += Double.valueOf(mediaItem.getSpeedCost());
            }
            consumeAmountMap.put(dateKey, consumeAmount);
        }


        int underwriteCount = 0;
        Map<String, String> transformRateMap = new HashMap<>();
        Map<String, String> clickCostResultMap = new HashMap<>();
        Map<String, String> transformCostMap = new HashMap<>();
        Iterator<String> consumeAmountIterator = consumeAmountMap.keySet().iterator();
        while(consumeAmountIterator.hasNext()){
            dateKey = consumeAmountIterator.next();

            if(StringUtils.isEmpty(consumeAmountMap.get(dateKey))){
                consumeAmount = 0.0d;
            }else{
                consumeAmount = consumeAmountMap.get(dateKey);
            }
            // 计算转化成本
            if(StringUtils.isEmpty(reserveCountMap.get(dateKey))){
                reserveCount = 0;
            }else{
                reserveCount = reserveCountMap.get(dateKey);
            }
            if(reserveCount == 0){
                transformCostMap.put(dateKey, "0.00");
            }else{
                transformCostMap.put(dateKey, decimalFormat.format(consumeAmount / reserveCount));
            }
            // 计算点击成本
            if(StringUtils.isEmpty(clickCountMap.get(dateKey))){
                clickCount = 0;
            }else{
                clickCount = (int)clickCountMap.get(dateKey);
            }
            if(clickCount == 0){
                clickCostResultMap.put(dateKey, "0.00");
            }else{
                clickCostResultMap.put(dateKey, decimalFormat.format(consumeAmount / clickCount));
            }
            // 计算转化率
            if(StringUtils.isEmpty(clickCountMap.get(dateKey))){
                clickCount = 0;
            }else{
                clickCount = (int)clickCountMap.get(dateKey);
            }
            if(clickCount == 0 || underwriteCountMap.get(dateKey) == null){
                transformRateMap.put(dateKey, "0.00%");
            }else{
                underwriteCount = underwriteCountMap.get(dateKey);
                transformRateMap.put(dateKey, decimalFormat.format(underwriteCount * 100.00d / clickCount) + "%");
            }
        }
        /** 计算转化成本与点击成本 **/

        JSONObject tableItem;
        List<JSONObject> tableItemList = new ArrayList<>();
        List<Integer> reserveCountList = new ArrayList<>();
        List<Integer> underwriteCountList = new ArrayList<>();
        List<String> titleList = DataUtils.initDateList(paramVo.getChooseDate(), paramVo.getDateType());
        for (String title : titleList) {
            tableItem = new JSONObject();
            tableItem.put("date", title);

            reserveCount = 0;
            if (reserveCountMap.get(title) != null) {
                reserveCount = reserveCountMap.get(title);
            }
            tableItem.put("reserveCount", reserveCount);
            reserveCountList.add(reserveCount);

            underwriteCount = 0;
            if (underwriteCountMap.get(title) != null) {
                underwriteCount = underwriteCountMap.get(title);
            }
            tableItem.put("underwriteCount", underwriteCount);
            underwriteCountList.add(underwriteCount);

            if (transformCostMap.get(title) == null) {
                tableItem.put("transformCost", 0);
            } else {
                tableItem.put("transformCost", transformCostMap.get(title));
            }
            if (transformCycleMap.get(title) == null) {
                tableItem.put("transformCycle", 0);
            } else {
                tableItem.put("transformCycle", transformCycleMap.get(title));
            }
            if (underwriteAmountMap.get(title) == null) {
                tableItem.put("underwriteAmount", 0);
            } else {
                tableItem.put("underwriteAmount", decimalFormat.format(underwriteAmountMap.get(title)));
            }
            if (transformRateMap.get(title) == null) {
                tableItem.put("transformRate", "0.00%");
            } else {
                tableItem.put("transformRate", transformRateMap.get(title));
            }
            tableItemList.add(tableItem);
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("reserveCountList", reserveCountList);
        resultJson.put("underwriteCountList", underwriteCountList);
        resultJson.put("tableItemList", tableItemList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }



    private void initDashboardParam(DashboardParamVo dashboardParamVo){
        if(dashboardParamVo == null){
            dashboardParamVo = new DashboardParamVo();
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
