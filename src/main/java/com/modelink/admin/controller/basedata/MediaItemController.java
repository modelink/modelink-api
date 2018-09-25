package com.modelink.admin.controller.basedata;

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
import com.modelink.reservation.bean.MediaItem;
import com.modelink.reservation.service.MediaItemService;
import com.modelink.reservation.vo.MediaItemParamPagerVo;
import com.modelink.reservation.vo.MediaItemVo;
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
 * 承保效果数据Controller
 */
@Controller
@RequestMapping("/admin/mediaItem")
public class MediaItemController {

    public static Logger logger = LoggerFactory.getLogger(MediaItemController.class);

    @Resource
    private MerchantService merchantService;
    @Resource
    private MediaItemService mediaItemService;
    @Resource
    private ExceptionLoggerService exceptionLoggerService;

    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/media/media-item-list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<MediaItemVo> list(MediaItemParamPagerVo paramPagerVo) {
        LayuiResultPagerVo<MediaItemVo> layuiResultPagerVo = new LayuiResultPagerVo<>();

        PageInfo<MediaItem> pageInfo = mediaItemService.findPagerByParam(paramPagerVo);
        List<MediaItem> mediaItemList = pageInfo.getList();
        List<MediaItemVo> advertiseAnalyseVoList = transformBean2VoList(mediaItemList);

        layuiResultPagerVo.setTotalCount((int) pageInfo.getTotal());
        layuiResultPagerVo.setRtnList(advertiseAnalyseVoList);
        return layuiResultPagerVo;
    }

