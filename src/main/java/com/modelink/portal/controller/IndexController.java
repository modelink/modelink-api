package com.modelink.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
public class IndexController {

    @RequestMapping(value = {"", "/", "index"})
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/index/index");
        return modelAndView;
    }

    @RequestMapping("/introduce")
    public ModelAndView introduce(@RequestParam String index) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/index/introduce");
        modelAndView.addObject("index", index);
        return modelAndView;
    }


}
