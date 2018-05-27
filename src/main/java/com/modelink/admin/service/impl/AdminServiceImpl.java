package com.modelink.admin.service.impl;

import com.modelink.admin.bean.Admin;
import com.modelink.admin.mapper.AdminMapper;
import com.modelink.admin.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    public static Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Resource
    private AdminMapper adminMapper;

    /**
     * 根据用户名查询管理员信息
     *
     * @param userName
     * @return
     */
    @Override
    public Admin findByUserName(String userName) {
        Admin admin = new Admin();
        admin.setUserName(userName);
        return adminMapper.selectOne(admin);
    }

    /**
     * 根据指定条件查询管理员信息
     *
     * @param adminParam
     * @return
     */
    @Override
    public Admin findByParam(Admin adminParam) {
        Admin admin;
        try {
            admin = adminMapper.selectOne(adminParam);
        } catch (Exception e) {
            logger.error("", e);
            admin = null;
        }
        return admin;
    }

    /**
     * 根据指定条件查询管理员信息列表
     *
     * @param adminParam
     * @return
     */
    @Override
    public List<Admin> findListByParam(Admin adminParam) {
        return adminMapper.select(adminParam);
    }
}
