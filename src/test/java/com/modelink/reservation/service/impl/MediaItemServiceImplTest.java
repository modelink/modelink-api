package com.modelink.reservation.service.impl;

import com.modelink.admin.vo.huaxiaReport.HuaxiaReportParamVo;
import com.modelink.reservation.bean.MediaItem;
import com.modelink.reservation.service.MediaItemService;
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

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MediaItemServiceImplTest {

    public static Logger logger = LoggerFactory.getLogger(MediaItemServiceImplTest.class);

    @Resource
    private MediaItemService mediaItemService;

    @Test
    public void findMapByMonthGroup() {
        HuaxiaReportParamVo paramVo = new HuaxiaReportParamVo();
        paramVo.setChooseDate("2017-12-01 - 2018-10-31");
        paramVo.setDataSource("预约");
        Map<String, Map<String, Object>> mediaItemMap = mediaItemService.findMapByMonthGroup(paramVo);
        Iterator<String> iterator = mediaItemMap.keySet().iterator();
        String month;
        while (iterator.hasNext()) {
            month = iterator.next();
            logger.info("[parameterName=" + month + "] [parameterValue=" + mediaItemMap.get(month) + "]");
        }
    }
}