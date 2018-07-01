package com.modelink.admin.controller;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.InsuranceParamPagerVo;
import com.modelink.admin.vo.InsuranceVo;
import com.modelink.common.enums.InsuranceDataTypeEnum;
import com.modelink.common.enums.InsurancePayTypeEnum;
import com.modelink.common.enums.PlatformEnum;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.excel.ExcelConfigation;
import com.modelink.common.excel.ExcelExportHelper;
import com.modelink.common.excel.ExcelImportHelper;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.Insurance;
import com.modelink.reservation.enums.ResourceTypeEnum;
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

    @Resource
    private MerchantService merchantService;

    @Resource
    private InsuranceService insuranceService;

    @RequestMapping
    public String index(){
        return "/admin/insurance/list";
    }

    @RequestMapping("/echart")
    public String echart() {
        return "/admin/insurance/echart";
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<InsuranceVo> list(InsuranceParamPagerVo paramPagerVo) {
        PageInfo<Insurance> pageInfo = insuranceService.findPagerByParam(paramPagerVo);
        LayuiResultPagerVo<InsuranceVo> layuiResultPagerVo = new LayuiResultPagerVo();

        Merchant merchant;
        InsuranceVo insuranceVo;
        List<InsuranceVo> insuranceVoList = new ArrayList<>();
        List<Insurance> insuranceList = pageInfo.getList();
        for(Insurance insurance : insuranceList){
            merchant = merchantService.findByAppKey(insurance.getMerchantId());
            insuranceVo = new InsuranceVo();
            BeanUtils.copyProperties(insurance, insuranceVo);
            // 字段格式化
            insuranceVo.setAddress("");
            insuranceVo.setDataTypeName(InsuranceDataTypeEnum.getTextByValue(insurance.getDataType()));
            insuranceVo.setMerchantName(merchant == null ? "" : merchant.getName());
            insuranceVo.setPlatformName(PlatformEnum.getTextByValue(insurance.getPlatform()));
            insuranceVo.setPayTypeName(InsurancePayTypeEnum.getTextByValue(insurance.getPayType()));
            insuranceVoList.add(insuranceVo);
        }

        layuiResultPagerVo.setTotalCount(pageInfo.getSize());
        layuiResultPagerVo.setRtnList(insuranceVoList);
        return layuiResultPagerVo;
    }

    @ResponseBody
    @RequestMapping("/importExcel")
    public ResultVo importExcel(@RequestParam MultipartFile file){
        ResultVo resultVo = new ResultVo();
        List<List<String>> dataList;
        ExcelConfigation configation = new ExcelConfigation();
        try {
            configation = new ExcelConfigation();
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
        List<Insurance> insuranceList = new ArrayList<>();
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

            contactTime = DateUtils.formatDate(dataItem.get(1), "yyyy/M/d");
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
            insurance.setMerchantId(1L);
            // 预约日期
            insurance.setContactTime(contactTime);
            // 渠道归属
            insurance.setPlatform(PlatformEnum.getValueByText(dataItem.get(2)));
            // 渠道明细
            insurance.setDataType(InsuranceDataTypeEnum.getValueByText(dataItem.get(3)));
            // 投保人电话
            insurance.setMobile(dataItem.get(4));
            // 下发日期
            arrangeTime = DateUtils.formatDate(dataItem.get(5), "yyyy/M/d");
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
            insurance.setProblem("shi".equals(dataItem.get(12)));
            // 成单时间
            finishTime = DateUtils.formatDate(dataItem.get(13), "yyyy/M/d");
            insurance.setFinishTime(finishTime);
            // 缴费类型
            insurance.setPayType(InsurancePayTypeEnum.getValueByText(dataItem.get(14)));
            // 保额
            insurance.setInsuranceAmount(new BigDecimal(dataItem.get(15)));
            // 件数
            insurance.setInsuranceCount(Integer.parseInt(dataItem.get(16)));
            // 保费
            insurance.setInsuranceFee(new BigDecimal(dataItem.get(17)));
            // 投保人性别
            insurance.setGender(dataItem.get(18));
            // 投保人生日
            birthday = DateUtils.formatDate(dataItem.get(19), "yyyy/M/d");
            insurance.setBirthday(birthday);
            // 投保人年龄
            insurance.setAge(Integer.parseInt(dataItem.get(20)));
            // 投保人地址
            insurance.setAddress(dataItem.get(21));
            // 保单编号
            insurance.setInsuranceNo(dataItem.get(22));
            insuranceService.insert(insurance);
        }

        return resultVo;
    }

    @RequestMapping("/exportExcel")
    public void download(InsuranceParamPagerVo paramPagerVo, HttpServletResponse response) throws Exception {
        // 创建文件名称
        String fileName = "投保明细列表_" + DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        // 创建Excel表格列名称
        List<String> columnNameList = new ArrayList<>();
        columnNameList.add("投保人姓名");
        columnNameList.add("投保人电话");
        columnNameList.add("预约时间");
        columnNameList.add("预约渠道");
        columnNameList.add("预约入口");
        columnNameList.add("创建时间");
        // 创建Excel 数据
        List<String> rowValueList;
        List<List<String>> dataList = new ArrayList<>();
        List<Insurance> insuranceList = insuranceService.findListByParam(paramPagerVo);
        for(Insurance insurance : insuranceList){
            rowValueList = new ArrayList<>();
            rowValueList.add(insurance.getName());
            rowValueList.add(insurance.getMobile());
            rowValueList.add(DateUtils.formatDate(insurance.getContactTime(), "yyyy-MM-dd"));
            rowValueList.add("小米渠道");
            rowValueList.add(ResourceTypeEnum.getTextByValue(insurance.getSourceType()));
            rowValueList.add(DateUtils.formatDate(insurance.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            dataList.add(rowValueList);
        }

        ExcelConfigation excelConfigation = ExcelConfigation.newInstance(fileName, columnNameList, dataList);
        ExcelExportHelper.exportExcel2Response(excelConfigation, response);
    }
}