package com.modelink.reservation.mapper;

import com.modelink.admin.vo.DashboardParamVo;
import com.modelink.reservation.bean.FlowReserve;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

@Repository
public interface FlowReserveMapper extends Mapper<FlowReserve>, MySqlMapper<FlowReserve> {

    /**
     * 查询广告活动列表
     *
     * @return
     */
    public List<String> findAdvertiseActiveList();

}
