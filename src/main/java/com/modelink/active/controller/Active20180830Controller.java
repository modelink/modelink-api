package com.modelink.active.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 活动名称：塑料姐妹不可怕，谁穷谁尴尬
 */
@Controller
@RequestMapping("/active/20180830")
public class Active20180830Controller {

    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();


        modelAndView.setViewName("/active/20180830/index");
        return modelAndView;
    }

}
