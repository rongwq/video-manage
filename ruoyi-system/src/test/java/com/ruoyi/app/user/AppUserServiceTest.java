package com.ruoyi.app.user;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.mapper.AppUserMapper;
import com.ruoyi.app.user.service.impl.AppUserServiceImpl;
import com.ruoyi.common.constant.UserConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest
{
    @Mock
    private AppUserMapper appUserMapper;

    @InjectMocks
    private AppUserServiceImpl appUserService;

    private AppUser testUser;

    @BeforeEach
    public void setUp()
    {
        testUser = new AppUser();
        testUser.setUserId(1L);
        testUser.setUserName("testuser");
        testUser.setNickName("测试用户");
        testUser.setPhonenumber("13800138000");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setStatus("0");
        testUser.setDelFlag("0");
        testUser.setCreateBy("admin");
    }

    @Test
    public void testSelectAppUserList()
    {
        List<AppUser> mockList = Arrays.asList(testUser);
        when(appUserMapper.selectAppUserList(any(AppUser.class))).thenReturn(mockList);

        AppUser queryParam = new AppUser();
        queryParam.setStatus("0");
        List<AppUser> result = appUserService.selectAppUserList(queryParam);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUserName());
        verify(appUserMapper, times(1)).selectAppUserList(queryParam);
        System.out.println("testSelectAppUserList 通过");
    }

    @Test
    public void testSelectAppUserByUserName()
    {
        when(appUserMapper.selectAppUserByUserName("testuser")).thenReturn(testUser);

        AppUser result = appUserService.selectAppUserByUserName("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUserName());
        assertEquals("测试用户", result.getNickName());
        verify(appUserMapper, times(1)).selectAppUserByUserName("testuser");
        System.out.println("testSelectAppUserByUserName 通过");
    }

    @Test
    public void testSelectAppUserById()
    {
        when(appUserMapper.selectAppUserById(1L)).thenReturn(testUser);

        AppUser result = appUserService.selectAppUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("testuser", result.getUserName());
        verify(appUserMapper, times(1)).selectAppUserById(1L);
        System.out.println("testSelectAppUserById 通过");
    }

    @Test
    public void testRegisterAppUser()
    {
        when(appUserMapper.insertAppUser(any(AppUser.class))).thenReturn(1);

        AppUser newUser = new AppUser();
        newUser.setUserName("newuser");
        newUser.setPassword("123456");

        boolean result = appUserService.registerAppUser(newUser);

        assertTrue(result);
        verify(appUserMapper, times(1)).insertAppUser(any(AppUser.class));
        System.out.println("testRegisterAppUser 通过");
    }

    @Test
    public void testInsertAppUser()
    {
        when(appUserMapper.insertAppUser(any(AppUser.class))).thenReturn(1);

        AppUser newUser = new AppUser();
        newUser.setUserName("newuser");
        newUser.setNickName("新用户");
        newUser.setPassword("123456");

        int result = appUserService.insertAppUser(newUser);

        assertEquals(1, result);
        verify(appUserMapper, times(1)).insertAppUser(any(AppUser.class));
        System.out.println("testInsertAppUser 通过");
    }

    @Test
    public void testUpdateAppUser()
    {
        when(appUserMapper.updateAppUser(any(AppUser.class))).thenReturn(1);

        testUser.setNickName("更新后昵称");
        int result = appUserService.updateAppUser(testUser);

        assertEquals(1, result);
        verify(appUserMapper, times(1)).updateAppUser(testUser);
        System.out.println("testUpdateAppUser 通过");
    }

    @Test
    public void testUpdateAppUserAvatar()
    {
        when(appUserMapper.updateAppUserAvatar("testuser", "avatar.jpg")).thenReturn(1);

        boolean result = appUserService.updateAppUserAvatar("testuser", "avatar.jpg");

        assertTrue(result);
        verify(appUserMapper, times(1)).updateAppUserAvatar("testuser", "avatar.jpg");
        System.out.println("testUpdateAppUserAvatar 通过");
    }

    @Test
    public void testResetAppUserPwd()
    {
        when(appUserMapper.resetAppUserPwd(eq("testuser"), anyString())).thenReturn(1);

        boolean result = appUserService.resetAppUserPwd("testuser", "newpassword");

        assertTrue(result);
        verify(appUserMapper, times(1)).resetAppUserPwd(eq("testuser"), anyString());
        System.out.println("testResetAppUserPwd 通过");
    }

    @Test
    public void testResetAppUserPwdById()
    {
        when(appUserMapper.resetAppUserPwdById(1L, "newpassword", "admin")).thenReturn(1);

        AppUser resetUser = new AppUser();
        resetUser.setUserId(1L);
        resetUser.setPassword("newpassword");
        resetUser.setUpdateBy("admin");

        int result = appUserService.resetAppUserPwdById(resetUser);

        assertEquals(1, result);
        verify(appUserMapper, times(1)).resetAppUserPwdById(1L, "newpassword", "admin");
        System.out.println("testResetAppUserPwdById 通过");
    }

    @Test
    public void testUpdateAppUserStatus()
    {
        when(appUserMapper.updateAppUserStatus(1L, "1", "admin")).thenReturn(1);

        AppUser statusUser = new AppUser();
        statusUser.setUserId(1L);
        statusUser.setStatus("1");
        statusUser.setUpdateBy("admin");

        int result = appUserService.updateAppUserStatus(statusUser);

        assertEquals(1, result);
        verify(appUserMapper, times(1)).updateAppUserStatus(1L, "1", "admin");
        System.out.println("testUpdateAppUserStatus 通过");
    }

    @Test
    public void testDeleteAppUserById()
    {
        when(appUserMapper.deleteAppUserById(1L)).thenReturn(1);

        int result = appUserService.deleteAppUserById(1L);

        assertEquals(1, result);
        verify(appUserMapper, times(1)).deleteAppUserById(1L);
        System.out.println("testDeleteAppUserById 通过");
    }

    @Test
    public void testDeleteAppUserByIds()
    {
        Long[] userIds = {1L, 2L, 3L};
        when(appUserMapper.deleteAppUserByIds(userIds)).thenReturn(3);

        int result = appUserService.deleteAppUserByIds(userIds);

        assertEquals(3, result);
        verify(appUserMapper, times(1)).deleteAppUserByIds(userIds);
        System.out.println("testDeleteAppUserByIds 通过");
    }

    @Test
    public void testCheckUserNameUnique_WhenNotExists()
    {
        when(appUserMapper.selectAppUserByUserName("newuser")).thenReturn(null);

        AppUser checkUser = new AppUser();
        checkUser.setUserName("newuser");

        boolean result = appUserService.checkUserNameUnique(checkUser);

        assertTrue(result);
        System.out.println("testCheckUserNameUnique_WhenNotExists 通过");
    }

    @Test
    public void testCheckUserNameUnique_WhenExists()
    {
        AppUser existingUser = new AppUser();
        existingUser.setUserId(2L);
        existingUser.setUserName("testuser");

        when(appUserMapper.selectAppUserByUserName("testuser")).thenReturn(existingUser);

        AppUser checkUser = new AppUser();
        checkUser.setUserName("testuser");

        boolean result = appUserService.checkUserNameUnique(checkUser);

        assertFalse(result);
        System.out.println("testCheckUserNameUnique_WhenExists 通过");
    }

    @Test
    public void testCheckUserNameUnique_WhenSameUser()
    {
        when(appUserMapper.selectAppUserByUserName("testuser")).thenReturn(testUser);

        AppUser checkUser = new AppUser();
        checkUser.setUserId(1L);
        checkUser.setUserName("testuser");

        boolean result = appUserService.checkUserNameUnique(checkUser);

        assertTrue(result);
        System.out.println("testCheckUserNameUnique_WhenSameUser 通过");
    }

    @Test
    public void testCheckPhoneUnique_WhenNotExists()
    {
        when(appUserMapper.checkPhoneUnique("13900139000")).thenReturn(null);

        AppUser checkUser = new AppUser();
        checkUser.setPhonenumber("13900139000");

        boolean result = appUserService.checkPhoneUnique(checkUser);

        assertTrue(result);
        System.out.println("testCheckPhoneUnique_WhenNotExists 通过");
    }

    @Test
    public void testCheckPhoneUnique_WhenExists()
    {
        AppUser existingUser = new AppUser();
        existingUser.setUserId(2L);
        existingUser.setPhonenumber("13800138000");

        when(appUserMapper.checkPhoneUnique("13800138000")).thenReturn(existingUser);

        AppUser checkUser = new AppUser();
        checkUser.setPhonenumber("13800138000");

        boolean result = appUserService.checkPhoneUnique(checkUser);

        assertFalse(result);
        System.out.println("testCheckPhoneUnique_WhenExists 通过");
    }

    @Test
    public void testCheckEmailUnique_WhenNotExists()
    {
        when(appUserMapper.checkEmailUnique("new@example.com")).thenReturn(null);

        AppUser checkUser = new AppUser();
        checkUser.setEmail("new@example.com");

        boolean result = appUserService.checkEmailUnique(checkUser);

        assertTrue(result);
        System.out.println("testCheckEmailUnique_WhenNotExists 通过");
    }

    @Test
    public void testCheckEmailUnique_WhenExists()
    {
        AppUser existingUser = new AppUser();
        existingUser.setUserId(2L);
        existingUser.setEmail("test@example.com");

        when(appUserMapper.checkEmailUnique("test@example.com")).thenReturn(existingUser);

        AppUser checkUser = new AppUser();
        checkUser.setEmail("test@example.com");

        boolean result = appUserService.checkEmailUnique(checkUser);

        assertFalse(result);
        System.out.println("testCheckEmailUnique_WhenExists 通过");
    }

    @Test
    public void testRecordLoginInfo()
    {
        when(appUserMapper.updateAppUser(any(AppUser.class))).thenReturn(1);

        testUser.setLoginIp("192.168.1.1");
        testUser.setLoginDate(new Date());
        appUserService.recordLoginInfo(testUser);

        verify(appUserMapper, times(1)).updateAppUser(testUser);
        System.out.println("testRecordLoginInfo 通过");
    }

    @Test
    public void testSelectAppUserListWithNickName()
    {
        List<AppUser> mockList = Arrays.asList(testUser);
        when(appUserMapper.selectAppUserList(any(AppUser.class))).thenReturn(mockList);

        AppUser queryParam = new AppUser();
        queryParam.setNickName("测试");
        List<AppUser> result = appUserService.selectAppUserList(queryParam);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appUserMapper, times(1)).selectAppUserList(queryParam);
        System.out.println("testSelectAppUserListWithNickName 通过");
    }

    @Test
    public void testDeleteAppUserLogicDelete()
    {
        when(appUserMapper.deleteAppUserById(1L)).thenReturn(1);

        int result = appUserService.deleteAppUserById(1L);

        assertEquals(1, result);
        verify(appUserMapper, times(1)).deleteAppUserById(1L);
        System.out.println("testDeleteAppUserLogicDelete 通过");
    }
}
