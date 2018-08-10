package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.Abnormal;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface AbnormalMapper extends Mapper<Abnormal>, MySqlMapper<Abnormal> {

}
