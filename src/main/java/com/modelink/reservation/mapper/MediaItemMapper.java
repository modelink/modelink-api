package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.MediaItem;
import com.modelink.reservation.bean.Underwrite;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface MediaItemMapper extends Mapper<MediaItem>, MySqlMapper<MediaItem> {

}
