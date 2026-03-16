package com.ruoyi.app.user.mapper;

import java.util.List;

import com.ruoyi.app.user.domain.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * APP用户Mapper单元测试
 *
 * @author ruoyi
 */
@SpringBootTest(classes = com.ruoyi.system.RuoYiSystemApplication.class)
@Transactional
public class AppUserMapperTest
{
    @Autowired
    private AppUserMapper appUserMapper;

    /**
     * 测试插入APP用户
     */
    @Test
    public void testInsertAppUser()
    {
        AppUser appUser = new AppUser();
        appUser.setUserName("mapperuser" + System.currentTimeMillis());
        appUser.setNickName("Mapper测试用户");
        appUser.setPassword("encryptedpassword");
        appUser.setPhonenumber("13100131000");
        appUser.setEmail("mapper@example.com");
        appUser.setAvatar("https://example.com/avatar.jpg");
        appUser.setStatus("0");
        appUser.setSex("0");
        appUser.setCreateBy("admin");
        appUser.setRemark("Mapper测试备注");

        int result = appUserMapper.insertAppUser(appUser);
        assertTrue(result > 0, "插入APP用户失败");
        assertNotNull(appUser.getUserId(), "用户ID不应为空");

        System.out.println("插入APP用户成功，ID: " + appUser.getUserId());
    }

    /**
     * 测试根据ID查询APP用户
     */
    @Test
    public void testSelectAppUserById()
    {
        // 先插入用户
        AppUser appUser = createTestUser("selectbyid" + System.currentTimeMillis());
        Long userId = appUser.getUserId();

        // 查询用户
        AppUser result = appUserMapper.selectAppUserById(userId);
        assertNotNull(result, "查询结果不应为空");
        assertEquals(userId, result.getUserId(), "用户ID应匹配");
        assertEquals(appUser.getUserName(), result.getUserName(), "用户名应匹配");
        assertEquals(appUser.getNickName(), result.getNickName(), "昵称应匹配");
        assertEquals(appUser.getPhonenumber(), result.getPhonenumber(), "手机号应匹配");
    }

    /**
     * 测试根据用户名查询APP用户
     */
    @Test
    public void testSelectAppUserByUserName()
    {
        // 先插入用户
        String userName = "selectbyname" + System.currentTimeMillis();
        AppUser appUser = createTestUser(userName);

        // 查询用户
        AppUser result = appUserMapper.selectAppUserByUserName(userName);
        assertNotNull(result, "查询结果不应为空");
        assertEquals(userName, result.getUserName(), "用户名应匹配");
    }

    /**
     * 测试查询APP用户列表 - 无条件
     */
    @Test
    public void testSelectAppUserListAll()
    {
        // 创建测试数据
        createTestUsers(3);

        AppUser queryParam = new AppUser();
        List<AppUser> list = appUserMapper.selectAppUserList(queryParam);

        assertNotNull(list, "用户列表不应为空");
        assertTrue(list.size() >= 3, "用户列表应包含至少3条记录");
    }

    /**
     * 测试查询APP用户列表 - 按用户名模糊查询
     */
    @Test
    public void testSelectAppUserListByUserNameLike()
    {
        // 创建特定用户名的用户
        String userName = "listusertest" + System.currentTimeMillis();
        createTestUser(userName);

        // 模糊查询
        AppUser queryParam = new AppUser();
        queryParam.setUserName("listusertest");
        List<AppUser> list = appUserMapper.selectAppUserList(queryParam);

        assertNotNull(list, "用户列表不应为空");
        assertTrue(list.size() >= 1, "应至少返回1条记录");
        assertTrue(list.get(0).getUserName().contains("listusertest"), "用户名应包含查询关键词");
    }

    /**
     * 测试查询APP用户列表 - 按昵称模糊查询
     */
    @Test
    public void testSelectAppUserListByNickNameLike()
    {
        // 创建特定昵称的用户
        String nickName = "昵称查询测试" + System.currentTimeMillis();
        AppUser appUser = new AppUser();
        appUser.setUserName("nicknamequery" + System.currentTimeMillis());
        appUser.setNickName(nickName);
        appUser.setPassword("123456");
        appUser.setPhonenumber("13100131001");
        appUser.setStatus("0");
        appUser.setCreateBy("admin");
        appUserMapper.insertAppUser(appUser);

        // 模糊查询
        AppUser queryParam = new AppUser();
        queryParam.setNickName("昵称查询测试");
        List<AppUser> list = appUserMapper.selectAppUserList(queryParam);

        assertNotNull(list, "用户列表不应为空");
        assertTrue(list.size() >= 1, "应至少返回1条记录");
    }

