package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.HuaxiaFlowReport;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;
import java.util.Map;

@Repository
public interface HuaxiaFlowReportMapper extends Mapper<HuaxiaFlowReport>, MySqlMapper<HuaxiaFlowReport> {

    @MapKey("unionKey")
    Map<String, Map<String, Object>> findMapByMonthGroup(Map<String, Object> paramMap);

    @MapKey("unionKey")
    public Map<String, HuaxiaFlowReport> findMapByParamGroup(Map<String, Object> paramMap);

    public List<HuaxiaFlowReport> findListByParamGroup(Map<String, Object> paramMap);

    public List<HuaxiaFlowReport> findListByMonthGroup(Map<String, Object> paramMap);
}
