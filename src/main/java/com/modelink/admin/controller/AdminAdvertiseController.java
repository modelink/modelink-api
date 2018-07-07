package com.modelink.admin.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.AdvertiseAnalyseVo;
import com.modelink.admin.vo.AdvertiseParamPagerVo;
import com.modelink.common.annotation.ExportField;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.excel.ExcelExportConfigation;
import com.modelink.common.excel.ExcelExportHelper;
import com.modelink.common.excel.ExcelImportConfigation;
import com.modelink.common.excel.ExcelImportHelper;
import com.modelink.common.utils.ClassReflectUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.AdvertiseAnalyse;
import com.modelink.reservation.service.AdvertiseAnalyseService;
import com.modelink.usercenter.bean.Merchant;
import com.modelink.usercenter.service.MerchantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/admin/advertise")
public class AdminAdvertiseController {

    public static Logger logger = LoggerFactory.getLogger(AdminAdvertiseController.class);

    public static String yyyyMMddFormat = "yyyy-MM-dd";
    public static String yyyyMMddHHmmssFormat = "yyyy-MM-dd HH:mm:ss";
    public static String dateContant = "2000-01-01 00:00:00";

    @Resource
    private MerchantService merchantService;

    @Resource
    private AdvertiseAnalyseService advertiseAnalyseService;

    @RequestMapping
    public String index(){
        return "/admin/advertise/list";
    }

