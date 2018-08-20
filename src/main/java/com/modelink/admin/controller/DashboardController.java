package com.modelink.admin.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.vo.DashboardParamVo;
import com.modelink.common.enums.AgePartEnum;
import com.modelink.common.enums.DateTypeEnum;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.*;
import com.modelink.reservation.service.*;
import com.modelink.reservation.vo.*;
import com.modelink.usercenter.bean.Area;
import com.modelink.usercenter.service.AreaService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping("/admin/dashboard")
public class DashboardController {

    @Resource
    private AreaService areaService;
    @Resource
    private UnderwriteService underwriteService;
    @Resource
    private FlowReserveService flowReserveService;
    @Resource
    private MediaItemService mediaItemService;
    @Resource
    private AbnormalService abnormalService;
    @Resource
    private RepellentService repellentService;

    @ResponseBody
    @RequestMapping("/getReserveCount")
    public ResultVo getReserveCount(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        String rtnMsg = initDashboardParam(paramVo);
        if(StringUtils.hasText(rtnMsg)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(rtnMsg);
            return resultVo;
        }

        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("date,reserveMobile");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        String dateKey;
        int reserveCount;
        Map<String, Object> statCountMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "int");
        for (FlowReserve flowReserve : flowReserveList) {
            dateKey = getDateKeyByDateType(flowReserve.getDate(), paramVo.getDateType());
            reserveCount = 0;
            if(statCountMap.get(dateKey) != null){
                reserveCount = (Integer)statCountMap.get(dateKey);
            }
            reserveCount ++;
            statCountMap.put(dateKey, reserveCount);
        }

        JSONObject resultJson = formLineEchartResultJson(statCountMap);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getUnderwriteCount")
    public ResultVo getUnderwriteCount(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        String rtnMsg = initDashboardParam(paramVo);
        if(StringUtils.hasText(rtnMsg)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(rtnMsg);
            return resultVo;
        }

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("finishDate,reserveMobile");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        String dateKey;
        int reserveCount;
        Map<String, Object> statCountMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "int");
        for (Underwrite underwrite : underwriteList) {
            dateKey = getDateKeyByDateType(underwrite.getFinishDate(), paramVo.getDateType());
            reserveCount = 0;
            if(statCountMap.get(dateKey) != null){
                reserveCount = (Integer)statCountMap.get(dateKey);
            }
            reserveCount ++;
            statCountMap.put(dateKey, reserveCount);
        }

        JSONObject resultJson = formLineEchartResultJson(statCountMap);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getUnderwriteAmount")
    public ResultVo getUnderwriteAmount(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        String rtnMsg = initDashboardParam(paramVo);
        if(StringUtils.hasText(rtnMsg)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(rtnMsg);
            return resultVo;
        }

        String dateKey;

        // 保费汇总
        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("finishDate,insuranceFee");
        paramPagerVo.setDateField("finishDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);
        double insuranceTotalAmount;
        Map<String, Object> totalAmountMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "double");
        for (Underwrite underwrite : underwriteList) {
            dateKey = getDateKeyByDateType(underwrite.getFinishDate(), paramVo.getDateType());
            insuranceTotalAmount = 0.00;
            if(totalAmountMap.get(dateKey) != null){
                insuranceTotalAmount = (double)totalAmountMap.get(dateKey);
            }
            insuranceTotalAmount += Double.valueOf(underwrite.getInsuranceFee());
            totalAmountMap.put(dateKey, insuranceTotalAmount);
        }

        // 退保保费汇总
        RepellentParamPagerVo repellentParamPagerVo = new RepellentParamPagerVo();
        repellentParamPagerVo.setChooseDate(paramVo.getChooseDate());
        repellentParamPagerVo.setMerchantId(paramVo.getMerchantId());
        repellentParamPagerVo.setColumnFieldIds("id,hesitateDate");
        repellentParamPagerVo.setDateField("hesitateDate");
        List<Repellent> repellentList = repellentService.findListByParam(repellentParamPagerVo);

        double refundTotalAmount;
        Map<String, Object> refundAmountMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "double");
        for (Repellent repellent : repellentList) {
            dateKey = getDateKeyByDateType(repellent.getHesitateDate(), paramVo.getDateType());
            refundTotalAmount = 0.00;
            if(refundAmountMap.get(dateKey) != null){
                refundTotalAmount = (double)refundAmountMap.get(dateKey);
            }
            refundTotalAmount += Double.valueOf(repellent.getInsuranceFee());
            refundAmountMap.put(dateKey, refundTotalAmount);
        }

