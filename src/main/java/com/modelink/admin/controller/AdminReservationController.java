package com.modelink.admin.controller;

import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.reservation.bean.Reservation;
import com.modelink.reservation.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

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
    public Object list(){
        Reservation reservation = new Reservation();
        List<Reservation> reservationList = reservationService.findListByParam(reservation);

        LayuiResultPagerVo<Reservation> layuiResultPagerVo = new LayuiResultPagerVo();
        layuiResultPagerVo.setTotalCount(reservationList.size());
        layuiResultPagerVo.setRtnList(reservationList);
        return layuiResultPagerVo;
    }
}
