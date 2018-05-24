package com.modelink.usercenter.controller;

import com.modelink.usercenter.mapper.UserMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/usercenter")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @ResponseBody
    @RequestMapping("/findList")
    public Object findList(){
        return userMapper.selectAll();
    }
}
