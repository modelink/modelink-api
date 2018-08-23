package com.modelink.admin.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.vo.DashboardParamVo;
import com.modelink.admin.vo.DashboardSummaryParamVo;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.FlowReserve;
import com.modelink.reservation.bean.MediaItem;
import com.modelink.reservation.bean.Underwrite;
import com.modelink.reservation.service.FlowReserveService;
import com.modelink.reservation.service.MediaItemService;
import com.modelink.reservation.service.UnderwriteService;
import com.modelink.reservation.vo.FlowReserveParamPagerVo;
import com.modelink.reservation.vo.MediaItemParamPagerVo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping("/admin/dashboard/keyword")
public class DashboardKeywordController {

    @Resource
    private MediaItemService mediaItemService;
    @Resource
    private FlowReserveService flowReserveService;
    @Resource
    private UnderwriteService underwriteService;

    @RequestMapping
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/dashboard/keyword");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/getClickCount")
    public ResultVo getClickCount(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        MediaItemParamPagerVo paramPagerVo = new MediaItemParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("date,keyWord,clickCount");
        paramPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(paramPagerVo);

        int clickCount;
        Map<String, Integer> clickCountMap = new HashMap<>();
        for (MediaItem mediaItem : mediaItemList) {
            if ("-".equals(mediaItem.getKeyWord()) || "".equals(mediaItem.getKeyWord())) {
                continue;
            }
            if (clickCountMap.get(mediaItem.getKeyWord()) == null) {
                clickCount = 0;
            } else {
                clickCount = clickCountMap.get(mediaItem.getKeyWord());
            }

            clickCount += mediaItem.getClickCount();
            clickCountMap.put(mediaItem.getKeyWord(), clickCount);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(clickCountMap.entrySet());
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

    @ResponseBody
    @RequestMapping("/getReserveCount")
    public ResultVo getReserveCount(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("date,searchWord");
        paramPagerVo.setDateField("date");
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
            }

            searchCount++;
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

    @ResponseBody
    @RequestMapping("/getUnderwriteAmount")
    public ResultVo getUnderwriteAmount(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("date,reserveMobile,searchWord");
        paramPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        Set<String> mobileSet = new HashSet<>();
        for (FlowReserve flowReserve : flowReserveList) {
            mobileSet.add(flowReserve.getReserveMobile());
        }
        double insuranceFee;
        Map<String, Double> insuranceFeeMap = new HashMap<>();
        List<Underwrite> underwriteList = underwriteService.findListByMobiles(mobileSet, "finish_date");
        for (Underwrite underwrite : underwriteList) {
            if(StringUtils.isEmpty(underwrite.getInsuranceFee()) || "-".equals(underwrite.getInsuranceFee())){
                continue;
            }
            if(insuranceFeeMap.get(underwrite.getReserveMobile()) == null){
                insuranceFee = 0.0d;
            }else{
                insuranceFee = insuranceFeeMap.get(underwrite.getReserveMobile());
            }
            insuranceFee += Double.parseDouble(underwrite.getInsuranceFee());
            insuranceFeeMap.put(underwrite.getReserveMobile(), insuranceFee);
        }


        Map<String, Double> searchWordMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            if ("-".equals(flowReserve.getSearchWord()) || "".equals(flowReserve.getSearchWord())) {
                continue;
            }
            if (StringUtils.isEmpty(flowReserve.getReserveMobile()) ||
                    insuranceFeeMap.get(flowReserve.getReserveMobile()) == null){
                continue;
            }
            if (searchWordMap.get(flowReserve.getSearchWord()) == null) {
                insuranceFee = 0.0d;
            } else {
                insuranceFee = searchWordMap.get(flowReserve.getSearchWord());
            }

            insuranceFee += insuranceFeeMap.get(flowReserve.getReserveMobile());
            searchWordMap.put(flowReserve.getSearchWord(), insuranceFee);
        }

        List<Map.Entry<String, Double>> list = new ArrayList<>(searchWordMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }

        });

        int count = 0;
        JSONObject wordCloud;
        JSONArray wordCloudArray = new JSONArray();
        for (Map.Entry<String, Double> mapping : list) {
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
    @RequestMapping("/getClickRateAndCost")
    public ResultVo getClickRateAndCost(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        MediaItemParamPagerVo paramPagerVo = new MediaItemParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("date,searchWord");
        paramPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(paramPagerVo);



        int showCount, clickCount;
        double consumeAmount;
        Map<String, Integer> showCountMap = new HashMap<>();
        Map<String, Integer> clickCountMap = new HashMap<>();
        Map<String, Double> consumeAmountMap = new HashMap<>();
        for (MediaItem mediaItem : mediaItemList) {
            if (StringUtils.isEmpty(mediaItem.getAdvertiseActive())) {
                continue;
            }
            showCount = 0;
            if (showCountMap.get(mediaItem.getAdvertiseActive()) != null) {
                showCount = showCountMap.get(mediaItem.getAdvertiseActive());
            }
            showCount += mediaItem.getShowCount();
            showCountMap.put(mediaItem.getAdvertiseActive(), showCount);

            clickCount = 0;
            if (clickCountMap.get(mediaItem.getAdvertiseActive()) != null) {
                clickCount = clickCountMap.get(mediaItem.getAdvertiseActive());
            }
            clickCount += mediaItem.getClickCount();
            clickCountMap.put(mediaItem.getAdvertiseActive(), clickCount);

            consumeAmount = 0.0d;
            if (consumeAmountMap.get(mediaItem.getAdvertiseActive()) != null) {
                consumeAmount = consumeAmountMap.get(mediaItem.getAdvertiseActive());
            }
            if (StringUtils.hasText(mediaItem.getSpeedCost())) {
                consumeAmount += Double.parseDouble(mediaItem.getSpeedCost());
            }
            consumeAmountMap.put(mediaItem.getAdvertiseActive(), consumeAmount);
        }

        String advertiseActive;
        JSONArray titleArray, clickRateArray, clickCostArray;
        Map<String, Double> resultMap = new HashMap<>();
        Iterator<String> iterator = showCountMap.keySet().iterator();
        titleArray = new JSONArray();
        clickRateArray = new JSONArray();
        clickCostArray = new JSONArray();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        while(iterator.hasNext()){
            advertiseActive = iterator.next();
            if(StringUtils.isEmpty(clickCountMap.get(advertiseActive))){
                continue;
            }
            if(showCountMap.get(advertiseActive) == 0){
                continue;
            }

            clickCount = clickCountMap.get(advertiseActive);
            showCount = showCountMap.get(advertiseActive);
            consumeAmount = consumeAmountMap.get(advertiseActive);
            titleArray.add(advertiseActive);
            clickRateArray.add(decimalFormat.format(clickCount * 100 / showCount));
            clickCostArray.add(decimalFormat.format(consumeAmount / clickCount));
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleArray);
        resultJson.put("clickRateList", clickRateArray);
        resultJson.put("clickCostList", clickCostArray);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }











    private void initDashboardParam(DashboardParamVo dashboardParamVo){
        if(dashboardParamVo == null){
            dashboardParamVo = new DashboardParamVo();
        }
        if(StringUtils.isEmpty(dashboardParamVo.getChooseDate())){
            String endDate = DateUtils.calculateDate(new Date(), Calendar.DAY_OF_YEAR, -1, "yyyy-MM-dd");
            String startDate = DateUtils.calculateDate(new Date(), Calendar.DAY_OF_YEAR, -8, "yyyy-MM-dd");
            dashboardParamVo.setChooseDate(startDate + " - " + endDate);
        }
        if(StringUtils.hasText(dashboardParamVo.getPlatformName())){
            dashboardParamVo.setPlatformName(dashboardParamVo.getPlatformName().toUpperCase());
        }
    }
}
