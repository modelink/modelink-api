package com.modelink.thirdparty.service;

import com.modelink.common.vo.ResultVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProgressService {

    public static final String ProgressPrefix = "redis:progress:";

    @Resource
    private RedisService redisService;

    public ResultVo findProgressById(Long progressId) {
        ResultVo resultVo = new ResultVo();
        String progressKey = ProgressPrefix + progressId;


        return resultVo;
    }
}
