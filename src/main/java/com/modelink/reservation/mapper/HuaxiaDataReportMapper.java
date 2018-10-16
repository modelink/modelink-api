package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.Abnormal;
import com.modelink.reservation.bean.HuaxiaDataReport;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface HuaxiaDataReportMapper extends Mapper<HuaxiaDataReport>, MySqlMapper<HuaxiaDataReport> {

}
