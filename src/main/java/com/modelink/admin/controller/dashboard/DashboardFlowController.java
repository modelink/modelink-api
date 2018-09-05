package com.modelink.admin.controller.dashboard;

import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.vo.DashboardFlowParamVo;
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
@RequestMapping("/admin/dashboard/flow")
public class DashboardFlowController {

    @Resource
    private AreaService areaService;
    @Resource
    private FlowService flowService;
    @Resource
    private FlowAreaService flowAreaService;

    @RequestMapping
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/dashboard/flow");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/getFlowSummary")
    public ResultVo getFlowSummary(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowParamPagerVo paramPagerVo = new FlowParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setColumnFieldIds("id,date,browseCount,inflowCount,userCount," +
                "againClickCount,againClickRate,averageStayTime,averageBrowsePageCount");
        paramPagerVo.setDateField("date");
        List<Flow> flowList = flowService.findListByParam(paramPagerVo);

        String dateKey;
        int browseCount, userCount;
        int browseTotalCount = 0;
        int inflowTotalCount = 0;
        int userTotalCount = 0;
        int againTotalCount = 0;
        double againTotalRate = 0.0d;
        double averageStayTime = 0.0d;
        double averageBrowsePage = 0.0d;
        Map<String, Object> browseCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), DateTypeEnum.日.getValue(), "int");
        Map<String, Object> userCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), DateTypeEnum.日.getValue(), "int");
        for (Flow flow : flowList) {
            dateKey = DataUtils.getDateKeyByDateType(flow.getDate(), DateTypeEnum.日.getValue());

            browseCount = (int)browseCountMap.get(dateKey);
            if(flow.getBrowseCount() != null){
                browseCount += flow.getBrowseCount();
            }
            browseCountMap.put(dateKey, browseCount);

            userCount = (int)userCountMap.get(dateKey);
            if(flow.getUserCount() != null){
                userCount += flow.getUserCount();
            }
            userCountMap.put(dateKey, userCount);

            String[] stayTimeList;
            if (flow.getBrowseCount() != null) {
                browseTotalCount += flow.getBrowseCount();
            }
            if (flow.getInflowCount() != null) {
                inflowTotalCount += flow.getInflowCount();
            }
            if (flow.getUserCount() != null) {
                userTotalCount += flow.getUserCount();
            }
            if(flow.getAgainClickCount() != null) {
                againTotalCount += flow.getAgainClickCount();
            }
            if(flow.getAgainClickRate() != null && !"-".equals(flow.getAgainClickRate())) {
                againTotalRate += Double.parseDouble(flow.getAgainClickRate());
            }
            if(flow.getAverageStayTime() != null && !"-".equals(flow.getAverageStayTime())) {
                if(">1h".equals(flow.getAverageStayTime())){
                    averageStayTime += 3600;
                }else{
                    stayTimeList = flow.getAverageStayTime().split("\\:");
                    averageStayTime += (Integer.parseInt(stayTimeList[0]) * 60 + Integer.parseInt(stayTimeList[1]));
                }
            }
            if(flow.getAverageBrowsePageCount() != null && !"-".equals(flow.getAverageBrowsePageCount())) {
                averageBrowsePage += Double.parseDouble(flow.getAverageBrowsePageCount());
            }
        }

        Set<String> keySet = browseCountMap.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        List<String> titleList = new ArrayList<>();
        List<Integer> browseCountList = new ArrayList<>();
        List<Integer> userCountList = new ArrayList<>();
        for (String date : keyArray) {
            titleList.add(date);
            browseCountList.add((int)browseCountMap.get(date));
            userCountList.add((int)userCountMap.get(date));
        }

        /** 汇总结果 **/
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        JSONObject flowSummary = new JSONObject();
        flowSummary.put("browseTotalCount", browseTotalCount);
        flowSummary.put("inflowTotalCount", inflowTotalCount);
        flowSummary.put("userTotalCount", userTotalCount);
        flowSummary.put("againTotalCount", againTotalCount);
        if(flowList.size() > 0) {
            flowSummary.put("againTotalRate", decimalFormat.format(againTotalRate / flowList.size()));
            flowSummary.put("averageStayTime", decimalFormat.format(averageStayTime / flowList.size()));
            flowSummary.put("averageBrowsePage", decimalFormat.format(averageBrowsePage / flowList.size()));
        }else{
            flowSummary.put("againTotalRate", "0.00");
            flowSummary.put("averageStayTime", "0.00");
            flowSummary.put("averageBrowsePage", "0.00");
        }
        JSONObject resultJson = new JSONObject();
        resultJson.put("flowSummary", flowSummary);
        resultJson.put("titleList", titleList);
        resultJson.put("userCountList", userCountList);
        resultJson.put("browseCountList", browseCountList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getFlowUser")
    public ResultVo getFlowUser(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowAreaParamPagerVo paramPagerVo = new FlowAreaParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setColumnFieldIds("id,provinceId,userCount");
        paramPagerVo.setDateField("date");
        List<FlowArea> flowAreaList = flowAreaService.findListByParam(paramPagerVo);

        int userCount;
        int provinceId;
        int totalCount = 0;
        List<Integer> provinceIdList = new ArrayList<>();
        Map<Integer, Integer> userCountMap = new HashMap<>();
        for (FlowArea flowArea : flowAreaList) {
            provinceId = flowArea.getProvinceId();

            userCount = 0;
            provinceIdList.add(provinceId);
            if(userCountMap.get(provinceId) != null) {
                userCount = userCountMap.get(provinceId);
            }
            if(flowArea.getUserCount() != null){
                userCount += flowArea.getUserCount();
                totalCount += flowArea.getUserCount();
            }
            userCountMap.put(provinceId, userCount);
        }

        List<Map.Entry<Integer, Integer>> entryList = new ArrayList(userCountMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<Integer, String> areaNameMap = new HashMap<>();
        areaNameMap.put(0, "未知地区");
        List<Area> areaList = areaService.findByIdList(provinceIdList);
        for (Area area : areaList) {
            areaNameMap.put(area.getAreaId(), area.getAreaName());
        }

        JSONObject tableItem;
        List<String> titleList = new ArrayList<>();
        List<Integer> contentList = new ArrayList<>();
        List<JSONObject> tableItemList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        int index = 1;
        int otherCount = totalCount;
        for (Map.Entry<Integer, Integer> entry : entryList) {
            if(index > 6){
                break;
            }
            titleList.add(areaNameMap.get(entry.getKey()));
            contentList.add(entry.getValue());
            tableItem = new JSONObject();
            tableItem.put("name", areaNameMap.get(entry.getKey()));
            tableItem.put("value", entry.getValue());
            tableItem.put("proportion", decimalFormat.format(entry.getValue() * 100.0d / totalCount) + "%");
            tableItemList.add(tableItem);
            otherCount -= entry.getValue();
            index ++;
        }
        tableItem = new JSONObject();
        tableItem.put("name", "其他");
        tableItem.put("value", otherCount);
        tableItem.put("proportion", decimalFormat.format(otherCount * 100.0d / totalCount) + "%");
        tableItemList.add(tableItem);


        /** 汇总结果 **/
        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("contentList", contentList);
        resultJson.put("tableItemList", tableItemList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getFlowSource")
    public ResultVo getFlowSource(DashboardFlowParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowParamPagerVo paramPagerVo = new FlowParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setColumnFieldIds("id,website,inflowCount,againClickRate,averageStayTime");
        paramPagerVo.setDateField("date");
        List<Flow> flowList = flowService.findListByParam(paramPagerVo);

        int inflowCount, websiteCount;
        double againRateAmount, stayTimeAmount;
        String[] stayTimeList;
        String website;

        Map<String, Integer> inflowCountMap = new HashMap<>();
        Map<String, Integer> websiteCountMap = new HashMap<>();
        Map<String, Double> againRateAmountMap = new HashMap<>();
        Map<String, Double> stayTimeAmountMap = new HashMap<>();
        for (Flow flow : flowList) {
            website = flow.getWebsite();

            inflowCount = 0;
            if(inflowCountMap.get(website) != null) {
                inflowCount = inflowCountMap.get(website);
            }
            if(flow.getInflowCount() != null){
                inflowCount += flow.getInflowCount();
            }
            inflowCountMap.put(website, inflowCount);

            websiteCount = 0;
            if(websiteCountMap.get(website) != null) {
                websiteCount = websiteCountMap.get(website);
            }
            websiteCount ++;
            websiteCountMap.put(website, websiteCount);

            againRateAmount = 0.0d;
            if(againRateAmountMap.get(website) != null) {
                againRateAmount = againRateAmountMap.get(website);
            }
            if(flow.getInflowCount() != null){
                againRateAmount += Double.parseDouble(flow.getAgainClickRate());
            }
            againRateAmountMap.put(website, againRateAmount);

            stayTimeAmount = 0.0d;
            if(stayTimeAmountMap.get(website) != null) {
                stayTimeAmount = stayTimeAmountMap.get(website);
            }
            if(flow.getAverageStayTime() != null){
                if(">1h".equals(flow.getAverageStayTime())){
                    stayTimeAmount += 3600;
                }else{
                    stayTimeList = flow.getAverageStayTime().split("\\:");
                    stayTimeAmount += (Integer.parseInt(stayTimeList[0]) * 60 + Integer.parseInt(stayTimeList[1]));
                }
            }
            stayTimeAmountMap.put(website, stayTimeAmount);
        }

        JSONObject tableItem;
        List<JSONObject> tableItemList = new ArrayList<>();
        Set<String> keySet = inflowCountMap.keySet();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        for (String key : keySet) {
            tableItem = new JSONObject();
            tableItem.put("website", key);
            tableItem.put("inflowCount", inflowCountMap.get(key));
            tableItem.put("againRate", decimalFormat.format(againRateAmountMap.get(key) / websiteCountMap.get(key)) + "%");
            tableItem.put("stayTime", decimalFormat.format(stayTimeAmountMap.get(key) / websiteCountMap.get(key)));
            tableItemList.add(tableItem);
        }

        /** 汇总结果 **/
        JSONObject resultJson = new JSONObject();
        resultJson.put("tableItemList", tableItemList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getInflowSource")
    public ResultVo getInflowSource(DashboardFlowParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowAreaParamPagerVo paramPagerVo = new FlowAreaParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setColumnFieldIds("id,source,userCount");
        paramPagerVo.setDateField("date");
        List<FlowArea> flowAreaList = flowAreaService.findListByParam(paramPagerVo);

        int userCount;
        String source;
        int totalCount = 0;
        Map<String, Integer> userCountMap = new HashMap<>();
        for (FlowArea flowArea : flowAreaList) {
            source = flowArea.getSource();

            userCount = 0;
            if(userCountMap.get(source) != null) {
                userCount = userCountMap.get(source);
            }
            if(flowArea.getUserCount() != null){
                userCount += flowArea.getUserCount();
                totalCount += flowArea.getUserCount();
            }
            userCountMap.put(source, userCount);
        }

        JSONObject tableItem;
        List<String> titleList = new ArrayList<>();
        List<Integer> contentList = new ArrayList<>();
        List<JSONObject> tableItemList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        Set<String> sourceSet = userCountMap.keySet();
        for (String sourceKey : sourceSet) {
            tableItem = new JSONObject();
            tableItem.put("name", sourceKey);
            tableItem.put("value", userCountMap.get(sourceKey));
            tableItem.put("proportion", decimalFormat.format(userCountMap.get(sourceKey) * 100.0d / totalCount) + "%");
            tableItemList.add(tableItem);
        }

        /** 汇总结果 **/
        JSONObject resultJson = new JSONObject();
        resultJson.put("tableItemList", tableItemList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getInflowSourceItem")
    public ResultVo getInflowSourceItem(DashboardFlowParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowAreaParamPagerVo paramPagerVo = new FlowAreaParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setColumnFieldIds("id,provinceId,inflowCount,browseCount,userCount,averageStayTime,againClickRate");
        paramPagerVo.setSource(paramVo.getSource());
        paramPagerVo.setDateField("date");
        List<FlowArea> flowAreaList = flowAreaService.findListByParam(paramPagerVo);


        int provinceId;
        String[] stayTimeList;
        int count;
        String againClickRate;
        int userCount, browseCount, inflowCount;
        double stayTimeAmount, againRateAmount;
        List<Integer> provinceIdList = new ArrayList<>();
        Map<Integer, Integer> countMap = new HashMap<>();
        Map<Integer, Integer> userCountMap = new HashMap<>();
        Map<Integer, Integer> browseCountMap = new HashMap<>();
        Map<Integer, Integer> inflowCountMap = new HashMap<>();
        Map<Integer, Double> stayTimeAmountMap = new HashMap<>();
        Map<Integer, Double> againRateAmountMap = new HashMap<>();
        for (FlowArea flowArea : flowAreaList) {
            provinceId = flowArea.getProvinceId();
            provinceIdList.add(provinceId);

            count = 0;
            if(countMap.get(provinceId) != null) {
                count = countMap.get(provinceId);
            }
            count ++;
            countMap.put(provinceId, count);

            userCount = 0;
            if(userCountMap.get(provinceId) != null) {
                userCount = userCountMap.get(provinceId);
            }
            if(flowArea.getUserCount() != null){
                userCount += flowArea.getUserCount();
            }
            userCountMap.put(provinceId, userCount);

            browseCount = 0;
            if(browseCountMap.get(provinceId) != null) {
                browseCount = browseCountMap.get(provinceId);
            }
            if(flowArea.getBrowseCount() != null){
                browseCount += flowArea.getBrowseCount();
            }
            browseCountMap.put(provinceId, browseCount);

            inflowCount = 0;
            if(inflowCountMap.get(provinceId) != null) {
                inflowCount = inflowCountMap.get(provinceId);
            }
            if(flowArea.getInflowCount() != null){
                inflowCount += flowArea.getInflowCount();
            }
            inflowCountMap.put(provinceId, inflowCount);

            stayTimeAmount = 0.0d;
            if(stayTimeAmountMap.get(provinceId) != null) {
                stayTimeAmount = stayTimeAmountMap.get(provinceId);
            }
            if(flowArea.getAverageStayTime() != null){
                if(">1h".equals(flowArea.getAverageStayTime())){
                    stayTimeAmount += 3600;
                }else{
                    stayTimeList = flowArea.getAverageStayTime().split("\\:");
                    stayTimeAmount += (Integer.parseInt(stayTimeList[0]) * 60 + Integer.parseInt(stayTimeList[1]));
                }
            }
            stayTimeAmountMap.put(provinceId, stayTimeAmount);

            againRateAmount = 0.0d;
            if(againRateAmountMap.get(provinceId) != null) {
                againRateAmount = againRateAmountMap.get(provinceId);
            }
            if(flowArea.getAgainClickRate() != null){
                againClickRate = flowArea.getAgainClickRate();
                if(againClickRate.contains("%")){
                    againClickRate = againClickRate.replaceAll("%", "");
                    againRateAmount += Double.parseDouble(againClickRate) / 100;
                }else {
                    againRateAmount += Double.parseDouble(againClickRate);
                }
            }
            againRateAmountMap.put(provinceId, againRateAmount);
        }

        Map<Integer, String> areaNameMap = new HashMap<>();
        areaNameMap.put(0, "未知地区");
        List<Area> areaList = areaService.findByIdList(provinceIdList);
        for (Area area : areaList) {
            areaNameMap.put(area.getAreaId(), area.getAreaName());
        }

        JSONObject tableItem;
        List<JSONObject> tableItemList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        for (Integer province : userCountMap.keySet()) {
            tableItem = new JSONObject();
            tableItem.put("areaName", areaNameMap.get(province));
            tableItem.put("userCount", userCountMap.get(province));
            tableItem.put("browseCount", browseCountMap.get(province));
            tableItem.put("inflowCount", inflowCountMap.get(province));
            tableItem.put("stayTime", "0.00");
            tableItem.put("againRate", "0.00%");
            if(countMap.get(province) != null) {
                tableItem.put("stayTime", decimalFormat.format(stayTimeAmountMap.get(province) / countMap.get(province)));
                tableItem.put("againRate", decimalFormat.format(againRateAmountMap.get(province) / countMap.get(province)) + "%");
            }
            tableItemList.add(tableItem);
        }

        /** 汇总结果 **/
        JSONObject resultJson = new JSONObject();
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
