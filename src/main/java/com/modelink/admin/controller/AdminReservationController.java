package com.modelink.admin.controller;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.ReservationParamPagerVo;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.reservation.bean.Reservation;
import com.modelink.reservation.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/admin/reservation")
public class AdminReservationController {

    @Resource
    private ReservationService reservationService;

    @RequestMapping
    public String index(){
        return "/admin/reservation/list";
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<Reservation> list(ReservationParamPagerVo paramPagerVo){
        PageInfo<Reservation> pageInfo = reservationService.findPagerByParam(paramPagerVo);
        LayuiResultPagerVo<Reservation> layuiResultPagerVo = new LayuiResultPagerVo();
        layuiResultPagerVo.setTotalCount(pageInfo.getSize());
        layuiResultPagerVo.setRtnList(pageInfo.getList());
        return layuiResultPagerVo;
    }
}
