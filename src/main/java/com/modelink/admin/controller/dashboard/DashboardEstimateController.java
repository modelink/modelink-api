package com.modelink.admin.controller.dashboard;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.vo.DashboardAreaParamVo;
import com.modelink.admin.vo.DashboardParamVo;
import com.modelink.common.enums.*;
import com.modelink.common.utils.DataUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.*;
import com.modelink.reservation.enums.FeeTypeEnum;
import com.modelink.reservation.service.*;
import com.modelink.reservation.vo.EstimateParamPagerVo;
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
@RequestMapping("/admin/dashboard/estimate")
public class DashboardEstimateController {

    @Resource
    private AreaService areaService;
    @Resource
    private EstimateService estimateService;
    @Resource
    private FlowReserveService flowReserveService;
    @Resource
    private UnderwriteService underwriteService;
    @Resource
    private RepellentService repellentService;
    @Resource
    private MediaItemService mediaItemService;

    @RequestMapping
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/dashboard/estimate");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/getEstimateResult")
    public ResultVo getEstimateResult(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        /** 查询预约数量 **/
        FlowReserveParamPagerVo flowReserveParamPagerVo = new FlowReserveParamPagerVo();
        flowReserveParamPagerVo.setChooseDate(paramVo.getChooseDate());
        flowReserveParamPagerVo.setMerchantId(paramVo.getMerchantId());
        flowReserveParamPagerVo.setPlatformName(paramVo.getPlatformName());
        flowReserveParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        flowReserveParamPagerVo.setColumnFieldIds("date,reserveMobile");
        flowReserveParamPagerVo.setFeeType(FeeTypeEnum.FEE_TYPE_ESTIMATE.getText());
        flowReserveParamPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(flowReserveParamPagerVo);

        String dateKey;
        int transformCount;
        int transformTotalCount = 0;
        Map<String, Object> transformCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), DateTypeEnum.日.getValue(), "int");
        for (FlowReserve flowReserve : flowReserveList) {
            dateKey = DataUtils.getDateKeyByDateType(flowReserve.getDate(), DateTypeEnum.日.getValue());

            transformCount = (int)transformCountMap.get(dateKey);
            transformCount ++;
            transformTotalCount ++;
            transformCountMap.put(dateKey, transformCount);
        }

        UnderwriteParamPagerVo underwriteParamPagerVo = new UnderwriteParamPagerVo();
        underwriteParamPagerVo.setChooseDate(paramVo.getChooseDate());
        underwriteParamPagerVo.setMerchantId(paramVo.getMerchantId());
        underwriteParamPagerVo.setColumnFieldIds("id,reserveDate,insuranceFee");
        underwriteParamPagerVo.setPlatformName(paramVo.getPlatformName());
        underwriteParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        underwriteParamPagerVo.setDateField("reserveDate");
        underwriteParamPagerVo.setSource("产品测保");
        List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);

        // 求退保保费
        Set<String> insuranceNoSet = new HashSet<>();
        for (Underwrite underwrite : underwriteList) {
            if(StringUtils.hasText(underwrite.getInsuranceNo())){
                insuranceNoSet.add(underwrite.getInsuranceNo());
            }
        }
        Map<String, String> repellentMap = new HashMap<>();
        List<Repellent> repellentList = repellentService.findListByInsuranceNoList(insuranceNoSet);
        for (Repellent repellent : repellentList) {
            if(StringUtils.hasText(repellent.getInsuranceFee()) && !"-".equals(repellent.getInsuranceFee())) {
                repellentMap.put(repellent.getInsuranceNo(), repellent.getInsuranceFee());
            }
        }

        int underwriteCount;
        double underwriteAmount;
        int underwriteTotalCount = 0;
        double underwriteTotalAmount = 0.00d;
        Map<String, Integer> underwriteCountMap = new HashMap<>();
        Map<String, Double> underwriteAmountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            dateKey = DataUtils.getDateKeyByDateType(underwrite.getReserveDate(), DateTypeEnum.日.getValue());

            underwriteCount = 0;
            if (underwriteCountMap.get(dateKey) != null){
                underwriteCount = underwriteCountMap.get(dateKey);
            }
            underwriteCount ++;
            underwriteTotalCount ++;
            underwriteCountMap.put(dateKey, underwriteCount);

            underwriteAmount = 0.0d;
            if (underwriteAmountMap.get(dateKey) != null) {
                underwriteAmount = underwriteAmountMap.get(dateKey);
            }
            if (StringUtils.hasText(underwrite.getInsuranceFee()) && !"-".equals(underwrite.getInsuranceFee())) {
                underwriteAmount += Double.parseDouble(underwrite.getInsuranceFee());
                underwriteTotalAmount += Double.parseDouble(underwrite.getInsuranceFee());
            }
            if (repellentMap.get(underwrite.getInsuranceNo()) != null) {
                underwriteAmount -= Double.parseDouble(repellentMap.get(underwrite.getInsuranceNo()));
                underwriteTotalAmount -= Double.parseDouble(repellentMap.get(underwrite.getInsuranceNo()));
            }
            underwriteAmountMap.put(dateKey, underwriteAmount);
        }

        Set<String> keySet = transformCountMap.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        List<String> titleList = new ArrayList<>();
        List<Integer> transformCountList = new ArrayList<>();
        List<Integer> underwriteCountList = new ArrayList<>();
        List<String> underwriteAmountList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        for (String key : keyArray) {
            titleList.add(key);

            transformCountList.add((int)transformCountMap.get(key));
            if (underwriteCountMap.get(key) == null) {
                underwriteCountList.add(0);
            } else {
                underwriteCountList.add(underwriteCountMap.get(key));
            }
            if (underwriteAmountMap.get(key) == null) {
                underwriteAmountList.add("0.00");
            } else {
                underwriteAmountList.add(decimalFormat.format(underwriteAmountMap.get(key)));
            }
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("transformCountList", transformCountList);
        resultJson.put("underwriteCountList", underwriteCountList);
        resultJson.put("underwriteAmountList", underwriteAmountList);
        resultJson.put("transformTotalCount", transformTotalCount);
        resultJson.put("underwriteTotalCount", underwriteTotalCount);
        resultJson.put("underwriteTotalAmount", underwriteTotalAmount);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getEstimateTransform")
    public ResultVo getEstimateTransform(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        MediaItemParamPagerVo mediaItemParamPagerVo = new MediaItemParamPagerVo();
        mediaItemParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaItemParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaItemParamPagerVo.setPlatformName(paramVo.getPlatformName());
        mediaItemParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        mediaItemParamPagerVo.setColumnFieldIds("date,merchantId,platformName,advertiseActive,clickCount,speedCost");
        mediaItemParamPagerVo.setFeeType(FeeTypeEnum.FEE_TYPE_ESTIMATE.getText());
        mediaItemParamPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);

        String dateKey;
        Double speedCost;
        double transformTotalAmount = 0.00d;
        Map<String, Double> speedCostMap = new HashMap<>();
        for (MediaItem mediaItem : mediaItemList) {
            dateKey = DataUtils.getDateKeyByDateType(mediaItem.getDate(), DateTypeEnum.日.getValue());

            speedCost = 0.00d;
            if (speedCostMap.get(dateKey) != null) {
                speedCost = speedCostMap.get(dateKey);
            }
            if (StringUtils.hasText(mediaItem.getSpeedCost()) && !"-".equals(mediaItem.getSpeedCost())) {
                speedCost += Double.parseDouble(mediaItem.getSpeedCost());
                transformTotalAmount += Double.parseDouble(mediaItem.getSpeedCost());
            }
            speedCostMap.put(dateKey, speedCost);
        }

        /** 查询预约数量 **/
        FlowReserveParamPagerVo flowReserveParamPagerVo = new FlowReserveParamPagerVo();
        flowReserveParamPagerVo.setChooseDate(paramVo.getChooseDate());
        flowReserveParamPagerVo.setMerchantId(paramVo.getMerchantId());
        flowReserveParamPagerVo.setPlatformName(paramVo.getPlatformName());
        flowReserveParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        flowReserveParamPagerVo.setColumnFieldIds("date,reserveMobile");
        flowReserveParamPagerVo.setFeeType(FeeTypeEnum.FEE_TYPE_ESTIMATE.getText());
        flowReserveParamPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(flowReserveParamPagerVo);
        int transformCount;
        int transformTotalCount = 0;
        Map<String, Object> transformCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), DateTypeEnum.日.getValue(), "int");
        for (FlowReserve flowReserve : flowReserveList) {
            dateKey = DataUtils.getDateKeyByDateType(flowReserve.getDate(), DateTypeEnum.日.getValue());

            transformCount = (int)transformCountMap.get(dateKey);
            transformTotalCount ++;
            transformCount ++;
            transformCountMap.put(dateKey, transformCount);
        }

        Set<String> keySet = transformCountMap.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        List<String> titleList = new ArrayList<>();
        List<String> transformCostList = new ArrayList<>();
        List<String> transformAmountList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        for (String key : keyArray) {
            titleList.add(key);

            if (speedCostMap.get(key) == null) {
                transformAmountList.add("0.00");
                transformCostList.add("0.00");
            } else if ((int) transformCountMap.get(key) == 0) {
                transformCostList.add("0.00");
                transformAmountList.add(decimalFormat.format(speedCostMap.get(key)));
            } else {
                transformAmountList.add(decimalFormat.format(speedCostMap.get(key)));
                transformCostList.add(decimalFormat.format(speedCostMap.get(key) / (int)transformCountMap.get(key)));
            }
        }

        if (transformTotalCount == 0) transformTotalCount = 1;
        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("transformCostList", transformCostList);
        resultJson.put("transformAmountList", transformAmountList);
        resultJson.put("transformTotalAmount", decimalFormat.format(transformTotalAmount));
        resultJson.put("transformTotalCost", decimalFormat.format(transformTotalAmount / transformTotalCount));
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getEstimateBrowse")
    public ResultVo getEstimateBrowse(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        MediaItemParamPagerVo mediaItemParamPagerVo = new MediaItemParamPagerVo();
        mediaItemParamPagerVo.setChooseDate(paramVo.getChooseDate());
        mediaItemParamPagerVo.setMerchantId(paramVo.getMerchantId());
        mediaItemParamPagerVo.setPlatformName(paramVo.getPlatformName());
        mediaItemParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        mediaItemParamPagerVo.setColumnFieldIds("date,merchantId,platformName,advertiseActive,clickCount,speedCost");
        mediaItemParamPagerVo.setFeeType(FeeTypeEnum.FEE_TYPE_ESTIMATE.getText());
        mediaItemParamPagerVo.setDateField("date");
        List<MediaItem> mediaItemList = mediaItemService.findListByParam(mediaItemParamPagerVo);

        String dateKey;
        Integer showCount;
        Integer clickCount;
        Integer showTotalCount = 0;
        Integer clickTotalCount = 0;
        Map<String, Object> showCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), DateTypeEnum.日.getValue(), "int");
        Map<String, Object> clickCountMap = DataUtils.initResultMap(paramVo.getChooseDate(), DateTypeEnum.日.getValue(), "int");
        for (MediaItem mediaItem : mediaItemList) {
            dateKey = DataUtils.getDateKeyByDateType(mediaItem.getDate(), DateTypeEnum.日.getValue());

            showCount = (int)showCountMap.get(dateKey);
            if (mediaItem.getShowCount() != null) {
                showCount += mediaItem.getShowCount();
                showTotalCount += mediaItem.getShowCount();
            }
            showCountMap.put(dateKey, showCount);

            clickCount = (int)clickCountMap.get(dateKey);
            if (mediaItem.getClickCount() != null) {
                clickCount += mediaItem.getClickCount();
                clickTotalCount += mediaItem.getClickCount();
            }
            clickCountMap.put(dateKey, clickCount);
        }

        List<String> titleList = new ArrayList<>();
        List<Integer> clickCountList = new ArrayList<>();
        List<String> clickRateList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        List<String> keyArray = DataUtils.initDateList(paramVo.getChooseDate(), DateTypeEnum.日.getValue());
        for (String key : keyArray) {
            titleList.add(key);
            clickCountList.add((int)clickCountMap.get(key));
            if ((int)showCountMap.get(key) == 0) {
                clickRateList.add("0.00");
            } else {
                clickRateList.add(decimalFormat.format((int)clickCountMap.get(key) * 100 / (int)showCountMap.get(key)));
            }
        }

        String clickTotalRate = "0.00";
        if (showTotalCount != 0) {
            clickTotalRate = decimalFormat.format(clickTotalCount * 100 / showTotalCount);
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("clickRateList", clickRateList);
        resultJson.put("clickCountList", clickCountList);

        resultJson.put("clickTotalRate", clickTotalRate);
        resultJson.put("clickTotalCount", clickTotalCount);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getEstimateKeyword")
    public ResultVo getEstimateKeyword(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        int count;
        String keyword;
        Map<String, Integer> countMap = new HashMap<>();
        if("underwrite".equals(paramVo.getSource())) {
            UnderwriteParamPagerVo underwriteParamPagerVo = new UnderwriteParamPagerVo();
            underwriteParamPagerVo.setChooseDate(paramVo.getChooseDate());
            underwriteParamPagerVo.setMerchantId(paramVo.getMerchantId());
            underwriteParamPagerVo.setPlatformName(paramVo.getPlatformName());
            underwriteParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
            underwriteParamPagerVo.setColumnFieldIds("id,keyword");
            underwriteParamPagerVo.setSource("产品测保");
            underwriteParamPagerVo.setDateField("reserveDate");
            List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);

            for (Underwrite underwrite : underwriteList) {
                keyword = underwrite.getKeyword();
                count = 0;
                if (countMap.get(keyword) != null) {
                    count = countMap.get(keyword);
                }
                count ++;
                countMap.put(keyword, count);
            }
        }else{
            FlowReserveParamPagerVo flowReserveParamPagerVo = new FlowReserveParamPagerVo();
            flowReserveParamPagerVo.setChooseDate(paramVo.getChooseDate());
            flowReserveParamPagerVo.setMerchantId(paramVo.getMerchantId());
            flowReserveParamPagerVo.setPlatformName(paramVo.getPlatformName());
            flowReserveParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
            flowReserveParamPagerVo.setColumnFieldIds("id,advertiseDesc");
            flowReserveParamPagerVo.setFeeType(FeeTypeEnum.FEE_TYPE_ESTIMATE.getText());
            flowReserveParamPagerVo.setDateField("date");
            List<FlowReserve> flowReserveList = flowReserveService.findListByParam(flowReserveParamPagerVo);

            for (FlowReserve flowReserve : flowReserveList) {
                keyword = flowReserve.getAdvertiseDesc();
                count = 0;
                if (countMap.get(keyword) != null) {
                    count = countMap.get(keyword);
                }
                count ++;
                countMap.put(keyword, count);
            }
        }

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(countMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int index = 1;
        List<String> titleList = new ArrayList<>();
        List<Integer> contentList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : entryList) {
            if (index > 30) break;
            titleList.add(entry.getKey());
            contentList.add(entry.getValue());
            index ++;
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("contentList", contentList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getEstimateArea")
    public ResultVo getEstimateArea(DashboardAreaParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        int count;
        Integer areaId;
        List<Integer> areaIdList = new ArrayList<>();
        Map<Integer, Integer> countMap = new HashMap<>();
        Area area = null;
        if(StringUtils.hasText(paramVo.getProvinceName())) {
            area = areaService.findByNameAndType(paramVo.getProvinceName(), AreaTypeEnum.省.getValue());
        }
        if("underwrite".equals(paramVo.getSource())) {
            UnderwriteParamPagerVo underwriteParamPagerVo = new UnderwriteParamPagerVo();
            underwriteParamPagerVo.setChooseDate(paramVo.getChooseDate());
            underwriteParamPagerVo.setMerchantId(paramVo.getMerchantId());
            underwriteParamPagerVo.setPlatformName(paramVo.getPlatformName());
            underwriteParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
            underwriteParamPagerVo.setColumnFieldIds("id,provinceId,cityId");
            underwriteParamPagerVo.setSource("产品测保");
            underwriteParamPagerVo.setDateField("reserveDate");
            if(StringUtils.hasText(paramVo.getProvinceName())) {
                underwriteParamPagerVo.setProvinceId(area == null ? 0 : area.getAreaId());
            }
            List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);

            for (Underwrite underwrite : underwriteList) {
                if(StringUtils.hasText(paramVo.getProvinceName())) {
                    areaId = underwrite.getCityId();
                }else{
                    areaId = underwrite.getProvinceId();
                }
                areaIdList.add(areaId);
                count = 0;
                if (countMap.get(areaId) != null) {
                    count = countMap.get(areaId);
                }
                count ++;
                countMap.put(areaId, count);
            }
        }else{
            FlowReserveParamPagerVo flowReserveParamPagerVo = new FlowReserveParamPagerVo();
            flowReserveParamPagerVo.setChooseDate(paramVo.getChooseDate());
            flowReserveParamPagerVo.setMerchantId(paramVo.getMerchantId());
            flowReserveParamPagerVo.setPlatformName(paramVo.getPlatformName());
            flowReserveParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
            flowReserveParamPagerVo.setColumnFieldIds("id,provinceId,cityId");
            flowReserveParamPagerVo.setFeeType(FeeTypeEnum.FEE_TYPE_ESTIMATE.getText());
            flowReserveParamPagerVo.setDateField("date");
            if(StringUtils.hasText(paramVo.getProvinceName())) {
                flowReserveParamPagerVo.setProvinceId(area == null ? 0 : area.getAreaId());
            }
            List<FlowReserve> flowReserveList = flowReserveService.findListByParam(flowReserveParamPagerVo);

            for (FlowReserve flowReserve : flowReserveList) {
                if(StringUtils.hasText(paramVo.getProvinceName())) {
                    areaId = flowReserve.getCityId();
                }else{
                    areaId = flowReserve.getProvinceId();
                }
                areaIdList.add(areaId);
                count = 0;
                if (countMap.get(areaId) != null) {
                    count = countMap.get(areaId);
                }
                count ++;
                countMap.put(areaId, count);
            }
        }

        List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(countMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<Integer, String> areaNameMap = new HashMap<>();
        areaNameMap.put(0, "未知地区");
        List<Area> areaList = areaService.findByIdList(areaIdList);
        for (Area areaBean : areaList) {
            areaNameMap.put(areaBean.getAreaId(), areaBean.getAreaName());
        }

        int index = 1;
        List<String> titleList = new ArrayList<>();
        List<Integer> contentList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : entryList) {
            if (index > 30) break;
            titleList.add(areaNameMap.get(entry.getKey()));
            contentList.add(entry.getValue());
            index ++;
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("contentList", contentList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getEstimateClient")
    public ResultVo getEstimateClient(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        int count;
        String key;
        List<Integer> areaIdList = new ArrayList<>();
        Map<String, Integer> countMap = new HashMap<>();

        FlowReserveParamPagerVo flowReserveParamPagerVo = new FlowReserveParamPagerVo();
        flowReserveParamPagerVo.setChooseDate(paramVo.getChooseDate());
        flowReserveParamPagerVo.setMerchantId(paramVo.getMerchantId());
        flowReserveParamPagerVo.setPlatformName(paramVo.getPlatformName());
        flowReserveParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        flowReserveParamPagerVo.setColumnFieldIds("id,browser,os,resolutionRatio");
        flowReserveParamPagerVo.setFeeType(FeeTypeEnum.FEE_TYPE_ESTIMATE.getText());
        flowReserveParamPagerVo.setDateField("date");
        List<FlowReserve> flowReserveList = flowReserveService.findListByParam(flowReserveParamPagerVo);
        if("os".equals(paramVo.getSource())) {
            for (FlowReserve flowReserve : flowReserveList) {
                key = flowReserve.getOs();
                count = 0;
                if (countMap.get(key) != null) {
                    count = countMap.get(key);
                }
                count ++;
                countMap.put(key, count);
            }
        }else if("resolutionRatio".equals(paramVo.getSource())) {
            for (FlowReserve flowReserve : flowReserveList) {
                key = flowReserve.getResolutionRatio();
                count = 0;
                if (countMap.get(key) != null) {
                    count = countMap.get(key);
                }
                count ++;
                countMap.put(key, count);
            }
        }else{
            for (FlowReserve flowReserve : flowReserveList) {
                key = flowReserve.getBrowser();
                if(StringUtils.hasText(key) && key.indexOf(" ") > 0) {
                    key = key.substring(0, key.indexOf(" "));
                }
                count = 0;
                if (countMap.get(key) != null) {
                    count = countMap.get(key);
                }
                count ++;
                countMap.put(key, count);
            }
        }

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(countMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int index = 1;
        List<String> titleList = new ArrayList<>();
        List<Integer> contentList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : entryList) {
            if (index > 30) break;
            titleList.add(entry.getKey());
            contentList.add(entry.getValue());
            index ++;
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("contentList", contentList);
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getGenderAge")
    public ResultVo getGenderAge(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        // 保费汇总
        UnderwriteParamPagerVo underwriteParamPagerVo = new UnderwriteParamPagerVo();
        underwriteParamPagerVo.setChooseDate(paramVo.getChooseDate());
        underwriteParamPagerVo.setMerchantId(paramVo.getMerchantId());
        underwriteParamPagerVo.setPlatformName(paramVo.getPlatformName());
        underwriteParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        underwriteParamPagerVo.setColumnFieldIds("id,gender,age,insuranceFee");
        underwriteParamPagerVo.setSource("产品测保");
        underwriteParamPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);

        // 求退保保费
        Set<String> insuranceNoSet = new HashSet<>();
        for (Underwrite underwrite : underwriteList) {
            if(StringUtils.hasText(underwrite.getInsuranceNo())){
                insuranceNoSet.add(underwrite.getInsuranceNo());
            }
        }
        Map<String, String> repellentMap = new HashMap<>();
        List<Repellent> repellentList = repellentService.findListByInsuranceNoList(insuranceNoSet);
        for (Repellent repellent : repellentList) {
            if(StringUtils.hasText(repellent.getInsuranceFee()) && !"-".equals(repellent.getInsuranceFee())) {
                repellentMap.put(repellent.getInsuranceNo(), repellent.getInsuranceFee());
            }
        }

        Map<Integer, Double> manMap = DataUtils.initAgeMap();
        Map<Integer, Double> womenMap = DataUtils.initAgeMap();
        int age, mapKey;
        String gender;
        double amount, totalAmount;
        for (Underwrite underwrite : underwriteList) {
            age = underwrite.getAge();
            gender = underwrite.getGender();

            amount = 0.0d;
            if(StringUtils.hasText(underwrite.getInsuranceFee()) && !"-".equals(underwrite.getInsuranceFee())){
                amount = Double.parseDouble(underwrite.getInsuranceFee());
            }
            if(repellentMap.get(underwrite.getInsuranceNo()) != null){
                amount -= Double.parseDouble(repellentMap.get(underwrite.getInsuranceNo()));
            }
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

        double maxAmount = 0.0d;
        List<String> titleList = new ArrayList<>();
        List<String> manList = new ArrayList<>();
        List<String> womanList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        for(AgePartEnum agePartEnum : AgePartEnum.values()){
            if(manMap.get(agePartEnum.getValue()) > maxAmount){
                maxAmount = manMap.get(agePartEnum.getValue());
            }
            if(womenMap.get(agePartEnum.getValue()) > maxAmount){
                maxAmount = womenMap.get(agePartEnum.getValue());
            }
            titleList.add(agePartEnum.getText());
            manList.add(decimalFormat.format(0 - manMap.get(agePartEnum.getValue())));
            womanList.add(decimalFormat.format(womenMap.get(agePartEnum.getValue())));
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("manList", manList);
        resultJson.put("womanList", womanList);
        resultJson.put("maxAmount", decimalFormat.format(maxAmount * 1.2));
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getTransformCycle")
    public ResultVo getTransformCycle(DashboardParamVo paramVo){
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        // 保费汇总
        UnderwriteParamPagerVo underwriteParamPagerVo = new UnderwriteParamPagerVo();
        underwriteParamPagerVo.setChooseDate(paramVo.getChooseDate());
        underwriteParamPagerVo.setMerchantId(paramVo.getMerchantId());
        underwriteParamPagerVo.setPlatformName(paramVo.getPlatformName());
        underwriteParamPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        underwriteParamPagerVo.setColumnFieldIds("id,reserveDate,finishDate,advertiseActive");
        underwriteParamPagerVo.setSource("产品测保");
        underwriteParamPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(underwriteParamPagerVo);

        int totalCount, difference;
        String finishDate, reserveDate;
        Integer underwriteTotalCount = 0;
        Integer totalDifference = 0;
        Map<Integer, Integer> transformCycleMap;
        Set<String> advertiseActiveList = new HashSet<>();
        Map<String, Map<Integer, Integer>> advertiseActiveMap = new HashMap<>();
        for(Underwrite underwrite : underwriteList){
            finishDate = underwrite.getFinishDate();
            reserveDate = underwrite.getReserveDate();
            difference = DateUtils.getDateDifference(reserveDate, finishDate);
            if(difference <= 0){
                continue;
            }
            underwriteTotalCount ++;
            totalDifference += difference;
            advertiseActiveList.add(underwrite.getAdvertiseActive());
            if (advertiseActiveMap.get(underwrite.getAdvertiseActive()) == null) {
                transformCycleMap = new HashMap<>(11);
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
            } else {
                transformCycleMap = advertiseActiveMap.get(underwrite.getAdvertiseActive());
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
            advertiseActiveMap.put(underwrite.getAdvertiseActive(), transformCycleMap);
        }

        Set<Integer> keySet;
        Integer[] keyArray;
        List<Integer> itemList;
        List<String> titleList = new ArrayList<>();
        List<List<Integer>> contentList = new ArrayList<>();
        for (String advertiseActive : advertiseActiveList) {
            titleList.add(advertiseActive);

            transformCycleMap = advertiseActiveMap.get(advertiseActive);
            keySet = transformCycleMap.keySet();
            keyArray = keySet.toArray(new Integer[keySet.size()]);
            Arrays.sort(keyArray, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            itemList = new ArrayList<>();
            for (Integer key : keyArray) {
                itemList.add(transformCycleMap.get(key));
            }
            contentList.add(itemList);
        }
        List<String> labelList = new ArrayList<>();
        for (TransformCycleEnum transformCycleEnum : TransformCycleEnum.values()) {
            labelList.add(transformCycleEnum.getText());
        }

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("contentList", contentList);
        resultJson.put("labelList", labelList);
        if (underwriteTotalCount == 0) {
            resultJson.put("transformTotalCycle", 0);
        } else {
            resultJson.put("transformTotalCycle", decimalFormat.format(totalDifference / underwriteTotalCount));
        }
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
