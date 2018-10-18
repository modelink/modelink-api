package com.modelink.reservation.service.impl;

import com.modelink.admin.vo.huaxiaReport.HuaxiaReportParamVo;
import com.modelink.reservation.bean.HuaxiaFlowReport;
import com.modelink.reservation.service.HuaxiaFlowReportService;
import com.modelink.reservation.vo.HuaxiaFlowReportParamPagerVo;
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
public class HuaxiaFlowReportServiceImplTest {

    private static Logger logger = LoggerFactory.getLogger(HuaxiaFlowReportServiceImplTest.class);

    @Resource
    private HuaxiaFlowReportService huaxiaFlowReportService;

    @Test
    public void findMapByDateGroup() {
        HuaxiaReportParamVo paramVo = new HuaxiaReportParamVo();
        paramVo.setChooseDate("2018-10-01 - 2018-10-30");
        paramVo.setDataSource("预约");
        paramVo.setPlatformName("PC");
        //paramVo.setAdvertiseActive("百度SEM,360SEM");
        Map<String, HuaxiaFlowReport> flowReportMap = huaxiaFlowReportService.findMapByParamGroup(paramVo);

        String parameterName;
        Iterator<String> iterator = flowReportMap.keySet().iterator();
        while (iterator.hasNext()) {
            parameterName = iterator.next();
            logger.info("[parameterName=" + parameterName + "] [value=" + flowReportMap.get(parameterName) + "]");
        }
    }

    @Test
    public void findListByDateGroup() {
        HuaxiaFlowReportParamPagerVo paramVo = new HuaxiaFlowReportParamPagerVo();
        paramVo.setChooseDate("2018-10-01 - 2018-10-30");
        paramVo.setDataSource("预约");
        paramVo.setPlatformName("PC");
        //paramVo.setAdvertiseActive("百度SEM,360SEM");
        List<HuaxiaFlowReport> flowReportList = huaxiaFlowReportService.findListByParamGroup(paramVo);

        HuaxiaFlowReport flowReport;
        Iterator<HuaxiaFlowReport> iterator = flowReportList.iterator();
        while (iterator.hasNext()) {
            flowReport = iterator.next();
            logger.info("[parameterName=dataSource] [value=" + flowReport.getDataSource() + "]");
        }
    }
}