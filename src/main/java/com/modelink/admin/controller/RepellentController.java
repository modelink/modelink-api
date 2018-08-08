package com.modelink.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/** 拒保Controller **/
@Controller
@RequestMapping("/admin/repellent")
public class RepellentController {

    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/repellent/list");
        return modelAndView;
    }
}
