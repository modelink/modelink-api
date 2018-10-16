package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.Abnormal;
import com.modelink.reservation.bean.HuaxiaFlowReport;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface HuaxiaFlowReportMapper extends Mapper<HuaxiaFlowReport>, MySqlMapper<HuaxiaFlowReport> {

}
