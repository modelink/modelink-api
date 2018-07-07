package com.modelink.admin.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.AreaParamPagerVo;
import com.modelink.admin.vo.AreaVo;
import com.modelink.common.enums.AreaTypeEnum;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.utils.DateUtils;
import com.modelink.common.vo.LayuiResultPagerVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.usercenter.bean.Area;
import com.modelink.usercenter.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/area")
public class AdminAreaController {

    public static Logger logger = LoggerFactory.getLogger(AdminAreaController.class);

    @Resource
    private AreaService areaService;

    @RequestMapping
    public String index(){
        return "/admin/area/list";
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiResultPagerVo<AreaVo> list(AreaParamPagerVo paramPagerVo) {
        PageInfo<Area> pageInfo = areaService.findPagerByParam(paramPagerVo);
        LayuiResultPagerVo<AreaVo> layuiResultPagerVo = new LayuiResultPagerVo();

        List<Area> areaList = pageInfo.getList();
        List<AreaVo> areaVoList = transformBean2VoList(areaList);

        layuiResultPagerVo.setTotalCount((int)pageInfo.getTotal());
        layuiResultPagerVo.setRtnList(areaVoList);
        return layuiResultPagerVo;
    }

    @ResponseBody
    @RequestMapping("/save")
    public ResultVo save(Area area){
        ResultVo resultVo = new ResultVo();
        int count = 0;
        try {
            count = areaService.insert(area);
            if(count <= 0){
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("地区数据保存失败");
            }else{
                resultVo.setRtnCode(RetStatus.Ok.getValue());
                resultVo.setRtnMsg(RetStatus.Ok.getText());
            }
        } catch (Exception e) {
            logger.error("[adminAreaController|save]发生异常。area={}", JSON.toJSONString(area), e);
            resultVo.setRtnCode(RetStatus.Exception.getValue());
            resultVo.setRtnMsg(RetStatus.Exception.getText());
        }
        return resultVo;
    }

    private List<AreaVo> transformBean2VoList(List<Area> areaList){
        AreaVo areaVo;
        Area parentArea;
        List<AreaVo> areaVoList = new ArrayList<>();
        for(Area area : areaList){
            areaVo = new AreaVo();
            BeanUtils.copyProperties(area, areaVo);

            areaVo.setParentName("（无）");
            if(area.getParentId() != null && area.getParentId() != 0) {
                parentArea = areaService.findById(area.getParentId());
                areaVo.setParentName(parentArea.getAreaName() + "(" + area.getParentId() + ")");
            }

            areaVo.setStatus(area.getStatus() == 0 ? "启用" : "禁用");
            areaVo.setAreaType(AreaTypeEnum.getTextByValue(area.getAreaType()));
            areaVo.setCreateTime(DateUtils.formatDate(area.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            areaVo.setUpdateTime(DateUtils.formatDate(area.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));

            areaVoList.add(areaVo);
        }
        return areaVoList;
    }

}