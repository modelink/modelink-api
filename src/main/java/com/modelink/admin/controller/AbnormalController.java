package com.modelink.admin.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.excel.ExcelImportConfigation;
import com.modelink.common.excel.ExcelImportHelper;
import com.modelink.common.utils.DataUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.Abnormal;
import com.modelink.reservation.service.AbnormalService;
import com.modelink.reservation.vo.AbnormalParamPagerVo;
import com.modelink.reservation.vo.AbnormalVo;
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
 * 异常数据Controller
 */
@Controller
@RequestMapping("/admin/abnormal")
public class AbnormalController {

    public static Logger logger = LoggerFactory.getLogger(AbnormalController.class);

    @Resource
    private AbnormalService abnormalService;

    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/abnormal/abnormal-list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<AbnormalVo> list(AbnormalParamPagerVo paramPagerVo){
        LayuiResultPagerVo<AbnormalVo> layuiResultPagerVo = new LayuiResultPagerVo<>();

        PageInfo<Abnormal> pageInfo = abnormalService.findPagerByParam(paramPagerVo);
        List<Abnormal> abnormalList = pageInfo.getList();
        List<AbnormalVo> advertiseAnalyseVoList = transformBean2VoList(abnormalList);

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
            String fileName = file.getOriginalFilename();
            if(StringUtils.isEmpty(fileName) || !fileName.startsWith("异常数据表")){
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("您导入表格不是异常数据表");
                return resultVo;
            }

            Map<Integer, String> fieldFormatMap = new HashMap<>();
            fieldFormatMap.put(0, "M月d日");

            configation = new ExcelImportConfigation();
            configation.setFieldFormatMap(fieldFormatMap);
            configation.setStartRowNum(1);
            dataList = ExcelImportHelper.importExcel(configation, file.getInputStream());
        } catch (Exception e) {
            logger.error("[abnormalController|importExcel]发生异常", e);
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
            if(dataItem.size() < 15){
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
        boolean exist;
        Abnormal abnormal;
        for(List<String> dataItem : dataList){

            // 跳过空行
            exist = true;
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
                abnormal = new Abnormal();
                abnormal.setDate(dataItem.get(1));
                abnormal.setSource(dataItem.get(4));
                abnormal.setMobile(dataItem.get(5));
                abnormal = abnormalService.findOneByParam(abnormal);
                if(abnormal == null){
                    exist = false;
                    abnormal = new Abnormal();
                }

                // 保存数据
                abnormal.setDate(dataItem.get(1));
                abnormal.setOrgName(dataItem.get(2));
                abnormal.setTsrName(dataItem.get(3));
                abnormal.setSource(dataItem.get(4));
                abnormal.setMobile(dataItem.get(5));
                abnormal.setReserveDate(dataItem.get(6));
                abnormal.setArrangeDate(dataItem.get(7));
                abnormal.setCallDate(dataItem.get(8));
                abnormal.setCallResult(dataItem.get(9));
                abnormal.setLastResult(dataItem.get(10));
                abnormal.setProblemData(dataItem.get(11));
                if("-".trim().equals(dataItem.get(12))) {
                    abnormal.setCallCount(1);
                }else{
                    abnormal.setCallCount(DataUtils.tranform2Integer(dataItem.get(12)));
                }
                abnormal.setSourceMedia(dataItem.get(13));
                abnormal.setDeviceName(dataItem.get(14));

                if(exist) {
                    abnormalService.update(abnormal);
                }else {
                    abnormalService.insert(abnormal);
                }
            } catch (Exception e) {
                logger.error("[abnormalController|importExcel]保存数据发生异常。abnormal={}", JSON.toJSONString(dataItem), e);
            }
        }
        return resultVo;
    }


    private List<AbnormalVo> transformBean2VoList(List<Abnormal> abnormalList){
        AbnormalVo abnormalVo;
        List<AbnormalVo> abnormalVoList = new ArrayList<>();
        for(Abnormal abnormal : abnormalList){
            abnormalVo = new AbnormalVo();
            BeanUtils.copyProperties(abnormal, abnormalVo);
            abnormalVo.setCreateTime(DateUtils.formatDate(abnormal.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            abnormalVo.setUpdateTime(DateUtils.formatDate(abnormal.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            abnormalVoList.add(abnormalVo);
        }
        return abnormalVoList;
    }
}
