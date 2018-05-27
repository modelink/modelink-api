package com.modelink.admin.controller;

import com.modelink.admin.constants.AdminConstant;
import com.modelink.admin.vo.AdminVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class HomeController {

    @RequestMapping("/home")
    public String home(){
        return "/admin/home/home";
    }

    @RequestMapping("/login")
    public String login() {
        return "/admin/login/login";
    }

    @RequestMapping("/doLogin")
    public String doLogin(HttpServletRequest httpServletRequest) {
        AdminVo adminVo = new AdminVo();
        adminVo.setId("10000");
        adminVo.setName("流利的风筝");
        httpServletRequest.getSession().setAttribute(AdminConstant.ADMIN_SESSION_NAME, adminVo);
        return "/admin/home/home";
    }
}

