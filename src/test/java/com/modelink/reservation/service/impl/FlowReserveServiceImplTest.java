package com.modelink.reservation.service.impl;

import com.modelink.admin.vo.huaxiaReport.HuaxiaReportParamVo;
import com.modelink.reservation.service.FlowReserveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FlowReserveServiceImplTest {

    private static Logger logger = LoggerFactory.getLogger(FlowReserveServiceImplTest.class);

    @Resource
    private FlowReserveService flowReserveService;

    @Test
    public void findAdvertiseActiveList() {
        List<String> advertiseActiveList = flowReserveService.findAdvertiseActiveList();
        for (String advertiseActive : advertiseActiveList) {
            System.out.println(advertiseActive);
        }
    }

    @Test
    public void findCountByDateGroup() {
        HuaxiaReportParamVo paramVo = new HuaxiaReportParamVo();
        paramVo.setChooseDate("2018-08-01 - 2018-10-30");
        paramVo.setDataSource("预约");
        Map<String, Map<String, Object>> flowReportMap = flowReserveService.findMapByParamGroup(paramVo);

        String parameterName;
        Iterator<String> iterator = flowReportMap.keySet().iterator();
        while (iterator.hasNext()) {
            parameterName = iterator.next();
            logger.info("[parameterName=" + parameterName + "] [value=" + flowReportMap.get(parameterName) + "]");
        }
    }
    @Test
    public void findMapByParamGroup() {
        HuaxiaReportParamVo paramVo = new HuaxiaReportParamVo();
        paramVo.setChooseDate("2018-10-10 - 2018-10-10");
        paramVo.setDataSource("预约");
        paramVo.setPlatformName("PC");
        paramVo.setAdvertiseActive("百度SEM,百度表单");
        Map<String, Map<String, Object>> flowReportMap = flowReserveService.findMapByParamGroup(paramVo);

        String parameterName;
        Iterator<String> iterator = flowReportMap.keySet().iterator();
        while (iterator.hasNext()) {
            parameterName = iterator.next();
            logger.info("[parameterName=" + parameterName + "] [value=" + flowReportMap.get(parameterName).get("reserveCount") + "]");
        }
    }
}