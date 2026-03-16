package com.ruoyi.app.user;

import java.util.Date;
import java.util.List;

import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * APP用户Service单元测试
 *
 * @author ruoyi
 */
@SpringBootTest(classes = com.ruoyi.system.RuoYiSystemApplication.class)
@Transactional
public class AppUserServiceTest
{
    @Autowired
    private IAppUserService appUserService;

    /**
     * 测试APP用户注册
     */
    @Test
    public void testRegisterAppUser()
    {
        AppUser appUser = new AppUser();
        appUser.setUserName("testuser" + System.currentTimeMillis());
        appUser.setNickName("测试用户");
        appUser.setPassword("123456");
        appUser.setEmail("test@example.com");
        appUser.setPhonenumber("13800138000");
        appUser.setSex("0");
        appUser.setStatus("0");
        appUser.setCreateBy("system");

        boolean result = appUserService.registerAppUser(appUser);
        assertTrue(result, "APP用户注册失败");
        assertNotNull(appUser.getUserId(), "用户ID不应为空");
        System.out.println("APP用户注册成功，ID: " + appUser.getUserId());
    }

    /**
     * 测试通过用户名查询用户
     */
    @Test
    public void testSelectAppUserByUserName()
    {
        // 先注册用户
        String userName = "queryuser" + System.currentTimeMillis();
        AppUser appUser = new AppUser();
        appUser.setUserName(userName);
        appUser.setNickName("查询测试用户");
        appUser.setPassword("123456");
        appUser.setEmail("query@example.com");
        appUser.setPhonenumber("13800138001");
        appUser.setSex("1");
        appUser.setStatus("0");
        appUser.setCreateBy("system");
        appUserService.registerAppUser(appUser);

        // 查询用户
        AppUser queryResult = appUserService.selectAppUserByUserName(userName);
        assertNotNull(queryResult, "查询结果不应为空");
        assertEquals(userName, queryResult.getUserName(), "用户名不匹配");
        assertEquals("查询测试用户", queryResult.getNickName(), "昵称不匹配");
        System.out.println("查询APP用户成功: " + queryResult.getUserName());
    }

    /**
     * 测试通过用户ID查询用户
     */
    @Test
    public void testSelectAppUserById()
    {
        // 先注册用户
        AppUser appUser = new AppUser();
        appUser.setUserName("idqueryuser" + System.currentTimeMillis());
        appUser.setNickName("ID查询测试用户");
        appUser.setPassword("123456");
        appUser.setEmail("idquery@example.com");
        appUser.setPhonenumber("13800138002");
        appUser.setSex("0");
        appUser.setStatus("0");
        appUser.setCreateBy("system");
        appUserService.registerAppUser(appUser);

        Long userId = appUser.getUserId();
        assertNotNull(userId, "用户ID不应为空");

        // 通过ID查询
        AppUser queryResult = appUserService.selectAppUserById(userId);
        assertNotNull(queryResult, "查询结果不应为空");
        assertEquals(userId, queryResult.getUserId(), "用户ID不匹配");
        System.out.println("通过ID查询APP用户成功: " + queryResult.getUserId());
    }

    /**
     * 测试查询APP用户列表
     */
    @Test
    public void testSelectAppUserList()
    {
        // 先注册多个用户
        for (int i = 0; i < 3; i++) {
            AppUser appUser = new AppUser();
            appUser.setUserName("listuser" + i + System.currentTimeMillis());
            appUser.setNickName("列表测试用户" + i);
            appUser.setPassword("123456");
            appUser.setEmail("list" + i + "@example.com");
            appUser.setPhonenumber("138001380" + (10 + i));
            appUser.setSex("0");
            appUser.setStatus("0");
            appUser.setCreateBy("system");
            appUserService.registerAppUser(appUser);
        }

        // 查询列表
        AppUser queryParam = new AppUser();
        queryParam.setStatus("0");
        List<AppUser> list = appUserService.selectAppUserList(queryParam);
        assertNotNull(list, "用户列表不应为空");
        assertTrue(list.size() >= 3, "用户列表数量应大于等于3");
        System.out.println("查询APP用户列表成功，共" + list.size() + "条记录");
    }