        // 结算最终结果
        double amount, refundAmount;
        Map<String, Object> resultStatMap = new HashMap<>();
        Iterator<String> iterator = totalAmountMap.keySet().iterator();
        while(iterator.hasNext()){
            dateKey = iterator.next();
            amount = (double)totalAmountMap.get(dateKey);
            refundAmount = (double)refundAmountMap.get(dateKey);
            resultStatMap.put(dateKey, amount - refundAmount);
        }

        JSONObject resultJson = formLineEchartResultJson(resultStatMap);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getRepellentAmount")
    public ResultVo getRepellentAmount(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        String rtnMsg = initDashboardParam(paramVo);
        if(StringUtils.hasText(rtnMsg)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(rtnMsg);
            return resultVo;
        }

        String dateKey;

        // 退保保费汇总
        RepellentParamPagerVo repellentParamPagerVo = new RepellentParamPagerVo();
        repellentParamPagerVo.setChooseDate(paramVo.getChooseDate());
        repellentParamPagerVo.setMerchantId(paramVo.getMerchantId());
        repellentParamPagerVo.setColumnFieldIds("id,hesitateDate");
        repellentParamPagerVo.setDateField("hesitateDate");
        List<Repellent> repellentList = repellentService.findListByParam(repellentParamPagerVo);

        double refundTotalAmount;
        Map<String, Object> refundAmountMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "double");
        for (Repellent repellent : repellentList) {
            dateKey = getDateKeyByDateType(repellent.getHesitateDate(), paramVo.getDateType());
            refundTotalAmount = 0.00;
            if(refundAmountMap.get(dateKey) != null){
                refundTotalAmount = (double)refundAmountMap.get(dateKey);
            }
            refundTotalAmount += Double.valueOf(repellent.getInsuranceFee());
            refundAmountMap.put(dateKey, refundTotalAmount);
        }

        JSONObject resultJson = formLineEchartResultJson(refundAmountMap);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getTransformCost")
    public ResultVo getTransformCost(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        String rtnMsg = initDashboardParam(paramVo);
        if(StringUtils.hasText(rtnMsg)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(rtnMsg);
            return resultVo;
        }

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        String dateKey;
        int totalCount;
        Map<String, Object> statCountMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "int");
        for (Underwrite underwrite : underwriteList) {
            dateKey = getDateKeyByDateType(underwrite.getFinishDate(), paramVo.getDateType());
            totalCount = 0;
            if(statCountMap.get(dateKey) != null){
                totalCount = (int)statCountMap.get(dateKey);
            }
            totalCount ++;
            statCountMap.put(dateKey, totalCount);
        }

