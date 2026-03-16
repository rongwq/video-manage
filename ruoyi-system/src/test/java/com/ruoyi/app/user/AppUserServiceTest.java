package com.ruoyi.app.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.ruoyi.system.RuoYiSystemApplication.class)
public class AppUserServiceTest
{
    @Autowired
    private IAppUserService appUserService;

    @Test
    public void testCheckUserNameUnique()
    {
        AppUser appUser = new AppUser();
        appUser.setUserName("testuser001");
        boolean result = appUserService.checkUserNameUnique(appUser);
        assertTrue(result, "用户名应该是唯一的");
        System.out.println("测试用户名唯一性检查: " + result);
    }

    @Test
    public void testCheckPhoneUnique()
    {
        AppUser appUser = new AppUser();
        appUser.setPhonenumber("13800138001");
        boolean result = appUserService.checkPhoneUnique(appUser);
        assertTrue(result, "手机号应该是唯一的");
        System.out.println("测试手机号唯一性检查: " + result);
    }

    @Test
    public void testCheckEmailUnique()
    {
        AppUser appUser = new AppUser();
        appUser.setEmail("test@example.com");
        boolean result = appUserService.checkEmailUnique(appUser);
        assertTrue(result, "邮箱应该是唯一的");
        System.out.println("测试邮箱唯一性检查: " + result);
    }
}
