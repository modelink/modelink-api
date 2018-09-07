package com.modelink.admin.controller.dashboard;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.vo.DashboardParamVo;
import com.modelink.common.enums.DateTypeEnum;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.utils.DataUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.*;
import com.modelink.reservation.service.*;
import com.modelink.reservation.vo.*;
import com.modelink.usercenter.bean.Area;
import com.modelink.usercenter.bean.Merchant;
import com.modelink.usercenter.service.AreaService;
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
@RequestMapping("/admin/dashboard/media")
public class DashboardMediaController {

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
    public ResultVo getMediaSummary(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("date,merchantId,platformName,advertiseActive,advertiseDesc");
        paramPagerVo.setFeeType(FlowReserve.FEE_TYPE_RESERVE);
        paramPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);
        int reserveCount = flowReserveList.size();

        /** 转化周期计算 **/
        UnderwriteParamPagerVo underwriteParamPagerVo = new UnderwriteParamPagerVo();
        underwriteParamPagerVo.setChooseDate(paramVo.getChooseDate());
        underwriteParamPagerVo.setMerchantId(paramVo.getMerchantId());
        underwriteParamPagerVo.setColumnFieldIds("id,merchantId,platformName,advertiseActive,reserveDate,finishDate,keyword");
        underwriteParamPagerVo.setPlatformName(paramVo.getPlatformName());
        underwriteParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        underwriteParamPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);

        String finishDate, reserveDate;
        int difference, totalDifference = 0;
        for(Underwrite underwrite : underwriteList){
            finishDate = underwrite.getFinishDate();
            reserveDate = underwrite.getReserveDate();
            difference = DateUtils.getDateDifference(reserveDate, finishDate);
            totalDifference += difference;
        }

        /** 转化周期计算 **/


        /** 计算转化成本与点击成本 **/
        MediaItemParamPagerVo mediaItemParamPagerVo = new MediaItemParamPagerVo();
        mediaItemParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaItemParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaItemParamPagerVo.setPlatformName(paramVo.getPlatformName());
        mediaItemParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        mediaItemParamPagerVo.setColumnFieldIds("date,merchantId,platformName,advertiseActive,keyWord,clickCount,speedCost");
        mediaItemParamPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);

        int clickCount = 0;
        int showCount = 0;
        double consumeAmount = 0.0d;
        for (MediaItem mediaItem : mediaItemList) {
            if(mediaItem.getClickCount() != null) {
                clickCount += mediaItem.getClickCount();
            }
            if(mediaItem.getShowCount() != null) {
                showCount += mediaItem.getShowCount();
            }
            if(StringUtils.hasText(mediaItem.getSpeedCost()) && !"-".equals(mediaItem.getSpeedCost())) {
                consumeAmount += Double.valueOf(mediaItem.getSpeedCost());
            }
        }

        int underwriteCount = underwriteList.size();
        String transformCycle = "0";
        String transformCost = "0";
        String underwriteRate = "0";
        String transformRate = "0";
        String reserveRate = "0";
        String clickRate = "0";
        if(reserveCount > 0) {
            transformCost = decimalFormat.format(consumeAmount / reserveCount);
            underwriteRate = decimalFormat.format(underwriteCount * 100.0d / reserveCount);
        }
        if(clickCount > 0) {
            transformRate = decimalFormat.format(underwriteCount * 100.00d / clickCount);
            reserveRate = decimalFormat.format(reserveCount * 100.0d / clickCount);
            clickRate = decimalFormat.format(showCount * 100.0d / clickCount);
        }
        if(underwriteCount > 0) {
            transformCycle = decimalFormat.format(totalDifference / underwriteCount);
        }


        List<String> contentList = new ArrayList<>();
        contentList.add(clickRate);
        contentList.add(reserveRate);
        contentList.add(underwriteRate);
        contentList.add(transformCycle);
        contentList.add(transformCost);
        contentList.add(transformRate);
        /** 汇总结果 **/
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(contentList);
        return resultVo;
    }


    @ResponseBody
    @RequestMapping("/getMediaTactics")
    public ResultVo getMediaTactics(DashboardParamVo paramVo){
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
            String endDate = DateUtils.calculateDate(new Date(), Calendar.DAY_OF_YEAR, -1, "yyyy") + "-12-31";
            String startDate = DateUtils.calculateDate(new Date(), Calendar.DAY_OF_YEAR, -8, "yyyy") + "-01-01";
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
