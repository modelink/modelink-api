package com.modelink.active.controller;

import com.modelink.admin.service.SmsService;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.Reservation;
import com.modelink.reservation.enums.ReservationStatusEnum;
import com.modelink.reservation.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;

/** 塑料姐妹不可怕，谁穷谁尴尬 **/
@Controller
@RequestMapping("/active/20180915")
public class Active20180915Controller {

    private static Logger logger = LoggerFactory.getLogger(Active20180915Controller.class);

    @Resource
    private SmsService smsService;
    @Resource
    private ReservationService reservationService;

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/active/20180915/index");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/doReserve")
    public ResultVo doReserve(String name, String mobile, String captcha) {
        ResultVo resultVo;

        // 校验验证码是否正确
        resultVo = smsService.validateCaptcha(mobile, captcha);
        if(RetStatus.Ok.getValue() != resultVo.getRtnCode()){
            return resultVo;
        }

        Reservation reservation = new Reservation();
        try {
            reservation.setContactName(name);
            reservation.setContactMobile(mobile);
            reservation.setContactTime(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
            reservation.setStatus(ReservationStatusEnum.CREATED.getValue());
            reservation.setChannel(10000L);
            reservation.setSourceType(1);//塑料姐妹不可怕，谁穷谁尴尬
            reservation.setCreateTime(new Date());
            reservation.setUpdateTime(new Date());
            int num = reservationService.insert(reservation);
            if(num > 0){
                resultVo.setRtnCode(RetStatus.Ok.getValue());
                resultVo.setRtnMsg(RetStatus.Ok.getText());
                smsService.clearCaptcha(mobile);
            }else{
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg(RetStatus.Fail.getText());
            }
        } catch (Exception e) {
            logger.error("[active20180915Controller|doReserve]预约登记发生异常。", e);
            resultVo.setRtnCode(RetStatus.Exception.getValue());
            resultVo.setRtnMsg(RetStatus.Exception.getText());
        }
        return resultVo;
    }

}
