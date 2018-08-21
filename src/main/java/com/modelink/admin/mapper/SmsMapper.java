package com.modelink.admin.mapper;

import com.modelink.admin.bean.Admin;
import com.modelink.admin.bean.Sms;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface SmsMapper extends Mapper<Sms>, MySqlMapper<Sms> {

}
