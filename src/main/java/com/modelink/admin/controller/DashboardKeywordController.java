package com.modelink.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/dashboard/keyword")
public class DashboardKeywordController {

    @RequestMapping
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/dashboard/keyword");
        return modelAndView;
    }
}
