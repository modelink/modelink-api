package com.modelink.active.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;

@Controller
@RequestMapping("/active/weixin")
public class WeixinActiveController {

    @RequestMapping("/reserve-index-20180810")
    public ModelAndView reserveIndex20180810(){
        ModelAndView modelAndView = new ModelAndView();


        modelAndView.setViewName("/active/weixin/reserve-index-20180810.html");
        return modelAndView;
    }

    @RequestMapping("/reserve-result-20180810")
    public ModelAndView result(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/active/weixin/reserve-result-20180810");
        return modelAndView;
    }
}
