package com.modelink.admin.controller.dashboard;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.vo.dashboard.DashboardAreaParamVo;
import com.modelink.admin.vo.dashboard.DashboardParamVo;
import com.modelink.common.enums.AreaTypeEnum;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.FlowArea;
import com.modelink.reservation.bean.FlowReserve;
import com.modelink.reservation.bean.Underwrite;
import com.modelink.reservation.enums.FeeTypeEnum;
import com.modelink.reservation.service.FlowAreaService;
import com.modelink.reservation.service.FlowReserveService;
import com.modelink.reservation.service.UnderwriteService;
import com.modelink.reservation.vo.FlowAreaParamPagerVo;
import com.modelink.reservation.vo.FlowReserveParamPagerVo;
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
    public ResultVo getReserveCount(DashboardAreaParamVo paramVo){
        Area area;
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowReserveParamPagerVo paramPagerVo = new FlowReserveParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("date,provinceId,cityId");
        paramPagerVo.setFeeType(FeeTypeEnum.FEE_TYPE_RESERVE.getText());
        paramPagerVo.setDateField("date");
        if(StringUtils.hasText(paramVo.getProvinceName())) {
            area = areaService.findByNameAndType(paramVo.getProvinceName(), AreaTypeEnum.省.getValue());
            paramPagerVo.setProvinceId(area == null ? 0 : area.getAreaId());
        }
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(paramPagerVo);

        Integer areaId;
        int reserveCount;
        Map<Integer, Integer> province2ReserveCountMap = new HashMap<>();
        for (FlowReserve flowReserve : flowReserveList) {
            if(StringUtils.hasText(paramVo.getProvinceName())){
                areaId = flowReserve.getCityId();
            }else {
                areaId = flowReserve.getProvinceId();
            }
            if (areaId == null) {
                continue;
            }
            reserveCount = 0;
            if (province2ReserveCountMap.get(areaId) != null) {
                reserveCount = province2ReserveCountMap.get(areaId);
            }
            reserveCount++;
            province2ReserveCountMap.put(areaId, reserveCount);
        }

        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(province2ReserveCountMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }

        });

        int count = 1;
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
    public ResultVo getUnderwriteCount(DashboardAreaParamVo paramVo){
        Area area;
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("id,reserveMobile,provinceId,cityId");
        paramPagerVo.setDateField("reserveDate");
        paramPagerVo.setSource("!产品测保");
        if(StringUtils.hasText(paramVo.getProvinceName())) {
            area = areaService.findByNameAndType(paramVo.getProvinceName(), AreaTypeEnum.省.getValue());
            paramPagerVo.setProvinceId(area == null ? 0 : area.getAreaId());
        }
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        Integer areaId;
        Integer underwriteCount;
        Map<Integer, Integer> underwriteCountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            underwriteCount = 0;
            if(StringUtils.hasText(paramVo.getProvinceName())) {
                areaId = underwrite.getCityId();
            }else{
                areaId = underwrite.getProvinceId();
            }
            if(underwriteCountMap.get(areaId) != null){
                underwriteCount = underwriteCountMap.get(areaId);
            }
            underwriteCount ++;
            underwriteCountMap.put(areaId, underwriteCount);
        }

        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(underwriteCountMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int count = 1;
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
    public ResultVo getUnderwriteAmount(DashboardAreaParamVo paramVo){
        Area area;
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("id,reserveMobile,provinceId,cityId,insuranceFee");
        paramPagerVo.setDateField("reserveDate");
        paramPagerVo.setSource("!产品测保");
        if(StringUtils.hasText(paramVo.getProvinceName())) {
            area = areaService.findByNameAndType(paramVo.getProvinceName(), AreaTypeEnum.省.getValue());
            paramPagerVo.setProvinceId(area == null ? 0 : area.getAreaId());
        }
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        Integer areaId;
        double underwriteAmount;
        List<Integer> provinceIdList = new ArrayList<>();
        Map<Integer, Double> underwriteAmountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            underwriteAmount = 0.00d;
            if(StringUtils.hasText(paramVo.getProvinceName())) {
                areaId = underwrite.getCityId();
            }else{
                areaId = underwrite.getProvinceId();
            }
            provinceIdList.add(areaId);
            if(underwriteAmountMap.get(areaId) != null){
                underwriteAmount = underwriteAmountMap.get(areaId);
            }
            if(StringUtils.hasText(underwrite.getInsuranceFee()) && !"-".equals(underwrite.getInsuranceFee())) {
                underwriteAmount += Double.parseDouble(underwrite.getInsuranceFee());
            }
            underwriteAmountMap.put(areaId, underwriteAmount);
        }

        // 获取省份的名称
        Map<Integer, String> areaNameMap = new HashMap<>();
        List<Area> areaList = areaService.findByIdList(provinceIdList);
        for(Area areaBean : areaList) {
            areaNameMap.put(areaBean.getAreaId(), areaBean.getAreaName());
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
        paramPagerVo.setSource("!产品测保");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        Integer provinceId;
        String insuranceFee;
        double underwriteAmount;
        double maxAmount = 0.00d;
        double underwriteTotalAmount = 0.00d;
        Map<Integer, Double> underwriteAmountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            provinceId = underwrite.getProvinceId();
            if(provinceId == null || provinceId == 0){
                continue;
            }

            underwriteAmount = 0.0d;
            if(underwriteAmountMap.get(provinceId) != null){
                underwriteAmount = underwriteAmountMap.get(provinceId);
            }
            insuranceFee = underwrite.getInsuranceFee();
            if(StringUtils.hasText(insuranceFee) && !"-".equals(insuranceFee)){
                underwriteAmount += Double.parseDouble(insuranceFee);
                underwriteTotalAmount += Double.parseDouble(insuranceFee);
                maxAmount = (maxAmount >= Double.parseDouble(insuranceFee) ? maxAmount : Double.parseDouble(insuranceFee));
            }
            underwriteAmountMap.put(provinceId, underwriteAmount);
        }

        // 获取省份的名称
        JSONObject provinceJson;
        Area areaParam = new Area();
        areaParam.setAreaType(AreaTypeEnum.省.getValue());
        List<Area> areaList = areaService.findListByParam(areaParam);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        JSONArray provinceArray = new JSONArray();
        for (Area area : areaList) {
            provinceJson = new JSONObject();
            provinceJson.put("name", area.getAreaName());
            if (underwriteAmountMap.get(area.getAreaId()) == null) {
                provinceJson.put("ratio", "0.00");
                provinceJson.put("value", "0.00");
            } else {
                provinceJson.put("ratio", decimalFormat.format(underwriteAmountMap.get(area.getAreaId()) * 100 / underwriteTotalAmount));
                provinceJson.put("value", decimalFormat.format(underwriteAmountMap.get(area.getAreaId())));
            }
            provinceArray.add(provinceJson);
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("provinceList", provinceArray);
        resultJson.put("maxAmount", maxAmount);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getAgainRate")
    public ResultVo getAgainRate(DashboardAreaParamVo paramVo){
        Area area;
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowAreaParamPagerVo paramPagerVo = new FlowAreaParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setColumnFieldIds("date,provinceId,cityId,againClickRate");
        paramPagerVo.setDateField("date");
        if(StringUtils.hasText(paramVo.getProvinceName())) {
            area = areaService.findByNameAndType(paramVo.getProvinceName(), AreaTypeEnum.省.getValue());
            paramPagerVo.setProvinceId(area == null ? 0 : area.getAreaId());
        }
        List<FlowArea> flowAreaList = flowAreaService.findListByParam(paramPagerVo);

        Integer areaId;
        int cityCount;
        double againRate;
        String againClickRate;
        List<Integer> provinceIdList = new ArrayList<>();
        Map<Integer, Double> againRateMap = new HashMap<>();
        Map<Integer, Integer> cityCountMap = new HashMap<>();
        for (FlowArea flowArea : flowAreaList) {
            if(StringUtils.hasText(paramVo.getProvinceName())) {
                areaId = flowArea.getCityId();
            }else{
                areaId = flowArea.getProvinceId();
            }
            if(areaId == null){
                continue;
            }
            provinceIdList.add(areaId);

            cityCount = 0;
            if(cityCountMap.get(areaId) != null){
                cityCount = cityCountMap.get(areaId);
            }
            cityCount ++;
            cityCountMap.put(areaId, cityCount);

            againRate = 0.0d;
            if(againRateMap.get(areaId) != null){
                againRate = againRateMap.get(areaId);
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
            againRateMap.put(areaId, againRate);
        }

        // 计算平均二跳率
        Map<Integer, Double> againRateResultMap = new HashMap<>();
        Iterator<Integer> iterator = cityCountMap.keySet().iterator();
        while (iterator.hasNext()) {
            areaId = iterator.next();
            againRate = againRateMap.get(areaId);
            cityCount = cityCountMap.get(areaId);
            againRateResultMap.put(areaId, againRate * 100 / cityCount);
        }

        // 获取省份的名称
        Map<Integer, String> areaNameMap = new HashMap<>();
        List<Area> areaList = areaService.findByIdList(provinceIdList);
        for(Area areaBean : areaList) {
            areaNameMap.put(areaBean.getAreaId(), areaBean.getAreaName());
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
            contentList.add(decimalFormat.format(mapping.getValue()) + "%");
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
    public ResultVo getUserCount(DashboardAreaParamVo paramVo){
        Area area;
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowAreaParamPagerVo paramPagerVo = new FlowAreaParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setColumnFieldIds("date,provinceId,cityId,userCount");
        paramPagerVo.setSource(paramVo.getSource());
        paramPagerVo.setDateField("date");
        if(StringUtils.hasText(paramVo.getProvinceName())) {
            area = areaService.findByNameAndType(paramVo.getProvinceName(), AreaTypeEnum.省.getValue());
            paramPagerVo.setProvinceId(area == null ? 0 : area.getAreaId());
        }
        List<FlowArea> flowAreaList = flowAreaService.findListByParam(paramPagerVo);

        int cityCount;
        int userCount;
        Integer areaId;
        List<Integer> areaIdList = new ArrayList<>();
        Map<Integer, Integer> userCountMap = new HashMap<>();
        Map<Integer, Integer> cityCountMap = new HashMap<>();
        for (FlowArea flowArea : flowAreaList) {
            if(StringUtils.hasText(paramVo.getProvinceName())) {
                areaId = flowArea.getCityId();
            }else{
                areaId = flowArea.getProvinceId();
            }
            if(areaId == null){
                continue;
            }
            areaIdList.add(areaId);

            cityCount = 0;
            if(cityCountMap.get(areaId) != null){
                cityCount = cityCountMap.get(areaId);
            }
            cityCount ++;
            cityCountMap.put(areaId, cityCount);

            userCount = 0;
            if(userCountMap.get(areaId) != null){
                userCount = userCountMap.get(areaId);
            }
            if(flowArea.getUserCount() != null){
                userCount += flowArea.getUserCount();
            }
            userCountMap.put(areaId, userCount);
        }

        // 计算平均用户数
        Map<Integer, Double> againRateResultMap = new HashMap<>();
        Iterator<Integer> iterator = cityCountMap.keySet().iterator();
        while (iterator.hasNext()) {
            areaId = iterator.next();
            userCount = userCountMap.get(areaId);
            cityCount = cityCountMap.get(areaId);
            againRateResultMap.put(areaId, userCount * 100.0d / cityCount);
        }

        // 获取省份的名称
        Map<Integer, String> areaNameMap = new HashMap<>();
        List<Area> areaList = areaService.findByIdList(areaIdList);
        for(Area areaBean : areaList) {
            areaNameMap.put(areaBean.getAreaId(), areaBean.getAreaName());
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
    public ResultVo getUserStayTime(DashboardAreaParamVo paramVo){
        Area area;
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        FlowAreaParamPagerVo paramPagerVo = new FlowAreaParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setColumnFieldIds("date,provinceId,averageStayTime");
        paramPagerVo.setSource(paramVo.getSource());
        paramPagerVo.setDateField("date");
        if(StringUtils.hasText(paramVo.getProvinceName())) {
            area = areaService.findByNameAndType(paramVo.getProvinceName(), AreaTypeEnum.省.getValue());
            paramPagerVo.setProvinceId(area == null ? 0 : area.getAreaId());
        }
        List<FlowArea> flowAreaList = flowAreaService.findListByParam(paramPagerVo);

        String[] stayTimeList;
        int userCount, totalStayTime;
        Integer areaId;
        List<Integer> provinceIdList = new ArrayList<>();
        Map<Integer, Integer> userCountMap = new HashMap<>();
        Map<Integer, Integer> totalStayTimeMap = new HashMap<>();
        for (FlowArea flowArea : flowAreaList) {
            if(StringUtils.hasText(paramVo.getProvinceName())) {
                areaId = flowArea.getCityId();
            }else{
                areaId = flowArea.getProvinceId();
            }
            if(areaId == null){
                continue;
            }
            provinceIdList.add(areaId);

            totalStayTime = 0;
            if(totalStayTimeMap.get(areaId) != null){
                totalStayTime = totalStayTimeMap.get(areaId);
            }
            if(StringUtils.hasText(flowArea.getAverageStayTime())) {
                if(">1h".equals(flowArea.getAverageStayTime())){
                    totalStayTime += 3600;
                }else{
                    stayTimeList = flowArea.getAverageStayTime().split("\\:");
                    totalStayTime += (Integer.parseInt(stayTimeList[0]) * 60 + Integer.parseInt(stayTimeList[1]));
                }
            }
            totalStayTimeMap.put(areaId, totalStayTime);

            userCount = 0;
            if(userCountMap.get(areaId) != null){
                userCount = userCountMap.get(areaId);
            }
            userCount ++;
            userCountMap.put(areaId, userCount);
        }

        // 计算平均停留时间
        Map<Integer, Double> stayTimeResultMap = new HashMap<>();
        Iterator<Integer> iterator = totalStayTimeMap.keySet().iterator();
        while (iterator.hasNext()) {
            areaId = iterator.next();
            userCount = userCountMap.get(areaId);
            totalStayTime = totalStayTimeMap.get(areaId);
            stayTimeResultMap.put(areaId, totalStayTime * 1.0d / userCount);
        }

        // 获取省份的名称
        Map<Integer, String> areaNameMap = new HashMap<>();
        List<Area> areaList = areaService.findByIdList(provinceIdList);
        for(Area areaBean : areaList) {
            areaNameMap.put(areaBean.getAreaId(), areaBean.getAreaName());
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
    public ResultVo getUserGender(DashboardAreaParamVo paramVo){
        Area area;
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("reserveDate,provinceId,cityId,gender");
        paramPagerVo.setDateField("reserveDate");
        paramPagerVo.setSource("!产品测保");
        if(StringUtils.hasText(paramVo.getProvinceName())) {
            area = areaService.findByNameAndType(paramVo.getProvinceName(), AreaTypeEnum.省.getValue());
            paramPagerVo.setProvinceId(area == null ? 0 : area.getAreaId());
        }
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        int manCount;
        int womanCount;
        int unknowCount;
        int totalCount;
        Integer areaId;
        List<Integer> provinceIdList = new ArrayList<>();
        Map<Integer, Integer> manCountMap = new HashMap<>();
        Map<Integer, Integer> womanCountMap = new HashMap<>();
        Map<Integer, Integer> totalCountMap = new HashMap<>();
        Map<Integer, Integer> unknowCountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            if(StringUtils.hasText(paramVo.getProvinceName())) {
                areaId = underwrite.getCityId();
            }else{
                areaId = underwrite.getProvinceId();
            }
            if(areaId == null){
                continue;
            }
            provinceIdList.add(areaId);

            if("男".equals(underwrite.getGender())) {
                manCount = 0;
                if (manCountMap.get(areaId) != null) {
                    manCount = manCountMap.get(areaId);
                }
                manCount++;
                manCountMap.put(areaId, manCount);
            }else if("女".equals(underwrite.getGender())) {
                womanCount = 0;
                if (womanCountMap.get(areaId) != null) {
                    womanCount = womanCountMap.get(areaId);
                }
                womanCount++;
                womanCountMap.put(areaId, womanCount);
            }else{
                unknowCount = 0;
                if (unknowCountMap.get(areaId) != null) {
                    unknowCount = unknowCountMap.get(areaId);
                }
                unknowCount++;
                unknowCountMap.put(areaId, unknowCount);
            }
            totalCount = 0;
            if(totalCountMap.get(areaId) != null){
                totalCount = totalCountMap.get(areaId);
            }
            totalCount ++;
            totalCountMap.put(areaId, totalCount);
        }

        // 获取省份的名称
        Map<Integer, String> areaNameMap = new HashMap<>();
        List<Area> areaList = areaService.findByIdList(provinceIdList);
        for(Area areaBean : areaList) {
            areaNameMap.put(areaBean.getAreaId(), areaBean.getAreaName());
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
            manCountList.add(manCountMap.get(mapping.getKey()) != null ? manCountMap.get(mapping.getKey()) : 0);
            womanCountList.add(womanCountMap.get(mapping.getKey()) != null ? womanCountMap.get(mapping.getKey()) : 0);
            unknowCountList.add(unknowCountMap.get(mapping.getKey()) != null ? unknowCountMap.get(mapping.getKey()) : 0);
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
        paramPagerVo.setFeeType(FeeTypeEnum.FEE_TYPE_RESERVE.getText());
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
        underwriteParamPagerVo.setSource("!产品测保");
        List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);

        double underwriteAmount;
        int difference, underwriteCount;
        String reserveDate, finishDate;
        Map<Integer, Double> underwriteAmountMap = new HashMap<>();
        Map<Integer, Integer> underwriteCountMap = new HashMap<>();
        Map<Integer, Integer> differenceMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            cityId = underwrite.getCityId();

            difference = 0;
            if (differenceMap.get(cityId) != null) {
                difference = differenceMap.get(cityId);
            }
            finishDate = underwrite.getFinishDate();
            reserveDate = underwrite.getReserveDate();
            difference += DateUtils.getDateDifference(reserveDate, finishDate);
            differenceMap.put(cityId, difference);

            underwriteCount = 0;
            if (underwriteCountMap.get(cityId) != null) {
                underwriteCount = underwriteCountMap.get(cityId);
            }
            underwriteCount ++;
            underwriteCountMap.put(cityId, underwriteCount);

            underwriteAmount = 0.00d;
            if(underwriteAmountMap.get(cityId) != null){
                underwriteAmount = underwriteAmountMap.get(cityId);
            }
            if(StringUtils.hasText(underwrite.getInsuranceFee()) && !"-".equals(underwrite.getInsuranceFee())) {
                underwriteAmount += Double.parseDouble(underwrite.getInsuranceFee());
            }
            underwriteAmountMap.put(cityId, underwriteAmount);
        }

        Map<Integer, String> transformCycleMap = new HashMap<>();
        Iterator<Integer> iterator = underwriteCountMap.keySet().iterator();
        while(iterator.hasNext()){
            cityId = iterator.next();

            difference = differenceMap.get(cityId);
            underwriteCount = underwriteCountMap.get(cityId);
            if (underwriteCount == 0) {
                transformCycleMap.put(cityId, "0.00");
            } else {
                transformCycleMap.put(cityId, decimalFormat.format(difference / underwriteCount));
            }
        }
        /** 转化周期计算 **/
        /** 转化周期计算 **/


        /** 浏览量计算 **/
        FlowAreaParamPagerVo flowAreaParamPagerVo = new FlowAreaParamPagerVo();
        flowAreaParamPagerVo.setChooseDate(paramVo.getChooseDate());
        flowAreaParamPagerVo.setMerchantId(paramVo.getMerchantId());
        flowAreaParamPagerVo.setPlatformName(paramVo.getPlatformName());
        flowAreaParamPagerVo.setColumnFieldIds("date,provinceId,cityId,browseCount");
        flowAreaParamPagerVo.setSource(paramVo.getSource());
        flowAreaParamPagerVo.setDateField("date");
        int browseCount;
        Map<Integer, Integer> browseCountMap = new HashMap<>();
        if (StringUtils.isEmpty(paramVo.getAdvertiseActive())) {
            List<FlowArea> flowAreaList = flowAreaService.findListByParam(flowAreaParamPagerVo);
            for(FlowArea flowArea : flowAreaList){
                cityId = flowArea.getCityId();
                browseCount = 0;
                if(browseCountMap.get(cityId) != null){
                    browseCount = browseCountMap.get(cityId);
                }
                browseCount += flowArea.getBrowseCount();
                browseCountMap.put(cityId, browseCount);
            }
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
        Map<String, Object> resultBean;
        List<Map<String, Object>> resultList = new ArrayList<>();
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
            resultBean.put("browseCount", browseCountMap.get(areaId) == null ? 0 : browseCountMap.get(areaId).toString());
            resultBean.put("reserveCount", reserveCountMap.get(areaId) == null ? 0 : reserveCountMap.get(areaId).toString());
            resultBean.put("underwriteCount", underwriteCountMap.get(areaId) == null ? 0 : underwriteCountMap.get(areaId).toString());
            resultBean.put("underwriteAmount", underwriteAmountMap.get(areaId) == null ? 0.00d : decimalFormat.format(underwriteAmountMap.get(areaId)));
            resultBean.put("transformCycle", transformCycleMap.get(areaId) == null ? 0.00d : Double.parseDouble(transformCycleMap.get(areaId)));
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
