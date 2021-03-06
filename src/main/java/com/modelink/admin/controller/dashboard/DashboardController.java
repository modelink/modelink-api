package com.modelink.admin.controller.dashboard;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.vo.dashboard.DashboardAreaParamVo;
import com.modelink.admin.vo.dashboard.DashboardParamVo;
import com.modelink.common.enums.*;
import com.modelink.common.utils.DataUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.*;
import com.modelink.reservation.enums.FeeTypeEnum;
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

/**
 * 该页面不区分预约和测保
 */
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
    @Resource
    private PermiumsService permiumsService;

    @RequestMapping("/home")
    public String home(){
        return "/admin/dashboard/home";
    }

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
        Map<String, Object> statCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "int");
        for (FlowReserve flowReserve : flowReserveList) {
            dateKey = DataUtils.getDateKeyByDateType(flowReserve.getDate(), paramVo.getDateType());
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
        paramPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        String dateKey;
        int reserveCount;
        Map<String, Object> statCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "int");
        for (Underwrite underwrite : underwriteList) {
            dateKey = DataUtils.getDateKeyByDateType(underwrite.getFinishDate(), paramVo.getDateType());
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
        PermiumsParamPagerVo paramPagerVo = new PermiumsParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("date,insuranceFee");
        paramPagerVo.setDateField("date");
        List<Permiums> permiumsList = permiumsService.findListByParam(paramPagerVo);

        double underwriteAmount;
        Map<String, Object> underwriteAmountMap = DataUtils.initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "double");
        for (Permiums permiums : permiumsList) {
            dateKey = DataUtils.getDateKeyByDateType(permiums.getDate(), paramVo.getDateType());
            underwriteAmount = 0.00;
            if(underwriteAmountMap.get(dateKey) != null){
                underwriteAmount = (double)underwriteAmountMap.get(dateKey);
            }
            if(StringUtils.hasText(permiums.getInsuranceFee()) && !"-".equals(permiums.getInsuranceFee())) {
                underwriteAmount += Double.valueOf(permiums.getInsuranceFee());
            }
            underwriteAmountMap.put(dateKey, underwriteAmount);
        }

        JSONObject resultJson = formLineEchartResultJson(underwriteAmountMap);
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
        repellentParamPagerVo.setColumnFieldIds("id,hesitateDate,insuranceFee");
        repellentParamPagerVo.setDateField("hesitateDate");
        List<Repellent> repellentList = repellentService.findListByParam(repellentParamPagerVo);

        double refundTotalAmount;
        Map<String, Object> refundAmountMap = DataUtils.initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "double");
        for (Repellent repellent : repellentList) {
            dateKey = DataUtils.getDateKeyByDateType(repellent.getHesitateDate(), paramVo.getDateType());
            refundTotalAmount = 0.00;
            if(refundAmountMap.get(dateKey) != null){
                refundTotalAmount = (double)refundAmountMap.get(dateKey);
            }
            if(StringUtils.hasText(repellent.getInsuranceFee()) && !"-".equals(repellent.getInsuranceFee())) {
                refundTotalAmount += Double.valueOf(repellent.getInsuranceFee());
            }
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
        paramPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        String dateKey;
        int totalCount;
        Map<String, Object> statCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "int");
        for (Underwrite underwrite : underwriteList) {
            dateKey = DataUtils.getDateKeyByDateType(underwrite.getFinishDate(), paramVo.getDateType());
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
        Map<String, Object> statAmountMap = DataUtils.initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "double");
        for (MediaItem mediaItem : mediaItemList) {
            dateKey = DataUtils.getDateKeyByDateType(mediaItem.getDate(), paramVo.getDateType());
            totalAmount = 0.00;
            if(statAmountMap.get(dateKey) != null){
                totalAmount = (double)statAmountMap.get(dateKey);
            }
            totalAmount += Double.valueOf(mediaItem.getSpeedCost());
            statAmountMap.put(dateKey, totalAmount);
        }

        int count = 0;
        double amount = 0.00;
        totalCount = 0;
        totalAmount = 0.00d;
        Map<String, Object> statResultMap = new HashMap<>();
        Iterator<String> iterator = statAmountMap.keySet().iterator();
        while(iterator.hasNext()){
            dateKey = iterator.next();
            if(StringUtils.isEmpty(statCountMap.get(dateKey))){
                count = 0;
            }else{
                count = (int)statCountMap.get(dateKey);
            }
            totalCount += count;
            if(StringUtils.isEmpty(statAmountMap.get(dateKey))){
                amount = 0.00;
            }else{
                amount = (double)statAmountMap.get(dateKey);
            }
            totalAmount += amount;
            if(count == 0){
                statResultMap.put(dateKey, 0.00d);
            }else{
                statResultMap.put(dateKey, amount / count);
            }
        }

        JSONObject resultJson = new JSONObject();
        JSONArray titleArray = new JSONArray();
        JSONArray contentArray = new JSONArray();
        Set<String> keySet = statCountMap.keySet();
        DecimalFormat decimalFormat = new DecimalFormat("#0");
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);

        for(String key : keyArray){
            titleArray.add(key);
            if (statCountMap.get(key) instanceof Double) {
                contentArray.add(decimalFormat.format((Double) statCountMap.get(key)));
            } else {
                contentArray.add(statCountMap.get(key));
            }
        }
        resultJson.put("titleList", titleArray);
        resultJson.put("contentList", contentArray);
        resultJson.put("totalTransformCost", totalCount == 0 ? "0" : decimalFormat.format(totalAmount / totalCount));

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
        Map<String, Object> statCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "int");
        for (Abnormal abnormal : abnormalList) {
            dateKey = DataUtils.getDateKeyByDateType(abnormal.getReserveDate(), paramVo.getDateType());
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

//    @ResponseBody
//    @RequestMapping("/getTransformCycle")
//    public ResultVo getTransformCycle(DashboardParamVo paramVo){
//        ResultVo resultVo = new ResultVo();
//
//        String rtnMsg = initDashboardParam(paramVo);
//        if(StringUtils.hasText(rtnMsg)){
//            resultVo.setRtnCode(RetStatus.Fail.getValue());
//            resultVo.setRtnMsg(rtnMsg);
//            return resultVo;
//        }
//
//        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
//        paramPagerVo.setChooseDate(paramVo.getChooseDate());
//        paramPagerVo.setMerchantId(paramVo.getMerchantId());
//        paramPagerVo.setColumnFieldIds("id,reserveDate,finishDate,reserveMobile");
//        paramPagerVo.setDateField("reserveDate");
//        paramPagerVo.setSource("!产品测保");
//        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);
//
//        String dateKey;
//        int difference, underwriteCount;
//        String finishDate, reserveDate;
//        Map<String, Integer> differenceMap = new HashMap<>();
//        Map<String, Integer> underwriteCountMap = new HashMap<>();
//        for(Underwrite underwrite : underwriteList) {
//            dateKey = DataUtils.getDateKeyByDateType(underwrite.getReserveDate(), paramVo.getDateType());
//
//            difference = 0;
//            if (differenceMap.get(dateKey) != null) {
//                difference = differenceMap.get(dateKey);
//            }
//            finishDate = underwrite.getFinishDate();
//            reserveDate = underwrite.getReserveDate();
//            difference += DateUtils.getDateDifference(reserveDate, finishDate);
//            differenceMap.put(dateKey, difference);
//
//            underwriteCount = 0;
//            if (underwriteCountMap.get(dateKey) != null) {
//                underwriteCount = underwriteCountMap.get(dateKey);
//            }
//            underwriteCount ++;
//            underwriteCountMap.put(dateKey, underwriteCount);
//        }
//
//
//        Iterator<String> iterator = underwriteCountMap.keySet().iterator();
//        Map<String, Object> statResultMap = DataUtils.initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "double");
//        while (iterator.hasNext()) {
//            dateKey = iterator.next();
//            difference = differenceMap.get(dateKey);
//            underwriteCount = underwriteCountMap.get(dateKey);
//            if (underwriteCount == 0) {
//                statResultMap.put(dateKey, 0.00d);
//            } else {
//                statResultMap.put(dateKey, difference / underwriteCount);
//            }
//        }
//
//        JSONObject resultJson = formLineEchartResultJson(statResultMap);
//        resultVo.setRtnCode(RetStatus.Ok.getValue());
//        resultVo.setRtnData(resultJson);
//        return resultVo;
//    }

    @ResponseBody
    @RequestMapping("/getTransformCycle")
    public ResultVo getTransformCycle(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        // 保费汇总
        UnderwriteParamPagerVo underwriteParamPagerVo = new UnderwriteParamPagerVo();
        underwriteParamPagerVo.setChooseDate(paramVo.getChooseDate());
        underwriteParamPagerVo.setMerchantId(paramVo.getMerchantId());
        underwriteParamPagerVo.setColumnFieldIds("id,reserveDate,finishDate,reserveMobile");
        underwriteParamPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);

        int totalCount, difference;
        String finishDate, reserveDate;
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
        for(Underwrite underwrite : underwriteList){
            finishDate = underwrite.getFinishDate();
            reserveDate = underwrite.getReserveDate();
            difference = DateUtils.getDateDifference(reserveDate, finishDate);
            if(difference <= 0){
                continue;
            }

            if(difference > 90){
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
            }else if(difference > 6){
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
        }

        Set<Integer> keySet;
        Integer[] keyArray;
        List<Integer> contentList;
        keySet = transformCycleMap.keySet();
        keyArray = keySet.toArray(new Integer[keySet.size()]);
        Arrays.sort(keyArray, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        contentList = new ArrayList<>();
        for (Integer key : keyArray) {
            contentList.add(transformCycleMap.get(key));
        }
        List<String> titleList = new ArrayList<>();
        for (TransformCycleEnum transformCycleEnum : TransformCycleEnum.values()) {
            titleList.add(transformCycleEnum.getText());
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("contentList", contentList);
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
        paramPagerVo.setColumnFieldIds("reserveDate,insuranceNo");
        paramPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);
        int totalCount;
        Map<String, Object> totalCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "int");
        for (Underwrite underwrite : underwriteList) {
            dateKey = DataUtils.getDateKeyByDateType(underwrite.getReserveDate(), paramVo.getDateType());
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
        Map<String, Object> clickTotalMap = DataUtils.initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "int");
        for (MediaItem mediaItem : mediaItemList) {
            dateKey = DataUtils.getDateKeyByDateType(mediaItem.getDate(), paramVo.getDateType());
            clickTotalCount = (int)clickTotalMap.get(dateKey);
            clickTotalCount += mediaItem.getClickCount();
            clickTotalMap.put(dateKey, clickTotalCount);
        }

        // 结算最终结果
        int count, clickCount;
        Map<String, Object> resultStatMap = new HashMap<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0");
        Iterator<String> iterator = totalCountMap.keySet().iterator();
        while(iterator.hasNext()){
            dateKey = iterator.next();
            count = (int)totalCountMap.get(dateKey);
            clickCount = (int)clickTotalMap.get(dateKey);
            if(clickCount == 0) {
                resultStatMap.put(dateKey, "0.00");
            }else{
                resultStatMap.put(dateKey, (count * 1000.0d) / clickCount);
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

        // 计算统计周期内预约总量
        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("date,reserveMobile");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);
        int reserveCount = flowReserveList.size();

        // 计算统计周期内保单号总量
        UnderwriteParamPagerVo underwriteParamPagerVo = new UnderwriteParamPagerVo();
        underwriteParamPagerVo.setChooseDate(paramVo.getChooseDate());
        underwriteParamPagerVo.setMerchantId(paramVo.getMerchantId());
        underwriteParamPagerVo.setColumnFieldIds("reserveDate,insuranceNo");
        underwriteParamPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);
        int underwriteCount = underwriteList.size();

        double underwriteAmount = 0.00d;
        // 保费汇总
        PermiumsParamPagerVo permiumsParamPagerVo = new PermiumsParamPagerVo();
        permiumsParamPagerVo.setChooseDate(paramVo.getChooseDate());
        permiumsParamPagerVo.setMerchantId(paramVo.getMerchantId());
        permiumsParamPagerVo.setColumnFieldIds("date,insuranceFee");
        permiumsParamPagerVo.setDateField("date");
        List<Permiums> permiumsList = permiumsService.findListByParam(permiumsParamPagerVo);
        for (Permiums permiums : permiumsList) {
            if (StringUtils.hasText(permiums.getInsuranceFee()) && !"-".equals(permiums.getInsuranceFee())) {
                underwriteAmount += Double.parseDouble(permiums.getInsuranceFee());
            }
        }

        // 计算统计周期内的点击量
        int clickCount = 0;
        MediaItemParamPagerVo mediaItemParamPagerVo = new MediaItemParamPagerVo();
        mediaItemParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaItemParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaItemParamPagerVo.setColumnFieldIds("date,clickCount,speedCost");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);
        for (MediaItem mediaItem : mediaItemList) {
            if(mediaItem.getClickCount() != null){
                clickCount += mediaItem.getClickCount();
            }
        }

        JSONObject resultJson = new JSONObject();
        DecimalFormat decimalFormat = new DecimalFormat("#0");
        DecimalFormat decimalFormatPercent = new DecimalFormat("#0.00");
        resultJson.put("capacity", decimalFormat.format(reserveCount == 0 ? 0 : underwriteAmount / reserveCount));
        resultJson.put("reserveRate", decimalFormatPercent.format(clickCount == 0 ? 0 : reserveCount * 100d / clickCount) + "%");
        resultJson.put("underwriteRate", decimalFormatPercent.format(reserveCount == 0 ? 0 : underwriteCount * 100d / reserveCount) + "%");

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
        paramPagerVo.setColumnFieldIds("reserveDate,gender,age,insuranceFee");
        paramPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        int age, mapKey;
        String gender;
        double amount, totalAmount, manAmount = 0.00d, womanAmount = 0.00d, unknowAmount = 0.00d;
        Map<Integer, Double> age2InsuranceFeeMap = new HashMap<>();
        for (AgePartEnum agePartEnum : AgePartEnum.values()) {
            age2InsuranceFeeMap.put(agePartEnum.getValue(), 0.00d);
        }
        for (Underwrite underwrite : underwriteList) {
            age = underwrite.getAge();
            gender = underwrite.getGender();
            amount = 0.0d;
            if(StringUtils.hasText(underwrite.getInsuranceFee()) && !"-".equals(underwrite.getInsuranceFee())) {
                amount = Double.parseDouble(underwrite.getInsuranceFee());
            }
            if("男".equals(gender)) {
                manAmount += amount;
            }else if("女".equals(gender)){
                womanAmount += amount;
            }else{
                unknowAmount += amount;
            }
            if(0 < age && age <= 5) {
                mapKey = AgePartEnum.from_0_5.getValue();
                totalAmount = age2InsuranceFeeMap.get(mapKey);
                totalAmount += amount;
                age2InsuranceFeeMap.put(mapKey, totalAmount);
            }else if(5 < age && age <= 18){
                mapKey = AgePartEnum.from_5_18.getValue();
                totalAmount = age2InsuranceFeeMap.get(mapKey);
                totalAmount += amount;
                age2InsuranceFeeMap.put(mapKey, totalAmount);
            }else if(18 < age && age <= 25){
                mapKey = AgePartEnum.from_18_25.getValue();
                totalAmount = age2InsuranceFeeMap.get(mapKey);
                totalAmount += amount;
                age2InsuranceFeeMap.put(mapKey, totalAmount);
            }else if(25 < age && age <= 30){
                mapKey = AgePartEnum.from_25_30.getValue();
                totalAmount = age2InsuranceFeeMap.get(mapKey);
                totalAmount += amount;
                age2InsuranceFeeMap.put(mapKey, totalAmount);
            }else if(30 < age && age <= 35){
                mapKey = AgePartEnum.from_30_35.getValue();
                totalAmount = age2InsuranceFeeMap.get(mapKey);
                totalAmount += amount;
                age2InsuranceFeeMap.put(mapKey, totalAmount);
            }else if(35 < age && age <= 40){
                mapKey = AgePartEnum.from_35_40.getValue();
                totalAmount = age2InsuranceFeeMap.get(mapKey);
                totalAmount += amount;
                age2InsuranceFeeMap.put(mapKey, totalAmount);
            }else if(40 < age && age <= 50){
                mapKey = AgePartEnum.from_40_50.getValue();
                totalAmount = age2InsuranceFeeMap.get(mapKey);
                totalAmount += amount;
                age2InsuranceFeeMap.put(mapKey, totalAmount);
            }else if(50 < age && age <= 55){
                mapKey = AgePartEnum.from_50_55.getValue();
                totalAmount = age2InsuranceFeeMap.get(mapKey);
                totalAmount += amount;
                age2InsuranceFeeMap.put(mapKey, totalAmount);
            }else if(age > 55){
                mapKey = AgePartEnum.from_55_100.getValue();
                totalAmount = age2InsuranceFeeMap.get(mapKey);
                totalAmount += amount;
                age2InsuranceFeeMap.put(mapKey, totalAmount);
            }
        }

        String contentValue;
        JSONObject contentItem;
        JSONObject labelJson = new JSONObject();
        labelJson.put("show", true);
        labelJson.put("position", "top");

        List<String> titleList = new ArrayList<>();
        List<JSONObject> contentList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        DecimalFormat decimalFormatPercent = new DecimalFormat("#0.0000");
        for (AgePartEnum agePartEnum : AgePartEnum.values()) {
            titleList.add(agePartEnum.getText());
            if (manAmount + womanAmount + unknowAmount <= 0) {
                contentValue = "0";
            } else {
                contentValue = decimalFormatPercent.format(age2InsuranceFeeMap.get(agePartEnum.getValue()) / (manAmount + womanAmount + unknowAmount));
            }
            contentItem = new JSONObject();
            contentItem.put("label", labelJson);
            contentItem.put("value", contentValue);
            contentList.add(contentItem);
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("contentList", contentList);
        if (manAmount + womanAmount + unknowAmount <= 0) {
            resultJson.put("manValue", "0.00%");
            resultJson.put("womanValue", "0.00%");
        } else {
            resultJson.put("manValue", decimalFormat.format(manAmount * 100 / (manAmount + womanAmount + unknowAmount)) + "%");
            resultJson.put("womanValue", decimalFormat.format(womanAmount * 100 / (manAmount + womanAmount + unknowAmount)) + "%");
        }

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
        paramPagerVo.setColumnFieldIds("reserveDate,provinceId,insuranceFee");
        paramPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);
        Integer provinceId;
        double underwriteAmount, insuranceTotalAmount = 0.00d;
        Map<Integer, Double> mapAmountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            provinceId = underwrite.getProvinceId();
            if(provinceId == null || provinceId == 0){
                continue;
            }
            underwriteAmount = 0.0d;
            if(mapAmountMap.get(provinceId) != null){
                underwriteAmount = mapAmountMap.get(provinceId);
            }
            if(StringUtils.hasText(underwrite.getInsuranceFee()) && !"".equals(underwrite.getInsuranceFee())){
                underwriteAmount += Double.parseDouble(underwrite.getInsuranceFee());
                insuranceTotalAmount += Double.parseDouble(underwrite.getInsuranceFee());
            }

            mapAmountMap.put(provinceId, underwriteAmount);
        }

        // 获取省份的名称
        JSONObject provinceJson;
        Area areaParam = new Area();
        areaParam.setAreaType(AreaTypeEnum.省.getValue());
        List<Area> areaList = areaService.findListByParam(areaParam);
        DecimalFormat decimalFormat = new DecimalFormat("#0");
        List<JSONObject> provinceList = new ArrayList<>();
        for (Area area : areaList) {
            provinceJson = new JSONObject();
            provinceJson.put("name", area.getAreaName());
            if (mapAmountMap.get(area.getAreaId()) == null) {
                provinceJson.put("value", "0.00");
            } else {
                provinceJson.put("value", decimalFormat.format(mapAmountMap.get(area.getAreaId()) * 100 / insuranceTotalAmount));
            }
            provinceList.add(provinceJson);
        }

        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(provinceList);
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
        paramPagerVo.setColumnFieldIds("date,advertiseDesc");
        paramPagerVo.setFeeType(FeeTypeEnum.FEE_TYPE_RESERVE.getText());
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        int searchCount;
        Map<String, Integer> keywordMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            if (StringUtils.isEmpty(flowReserve.getAdvertiseDesc()) || "-".equals(flowReserve.getAdvertiseDesc())) {
                continue;
            }
            if (keywordMap.get(flowReserve.getAdvertiseDesc()) == null) {
                searchCount = 0;
            } else {
                searchCount = keywordMap.get(flowReserve.getAdvertiseDesc());
            }

            searchCount++;
            keywordMap.put(flowReserve.getAdvertiseDesc(), searchCount);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(keywordMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }

        });

        int count = 1;
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

    @ResponseBody
    @RequestMapping("/getInsuranceFeeDistribution")
    public ResultVo getInsuranceFeeDistribution(DashboardParamVo paramVo){
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
        paramPagerVo.setColumnFieldIds("reserveDate,provinceId,insuranceFee");
        paramPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);
        Integer provinceId;
        double underwriteAmount;
        List<Integer> provinceIdList = new ArrayList<>();
        Map<Integer, Double> insuranceAmountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            provinceId = underwrite.getProvinceId();
            if(provinceId == null || provinceId == 0){
                continue;
            }
            provinceIdList.add(provinceId);

            underwriteAmount = 0.0d;
            if(insuranceAmountMap.get(provinceId) != null){
                underwriteAmount = insuranceAmountMap.get(provinceId);
            }
            if(StringUtils.hasText(underwrite.getInsuranceFee()) && !"".equals(underwrite.getInsuranceFee())){
                underwriteAmount += Double.parseDouble(underwrite.getInsuranceFee());
            }
            insuranceAmountMap.put(provinceId, underwriteAmount);
        }

        Map<Integer, String> areaNameMap = new HashMap<>();
        areaNameMap.put(0, "未知地区");
        List<Area> areaList = areaService.findByIdList(provinceIdList);
        for (Area area : areaList) {
            areaNameMap.put(area.getAreaId(), area.getAreaName());
        }

        List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(insuranceAmountMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // 获取省份的名称
        int index = 0;
        JSONObject provinceJson;
        DecimalFormat decimalFormat = new DecimalFormat("#0");
        List<JSONObject> provinceList = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : entryList) {
            if (index >= 10) {
                break;
            }
            provinceJson = new JSONObject();
            provinceJson.put("name", areaNameMap.get(entry.getKey()));
            provinceJson.put("value", decimalFormat.format(entry.getValue()));
            provinceList.add(provinceJson);
            index ++;
        }

        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(provinceList);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getConsumeAmount")
    public ResultVo getConsumeAmount(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        String rtnMsg = initDashboardParam(paramVo);
        if(StringUtils.hasText(rtnMsg)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(rtnMsg);
            return resultVo;
        }

        String dateKey;
        double totalAmount;
        MediaItemParamPagerVo mediaItemParamPagerVo = new MediaItemParamPagerVo();
        mediaItemParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaItemParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaItemParamPagerVo.setColumnFieldIds("date,speedCost");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);
        Map<String, Object> consumeAmountMap = DataUtils.initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "double");
        for (MediaItem mediaItem : mediaItemList) {
            dateKey = DataUtils.getDateKeyByDateType(mediaItem.getDate(), paramVo.getDateType());
            totalAmount = 0.00;
            if(consumeAmountMap.get(dateKey) != null){
                totalAmount = (double)consumeAmountMap.get(dateKey);
            }
            if(StringUtils.hasText(mediaItem.getSpeedCost()) && !"-".equals(mediaItem.getSpeedCost())) {
                totalAmount += Double.valueOf(mediaItem.getSpeedCost());
            }
            consumeAmountMap.put(dateKey, totalAmount);
        }

        JSONObject resultJson = formLineEchartResultJson(consumeAmountMap);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }









    private JSONObject formAgeBarEchartResultJson(Map<Integer, Double> paramMap) {
        JSONObject resultJson = new JSONObject();
        JSONArray contentArray = new JSONArray();
        Set<Integer> keySet = paramMap.keySet();
        Integer[] keyArray = keySet.toArray(new Integer[keySet.size()]);
        Arrays.sort(keyArray);

        Integer maxKey = 0;
        DecimalFormat decimalFormat = new DecimalFormat("#0");
        double currentAmount, maxAmount = 0.00;
        for(Integer key : keyArray){
            currentAmount = paramMap.get(key);
            contentArray.add(decimalFormat.format(currentAmount));
            if(currentAmount > maxAmount){
                maxAmount = currentAmount;
                maxKey = key;
            }
        }
        resultJson.put("contentList", contentArray);
        resultJson.put("maxAmount", decimalFormat.format(maxAmount));
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
        DecimalFormat decimalFormat = new DecimalFormat("#0");
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);

        Integer intValue = 0;
        Double doubleValue = 0.00d;
        for(String key : keyArray){
            titleArray.add(key);
            if (statCountMap.get(key) instanceof Double) {
                doubleValue += (Double) statCountMap.get(key);
                contentArray.add(decimalFormat.format((Double) statCountMap.get(key)));
            } else {
                intValue += (Integer)statCountMap.get(key);
                contentArray.add(statCountMap.get(key));
            }
        }
        resultJson.put("titleList", titleArray);
        resultJson.put("contentList", contentArray);
        resultJson.put("totalValue", decimalFormat.format(intValue + doubleValue));

        int size = contentArray.size();
        double lastValue = 0;
        double penultValue = 0;
        double trendRate = 0;

        if(size > 0){
            lastValue = contentArray.getDoubleValue(size - 1);
        }
        resultJson.put("lastValue", decimalFormat.format(lastValue));
        if(size > 1){
            penultValue = contentArray.getDoubleValue(size - 2);
        }
        resultJson.put("penultValue", decimalFormat.format(penultValue));
        if(penultValue != 0 && penultValue != 0){
            trendRate = (lastValue - penultValue) * 100 / penultValue;
        }
        resultJson.put("trendRate", decimalFormat.format(trendRate));
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
            String startDate = DateUtils.formatDate(new Date(), "yyyy-MM") + "-01";
            paramVo.setChooseDate(startDate + " - " + endDate);
        }
        if(StringUtils.isEmpty(paramVo.getDateType())){
            paramVo.setDateType(DateTypeEnum.日.getValue());
        }

        return null;
    }

}
