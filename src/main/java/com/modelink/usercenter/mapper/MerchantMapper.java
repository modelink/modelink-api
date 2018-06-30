package com.modelink.usercenter.mapper;

import com.modelink.usercenter.bean.Merchant;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface MerchantMapper extends Mapper<Merchant>, MySqlMapper<Merchant> {

}
