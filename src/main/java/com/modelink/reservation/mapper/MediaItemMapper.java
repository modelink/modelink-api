package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.MediaItem;
import com.modelink.reservation.bean.Underwrite;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.Map;

@Repository
public interface MediaItemMapper extends Mapper<MediaItem>, MySqlMapper<MediaItem> {

    @MapKey("unionKey")
    public Map<String, MediaItem> findMapByParamGroup(Map<String, Object> paramMap);
}