    /**
     * 测试修改APP用户信息
     */
    @Test
    public void testUpdateAppUser()
    {
        // 先注册用户
        AppUser appUser = new AppUser();
        appUser.setUserName("updateuser" + System.currentTimeMillis());
        appUser.setNickName("修改前昵称");
        appUser.setPassword("123456");
        appUser.setEmail("update@example.com");
        appUser.setPhonenumber("13800138003");
        appUser.setSex("0");
        appUser.setStatus("0");
        appUser.setCreateBy("system");
        appUserService.registerAppUser(appUser);

        // 修改用户信息
        appUser.setNickName("修改后昵称");
        appUser.setEmail("updated@example.com");
        appUser.setSex("1");
        appUser.setUpdateBy("system");
        int result = appUserService.updateAppUser(appUser);
        assertTrue(result > 0, "修改用户信息失败");

        // 验证修改结果
        AppUser updated = appUserService.selectAppUserById(appUser.getUserId());
        assertEquals("修改后昵称", updated.getNickName(), "昵称未更新");
        assertEquals("updated@example.com", updated.getEmail(), "邮箱未更新");
        assertEquals("1", updated.getSex(), "性别未更新");
        System.out.println("修改APP用户信息成功: " + updated.getNickName());
    }

    /**
     * 测试重置APP用户密码
     */
    @Test
    public void testResetAppUserPwd()
    {
        // 先注册用户
        String userName = "pwduser" + System.currentTimeMillis();
        AppUser appUser = new AppUser();
        appUser.setUserName(userName);
        appUser.setNickName("密码测试用户");
        appUser.setPassword("oldpassword");
        appUser.setEmail("pwd@example.com");
        appUser.setPhonenumber("13800138004");
        appUser.setSex("0");
        appUser.setStatus("0");
        appUser.setCreateBy("system");
        appUserService.registerAppUser(appUser);

        // 重置密码
        boolean result = appUserService.resetAppUserPwd(userName, "newpassword");
        assertTrue(result, "重置密码失败");

        // 验证密码已修改（通过查询用户确认操作成功）
        AppUser updated = appUserService.selectAppUserByUserName(userName);
        assertNotNull(updated, "用户应存在");
        System.out.println("重置APP用户密码成功");
    }

    /**
     * 测试修改APP用户头像
     */
    @Test
    public void testUpdateAppUserAvatar()
    {
        // 先注册用户
        String userName = "avataruser" + System.currentTimeMillis();
        AppUser appUser = new AppUser();
        appUser.setUserName(userName);
        appUser.setNickName("头像测试用户");
        appUser.setPassword("123456");
        appUser.setEmail("avatar@example.com");
        appUser.setPhonenumber("13800138005");
        appUser.setSex("0");
        appUser.setStatus("0");
        appUser.setCreateBy("system");
        appUserService.registerAppUser(appUser);

        // 修改头像
        String newAvatar = "https://example.com/avatar.jpg";
        boolean result = appUserService.updateAppUserAvatar(userName, newAvatar);
        assertTrue(result, "修改头像失败");
        System.out.println("修改APP用户头像成功");
    }

    /**
     * 测试删除APP用户
     */
    @Test
    public void testDeleteAppUserById()
    {
        // 先注册用户
        AppUser appUser = new AppUser();
        appUser.setUserName("deleteuser" + System.currentTimeMillis());
        appUser.setNickName("删除测试用户");
        appUser.setPassword("123456");
        appUser.setEmail("delete@example.com");
        appUser.setPhonenumber("13800138006");
        appUser.setSex("0");
        appUser.setStatus("0");
        appUser.setCreateBy("system");
        appUserService.registerAppUser(appUser);

        Long userId = appUser.getUserId();
        assertNotNull(userId, "用户ID不应为空");

        // 删除用户
        int result = appUserService.deleteAppUserById(userId);
        assertTrue(result > 0, "删除用户失败");
        System.out.println("删除APP用户成功，ID: " + userId);
    }

    /**
     * 测试批量删除APP用户
     */
    @Test
    public void testDeleteAppUserByIds()
    {
        // 先注册多个用户
        Long[] userIds = new Long[3];
        for (int i = 0; i < 3; i++) {
            AppUser appUser = new AppUser();
            appUser.setUserName("batchdelete" + i + System.currentTimeMillis());
            appUser.setNickName("批量删除测试用户" + i);
            appUser.setPassword("123456");
            appUser.setEmail("batch" + i + "@example.com");
            appUser.setPhonenumber("138001380" + (20 + i));
            appUser.setSex("0");
            appUser.setStatus("0");
            appUser.setCreateBy("system");
            appUserService.registerAppUser(appUser);
            userIds[i] = appUser.getUserId();
        }

        // 批量删除
        int result = appUserService.deleteAppUserByIds(userIds);
        assertTrue(result == 3, "批量删除应删除3条记录");
        System.out.println("批量删除APP用户成功，共删除" + result + "条记录");
    }