    @ResponseBody
    @RequestMapping("/importExcel")
    public ResultVo importExcel(@RequestParam MultipartFile file) {
        ResultVo resultVo = new ResultVo();
        List<List<String>> dataList;
        ExcelImportConfigation configation = new ExcelImportConfigation();
        try {
            String fileName = file.getOriginalFilename();
            if(StringUtils.isEmpty(fileName) || !fileName.startsWith("媒体数据表")){
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("您导入表格不是媒体数据表");
                return resultVo;
            }

            Map<Integer, String> fieldFormatMap = new HashMap<>();
            configation = new ExcelImportConfigation();
            configation.setFieldFormatMap(fieldFormatMap);
            configation.setStartRowNum(1);
            dataList = ExcelImportHelper.importExcel(configation, file.getInputStream());
        } catch (Exception e) {
            logger.error("[mediaItemController|importExcel]发生异常", e);
            resultVo.setRtnCode(RetStatus.Exception.getValue());
            resultVo.setRtnMsg(e.getMessage());
            dataList = null;
        }
        if (dataList == null) {
            return resultVo;
        }

        if (dataList.size() <= 0) {
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("excel中没有符合条件的数据");
            return resultVo;
        }


        // 校验Excel数据是否符合规定
        boolean isExist;
        boolean isFullNull;
        StringBuilder messageBuilder = new StringBuilder();
        int rowIndex = configation.getStartRowNum();
        for (List<String> dataItem : dataList) {
            if (dataItem.size() < 16) {
                messageBuilder.append("第").append(rowIndex).append("行：数据不足").append(";");
            }
            isFullNull = true;
            for (String dataString : dataItem) {
                if (StringUtils.hasText(dataString)) {
                    isFullNull = false;
                }
            }
            if (isFullNull) {
                continue;
            }

            rowIndex++;
        }
        if (StringUtils.hasText(messageBuilder.toString())) {
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(messageBuilder.toString());
            return resultVo;
        }

        // 数据入库
        MediaItem mediaItem;
        Merchant merchant;
        int totalCount = 0;
        for (List<String> dataItem : dataList) {

            // 跳过空行
            isExist = true;
            isFullNull = true;
            for (String dataString : dataItem) {
                if (StringUtils.hasText(dataString)) {
                    isFullNull = false;
                }
            }
            if (isFullNull) {
                continue;
            }
            try {
                merchant = merchantService.findByName(dataItem.get(2));
                // 重复数据校验
                mediaItem = new MediaItem();
                mediaItem.setDate(dataItem.get(1));
                mediaItem.setMerchantId(merchant == null ? 0L : merchant.getId());
                // 渠道归属
                mediaItem.setPlatformName(dataItem.get(3).toUpperCase());
                // 广告活动
                mediaItem.setAdvertiseActive(dataItem.get(4).toUpperCase());

                mediaItem.setAdvertiseMedia(dataItem.get(5));
                mediaItem.setAdvertiseSeries(dataItem.get(6));
                mediaItem.setKeyWordGroup(dataItem.get(7));
                mediaItem.setKeyWord(dataItem.get(8).toUpperCase());
                mediaItem.setShowCount(DataUtils.tranform2Integer(dataItem.get(9)));
                mediaItem.setClickCount(DataUtils.tranform2Integer(dataItem.get(10)));
                mediaItem.setSpeedCost(dataItem.get(11));
                mediaItem.setClickRate(dataItem.get(12));
                mediaItem.setAverageClickPrice(dataItem.get(13));
                mediaItem.setAverageRank(dataItem.get(14));
                mediaItem.setFeeType(dataItem.get(15));
                mediaItem = mediaItemService.findOneByParam(mediaItem);
                if (mediaItem == null) {
                    isExist = false;
                    mediaItem = new MediaItem();
                } else {
                    logger.info("[mediaItemController|importExcel]重复数据{}", JSON.toJSONString(dataItem));
                    ExceptionLogger exceptionLogger = new ExceptionLogger();
                    exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据重复");
                    exceptionLogger.setLoggerType("media-item");
                    exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                    exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                    exceptionLoggerService.save(exceptionLogger);
                }

                // 保存数据
                mediaItem.setDate(dataItem.get(1));
                mediaItem.setMerchantId(merchant == null ? 0L : merchant.getId());
                // 渠道归属
                mediaItem.setPlatformName(dataItem.get(3).toUpperCase());
                // 广告活动
                mediaItem.setAdvertiseActive(dataItem.get(4).toUpperCase());

                mediaItem.setAdvertiseMedia(dataItem.get(5));
                mediaItem.setAdvertiseSeries(dataItem.get(6));
                mediaItem.setKeyWordGroup(dataItem.get(7));
                mediaItem.setKeyWord(dataItem.get(8).toUpperCase());
                mediaItem.setShowCount(DataUtils.tranform2Integer(dataItem.get(9)));
                mediaItem.setClickCount(DataUtils.tranform2Integer(dataItem.get(10)));
                mediaItem.setSpeedCost(dataItem.get(11));
                mediaItem.setClickRate(dataItem.get(12));
                mediaItem.setAverageClickPrice(dataItem.get(13));
                mediaItem.setAverageRank(dataItem.get(14));
                mediaItem.setFeeType(dataItem.get(15));
                if (isExist) {
                    mediaItemService.update(mediaItem);
                } else {
                    mediaItemService.insert(mediaItem);
                    totalCount ++;
                }
            } catch (Exception e) {
                logger.error("[mediaItemController|importExcel]保存数据发生异常。mediaItem={}", JSON.toJSONString(dataItem), e);
                ExceptionLogger exceptionLogger = new ExceptionLogger();
                exceptionLogger.setLoggerKey(dataItem.get(0) + "行数据异常");
                exceptionLogger.setLoggerType("media-item");
                exceptionLogger.setLoggerDesc(JSON.toJSONString(dataItem));
                exceptionLogger.setLoggerDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                exceptionLoggerService.save(exceptionLogger);
            }

        }
        resultVo.setRtnData(totalCount);
        return resultVo;
    }


    private List<MediaItemVo> transformBean2VoList(List<MediaItem> mediaItemList) {
        Merchant merchant;
        MediaItemVo mediaItemVo;
        List<MediaItemVo> mediaItemVoList = new ArrayList<>();
        for (MediaItem mediaItem : mediaItemList) {
            merchant = merchantService.findById(mediaItem.getMerchantId());
            mediaItemVo = new MediaItemVo();
            BeanUtils.copyProperties(mediaItem, mediaItemVo);
            mediaItemVo.setMerchantName(merchant == null ? "" : merchant.getName());
            mediaItemVo.setCreateTime(DateUtils.formatDate(mediaItem.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            mediaItemVo.setUpdateTime(DateUtils.formatDate(mediaItem.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            mediaItemVoList.add(mediaItemVo);
        }
        return mediaItemVoList;
    }
}
