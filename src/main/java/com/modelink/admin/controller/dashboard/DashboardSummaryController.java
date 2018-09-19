package com.modelink.admin.controller.dashboard;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.vo.DashboardSummaryParamVo;
import com.modelink.common.enums.DateTypeEnum;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.enums.TransformCycleEnum;
import com.modelink.common.utils.DataUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.*;
import com.modelink.reservation.service.*;
import com.modelink.reservation.vo.*;
import org.apache.xmlbeans.impl.jam.JSourcePosition;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping("/admin/dashboard/summary")
public class DashboardSummaryController {

    @Resource
    private AbnormalService abnormalService;
    @Resource
    private UnderwriteService underwriteService;
    @Resource
    private RepellentService repellentService;
    @Resource
    private MediaItemService mediaItemService;
    @Resource
    private FlowReserveService flowReserveService;

    @RequestMapping
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/dashboard/summary");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/getConsumeTrend")
    public ResultVo getConsumeTrend(DashboardSummaryParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        // 初始化默认参数
        initDashboardParam(paramVo);

        // 查询符合条件的数据
        MediaItemParamPagerVo paramPagerVo = new MediaItemParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("id,date,speedCost");
        paramPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(paramPagerVo);

        // 处理数据
        String dateKey;
        Double amount, consumeTotalAmount = 0.00d;
        Map<String, Object> date2ListMap = DataUtils.initResultMap(paramVo.getChooseDate(), DateTypeEnum.日.getValue(), "double");
        for(MediaItem mediaItem : mediaItemList){
            dateKey = DataUtils.getDateKeyByDateType(mediaItem.getDate(), DateTypeEnum.日.getValue());
            amount = (double)date2ListMap.get(dateKey);
            if(StringUtils.hasText(mediaItem.getSpeedCost()) && !"-".equals(mediaItem.getSpeedCost())) {
                amount += Double.parseDouble(mediaItem.getSpeedCost());
                consumeTotalAmount += Double.parseDouble(mediaItem.getSpeedCost());
            }
            date2ListMap.put(dateKey, amount);
        }

        // 构建返回数据
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        JSONObject resultJson = formResultJsonWithKeySort(date2ListMap);
        resultJson.put("consumeTotalAmount", decimalFormat.format(consumeTotalAmount));
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getConsumePieByDate")
    public ResultVo getConsumePieByDate(String date, Long merchantId, String platformName){
        ResultVo resultVo = new ResultVo();
        String dateKey = DateUtils.dateFormatTransform(date, "yyyyMMdd", "yyyy-MM-dd");
        // 查询符合条件的数据
        MediaItemParamPagerVo paramPagerVo = new MediaItemParamPagerVo();
        paramPagerVo.setChooseDate(dateKey + " - " + dateKey);
        paramPagerVo.setMerchantId(merchantId);
        paramPagerVo.setPlatformName(platformName);
        paramPagerVo.setColumnFieldIds("id,advertiseActive,speedCost");
        paramPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(paramPagerVo);

        String advertiseActive;
        Double speedCost;
        Map<String, Double> speedCostMap = new HashMap<>();
        for (MediaItem mediaItem : mediaItemList) {
            advertiseActive = mediaItem.getAdvertiseActive();
            if (StringUtils.isEmpty(advertiseActive)) {
                advertiseActive = "-";
            }

            speedCost = 0.00d;
            if (speedCostMap.get(advertiseActive) != null) {
                speedCost = speedCostMap.get(advertiseActive);
            }

            if (StringUtils.hasText(mediaItem.getSpeedCost()) && !"-".equals(mediaItem.getSpeedCost())) {
                speedCost += Double.parseDouble(mediaItem.getSpeedCost());
            }
            speedCostMap.put(advertiseActive, speedCost);
        }

        JSONObject tableItem;
        List<JSONObject> tableItemList = new ArrayList<>();
        Iterator<String> iterator = speedCostMap.keySet().iterator();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        while (iterator.hasNext()) {
            advertiseActive = iterator.next();
            tableItem = new JSONObject();
            tableItem.put("name", advertiseActive);
            tableItem.put("value", decimalFormat.format(speedCostMap.get(advertiseActive)));
            tableItemList.add(tableItem);
        }

        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(tableItemList);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getConsumeTableByDate")
    public ResultVo getConsumeTableByDate(String date, Long merchantId, String platformName, String advertiseActive){
        ResultVo resultVo = new ResultVo();
        String dateKey = DateUtils.dateFormatTransform(date, "yyyyMMdd", "yyyy-MM-dd");
        // 查询符合条件的数据
        MediaItemParamPagerVo paramPagerVo = new MediaItemParamPagerVo();
        paramPagerVo.setChooseDate(dateKey + " - " + dateKey);
        paramPagerVo.setMerchantId(merchantId);
        paramPagerVo.setPlatformName(platformName);
        paramPagerVo.setAdvertiseActive(advertiseActive);
        paramPagerVo.setColumnFieldIds("id,advertiseSeries,keyWord,speedCost");
        paramPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(paramPagerVo);

        Double speedCost;
        String advertiseSeries, keyword, key;
        Map<String, Double> speedCostMap = new HashMap<>();
        for (MediaItem mediaItem : mediaItemList) {
            advertiseSeries = mediaItem.getAdvertiseSeries();
            if (StringUtils.isEmpty(advertiseSeries)) {
                advertiseSeries = "-";
            }
            keyword = mediaItem.getKeyWord();
            if (StringUtils.isEmpty(keyword)) {
                keyword = "-";
            }
            key = advertiseSeries + "+" + keyword;
            speedCost = 0.00d;
            if (speedCostMap.get(key) != null) {
                speedCost = speedCostMap.get(key);
            }
            if (StringUtils.hasText(mediaItem.getSpeedCost()) && !"-".equals(mediaItem.getSpeedCost())) {
                speedCost += Double.parseDouble(mediaItem.getSpeedCost());
            }
            speedCostMap.put(key, speedCost);
        }

        JSONObject tableItem;
        String[] keyArray;
        List<JSONObject> tableItemList = new ArrayList<>();
        Iterator<String> iterator = speedCostMap.keySet().iterator();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        while (iterator.hasNext()) {
            key = iterator.next();
            keyArray = key.split("\\+");
            tableItem = new JSONObject();
            tableItem.put("advertiseSeries", keyArray[0]);
            tableItem.put("keyword", keyArray[1]);
            tableItem.put("speedCost", decimalFormat.format(speedCostMap.get(key)));
            tableItemList.add(tableItem);
        }

        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(tableItemList);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getTransformSummary")
    public ResultVo getTransformSummary(DashboardSummaryParamVo paramVo){
        String dateKey;
        JSONObject tempJsonObject;
        JSONObject resultJson = new JSONObject();
        ResultVo resultVo = new ResultVo();

        // 初始化默认参数
        initDashboardParam(paramVo);

        /** 查询预约数量 **/
        FlowReserveParamPagerVo flowReserveParamPagerVo = new FlowReserveParamPagerVo();
        flowReserveParamPagerVo.setChooseDate(paramVo.getChooseDate());
        flowReserveParamPagerVo.setMerchantId(paramVo.getMerchantId());
        flowReserveParamPagerVo.setPlatformName(paramVo.getPlatformName());
        flowReserveParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        flowReserveParamPagerVo.setColumnFieldIds("date,reserveMobile");
        flowReserveParamPagerVo.setFeeType(FlowReserve.FEE_TYPE_RESERVE);
        flowReserveParamPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(flowReserveParamPagerVo);

        int reserveCount, reserveTotalCount = 0;
        Map<String, Object> statCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), DateTypeEnum.日.getValue(), "int");
        for (FlowReserve flowReserve : flowReserveList) {
            dateKey = DataUtils.getDateKeyByDateType(flowReserve.getDate(), DateTypeEnum.日.getValue());
            reserveCount = 0;
            if(statCountMap.get(dateKey) != null){
                reserveCount = (Integer)statCountMap.get(dateKey);
            }
            reserveCount ++;
            reserveTotalCount ++;
            statCountMap.put(dateKey, reserveCount);
        }
        tempJsonObject = formResultJsonWithKeySort(statCountMap);
        resultJson.put("reserveTotalCount", reserveTotalCount);
        resultJson.put("titleList", tempJsonObject.getJSONArray("titleList"));
        resultJson.put("reserveCountList", tempJsonObject.getJSONArray("contentList"));
        /** 查询预约数量 **/

        /** 查询承保件数 **/
        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setDateField("reserveDate");
        paramPagerVo.setColumnFieldIds("id,reserveDate,platformName,advertiseActive,insuranceFee");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        int insuranceCount, underwriteTotalCount = 0;
        statCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), DateTypeEnum.日.getValue(), "int");
        for (Underwrite underwrite : underwriteList) {
            dateKey = DataUtils.getDateKeyByDateType(underwrite.getReserveDate(), DateTypeEnum.日.getValue());
            insuranceCount = 0;
            if(statCountMap.get(dateKey) != null){
                insuranceCount = (Integer)statCountMap.get(dateKey);
            }
            insuranceCount ++;
            underwriteTotalCount ++;
            statCountMap.put(dateKey, insuranceCount);
        }
        tempJsonObject = formResultJsonWithKeySort(statCountMap);
        resultJson.put("underwriteTotalCount", underwriteTotalCount);
        resultJson.put("insuranceCountList", tempJsonObject.getJSONArray("contentList"));
        /** 查询承保件数 **/

        /** 查询承保保费 **/
        // 保费汇总
        double insuranceTotalAmount, underwriteTotalAmount = 0.0d;
        Map<String, Object> totalAmountMap = DataUtils.initResultMap(paramVo.getChooseDate(), DateTypeEnum.日.getValue(), "double");
        for (Underwrite underwrite : underwriteList) {
            dateKey = DataUtils.getDateKeyByDateType(underwrite.getReserveDate(), DateTypeEnum.日.getValue());
            insuranceTotalAmount = 0.00;
            if(totalAmountMap.get(dateKey) != null){
                insuranceTotalAmount = (double)totalAmountMap.get(dateKey);
            }
            if(StringUtils.hasText(underwrite.getInsuranceFee()) && !"-".equals(underwrite.getInsuranceFee())) {
                insuranceTotalAmount += Double.valueOf(underwrite.getInsuranceFee());
                underwriteTotalAmount += Double.valueOf(underwrite.getInsuranceFee());
            }
            totalAmountMap.put(dateKey, insuranceTotalAmount);
        }
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        tempJsonObject = formResultJsonWithKeySort(totalAmountMap);
        resultJson.put("underwriteTotalAmount", decimalFormat.format(underwriteTotalAmount));
        resultJson.put("insuranceFeeList", tempJsonObject.getJSONArray("contentList"));
        /** 查询承保保费 **/

        // 构建返回数据
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getTransformSummaryByDate")
    public ResultVo getTransformSummaryByDate(String date, Long merchantId, String platformName, String advertiseActive){

        ResultVo resultVo = new ResultVo();

        String dateKey = DateUtils.dateFormatTransform(date, "yyyyMMdd", "yyyy-MM-dd");
        /** 查询预约数量 **/
        FlowReserveParamPagerVo flowReserveParamPagerVo = new FlowReserveParamPagerVo();
        flowReserveParamPagerVo.setChooseDate(dateKey + " - " + dateKey);
        flowReserveParamPagerVo.setMerchantId(merchantId);
        flowReserveParamPagerVo.setPlatformName(platformName);
        flowReserveParamPagerVo.setAdvertiseActive(advertiseActive);
        flowReserveParamPagerVo.setColumnFieldIds("id,date,advertiseActive,advertiseSeries,advertiseDesc");
        flowReserveParamPagerVo.setFeeType(FlowReserve.FEE_TYPE_RESERVE);
        flowReserveParamPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(flowReserveParamPagerVo);

        String key;
        int reserveCount;
        Set<String> keySet = new HashSet<>();
        Map<String, Integer> reserveCountMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            if(StringUtils.hasText(advertiseActive)){
                key = flowReserve.getAdvertiseSeries() + "+" + flowReserve.getAdvertiseDesc();
            }else{
                key = flowReserve.getAdvertiseActive();
            }
            keySet.add(key);

            reserveCount = 0;
            if(reserveCountMap.get(key) != null){
                reserveCount = reserveCountMap.get(key);
            }
            reserveCount ++;
            reserveCountMap.put(key, reserveCount);
        }
        /** 查询预约数量 **/

        /** 查询承保件数和保费 **/
        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(dateKey + " - " + dateKey);
        paramPagerVo.setMerchantId(merchantId);
        paramPagerVo.setPlatformName(platformName);
        paramPagerVo.setAdvertiseActive(advertiseActive);
        paramPagerVo.setDateField("reserveDate");
        paramPagerVo.setColumnFieldIds("id,reserveDate,advertiseActive,keyword,advertiseSeries,insuranceFee");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        int underwriteCount;
        double underwriteAmount;
        Map<String, Integer> underwriteCountMap = new HashMap<>();
        Map<String, Double> underwriteAmountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            if(StringUtils.hasText(advertiseActive)){
                key = underwrite.getAdvertiseSeries() + "+" + underwrite.getKeyword();
            }else{
                key = underwrite.getAdvertiseActive();
            }
            keySet.add(key);
            underwriteCount = 0;
            if(underwriteCountMap.get(key) != null){
                underwriteCount = underwriteCountMap.get(key);
            }
            underwriteCount ++;
            underwriteCountMap.put(key, underwriteCount);

            underwriteAmount = 0.00;
            if(underwriteAmountMap.get(key) != null){
                underwriteAmount = underwriteAmountMap.get(key);
            }
            if(StringUtils.hasText(underwrite.getInsuranceFee()) && !"-".equals(underwrite.getInsuranceFee())) {
                underwriteAmount += Double.valueOf(underwrite.getInsuranceFee());
            }
            underwriteAmountMap.put(key, underwriteAmount);
        }
        /** 查询承保件数和保费 **/
        JSONObject tableItem;
        String[] keywordArray;
        List<JSONObject> tableItemList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        for (String keyword : keySet) {
            tableItem = new JSONObject();
            if (StringUtils.hasText(advertiseActive)) {
                keywordArray = keyword.split("\\+");
                tableItem.put("advertiseSeries", keywordArray[0]);
                tableItem.put("keyword", keywordArray[1]);
            } else {
                tableItem.put("advertiseActive", keyword);
            }

            if (reserveCountMap.get(keyword) == null) {
                tableItem.put("reserveCount", 0);
            } else {
                tableItem.put("reserveCount", reserveCountMap.get(keyword));
            }

            if (underwriteCountMap.get(keyword) == null) {
                tableItem.put("underwriteCount", 0);
            } else {
                tableItem.put("underwriteCount", underwriteCountMap.get(keyword));
            }

            if (underwriteAmountMap.get(keyword) == null) {
                tableItem.put("underwriteAmount", 0);
            } else {
                tableItem.put("underwriteAmount", decimalFormat.format(underwriteAmountMap.get(keyword)));
            }

            tableItemList.add(tableItem);
        }

        // 构建返回数据
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(tableItemList);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getAbnormalCount")
    public ResultVo getAbnormalCount(DashboardSummaryParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        AbnormalParamPagerVo paramPagerVo = new AbnormalParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("id,reserveDate,problemData");
        paramPagerVo.setDateField("reserveDate");
        paramPagerVo.setProblemData("是");
        List<Abnormal> abnormalList = abnormalService.findListByParam(paramPagerVo);

        Set<String> mobileSet = new HashSet<>();
        for (Abnormal abnormal : abnormalList) {
            mobileSet.add(abnormal.getMobile());
        }

        List<FlowReserve> flowReserveList = flowReserveService.findListByMobiles(mobileSet, "date");
        String dateKey;
        int abnormalCount, abnormalTotalCount = 0;
        Map<String, Object> statCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), DateTypeEnum.日.getValue(), "int");
        for (FlowReserve flowReserve : flowReserveList) {
            if(StringUtils.hasText(paramVo.getPlatformName()) &&
                    !paramVo.getPlatformName().equals(flowReserve.getPlatformName())){
                continue;
            }
            if(StringUtils.hasText(paramVo.getAdvertiseActive()) &&
                    !paramVo.getAdvertiseActive().equals(flowReserve.getAdvertiseActive())){
                continue;
            }
            dateKey = DataUtils.getDateKeyByDateType(flowReserve.getDate(), DateTypeEnum.日.getValue());
            abnormalCount = 0;
            if(statCountMap.get(dateKey) != null){
                abnormalCount = (Integer)statCountMap.get(dateKey);
            }
            abnormalCount ++;
            abnormalTotalCount ++;
            statCountMap.put(dateKey, abnormalCount);
        }

        JSONObject resultJson = formResultJsonWithKeySort(statCountMap);
        resultJson.put("abnormalTotalCount", abnormalTotalCount);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getRepellentAmount")
    public ResultVo getRepellentAmount(DashboardSummaryParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        String dateKey;
        // 退保保费汇总
        RepellentParamPagerVo repellentParamPagerVo = new RepellentParamPagerVo();
        repellentParamPagerVo.setChooseDate(paramVo.getChooseDate());
        repellentParamPagerVo.setMerchantId(paramVo.getMerchantId());
        repellentParamPagerVo.setColumnFieldIds("id,hesitateDate,insuranceNo");
        repellentParamPagerVo.setDateField("hesitateDate");
        List<Repellent> repellentList = repellentService.findListByParam(repellentParamPagerVo);

        Set<String> insuranceNoSet = new HashSet<>();
        for (Repellent repellent : repellentList) {
            if(StringUtils.isEmpty(repellent.getInsuranceNo())){
                continue;
            }
            insuranceNoSet.add(repellent.getInsuranceNo());
        }
        List<Underwrite> underwriteList = underwriteService.findListByInsuranceNoSet(
                insuranceNoSet, "id,insuranceNo,platformName,advertiseActive");
        Map<String, String> underwriteMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            underwriteMap.put(underwrite.getInsuranceNo(), underwrite.getPlatformName() + "|" + underwrite.getAdvertiseActive());
        }

        String insuranceNo;
        String[] platformAdvertise;
        double refundTotalAmount;
        double repellentTotalAmount = 0.0d;
        Map<String, Object> refundAmountMap = DataUtils.initResultMap(paramVo.getChooseDate(), DateTypeEnum.日.getValue(), "double");
        for (Repellent repellent : repellentList) {
            insuranceNo = repellent.getInsuranceNo();
            if(StringUtils.isEmpty(insuranceNo) || underwriteMap.get(insuranceNo) == null){
                continue;
            }
            platformAdvertise = underwriteMap.get(insuranceNo).split("\\|");
            if(StringUtils.hasText(paramVo.getPlatformName()) && !paramVo.getPlatformName().equals(platformAdvertise[0])){
                continue;
            }
            if(StringUtils.hasText(paramVo.getAdvertiseActive()) && !platformAdvertise[1].contains(paramVo.getAdvertiseActive())){
                continue;
            }

            dateKey = DataUtils.getDateKeyByDateType(repellent.getHesitateDate(), DateTypeEnum.日.getValue());
            refundTotalAmount = 0.00;
            if(refundAmountMap.get(dateKey) != null){
                refundTotalAmount = (double)refundAmountMap.get(dateKey);
            }
            if(StringUtils.hasText(repellent.getInsuranceFee()) && !"-".equals(repellent.getInsuranceFee())) {
                refundTotalAmount += Double.valueOf(repellent.getInsuranceFee());
                repellentTotalAmount += Double.valueOf(repellent.getInsuranceFee());
            }
            refundAmountMap.put(dateKey, refundTotalAmount);
        }

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        JSONObject resultJson = formResultJsonWithKeySort(refundAmountMap);
        resultJson.put("repellentTotalAmount", decimalFormat.format(repellentTotalAmount));
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getTransformRate")
    public ResultVo getTransformRate(DashboardSummaryParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        String dateKey;
        // 计算统计周期内保单号总量
        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("finishDate,insuranceNo");
        paramPagerVo.setDateField("finishDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);
        int totalCount = underwriteList.size();

        // 计算统计周期内点击量总数
        MediaItemParamPagerVo mediaItemParamPagerVo = new MediaItemParamPagerVo();
        mediaItemParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaItemParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaItemParamPagerVo.setColumnFieldIds("id,date,clickCount");
        mediaItemParamPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);
        int clickTotalCount = 0;
        for (MediaItem mediaItem : mediaItemList) {
            clickTotalCount += mediaItem.getClickCount();
        }

        double transformRate = 0.0d;
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        if(clickTotalCount != 0){
            transformRate = totalCount * 1000 / clickTotalCount;
        }

        JSONArray jsonArray = new JSONArray();
        JSONObject transformRateJson = new JSONObject();
        transformRateJson.put("name", "总转化率");
        transformRateJson.put("value", decimalFormat.format(transformRate));
        jsonArray.add(transformRateJson);

        JSONObject resultJson = new JSONObject();
        resultJson.put("contentList", jsonArray);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getTransformCycle")
    public ResultVo getTransformCycle(DashboardSummaryParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("id,reserveDate,reserveMobile");
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        Set<String> mobileSet = new HashSet<>();
        for (Underwrite underwrite : underwriteList) {
            mobileSet.add(underwrite.getReserveMobile());
        }

        List<FlowReserve> flowReserveList = flowReserveService.findListByMobiles(mobileSet, "date");
        Map<String, String> mobile2DateMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            mobile2DateMap.put(flowReserve.getReserveMobile(), flowReserve.getDate());
        }

        String mobile;
        int difference, totalCount;
        int totalDifference = 0, reserveTotalCount = 0;
        String reserveDate, finishDate;

        Map<Integer, Integer> transformCycleMap = new HashMap<>();
        transformCycleMap.put(TransformCycleEnum.from_1.getValue(), 0);
        transformCycleMap.put(TransformCycleEnum.from_2.getValue(), 0);
        transformCycleMap.put(TransformCycleEnum.from_3.getValue(), 0);
        transformCycleMap.put(TransformCycleEnum.from_4.getValue(), 0);
        transformCycleMap.put(TransformCycleEnum.from_5.getValue(), 0);
        transformCycleMap.put(TransformCycleEnum.from_6.getValue(), 0);
        transformCycleMap.put(TransformCycleEnum.from_7_14.getValue(), 0);
        transformCycleMap.put(TransformCycleEnum.from_15_30.getValue(), 0);
        transformCycleMap.put(TransformCycleEnum.from_31_60.getValue(), 0);
        transformCycleMap.put(TransformCycleEnum.from_61_90.getValue(), 0);
        transformCycleMap.put(TransformCycleEnum.from_91.getValue(), 0);
        for (Underwrite underwrite : underwriteList) {
            mobile = underwrite.getReserveMobile();
            if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(mobile2DateMap.get(mobile))){
                continue;
            }
            finishDate = underwrite.getFinishDate();
            reserveDate = underwrite.getReserveDate();
            difference = DateUtils.getDateDifference(reserveDate, finishDate);
            if(difference <= 0){
                difference = 0;
            }else if(difference > 90){
                totalCount = transformCycleMap.get(TransformCycleEnum.from_91.getValue());
                totalCount ++;
                transformCycleMap.put(TransformCycleEnum.from_91.getValue(), totalCount);
            }else if(difference > 60){
                totalCount = transformCycleMap.get(TransformCycleEnum.from_61_90.getValue());
                totalCount ++;
                transformCycleMap.put(TransformCycleEnum.from_61_90.getValue(), totalCount);
            }else if(difference > 30){
                totalCount = transformCycleMap.get(TransformCycleEnum.from_31_60.getValue());
                totalCount ++;
                transformCycleMap.put(TransformCycleEnum.from_31_60.getValue(), totalCount);
            }else if(difference > 14){
                totalCount = transformCycleMap.get(TransformCycleEnum.from_15_30.getValue());
                totalCount ++;
                transformCycleMap.put(TransformCycleEnum.from_15_30.getValue(), totalCount);
            }else if(difference > 7){
                totalCount = transformCycleMap.get(TransformCycleEnum.from_7_14.getValue());
                totalCount ++;
                transformCycleMap.put(TransformCycleEnum.from_7_14.getValue(), totalCount);
            }else if(difference == 6){
                totalCount = transformCycleMap.get(TransformCycleEnum.from_6.getValue());
                totalCount ++;
                transformCycleMap.put(TransformCycleEnum.from_6.getValue(), totalCount);
            }else if(difference == 5){
                totalCount = transformCycleMap.get(TransformCycleEnum.from_5.getValue());
                totalCount ++;
                transformCycleMap.put(TransformCycleEnum.from_5.getValue(), totalCount);
            }else if(difference == 4){
                totalCount = transformCycleMap.get(TransformCycleEnum.from_4.getValue());
                totalCount ++;
                transformCycleMap.put(TransformCycleEnum.from_4.getValue(), totalCount);
            }else if(difference == 3){
                totalCount = transformCycleMap.get(TransformCycleEnum.from_3.getValue());
                totalCount ++;
                transformCycleMap.put(TransformCycleEnum.from_3.getValue(), totalCount);
            }else if(difference == 2){
                totalCount = transformCycleMap.get(TransformCycleEnum.from_2.getValue());
                totalCount ++;
                transformCycleMap.put(TransformCycleEnum.from_2.getValue(), totalCount);
            }else if(difference == 1){
                totalCount = transformCycleMap.get(TransformCycleEnum.from_1.getValue());
                totalCount ++;
                transformCycleMap.put(TransformCycleEnum.from_1.getValue(), totalCount);
            }
            totalDifference += difference;
            reserveTotalCount ++;
        }

        // 组装数据视图JSON
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        JSONObject resultJson = new JSONObject();
        JSONArray titleArray = new JSONArray();
        JSONArray contentArray = new JSONArray();
        Set<Integer> keySet = transformCycleMap.keySet();
        Integer[] keyArray = keySet.toArray(new Integer[keySet.size()]);
        Arrays.sort(keyArray);

        for(Integer key : keyArray){
            titleArray.add(TransformCycleEnum.getTextByValue(key));
            contentArray.add(transformCycleMap.get(key));
        }
        if(reserveTotalCount == 0) reserveTotalCount = 1;
        resultJson.put("transformCycle", decimalFormat.format(totalDifference / reserveTotalCount));
        resultJson.put("titleList", titleArray);
        resultJson.put("contentList", contentArray);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getCostSummary")
    public ResultVo getCostSummary(DashboardSummaryParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("id,date");
        paramPagerVo.setFeeType(FlowReserve.FEE_TYPE_RESERVE);
        paramPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        String dateKey;
        int reserveCount, reserveTotalCount = 0;
        Map<String, Object> reserveCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), DateTypeEnum.日.getValue(), "int");
        for (FlowReserve flowReserve : flowReserveList) {
            dateKey = DataUtils.getDateKeyByDateType(flowReserve.getDate(), DateTypeEnum.日.getValue());
            reserveCount = 0;
            if(reserveCountMap.get(dateKey) != null){
                reserveCount = (int)reserveCountMap.get(dateKey);
            }
            reserveCount ++;
            reserveTotalCount ++;
            reserveCountMap.put(dateKey, reserveCount);
        }

        int clickCount;
        double totalAmount, consumeTotalAmount = 0.0d;
        MediaItemParamPagerVo mediaItemParamPagerVo = new MediaItemParamPagerVo();
        mediaItemParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaItemParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaItemParamPagerVo.setPlatformName(paramVo.getPlatformName());
        mediaItemParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        mediaItemParamPagerVo.setColumnFieldIds("date,clickCount,speedCost");
        mediaItemParamPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);
        Map<String, Object> statAmountMap = DataUtils.initResultMap(paramVo.getChooseDate(), DateTypeEnum.日.getValue(), "double");
        Map<String, Object> clickCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), DateTypeEnum.日.getValue(), "int");
        for (MediaItem mediaItem : mediaItemList) {
            dateKey = DataUtils.getDateKeyByDateType(mediaItem.getDate(), DateTypeEnum.日.getValue());
            clickCount = (int)clickCountMap.get(dateKey);
            if(mediaItem.getClickCount() != null) {
                clickCount += mediaItem.getClickCount();
            }
            clickCountMap.put(dateKey, clickCount);

            totalAmount = (double)statAmountMap.get(dateKey);
            if(StringUtils.hasText(mediaItem.getSpeedCost()) && !"-".equals(mediaItem.getSpeedCost())) {
                totalAmount += Double.valueOf(mediaItem.getSpeedCost());
                consumeTotalAmount += Double.valueOf(mediaItem.getSpeedCost());
            }
            statAmountMap.put(dateKey, totalAmount);
        }


        double amount = 0.00;
        Map<String, Object> clickCostResultMap = new HashMap<>();
        Map<String, Object> transformCostResultMap = new HashMap<>();
        Iterator<String> iterator = statAmountMap.keySet().iterator();
        while(iterator.hasNext()){
            dateKey = iterator.next();

            if(StringUtils.isEmpty(statAmountMap.get(dateKey))){
                amount = 0.00;
            }else{
                amount = (double)statAmountMap.get(dateKey);
            }
            // 计算转化成本
            if(StringUtils.isEmpty(reserveCountMap.get(dateKey))){
                reserveCount = 0;
            }else{
                reserveCount = (int)reserveCountMap.get(dateKey);
            }
            if(reserveCount == 0){
                transformCostResultMap.put(dateKey, 0);
            }else{
                transformCostResultMap.put(dateKey, amount / reserveCount);
            }
            // 计算点击成本
            if(StringUtils.isEmpty(clickCountMap.get(dateKey))){
                clickCount = 0;
            }else{
                clickCount = (int)clickCountMap.get(dateKey);
            }
            if(clickCount == 0){
                clickCostResultMap.put(dateKey, 0);
            }else{
                clickCostResultMap.put(dateKey, amount / clickCount);
            }
        }

        if(reserveTotalCount == 0) reserveTotalCount = 1;
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        JSONObject clickCostResultJson = formResultJsonWithKeySort(clickCostResultMap);
        JSONObject transformCostResultJson = formResultJsonWithKeySort(transformCostResultMap);
        JSONObject resultJson = new JSONObject();
        resultJson.put("transformCost", decimalFormat.format(consumeTotalAmount / reserveTotalCount));
        resultJson.put("titleList", clickCostResultJson.get("titleList"));
        resultJson.put("clickCostList", clickCostResultJson.get("contentList"));
        resultJson.put("transformCostList", transformCostResultJson.get("contentList"));
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getTransformCostRank")
    public ResultVo getTransformCostRank(DashboardSummaryParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setDateField("finishDate");
        paramPagerVo.setColumnFieldIds("id,finishDate,platformName,advertiseActive");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        String channelKey;
        int reserveCount;
        Map<String, Integer> channel2CountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            reserveCount = 0;
            channelKey = underwrite.getPlatformName() + "+" + underwrite.getAdvertiseActive();
            if(channel2CountMap.get(channelKey) != null){
                reserveCount = channel2CountMap.get(channelKey);
            }
            reserveCount ++;
            channel2CountMap.put(channelKey, reserveCount);
        }

        double totalAmount;
        MediaItemParamPagerVo mediaItemParamPagerVo = new MediaItemParamPagerVo();
        mediaItemParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaItemParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaItemParamPagerVo.setPlatformName(paramVo.getPlatformName());
        mediaItemParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        mediaItemParamPagerVo.setColumnFieldIds("date,speedCost,platformName,advertiseActive");
        mediaItemParamPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);
        Map<String, Double> channel2AmountMap = new HashMap<>();
        for (MediaItem mediaItem : mediaItemList) {
            totalAmount = 0.0d;
            channelKey = mediaItem.getPlatformName() + "+" + mediaItem.getAdvertiseActive();
            if(channel2AmountMap.get(channelKey) != null) {
                totalAmount = channel2AmountMap.get(channelKey);
            }
            totalAmount += Double.valueOf(mediaItem.getSpeedCost());
            channel2AmountMap.put(channelKey, totalAmount);
        }


        Map<String, Double> transformCostResultMap = new HashMap<>();
        Iterator<String> iterator = channel2CountMap.keySet().iterator();
        while(iterator.hasNext()){
            channelKey = iterator.next();

            if(StringUtils.isEmpty(channel2AmountMap.get(channelKey))){
                totalAmount = 0.00;
            }else{
                totalAmount = channel2AmountMap.get(channelKey);
            }
            // 计算转化成本
            if(StringUtils.isEmpty(channel2CountMap.get(channelKey))){
                reserveCount = 0;
            }else{
                reserveCount = channel2CountMap.get(channelKey);
            }
            if(reserveCount == 0){
                transformCostResultMap.put(channelKey, 0.00d);
            }else{
                transformCostResultMap.put(channelKey, totalAmount / reserveCount);
            }
        }

        JSONObject resultJson = formResultJsonWithDoubleValueSort(transformCostResultMap);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getUnderwriteCountRank")
    public ResultVo getUnderwriteCountRank(DashboardSummaryParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("finishDate,reserveMobile,platformName,advertiseActive");
        paramPagerVo.setDateField("finishDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        String channelKey;
        int underwriteCount;
        Map<String, Integer> statCountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            channelKey = underwrite.getPlatformName() + "+" + underwrite.getAdvertiseActive();
            underwriteCount = 0;
            if(statCountMap.get(channelKey) != null){
                underwriteCount = (Integer)statCountMap.get(channelKey);
            }
            underwriteCount ++;
            statCountMap.put(channelKey, underwriteCount);
        }

        JSONObject resultJson = formResultJsonWithIntegerValueSort(statCountMap);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getUnderwriteAmountRank")
    public ResultVo getUnderwriteAmountRank(DashboardSummaryParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setDateField("finishDate");
        paramPagerVo.setColumnFieldIds("id,finishDate,platformName,advertiseActive,insuranceFee");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        String channelKey;
        double totalAmount;
        Map<String, Double> channel2AmountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            totalAmount = 0.00d;
            channelKey = underwrite.getPlatformName() + "+" + underwrite.getAdvertiseActive();
            if(channel2AmountMap.get(channelKey) != null){
                totalAmount = channel2AmountMap.get(channelKey);
            }
            if(StringUtils.hasText(underwrite.getInsuranceFee()) && !"-".equals(underwrite.getInsuranceFee())) {
                totalAmount += Double.parseDouble(underwrite.getInsuranceFee());
            }
            channel2AmountMap.put(channelKey, totalAmount);
        }

        JSONObject resultJson = formResultJsonWithDoubleValueSort(channel2AmountMap);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }









    /**
     * 初始化传入参数
     * @param paramVo
     * @return
     */
    private void initDashboardParam(DashboardSummaryParamVo paramVo){
        if(paramVo == null){
            paramVo = new DashboardSummaryParamVo();
        }
        if(StringUtils.isEmpty(paramVo.getChooseDate())){
            String endDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
            String startDate = DateUtils.formatDate(new Date(), "yyyy-MM") + "-01";
            paramVo.setChooseDate(startDate + " - " + endDate);
        }
        if(StringUtils.hasText(paramVo.getPlatformName())){
            paramVo.setPlatformName(paramVo.getPlatformName().toUpperCase());
        }
    }
    /**
     * 构建返回数据
     * @param statCountMap
     * @return
     */
    private JSONObject formResultJsonWithKeySort(Map<String, Object> statCountMap){
        JSONObject resultJson = new JSONObject();
        JSONArray titleArray = new JSONArray();
        JSONArray contentArray = new JSONArray();
        Set<String> keySet = statCountMap.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        for(String key : keyArray){
            titleArray.add(key);
            if(statCountMap.get(key) instanceof Double) {
                contentArray.add(decimalFormat.format(statCountMap.get(key)));
            }else{
                contentArray.add(statCountMap.get(key));
            }
        }
        resultJson.put("titleList", titleArray);
        resultJson.put("contentList", contentArray);

        return resultJson;
    }
    /**
     * 构建返回数据
     * @param statCountMap
     * @return
     */
    private JSONObject formResultJsonWithDoubleValueSort(Map<String, Double> statCountMap){
        JSONObject resultJson = new JSONObject();
        JSONArray titleArray = new JSONArray();
        JSONArray contentArray = new JSONArray();

        List<Map.Entry<String, Double>> list = new ArrayList<>(statCountMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        for (Map.Entry<String, Double> mapping : list) {
            titleArray.add(mapping.getKey());
            contentArray.add(decimalFormat.format(mapping.getValue()));
        }

        resultJson.put("titleList", titleArray);
        resultJson.put("contentList", contentArray);

        return resultJson;
    }
    /**
     * 构建返回数据
     * @param statCountMap
     * @return
     */
    private JSONObject formResultJsonWithIntegerValueSort(Map<String, Integer> statCountMap){
        JSONObject resultJson = new JSONObject();
        JSONArray titleArray = new JSONArray();
        JSONArray contentArray = new JSONArray();

        List<Map.Entry<String, Integer>> list = new ArrayList<>(statCountMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (Map.Entry<String, Integer> mapping : list) {
            titleArray.add(mapping.getKey());
            contentArray.add(mapping.getValue());
        }

        resultJson.put("titleList", titleArray);
        resultJson.put("contentList", contentArray);

        return resultJson;
    }
}
