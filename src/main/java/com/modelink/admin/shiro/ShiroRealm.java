package com.modelink.admin.shiro;

import com.modelink.admin.bean.Admin;
import com.modelink.admin.bean.Resource;
import com.modelink.admin.bean.Role;
import com.modelink.admin.mapper.ResourceMapper;
import com.modelink.admin.mapper.RoleMapper;
import com.modelink.admin.service.AdminService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 实现AuthorizingRealm接口用户用户认证
 */
public class ShiroRealm extends AuthorizingRealm {

    //用于用户查询
    @javax.annotation.Resource
    private AdminService adminService;
    @javax.annotation.Resource
    private RoleMapper roleMapper;
    @javax.annotation.Resource
    private ResourceMapper resourceMapper;

    //角色权限和对应权限添加
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录用户名
        String userName = (String) principalCollection.getPrimaryPrincipal();
        //查询用户名称
        Admin admin = adminService.findByUserName(userName);
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        Role role;
        Resource resource;
        for (Long roleId: admin.getRoleIdList()) {
            //添加角色
            role = roleMapper.selectByPrimaryKey(roleId);
            simpleAuthorizationInfo.addRole(role.getName());
            for (Long resourceId: role.getResourceIdList()) {
                //添加权限
                resource = resourceMapper.selectByPrimaryKey(resourceId);
                simpleAuthorizationInfo.addStringPermission(resource.getName());
            }
        }
        return simpleAuthorizationInfo;
    }

    //用户认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        //获取用户信息
        String userName = authenticationToken.getPrincipal().toString();
        Admin admin = adminService.findByUserName(userName);
        if (admin == null) {
            //这里返回后会报出对应异常
            return null;
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userName, admin.getPassword().toString(), getName());
            return simpleAuthenticationInfo;
        }
    }
}
