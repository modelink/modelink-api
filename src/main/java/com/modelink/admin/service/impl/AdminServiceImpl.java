package com.modelink.admin.service.impl;

import com.modelink.admin.bean.Admin;
import com.modelink.admin.bean.Permission;
import com.modelink.admin.bean.Role;
import com.modelink.admin.mapper.AdminMapper;
import com.modelink.admin.mapper.PermissionMapper;
import com.modelink.admin.mapper.RoleMapper;
import com.modelink.admin.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    public static Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Resource
    private AdminMapper adminMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 更新用户信息
     *
     * @param admin
     * @return
     */
    @Override
    public boolean update(Admin admin) {
        int num = adminMapper.updateByPrimaryKeySelective(admin);
        return num > 0 ? true : false;
    }

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

    /**
     * 获取指定管理员的菜单列表
     *
     * @param admin
     * @return
     */
    @Override
    public List<Permission> findPermissionList(Admin admin) {
        List<Permission> permissionList = new ArrayList<>();
        if(admin == null || StringUtils.isEmpty(admin.getRoleIds())){
            return permissionList;
        }
        // 查找所有角色ID
        List<Long> roleIdList = new ArrayList<>();
        for(String roleId : admin.getRoleIds().split(",")){
            roleIdList.add(Long.parseLong(roleId));
        }
        // 查找所有角色内容
        Example roleExample = new Example(Role.class);
        Example.Criteria roleCriteria = roleExample.createCriteria();
        roleCriteria.andIn("id", roleIdList);
        roleCriteria.andEqualTo("available", true);
        List<Role> roleList = roleMapper.selectByExample(roleExample);
        // 根据角色找到权限ID列表
        List<Long> permissionIdList = new ArrayList<>();
        for(Role role : roleList){
            if(StringUtils.isEmpty(role.getPermissionIds())) continue;
            for(String permissionId : role.getPermissionIds().split(",")){
                permissionIdList.add(Long.parseLong(permissionId));
            }
        }
        // 查找所有权限列表
        Example permissionExample = new Example(Permission.class);
        Example.Criteria permissionCriteria = permissionExample.createCriteria();
        permissionCriteria.andIn("id", permissionIdList);
        permissionCriteria.andEqualTo("available", true);
        permissionList = permissionMapper.selectByExample(permissionExample);

        return permissionList;
    }
}
