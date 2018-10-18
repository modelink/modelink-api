package com.modelink.admin.controller.dashboard;

import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.vo.dashboard.DashboardMediaParamVo;
import com.modelink.admin.vo.dashboard.DashboardParamVo;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.*;
import com.modelink.reservation.enums.FeeTypeEnum;
import com.modelink.reservation.service.*;
import com.modelink.reservation.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping("/admin/dashboard/media")
public class DashboardMediaController {

    public static Logger logger = LoggerFactory.getLogger(DashboardMediaController.class);

    @Resource
    private MediaItemService mediaItemService;
    @Resource
    private UnderwriteService underwriteService;
    @Resource
    private FlowReserveService flowReserveService;
    @Resource
    private MediaTacticsService mediaTacticsService;

    @RequestMapping
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/dashboard/media");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/getMediaSummary")
    public ResultVo getMediaSummary(DashboardMediaParamVo paramVo){
        String key;
        long startTime, endTime;
        Set<String> keySet = new HashSet<>();
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");


        startTime = System.currentTimeMillis();
        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("date,merchantId,platformName,advertiseActive,advertiseDesc");
        paramPagerVo.setFeeType(paramVo.getFeeType());
        paramPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);
        endTime = System.currentTimeMillis();
        logger.info("[dashboardMediaController|getMediaSummary]查询 flowReserveList runtime = {}", (endTime - startTime));
        startTime = endTime;
        int reserveTotalCount = flowReserveList.size();
        int reserveCount;
        Map<String, Integer> reserveMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            reserveCount = 0;
            key = (StringUtils.hasText(flowReserve.getPlatformName()) ? flowReserve.getPlatformName() : "-") + "|"
                    + (StringUtils.hasText(flowReserve.getAdvertiseActive()) ? flowReserve.getAdvertiseActive() : "-");
            keySet.add(key);
            if (reserveMap.get(key) != null) {
                reserveCount = reserveMap.get(key);
            }
            reserveCount ++;
            reserveMap.put(key, reserveCount);
        }
        endTime = System.currentTimeMillis();
        logger.info("[dashboardMediaController|getMediaSummary]查询 reserveMap runtime = {}", (endTime - startTime));
        startTime = endTime;
        /** 转化周期计算 **/
        UnderwriteParamPagerVo underwriteParamPagerVo = new UnderwriteParamPagerVo();
        underwriteParamPagerVo.setChooseDate(paramVo.getChooseDate());
        underwriteParamPagerVo.setMerchantId(paramVo.getMerchantId());
        underwriteParamPagerVo.setColumnFieldIds("id,merchantId,platformName,advertiseActive,reserveDate,finishDate,keyword");
        underwriteParamPagerVo.setPlatformName(paramVo.getPlatformName());
        underwriteParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        underwriteParamPagerVo.setDateField("reserveDate");
        if (StringUtils.hasText(paramVo.getFeeType())) {
            if (FeeTypeEnum.FEE_TYPE_RESERVE.getText().equals(paramVo.getFeeType())) {
                underwriteParamPagerVo.setSource("!产品测保");
            } else {
                underwriteParamPagerVo.setSource("产品测保");
            }
        }
        List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);
        endTime = System.currentTimeMillis();
        logger.info("[dashboardMediaController|getMediaSummary]查询 underwriteList runtime = {}", (endTime - startTime));
        startTime = endTime;
        int underwriteCount;
        String finishDate, reserveDate;
        int difference, totalDifference = 0;
        Map<String, Integer> differenceMap = new HashMap<>();
        Map<String, Integer> underwriteCountMap = new HashMap<>();
        for(Underwrite underwrite : underwriteList){
            finishDate = underwrite.getFinishDate();
            reserveDate = underwrite.getReserveDate();
            key = (StringUtils.hasText(underwrite.getPlatformName()) ? underwrite.getPlatformName() : "-") + "|"
                    + (StringUtils.hasText(underwrite.getAdvertiseActive()) ? underwrite.getAdvertiseActive() : "-");

            difference = 0;
            if (differenceMap.get(key) != null) {
                difference = differenceMap.get(key);
            }
            difference += DateUtils.getDateDifference(reserveDate, finishDate);
            totalDifference += DateUtils.getDateDifference(reserveDate, finishDate);;
            differenceMap.put(key, difference);

            underwriteCount = 0;
            if (underwriteCountMap.get(key) != null) {
                underwriteCount = underwriteCountMap.get(key);
            }
            underwriteCount ++;
            underwriteCountMap.put(key, underwriteCount);
        }
        /** 转化周期计算 **/
        endTime = System.currentTimeMillis();
        logger.info("[dashboardMediaController|getMediaSummary]查询 underwriteCountMap runtime = {}", (endTime - startTime));
        startTime = endTime;

        /** 计算转化成本与点击成本 **/
        MediaItemParamPagerVo mediaItemParamPagerVo = new MediaItemParamPagerVo();
        mediaItemParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaItemParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaItemParamPagerVo.setPlatformName(paramVo.getPlatformName());
        mediaItemParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        mediaItemParamPagerVo.setColumnFieldIds("date,merchantId,platformName,advertiseActive,keyWord,clickCount,speedCost");
        mediaItemParamPagerVo.setFeeType(paramVo.getFeeType());
        mediaItemParamPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);
        endTime = System.currentTimeMillis();
        logger.info("[dashboardMediaController|getMediaSummary]查询 mediaItemList runtime = {}", (endTime - startTime));
        startTime = endTime;
        int clickCount, showCount;
        double consumeAmount;
        int clickTotalCount = 0;
        int showTotalCount = 0;
        double consumeTotalAmount = 0.0d;
        Map<String, Integer> showCountMap = new HashMap<>();
        Map<String, Integer> clickCountMap = new HashMap<>();
        Map<String, Double> consumeAmountMap = new HashMap<>();
        for (MediaItem mediaItem : mediaItemList) {
            key = (StringUtils.hasText(mediaItem.getPlatformName()) ? mediaItem.getPlatformName() : "-") + "|"
                    + (StringUtils.hasText(mediaItem.getAdvertiseActive()) ? mediaItem.getAdvertiseActive() : "-");
            showCount = 0;
            if (showCountMap.get(key) != null) {
                showCount = showCountMap.get(key);
            }
            if (mediaItem.getShowCount() != null) {
                showCount += mediaItem.getShowCount();
                showTotalCount += mediaItem.getShowCount();
            }
            showCountMap.put(key, showCount);

            clickCount = 0;
            if (clickCountMap.get(key) != null) {
                clickCount = clickCountMap.get(key);
            }
            if (mediaItem.getClickCount() != null) {
                clickCount += mediaItem.getClickCount();
                clickTotalCount += mediaItem.getClickCount();
            }
            clickCountMap.put(key, clickCount);

            consumeAmount = 0.00d;
            if (consumeAmountMap.get(key) != null) {
                consumeAmount = consumeAmountMap.get(key);
            }
            if(StringUtils.hasText(mediaItem.getSpeedCost()) && !"-".equals(mediaItem.getSpeedCost())) {
                consumeAmount += Double.valueOf(mediaItem.getSpeedCost());
                consumeTotalAmount += Double.valueOf(mediaItem.getSpeedCost());
            }
            consumeAmountMap.put(key, consumeAmount);
        }
        endTime = System.currentTimeMillis();
        logger.info("[dashboardMediaController|getMediaSummary]查询 consumeAmountMap runtime = {}", (endTime - startTime));
        startTime = endTime;
        int underwriteTotalCount = underwriteList.size();
        double transformTotalCycle = 0.00d;
        double transformTotalCost = 0.00d;
        String underwriteTotalRate = "0";
        String transformTotalRate = "0";
        String reserveTotalRate = "0";
        String clickTotalRate = "0";
        if(reserveTotalCount > 0) {
            transformTotalCost = consumeTotalAmount / reserveTotalCount;
            underwriteTotalRate = decimalFormat.format(underwriteTotalCount * 100.0d / reserveTotalCount);
        }
        if(clickTotalCount > 0) {
            transformTotalRate = decimalFormat.format(underwriteTotalCount * 100.00d / clickTotalCount);
            reserveTotalRate = decimalFormat.format(reserveTotalCount * 100.0d / clickTotalCount);
        }
        if(showTotalCount > 0) {
            clickTotalRate = decimalFormat.format(clickTotalCount * 100.0d / showTotalCount);
        }
        if(underwriteTotalCount > 0) {
            transformTotalCycle = totalDifference / underwriteTotalCount;
        }
        String[] keyArray;
        JSONObject tableItem;
        List<JSONObject> tableItemList = new ArrayList<>();
        for (String keyword : keySet) {
            tableItem = new JSONObject();
            keyArray = keyword.split("\\|");
            tableItem.put("platformName", keyArray[0]);
            tableItem.put("advertiseActive", keyArray[1]);

            reserveCount = reserveMap.get(keyword) == null ? 0 : reserveMap.get(keyword);
            underwriteCount = underwriteCountMap.get(keyword) == null ? 0 : underwriteCountMap.get(keyword);
            consumeAmount = consumeAmountMap.get(keyword) == null ? 0.00d : consumeAmountMap.get(keyword);
            difference = differenceMap.get(keyword) == null ? 0 : differenceMap.get(keyword);
            clickCount = clickCountMap.get(keyword) == null ? 0 : clickCountMap.get(keyword);
            showCount = showCountMap.get(keyword) == null ? 0 : showCountMap.get(keyword);
            if (reserveCount != 0) {
                tableItem.put("transformCost", decimalFormat.format(consumeAmount / reserveCount));
                tableItem.put("underwriteRate", decimalFormat.format(underwriteCount * 100.0d / reserveCount));
            } else {
                tableItem.put("transformCost", "0.00");
                tableItem.put("underwriteRate", "0.00");
            }
            if (underwriteCount != 0) {
                tableItem.put("transformCycle", decimalFormat.format(difference / underwriteCount));
            } else {
                tableItem.put("transformCycle", "0.00");
            }
            if (showCount != 0) {
                tableItem.put("clickRate", decimalFormat.format(clickCount * 100.0d / showCount));
            } else {
                tableItem.put("clickRate", "0.00");
            }
            if (clickCount != 0) {
                tableItem.put("reserveRate", decimalFormat.format(reserveCount * 100.0d / clickCount));
                tableItem.put("transformRate", decimalFormat.format(underwriteCount * 100.0d / clickCount));
            } else {
                tableItem.put("reserveRate", "0.00");
                tableItem.put("transformRate", "0.00");
            }
            tableItemList.add(tableItem);
        }

        // 数据转化
        if(transformTotalCycle > 90){
            transformTotalCycle = 5;
        }else if(transformTotalCycle > 60){
            transformTotalCycle = 10;
        }else if(transformTotalCycle > 30){
            transformTotalCycle = 20;
        }else if(transformTotalCycle > 14){
            transformTotalCycle = 30;
        }else if(transformTotalCycle > 6){
            transformTotalCycle = 40;
        }else if(transformTotalCycle > 5){
            transformTotalCycle = 50;
        }else if(transformTotalCycle > 4){
            transformTotalCycle = 60;
        }else if(transformTotalCycle > 3){
            transformTotalCycle = 70;
        }else if(transformTotalCycle > 2){
            transformTotalCycle = 80;
        }else if(transformTotalCycle > 1){
            transformTotalCycle = 90;
        }else if(transformTotalCycle > 0){
            transformTotalCycle = 98;
        }
        if(transformTotalCost > 5000){
            transformTotalCost = 10;
        }else if(transformTotalCost > 2000){
            transformTotalCost = 20;
        }else if(transformTotalCost > 800){
            transformTotalCost = 30;
        }else if(transformTotalCost > 500){
            transformTotalCost = 40;
        }else if(transformTotalCost > 300){
            transformTotalCost = 50;
        }else if(transformTotalCost > 250){
            transformTotalCost = 60;
        }else if(transformTotalCost > 200){
            transformTotalCost = 70;
        }else if(transformTotalCost > 150){
            transformTotalCost = 80;
        }else if(transformTotalCost > 100){
            transformTotalCost = 90;
        }else if(transformTotalCost > 0){
            transformTotalCost = 98;
        }
        endTime = System.currentTimeMillis();
        logger.info("[dashboardMediaController|getMediaSummary]查询 resultJson runtime = {}", (endTime - startTime));
        List<String> contentList = new ArrayList<>();
        contentList.add(clickTotalRate);
        contentList.add(reserveTotalRate);
        contentList.add(underwriteTotalRate);
        contentList.add(String.valueOf(transformTotalCycle));
        contentList.add(String.valueOf(transformTotalCost));
        contentList.add(transformTotalRate);
        /** 汇总结果 **/
        JSONObject resultJson = new JSONObject();
        resultJson.put("contentList", contentList);
        resultJson.put("tableItemList", tableItemList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }


    @ResponseBody
    @RequestMapping("/getMediaTactics")
    public ResultVo getMediaTactics(DashboardMediaParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        UnderwriteParamPagerVo underwriteParamPagerVo = new UnderwriteParamPagerVo();
        underwriteParamPagerVo.setChooseDate(paramVo.getChooseDate());
        underwriteParamPagerVo.setMerchantId(paramVo.getMerchantId());
        underwriteParamPagerVo.setColumnFieldIds("id,merchantId,platformName,advertiseActive,reserveDate,insuranceFee");
        underwriteParamPagerVo.setPlatformName(paramVo.getPlatformName());
        underwriteParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        underwriteParamPagerVo.setDateField("reserveDate");
        if (StringUtils.hasText(paramVo.getFeeType())) {
            if (FeeTypeEnum.FEE_TYPE_RESERVE.getText().equals(paramVo.getFeeType())) {
                underwriteParamPagerVo.setSource("!产品测保");
            } else {
                underwriteParamPagerVo.setSource("产品测保");
            }
        }
        List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);
        if(underwriteList.size() <= 0){
            return resultVo;
        }

        String dateKey;
        Double underwriteAmount;
        Map<String, Double> underwriteAmountMap = initYearResultMap();
        for (Underwrite underwrite : underwriteList) {
            dateKey = getDateKeyByDateType(underwrite.getReserveDate());
            underwriteAmount = 0.00d;
            if(underwriteAmountMap.get(dateKey) != null){
                underwriteAmount = underwriteAmountMap.get(dateKey);
            }
            if(StringUtils.hasText(underwrite.getInsuranceFee()) && !"-".equals(underwrite.getInsuranceFee())) {
                underwriteAmount += Double.valueOf(underwrite.getInsuranceFee());
            }
            underwriteAmountMap.put(dateKey, underwriteAmount);
        }

        MediaTacticsParamPagerVo mediaTacticsParamPagerVo = new MediaTacticsParamPagerVo();
        mediaTacticsParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaTacticsParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaTacticsParamPagerVo.setColumnFieldIds("id,month,operateCount,optimizeKeyWord," +
                "optimizeWordIdea,optimizeImageIdea,optimizeFlowIdea,optimizeFlowPeople");
        mediaTacticsParamPagerVo.setPlatformName(paramVo.getPlatformName());
        mediaTacticsParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        mediaTacticsParamPagerVo.setDateField("month");
        List<MediaTactics> mediaTacticsList = mediaTacticsService.findListByParam(mediaTacticsParamPagerVo);

        Integer[] tacticsCount;
        Map<String, Integer[]> tacticsCountMap = new HashMap<>();
        for (MediaTactics mediaTactics : mediaTacticsList) {
            dateKey = DateUtils.dateFormatTransform(mediaTactics.getMonth(), "yyyy-MM", "MM");
            tacticsCount = tacticsCountMap.get(dateKey);
            if(tacticsCount == null){
                tacticsCount = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            }
            tacticsCount[0] += mediaTactics.getOperateCount();
            tacticsCount[1] += mediaTactics.getOptimizeKeyWord();
            tacticsCount[2] += mediaTactics.getAddBid();
            tacticsCount[3] += mediaTactics.getReduceBid();
            tacticsCount[4] += mediaTactics.getAddPatten();
            tacticsCount[5] += mediaTactics.getReducePatten();
            tacticsCount[6] += mediaTactics.getAddKeyWord();
            tacticsCount[7] += mediaTactics.getReduceKeyWord();
            tacticsCount[8] += mediaTactics.getFilteKeyWord();

            tacticsCount[9] += mediaTactics.getOptimizeWordIdea();
            tacticsCount[10] += mediaTactics.getAddStyle();
            tacticsCount[11] += mediaTactics.getAddWordIdea();

            tacticsCount[12] += mediaTactics.getOptimizeImageIdea();
            tacticsCount[13] += mediaTactics.getAddImageIdea();
            tacticsCount[14] += mediaTactics.getReduceImageIdea();
            tacticsCount[15] += mediaTactics.getModifyImageBid();

            tacticsCount[16] += mediaTactics.getOptimizeFlowIdea();
            tacticsCount[17] += mediaTactics.getModifyCopywrite();

            tacticsCount[18] += mediaTactics.getOptimizeFlowPeople();
            tacticsCount[19] += mediaTactics.getModifyKeyWord();

            tacticsCountMap.put(dateKey, tacticsCount);
        }

        Set<String> keySet = underwriteAmountMap.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        List<String> underwriteAmountList = new ArrayList<>(12);
        List<Integer> operateCountList = new ArrayList<>(12);

        List<Integer> keywordCountList = new ArrayList<>(12);
        List<Integer> addBidCountList = new ArrayList<>(12);
        List<Integer> reduceBidCountList = new ArrayList<>(12);
        List<Integer> addPattenCountList = new ArrayList<>(12);
        List<Integer> reducePattenCountList = new ArrayList<>(12);
        List<Integer> addKeyWordCountList = new ArrayList<>(12);
        List<Integer> reduceKeyWordCountList = new ArrayList<>(12);
        List<Integer> filteKeyWordCountList = new ArrayList<>(12);

        List<Integer> wordIdeaCountList = new ArrayList<>(12);
        List<Integer> addStyleCountList = new ArrayList<>(12);
        List<Integer> addWordIdeaCountList = new ArrayList<>(12);

        List<Integer> imageIdeaCountList = new ArrayList<>(12);
        List<Integer> addImageIdeaCountList = new ArrayList<>(12);
        List<Integer> reduceImageIdeaCountList = new ArrayList<>(12);
        List<Integer> modifyImageBidCountList = new ArrayList<>(12);

        List<Integer> flowIdeaCountList = new ArrayList<>(12);
        List<Integer> modifyCopywriteCountList = new ArrayList<>(12);

        List<Integer> flowPeopleCountList = new ArrayList<>(12);
        List<Integer> modifyKeyWordCountList = new ArrayList<>(12);


        for (String key : keyArray) {
            underwriteAmount = underwriteAmountMap.get(key);
            if(underwriteAmount == null){
                underwriteAmount = 0.00d;
            }
            underwriteAmountList.add(decimalFormat.format(underwriteAmount));

            tacticsCount = tacticsCountMap.get(key);
            if(tacticsCount == null){
                tacticsCount = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            }

            operateCountList.add(tacticsCount[0]);

            keywordCountList.add(tacticsCount[1]);
            addBidCountList.add(tacticsCount[2]);
            reduceBidCountList.add(tacticsCount[3]);
            addPattenCountList.add(tacticsCount[4]);
            reducePattenCountList.add(tacticsCount[5]);
            addKeyWordCountList.add(tacticsCount[6]);
            reduceKeyWordCountList.add(tacticsCount[7]);
            filteKeyWordCountList.add(tacticsCount[8]);

            wordIdeaCountList.add(tacticsCount[9]);
            addStyleCountList.add(tacticsCount[10]);
            addWordIdeaCountList.add(tacticsCount[11]);

            imageIdeaCountList.add(tacticsCount[12]);
            addImageIdeaCountList.add(tacticsCount[13]);
            reduceImageIdeaCountList.add(tacticsCount[14]);
            modifyImageBidCountList.add(tacticsCount[15]);

            flowIdeaCountList.add(tacticsCount[16]);
            modifyCopywriteCountList.add(tacticsCount[17]);

            flowPeopleCountList.add(tacticsCount[18]);
            modifyKeyWordCountList.add(tacticsCount[19]);
        }

        int index;
        JSONObject tableItem;
        List<JSONObject> tableItemList = new ArrayList<>();

        // 组成表格数据
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", false);
        tableItem.put("operate", "操作数（次）");
        for(Integer count : operateCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", true);
        tableItem.put("operate", "保费（元）");
        for(String count : underwriteAmountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", true);
        tableItem.put("operate", "关键词优化（次）");
        for(Integer count : operateCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", false);
        tableItem.put("operate", "增加出价（次）");
        for(Integer count : addBidCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", false);
        tableItem.put("operate", "降低出价（次）");
        for(Integer count : reduceBidCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", false);
        tableItem.put("operate", "调宽匹配模式（次）");
        for(Integer count : addPattenCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", false);
        tableItem.put("operate", "调窄匹配模式（次）");
        for(Integer count : reducePattenCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", false);
        tableItem.put("operate", "增加关键词（次）");
        for(Integer count : addKeyWordCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", false);
        tableItem.put("operate", "删除关键词（次）");
        for(Integer count : reduceKeyWordCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", false);
        tableItem.put("operate", "搜索词过滤（次）");
        for(Integer count : filteKeyWordCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", true);
        tableItem.put("operate", "文字创意优化（次）");
        for(Integer count : wordIdeaCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", false);
        tableItem.put("operate", "增加图片等高级样式（次）");
        for(Integer count : addStyleCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", false);
        tableItem.put("operate", "增加文字创意（次）");
        for(Integer count : addWordIdeaCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", true);
        tableItem.put("operate", "展示类图片创意优化（次）");
        for(Integer count : imageIdeaCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", false);
        tableItem.put("operate", "增加图片创意（次）");
        for(Integer count : addImageIdeaCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", false);
        tableItem.put("operate", "删除图片创意（次）");
        for(Integer count : reduceImageIdeaCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", false);
        tableItem.put("operate", "调整图片出价（次）");
        for(Integer count : modifyImageBidCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", true);
        tableItem.put("operate", "信息流文字创意优化（次）");
        for(Integer count : flowIdeaCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", false);
        tableItem.put("operate", "文案调整（次）");
        for(Integer count : modifyCopywriteCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", true);
        tableItem.put("operate", "信息流人群优化（次）");
        for(Integer count : flowPeopleCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);
        index = 1;
        tableItem = new JSONObject();
        tableItem.put("keyword", false);
        tableItem.put("operate", "修改定向关键词（次）");
        for(Integer count : modifyKeyWordCountList){
            tableItem.put("month" + index, count);
            index ++;
        }
        tableItemList.add(tableItem);


        JSONObject resultJson = new JSONObject();
        /** 汇总结果 **/
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultJson.put("tableItemList", tableItemList);
        resultJson.put("monthList", keyArray);
        resultJson.put("operateCountList", operateCountList);

        resultJson.put("keywordCountList", keywordCountList);
        resultJson.put("addBidCountList", addBidCountList);
        resultJson.put("reduceBidCountList", reduceBidCountList);
        resultJson.put("addPattenCountList", addPattenCountList);
        resultJson.put("reducePattenCountList", reducePattenCountList);
        resultJson.put("addKeyWordCountList", addKeyWordCountList);
        resultJson.put("reduceKeyWordCountList", reduceKeyWordCountList);
        resultJson.put("filteKeyWordCountList", filteKeyWordCountList);

        resultJson.put("wordIdeaCountList", wordIdeaCountList);
        resultJson.put("addStyleCountList", addStyleCountList);
        resultJson.put("addWordIdeaCountList", addWordIdeaCountList);

        resultJson.put("imageIdeaCountList", imageIdeaCountList);
        resultJson.put("addImageIdeaCountList", addImageIdeaCountList);
        resultJson.put("reduceImageIdeaCountList", reduceImageIdeaCountList);
        resultJson.put("modifyImageBidCountList", modifyImageBidCountList);

        resultJson.put("flowIdeaCountList", flowIdeaCountList);
        resultJson.put("modifyCopywriteCountList", modifyCopywriteCountList);

        resultJson.put("flowPeopleCountList", flowPeopleCountList);
        resultJson.put("modifyKeyWordCountList", modifyKeyWordCountList);

        resultJson.put("underwriteAmountList", underwriteAmountList);

        resultVo.setRtnData(resultJson);
        return resultVo;
    }



    private String getDateKeyByDateType(String dateString){
        Date date;
        String resultDate;
        String format = "yyyyMMdd";
        try {
            format = "MM";
            date = DateUtils.formatDate(dateString, "yyyy-MM-dd");
            resultDate = DateUtils.formatDate(date, format);
        } catch (Exception e) {
            resultDate = "";
        }
        return resultDate;
    }
    private Map<String, Double> initYearResultMap() {
        Map<String, Double> rtnMap = new HashMap<>();
        rtnMap.put("01", 0.00d);
        rtnMap.put("02", 0.00d);
        rtnMap.put("03", 0.00d);
        rtnMap.put("04", 0.00d);
        rtnMap.put("05", 0.00d);
        rtnMap.put("06", 0.00d);
        rtnMap.put("07", 0.00d);
        rtnMap.put("08", 0.00d);
        rtnMap.put("09", 0.00d);
        rtnMap.put("10", 0.00d);
        rtnMap.put("11", 0.00d);
        rtnMap.put("12", 0.00d);
        return rtnMap;
    }

    private void initDashboardParam(DashboardParamVo dashboardParamVo){
        if(dashboardParamVo == null){
            dashboardParamVo = new DashboardParamVo();
        }
        if(StringUtils.isEmpty(dashboardParamVo.getChooseDate())){
            String endDate = DateUtils.formatDate(new Date(), "yyyy") + "-12-31";
            String startDate = DateUtils.formatDate(new Date(), "yyyy") + "-01-01";
            dashboardParamVo.setChooseDate(startDate + " - " + endDate);
        }else{
            String[] dateArray = dashboardParamVo.getChooseDate().split(" - ");
            String endDate = dateArray[1] + "-12-31";
            String startDate = dateArray[0] + "-01-01";
            dashboardParamVo.setChooseDate(startDate + " - " + endDate);
        }
        if(StringUtils.hasText(dashboardParamVo.getPlatformName())){
            dashboardParamVo.setPlatformName(dashboardParamVo.getPlatformName().toUpperCase());
        }
    }
}
