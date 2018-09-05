package com.modelink.admin.controller.basedata;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.InsuranceParamPagerVo;
import com.modelink.admin.vo.InsuranceVo;
import com.modelink.common.annotation.ExportField;
import com.modelink.common.enums.InsurancePayTypeEnum;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.excel.ExcelExportConfigation;
import com.modelink.common.excel.ExcelExportHelper;
import com.modelink.common.excel.ExcelImportConfigation;
import com.modelink.common.excel.ExcelImportHelper;
import com.modelink.common.utils.ClassReflectUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.Insurance;
import com.modelink.reservation.service.InsuranceService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/insurance")
public class AdminInsuranceController {

    public static Logger logger = LoggerFactory.getLogger(AdminInsuranceController.class);

    public static String yyyyMMddFormat = "yyyy-MM-dd";
    public static String yyyyMMddHHmmssFormat = "yyyy-MM-dd HH:mm:ss";
    public static String dateContant = "2000-01-01 00:00:00";

    @Resource
    private MerchantService merchantService;

    @Resource
    private InsuranceService insuranceService;

    @RequestMapping
    public String index(){
        return "/admin/insurance/list";
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<InsuranceVo> list(InsuranceParamPagerVo paramPagerVo) {
        PageInfo<Insurance> pageInfo = insuranceService.findPagerByParam(paramPagerVo);
        LayuiResultPagerVo<InsuranceVo> layuiResultPagerVo = new LayuiResultPagerVo();

        List<Insurance> insuranceList = pageInfo.getList();
        List<InsuranceVo> insuranceVoList = transformBean2VoList(insuranceList);

        layuiResultPagerVo.setTotalCount((int)pageInfo.getTotal());
        layuiResultPagerVo.setRtnList(insuranceVoList);
        return layuiResultPagerVo;
    }

    @ResponseBody
    @RequestMapping("/importExcel")
    public ResultVo importExcel(@RequestParam MultipartFile file){
        ResultVo resultVo = new ResultVo();
        List<List<String>> dataList;
        ExcelImportConfigation configation = new ExcelImportConfigation();
        try {
            configation = new ExcelImportConfigation();
            configation.setStartRowNum(1);
            dataList = ExcelImportHelper.importExcel(configation, file.getInputStream());
        } catch (Exception e) {
            logger.error("[adminInsuranceController|importExcel]发生异常", e);
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
            if(dataItem.size() < 23){
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
                messageBuilder.append("第").append(rowIndex).append("行：预约日期不能为空").append(";");
            }
            if(StringUtils.isEmpty(dataItem.get(4))){
                messageBuilder.append("第").append(rowIndex).append("行：预约电话不能为空").append(";");
            }

            rowIndex ++;
        }
        if(StringUtils.hasText(messageBuilder.toString())){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(messageBuilder.toString());
            return resultVo;
        }

        // 数据入库
        Insurance insurance;
        Merchant merchant;
        String insuranceFee, insuranceAmount, insuranceCount;
        Date contactTime, arrangeTime, finishTime, birthday;
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

            contactTime = DateUtils.formatDate(dataItem.get(1), yyyyMMddFormat);
            // 重复数据校验
            insurance = new Insurance();
            insurance.setContactTime(contactTime);
            insurance.setMobile(dataItem.get(4));
            insurance = insuranceService.findOneByParam(insurance);
            if(insurance != null){
                continue;
            }

            // 保存数据
            insurance = new Insurance();
            // 合作商
            merchant = merchantService.findByName(dataItem.get(0));
            insurance.setMerchantId(merchant == null ? 0L : merchant.getId());
            // 预约日期
            insurance.setContactTime(contactTime);
            // 渠道归属
            insurance.setPlatform(dataItem.get(2));
            // 渠道明细
            insurance.setDataType(dataItem.get(3));
            // 投保人电话
            insurance.setMobile(dataItem.get(4));
            // 下发日期
            arrangeTime = null;
            if(StringUtils.hasText(dataItem.get(5))){
                arrangeTime = DateUtils.formatDate(dataItem.get(5), yyyyMMddFormat);
            }
            insurance.setArrangeTime(arrangeTime);
            // 机构名称
            insurance.setOrgName(dataItem.get(6));
            // 客服姓名
            insurance.setTsrName(dataItem.get(7));
            // 第一天电话
            insurance.setFirstCall(dataItem.get(8));
            // 第二天电话
            insurance.setSecondCall(dataItem.get(9));
            // 第三天电话
            insurance.setThreeCall(dataItem.get(10));
            // 拨打状态
            insurance.setCallStatus(dataItem.get(11));
            // 问题数据
            insurance.setProblem("是".equals(dataItem.get(12)));
            // 成单时间
            finishTime = null;
            if(StringUtils.hasText(dataItem.get(13))){
                finishTime = DateUtils.formatDate(dataItem.get(13), yyyyMMddFormat);
            }
            insurance.setFinishTime(finishTime);
            // 投保人性别
            insurance.setGender(dataItem.get(14));
            // 投保人生日
            birthday = null;
            if(StringUtils.hasText(dataItem.get(15))) {
                birthday = DateUtils.formatDate(dataItem.get(15), yyyyMMddFormat);
            }
            insurance.setBirthday(birthday);
            // 投保人年龄
            if(birthday != null){
                insurance.setAge(DateUtils.getAgeByBirthday(birthday));
            }else{
                insurance.setAge(0);
            }
            // 投保人地址
            insurance.setAddress(dataItem.get(17));
            // 缴费类型
            insurance.setPayType(InsurancePayTypeEnum.getValueByText(dataItem.get(18)));
            // 保额
            insuranceAmount = dataItem.get(19);
            insurance.setInsuranceAmount(StringUtils.hasText(insuranceAmount) ? new BigDecimal(insuranceAmount) : new BigDecimal("0"));
            // 件数
            insuranceCount = dataItem.get(20);
            insurance.setInsuranceCount(StringUtils.hasText(insuranceCount) ? Integer.parseInt(insuranceCount) : 0);
            // 保费
            insuranceFee = dataItem.get(21);
            insurance.setInsuranceFee(StringUtils.hasText(insuranceFee) ? new BigDecimal(insuranceFee) : new BigDecimal("0"));
            // 保单编号
            insurance.setInsuranceNo(dataItem.get(22));
            try {
                insuranceService.insert(insurance);
            } catch (Exception e) {
                logger.error("[adminInsuranceController|importExcel]保存数据发生异常。insurance={}", JSON.toJSONString(insurance), e);
            }

        }

        return resultVo;
    }

