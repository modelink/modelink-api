package com.modelink.admin.controller.dashboard;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.vo.DashboardParamVo;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.FlowArea;
import com.modelink.reservation.bean.FlowReserve;
import com.modelink.reservation.bean.MediaItem;
import com.modelink.reservation.bean.Underwrite;
import com.modelink.reservation.service.FlowAreaService;
import com.modelink.reservation.service.FlowReserveService;
import com.modelink.reservation.service.MediaItemService;
import com.modelink.reservation.service.UnderwriteService;
import com.modelink.reservation.vo.FlowAreaParamPagerVo;
import com.modelink.reservation.vo.FlowReserveParamPagerVo;
import com.modelink.reservation.vo.MediaItemParamPagerVo;
import com.modelink.reservation.vo.UnderwriteParamPagerVo;
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
@RequestMapping("/admin/dashboard/area")
public class DashboardAreaController {

    @Resource
    private AreaService areaService;
    @Resource
    private MerchantService merchantService;
    @Resource
    private MediaItemService mediaItemService;
    @Resource
    private FlowReserveService flowReserveService;
    @Resource
    private UnderwriteService underwriteService;
    @Resource
    private FlowAreaService flowAreaService;

    @RequestMapping
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/dashboard/area");
        return modelAndView;
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
        paramPagerVo.setColumnFieldIds("date,provinceId");
        paramPagerVo.setFeeType(FlowReserve.FEE_TYPE_RESERVE);
        paramPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        int reserveCount;
        Map<Integer, Integer> province2ReserveCountMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            if (flowReserve.getProvinceId() == null) {
                continue;
            }
            reserveCount = 0;
            if (province2ReserveCountMap.get(flowReserve.getProvinceId()) != null) {
                reserveCount = province2ReserveCountMap.get(flowReserve.getProvinceId());
            }
            reserveCount++;
            province2ReserveCountMap.put(flowReserve.getProvinceId(), reserveCount);
        }

        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(province2ReserveCountMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }

        });

        int count = 1;
        Area area = null;
        JSONObject resultJson = new JSONObject();
        List<String> titleList = new ArrayList<>();
        List<Integer> contentList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> mapping : list) {
            if(count > 10){
                break;
            }
            area = areaService.findById(mapping.getKey());
            titleList.add(area == null ? "未知地区" : area.getAreaName());
            contentList.add(mapping.getValue());
            count ++;
        }
        resultJson.put("titleList", titleList);
        resultJson.put("contentList", contentList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getUnderwriteCount")
    public ResultVo getUnderwriteCount(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("id,reserveMobile,provinceId");
        paramPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        Integer underwriteCount;
        Map<Integer, Integer> underwriteCountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            underwriteCount = 0;
            if(underwriteCountMap.get(underwrite.getProvinceId()) != null){
                underwriteCount = underwriteCountMap.get(underwrite.getProvinceId());
            }
            underwriteCount ++;
            underwriteCountMap.put(underwrite.getProvinceId(), underwriteCount);
        }

        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(underwriteCountMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int count = 1;
        Area area = null;
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        JSONObject resultJson = new JSONObject();
        List<String> titleList = new ArrayList<>();
        List<String> contentList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> mapping : list) {
            if(count > 10){
                break;
            }
            area = areaService.findById(mapping.getKey());
            titleList.add(area == null ? "未知地区" : area.getAreaName());
            contentList.add(decimalFormat.format(mapping.getValue()));
            count ++;
        }
        resultJson.put("titleList", titleList);
        resultJson.put("contentList", contentList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getUnderwriteAmount")
    public ResultVo getUnderwriteAmount(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("id,reserveMobile,provinceId,insuranceFee");
        paramPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        double underwriteAmount;
        List<Integer> provinceIdList = new ArrayList<>();
        Map<Integer, Double> underwriteAmountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            underwriteAmount = 0.00d;
            provinceIdList.add(underwrite.getProvinceId());
            if(underwriteAmountMap.get(underwrite.getProvinceId()) != null){
                underwriteAmount = underwriteAmountMap.get(underwrite.getProvinceId());
            }
            if(StringUtils.hasText(underwrite.getInsuranceFee()) && !"-".equals(underwrite.getInsuranceFee())) {
                underwriteAmount += Double.parseDouble(underwrite.getInsuranceFee());
            }
            underwriteAmountMap.put(underwrite.getProvinceId(), underwriteAmount);
        }

        // 获取省份的名称
        Map<Integer, String> areaNameMap = new HashMap<>();
        List<Area> areaList = areaService.findByIdList(provinceIdList);
        for(Area area : areaList) {
            areaNameMap.put(area.getAreaId(), area.getAreaName());
        }

        List<Map.Entry<Integer, Double>> list = new ArrayList<>(underwriteAmountMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int count = 1;
        String areaName;
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        JSONObject resultJson = new JSONObject();
        List<String> titleList = new ArrayList<>();
        List<String> contentList = new ArrayList<>();
        for (Map.Entry<Integer, Double> mapping : list) {
            if(count > 10){
                break;
            }
            areaName = areaNameMap.get(mapping.getKey());
            titleList.add(areaName == null ? "未知地区" : areaName);
            contentList.add(decimalFormat.format(mapping.getValue()));
            count ++;
        }
        resultJson.put("titleList", titleList);
        resultJson.put("contentList", contentList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getUnderwriteMap")
    public ResultVo getUnderwriteMap(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        // 保费汇总
        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("reserveDate,provinceId,insuranceFee");
        paramPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        Integer provinceId;
        String insuranceFee;
        double underwriteAmount;
        List<Integer> provinceIdList = new ArrayList<>();
        Map<Integer, Double> underwriteAmountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            provinceId = underwrite.getProvinceId();
            if(provinceId == null || provinceId == 0){
                continue;
            }
            provinceIdList.add(provinceId);

            underwriteAmount = 0.0d;
            if(underwriteAmountMap.get(provinceId) != null){
                underwriteAmount = underwriteAmountMap.get(provinceId);
            }
            insuranceFee = underwrite.getInsuranceFee();
            if(StringUtils.hasText(insuranceFee) && !"-".equals(insuranceFee)){
                underwriteAmount += Double.parseDouble(insuranceFee);
            }
            underwriteAmountMap.put(provinceId, underwriteAmount);
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
        Iterator<Integer> iterator = underwriteAmountMap.keySet().iterator();
        while(iterator.hasNext()){
            provinceId = iterator.next();
            provinceJson = new JSONObject();
            provinceJson.put("name", areaNameMap.get(provinceId));
            provinceJson.put("value", decimalFormat.format(underwriteAmountMap.get(provinceId)));
            provinceArray.add(provinceJson);
        }

        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(provinceArray);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getAgainRate")
    public ResultVo getAgainRate(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowAreaParamPagerVo paramPagerVo = new FlowAreaParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("date,provinceId,againClickRate");
        paramPagerVo.setDateField("date");
        List<FlowArea> flowAreaList = flowAreaService.findListByParam(paramPagerVo);

        int cityCount;
        double againRate;
        Integer provinceId;
        String againClickRate;
        List<Integer> provinceIdList = new ArrayList<>();
        Map<Integer, Double> againRateMap = new HashMap<>();
        Map<Integer, Integer> cityCountMap = new HashMap<>();
        for (FlowArea flowArea : flowAreaList) {
            provinceId = flowArea.getProvinceId();
            if(provinceId == null){
                continue;
            }
            provinceIdList.add(provinceId);

            cityCount = 0;
            if(cityCountMap.get(provinceId) != null){
                cityCount = cityCountMap.get(provinceId);
            }
            cityCount ++;
            cityCountMap.put(provinceId, cityCount);

            againRate = 0.0d;
            if(againRateMap.get(provinceId) != null){
                againRate = againRateMap.get(provinceId);
            }
            againClickRate = flowArea.getAgainClickRate();
            if(StringUtils.hasText(againClickRate) && !"-".equals(againClickRate)){
                if(againClickRate.endsWith("%")) {
                    againClickRate = againClickRate.substring(0, againClickRate.length() - 1);
                    againRate += Double.parseDouble(againClickRate) / 100;
                }else {
                    againRate += Double.parseDouble(againClickRate);
                }
            }
            againRateMap.put(provinceId, againRate);
        }

        // 计算平均二跳率
        Map<Integer, Double> againRateResultMap = new HashMap<>();
        Iterator<Integer> iterator = cityCountMap.keySet().iterator();
        while (iterator.hasNext()) {
            provinceId = iterator.next();
            againRate = againRateMap.get(provinceId);
            cityCount = cityCountMap.get(provinceId);
            againRateResultMap.put(provinceId, againRate / cityCount);
        }

        // 获取省份的名称
        Map<Integer, String> areaNameMap = new HashMap<>();
        List<Area> areaList = areaService.findByIdList(provinceIdList);
        for(Area area : areaList) {
            areaNameMap.put(area.getAreaId(), area.getAreaName());
        }

        List<Map.Entry<Integer, Double>> list = new ArrayList<>(againRateResultMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int count = 1;
        String areaName;
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        JSONObject resultJson = new JSONObject();
        List<String> titleList = new ArrayList<>();
        List<String> contentList = new ArrayList<>();
        for (Map.Entry<Integer, Double> mapping : list) {
            if(count > 10){
                break;
            }
            areaName = areaNameMap.get(mapping.getKey());
            titleList.add(areaName == null ? "未知地区" : areaName);
            contentList.add(decimalFormat.format(mapping.getValue()));
            count ++;
        }
        resultJson.put("titleList", titleList);
        resultJson.put("contentList", contentList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getUserCount")
    public ResultVo getUserCount(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowAreaParamPagerVo paramPagerVo = new FlowAreaParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("date,provinceId,userCount");
        paramPagerVo.setSource(paramVo.getSource());
        paramPagerVo.setDateField("date");
        List<FlowArea> flowAreaList = flowAreaService.findListByParam(paramPagerVo);

        int cityCount;
        int userCount;
        Integer provinceId;
        List<Integer> provinceIdList = new ArrayList<>();
        Map<Integer, Integer> userCountMap = new HashMap<>();
        Map<Integer, Integer> cityCountMap = new HashMap<>();
        for (FlowArea flowArea : flowAreaList) {
            provinceId = flowArea.getProvinceId();
            if(provinceId == null){
                continue;
            }
            provinceIdList.add(provinceId);

            cityCount = 0;
            if(cityCountMap.get(provinceId) != null){
                cityCount = cityCountMap.get(provinceId);
            }
            cityCount ++;
            cityCountMap.put(provinceId, cityCount);

            userCount = 0;
            if(userCountMap.get(provinceId) != null){
                userCount = userCountMap.get(provinceId);
            }
            if(flowArea.getUserCount() != null){
                userCount += flowArea.getUserCount();
            }
            userCountMap.put(provinceId, userCount);
        }

        // 计算平均用户数
        Map<Integer, Double> againRateResultMap = new HashMap<>();
        Iterator<Integer> iterator = cityCountMap.keySet().iterator();
        while (iterator.hasNext()) {
            provinceId = iterator.next();
            userCount = userCountMap.get(provinceId);
            cityCount = cityCountMap.get(provinceId);
            againRateResultMap.put(provinceId, userCount * 100.0d / cityCount);
        }

        // 获取省份的名称
        Map<Integer, String> areaNameMap = new HashMap<>();
        List<Area> areaList = areaService.findByIdList(provinceIdList);
        for(Area area : areaList) {
            areaNameMap.put(area.getAreaId(), area.getAreaName());
        }

        List<Map.Entry<Integer, Double>> list = new ArrayList<>(againRateResultMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int count = 1;
        String areaName;
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        JSONObject resultJson = new JSONObject();
        List<String> titleList = new ArrayList<>();
        List<String> contentList = new ArrayList<>();
        for (Map.Entry<Integer, Double> mapping : list) {
            if(count > 10){
                break;
            }
            areaName = areaNameMap.get(mapping.getKey());
            titleList.add(areaName == null ? "未知地区" : areaName);
            contentList.add(decimalFormat.format(mapping.getValue()));
            count ++;
        }
        resultJson.put("titleList", titleList);
        resultJson.put("contentList", contentList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getUserStayTime")
    public ResultVo getUserStayTime(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowAreaParamPagerVo paramPagerVo = new FlowAreaParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("date,provinceId,averageStayTime");
        paramPagerVo.setSource(paramVo.getSource());
        paramPagerVo.setDateField("date");
        List<FlowArea> flowAreaList = flowAreaService.findListByParam(paramPagerVo);

        String[] stayTimeList;
        int userCount, totalStayTime;
        Integer provinceId;
        List<Integer> provinceIdList = new ArrayList<>();
        Map<Integer, Integer> userCountMap = new HashMap<>();
        Map<Integer, Integer> totalStayTimeMap = new HashMap<>();
        for (FlowArea flowArea : flowAreaList) {
            provinceId = flowArea.getProvinceId();
            if(provinceId == null){
                continue;
            }
            provinceIdList.add(provinceId);

            totalStayTime = 0;
            if(totalStayTimeMap.get(provinceId) != null){
                totalStayTime = totalStayTimeMap.get(provinceId);
            }
            if(StringUtils.hasText(flowArea.getAverageStayTime())) {
                if(">1h".equals(flowArea.getAverageStayTime())){
                    totalStayTime += 3600;
                }else{
                    stayTimeList = flowArea.getAverageStayTime().split("\\:");
                    totalStayTime += (Integer.parseInt(stayTimeList[0]) * 60 + Integer.parseInt(stayTimeList[1]));
                }
            }
            totalStayTimeMap.put(provinceId, totalStayTime);

            userCount = 0;
            if(userCountMap.get(provinceId) != null){
                userCount = userCountMap.get(provinceId);
            }
            userCount ++;
            userCountMap.put(provinceId, userCount);
        }

        // 计算平均停留时间
        Map<Integer, Double> stayTimeResultMap = new HashMap<>();
        Iterator<Integer> iterator = totalStayTimeMap.keySet().iterator();
        while (iterator.hasNext()) {
            provinceId = iterator.next();
            userCount = userCountMap.get(provinceId);
            totalStayTime = totalStayTimeMap.get(provinceId);
            stayTimeResultMap.put(provinceId, totalStayTime * 1.0d / userCount);
        }

        // 获取省份的名称
        Map<Integer, String> areaNameMap = new HashMap<>();
        List<Area> areaList = areaService.findByIdList(provinceIdList);
        for(Area area : areaList) {
            areaNameMap.put(area.getAreaId(), area.getAreaName());
        }

        List<Map.Entry<Integer, Double>> list = new ArrayList<>(stayTimeResultMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int count = 1;
        String areaName;
        DecimalFormat decimalFormat = new DecimalFormat("#0");
        JSONObject resultJson = new JSONObject();
        List<String> titleList = new ArrayList<>();
        List<String> contentList = new ArrayList<>();
        for (Map.Entry<Integer, Double> mapping : list) {
            if(count > 10){
                break;
            }
            areaName = areaNameMap.get(mapping.getKey());
            titleList.add(areaName == null ? "未知地区" : areaName);
            contentList.add(decimalFormat.format(mapping.getValue()));
            count ++;
        }
        resultJson.put("titleList", titleList);
        resultJson.put("contentList", contentList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getUserGender")
    public ResultVo getUserGender(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("reserveDate,provinceId,gender");
        paramPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        int manCount;
        int womanCount;
        int unknowCount;
        int totalCount;
        Integer provinceId;
        List<Integer> provinceIdList = new ArrayList<>();
        Map<Integer, Integer> manCountMap = new HashMap<>();
        Map<Integer, Integer> womanCountMap = new HashMap<>();
        Map<Integer, Integer> totalCountMap = new HashMap<>();
        Map<Integer, Integer> unknowCountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            provinceId = underwrite.getProvinceId();
            if(provinceId == null){
                continue;
            }
            provinceIdList.add(provinceId);

            if("男".equals(underwrite.getGender())) {
                manCount = 0;
                if (manCountMap.get(provinceId) != null) {
                    manCount = manCountMap.get(provinceId);
                }
                manCount++;
                manCountMap.put(provinceId, manCount);
            }else if("女".equals(underwrite.getGender())) {
                womanCount = 0;
                if (womanCountMap.get(provinceId) != null) {
                    womanCount = womanCountMap.get(provinceId);
                }
                womanCount++;
                womanCountMap.put(provinceId, womanCount);
            }else{
                unknowCount = 0;
                if (unknowCountMap.get(provinceId) != null) {
                    unknowCount = unknowCountMap.get(provinceId);
                }
                unknowCount++;
                unknowCountMap.put(provinceId, unknowCount);
            }
            totalCount = 0;
            if(totalCountMap.get(provinceId) != null){
                totalCount = totalCountMap.get(provinceId);
            }
            totalCount ++;
            totalCountMap.put(provinceId, totalCount);
        }

        // 获取省份的名称
        Map<Integer, String> areaNameMap = new HashMap<>();
        List<Area> areaList = areaService.findByIdList(provinceIdList);
        for(Area area : areaList) {
            areaNameMap.put(area.getAreaId(), area.getAreaName());
        }

        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(totalCountMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int count = 1;
        String areaName;
        JSONObject resultJson = new JSONObject();
        List<String> titleList = new ArrayList<>();
        List<Integer> manCountList = new ArrayList<>();
        List<Integer> womanCountList = new ArrayList<>();
        List<Integer> unknowCountList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> mapping : list) {
            if(count > 10){
                break;
            }
            areaName = areaNameMap.get(mapping.getKey());
            titleList.add(areaName == null ? "未知地区" : areaName);
            manCountList.add(manCountMap.get(mapping.getKey()));
            womanCountList.add(womanCountMap.get(mapping.getKey()));
            unknowCountList.add(manCountMap.get(mapping.getKey()));
            count ++;
        }
        resultJson.put("titleList", titleList);
        resultJson.put("manList", manCountList);
        resultJson.put("womanList", womanCountList);
        resultJson.put("unknowList", unknowCountList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/areaTableGrid")
    public ResultVo areaTableGrid(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);
        Set<Integer> cityIdList = new HashSet<>();
        Map<Integer, Integer> city2ProvinceMap = new HashMap<>();
        Map<Integer, String> merchantMap = new HashMap<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("date,merchantId,platformName,advertiseActive,provinceId,cityId");
        paramPagerVo.setFeeType(FlowReserve.FEE_TYPE_RESERVE);
        paramPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        Integer cityId;
        int reserveCount;
        Map<Integer, Integer> reserveCountMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            cityId = flowReserve.getCityId();
            reserveCount = 0;
            if(reserveCountMap.get(cityId) != null){
                reserveCount = reserveCountMap.get(cityId);
            }
            reserveCount ++;
            reserveCountMap.put(cityId, reserveCount);
            cityIdList.add(cityId);
            city2ProvinceMap.put(cityId, flowReserve.getProvinceId());
            merchantMap.put(cityId, flowReserve.getMerchantId() + "|" + flowReserve.getPlatformName() + "|" + flowReserve.getAdvertiseActive());
        }


        /** 转化周期计算 **/
        UnderwriteParamPagerVo underwriteParamPagerVo = new UnderwriteParamPagerVo();
        underwriteParamPagerVo.setChooseDate(paramVo.getChooseDate());
        underwriteParamPagerVo.setMerchantId(paramVo.getMerchantId());
        underwriteParamPagerVo.setColumnFieldIds("id,merchantId,platformName,advertiseActive,insuranceFee,reserveDate,finishDate,provinceId,cityId");
        underwriteParamPagerVo.setPlatformName(paramVo.getPlatformName());
        underwriteParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        underwriteParamPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);

        List<Underwrite> tempUnderwriteList;
        Map<Integer, List<Underwrite>> underwriteListMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            if(StringUtils.isEmpty(underwrite.getCityId())) continue;
            cityId = underwrite.getCityId();
            merchantMap.put(cityId, underwrite.getMerchantId() + "|" + underwrite.getPlatformName() + "|" + underwrite.getAdvertiseActive());
            tempUnderwriteList = new ArrayList<>();
            if(underwriteListMap.get(cityId) != null){
                tempUnderwriteList = underwriteListMap.get(cityId);
            }
            tempUnderwriteList.add(underwrite);
            underwriteListMap.put(cityId, tempUnderwriteList);
        }
        String transformCycle;
        double insuranceAmount;
        int difference, totalDifference;
        String reserveDate, finishDate;
        Map<Integer, String> underwriteAmountMap = new HashMap<>();
        Map<Integer, Integer> underwriteCountMap = new HashMap<>();
        Map<Integer, String> transformCycleMap = new HashMap<>();
        Iterator<Integer> iterator = underwriteListMap.keySet().iterator();
        while(iterator.hasNext()){
            cityId = iterator.next();
            tempUnderwriteList = underwriteListMap.get(cityId);
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
            transformCycleMap.put(cityId, transformCycle);
            underwriteCountMap.put(cityId, tempUnderwriteList.size());
            underwriteAmountMap.put(cityId, decimalFormat.format(insuranceAmount));
        }
        /** 转化周期计算 **/


        /** 浏览量计算 **/
        FlowAreaParamPagerVo flowAreaParamPagerVo = new FlowAreaParamPagerVo();
        flowAreaParamPagerVo.setChooseDate(paramVo.getChooseDate());
        flowAreaParamPagerVo.setMerchantId(paramVo.getMerchantId());
        flowAreaParamPagerVo.setPlatformName(paramVo.getPlatformName());
        flowAreaParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        flowAreaParamPagerVo.setColumnFieldIds("date,provinceId,cityId,browseCount");
        flowAreaParamPagerVo.setSource(paramVo.getSource());
        flowAreaParamPagerVo.setDateField("date");
        List<FlowArea> flowAreaList = flowAreaService.findListByParam(flowAreaParamPagerVo);

        int browseCount;
        Map<Integer, Integer> browseCountMap = new HashMap<>();
        for(FlowArea flowArea : flowAreaList){
            cityId = flowArea.getCityId();
            browseCount = 0;
            if(browseCountMap.get(cityId) != null){
                browseCount = browseCountMap.get(cityId);
            }
            browseCount += flowArea.getBrowseCount();
            browseCountMap.put(cityId, browseCount);
        }
        /** 浏览量计算 **/


        List<Integer> areaIdList = new ArrayList<>(cityIdList);
        areaIdList.addAll(city2ProvinceMap.values());
        Map<Integer, String> areaNameMap = new HashMap<>();
        List<Area> areaList = areaService.findByIdList(areaIdList);
        for(Area area : areaList){
            areaNameMap.put(area.getAreaId(), area.getAreaName());
        }
        areaNameMap.put(0, "未知区域");


        /** 汇总结果 **/
        int indexNo = 0;
        Merchant merchant;
        String[] merchantList;
        Map<String, String> resultBean;
        List<Map<String, String>> resultList = new ArrayList<>();
        for (Integer areaId : cityIdList) {
            indexNo ++;
            resultBean = new HashMap<>();
            resultBean.put("cityName", areaNameMap.get(areaId));
            resultBean.put("provinceName", areaNameMap.get(city2ProvinceMap.get(areaId)));
            resultBean.put("indexNo", String.valueOf(indexNo));

            if(merchantMap.get(areaId) != null){
                merchantList = merchantMap.get(areaId).split("\\|");
                merchant = merchantService.findById(Long.parseLong(merchantList[0]));
                resultBean.put("merchantName", merchant == null ? "" : merchant.getName());
                resultBean.put("platformName", merchantList[1]);
                resultBean.put("advertiseActive", merchantList[2]);
            }
            resultBean.put("browseCount", browseCountMap.get(areaId) == null ? "0" : browseCountMap.get(areaId).toString());
            resultBean.put("reserveCount", reserveCountMap.get(areaId) == null ? "0" : reserveCountMap.get(areaId).toString());
            resultBean.put("underwriteCount", underwriteCountMap.get(areaId) == null ? "0" : underwriteCountMap.get(areaId).toString());
            resultBean.put("underwriteAmount", underwriteAmountMap.get(areaId) == null ? "0" : underwriteAmountMap.get(areaId).toString());
            resultBean.put("transformCycle", transformCycleMap.get(areaId) == null ? "0" : transformCycleMap.get(areaId));
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
            String endDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
            String startDate = DateUtils.formatDate(new Date(), "yyyy-MM") + "-01";
            dashboardParamVo.setChooseDate(startDate + " - " + endDate);
        }
        if(StringUtils.hasText(dashboardParamVo.getPlatformName())){
            dashboardParamVo.setPlatformName(dashboardParamVo.getPlatformName().toUpperCase());
        }
    }
}
