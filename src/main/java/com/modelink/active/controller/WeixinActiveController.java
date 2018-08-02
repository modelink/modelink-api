package com.modelink.active.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/active/weixin")
public class WeixinActiveController {

    @RequestMapping("/reserve-result-20180810")
    public ModelAndView result(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/active/weixin/reserve-result-20180810");
        return modelAndView;
    }
}
