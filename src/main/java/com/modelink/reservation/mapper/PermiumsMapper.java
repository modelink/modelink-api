package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.Abnormal;
import com.modelink.reservation.bean.Permiums;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface PermiumsMapper extends Mapper<Permiums>, MySqlMapper<Permiums> {

}
