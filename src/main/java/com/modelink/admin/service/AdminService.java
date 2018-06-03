package com.modelink.admin.service;

import com.modelink.admin.bean.Admin;
import com.modelink.admin.bean.Permission;

import java.util.List;

public interface AdminService {

    /**
     * 更新用户信息
     * @param admin
     * @return
     */
    public boolean update(Admin admin);

    /**
     * 根据用户名查询管理员信息
     * @param userName
     * @return
     */
    public Admin findByUserName(String userName);

    /**
     * 根据指定条件查询管理员信息
     * @param admin
     * @return
     */
    public Admin findByParam(Admin admin);

    /**
     * 根据指定条件查询管理员信息列表
     * @param admin
     * @return
     */
    public List<Admin> findListByParam(Admin admin);

    /**
     * 获取指定管理员的菜单列表
     * @param admin
     * @return
     */
    public List<Permission> findPermissionList(Admin admin);
}
