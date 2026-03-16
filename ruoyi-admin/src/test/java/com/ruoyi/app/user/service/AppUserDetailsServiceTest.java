package com.ruoyi.app.user.service;

import com.ruoyi.app.user.domain.AppLoginUser;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.mapper.AppUserMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.*;

/**
 * APP用户认证Service单元测试
 */
@SpringBootTest(classes = com.ruoyi.RuoYiApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppUserDetailsServiceTest
{
    @Autowired
    private AppUserDetailsServiceImpl appUserDetailsService;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private IAppUserService appUserService;

    private static final String TEST_USER_NAME = "auth_test_user_" + System.currentTimeMillis();
    private static Long testUserId;

    @Test
    @Order(1)
    public void testSetupUser()
    {
        AppUser appUser = new AppUser();
        appUser.setUserName(TEST_USER_NAME);
        appUser.setPassword("test_password");
        appUser.setNickName("认证测试用户");
        appUser.setStatus("0");
        
        appUserService.insertAppUser(appUser);
        testUserId = appUser.getUserId();
        assertNotNull(testUserId, "用户ID不应为空");
        System.out.println("创建测试用户成功，ID: " + testUserId);
    }

    @Test
    @Order(2)
    public void testLoadUserByUsername()
    {
        UserDetails userDetails = appUserDetailsService.loadUserByUsername(TEST_USER_NAME);
        
        assertNotNull(userDetails, "用户详情不应为空");
        assertEquals(TEST_USER_NAME, userDetails.getUsername(), "用户名不匹配");
        assertTrue(userDetails instanceof AppLoginUser, "应为AppLoginUser类型");
        
        AppLoginUser appLoginUser = (AppLoginUser) userDetails;
        assertNotNull(appLoginUser.getUser(), "用户信息不应为空");
        System.out.println("加载用户详情成功: " + userDetails.getUsername());
    }

    @Test
    @Order(3)
    public void testLoadUserByUsernameNotFound()
    {
        assertThrows(Exception.class, () -> {
            appUserDetailsService.loadUserByUsername("non_existent_user_" + System.currentTimeMillis());
        }, "不存在的用户应抛出异常");
        System.out.println("不存在用户加载校验通过");
    }

    @Test
    @Order(4)
    public void testLoadUserWithDisabledStatus()
    {
        AppUser disabledUser = new AppUser();
        disabledUser.setUserName("disabled_user_" + System.currentTimeMillis());
        disabledUser.setPassword("password");
        disabledUser.setStatus("1");
        appUserService.insertAppUser(disabledUser);

        assertThrows(Exception.class, () -> {
            appUserDetailsService.loadUserByUsername(disabledUser.getUserName());
        }, "停用用户应抛出异常");
        
        appUserService.deleteAppUserById(disabledUser.getUserId());
        System.out.println("停用用户加载校验通过");
    }

    @Test
    @Order(100)
    public void testCleanup()
    {
        if (testUserId != null)
        {
            appUserService.deleteAppUserById(testUserId);
            System.out.println("清理测试用户成功");
        }
    }
}
