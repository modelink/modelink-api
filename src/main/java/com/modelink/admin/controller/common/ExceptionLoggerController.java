package com.modelink.admin.controller.common;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.bean.ExceptionLogger;
import com.modelink.admin.service.ExceptionLoggerService;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.vo.ExceptionLoggerParamPagerVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * 异常数据Controller
 */
@Controller
@RequestMapping("/admin/exceptionLogger")
public class ExceptionLoggerController {

    public static Logger logger = LoggerFactory.getLogger(ExceptionLoggerController.class);

    @Resource
    private ExceptionLoggerService exceptionLoggerService;

    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/message/exception-logger-list");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<ExceptionLogger> list(ExceptionLoggerParamPagerVo paramPagerVo){
        LayuiResultPagerVo<ExceptionLogger> layuiResultPagerVo = new LayuiResultPagerVo<>();

        PageInfo<ExceptionLogger> pageInfo = exceptionLoggerService.findPagerByParam(paramPagerVo);
        layuiResultPagerVo.setTotalCount((int)pageInfo.getTotal());
        layuiResultPagerVo.setRtnList(pageInfo.getList());
        return layuiResultPagerVo;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public ResultVo delete(){
        ResultVo resultVo = new ResultVo();
        exceptionLoggerService.delete(new ExceptionLogger());
        return resultVo;
    }

}
