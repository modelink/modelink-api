package com.modelink.admin.controller.basedata;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.ReservationParamPagerVo;
import com.modelink.admin.vo.ReservationVo;
import com.modelink.common.enums.HXSourceTypeEnum;
import com.modelink.common.excel.ExcelExportConfigation;
import com.modelink.common.excel.ExcelExportHelper;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.reservation.bean.Reservation;
import com.modelink.reservation.enums.ResourceTypeEnum;
import com.modelink.reservation.service.ReservationService;
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
@RequestMapping("/admin/reservation")
public class AdminReservationController {

    @Resource
    private MerchantService merchantService;
    @Resource
    private ReservationService reservationService;

    @RequestMapping
    public String index(){
        return "/admin/reservation/list";
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<ReservationVo> list(ReservationParamPagerVo paramPagerVo) {
        PageInfo<Reservation> pageInfo = reservationService.findPagerByParam(paramPagerVo);
        LayuiResultPagerVo<ReservationVo> layuiResultPagerVo = new LayuiResultPagerVo();

        Merchant merchant;
        ReservationVo reservationVo;
        List<ReservationVo> reservationVoList = new ArrayList<>();
        for (Reservation reservation : pageInfo.getList()) {
            reservationVo = new ReservationVo();
            BeanUtils.copyProperties(reservation, reservationVo);

            merchant = merchantService.findById(reservation.getChannel());
            reservationVo.setChannel(merchant == null ? "" : merchant.getName());
            if(merchant.getAppKey() == 10000L){
                reservationVo.setSourceType("H5:塑料姐妹情");
            }else if(merchant.getAppKey() == 10001L){
                reservationVo.setSourceType(HXSourceTypeEnum.getTextByValue(reservation.getSourceType()));
            }
            reservationVoList.add(reservationVo);
        }
        layuiResultPagerVo.setTotalCount((int)pageInfo.getTotal());
        layuiResultPagerVo.setRtnList(reservationVoList);
        return layuiResultPagerVo;
    }

    @RequestMapping("/download")
    public void download(ReservationParamPagerVo paramPagerVo, HttpServletResponse response) throws Exception {
        // 创建文件名称
        String fileName = "专家保险预约列表_" + DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        // 创建Excel表格列名称
        List<String> columnNameList = new ArrayList<>();
        columnNameList.add("预约姓名");
        columnNameList.add("预约电话");
        columnNameList.add("预约时间");
        columnNameList.add("预约渠道");
        columnNameList.add("预约入口");
        columnNameList.add("创建时间");
        // 创建Excel 数据
        Merchant merchant;
        List<String> rowValueList;
        List<List<String>> dataList = new ArrayList<>();
        List<Reservation> reservationList = reservationService.findListByParam(paramPagerVo);
        for(Reservation reservation : reservationList){
            rowValueList = new ArrayList<>();
            rowValueList.add(reservation.getContactName());
            rowValueList.add(reservation.getContactMobile());
            rowValueList.add(reservation.getContactTime());
            merchant = merchantService.findById(reservation.getChannel());
            rowValueList.add(merchant == null ? "" : merchant.getName());
            if(merchant.getAppKey() == 10000L){
                rowValueList.add("H5:塑料姐妹情");
            }else if(merchant.getAppKey() == 10001L){
                rowValueList.add(HXSourceTypeEnum.getTextByValue(reservation.getSourceType()));
            }
            rowValueList.add(DateUtils.formatDate(reservation.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            dataList.add(rowValueList);
        }

        ExcelExportConfigation excelConfigation = ExcelExportConfigation.newInstance(fileName, columnNameList, dataList);
        ExcelExportHelper.exportExcel2Response(excelConfigation, response);
    }
}
