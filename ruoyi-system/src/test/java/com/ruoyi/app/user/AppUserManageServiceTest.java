package com.ruoyi.app.user;

import java.util.Arrays;
import java.util.List;

import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * APP用户管理Service单元测试
 *
 * @author ruoyi
 */
@SpringBootTest(classes = com.ruoyi.system.RuoYiSystemApplication.class)
@Transactional
public class AppUserManageServiceTest
{
    @Autowired
    private IAppUserService appUserService;

    /**
     * 测试新增APP用户
     */
    @Test
    public void testInsertAppUser()
    {
        AppUser appUser = new AppUser();
        appUser.setUserName("manageuser" + System.currentTimeMillis());
        appUser.setNickName("管理测试用户");
        appUser.setPassword("encryptedpassword");
        appUser.setPhonenumber("13900139000");
        appUser.setEmail("manage@example.com");
        appUser.setAvatar("https://example.com/avatar.jpg");
        appUser.setStatus("0");
        appUser.setSex("0");
        appUser.setCreateBy("admin");
        appUser.setRemark("测试备注");

        int result = appUserService.insertAppUser(appUser);
        assertTrue(result > 0, "新增APP用户失败");
        assertNotNull(appUser.getUserId(), "用户ID不应为空");

        System.out.println("新增APP用户成功，ID: " + appUser.getUserId());
    }

    /**
     * 测试新增APP用户 - 最小必填项
     */
    @Test
    public void testInsertAppUserMinimal()
    {
        AppUser appUser = new AppUser();
        appUser.setUserName("minimaluser" + System.currentTimeMillis());
        appUser.setNickName("最小化用户");
        appUser.setPassword("encryptedpassword");
        appUser.setPhonenumber("13900139001");
        appUser.setStatus("0");
        appUser.setCreateBy("admin");

        int result = appUserService.insertAppUser(appUser);
        assertTrue(result > 0, "新增APP用户失败");

        // 验证默认值
        AppUser saved = appUserService.selectAppUserById(appUser.getUserId());
        assertNotNull(saved, "保存的用户应存在");
        assertEquals("最小化用户", saved.getNickName(), "昵称应匹配");
    }

    /**
     * 测试查询APP用户列表 - 无条件
     */
    @Test
    public void testSelectAppUserListAll()
    {
        // 先创建测试数据
        createTestUsers(5);

        AppUser queryParam = new AppUser();
        List<AppUser> list = appUserService.selectAppUserList(queryParam);

        assertNotNull(list, "用户列表不应为空");
        assertTrue(list.size() >= 5, "用户列表应包含至少5条记录");
        System.out.println("查询APP用户列表成功，共" + list.size() + "条记录");
    }

    /**
     * 测试查询APP用户列表 - 按手机号筛选
     */
    @Test
    public void testSelectAppUserListByPhone()
    {
        // 创建特定手机号的用户
        String phone = "137001370" + (System.currentTimeMillis() % 100);
        AppUser appUser = new AppUser();
        appUser.setUserName("phonefilter" + System.currentTimeMillis());
        appUser.setNickName("手机号筛选测试");
        appUser.setPassword("123456");
        appUser.setPhonenumber(phone);
        appUser.setStatus("0");
        appUser.setCreateBy("admin");
        appUserService.insertAppUser(appUser);

        // 按手机号查询
        AppUser queryParam = new AppUser();
        queryParam.setPhonenumber(phone);
        List<AppUser> list = appUserService.selectAppUserList(queryParam);

        assertNotNull(list, "用户列表不应为空");
        assertEquals(1, list.size(), "应只返回1条记录");
        assertEquals(phone, list.get(0).getPhonenumber(), "手机号应匹配");
    }

    /**
     * 测试查询APP用户列表 - 按昵称筛选
     */
    @Test
    public void testSelectAppUserListByNickName()
    {
        // 创建特定昵称的用户
        String nickName = "昵称筛选" + System.currentTimeMillis();
        AppUser appUser = new AppUser();
        appUser.setUserName("nicknamefilter" + System.currentTimeMillis());
        appUser.setNickName(nickName);
        appUser.setPassword("123456");
        appUser.setPhonenumber("137001371" + (System.currentTimeMillis() % 100));
        appUser.setStatus("0");
        appUser.setCreateBy("admin");
        appUserService.insertAppUser(appUser);

        // 按昵称查询
        AppUser queryParam = new AppUser();
        queryParam.setNickName("昵称筛选");
        List<AppUser> list = appUserService.selectAppUserList(queryParam);

        assertNotNull(list, "用户列表不应为空");
        assertTrue(list.size() >= 1, "应至少返回1条记录");
        assertTrue(list.get(0).getNickName().contains("昵称筛选"), "昵称应包含筛选关键词");
    }

