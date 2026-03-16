package com.ruoyi.app.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * APP用户信息Controller单元测试
 *
 * @author ruoyi
 */
@WebMvcTest(InfoUserController.class)
public class InfoUserControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAppUserService appUserService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 测试根据用户ID获取用户信息
     */
    @Test
    @WithMockUser(username = "testuser")
    public void testGetUserById() throws Exception
    {
        AppUser appUser = new AppUser();
        appUser.setUserId(1000L);
        appUser.setUserName("testuser");
        appUser.setNickName("测试用户");
        appUser.setEmail("test@example.com");
        appUser.setPhonenumber("13800138000");
        appUser.setStatus("0");

        when(appUserService.selectAppUserById(1000L)).thenReturn(appUser);

        mockMvc.perform(get("/app/api/user/1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userName").value("testuser"))
                .andExpect(jsonPath("$.data.nickName").value("测试用户"));
    }

    /**
     * 测试根据用户ID获取用户信息 - 用户不存在
     */
    @Test
    @WithMockUser(username = "testuser")
    public void testGetUserByIdNotFound() throws Exception
    {
        when(appUserService.selectAppUserById(9999L)).thenReturn(null);

        mockMvc.perform(get("/app/api/user/9999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("用户不存在"));
    }

    /**
     * 测试根据用户名获取用户信息
     */
    @Test
    @WithMockUser(username = "testuser")
    public void testGetUserByName() throws Exception
    {
        AppUser appUser = new AppUser();
        appUser.setUserId(1000L);
        appUser.setUserName("testuser");
        appUser.setNickName("测试用户");
        appUser.setEmail("test@example.com");
        appUser.setStatus("0");

        when(appUserService.selectAppUserByUserName("testuser")).thenReturn(appUser);

        mockMvc.perform(get("/app/api/user/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userName").value("testuser"));
    }

    /**
     * 测试修改用户信息
     */
    @Test
    @WithMockUser(username = "testuser")
    public void testUpdateUser() throws Exception
    {
        AppUser currentUser = new AppUser();
        currentUser.setUserId(1000L);
        currentUser.setUserName("testuser");
        currentUser.setNickName("原昵称");

        when(appUserService.selectAppUserByUserName("testuser")).thenReturn(currentUser);
        when(appUserService.updateAppUser(any(AppUser.class))).thenReturn(1);

        AppUser updateUser = new AppUser();
        updateUser.setUserId(1000L);
        updateUser.setNickName("新昵称");
        updateUser.setEmail("new@example.com");

        mockMvc.perform(put("/app/api/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("修改成功"));
    }

    /**
     * 测试修改密码
     */
    @Test
    @WithMockUser(username = "testuser")
    public void testUpdatePwd() throws Exception
    {
        AppUser appUser = new AppUser();
        appUser.setUserId(1000L);
        appUser.setUserName("testuser");
        appUser.setPassword("$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2");

        when(appUserService.selectAppUserByUserName("testuser")).thenReturn(appUser);
        when(appUserService.resetAppUserPwd("testuser", "newpassword")).thenReturn(true);

        mockMvc.perform(put("/app/api/user/updatePwd")
                        .param("oldPassword", "admin123")
                        .param("newPassword", "newpassword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("密码修改成功"));
    }

    /**
     * 测试修改密码 - 旧密码错误
     */
    @Test
    @WithMockUser(username = "testuser")
    public void testUpdatePwdWrongOldPwd() throws Exception
    {
        AppUser appUser = new AppUser();
        appUser.setUserId(1000L);
        appUser.setUserName("testuser");
        appUser.setPassword("$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2");

        when(appUserService.selectAppUserByUserName("testuser")).thenReturn(appUser);

        mockMvc.perform(put("/app/api/user/updatePwd")
                        .param("oldPassword", "wrongpassword")
                        .param("newPassword", "newpassword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("旧密码错误"));
    }

    /**
     * 测试修改密码 - 新密码长度不足
     */
    @Test
    @WithMockUser(username = "testuser")
    public void testUpdatePwdShortNewPwd() throws Exception
    {
        AppUser appUser = new AppUser();
        appUser.setUserId(1000L);
        appUser.setUserName("testuser");
        appUser.setPassword("$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2");

        when(appUserService.selectAppUserByUserName("testuser")).thenReturn(appUser);

        mockMvc.perform(put("/app/api/user/updatePwd")
                        .param("oldPassword", "admin123")
                        .param("newPassword", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("新密码长度必须在6-20个字符之间"));
    }
}
