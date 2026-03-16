package com.ruoyi.app.user.service;

import com.ruoyi.app.user.domain.AppLoginUser;
import com.ruoyi.app.user.domain.AppRegisterBody;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.common.exception.ServiceException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

/**
 * APP登录Service单元测试
 */
@SpringBootTest(classes = com.ruoyi.RuoYiApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppLoginServiceTest
{
    @Autowired
    private AppLoginService appLoginService;

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private AppTokenService appTokenService;

    private static final String TEST_USER_NAME = "login_test_user_" + System.currentTimeMillis();
    private static final String TEST_PASSWORD = "test123456";
    private static String testToken;

    @Test
    @Order(1)
    public void testRegister()
    {
        AppRegisterBody registerBody = new AppRegisterBody();
        registerBody.setUserName(TEST_USER_NAME);
        registerBody.setPassword(TEST_PASSWORD);
        registerBody.setNickName("登录测试用户");
        registerBody.setPhonenumber("13700137001");
        registerBody.setDeviceId("test_device_login");

        String token = appLoginService.register(registerBody);
        assertNotNull(token, "注册后应返回token");
        assertFalse(token.isEmpty(), "token不应为空");
        testToken = token;
        System.out.println("注册成功，token: " + token.substring(0, Math.min(20, token.length())) + "...");
    }

    @Test
    @Order(2)
    public void testRegisterDuplicateUserName()
    {
        AppRegisterBody registerBody = new AppRegisterBody();
        registerBody.setUserName(TEST_USER_NAME);
        registerBody.setPassword("another_password");

        assertThrows(ServiceException.class, () -> {
            appLoginService.register(registerBody);
        }, "重复用户名注册应抛出异常");
        System.out.println("重复用户名注册校验通过");
    }

    @Test
    @Order(3)
    public void testLogin()
    {
        String token = appLoginService.login(TEST_USER_NAME, TEST_PASSWORD);
        assertNotNull(token, "登录后应返回token");
        assertFalse(token.isEmpty(), "token不应为空");
        testToken = token;
        System.out.println("登录成功，token: " + token.substring(0, Math.min(20, token.length())) + "...");
    }

    @Test
    @Order(4)
    public void testLoginWithWrongPassword()
    {
        assertThrows(ServiceException.class, () -> {
            appLoginService.login(TEST_USER_NAME, "wrong_password");
        }, "错误密码登录应抛出异常");
        System.out.println("错误密码登录校验通过");
    }

    @Test
    @Order(5)
    public void testLoginWithNonExistentUser()
    {
        assertThrows(ServiceException.class, () -> {
            appLoginService.login("non_existent_user_" + System.currentTimeMillis(), "password");
        }, "不存在的用户登录应抛出异常");
        System.out.println("不存在用户登录校验通过");
    }

    @Test
    @Order(6)
    public void testTokenServiceCreateToken()
    {
        AppUser appUser = appUserService.selectAppUserByUserName(TEST_USER_NAME);
        assertNotNull(appUser, "测试用户应存在");

        AppLoginUser loginUser = new AppLoginUser(appUser, new java.util.HashSet<>());
        String token = appTokenService.createToken(loginUser);
        
        assertNotNull(token, "创建token成功");
        assertNotNull(loginUser.getToken(), "loginUser应有token");
        assertNotNull(loginUser.getLoginTime(), "loginUser应有登录时间");
        assertNotNull(loginUser.getExpireTime(), "loginUser应有过期时间");
        System.out.println("Token服务创建token成功");
    }

    @Test
    @Order(100)
    public void testCleanup()
    {
        AppUser user = appUserService.selectAppUserByUserName(TEST_USER_NAME);
        if (user != null)
        {
            appUserService.deleteAppUserById(user.getUserId());
            System.out.println("清理测试用户成功");
        }
    }
}
