package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.Abnormal;
import com.modelink.reservation.bean.Repellent;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface RepellentMapper extends Mapper<Repellent>, MySqlMapper<Repellent> {

}