    /**
     * 测试校验用户名唯一性
     */
    @Test
    public void testCheckUserNameUnique()
    {
        // 先注册用户
        String userName = "uniqueuser" + System.currentTimeMillis();
        AppUser appUser = new AppUser();
        appUser.setUserName(userName);
        appUser.setNickName("唯一性测试用户");
        appUser.setPassword("123456");
        appUser.setEmail("unique@example.com");
        appUser.setPhonenumber("13800138007");
        appUser.setSex("0");
        appUser.setStatus("0");
        appUser.setCreateBy("system");
        appUserService.registerAppUser(appUser);

        // 校验已存在的用户名
        AppUser checkUser = new AppUser();
        checkUser.setUserName(userName);
        boolean isUnique = appUserService.checkUserNameUnique(checkUser);
        assertFalse(isUnique, "已存在的用户名应返回不唯一");

        // 校验不存在的用户名
        checkUser.setUserName("nonexistent" + System.currentTimeMillis());
        isUnique = appUserService.checkUserNameUnique(checkUser);
        assertTrue(isUnique, "不存在的用户名应返回唯一");

        System.out.println("校验用户名唯一性测试成功");
    }

    /**
     * 测试校验手机号唯一性
     */
    @Test
    public void testCheckPhoneUnique()
    {
        // 先注册用户
        String phone = "138001380" + (System.currentTimeMillis() % 100);
        AppUser appUser = new AppUser();
        appUser.setUserName("phoneuser" + System.currentTimeMillis());
        appUser.setNickName("手机号测试用户");
        appUser.setPassword("123456");
        appUser.setEmail("phone@example.com");
        appUser.setPhonenumber(phone);
        appUser.setSex("0");
        appUser.setStatus("0");
        appUser.setCreateBy("system");
        appUserService.registerAppUser(appUser);

        // 校验已存在的手机号
        AppUser checkUser = new AppUser();
        checkUser.setPhonenumber(phone);
        boolean isUnique = appUserService.checkPhoneUnique(checkUser);
        assertFalse(isUnique, "已存在的手机号应返回不唯一");

        System.out.println("校验手机号唯一性测试成功");
    }

    /**
     * 测试校验邮箱唯一性
     */
    @Test
    public void testCheckEmailUnique()
    {
        // 先注册用户
        String email = "email" + System.currentTimeMillis() + "@example.com";
        AppUser appUser = new AppUser();
        appUser.setUserName("emailuser" + System.currentTimeMillis());
        appUser.setNickName("邮箱测试用户");
        appUser.setPassword("123456");
        appUser.setEmail(email);
        appUser.setPhonenumber("13800138008");
        appUser.setSex("0");
        appUser.setStatus("0");
        appUser.setCreateBy("system");
        appUserService.registerAppUser(appUser);

        // 校验已存在的邮箱
        AppUser checkUser = new AppUser();
        checkUser.setEmail(email);
        boolean isUnique = appUserService.checkEmailUnique(checkUser);
        assertFalse(isUnique, "已存在的邮箱应返回不唯一");

        System.out.println("校验邮箱唯一性测试成功");
    }

    /**
     * 测试记录登录信息
     */
    @Test
    public void testRecordLoginInfo()
    {
        // 先注册用户
        AppUser appUser = new AppUser();
        appUser.setUserName("loginuser" + System.currentTimeMillis());
        appUser.setNickName("登录测试用户");
        appUser.setPassword("123456");
        appUser.setEmail("login@example.com");
        appUser.setPhonenumber("13800138009");
        appUser.setSex("0");
        appUser.setStatus("0");
        appUser.setCreateBy("system");
        appUserService.registerAppUser(appUser);

        // 记录登录信息
        appUser.setLoginIp("192.168.1.1");
        appUser.setLoginDate(new Date());
        appUserService.recordLoginInfo(appUser);

        // 验证登录信息已记录
        AppUser updated = appUserService.selectAppUserById(appUser.getUserId());
        assertEquals("192.168.1.1", updated.getLoginIp(), "登录IP应已更新");
        assertNotNull(updated.getLoginDate(), "登录时间应已更新");
        System.out.println("记录登录信息测试成功");
    }
}
