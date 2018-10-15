package com.modelink.admin.controller.basedata;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.EstimationParamPagerVo;
import com.modelink.admin.vo.EstimationVo;
import com.modelink.common.enums.HXEstimateGoodsEnum;
import com.modelink.common.enums.HXReserveGoodsEnum;
import com.modelink.common.excel.ExcelExportConfigation;
import com.modelink.common.excel.ExcelExportHelper;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.reservation.bean.Estimation;
import com.modelink.reservation.service.EstimationService;
import com.modelink.usercenter.bean.Merchant;
import com.modelink.usercenter.service.MerchantService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/estimation")
public class AdminEstimationController {

    @Resource
    private MerchantService merchantService;
    @Resource
    private EstimationService estimationService;

    @RequestMapping
    public String index(){
        return "/admin/estimation/list";
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<EstimationVo> list(EstimationParamPagerVo paramPagerVo) {
        PageInfo<Estimation> pageInfo = estimationService.findPagerByParam(paramPagerVo);
        LayuiResultPagerVo<EstimationVo> layuiResultPagerVo = new LayuiResultPagerVo();

        Merchant merchant;
        EstimationVo reservationVo;
        List<EstimationVo> estimationVoList = new ArrayList<>();
        for (Estimation estimation : pageInfo.getList()) {
            reservationVo = new EstimationVo();
            BeanUtils.copyProperties(estimation, reservationVo);

            merchant = merchantService.findById(estimation.getMerchantId());
            reservationVo.setMerchantName(merchant == null ? "" : merchant.getName());
            reservationVo.setSourceType(HXEstimateGoodsEnum.getTextByValue(estimation.getSourceType()));
            estimationVoList.add(reservationVo);
        }
        layuiResultPagerVo.setTotalCount((int)pageInfo.getTotal());
        layuiResultPagerVo.setRtnList(estimationVoList);
        return layuiResultPagerVo;
    }

    @RequestMapping("/download")
    public void download(EstimationParamPagerVo paramPagerVo, HttpServletResponse response) throws Exception {
        // 创建文件名称
        String fileName = "测保预约列表_" + DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        // 创建Excel表格列名称
        List<String> columnNameList = new ArrayList<>();
        columnNameList.add("合作商户");
        columnNameList.add("预约姓名");
        columnNameList.add("预约性别");
        columnNameList.add("预约电话");
        columnNameList.add("预约生日");
        columnNameList.add("测保产品");
        columnNameList.add("创建时间");
        // 创建Excel 数据
        Merchant merchant;
        List<String> rowValueList;
        List<List<String>> dataList = new ArrayList<>();
        List<Estimation> estimationList = estimationService.findListByParam(paramPagerVo);
        for(Estimation estimation : estimationList){
            rowValueList = new ArrayList<>();
            merchant = merchantService.findById(estimation.getMerchantId());
            rowValueList.add(merchant == null ? "" : merchant.getName());
            rowValueList.add(estimation.getName());
            rowValueList.add(estimation.getGender());
            rowValueList.add(estimation.getMobile());
            rowValueList.add(estimation.getBirthday());
            rowValueList.add(HXEstimateGoodsEnum.getTextByValue(estimation.getSourceType()));
            rowValueList.add(DateUtils.formatDate(estimation.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            dataList.add(rowValueList);
        }

        ExcelExportConfigation excelConfigation = ExcelExportConfigation.newInstance(fileName, columnNameList, dataList);
        ExcelExportHelper.exportExcel2Response(excelConfigation, response);
    }
}
