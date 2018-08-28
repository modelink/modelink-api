package com.modelink.admin.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.vo.DashboardParamVo;
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
import com.modelink.reservation.vo.UnderwriteParamPagerVo;
import com.modelink.usercenter.bean.Merchant;
import com.modelink.usercenter.service.MerchantService;
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
    private MerchantService merchantService;
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

        int count = 1;
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
        paramPagerVo.setColumnFieldIds("date,advertiseActive,clickCount,showCount,speedCost");
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

    @ResponseBody
    @RequestMapping("/getClickRateAndCostByKeyword")
    public ResultVo getClickRateAndCostByKeyword(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();
        if(StringUtils.isEmpty(paramVo.getAdvertiseActive())){
            return resultVo;
        }
        initDashboardParam(paramVo);

        MediaItemParamPagerVo paramPagerVo = new MediaItemParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("date,keyWord,clickCount,showCount,speedCost");
        paramPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(paramPagerVo);


        int showCount, clickCount;
        double consumeAmount;
        Map<String, Integer> showCountMap = new HashMap<>();
        Map<String, Integer> clickCountMap = new HashMap<>();
        Map<String, Double> consumeAmountMap = new HashMap<>();
        for (MediaItem mediaItem : mediaItemList) {
            if (StringUtils.isEmpty(mediaItem.getKeyWord())) {
                continue;
            }
            showCount = 0;
            if (showCountMap.get(mediaItem.getKeyWord()) != null) {
                showCount = showCountMap.get(mediaItem.getKeyWord());
            }
            showCount += mediaItem.getShowCount();
            showCountMap.put(mediaItem.getKeyWord(), showCount);

            clickCount = 0;
            if (clickCountMap.get(mediaItem.getKeyWord()) != null) {
                clickCount = clickCountMap.get(mediaItem.getKeyWord());
            }
            clickCount += mediaItem.getClickCount();
            clickCountMap.put(mediaItem.getKeyWord(), clickCount);

            consumeAmount = 0.0d;
            if (consumeAmountMap.get(mediaItem.getKeyWord()) != null) {
                consumeAmount = consumeAmountMap.get(mediaItem.getKeyWord());
            }
            if (StringUtils.hasText(mediaItem.getSpeedCost())) {
                consumeAmount += Double.parseDouble(mediaItem.getSpeedCost());
            }
            consumeAmountMap.put(mediaItem.getKeyWord(), consumeAmount);
        }


        // 挑选点击量前30的关键字
        int idx = 1;
        Set<String> keywordSet = new HashSet<>();
        List<Map.Entry<String, Integer>> list = new ArrayList<>(clickCountMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        for (Map.Entry<String, Integer> mapping : list) {
            if(idx >= 30){
                break;
            }
            keywordSet.add(mapping.getKey());
            idx ++;
        }


        String keyword;
        JSONArray titleArray, clickRateArray, clickCostArray;
        titleArray = new JSONArray();
        clickRateArray = new JSONArray();
        clickCostArray = new JSONArray();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        Iterator<String> iterator = showCountMap.keySet().iterator();
        while(iterator.hasNext()){
            keyword = iterator.next();
            if(!keywordSet.contains(keyword)){
                continue;
            }
            if(StringUtils.isEmpty(clickCountMap.get(keyword))){
                continue;
            }
            if(showCountMap.get(keyword) == 0){
                continue;
            }

            clickCount = clickCountMap.get(keyword);
            if(clickCount == 0) clickCount = 1;
            showCount = showCountMap.get(keyword);
            if(showCount == 0) showCount = 1;
            consumeAmount = consumeAmountMap.get(keyword);
            titleArray.add(keyword);
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

    @ResponseBody
    @RequestMapping("/getUnderwriteSummaryByKeyword")
    public ResultVo getUnderwriteSummaryByKeyword(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("date,reserveMobile,advertiseDesc");
        paramPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        int reserveCount;
        Set<String> mobileSet = new HashSet<>();
        Map<String, Integer> reserveCountMap = new HashMap<>();
        Map<String, String> mobile2SearchKeyMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            mobileSet.add(flowReserve.getReserveMobile());
            mobile2SearchKeyMap.put(flowReserve.getReserveMobile(), flowReserve.getAdvertiseDesc());

            reserveCount = 0;
            if (reserveCountMap.get(flowReserve.getAdvertiseDesc()) != null) {
                reserveCount = reserveCountMap.get(flowReserve.getAdvertiseDesc());
            }
            reserveCount ++;
            reserveCountMap.put(flowReserve.getAdvertiseDesc(), reserveCount);
        }
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(reserveCountMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int idx = 1;
        List<String> keywordList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : entryList) {
            if(idx > 30) break;
            keywordList.add(entry.getKey());
            idx ++;
        }

        UnderwriteParamPagerVo underwriteParamPagerVo = new UnderwriteParamPagerVo();
        underwriteParamPagerVo.setChooseDate(paramVo.getChooseDate());
        underwriteParamPagerVo.setMerchantId(paramVo.getMerchantId());
        underwriteParamPagerVo.setPlatformName(paramVo.getPlatformName());
        underwriteParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        underwriteParamPagerVo.setMobiles(mobileSet);
        underwriteParamPagerVo.setColumnFieldIds("id,reserveMobile,insuranceFee");
        underwriteParamPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);

        int underwriteCount;
        double underwriteAmount;
        String keyword; // 媒体表中的关键词相当于预约表中的广告描述
        Map<String, Integer> underwriteCountMap = new HashMap<>();
        Map<String, Double> underwriteAmountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            if(mobile2SearchKeyMap.get(underwrite.getReserveMobile()) == null) continue;
            keyword = mobile2SearchKeyMap.get(underwrite.getReserveMobile());

            underwriteCount = 0;
            if(underwriteCountMap.get(keyword) != null){
                underwriteCount = underwriteCountMap.get(keyword);
            }
            underwriteCount ++;
            underwriteCountMap.put(keyword, underwriteCount);

            underwriteAmount = 0.0d;
            if(underwriteAmountMap.get(keyword) != null){
                underwriteAmount = underwriteAmountMap.get(keyword);
            }
            if (StringUtils.hasText(underwrite.getInsuranceFee())
                    && !"-".equals(underwrite.getInsuranceFee())) {
                underwriteAmount += Double.parseDouble(underwrite.getInsuranceFee());
            }
            underwriteAmountMap.put(keyword, underwriteAmount);
        }

        List<Integer> reserveCountList = new ArrayList<>();
        List<Integer> underwriteCountList = new ArrayList<>();
        List<Double> underwriteAmountList = new ArrayList<>();
        for(String keywordStr : keywordList){
            reserveCountList.add(reserveCountMap.get(keywordStr));
            underwriteCountList.add(underwriteCountMap.get(keywordStr));
            underwriteAmountList.add(underwriteAmountMap.get(keywordStr));
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", keywordList);
        resultJson.put("reserveCountList", reserveCountList);
        resultJson.put("underwriteCountList", underwriteCountList);
        resultJson.put("underwriteAmountList", underwriteAmountList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getCostSummaryByKeyword")
    public ResultVo getCostSummaryByKeyword(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("date,advertiseDesc");
        paramPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        String keyword;
        int reserveCount;
        Map<String, Integer> reserveCountMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            keyword = flowReserve.getAdvertiseDesc();
            reserveCount = 0;
            if(reserveCountMap.get(keyword) != null){
                reserveCount = reserveCountMap.get(keyword);
            }
            reserveCount ++;
            reserveCountMap.put(keyword, reserveCount);
        }

        int clickCount;
        double consumeAmount;
        MediaItemParamPagerVo mediaItemParamPagerVo = new MediaItemParamPagerVo();
        mediaItemParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaItemParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaItemParamPagerVo.setPlatformName(paramVo.getPlatformName());
        mediaItemParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        mediaItemParamPagerVo.setColumnFieldIds("date,keyWord,clickCount,speedCost");
        mediaItemParamPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);
        Map<String, Double> consumeAmountMap = new HashMap<>();
        Map<String, Integer> clickCountMap = new HashMap<>();
        for (MediaItem mediaItem : mediaItemList) {
            keyword = mediaItem.getKeyWord();
            clickCount = 0;
            if(clickCountMap.get(keyword) != null) {
                clickCount = clickCountMap.get(keyword);
            }
            if(mediaItem.getClickCount() != null) {
                clickCount += mediaItem.getClickCount();
            }
            clickCountMap.put(keyword, clickCount);

            consumeAmount = 0.0d;
            if(consumeAmountMap.get(keyword) != null) {
                consumeAmount = consumeAmountMap.get(keyword);
            }
            if(StringUtils.hasText(mediaItem.getSpeedCost()) && !"-".equals(mediaItem.getSpeedCost())) {
                consumeAmount += Double.valueOf(mediaItem.getSpeedCost());
            }
            consumeAmountMap.put(keyword, consumeAmount);
        }


        Map<String, Double> clickCostResultMap = new HashMap<>();
        Map<String, Double> transformCostResultMap = new HashMap<>();
        Iterator<String> iterator = consumeAmountMap.keySet().iterator();
        while(iterator.hasNext()){
            keyword = iterator.next();

            if(StringUtils.isEmpty(consumeAmountMap.get(keyword))){
                consumeAmount = 0.0d;
            }else{
                consumeAmount = consumeAmountMap.get(keyword);
            }
            // 计算转化成本
            if(StringUtils.isEmpty(reserveCountMap.get(keyword))){
                reserveCount = 0;
            }else{
                reserveCount = reserveCountMap.get(keyword);
            }
            if(reserveCount == 0){
                transformCostResultMap.put(keyword, 0.0d);
            }else{
                transformCostResultMap.put(keyword, consumeAmount / reserveCount);
            }
            // 计算点击成本
            if(StringUtils.isEmpty(clickCountMap.get(keyword))){
                clickCount = 0;
            }else{
                clickCount = (int)clickCountMap.get(keyword);
            }
            if(clickCount == 0){
                clickCostResultMap.put(keyword, 0.0d);
            }else{
                clickCostResultMap.put(keyword, consumeAmount / clickCount);
            }
        }


        List<Map.Entry<String, Double>> entryList = new ArrayList(transformCostResultMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        int idx = 1;
        List<String> titleList = new ArrayList<>();
        List<Double> transformCostList = new ArrayList<>();
        List<Double> clickCostList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : entryList) {
            if(idx > 30) break;
            titleList.add(entry.getKey());
            transformCostList.add(entry.getValue());
            clickCostList.add(clickCostResultMap.get(entry.getKey()));
            idx ++;
        }


        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("clickCostList", clickCostList);
        resultJson.put("transformCostList", transformCostList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getTransformCycleAndRate")
    public ResultVo getTransformCycleAndRate(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setColumnFieldIds("id,reserveDate,finishDate,keyword");
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        List<Underwrite> tempUnderwriteList;
        Map<String, List<Underwrite>> underwriteMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            if(StringUtils.isEmpty(underwrite.getKeyword())) continue;
            tempUnderwriteList = new ArrayList<>();
            if(underwriteMap.get(underwrite.getKeyword()) != null){
                tempUnderwriteList = underwriteMap.get(underwrite.getKeyword());
            }
            tempUnderwriteList.add(underwrite);
            underwriteMap.put(underwrite.getKeyword(), tempUnderwriteList);
        }

        String keyword;
        String transformCycle;
        int difference, totalDifference;
        String reserveDate, finishDate;
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        Map<String, Integer> underwriteCountMap = new HashMap<>();
        Map<String, String> transformCycleMap = new HashMap<>();
        Iterator<String> iterator = underwriteMap.keySet().iterator();
        while(iterator.hasNext()){
            keyword = iterator.next();
            tempUnderwriteList = underwriteMap.get(keyword);
            if(tempUnderwriteList == null || tempUnderwriteList.size() <= 0){
                continue;
            }

            totalDifference = 0;
            for(Underwrite underwrite : tempUnderwriteList){
                finishDate = underwrite.getFinishDate();
                reserveDate = underwrite.getReserveDate();
                difference = DateUtils.getDateDifference(reserveDate, finishDate);
                totalDifference += difference;
            }
            transformCycle = decimalFormat.format(totalDifference / tempUnderwriteList.size());
            transformCycleMap.put(keyword, transformCycle);
            underwriteCountMap.put(keyword, tempUnderwriteList.size());
        }

        // 查询符合条件的数据
        MediaItemParamPagerVo mediaItemParamPagerVo = new MediaItemParamPagerVo();
        mediaItemParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaItemParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaItemParamPagerVo.setPlatformName(paramVo.getPlatformName());
        mediaItemParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("id,keyWord,clickCount");
        paramPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);

        String transformRate;
        int clickCount, underwriteCount;
        Map<String, Integer> clickCountMap = new HashMap<>();
        for (MediaItem mediaItem : mediaItemList) {
            if(mediaItem.getKeyWord() == null){
                continue;
            }
            if(StringUtils.isEmpty(mediaItem.getClickCount())){
                continue;
            }
            clickCount = 0;
            if(clickCountMap.get(mediaItem.getKeyWord()) != null){
                clickCount = clickCountMap.get(mediaItem.getKeyWord());
            }
            clickCount += mediaItem.getClickCount();
            clickCountMap.put(mediaItem.getKeyWord(), clickCount);
        }
        Iterator<String> clickCountIter = clickCountMap.keySet().iterator();
        Map<String, String> transformRateMap = new HashMap<>();
        while(clickCountIter.hasNext()){
            keyword = clickCountIter.next();
            underwriteCount = 0;
            if(underwriteCountMap.get(keyword) != null){
                underwriteCount = underwriteCountMap.get(keyword);
            }

            if(clickCountMap.get(keyword) == 0){
                transformRate = "0";
            }else {
                transformRate = decimalFormat.format(underwriteCount * 100 / clickCountMap.get(keyword));
            }
            transformRateMap.put(keyword, transformRate);
        }

        // 计算出转化率最高的TOP30
        List<Map.Entry<String, String>> entryList = new ArrayList<>(transformRateMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        int idx = 1;
        List<String> titleList = new ArrayList<>();
        List<String> transformRateList = new ArrayList<>();
        List<String> transformCycleList = new ArrayList<>();
        for (Map.Entry<String, String> entry : entryList) {
            if(idx > 30) break;
            titleList.add(entry.getKey());
            transformRateList.add(transformRateMap.get(entry.getKey()));
            transformCycleList.add(transformCycleMap.get(entry.getKey()));
            idx ++;
        }


        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("transformRateList", transformRateList);
        resultJson.put("transformCycleList", transformCycleList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/keywordTableGrid")
    public ResultVo keywordTableGrid(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);
        Set<String> keywordSet = new HashSet<>();
        Map<String, String> merchantMap = new HashMap<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("date,merchantId,platformName,advertiseActive,advertiseDesc");
        paramPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        String keyword;
        int reserveCount;
        Map<String, Integer> reserveCountMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            keyword = flowReserve.getAdvertiseDesc();
            reserveCount = 0;
            if(reserveCountMap.get(keyword) != null){
                reserveCount = reserveCountMap.get(keyword);
            }
            reserveCount ++;
            reserveCountMap.put(keyword, reserveCount);
            keywordSet.add(keyword);
            merchantMap.put(keyword, flowReserve.getMerchantId() + "|" + flowReserve.getPlatformName() + "|" + flowReserve.getAdvertiseActive());
        }


        /** 转化周期计算 **/
        UnderwriteParamPagerVo underwriteParamPagerVo = new UnderwriteParamPagerVo();
        underwriteParamPagerVo.setChooseDate(paramVo.getChooseDate());
        underwriteParamPagerVo.setMerchantId(paramVo.getMerchantId());
        underwriteParamPagerVo.setColumnFieldIds("id,merchantId,platformName,advertiseActive,reserveDate,finishDate,keyword");
        underwriteParamPagerVo.setPlatformName(paramVo.getPlatformName());
        underwriteParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        underwriteParamPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);

        List<Underwrite> tempUnderwriteList;
        Map<String, List<Underwrite>> underwriteListMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            if(StringUtils.isEmpty(underwrite.getKeyword())) continue;
            keyword = underwrite.getKeyword();
            keywordSet.add(keyword);
            merchantMap.put(keyword, underwrite.getMerchantId() + "|" + underwrite.getPlatformName() + "|" + underwrite.getAdvertiseActive());
            tempUnderwriteList = new ArrayList<>();
            if(underwriteListMap.get(keyword) != null){
                tempUnderwriteList = underwriteListMap.get(keyword);
            }
            tempUnderwriteList.add(underwrite);
            underwriteListMap.put(keyword, tempUnderwriteList);
        }
        String transformCycle;
        double insuranceAmount;
        int difference, totalDifference;
        String reserveDate, finishDate;

        Map<String, Double> underwriteAmountMap = new HashMap<>();
        Map<String, Integer> underwriteCountMap = new HashMap<>();
        Map<String, String> transformCycleMap = new HashMap<>();
        Iterator<String> iterator = underwriteListMap.keySet().iterator();
        while(iterator.hasNext()){
            keyword = iterator.next();
            tempUnderwriteList = underwriteListMap.get(keyword);
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
            transformCycleMap.put(keyword, transformCycle);
            underwriteCountMap.put(keyword, tempUnderwriteList.size());
            underwriteAmountMap.put(keyword, insuranceAmount);
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
        mediaItemParamPagerVo.setColumnFieldIds("date,merchantId,platformName,advertiseActive,keyWord,clickCount,speedCost");
        mediaItemParamPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);
        Map<String, Double> consumeAmountMap = new HashMap<>();
        Map<String, Integer> clickCountMap = new HashMap<>();
        for (MediaItem mediaItem : mediaItemList) {
            keyword = mediaItem.getKeyWord();
            keywordSet.add(keyword);
            merchantMap.put(keyword, mediaItem.getMerchantId() + "|" + mediaItem.getPlatformName() + "|" + mediaItem.getAdvertiseActive());

            clickCount = 0;
            if(clickCountMap.get(keyword) != null) {
                clickCount = clickCountMap.get(keyword);
            }
            if(mediaItem.getClickCount() != null) {
                clickCount += mediaItem.getClickCount();
            }
            clickCountMap.put(keyword, clickCount);

            consumeAmount = 0.0d;
            if(consumeAmountMap.get(keyword) != null) {
                consumeAmount = consumeAmountMap.get(keyword);
            }
            if(StringUtils.hasText(mediaItem.getSpeedCost()) && !"-".equals(mediaItem.getSpeedCost())) {
                consumeAmount += Double.valueOf(mediaItem.getSpeedCost());
            }
            consumeAmountMap.put(keyword, consumeAmount);
        }


        int underwriteCount = 0;
        Map<String, String> transformRateMap = new HashMap<>();
        Map<String, String> clickCostResultMap = new HashMap<>();
        Map<String, String> transformCostResultMap = new HashMap<>();
        Iterator<String> consumeAmountIterator = consumeAmountMap.keySet().iterator();
        while(consumeAmountIterator.hasNext()){
            keyword = consumeAmountIterator.next();

            if(StringUtils.isEmpty(consumeAmountMap.get(keyword))){
                consumeAmount = 0.0d;
            }else{
                consumeAmount = consumeAmountMap.get(keyword);
            }
            // 计算转化成本
            if(StringUtils.isEmpty(reserveCountMap.get(keyword))){
                reserveCount = 0;
            }else{
                reserveCount = reserveCountMap.get(keyword);
            }
            if(reserveCount == 0){
                transformCostResultMap.put(keyword, "0.00");
            }else{
                transformCostResultMap.put(keyword, decimalFormat.format(consumeAmount / reserveCount));
            }
            // 计算点击成本
            if(StringUtils.isEmpty(clickCountMap.get(keyword))){
                clickCount = 0;
            }else{
                clickCount = (int)clickCountMap.get(keyword);
            }
            if(clickCount == 0){
                clickCostResultMap.put(keyword, "0.00");
            }else{
                clickCostResultMap.put(keyword, decimalFormat.format(consumeAmount / clickCount));
            }
            // 计算转化率
            if(StringUtils.isEmpty(clickCountMap.get(keyword))){
                clickCount = 0;
            }else{
                clickCount = (int)clickCountMap.get(keyword);
            }
            if(clickCount == 0 || underwriteCountMap.get(keyword) == null){
                transformRateMap.put(keyword, "0.00");
            }else{
                underwriteCount = underwriteCountMap.get(keyword);
                transformRateMap.put(keyword, decimalFormat.format(underwriteCount * 100.00d / clickCount));
            }
        }
        /** 计算转化成本与点击成本 **/


        /** 汇总结果 **/
        int indexNo = 0;
        Merchant merchant;
        String[] merchantList;
        Map<String, String> resultBean;
        List<Map<String, String>> resultList = new ArrayList<>();
        for (String keywordBean : keywordSet) {
            indexNo ++;
            resultBean = new HashMap<>();
            resultBean.put("keyword", keywordBean);
            resultBean.put("indexNo", String.valueOf(indexNo));

            if(merchantMap.get(keywordBean) != null){
                merchantList = merchantMap.get(keywordBean).split("\\|");
                merchant = merchantService.findById(Long.parseLong(merchantList[0]));
                resultBean.put("merchantName", merchant == null ? "" : merchant.getName());
                resultBean.put("platformName", merchantList[1]);
                resultBean.put("advertiseActive", merchantList[2]);
            }
            resultBean.put("clickCount", clickCountMap.get(keywordBean) == null ? "0" : clickCountMap.get(keywordBean).toString());
            resultBean.put("reserveCount", reserveCountMap.get(keywordBean) == null ? "0" : reserveCountMap.get(keywordBean).toString());
            resultBean.put("underwriteCount", underwriteCountMap.get(keywordBean) == null ? "0" : underwriteCountMap.get(keywordBean).toString());
            resultBean.put("underwriteAmount", underwriteAmountMap.get(keywordBean) == null ? "0" : underwriteAmountMap.get(keywordBean).toString());
            resultBean.put("transformCost", transformCostResultMap.get(keywordBean) == null ? "0" : transformCostResultMap.get(keywordBean));
            resultBean.put("clickCost", clickCostResultMap.get(keywordBean) == null ? "0" : clickCostResultMap.get(keywordBean));
            resultBean.put("transformCycle", transformCycleMap.get(keywordBean) == null ? "0" : transformCycleMap.get(keywordBean));
            resultBean.put("transformRate", transformRateMap.get(keywordBean) == null ? "0" : transformRateMap.get(keywordBean));
            resultList.add(resultBean);
        }
        /** 汇总结果 **/
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultList);
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