    @RequestMapping("/exportExcel")
    public void download(InsuranceParamPagerVo paramPagerVo, HttpServletResponse response) throws Exception {
        // 创建文件名称
        String fileName = "效果数据列表_" + DateUtils.formatDate(new Date(), "yyyy-MM-dd");

        // 创建Excel表格列名称
        ExportField exportField;
        String[] fieldIds = new String[]{};
        List<String> columnNameList = new ArrayList<>();
        if(StringUtils.hasText(paramPagerVo.getColumnFieldIds())){
            fieldIds = paramPagerVo.getColumnFieldIds().split(",");
            for(String fieldId : fieldIds){
                if(StringUtils.hasText(fieldId)){
                    exportField = ClassReflectUtils.getAnnotationByFieldName(fieldId, InsuranceVo.class);
                    columnNameList.add(exportField.value());
                }
            }
        }

        // 转换到VO列表
        List<Insurance> insuranceList = insuranceService.findListByParam(paramPagerVo);
        List<InsuranceVo> insuranceVoList = transformBean2VoList(insuranceList);

        // 创建Excel 数据
        Object fieldValue;
        List<String> rowValueList;
        List<List<String>> dataList = new ArrayList<>();
        for(InsuranceVo insuranceVo : insuranceVoList){
            rowValueList = new ArrayList<>();
            for(String fieldId : fieldIds){
                if(StringUtils.hasText(fieldId)) {
                    fieldValue = ClassReflectUtils.getValueByFieldName(fieldId, insuranceVo, InsuranceVo.class);
                    rowValueList.add(fieldValue == null ? "" : fieldValue.toString());
                }
            }
            dataList.add(rowValueList);
        }

        ExcelExportConfigation excelConfigation = ExcelExportConfigation.newInstance(fileName, columnNameList, dataList);
        ExcelExportHelper.exportExcel2Response(excelConfigation, response);
    }


    private List<InsuranceVo> transformBean2VoList(List<Insurance> insuranceList){
        Merchant merchant;
        InsuranceVo insuranceVo;
        String birthday, contactTime, finishTime, arrangeTime;
        List<InsuranceVo> insuranceVoList = new ArrayList<>();
        for(Insurance insurance : insuranceList){
            merchant = merchantService.findById(insurance.getMerchantId());
            insuranceVo = new InsuranceVo();
            BeanUtils.copyProperties(insurance, insuranceVo);
            // 字段格式化
            insuranceVo.setAddress("");

            birthday = DateUtils.formatDate(insurance.getBirthday(), yyyyMMddHHmmssFormat);
            if(dateContant.equals(birthday)){
                insuranceVo.setBirthday("");
            }else{
                birthday = DateUtils.formatDate(insurance.getBirthday(), yyyyMMddFormat);
                insuranceVo.setBirthday(birthday);
            }
            contactTime = DateUtils.formatDate(insurance.getContactTime(), yyyyMMddHHmmssFormat);
            if(dateContant.equals(contactTime)){
                insuranceVo.setContactTime("");
            }else{
                contactTime = DateUtils.formatDate(insurance.getContactTime(), yyyyMMddFormat);
                insuranceVo.setContactTime(contactTime);
            }
            finishTime = DateUtils.formatDate(insurance.getFinishTime(), yyyyMMddHHmmssFormat);
            if(dateContant.equals(finishTime)){
                insuranceVo.setFinishTime("");
            }else{
                finishTime = DateUtils.formatDate(insurance.getFinishTime(), yyyyMMddFormat);
                insuranceVo.setFinishTime(finishTime);
            }
            arrangeTime = DateUtils.formatDate(insurance.getArrangeTime(), yyyyMMddHHmmssFormat);
            if(dateContant.equals(arrangeTime)){
                insuranceVo.setArrangeTime("");
            }else{
                arrangeTime = DateUtils.formatDate(insurance.getArrangeTime(), yyyyMMddFormat);
                insuranceVo.setArrangeTime(arrangeTime);
            }

            insuranceVo.setDataTypeName(insurance.getDataType());
            insuranceVo.setMerchantName(merchant == null ? "" : merchant.getName());
            insuranceVo.setPlatformName(insurance.getPlatform());
            insuranceVo.setPayTypeName(InsurancePayTypeEnum.getTextByValue(insurance.getPayType()));
            insuranceVo.setSourceTypeName(String.valueOf(insurance.getSourceType()));

            insuranceVo.setCreateTime(DateUtils.formatDate(insurance.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            insuranceVo.setUpdateTime(DateUtils.formatDate(insurance.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));

            insuranceVoList.add(insuranceVo);
        }
        return insuranceVoList;
    }

}