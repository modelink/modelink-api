package com.modelink.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.Estimation;
import com.modelink.reservation.bean.Reservation;
import com.modelink.reservation.enums.ReservationStatusEnum;
import com.modelink.reservation.service.EstimationService;
import com.modelink.reservation.service.ReservationService;
import com.modelink.reservation.vo.EstimateParamVo;
import com.modelink.reservation.vo.ReserveDateVo;
import com.modelink.reservation.vo.ReserveHeaderVo;
import com.modelink.reservation.vo.ReserveParamVo;
import com.modelink.usercenter.service.MerchantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    public static Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Resource
    private EstimationService estimationService;
    @Resource
    private ReservationService reservationService;

    /**
     * 专家保险预约
     * @return
     */
    @ResponseBody
    @RequestMapping("/toReserve")
    public ResultVo toReserve(HttpServletRequest request, ReserveParamVo reserveParamVo){
        ResultVo resultVo = new ResultVo();
        Reservation reservation = new Reservation();
        try {
            BeanUtils.copyProperties(reserveParamVo, reservation);
            reservation.setStatus(ReservationStatusEnum.CREATED.getValue());
            reservation.setChannel((Long)request.getAttribute("merchant"));
            reservation.setCreateTime(new Date());
            reservation.setUpdateTime(new Date());
            int num = reservationService.insert(reservation);
            if(num > 0){
                resultVo.setRtnCode(RetStatus.Ok.getValue());
                resultVo.setRtnMsg(RetStatus.Ok.getText());
            }else{
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg(RetStatus.Fail.getText());
            }
        } catch (Exception e) {
            logger.error("[reservationController|toReserve]预约登记发生异常。", e);
            resultVo.setRtnCode(RetStatus.Exception.getValue());
            resultVo.setRtnMsg(RetStatus.Exception.getText());
        }
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/reserveTimeAndNum")
    public ResultVo reserveTimeAndNum(HttpServletRequest request, ReserveHeaderVo reserveHeaderVo){
        ResultVo resultVo = new ResultVo();

        Reservation reservation = new Reservation();
        reservation.setChannel((Long)request.getAttribute("channel"));
        try {
            int reserveNum = reservationService.countByParam(reservation);
            List<ReserveDateVo> reserveDateVoList = reserveTimeList();

            JSONObject responseData = new JSONObject();
            responseData.put("totalReserveNum", reserveNum);
            responseData.put("reserveTimeList", reserveDateVoList);

            resultVo.setRtnData(responseData);
        } catch (Exception e) {
            logger.error("[reservationController|toReserve]预约时间和人数查询异常。", e);
            resultVo.setRtnCode(RetStatus.Exception.getValue());
            resultVo.setRtnMsg(RetStatus.Exception.getText());
        }
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("/totalReserveNum")
    public ResultVo totalReserveNum(HttpServletRequest request, ReserveHeaderVo reserveHeaderVo){
        ResultVo resultVo = new ResultVo();

        Reservation reservation = new Reservation();
        reservation.setChannel((Long)request.getAttribute("channel"));
        try {
            int reserveNum = reservationService.countByParam(reservation);
            resultVo.setRtnData(reserveNum);
        } catch (Exception e) {
            logger.error("[reservationController|toReserve]预约人数查询异常。", e);
            resultVo.setRtnCode(RetStatus.Exception.getValue());
            resultVo.setRtnMsg(RetStatus.Exception.getText());
        }
        return resultVo;
    }

    /**
     * 获取预约时间列表
     * @return
     */
    private List<ReserveDateVo> reserveTimeList(){
        Date currentDate = new Date();
        List<ReserveDateVo> reserveDateVoList = new ArrayList<>();

        String week;
        Date resultDate;
        ReserveDateVo reserveDateVo;
        int dateNum = 0, addNum = 0;
        while(addNum < 3){
            dateNum ++;
            resultDate = DateUtils.calculateDate(currentDate, Calendar.DATE, dateNum);
            reserveDateVo = new ReserveDateVo();

            week = DateUtils.printWeekValue(resultDate);
            if(week.equals("星期日")){
                continue;
            }
            reserveDateVo.setWeek(week);
            reserveDateVo.setDate(DateUtils.formatDate(resultDate, "yyyy-MM-dd"));
            reserveDateVoList.add(reserveDateVo);
            addNum ++;
        }

        return reserveDateVoList;
    }

    /**
     * 保费测算
     * @2018-10-15
     * @return
     */
    @ResponseBody
    @RequestMapping("/toEstimate")
    public ResultVo toEstimate(HttpServletRequest request, EstimateParamVo estimateParamVo){
        ResultVo resultVo = new ResultVo();
        Estimation estimation = new Estimation();
        try {
            BeanUtils.copyProperties(estimateParamVo, estimation);
            estimation.setStatus(ReservationStatusEnum.CREATED.getValue());
            estimation.setMerchantId((Long)request.getAttribute("merchant"));
            estimation.setCreateTime(new Date());
            estimation.setUpdateTime(new Date());
            int num = estimationService.insert(estimation);
            if(num > 0){
                resultVo.setRtnCode(RetStatus.Ok.getValue());
                resultVo.setRtnMsg(RetStatus.Ok.getText());
            }else{
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg(RetStatus.Fail.getText());
            }
        } catch (Exception e) {
            logger.error("[reservationController|toEstimate]测保登记发生异常。", e);
            resultVo.setRtnCode(RetStatus.Exception.getValue());
            resultVo.setRtnMsg(RetStatus.Exception.getText());
        }
        return resultVo;
    }
}
