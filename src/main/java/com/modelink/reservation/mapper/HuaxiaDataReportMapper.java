package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.Abnormal;
import com.modelink.reservation.bean.HuaxiaDataReport;
import com.modelink.reservation.bean.HuaxiaFlowReport;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;
import java.util.Map;

@Repository
public interface HuaxiaDataReportMapper extends Mapper<HuaxiaDataReport>, MySqlMapper<HuaxiaDataReport> {

    public List<HuaxiaDataReport> findListByMonthGroup(Map<String, Object> paramMap);
}
