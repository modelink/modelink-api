package com.modelink.admin.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.vo.DashboardParamVo;
import com.modelink.common.enums.DateTypeEnum;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.Abnormal;
import com.modelink.reservation.bean.FlowReserve;
import com.modelink.reservation.bean.MediaItem;
import com.modelink.reservation.bean.Underwrite;
import com.modelink.reservation.service.AbnormalService;
import com.modelink.reservation.service.FlowReserveService;
import com.modelink.reservation.service.MediaItemService;
import com.modelink.reservation.service.UnderwriteService;
import com.modelink.reservation.vo.AbnormalParamPagerVo;
import com.modelink.reservation.vo.FlowReserveParamPagerVo;
import com.modelink.reservation.vo.MediaItemParamPagerVo;
import com.modelink.reservation.vo.UnderwriteParamPagerVo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("/admin/dashboard")
public class DashboardController {

    @Resource
    private UnderwriteService underwriteService;
    @Resource
    private FlowReserveService flowReserveService;
    @Resource
    private MediaItemService mediaItemService;
    @Resource
    private AbnormalService abnormalService;

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

        JSONObject resultJson = formResultJson(statCountMap);
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

        JSONObject resultJson = formResultJson(statCountMap);
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

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("finishDate,insuranceFee");
        paramPagerVo.setDateField("finishDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        String dateKey;
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

        JSONObject resultJson = formResultJson(totalAmountMap);
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
        Map<String, Object> statResultMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "double");
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

        JSONObject resultJson = formResultJson(statResultMap);
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
        paramPagerVo.setDateField("date");
        paramPagerVo.setColumnFieldIds("id,date");
        List<Abnormal> abnormalList = abnormalService.findListByParam(paramPagerVo);

        String dateKey;
        int reserveCount;
        Map<String, Object> statCountMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "int");
        for (Abnormal abnormal : abnormalList) {
            dateKey = getDateKeyByDateType(abnormal.getDate(), paramVo.getDateType());
            reserveCount = 0;
            if(statCountMap.get(dateKey) != null){
                reserveCount = (Integer)statCountMap.get(dateKey);
            }
            reserveCount ++;
            statCountMap.put(dateKey, reserveCount);
        }

        JSONObject resultJson = formResultJson(statCountMap);
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


        Set<String> mobileSet = new HashSet<>();
        for (Underwrite underwrite : underwriteList) {
            mobileSet.add(underwrite.getReserveMobile());
        }

        Map<String, String> mobile2DateMap = new HashMap<>();
        List<FlowReserve> flowReserveList = flowReserveService.findListByMobiles(mobileSet, "date asc");
        for(FlowReserve flowReserve : flowReserveList) {
            mobile2DateMap.put(flowReserve.getReserveMobile(), flowReserve.getDate());
        }

        for(Underwrite underwrite : underwriteList){
            String finishDate = underwrite.getFinishDate();
            String reserveDate = mobile2DateMap.get(underwrite.getReserveMobile());
            //String transformCycle = (finishDate - reserveDate) / mobileSet.size();
        }


        String dateKey;
        double totalAmount;
        FlowReserveParamPagerVo flowReserveParamPagerVo = new FlowReserveParamPagerVo();
        flowReserveParamPagerVo.setChooseDate(paramVo.getChooseDate());
        flowReserveParamPagerVo.setMerchantId(paramVo.getMerchantId());
        flowReserveParamPagerVo.setColumnFieldIds("date,speedCost");
        //List<FlowReserve> flowReserveList = flowReserveService.findListByParam(flowReserveParamPagerVo);
        Map<String, Object> statAmountMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "double");
        for (FlowReserve flowReserve : flowReserveList) {
            dateKey = getDateKeyByDateType(flowReserve.getDate(), paramVo.getDateType());
            totalAmount = 0.00;
            if(statAmountMap.get(dateKey) != null){
                totalAmount = (double)statAmountMap.get(dateKey);
            }
            totalAmount += Double.valueOf(flowReserve.getBrowser());
            statAmountMap.put(dateKey, totalAmount);
        }

        int count = 0;
        double amount = 0.00;
        Map<String, Object> statResultMap = initResultMap(paramVo.getChooseDate(), paramVo.getDateType(), "double");
        Iterator<String> iterator = statAmountMap.keySet().iterator();
//        while(iterator.hasNext()){
//            dateKey = iterator.next();
//            if(StringUtils.isEmpty(statCountMap.get(dateKey))){
//                count = 0;
//            }else{
//                count = (int)statCountMap.get(dateKey);
//            }
//            if(StringUtils.isEmpty(statAmountMap.get(dateKey))){
//                amount = 0.00;
//            }else{
//                amount = (double)statAmountMap.get(dateKey);
//            }
//            if(count == 0){
//                statResultMap.put(dateKey, 0);
//            }else{
//                statResultMap.put(dateKey, amount / count);
//            }
//        }

        JSONObject resultJson = formResultJson(statResultMap);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

















    private JSONObject formResultJson(Map<String, Object> statCountMap){
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
            startDate = DateUtils.formatDate(nextDate, "yyyy-MM-dd");
            nextDate = DateUtils.calculateDate(startDate, Calendar.DAY_OF_YEAR, 1, "yyyy-MM-dd");
            dateKey = getDateKeyByDateType(nextDate, dateType);
            resultMap.put(dateKey, initValue);
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