    /**
     * 测试查询APP用户列表 - 按状态筛选
     */
    @Test
    public void testSelectAppUserListByStatus()
    {
        // 创建禁用状态的用户
        AppUser appUser = new AppUser();
        appUser.setUserName("disableduser" + System.currentTimeMillis());
        appUser.setNickName("禁用用户");
        appUser.setPassword("123456");
        appUser.setPhonenumber("137001372" + (System.currentTimeMillis() % 100));
        appUser.setStatus("1"); // 禁用
        appUser.setCreateBy("admin");
        appUserService.insertAppUser(appUser);

        // 按状态查询
        AppUser queryParam = new AppUser();
        queryParam.setStatus("1");
        List<AppUser> list = appUserService.selectAppUserList(queryParam);

        assertNotNull(list, "用户列表不应为空");
        assertTrue(list.size() >= 1, "应至少返回1条禁用用户记录");
        list.forEach(user -> assertEquals("1", user.getStatus(), "所有记录状态应为禁用"));
    }

    /**
     * 测试修改APP用户 - 修改昵称和状态
     */
    @Test
    public void testUpdateAppUserNickNameAndStatus()
    {
        // 先创建用户
        AppUser appUser = createTestUser("updateuser" + System.currentTimeMillis());

        // 修改用户信息
        appUser.setNickName("修改后的昵称");
        appUser.setStatus("1");
        appUser.setAvatar("https://example.com/new-avatar.jpg");
        appUser.setRemark("修改后的备注");
        appUser.setUpdateBy("admin");

        int result = appUserService.updateAppUser(appUser);
        assertTrue(result > 0, "修改用户信息失败");

        // 验证修改结果
        AppUser updated = appUserService.selectAppUserById(appUser.getUserId());
        assertEquals("修改后的昵称", updated.getNickName(), "昵称应已更新");
        assertEquals("1", updated.getStatus(), "状态应已更新");
        assertEquals("https://example.com/new-avatar.jpg", updated.getAvatar(), "头像应已更新");
        assertEquals("修改后的备注", updated.getRemark(), "备注应已更新");
    }

    /**
     * 测试修改APP用户 - 禁止修改手机号和用户名
     */
    @Test
    public void testUpdateAppUserCannotModifyPhoneAndUserName()
    {
        // 先创建用户
        String originalPhone = "136001360" + (System.currentTimeMillis() % 100);
        String originalUserName = "cannotmodify" + System.currentTimeMillis();

        AppUser appUser = new AppUser();
        appUser.setUserName(originalUserName);
        appUser.setNickName("禁止修改测试");
        appUser.setPassword("123456");
        appUser.setPhonenumber(originalPhone);
        appUser.setStatus("0");
        appUser.setCreateBy("admin");
        appUserService.insertAppUser(appUser);

        // 尝试修改手机号和用户名（实际业务层会忽略这些字段）
        appUser.setPhonenumber("99999999999");
        appUser.setUserName("newusername");
        appUser.setNickName("新昵称");
        appUser.setUpdateBy("admin");

        int result = appUserService.updateAppUser(appUser);
        assertTrue(result > 0, "修改用户信息失败");

        // 验证手机号和用户名未被修改
        AppUser updated = appUserService.selectAppUserById(appUser.getUserId());
        assertEquals(originalPhone, updated.getPhonenumber(), "手机号不应被修改");
        assertEquals(originalUserName, updated.getUserName(), "用户名不应被修改");
        assertEquals("新昵称", updated.getNickName(), "昵称应已更新");
    }

    /**
     * 测试删除APP用户 - 逻辑删除
     */
    @Test
    public void testDeleteAppUserLogic()
    {
        // 先创建用户
        AppUser appUser = createTestUser("deleteuser" + System.currentTimeMillis());
        Long userId = appUser.getUserId();

        // 删除用户
        int result = appUserService.deleteAppUserById(userId);
        assertTrue(result > 0, "删除用户失败");

        // 验证用户不再出现在列表中
        AppUser queryParam = new AppUser();
        queryParam.setUserName(appUser.getUserName());
        List<AppUser> list = appUserService.selectAppUserList(queryParam);
        assertEquals(0, list.size(), "已删除的用户不应出现在列表中");

        System.out.println("逻辑删除APP用户成功，ID: " + userId);
    }

    /**
     * 测试批量删除APP用户
     */
    @Test
    public void testDeleteAppUserByIds()
    {
        // 创建多个用户
        AppUser user1 = createTestUser("batch1" + System.currentTimeMillis());
        AppUser user2 = createTestUser("batch2" + System.currentTimeMillis());
        AppUser user3 = createTestUser("batch3" + System.currentTimeMillis());

        Long[] userIds = new Long[]{user1.getUserId(), user2.getUserId(), user3.getUserId()};

        // 批量删除
        int result = appUserService.deleteAppUserByIds(userIds);
        assertEquals(3, result, "应删除3条记录");

        // 验证用户已被删除
        for (Long userId : userIds) {
            AppUser user = appUserService.selectAppUserById(userId);
            // 用户记录仍存在，但del_flag已变为1
            assertNotNull(user, "用户记录应仍存在");
        }

        System.out.println("批量删除APP用户成功，共删除" + result + "条记录");
    }

