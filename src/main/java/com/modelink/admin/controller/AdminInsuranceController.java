package com.modelink.admin.controller;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.InsuranceParamPagerVo;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.reservation.bean.Insurance;
import com.modelink.reservation.service.InsuranceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Controller
@RequestMapping("/admin/insurance")
public class AdminInsuranceController {

    @Resource
    private InsuranceService insuranceService;

    @RequestMapping
    public String index(){
        return "/admin/insurance/list";
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<Insurance> list(InsuranceParamPagerVo paramPagerVo) {
        PageInfo<Insurance> pageInfo = insuranceService.findPagerByParam(paramPagerVo);
        LayuiResultPagerVo<Insurance> layuiResultPagerVo = new LayuiResultPagerVo();
        layuiResultPagerVo.setTotalCount(pageInfo.getSize());
        layuiResultPagerVo.setRtnList(pageInfo.getList());
        return layuiResultPagerVo;
    }

    @ResponseBody
    @RequestMapping("/import")
    public ResultVo importExcel(@RequestParam MultipartFile file){
        ResultVo resultVo = new ResultVo();


        return resultVo;
    }
}