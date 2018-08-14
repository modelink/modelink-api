package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.FlowReserve;
import com.modelink.reservation.bean.Underwrite;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

@Repository
public interface UnderwriteMapper extends Mapper<Underwrite>, MySqlMapper<Underwrite> {

    /**
     * 获取指定日期内的数据（只查日期与联系方式两列，节省内存）
     * @param startDate
     * @param endDate
     * @param merchantId
     * @return
     */
    public List<Underwrite> findListWithLimitColumnByDateRange(
            @Param("startDate")String startDate,
            @Param("endDate")String endDate,
            @Param("merchantId")Long merchantId);

}
