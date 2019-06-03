package org.unamedgroup.conference.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unamedgroup.conference.dao.UserRepository;
import org.unamedgroup.conference.entity.User;
import org.unamedgroup.conference.service.UserManageService;

/**
 * MyRealm
 *
 * @author EndangeredFish
 * @mender liumengxiao
 * @date 2019/04/10
 */

@Service
public class MyRealm extends AuthorizingRealm {
//    private UserService userService;
//    @Autowired
//    private UserManageService userService;

    @Autowired
    private UserRepository userRepository;

//    public void setUserService(UserManageService userService) {
//        this.userService = userService;
//    }

    /**
     * 大坑！必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     * 这一段是干嘛的？？？？
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String phoneNumber = JWTUtil.getPhoneNumber(principals.toString());
        User user = userRepository.getUserByPhoneNumber(phoneNumber);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole("user");
//        simpleAuthorizationInfo.addRole(user.getRole());
//        Set<String> permission = new HashSet<>(Arrays.asList(user.getPermission().split(",")));
//        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密token获得phone_number，用于和数据库进行对比
        String phone_number = JWTUtil.getPhoneNumber(token);

        // 检查token有效性
        if (phone_number == null) {
            System.err.println("无法从用户提交的Token中获取有效手机号。");
            throw new AuthenticationException("Token invalid!");
        }

        // 检查user存在性
        User user = userRepository.getUserByPhoneNumber(phone_number);
        if (user == null) {
            System.err.println("找不到用户！");
            throw new AuthenticationException("User Not Found!");
        }

        // 利用数据库中密码hash作为secret，检测了phone_number和password_hash的一致性。（取巧）
        if (!JWTUtil.verify(token, phone_number, user.getPasswordHash())) {
            throw new AuthenticationException("Username or Password Error");
        }
        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }

}
