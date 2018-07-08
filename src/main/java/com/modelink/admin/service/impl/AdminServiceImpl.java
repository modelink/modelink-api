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
import java.util.*;

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

        Permission parentPermission;
        List<Permission> menuList = new ArrayList<>();
        for(Permission permission : permissionList){
            parentPermission = recursionSearchMenu(permission);
            menuList.add(parentPermission);
        }

        permissionList = recursionMergeMenu(menuList);
        return permissionList;
    }

    // 递归找到父级菜单
    private Permission recursionSearchMenu(Permission permission){
        if(permission.getParentId() == 0){
            return permission;
        }
        Permission parentPermission = permissionMapper.selectByPrimaryKey(permission.getParentId());
        if(parentPermission == null){
            return permission;
        }
        // 将自身放到父级菜单中
        List<Permission> permissionList = parentPermission.getPermissionList();
        if(permissionList == null){
            permissionList = new ArrayList<>();
        }
        permissionList.add(permission);
        parentPermission.setPermissionList(permissionList);

        if(parentPermission.getParentId() == 0){
            return parentPermission;
        }
        return recursionSearchMenu(parentPermission);
    }

    // 递归合并子菜单
    private List<Permission> recursionMergeMenu(List<Permission> permissionList){
        Map<Long, Permission> permissionMap = new HashMap<>();
        List<Permission> resultList = new ArrayList<>();

        Permission childPermission;
        for(Permission permission : permissionList){

            childPermission = permissionMap.get(permission.getId());
            if(childPermission == null){
                childPermission = permission;
            }else if(permission.getPermissionList() != null) {
                childPermission.getPermissionList().addAll(permission.getPermissionList());
            }
            permissionMap.put(permission.getId(), childPermission);
        }
        Iterator<Long> iterator = permissionMap.keySet().iterator();
        while(iterator.hasNext()){
            childPermission = permissionMap.get(iterator.next());
            childPermission.setPermissionList(recursionMergeMenu(childPermission.getPermissionList()));
            resultList.add(childPermission);
        }
        return resultList;
    }

}