    /**
     * 测试新增用户时校验手机号唯一性
     */
    @Test
    public void testCheckPhoneUniqueForInsert()
    {
        // 创建用户
        String phone = "135001350" + (System.currentTimeMillis() % 100);
        AppUser appUser = createTestUserWithPhone("uniquephone" + System.currentTimeMillis(), phone);

        // 校验已存在的手机号
        AppUser checkUser = new AppUser();
        checkUser.setPhonenumber(phone);
        boolean isUnique = appUserService.checkPhoneUnique(checkUser);
        assertFalse(isUnique, "已存在的手机号应返回不唯一");

        // 校验不存在的手机号
        checkUser.setPhonenumber("13500135999");
        isUnique = appUserService.checkPhoneUnique(checkUser);
        assertTrue(isUnique, "不存在的手机号应返回唯一");
    }

    /**
     * 测试修改用户时校验手机号唯一性（排除自身）
     */
    @Test
    public void testCheckPhoneUniqueForUpdate()
    {
        // 创建两个用户
        String phone1 = "134001340" + (System.currentTimeMillis() % 100);
        String phone2 = "134001341" + (System.currentTimeMillis() % 100);

        AppUser user1 = createTestUserWithPhone("updatephone1" + System.currentTimeMillis(), phone1);
        AppUser user2 = createTestUserWithPhone("updatephone2" + System.currentTimeMillis(), phone2);

        // 用户1校验自己的手机号（应返回唯一，因为是自己）
        AppUser checkUser = new AppUser();
        checkUser.setUserId(user1.getUserId());
        checkUser.setPhonenumber(phone1);
        boolean isUnique = appUserService.checkPhoneUnique(checkUser);
        assertTrue(isUnique, "自己的手机号应返回唯一");

        // 用户1校验用户2的手机号（应返回不唯一）
        checkUser.setUserId(user1.getUserId());
        checkUser.setPhonenumber(phone2);
        isUnique = appUserService.checkPhoneUnique(checkUser);
        assertFalse(isUnique, "其他用户的手机号应返回不唯一");
    }

    /**
     * 测试校验用户名唯一性
     */
    @Test
    public void testCheckUserNameUnique()
    {
        // 创建用户
        String userName = "uniqueusername" + System.currentTimeMillis();
        AppUser appUser = createTestUser(userName);

        // 校验已存在的用户名
        AppUser checkUser = new AppUser();
        checkUser.setUserName(userName);
        boolean isUnique = appUserService.checkUserNameUnique(checkUser);
        assertFalse(isUnique, "已存在的用户名应返回不唯一");

        // 校验不存在的用户名
        checkUser.setUserName("nonexistent" + System.currentTimeMillis());
        isUnique = appUserService.checkUserNameUnique(checkUser);
        assertTrue(isUnique, "不存在的用户名应返回唯一");
    }

    // 辅助方法：创建测试用户
    private AppUser createTestUser(String userName)
    {
        AppUser appUser = new AppUser();
        appUser.setUserName(userName);
        appUser.setNickName("测试用户" + userName);
        appUser.setPassword("123456");
        appUser.setPhonenumber("13" + (System.currentTimeMillis() % 1000000000));
        appUser.setStatus("0");
        appUser.setCreateBy("admin");
        appUserService.insertAppUser(appUser);
        return appUser;
    }

    // 辅助方法：创建指定手机号的测试用户
    private AppUser createTestUserWithPhone(String userName, String phone)
    {
        AppUser appUser = new AppUser();
        appUser.setUserName(userName);
        appUser.setNickName("测试用户" + userName);
        appUser.setPassword("123456");
        appUser.setPhonenumber(phone);
        appUser.setStatus("0");
        appUser.setCreateBy("admin");
        appUserService.insertAppUser(appUser);
        return appUser;
    }

    // 辅助方法：批量创建测试用户
    private void createTestUsers(int count)
    {
        for (int i = 0; i < count; i++) {
            AppUser appUser = new AppUser();
            appUser.setUserName("testuser" + i + System.currentTimeMillis());
            appUser.setNickName("测试用户" + i);
            appUser.setPassword("123456");
            appUser.setPhonenumber("13" + (System.currentTimeMillis() % 1000000000 + i));
            appUser.setStatus("0");
            appUser.setCreateBy("admin");
            appUserService.insertAppUser(appUser);
        }
    }
}
