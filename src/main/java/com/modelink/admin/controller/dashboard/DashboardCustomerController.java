package com.modelink.admin.controller.dashboard;

import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.vo.DashboardParamVo;
import com.modelink.common.enums.AgePartEnum;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.Repellent;
import com.modelink.reservation.bean.Underwrite;
import com.modelink.reservation.service.RepellentService;
import com.modelink.reservation.service.UnderwriteService;
import com.modelink.reservation.vo.UnderwriteParamPagerVo;
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
@RequestMapping("/admin/dashboard/customer")
public class DashboardCustomerController {

    @Resource
    private AreaService areaService;
    @Resource
    private RepellentService repellentService;
    @Resource
    private UnderwriteService underwriteService;

    @RequestMapping
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/dashboard/customer");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/getUnderwriteCount")
    public ResultVo getUnderwriteCount(DashboardParamVo paramVo) {
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

        int totalCount = 0;
        Integer underwriteCount;
        List<Integer> provinceIdList = new ArrayList<>();
        Map<Integer, Integer> underwriteCountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            underwriteCount = 0;
            if(underwriteCountMap.get(underwrite.getProvinceId()) != null){
                underwriteCount = underwriteCountMap.get(underwrite.getProvinceId());
            }
            totalCount ++;
            underwriteCount ++;
            provinceIdList.add(underwrite.getProvinceId());
            underwriteCountMap.put(underwrite.getProvinceId(), underwriteCount);
        }

        List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(underwriteCountMap.entrySet());
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

        JSONObject jsonObject;
        List<String> titleList = new ArrayList<>();
        List<JSONObject> contentList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        for (Map.Entry<Integer, Integer> entry : entryList) {
            titleList.add(areaNameMap.get(entry.getKey()));
            jsonObject = new JSONObject();
            jsonObject.put("name", areaNameMap.get(entry.getKey()));
            jsonObject.put("value", entry.getValue());
            jsonObject.put("proportion", decimalFormat.format(entry.getValue() * 100.0d / totalCount) + "%");
            contentList.add(jsonObject);
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("contentList", contentList);
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getUnderwriteAmount")
    public ResultVo getUnderwriteAmount(DashboardParamVo paramVo) {
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("id,gender,age,insuranceFee");
        paramPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        Map<Integer, Double> manAmountMap = initDoubleAgeMap();
        Map<Integer, Double> womanAmountMap = initDoubleAgeMap();
        Map<Integer, Integer> manCountMap = new HashMap<>();
        Map<Integer, Integer> womanCountMap = new HashMap<>();
        int age, mapKey;
        String gender;
        int totalCount;
        double insuranceFee, amount, totalAmount;

        totalAmount = 0.0d;
        for (Underwrite underwrite : underwriteList) {
            age = underwrite.getAge();
            gender = underwrite.getGender();
            if(StringUtils.isEmpty(underwrite.getInsuranceFee()) || "-".equals(underwrite.getInsuranceFee())){
                continue;
            }
            insuranceFee = Double.parseDouble(underwrite.getInsuranceFee());
            if(0 < age && age <= 5) {
                mapKey = AgePartEnum.from_0_5.getValue();
            }else if(5 < age && age <= 18){
                mapKey = AgePartEnum.from_5_18.getValue();
            }else if(18 < age && age <= 25){
                mapKey = AgePartEnum.from_18_25.getValue();
            }else if(25 < age && age <= 30){
                mapKey = AgePartEnum.from_25_30.getValue();
            }else if(30 < age && age <= 35){
                mapKey = AgePartEnum.from_30_35.getValue();
            }else if(35 < age && age <= 40){
                mapKey = AgePartEnum.from_35_40.getValue();
            }else if(40 < age && age <= 50){
                mapKey = AgePartEnum.from_40_50.getValue();
            }else if(50 < age && age <= 55){
                mapKey = AgePartEnum.from_50_55.getValue();
            }else if(age > 55){
                mapKey = AgePartEnum.from_55_100.getValue();
            }else{
                mapKey = 100;
            }
            totalAmount += insuranceFee;
            if("男".equals(gender)) {
                amount = manAmountMap.get(mapKey);
                amount += insuranceFee;
                manAmountMap.put(mapKey, amount);

                totalCount = 0;
                if(manCountMap.get(mapKey) != null) {
                    totalCount = manCountMap.get(mapKey);
                }
                totalCount ++;
                manCountMap.put(mapKey, totalCount);
            }else if("女".equals(gender)){
                amount = womanAmountMap.get(mapKey);
                amount += insuranceFee;
                womanAmountMap.put(mapKey, amount);

                totalCount = 0;
                if(womanCountMap.get(mapKey) != null) {
                    totalCount = womanCountMap.get(mapKey);
                }
                totalCount ++;
                womanCountMap.put(mapKey, totalCount);
            }
        }

        JSONObject tableItem;
        double manAmount, womanAmount;
        List<String> titleList = new ArrayList<>();
        List<String> manAmountList = new ArrayList<>();
        List<String> womanAmountList = new ArrayList<>();
        List<JSONObject> tableItemList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        if(totalAmount == 0.0d) totalAmount = 1.0d;
        for (AgePartEnum agePartEnum : AgePartEnum.values()){
            titleList.add(agePartEnum.getText());
            tableItem = new JSONObject();
            manAmount = manAmountMap.get(agePartEnum.getValue());
            womanAmount = womanAmountMap.get(agePartEnum.getValue());
            tableItem.put("agePart", agePartEnum.getText());
            tableItem.put("manCount", manCountMap.get(agePartEnum.getValue()) == null ? 0 : manCountMap.get(agePartEnum.getValue()));
            tableItem.put("womanCount", womanCountMap.get(agePartEnum.getValue()) == null ? 0 : womanCountMap.get(agePartEnum.getValue()));
            tableItem.put("manAmount", decimalFormat.format(manAmount));
            tableItem.put("womanAmount", decimalFormat.format(womanAmount));
            tableItem.put("proportion", decimalFormat.format((manAmount + womanAmount) * 100 / totalAmount) + "%");
            tableItemList.add(tableItem);

            manAmountList.add(decimalFormat.format(manAmountMap.get(agePartEnum.getValue())));
            womanAmountList.add(decimalFormat.format(womanAmountMap.get(agePartEnum.getValue())));
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("titleList", titleList);
        resultJson.put("manAmountList", manAmountList);
        resultJson.put("womanAmountList", womanAmountList);
        resultJson.put("tableItemList", tableItemList);
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getUnderwriteKeyword")
    public ResultVo getUnderwriteKeyword(DashboardParamVo paramVo) {
        ResultVo resultVo = new ResultVo();

        initDashboardParam(paramVo);

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setColumnFieldIds("id,age,gender,keyword");
        paramPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        String gender;
        int age, mapKey;
        int totalCount;
        Map<String, Integer> keyword2CountMap;
        Map<Integer, Map<String, Integer>> manCountMap = new HashMap<>();
        Map<Integer, Map<String, Integer>> womanCountMap = new HashMap<>();
        for (Underwrite underwrite : underwriteList) {
            age = underwrite.getAge();
            gender = underwrite.getGender();
            if(StringUtils.isEmpty(underwrite.getKeyword()) || "-".equals(underwrite.getKeyword())){
                continue;
            }
            if(0 < age && age <= 5) {
                mapKey = AgePartEnum.from_0_5.getValue();
            }else if(5 < age && age <= 18){
                mapKey = AgePartEnum.from_5_18.getValue();
            }else if(18 < age && age <= 25){
                mapKey = AgePartEnum.from_18_25.getValue();
            }else if(25 < age && age <= 30){
                mapKey = AgePartEnum.from_25_30.getValue();
            }else if(30 < age && age <= 35){
                mapKey = AgePartEnum.from_30_35.getValue();
            }else if(35 < age && age <= 40){
                mapKey = AgePartEnum.from_35_40.getValue();
            }else if(40 < age && age <= 50){
                mapKey = AgePartEnum.from_40_50.getValue();
            }else if(50 < age && age <= 55){
                mapKey = AgePartEnum.from_50_55.getValue();
            }else if(age > 55){
                mapKey = AgePartEnum.from_55_100.getValue();
            }else{
                mapKey = 100;
            }
            if("男".equals(gender)) {
                totalCount = 0;
                if(manCountMap.get(mapKey) == null) {
                    keyword2CountMap = new HashMap<>();
                }else{
                    keyword2CountMap = manCountMap.get(mapKey);
                }
                if(keyword2CountMap.get(underwrite.getKeyword()) != null){
                    totalCount = keyword2CountMap.get(underwrite.getKeyword());
                }
                totalCount ++;
                keyword2CountMap.put(underwrite.getKeyword(), totalCount);
                manCountMap.put(mapKey, keyword2CountMap);
            }else if("女".equals(gender)){
                totalCount = 0;
                if(womanCountMap.get(mapKey) == null) {
                    keyword2CountMap = new HashMap<>();
                }else{
                    keyword2CountMap = womanCountMap.get(mapKey);
                }
                if(keyword2CountMap.get(underwrite.getKeyword()) != null){
                    totalCount = keyword2CountMap.get(underwrite.getKeyword());
                }
                totalCount ++;
                keyword2CountMap.put(underwrite.getKeyword(), totalCount);
                womanCountMap.put(mapKey, keyword2CountMap);
            }
        }

        int count;
        JSONObject tableItem;
        List<Map.Entry<String, Integer>> entryList;
        List<JSONObject> tableItemList = new ArrayList<>();
        for (AgePartEnum agePartEnum : AgePartEnum.values()) {
            tableItem = new JSONObject();
            tableItem.put("agePart", agePartEnum.getText());

            tableItem.put("manKeyword1", "");
            tableItem.put("manKeyword2", "");
            tableItem.put("manKeyword3", "");
            keyword2CountMap = manCountMap.get(agePartEnum.getValue());
            if(keyword2CountMap != null){
                entryList = new ArrayList<>(keyword2CountMap.entrySet());
                Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
                count = 1;
                for(Map.Entry<String, Integer> entry : entryList){
                    if(count == 1){
                        tableItem.put("manKeyword1", entry.getKey() + ":" + entry.getValue());
                    }else if(count == 2){
                        tableItem.put("manKeyword2", entry.getKey() + ":" + entry.getValue());
                    }else if(count == 3){
                        tableItem.put("manKeyword3", entry.getKey() + ":" + entry.getValue());
                    }else{
                        break;
                    }
                    count ++;
                }
            }

            tableItem.put("womanKeyword1", "");
            tableItem.put("womanKeyword2", "");
            tableItem.put("womanKeyword3", "");
            keyword2CountMap = womanCountMap.get(agePartEnum.getValue());
            if(keyword2CountMap != null){
                entryList = new ArrayList<>(keyword2CountMap.entrySet());
                Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
                count = 1;
                for(Map.Entry<String, Integer> entry : entryList){
                    if(count == 1){
                        tableItem.put("womanKeyword1", entry.getKey() + ":" + entry.getValue());
                    }else if(count == 2){
                        tableItem.put("womanKeyword2", entry.getKey() + ":" + entry.getValue());
                    }else if(count == 3){
                        tableItem.put("womanKeyword3", entry.getKey() + ":" + entry.getValue());
                    }else{
                        break;
                    }
                    count ++;
                }
            }
            tableItemList.add(tableItem);
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("tableItemList", tableItemList);
        resultVo.setRtnData(resultJson);
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/getUnderwriteList")
    public ResultVo getUnderwriteList(DashboardParamVo paramVo) {
        ResultVo resultVo = new ResultVo();
        initDashboardParam(paramVo);

        UnderwriteParamPagerVo paramPagerVo = new UnderwriteParamPagerVo();
        paramPagerVo.setChooseDate(paramVo.getChooseDate());
        paramPagerVo.setMerchantId(paramVo.getMerchantId());
        paramPagerVo.setPlatformName(paramVo.getPlatformName());
        paramPagerVo.setAdvertiseActive(paramVo.getAdvertiseActive());
        paramPagerVo.setDateField("reserveDate");
        List<Underwrite> underwriteList = underwriteService.findListByParam(paramPagerVo);

        Set<String> insuranceNoList = new HashSet<>();
        for (Underwrite underwrite : underwriteList) {
            insuranceNoList.add(underwrite.getInsuranceNo());
        }
        Map<String, Repellent> repellentMap = new HashMap<>();
        List<Repellent> repellentList = repellentService.findListByInsuranceNoList(insuranceNoList);
        for (Repellent repellent : repellentList) {
            repellentMap.put(repellent.getInsuranceNo(), repellent);
        }

        int indexNo = 0;
        JSONObject tableItem;
        List<JSONObject> tableItemList = new ArrayList<>();
        for (Underwrite underwrite : underwriteList) {
            indexNo ++;
            tableItem = new JSONObject();
            tableItem.put("indexNo", indexNo);
            tableItem.put("mobile", underwrite.getReserveMobile());
            tableItem.put("merchantName", underwrite.getMerchantId());
            tableItem.put("platformName", underwrite.getPlatformName());
            tableItem.put("advertiseActive", underwrite.getAdvertiseActive());
            tableItem.put("reserveDate", underwrite.getReserveDate());
            tableItem.put("finishDate", underwrite.getFinishDate());
            tableItem.put("insuranceFee", underwrite.getInsuranceFee());
            if(repellentMap.get(underwrite.getInsuranceNo()) == null){
                tableItem.put("isRepellent", "否");
            } else {
                tableItem.put("isRepellent", "是");
            }
            tableItemList.add(tableItem);
        }
        JSONObject resultJson = new JSONObject();
        resultJson.put("tableItemList", tableItemList);
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
    private Map<Integer, Double> initDoubleAgeMap(){
        Map<Integer, Double> ageMap = new HashMap<>(9);
        ageMap.put(AgePartEnum.from_0_5.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_5_18.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_18_25.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_25_30.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_30_35.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_35_40.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_40_50.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_50_55.getValue(), 0.00);
        ageMap.put(AgePartEnum.from_55_100.getValue(), 0.00);
        return ageMap;
    }
}
