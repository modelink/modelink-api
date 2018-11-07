package com.modelink.thirdparty.controller;

import com.modelink.common.vo.ResultVo;
import com.modelink.thirdparty.service.ProgressService;
import com.modelink.thirdparty.service.RedisService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/progress")
public class ProgressController {

    @Resource
    private ProgressService progressService;

    @ResponseBody
    @RequestMapping("/present")
    public ResultVo present() {
        ResultVo resultVo = new ResultVo();

        return resultVo;
    }
}
