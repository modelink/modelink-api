package com.modelink.reservation.controller;

import com.modelink.common.vo.ResultVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    /**
     * 专家保险预约
     * @return
     */
    @ResponseBody
    @RequestMapping("/toReserve")
    public ResultVo toReserve(){
        ResultVo resultVo = new ResultVo();


        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/usableReserveTime")
    public ResultVo usableReserveTime(){
        ResultVo resultVo = new ResultVo();


        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/totalReserveNum")
    public ResultVo totalReserveNum(){
        ResultVo resultVo = new ResultVo();


        return resultVo;
    }
}
