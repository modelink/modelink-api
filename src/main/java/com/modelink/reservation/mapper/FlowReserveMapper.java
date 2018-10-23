package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.FlowReserve;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;
import java.util.Map;

@Repository
public interface FlowReserveMapper extends Mapper<FlowReserve>, MySqlMapper<FlowReserve> {

    /**
     * 查询广告活动列表
     *
     * @return
     */
    public List<String> findAdvertiseActiveList();

    @MapKey("unionKey")
    public Map<String, Map<String, Object>> findMapByParamGroup(Map<String, Object> paramMap);

    @MapKey("unionKey")
    public Map<String, Map<String, Object>> findMapByMonthGroup(Map<String, Object> paramMap);
}