    /**
     * 测试查询APP用户列表 - 按手机号模糊查询
     */
    @Test
    public void testSelectAppUserListByPhoneLike()
    {
        // 创建特定手机号的用户
        String phone = "131001310" + (System.currentTimeMillis() % 100);
        AppUser appUser = new AppUser();
        appUser.setUserName("phonequery" + System.currentTimeMillis());
        appUser.setNickName("手机号查询测试");
        appUser.setPassword("123456");
        appUser.setPhonenumber(phone);
        appUser.setStatus("0");
        appUser.setCreateBy("admin");
        appUserMapper.insertAppUser(appUser);

        // 模糊查询
        AppUser queryParam = new AppUser();
        queryParam.setPhonenumber(phone);
        List<AppUser> list = appUserMapper.selectAppUserList(queryParam);

        assertNotNull(list, "用户列表不应为空");
        assertTrue(list.size() >= 1, "应至少返回1条记录");
    }

    /**
     * 测试查询APP用户列表 - 按状态筛选
     */
    @Test
    public void testSelectAppUserListByStatus()
    {
        // 创建禁用状态的用户
        AppUser appUser = new AppUser();
        appUser.setUserName("statusquery" + System.currentTimeMillis());
        appUser.setNickName("状态查询测试");
        appUser.setPassword("123456");
        appUser.setPhonenumber("131001310" + (System.currentTimeMillis() % 100));
        appUser.setStatus("1"); // 禁用
        appUser.setCreateBy("admin");
        appUserMapper.insertAppUser(appUser);

        // 按状态查询
        AppUser queryParam = new AppUser();
        queryParam.setStatus("1");
        List<AppUser> list = appUserMapper.selectAppUserList(queryParam);

        assertNotNull(list, "用户列表不应为空");
        list.forEach(user -> assertEquals("1", user.getStatus(), "所有记录状态应为禁用"));
    }

    /**
     * 测试查询APP用户列表 - 按时间范围筛选
     */
    @Test
    public void testSelectAppUserListByTimeRange()
    {
        // 创建用户
        createTestUser("timerange" + System.currentTimeMillis());

        // 按时间范围查询
        AppUser queryParam = new AppUser();
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("beginTime", "2020-01-01");
        params.put("endTime", "2099-12-31");
        queryParam.setParams(params);

        List<AppUser> list = appUserMapper.selectAppUserList(queryParam);
        assertNotNull(list, "用户列表不应为空");
        assertTrue(list.size() >= 1, "应至少返回1条记录");
    }

    /**
     * 测试查询APP用户列表 - 排序
     */
    @Test
    public void testSelectAppUserListWithOrderBy()
    {
        // 创建多个用户
        createTestUsers(3);

        // 按创建时间降序排序
        AppUser queryParam = new AppUser();
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("orderByColumn", "create_time");
        params.put("isAsc", "desc");
        queryParam.setParams(params);

        List<AppUser> list = appUserMapper.selectAppUserList(queryParam);
        assertNotNull(list, "用户列表不应为空");
        assertTrue(list.size() >= 3, "应至少返回3条记录");

        // 验证排序（降序）
        for (int i = 1; i < list.size(); i++) {
            assertTrue(list.get(i - 1).getCreateTime().compareTo(list.get(i).getCreateTime()) >= 0,
                    "列表应按创建时间降序排列");
        }
    }

    /**
     * 测试更新APP用户
     */
    @Test
    public void testUpdateAppUser()
    {
        // 先插入用户
        AppUser appUser = createTestUser("updatemapper" + System.currentTimeMillis());

        // 更新用户信息
        appUser.setNickName("更新后的昵称");
        appUser.setEmail("updated@example.com");
        appUser.setAvatar("https://example.com/updated-avatar.jpg");
        appUser.setStatus("1");
        appUser.setRemark("更新后的备注");
        appUser.setUpdateBy("admin");

        int result = appUserMapper.updateAppUser(appUser);
        assertTrue(result > 0, "更新用户失败");

        // 验证更新结果
        AppUser updated = appUserMapper.selectAppUserById(appUser.getUserId());
        assertEquals("更新后的昵称", updated.getNickName(), "昵称应已更新");
        assertEquals("updated@example.com", updated.getEmail(), "邮箱应已更新");
        assertEquals("https://example.com/updated-avatar.jpg", updated.getAvatar(), "头像应已更新");
        assertEquals("1", updated.getStatus(), "状态应已更新");
        assertEquals("更新后的备注", updated.getRemark(), "备注应已更新");
    }

    /**
     * 测试更新用户头像
     */
    @Test
    public void testUpdateAppUserAvatar()
    {
        // 先插入用户
        String userName = "avatarupdate" + System.currentTimeMillis();
        AppUser appUser = createTestUser(userName);

        // 更新头像
        String newAvatar = "https://example.com/new-avatar.jpg";
        int result = appUserMapper.updateAppUserAvatar(userName, newAvatar);
        assertTrue(result > 0, "更新头像失败");

        // 验证更新结果
        AppUser updated = appUserMapper.selectAppUserByUserName(userName);
        assertEquals(newAvatar, updated.getAvatar(), "头像应已更新");
    }

