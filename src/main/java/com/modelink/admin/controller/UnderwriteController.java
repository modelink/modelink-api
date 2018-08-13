package com.modelink.admin.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.modelink.common.enums.AreaTypeEnum;
import com.modelink.common.enums.InsurancePayTypeEnum;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.excel.ExcelImportConfigation;
import com.modelink.common.excel.ExcelImportHelper;
import com.modelink.common.utils.DataUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.Underwrite;
import com.modelink.reservation.service.UnderwriteService;
import com.modelink.reservation.vo.UnderwriteParamPagerVo;
import com.modelink.reservation.vo.UnderwriteVo;
import com.modelink.usercenter.bean.Area;
import com.modelink.usercenter.bean.Merchant;
import com.modelink.usercenter.service.AreaService;
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
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 承保效果数据Controller
 */
@Controller
@RequestMapping("/admin/underwrite")
public class UnderwriteController {

    public static Logger logger = LoggerFactory.getLogger(UnderwriteController.class);
    public static String yyyyMMddFormat = "yyyy-MM-dd";

    @Resource
    private AreaService areaService;
    @Resource
    private MerchantService merchantService;
    @Resource
    private UnderwriteService underwriteService;

    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/underwrite/list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<UnderwriteVo> list(UnderwriteParamPagerVo paramPagerVo){
        LayuiResultPagerVo<UnderwriteVo> layuiResultPagerVo = new LayuiResultPagerVo<>();

        PageInfo<Underwrite> pageInfo = underwriteService.findPagerByParam(paramPagerVo);
        List<Underwrite> underwriteList = pageInfo.getList();
        List<UnderwriteVo> advertiseAnalyseVoList = transformBean2VoList(underwriteList);

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
            //fieldFormatMap.put(16, "HH:mm:ss");

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
            if(dataItem.size() < 20){
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

            rowIndex ++;
        }
        if(StringUtils.hasText(messageBuilder.toString())){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(messageBuilder.toString());
            return resultVo;
        }

        // 数据入库
        Area area;
        Underwrite underwrite;
        Merchant merchant;
        String reserveDate;
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
                underwrite = new Underwrite();

                reserveDate = dataItem.get(9);
                underwrite.setReserveDate(reserveDate);
                merchant = merchantService.findByName(dataItem.get(1));
                underwrite.setMerchantId(merchant == null ? 0L : merchant.getId());
                underwrite.setPlatformName(dataItem.get(2));
                underwrite.setSourceType(dataItem.get(3));
                underwrite = underwriteService.findOneByParam(underwrite);
                if(underwrite != null){
                    continue;
                }

                // 保存数据
                underwrite = new Underwrite();

                underwrite.setMerchantId(merchant == null ? 0L : merchant.getId());
                // 渠道归属
                underwrite.setPlatformName(dataItem.get(2));
                // 渠道明细
                underwrite.setSourceType(dataItem.get(3));

                underwrite.setProductName(dataItem.get(4));

                underwrite.setOrgName(dataItem.get(5));
                underwrite.setInsuranceNo(dataItem.get(6));
                underwrite.setReserveMobile(dataItem.get(7));
                underwrite.setThirdSourceType(dataItem.get(8));
                underwrite.setReserveDate(dataItem.get(9));
                underwrite.setFinishDate(dataItem.get(10));
                underwrite.setPayType(InsurancePayTypeEnum.getValueByText(dataItem.get(11)));
                underwrite.setInsuranceAmount(dataItem.get(12));
                underwrite.setInsuranceFee(dataItem.get(13));
                underwrite.setGender(dataItem.get(14));
                underwrite.setBirthday(dataItem.get(15));
                underwrite.setAge(DataUtils.tranform2Integer(dataItem.get(16)));
                underwrite.setAddress(dataItem.get(17));
                // 查找省份数据
                area = areaService.findByNameAndType(dataItem.get(18), AreaTypeEnum.省.getValue());
                underwrite.setProvinceId(area == null ? 0 : area.getAreaId());
                // 查找城市数据
                area = areaService.findByNameAndType(dataItem.get(19), AreaTypeEnum.市.getValue());
                underwrite.setCityId(area == null ? 0 : area.getAreaId());

                underwriteService.insert(underwrite);
            } catch (Exception e) {
                logger.error("[adminAdvertiseController|importExcel]保存数据发生异常。underwrite={}", JSON.toJSONString(dataItem), e);
            }

        }

        return resultVo;
    }


    private List<UnderwriteVo> transformBean2VoList(List<Underwrite> underwriteList){
        Merchant merchant;
        UnderwriteVo underwriteVo;
        List<UnderwriteVo> underwriteVoList = new ArrayList<>();
        for(Underwrite underwrite : underwriteList){
            merchant = merchantService.findById(underwrite.getMerchantId());
            underwriteVo = new UnderwriteVo();
            BeanUtils.copyProperties(underwrite, underwriteVo);
            underwriteVo.setMerchantName(merchant == null ? "" : merchant.getName());
            underwriteVo.setCreateTime(DateUtils.formatDate(underwrite.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            underwriteVo.setUpdateTime(DateUtils.formatDate(underwrite.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            underwriteVoList.add(underwriteVo);
        }
        return underwriteVoList;
    }
}