    @RequestMapping("/echart")
    public String echart() {
        return "/admin/advertise/echart";
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<AdvertiseAnalyseVo> list(AdvertiseParamPagerVo paramPagerVo) {
        PageInfo<AdvertiseAnalyse> pageInfo = advertiseAnalyseService.findPagerByParam(paramPagerVo);
        LayuiResultPagerVo<AdvertiseAnalyseVo> layuiResultPagerVo = new LayuiResultPagerVo();

        List<AdvertiseAnalyse> advertiseAnalyseList = pageInfo.getList();
        List<AdvertiseAnalyseVo> advertiseAnalyseVoList = transformBean2VoList(advertiseAnalyseList);

        layuiResultPagerVo.setTotalCount((int)pageInfo.getTotal());
        layuiResultPagerVo.setRtnList(advertiseAnalyseVoList);
        return layuiResultPagerVo;
    }

    @ResponseBody
    @RequestMapping("/importExcel")
    public ResultVo importExcel(@RequestParam MultipartFile file){
        ResultVo resultVo = new ResultVo();
        List<List<String>> dataList;
        ExcelImportConfigation configation = new ExcelImportConfigation();
        try {
            Map<Integer, String> fieldFormatMap = new HashMap<>();
            fieldFormatMap.put(16, "HH:mm:ss");

            configation = new ExcelImportConfigation();
            configation.setFieldFormatMap(fieldFormatMap);
            configation.setStartRowNum(1);
            dataList = ExcelImportHelper.importExcel(configation, file.getInputStream());
        } catch (Exception e) {
            logger.error("[adminAdvertiseController|importExcel]发生异常", e);
            resultVo.setRtnCode(RetStatus.Exception.getValue());
            resultVo.setRtnMsg(e.getMessage());
            dataList = null;
        }
        if(dataList == null){
            return resultVo;
        }

        if(dataList.size() <= 0){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("excel中没有符合条件的数据");
            return resultVo;
        }


        // 校验Excel数据是否符合规定
        boolean isFullNull;
        StringBuilder messageBuilder = new StringBuilder();
        int rowIndex = configation.getStartRowNum();
        for(List<String> dataItem : dataList){
            if(dataItem.size() < 22){
                messageBuilder.append("第").append(rowIndex).append("行：数据不足").append(";");
            }
            isFullNull = true;
            for(String dataString : dataItem){
                if(StringUtils.hasText(dataString)){
                    isFullNull = false;
                }
            }
            if(isFullNull){
                continue;
            }
            // 校验业务数据是否符合规定
            if(StringUtils.isEmpty(dataItem.get(0))){
                messageBuilder.append("第").append(rowIndex).append("行：项目字段不能为空").append(";");
            }
            if(StringUtils.isEmpty(dataItem.get(1))){
                messageBuilder.append("第").append(rowIndex).append("行：日期字段不能为空").append(";");
            }
            if(StringUtils.isEmpty(dataItem.get(2))){
                messageBuilder.append("第").append(rowIndex).append("行：预约渠道不能为空").append(";");
            }
            if(StringUtils.isEmpty(dataItem.get(3))){
                messageBuilder.append("第").append(rowIndex).append("行：预约明细不能为空").append(";");
            }

            rowIndex ++;
        }
        if(StringUtils.hasText(messageBuilder.toString())){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(messageBuilder.toString());
            return resultVo;
        }

        // 数据入库
        AdvertiseAnalyse advertiseAnalyse;
        Merchant merchant;
        Date statTime;
        for(List<String> dataItem : dataList){

            // 跳过空行
            isFullNull = true;
            for(String dataString : dataItem){
                if(StringUtils.hasText(dataString)){
                    isFullNull = false;
                }
            }
            if(isFullNull){
                continue;
            }
            try {
                // 重复数据校验
                advertiseAnalyse = new AdvertiseAnalyse();

                statTime = DateUtils.formatDate(dataItem.get(1), yyyyMMddFormat);
                advertiseAnalyse.setStatTime(statTime);

                merchant = merchantService.findByName(dataItem.get(0));
                advertiseAnalyse.setMerchantId(merchant == null ? 0L : merchant.getId());
                advertiseAnalyse.setPlatform(dataItem.get(2));
                advertiseAnalyse.setDataType(dataItem.get(3));
                advertiseAnalyse = advertiseAnalyseService.findOneByParam(advertiseAnalyse);
                if(advertiseAnalyse != null){
                    continue;
                }

                // 保存数据
                advertiseAnalyse = new AdvertiseAnalyse();

                advertiseAnalyse.setMerchantId(merchant == null ? 0L : merchant.getId());
                // 统计日期
                advertiseAnalyse.setStatTime(statTime);
                // 渠道归属
                advertiseAnalyse.setPlatform(dataItem.get(2));
                // 渠道明细
                advertiseAnalyse.setDataType(dataItem.get(3));

                advertiseAnalyse.setViewCount(tranform2Integer(dataItem.get(5)));
                advertiseAnalyse.setClickCount(tranform2Integer(dataItem.get(6)));
                advertiseAnalyse.setBrowseCount(tranform2Integer(dataItem.get(10)));
                advertiseAnalyse.setArriveCount(tranform2Integer(dataItem.get(11)));
                advertiseAnalyse.setArriveUserCount(tranform2Integer(dataItem.get(12)));
                advertiseAnalyse.setArriveRate(tranform2BigDecimal(dataItem.get(13)));
                advertiseAnalyse.setAgainCount(tranform2Integer(dataItem.get(14)));
                advertiseAnalyse.setAgainRate(tranform2BigDecimal(dataItem.get(15)));
                advertiseAnalyse.setAverageStayTime(dataItem.get(16));
                advertiseAnalyse.setTransformCount(tranform2Integer(dataItem.get(17)));
                advertiseAnalyse.setDirectTransformCount(tranform2Integer(dataItem.get(18)));
                advertiseAnalyse.setBackTransformCount(tranform2Integer(dataItem.get(19)));
                advertiseAnalyse.setTransformCost(tranform2BigDecimal(dataItem.get(20)));
                advertiseAnalyse.setInsuranceFee(tranform2BigDecimal(dataItem.get(21)));

                advertiseAnalyseService.insert(advertiseAnalyse);
            } catch (Exception e) {
                logger.error("[adminAdvertiseController|importExcel]保存数据发生异常。advertiseAnalyse={}", JSON.toJSONString(dataItem), e);
            }

        }

        return resultVo;
    }

    @RequestMapping("/exportExcel")
    public void download(AdvertiseParamPagerVo paramPagerVo, HttpServletResponse response) throws Exception {
        // 创建文件名称
        String fileName = "广告数据列表_" + DateUtils.formatDate(new Date(), "yyyy-MM-dd");

        // 创建Excel表格列名称
        ExportField exportField;
        String[] fieldIds = new String[]{};
        List<String> columnNameList = new ArrayList<>();
        if(StringUtils.hasText(paramPagerVo.getColumnFieldIds())){
            fieldIds = paramPagerVo.getColumnFieldIds().split(",");
            for(String fieldId : fieldIds){
                if(StringUtils.hasText(fieldId)){
                    exportField = ClassReflectUtils.getAnnotationByFieldName(fieldId, AdvertiseAnalyseVo.class);
                    columnNameList.add(exportField.value());
                }
            }
        }

        // 转换到VO列表
        List<AdvertiseAnalyse> advertiseAnalyseList = advertiseAnalyseService.findListByParam(paramPagerVo);
        List<AdvertiseAnalyseVo> advertiseAnalyseVoList = transformBean2VoList(advertiseAnalyseList);

        // 创建Excel 数据
        Object fieldValue;
        List<String> rowValueList;
        List<List<String>> dataList = new ArrayList<>();
        for(AdvertiseAnalyseVo advertiseAnalyseVo : advertiseAnalyseVoList){
            rowValueList = new ArrayList<>();
            for(String fieldId : fieldIds){
                if(StringUtils.hasText(fieldId)) {
                    fieldValue = ClassReflectUtils.getValueByFieldName(fieldId, advertiseAnalyseVo, AdvertiseAnalyseVo.class);
                    rowValueList.add(fieldValue == null ? "" : fieldValue.toString());
                }
            }
            dataList.add(rowValueList);
        }

        ExcelExportConfigation excelConfigation = ExcelExportConfigation.newInstance(fileName, columnNameList, dataList);
        ExcelExportHelper.exportExcel2Response(excelConfigation, response);
    }

    private int tranform2Integer(String varchar){
        if(StringUtils.hasText(varchar)){
            return Integer.parseInt(varchar);
        }
        return 0;
    }
    private BigDecimal tranform2BigDecimal(String varchar){
        if(StringUtils.hasText(varchar)){
            return new BigDecimal(varchar);
        }
        return new BigDecimal("0.00");
    }
    private List<AdvertiseAnalyseVo> transformBean2VoList(List<AdvertiseAnalyse> advertiseAnalyseList){
        Merchant merchant;
        AdvertiseAnalyseVo advertiseAnalyseVo;
        List<AdvertiseAnalyseVo> advertiseAnalyseVoList = new ArrayList<>();
        for(AdvertiseAnalyse advertiseAnalyse : advertiseAnalyseList){
            merchant = merchantService.findById(advertiseAnalyse.getMerchantId());
            advertiseAnalyseVo = new AdvertiseAnalyseVo();
            BeanUtils.copyProperties(advertiseAnalyse, advertiseAnalyseVo);
            advertiseAnalyseVo.setDataTypeName(advertiseAnalyse.getDataType());
            advertiseAnalyseVo.setMerchantName(merchant == null ? "" : merchant.getName());
            advertiseAnalyseVo.setPlatformName(advertiseAnalyse.getPlatform());

            advertiseAnalyseVo.setStatTime(DateUtils.formatDate(advertiseAnalyse.getStatTime(), "yyyy-MM-dd"));
            advertiseAnalyseVo.setArriveRate(advertiseAnalyse.getArriveRate().multiply(new BigDecimal(100)) + "%");
            advertiseAnalyseVo.setAgainRate(advertiseAnalyse.getAgainRate().multiply(new BigDecimal(100)).toPlainString() + "%");
            advertiseAnalyseVo.setTransformCost(advertiseAnalyse.getTransformCost().toPlainString());
            advertiseAnalyseVo.setInsuranceFee(advertiseAnalyse.getInsuranceFee().toPlainString());

            advertiseAnalyseVo.setCreateTime(DateUtils.formatDate(advertiseAnalyse.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            advertiseAnalyseVo.setUpdateTime(DateUtils.formatDate(advertiseAnalyse.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            advertiseAnalyseVoList.add(advertiseAnalyseVo);
        }
        return advertiseAnalyseVoList;
    }

}