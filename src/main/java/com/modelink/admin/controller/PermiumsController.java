package com.modelink.admin.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.bean.ExceptionLogger;
import com.modelink.admin.service.ExceptionLoggerService;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.excel.ExcelImportConfigation;
import com.modelink.common.excel.ExcelImportHelper;
import com.modelink.common.utils.DataUtils;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.Permiums;
import com.modelink.reservation.service.PermiumsService;
import com.modelink.reservation.vo.PermiumsParamPagerVo;
import com.modelink.reservation.vo.PermiumsVo;
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
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

/**
 * 保费数据Controller
 */
@Controller
@RequestMapping("/admin/permiums")
public class PermiumsController {

    public static Logger logger = LoggerFactory.getLogger(PermiumsController.class);

    @Resource
    private MerchantService merchantService;
    @Resource
    private PermiumsService permiumsService;
    @Resource
    private ExceptionLoggerService exceptionLoggerService;

    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/permiums/permiums-list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<PermiumsVo> list(PermiumsParamPagerVo paramPagerVo){
        LayuiResultPagerVo<PermiumsVo> layuiResultPagerVo = new LayuiResultPagerVo<>();

        PageInfo<Permiums> pageInfo = permiumsService.findPagerByParam(paramPagerVo);
        List<Permiums> permiumsList = pageInfo.getList();
        List<PermiumsVo> advertiseAnalyseVoList = transformBean2VoList(permiumsList);

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
            if(StringUtils.isEmpty(fileName) || !fileName.startsWith("合作方提供的保费数据表")){
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("您导入表格不是合作方提供的保费数据表");
                return resultVo;
            }

            Map<Integer, String> fieldFormatMap = new HashMap<>();
            fieldFormatMap.put(0, "M月d日");

            configation = new ExcelImportConfigation();
            configation.setFieldFormatMap(fieldFormatMap);
            configation.setStartRowNum(1);
            dataList = ExcelImportHelper.importExcel(configation, file.getInputStream());
        } catch (Exception e) {
            logger.error("[permiumsController|importExcel]发生异常", e);
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
        Permiums permiums;
        int totalCount = 0;
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

                Merchant merchant = merchantService.findByName(dataItem.get(1));
                // 重复数据校验
                permiums = new Permiums();
                permiums.setDate(dataItem.get(2));
                permiums.setConsumeAmount(dataItem.get(6));
                permiums.setInsuranceFee(dataItem.get(10));
                permiums = permiumsService.findOneByParam(permiums);
                if(permiums == null){
                    exist = false;
                    permiums = new Permiums();
                }else{
                    logger.info("[permiumsController|importExcel]重复数据{}", JSON.toJSONString(permiums));
                    ExceptionLogger exceptionLogger = new ExceptionLogger();
                    exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据重复");
                    exceptionLogger.setLoggerType("permiums");
                    exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                    exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                    exceptionLoggerService.save(exceptionLogger);
                }

                // 保存数据
                permiums.setDate(dataItem.get(2));
                permiums.setMerchantId(merchant == null ? 0 : merchant.getId());
                permiums.setValidCount(DataUtils.tranform2Integer(dataItem.get(3)));
                permiums.setTransformCount(DataUtils.tranform2Integer(dataItem.get(4)));
                permiums.setTransformCountNowx(DataUtils.tranform2Integer(dataItem.get(5)));
                permiums.setConsumeAmount(dataItem.get(6));
                permiums.setDirectTransformCost(dataItem.get(7));
                permiums.setTotalTransformCost(dataItem.get(8));
                permiums.setInsuranceCount(DataUtils.tranform2Integer(dataItem.get(9)));
                permiums.setInsuranceFee(dataItem.get(10));

                if(exist) {
                    permiumsService.update(permiums);
                }else {
                    permiumsService.insert(permiums);
                    totalCount ++;
                }

            } catch (Exception e) {
                logger.error("[permiumsController|importExcel]保存数据发生异常。permiums={}", JSON.toJSONString(dataItem), e);
                ExceptionLogger exceptionLogger = new ExceptionLogger();
                exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据异常");
                exceptionLogger.setLoggerType("permiums");
                exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                exceptionLoggerService.save(exceptionLogger);
            }
        }
        resultVo.setRtnData(totalCount);
        return resultVo;
    }


    private List<PermiumsVo> transformBean2VoList(List<Permiums> permiumsList){
        Merchant merchant;
        PermiumsVo permiumsVo;
        List<PermiumsVo> permiumsVoList = new ArrayList<>();
        for(Permiums permiums : permiumsList){
            permiumsVo = new PermiumsVo();
            merchant = merchantService.findById(permiums.getMerchantId());
            BeanUtils.copyProperties(permiums, permiumsVo);
            permiumsVo.setMerchantName(merchant == null ? "" : merchant.getName());
            permiumsVo.setCreateTime(DateUtils.formatDate(permiums.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            permiumsVo.setUpdateTime(DateUtils.formatDate(permiums.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            permiumsVoList.add(permiumsVo);
        }
        return permiumsVoList;
    }
}
