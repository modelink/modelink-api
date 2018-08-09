package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.Underwrite;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface UnderwriteMapper extends Mapper<Underwrite>, MySqlMapper<Underwrite> {

}
