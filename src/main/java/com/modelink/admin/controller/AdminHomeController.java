package com.modelink.admin.controller;

import com.modelink.admin.bean.Admin;
import com.modelink.admin.bean.Permission;
import com.modelink.admin.constants.AdminConstant;
import com.modelink.admin.service.AdminService;
import com.modelink.admin.vo.AdminLoginParamVo;
import com.modelink.admin.vo.AdminPasswordParamVo;
import com.modelink.admin.vo.AdminVo;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.vo.ResultVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    @Resource
    private AdminService adminService;

    @RequestMapping
    public String index(){
        return "/admin/layout/layout";
    }

    @RequestMapping("/home")
    public String home(){
        return "/admin/home/home";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request) {
        AdminVo adminVo = (AdminVo)request.getSession().getAttribute(AdminConstant.ADMIN_SESSION_NAME);
        if(adminVo != null){
            return "redirect:/admin";
        }else {
            return "/admin/login/login";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute(AdminConstant.ADMIN_SESSION_NAME);
        return "redirect:/admin/login";
    }

    @ResponseBody
    @RequestMapping("/doLogin")
    public ResultVo doLogin(HttpServletRequest request, AdminLoginParamVo adminLoginParamVo) {
        ResultVo resultVo = new ResultVo();
        String captcha = (String)request.getSession().getAttribute("KAPTCHA_SESSION_KEY");
        if(StringUtils.isEmpty(captcha) || !captcha.equalsIgnoreCase(adminLoginParamVo.getCaptcha())){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("验证码输入错误");
            return resultVo;
        }

        AdminVo adminVo = new AdminVo();
        Admin admin = adminService.findByUserName(adminLoginParamVo.getUserName());
        if(admin == null){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("该用户不存在");
            return resultVo;
        }
        if(!admin.getPassword().equals(adminLoginParamVo.getPassword())){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("用户名密码错误");
            return resultVo;
        }
        List<Permission> permissionList = adminService.findPermissionList(admin);
        adminVo.setPermissionList(permissionList);
        BeanUtils.copyProperties(admin, adminVo);
        // thymeleaf 无法获取属性中包含 "-" 的Session对象
        request.getSession().setAttribute(AdminConstant.ADMIN_SESSION_NAME, adminVo);
        resultVo.setRtnData("/admin/login");
        return resultVo;
    }

    @RequestMapping("/resetPassword")
    public String resetPassword(HttpServletRequest request) {
        return "/admin/home/reset-password";
    }

    @ResponseBody
    @RequestMapping("/doResetPassword")
    public ResultVo doResetPassword(HttpServletRequest request, AdminPasswordParamVo adminPasswordParamVo) {
        ResultVo resultVo = new ResultVo();
        AdminVo adminVo = (AdminVo)request.getSession().getAttribute(AdminConstant.ADMIN_SESSION_NAME);
        if(StringUtils.isEmpty(adminVo) || StringUtils.isEmpty(adminVo.getUserName())){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("用户会话已经过期");
            return resultVo;
        }

        Admin admin = adminService.findByUserName(adminVo.getUserName());
        if(StringUtils.isEmpty(admin)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("该用户不存在");
            return resultVo;
        }

        if(!adminPasswordParamVo.getOldPassword().equals(admin.getPassword())){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("当前密码输入错误");
            return resultVo;
        }

        if(!adminPasswordParamVo.getOnePassword().equals(adminPasswordParamVo.getTwoPassword())){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("两次输入的密码不一致");
            return resultVo;
        }

        admin.setPassword(adminPasswordParamVo.getOnePassword());
        adminService.update(admin);
        request.getSession().removeAttribute(AdminConstant.ADMIN_SESSION_NAME);
        resultVo.setRtnData("/admin/login");
        return resultVo;
    }

}

