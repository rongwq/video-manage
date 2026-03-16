package com.ruoyi.app.user.service;

import com.ruoyi.app.user.domain.AppUser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * APP用户Service单元测试
 */
@SpringBootTest(classes = com.ruoyi.RuoYiApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppUserServiceTest
{
    @Autowired
    private IAppUserService appUserService;

    private static Long testUserId;

    @Test
    @Order(1)
    public void testInsertAppUser()
    {
        AppUser appUser = new AppUser();
        appUser.setUserName("test_app_user_" + System.currentTimeMillis());
        appUser.setNickName("测试APP用户");
        appUser.setPassword("123456");
        appUser.setPhonenumber("13800138001");
        appUser.setEmail("test@example.com");
        appUser.setSex("0");
        appUser.setDeviceId("test_device_001");
        appUser.setRemark("单元测试创建");

        int result = appUserService.insertAppUser(appUser);
        assertTrue(result > 0, "新增APP用户失败");
        assertNotNull(appUser.getUserId(), "用户ID不应为空");
        testUserId = appUser.getUserId();
        System.out.println("新增APP用户成功，ID: " + testUserId);
    }

    @Test
    @Order(2)
    public void testSelectAppUserById()
    {
        if (testUserId == null)
        {
            testInsertAppUser();
        }
        AppUser user = appUserService.selectAppUserById(testUserId);
        assertNotNull(user, "查询结果不应为空");
        assertEquals(testUserId, user.getUserId(), "用户ID不匹配");
        System.out.println("查询APP用户成功: " + user.getUserName());
    }

    @Test
    @Order(3)
    public void testSelectAppUserByUserName()
    {
        String userName = "test_query_user_" + System.currentTimeMillis();
        AppUser appUser = new AppUser();
        appUser.setUserName(userName);
        appUser.setNickName("查询测试用户");
        appUser.setPassword("123456");
        appUserService.insertAppUser(appUser);

        AppUser queryResult = appUserService.selectAppUserByUserName(userName);
        assertNotNull(queryResult, "查询结果不应为空");
        assertEquals(userName, queryResult.getUserName(), "用户名不匹配");
        System.out.println("按用户名查询成功: " + userName);
    }

    @Test
    @Order(4)
    public void testSelectAppUserList()
    {
        AppUser queryParam = new AppUser();
        queryParam.setStatus("0");
        List<AppUser> list = appUserService.selectAppUserList(queryParam);
        assertNotNull(list, "用户列表不应为空");
        System.out.println("查询APP用户列表成功，共" + list.size() + "条记录");
    }

    @Test
    @Order(5)
    public void testUpdateAppUser()
    {
        if (testUserId == null)
        {
            testInsertAppUser();
        }
        AppUser appUser = appUserService.selectAppUserById(testUserId);
        assertNotNull(appUser, "用户不存在");

        String newNickName = "更新后的昵称_" + System.currentTimeMillis();
        appUser.setNickName(newNickName);
        appUser.setEmail("updated@example.com");

        int result = appUserService.updateAppUser(appUser);
        assertTrue(result > 0, "更新APP用户失败");

        AppUser updated = appUserService.selectAppUserById(testUserId);
        assertEquals(newNickName, updated.getNickName(), "昵称未更新");
        System.out.println("更新APP用户成功: " + updated.getNickName());
    }

    @Test
    @Order(6)
    public void testCheckUserNameUnique()
    {
        String uniqueName = "unique_user_" + System.currentTimeMillis();
        boolean isUnique = appUserService.checkUserNameUnique(uniqueName);
        assertTrue(isUnique, "用户名应该是唯一的");

        AppUser appUser = new AppUser();
        appUser.setUserName(uniqueName);
        appUser.setPassword("123456");
        appUserService.insertAppUser(appUser);

        boolean isNotUnique = appUserService.checkUserNameUnique(uniqueName);
        assertFalse(isNotUnique, "用户名应该已存在");
        System.out.println("用户名唯一性校验通过");
    }

    @Test
    @Order(7)
    public void testCheckPhoneUnique()
    {
        String uniquePhone = "139" + String.format("%08d", System.currentTimeMillis() % 100000000);
        boolean isUnique = appUserService.checkPhoneUnique(uniquePhone);
        assertTrue(isUnique, "手机号应该是唯一的");

        AppUser appUser = new AppUser();
        appUser.setUserName("phone_test_user_" + System.currentTimeMillis());
        appUser.setPassword("123456");
        appUser.setPhonenumber(uniquePhone);
        appUserService.insertAppUser(appUser);

        boolean isNotUnique = appUserService.checkPhoneUnique(uniquePhone);
        assertFalse(isNotUnique, "手机号应该已存在");
        System.out.println("手机号唯一性校验通过");
    }

    @Test
    @Order(8)
    public void testUpdateLoginInfo()
    {
        if (testUserId == null)
        {
            testInsertAppUser();
        }
        appUserService.updateLoginInfo(testUserId, "127.0.0.1");

        AppUser user = appUserService.selectAppUserById(testUserId);
        assertEquals("127.0.0.1", user.getLoginIp(), "登录IP未更新");
        assertNotNull(user.getLoginDate(), "登录时间不应为空");
        System.out.println("更新登录信息成功");
    }

    @Test
    @Order(100)
    public void testDeleteAppUserById()
    {
        AppUser appUser = new AppUser();
        appUser.setUserName("delete_test_user_" + System.currentTimeMillis());
        appUser.setPassword("123456");
        appUserService.insertAppUser(appUser);

        Long deleteId = appUser.getUserId();
        assertNotNull(deleteId, "用户ID不应为空");

        int result = appUserService.deleteAppUserByIds(new Long[]{deleteId});
        assertTrue(result > 0, "删除APP用户失败");

        AppUser deleted = appUserService.selectAppUserById(deleteId);
        assertNull(deleted, "用户应已被删除");
        System.out.println("删除APP用户成功，ID: " + deleteId);
    }

    @Test
    @Order(101)
    public void testDeleteAppUserByIds()
    {
        Long[] ids = new Long[2];
        for (int i = 0; i < 2; i++)
        {
            AppUser appUser = new AppUser();
            appUser.setUserName("batch_delete_user_" + i + "_" + System.currentTimeMillis());
            appUser.setPassword("123456");
            appUserService.insertAppUser(appUser);
            ids[i] = appUser.getUserId();
        }

        int result = appUserService.deleteAppUserByIds(ids);
        assertTrue(result == 2, "批量删除应删除2条记录");

        for (Long id : ids)
        {
            AppUser deleted = appUserService.selectAppUserById(id);
            assertNull(deleted, "用户ID " + id + " 应已被删除");
        }
        System.out.println("批量删除APP用户成功，共删除" + result + "条记录");
    }
}
