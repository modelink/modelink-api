package com.modelink.active.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;

@Controller
@RequestMapping("/active/20180810")
public class Active20180810Controller {

    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();


        modelAndView.setViewName("/active/20180810/index");
        return modelAndView;
    }

    @RequestMapping("/result")
    public ModelAndView result(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/active/20180810/result");
        return modelAndView;
    }
}