    /**
     * 测试重置用户密码
     */
    @Test
    public void testResetAppUserPwd()
    {
        // 先插入用户
        String userName = "pwdreset" + System.currentTimeMillis();
        AppUser appUser = createTestUser(userName);

        // 重置密码
        String newPassword = "newencryptedpassword";
        int result = appUserMapper.resetAppUserPwd(userName, newPassword);
        assertTrue(result > 0, "重置密码失败");
    }

    /**
     * 测试删除APP用户 - 逻辑删除
     */
    @Test
    public void testDeleteAppUserById()
    {
        // 先插入用户
        AppUser appUser = createTestUser("deletemapper" + System.currentTimeMillis());
        Long userId = appUser.getUserId();

        // 删除用户
        int result = appUserMapper.deleteAppUserById(userId);
        assertTrue(result > 0, "删除用户失败");

        // 验证用户不再出现在列表中
        AppUser queryParam = new AppUser();
        queryParam.setUserName(appUser.getUserName());
        List<AppUser> list = appUserMapper.selectAppUserList(queryParam);
        assertEquals(0, list.size(), "已删除的用户不应出现在列表中");
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
        int result = appUserMapper.deleteAppUserByIds(userIds);
        assertEquals(3, result, "应删除3条记录");

        // 验证用户已被删除
        for (Long userId : userIds) {
            AppUser user = appUserMapper.selectAppUserById(userId);
            // 用户记录仍存在，但del_flag已变为1
            assertNotNull(user, "用户记录应仍存在");
        }
    }

    /**
     * 测试校验用户名唯一性
     */
    @Test
    public void testCheckUserNameUnique()
    {
        // 创建用户
        String userName = "uniquecheck" + System.currentTimeMillis();
        createTestUser(userName);

        // 校验已存在的用户名
        int count = appUserMapper.checkUserNameUnique(userName);
        assertTrue(count > 0, "已存在的用户名应返回大于0");

        // 校验不存在的用户名
        count = appUserMapper.checkUserNameUnique("nonexistent" + System.currentTimeMillis());
        assertEquals(0, count, "不存在的用户名应返回0");
    }

    /**
     * 测试校验手机号唯一性
     */
    @Test
    public void testCheckPhoneUnique()
    {
        // 创建用户
        String phone = "131001310" + (System.currentTimeMillis() % 100);
        AppUser appUser = new AppUser();
        appUser.setUserName("phoneunique" + System.currentTimeMillis());
        appUser.setNickName("手机号唯一性测试");
        appUser.setPassword("123456");
        appUser.setPhonenumber(phone);
        appUser.setStatus("0");
        appUser.setCreateBy("admin");
        appUserMapper.insertAppUser(appUser);

        // 校验已存在的手机号
        AppUser result = appUserMapper.checkPhoneUnique(phone);
        assertNotNull(result, "已存在的手机号应返回用户对象");

        // 校验不存在的手机号
        result = appUserMapper.checkPhoneUnique("13100131999");
        assertNull(result, "不存在的手机号应返回null");
    }

    /**
     * 测试校验邮箱唯一性
     */
    @Test
    public void testCheckEmailUnique()
    {
        // 创建用户
        String email = "email" + System.currentTimeMillis() + "@example.com";
        AppUser appUser = new AppUser();
        appUser.setUserName("emailunique" + System.currentTimeMillis());
        appUser.setNickName("邮箱唯一性测试");
        appUser.setPassword("123456");
        appUser.setEmail(email);
        appUser.setPhonenumber("131001310" + (System.currentTimeMillis() % 100));
        appUser.setStatus("0");
        appUser.setCreateBy("admin");
        appUserMapper.insertAppUser(appUser);

        // 校验已存在的邮箱
        AppUser result = appUserMapper.checkEmailUnique(email);
        assertNotNull(result, "已存在的邮箱应返回用户对象");

        // 校验不存在的邮箱
        result = appUserMapper.checkEmailUnique("nonexistent" + System.currentTimeMillis() + "@example.com");
        assertNull(result, "不存在的邮箱应返回null");
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
        appUserMapper.insertAppUser(appUser);
        return appUser;
    }

    // 辅助方法：批量创建测试用户
    private void createTestUsers(int count)
    {
        for (int i = 0; i < count; i++) {
            AppUser appUser = new AppUser();
            appUser.setUserName("mapperuser" + i + System.currentTimeMillis());
            appUser.setNickName("Mapper测试用户" + i);
            appUser.setPassword("123456");
            appUser.setPhonenumber("13" + (System.currentTimeMillis() % 1000000000 + i));
            appUser.setStatus("0");
            appUser.setCreateBy("admin");
            appUserMapper.insertAppUser(appUser);
        }
    }
}