        double totalAmount;
        MediaItemParamPagerVo mediaItemParamPagerVo = new MediaItemParamPagerVo();
        mediaItemParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaItemParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaItemParamPagerVo.setColumnFieldIds("date,speedCost");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);
        Map<String, Object> statAmountMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "double");
        for (MediaItem mediaItem : mediaItemList) {
            dateKey = getDateKeyByDateType(mediaItem.getDate(), paramVo.getDateType());
            totalAmount = 0.00;
            if(statAmountMap.get(dateKey) != null){
                totalAmount = (double)statAmountMap.get(dateKey);
            }
            totalAmount += Double.valueOf(mediaItem.getSpeedCost());
            statAmountMap.put(dateKey, totalAmount);
        }

        int count = 0;
        double amount = 0.00;
        Map<String, Object> statResultMap = new HashMap<>();
        Iterator<String> iterator = statAmountMap.keySet().iterator();
        while(iterator.hasNext()){
            dateKey = iterator.next();
            if(StringUtils.isEmpty(statCountMap.get(dateKey))){
                count = 0;
            }else{
                count = (int)statCountMap.get(dateKey);
            }
            if(StringUtils.isEmpty(statAmountMap.get(dateKey))){
                amount = 0.00;
            }else{
                amount = (double)statAmountMap.get(dateKey);
            }
            if(count == 0){
                statResultMap.put(dateKey, 0);
            }else{
                statResultMap.put(dateKey, amount / count);
            }
        }

        JSONObject resultJson = formLineEchartResultJson(statResultMap);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getAbnormalCount")
    public ResultVo getAbnormalCount(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        String rtnMsg = initDashboardParam(paramVo);
        if(StringUtils.hasText(rtnMsg)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(rtnMsg);
            return resultVo;
        }

        AbnormalParamPagerVo paramPagerVo = new AbnormalParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setDateField("reserveDate");
        paramPagerVo.setProblemData("是");
        paramPagerVo.setColumnFieldIds("id,reserveDate");
        List<Abnormal> abnormalList = abnormalService.findListByParam(paramPagerVo);

        String dateKey;
        int reserveCount;
        Map<String, Object> statCountMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "int");
        for (Abnormal abnormal : abnormalList) {
            if("是".equals(abnormal.getProblemData())){
                dateKey = getDateKeyByDateType(abnormal.getDate(), paramVo.getDateType());
                reserveCount = 0;
                if(statCountMap.get(dateKey) != null){
                    reserveCount = (Integer)statCountMap.get(dateKey);
                }
                reserveCount ++;
                statCountMap.put(dateKey, reserveCount);
            }
        }

        JSONObject resultJson = formLineEchartResultJson(statCountMap);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getTransformCycle")
    public ResultVo getTransformCycle(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        String rtnMsg = initDashboardParam(paramVo);
        if(StringUtils.hasText(rtnMsg)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(rtnMsg);
            return resultVo;
        }

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("id,finishDate,reserveMobile");
        paramPagerVo.setDateField("finishDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        String dateKey;
        List<Underwrite> tempList;
        Map<String, List<Underwrite>> underwriteListMap = new HashMap<>();
        for(Underwrite underwrite : underwriteList) {
            dateKey = getDateKeyByDateType(underwrite.getFinishDate(), paramVo.getDateType());
            tempList = underwriteListMap.get(dateKey);
            if(tempList == null){
                tempList = new ArrayList<>();
            }
            tempList.add(underwrite);
            underwriteListMap.put(dateKey, tempList);
        }


        int difference;
        int totalDifference;
        int totalCount;
        String finishDate;
        String reserveDate;
        Set<String> mobileSet;
        Map<String, String> mobile2DateMap;
        Iterator<String> iterator = underwriteListMap.keySet().iterator();

        Map<String, Object> statResultMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "double");
        while (iterator.hasNext()) {
            dateKey = iterator.next();
            tempList = underwriteListMap.get(dateKey);
            mobileSet = new HashSet<>();
            for (Underwrite underwrite : tempList) {
                mobileSet.add(underwrite.getReserveMobile());
            }

            mobile2DateMap = new HashMap<>();
            List<FlowReserve> flowReserveList = flowReserveService.findListByMobiles(mobileSet, "date asc");
            if(flowReserveList.size() <= 0){
                continue;
            }
            for(FlowReserve flowReserve : flowReserveList) {
                mobile2DateMap.put(flowReserve.getReserveMobile(), flowReserve.getDate());
            }

            totalDifference = 0;
            totalCount = flowReserveList.size();
            for(Underwrite underwrite : tempList){
                finishDate = underwrite.getFinishDate();
                reserveDate = mobile2DateMap.get(underwrite.getReserveMobile());
                difference = DateUtils.getDateDifference(reserveDate, finishDate);
                if(difference == 0){
                    continue;
                }
                totalDifference += difference;
            }
            statResultMap.put(dateKey, totalDifference / totalCount);
        }


        JSONObject resultJson = formLineEchartResultJson(statResultMap);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getTransformRate")
    public ResultVo getTransformRate(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        String rtnMsg = initDashboardParam(paramVo);
        if(StringUtils.hasText(rtnMsg)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(rtnMsg);
            return resultVo;
        }

        String dateKey;

        // 计算统计周期内保单号总量
        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("finishDate,insuranceNo");
        paramPagerVo.setDateField("finishDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);
        int totalCount;
        Map<String, Object> totalCountMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "int");
        for (Underwrite underwrite : underwriteList) {
            dateKey = getDateKeyByDateType(underwrite.getFinishDate(), paramVo.getDateType());
            totalCount = (int)totalCountMap.get(dateKey);
            totalCount ++;
            totalCountMap.put(dateKey, totalCount);
        }

        // 计算统计周期内点击量总数
        MediaItemParamPagerVo mediaItemParamPagerVo = new MediaItemParamPagerVo();
        mediaItemParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaItemParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaItemParamPagerVo.setColumnFieldIds("id,date,clickCount");
        mediaItemParamPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);

        int clickTotalCount;
        Map<String, Object> clickTotalMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "int");
        for (MediaItem mediaItem : mediaItemList) {
            dateKey = getDateKeyByDateType(mediaItem.getDate(), paramVo.getDateType());
            clickTotalCount = (int)clickTotalMap.get(dateKey);
            clickTotalCount += mediaItem.getClickCount();
            clickTotalMap.put(dateKey, clickTotalCount);
        }

        // 结算最终结果
        int count, clickCount;
        Map<String, Object> resultStatMap = new HashMap<>();
        Iterator<String> iterator = totalCountMap.keySet().iterator();
        while(iterator.hasNext()){
            dateKey = iterator.next();
            count = (int)totalCountMap.get(dateKey);
            clickCount = (int)clickTotalMap.get(dateKey);
            if(clickCount == 0) {
                resultStatMap.put(dateKey, clickCount);
            }else{
                resultStatMap.put(dateKey, (double)(count * 100) / clickCount);
            }
        }

        JSONObject resultJson = formLineEchartResultJson(resultStatMap);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getReserveClick")
    public ResultVo getReserveClick(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        String rtnMsg = initDashboardParam(paramVo);
        if(StringUtils.hasText(rtnMsg)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(rtnMsg);
            return resultVo;
        }

        String dateKey;

        // 计算统计周期内预约总量
        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("date,reserveMobile");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        int reserveCount;
        Map<String, Object> statCountMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "int");
        for (FlowReserve flowReserve : flowReserveList) {
            dateKey = getDateKeyByDateType(flowReserve.getDate(), paramVo.getDateType());
            reserveCount = 0;
            if(statCountMap.get(dateKey) != null){
                reserveCount = (Integer)statCountMap.get(dateKey);
            }
            reserveCount ++;
            statCountMap.put(dateKey, reserveCount);
        }

        // 计算统计周期内点击量总数
        MediaItemParamPagerVo mediaItemParamPagerVo = new MediaItemParamPagerVo();
        mediaItemParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaItemParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaItemParamPagerVo.setColumnFieldIds("id,date,clickCount");
        mediaItemParamPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);

        int clickTotalCount;
        Map<String, Object> clickTotalMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "int");
        for (MediaItem mediaItem : mediaItemList) {
            dateKey = getDateKeyByDateType(mediaItem.getDate(), paramVo.getDateType());
            clickTotalCount = (int)clickTotalMap.get(dateKey);
            clickTotalCount += mediaItem.getClickCount();
            clickTotalMap.put(dateKey, clickTotalCount);
        }

        // 结算最终结果
        int count, clickCount;
        Map<String, Object> resultStatMap = new HashMap<>();
        Iterator<String> iterator = statCountMap.keySet().iterator();
        while(iterator.hasNext()){
            dateKey = iterator.next();
            count = (int)statCountMap.get(dateKey);
            clickCount = (int)clickTotalMap.get(dateKey);
            if(clickCount == 0) {
                resultStatMap.put(dateKey, clickCount);
            }else{
                resultStatMap.put(dateKey, (double)(count * 100) / clickCount);
            }
        }

        JSONObject resultJson = formLineEchartResultJson(resultStatMap);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getGenderAge")
    public ResultVo getGenderAge(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        String rtnMsg = initDashboardParam(paramVo);
        if(StringUtils.hasText(rtnMsg)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(rtnMsg);
            return resultVo;
        }

        // 保费汇总
        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("finishDate,gender,age,insuranceFee");
        paramPagerVo.setDateField("finishDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        Map<Integer, Double> manMap = initAgeMap();
        Map<Integer, Double> womenMap = initAgeMap();
        int age, mapKey;
        String gender;
        double amount, totalAmount;
        for (Underwrite underwrite : underwriteList) {
            age = underwrite.getAge();
            gender = underwrite.getGender();
            amount = Double.parseDouble(underwrite.getInsuranceFee());
            if(0 < age && age <= 5) {
                mapKey = AgePartEnum.from_0_5.getValue();
                if("男".equals(gender)) {
                    totalAmount = manMap.get(mapKey);
                    totalAmount += amount;
                    manMap.put(mapKey, totalAmount);
                }else if("女".equals(gender)){
                    totalAmount = womenMap.get(mapKey);
                    totalAmount += amount;
                    womenMap.put(mapKey, totalAmount);
                }
            }else if(5 < age && age <= 18){
                mapKey = AgePartEnum.from_5_18.getValue();
                if("男".equals(gender)) {
                    totalAmount = manMap.get(mapKey);
                    totalAmount += amount;
                    manMap.put(mapKey, totalAmount);
                }else if("女".equals(gender)){
                    totalAmount = womenMap.get(mapKey);
                    totalAmount += amount;
                    womenMap.put(mapKey, totalAmount);
                }
            }else if(18 < age && age <= 25){
                mapKey = AgePartEnum.from_18_25.getValue();
                if("男".equals(gender)) {
                    totalAmount = manMap.get(mapKey);
                    totalAmount += amount;
                    manMap.put(mapKey, totalAmount);
                }else if("女".equals(gender)){
                    totalAmount = womenMap.get(mapKey);
                    totalAmount += amount;
                    womenMap.put(mapKey, totalAmount);
                }
            }else if(25 < age && age <= 30){
                mapKey = AgePartEnum.from_25_30.getValue();
                if("男".equals(gender)) {
                    totalAmount = manMap.get(mapKey);
                    totalAmount += amount;
                    manMap.put(mapKey, totalAmount);
                }else if("女".equals(gender)){
                    totalAmount = womenMap.get(mapKey);
                    totalAmount += amount;
                    womenMap.put(mapKey, totalAmount);
                }
            }else if(30 < age && age <= 35){
                mapKey = AgePartEnum.from_30_35.getValue();
                if("男".equals(gender)) {
                    totalAmount = manMap.get(mapKey);
                    totalAmount += amount;
                    manMap.put(mapKey, totalAmount);
                }else if("女".equals(gender)){
                    totalAmount = womenMap.get(mapKey);
                    totalAmount += amount;
                    womenMap.put(mapKey, totalAmount);
                }
            }else if(35 < age && age <= 40){
                mapKey = AgePartEnum.from_35_40.getValue();
                if("男".equals(gender)) {
                    totalAmount = manMap.get(mapKey);
                    totalAmount += amount;
                    manMap.put(mapKey, totalAmount);
                }else if("女".equals(gender)){
                    totalAmount = womenMap.get(mapKey);
                    totalAmount += amount;
                    womenMap.put(mapKey, totalAmount);
                }
            }else if(40 < age && age <= 50){
                mapKey = AgePartEnum.from_40_50.getValue();
                if("男".equals(gender)) {
                    totalAmount = manMap.get(mapKey);
                    totalAmount += amount;
                    manMap.put(mapKey, totalAmount);
                }else if("女".equals(gender)){
                    totalAmount = womenMap.get(mapKey);
                    totalAmount += amount;
                    womenMap.put(mapKey, totalAmount);
                }
            }else if(50 < age && age <= 55){
                mapKey = AgePartEnum.from_50_55.getValue();
                if("男".equals(gender)) {
                    totalAmount = manMap.get(mapKey);
                    totalAmount += amount;
                    manMap.put(mapKey, totalAmount);
                }else if("女".equals(gender)){
                    totalAmount = womenMap.get(mapKey);
                    totalAmount += amount;
                    womenMap.put(mapKey, totalAmount);
                }
            }else if(age > 55){
                mapKey = AgePartEnum.from_55_100.getValue();
                if("男".equals(gender)) {
                    totalAmount = manMap.get(mapKey);
                    totalAmount += amount;
                    manMap.put(mapKey, totalAmount);
                }else if("女".equals(gender)){
                    totalAmount = womenMap.get(mapKey);
                    totalAmount += amount;
                    womenMap.put(mapKey, totalAmount);
                }
            }
        }
        StringBuilder labelValue = new StringBuilder("");
        JSONObject manJson = formAgeBarEchartResultJson(manMap);
        JSONObject womanJson = formAgeBarEchartResultJson(womenMap);
        if(manJson.getDouble("maxAmount") > womanJson.getDouble("maxAmount")){
            labelValue.append(AgePartEnum.getTextByValue(manJson.getInteger("agePart")));
            labelValue.append("  ");
            labelValue.append(manJson.getDouble("maxAmount"));
        }else{
            labelValue.append(AgePartEnum.getTextByValue(womanJson.getInteger("agePart")));
            labelValue.append("  ");
            labelValue.append(womanJson.getDouble("maxAmount"));
        }

        // 结算最终结果
        JSONArray manJsonArray = new JSONArray();
        for(int index = 0; index < manJson.getJSONArray("contentList").size(); index ++){
            manJsonArray.add(0 - manJson.getJSONArray("contentList").getDouble(index));
        }
        JSONObject resultJson = new JSONObject();
        resultJson.put("manData", manJsonArray);
        resultJson.put("womenData", womanJson.getJSONArray("contentList"));
        resultJson.put("labelValue", labelValue.toString());

        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getInsuranceMap")
    public ResultVo getInsuranceMap(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        String rtnMsg = initDashboardParam(paramVo);
        if(StringUtils.hasText(rtnMsg)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(rtnMsg);
            return resultVo;
        }

        String dateKey;

        // 保费汇总
        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("finishDate,provinceId,insuranceFee");
        paramPagerVo.setDateField("finishDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);
        Integer provinceId;
        String insuranceFee;
        List<Integer> provinceIdList = new ArrayList<>();
        double insuranceFeeAmount, insuranceTotalAmount;
        Map<Integer, Double> mapAmountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            provinceId = underwrite.getProvinceId();
            if(provinceId == null || provinceId == 0){
                continue;
            }
            provinceIdList.add(provinceId);

            insuranceFeeAmount = 0.0d;
            insuranceFee = underwrite.getInsuranceFee();
            if(StringUtils.hasText(insuranceFee)){
                insuranceFeeAmount = Double.parseDouble(insuranceFee);
            }
            if(mapAmountMap.get(provinceId) == null){
                insuranceTotalAmount = insuranceFeeAmount;
            }else{
                insuranceTotalAmount = mapAmountMap.get(provinceId);
                insuranceTotalAmount += insuranceFeeAmount;
            }
            mapAmountMap.put(provinceId, insuranceTotalAmount);
        }

        // 获取省份的名称
        Map<Integer, String> areaNameMap = new HashMap<>();
        List<Area> areaList = areaService.findByIdList(provinceIdList);
        for(Area area : areaList) {
            areaNameMap.put(area.getAreaId(), area.getAreaName());
        }

        JSONObject provinceJson;

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        JSONArray provinceArray = new JSONArray();
        Iterator<Integer> iterator = mapAmountMap.keySet().iterator();
        while(iterator.hasNext()){
            provinceId = iterator.next();
            provinceJson = new JSONObject();
            provinceJson.put("name", areaNameMap.get(provinceId));
            provinceJson.put("value", decimalFormat.format(mapAmountMap.get(provinceId)));
            provinceArray.add(provinceJson);
        }

        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(provinceArray);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getWordClouds")
    public ResultVo getWordClouds(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        String rtnMsg = initDashboardParam(paramVo);
        if(StringUtils.hasText(rtnMsg)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(rtnMsg);
            return resultVo;
        }

        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("date,searchWord");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        int searchCount;
        Map<String, Integer> searchWordMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            if ("-".equals(flowReserve.getSearchWord()) || "".equals(flowReserve.getSearchWord())) {
                continue;
            }
            if (searchWordMap.get(flowReserve.getSearchWord()) == null) {
                searchCount = 0;
            } else {
                searchCount = searchWordMap.get(flowReserve.getSearchWord());
                searchCount++;
            }
            searchWordMap.put(flowReserve.getSearchWord(), searchCount);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(searchWordMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }

        });

        int count = 0;
        JSONObject wordCloud;
        JSONArray wordCloudArray = new JSONArray();
        for (Map.Entry<String, Integer> mapping : list) {
            if(count > 30){
                break;
            }
            wordCloud = new JSONObject();
            wordCloud.put("name", mapping.getKey());
            wordCloud.put("value", mapping.getValue());
            wordCloudArray.add(wordCloud);
            count ++;
        }

        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(wordCloudArray);
        return resultVo;
    }









    private JSONObject formAgeBarEchartResultJson(Map<Integer, Double> paramMap) {
        JSONObject resultJson = new JSONObject();
        JSONArray contentArray = new JSONArray();
        Set<Integer> keySet = paramMap.keySet();
        Integer[] keyArray = keySet.toArray(new Integer[keySet.size()]);
        Arrays.sort(keyArray);

        Integer maxKey = 0;
        double currentAmount, maxAmount = 0.00;
        for(Integer key : keyArray){
            currentAmount = paramMap.get(key);
            contentArray.add(currentAmount);
            if(currentAmount > maxAmount){
                maxAmount = currentAmount;
                maxKey = key;
            }
        }
        resultJson.put("contentList", contentArray);
        resultJson.put("maxAmount", maxAmount);
        resultJson.put("agePart", maxKey);
        return resultJson;
    }

    /**
     * 构建折线图的返回数据
     * @param statCountMap
     * @return
     */
    private JSONObject formLineEchartResultJson(Map<String, Object> statCountMap){
        JSONObject resultJson = new JSONObject();
        JSONArray titleArray = new JSONArray();
        JSONArray contentArray = new JSONArray();
        Set<String> keySet = statCountMap.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        for(String key : keyArray){
            titleArray.add(key);
            contentArray.add(statCountMap.get(key));
        }
        resultJson.put("titleList", titleArray);
        resultJson.put("contentList", contentArray);

        int size = contentArray.size();
        int lastValue = 0;
        int penultValue = 0;
        int trendRate = 0;
        if(size > 0){
            lastValue = contentArray.getIntValue(size - 1);
        }
        resultJson.put("lastValue", lastValue);
        if(size > 1){
            penultValue = contentArray.getIntValue(size - 2);
        }
        resultJson.put("penultValue", penultValue);
        if(penultValue != 0 && penultValue != 0){
            trendRate = (lastValue - penultValue) * 100 / penultValue;
        }
        resultJson.put("trendRate", trendRate);
        return resultJson;
    }

    /**
     * 初始化传入参数
     * @param paramVo
     * @return
     */
    private String initDashboardParam(DashboardParamVo paramVo){
        if(paramVo == null){
            paramVo = new DashboardParamVo();
        }
        if(StringUtils.isEmpty(paramVo.getChooseDate())){
            String endDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
            String startDate = DateUtils.calculateDate(new Date(), Calendar.DAY_OF_YEAR, -5, "yyyy-MM-dd");
            paramVo.setChooseDate(startDate + " - " + endDate);
        }
        if(StringUtils.isEmpty(paramVo.getDateType())){
            paramVo.setDateType(DateTypeEnum.日.getValue());
        }

        return null;
    }

    private Map<Integer, Double> initAgeMap(){
        Map<Integer, Double> ageMap = new HashMap<>(9);
        ageMap.put(AgePartEnum.from_0_5.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_5_18.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_18_25.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_25_30.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_30_35.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_35_40.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_40_50.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_50_55.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_55_100.getValue(), 0.00);
        return ageMap;
    }

    /**
     * 根据选择的日期初始化Map集合
     * @param chooseDate
     * @param dateType
     * @return
     */
    private Map<String, Object> initResultMap(String chooseDate, int dateType, String dataType){
        Map<String, Object> resultMap = new HashMap<>();
        String[] dateArray = chooseDate.split(" - ");

        Object initValue = "";
        if("int".equals(dataType)){
            initValue = 0;
        }else if("double".equals(dataType)){
            initValue = 0.00;
        }

        String dateKey = getDateKeyByDateType(dateArray[0], dateType);
        resultMap.put(dateKey, initValue);

        if(dateArray[0].equals(dateArray[1])){
            return resultMap;
        }
        Date startDate = DateUtils.formatDate(dateArray[0], "yyyy-MM-dd");
        String nextDate = DateUtils.calculateDate(startDate, Calendar.DAY_OF_YEAR, 1, "yyyy-MM-dd");
        while(!dateArray[1].equals(nextDate)){
            dateKey = getDateKeyByDateType(nextDate, dateType);
            resultMap.put(dateKey, initValue);
            startDate = DateUtils.formatDate(nextDate, "yyyy-MM-dd");
            nextDate = DateUtils.calculateDate(startDate, Calendar.DAY_OF_YEAR, 1, "yyyy-MM-dd");
        }
        dateKey = getDateKeyByDateType(nextDate, dateType);
        resultMap.put(dateKey, initValue);
        return resultMap;
    }

    private String getDateKeyByDateType(String dateString, int dateType){
        String format = "yyyyMMdd";
        Date date = DateUtils.formatDate(dateString, "yyyy-MM-dd");
        if(dateType == DateTypeEnum.年.getValue()){
            format = "yyyy";
        }else if(dateType == DateTypeEnum.季度.getValue()){
            format = "yyyy";
            return DateUtils.formatDate(date, format) + "0" + DateUtils.getSeasonByDate(date);
        }else if(dateType == DateTypeEnum.月.getValue()){
            format = "yyyyMM";
        }else if(dateType == DateTypeEnum.周.getValue()){
            format = "yyyyww";
        }else if(dateType == DateTypeEnum.日.getValue()){
            format = "yyyyMMdd";
        }
        return DateUtils.formatDate(date, format);
    }
}
