package com.ruoyi.app.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.framework.web.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * APP用户登录Controller单元测试
 *
 * @author ruoyi
 */
@WebMvcTest(LoginUserController.class)
public class LoginUserControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAppUserService appUserService;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 测试正常登录
     */
    @Test
    public void testLoginSuccess() throws Exception
    {
        // 模拟用户存在
        AppUser appUser = new AppUser();
        appUser.setUserId(1000L);
        appUser.setUserName("testuser");
        appUser.setNickName("测试用户");
        appUser.setPassword("$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2"); // 加密后的123456
        appUser.setStatus("0");
        appUser.setAvatar("https://example.com/avatar.jpg");

        when(appUserService.selectAppUserByUserName("testuser")).thenReturn(appUser);
        when(tokenService.createToken(any())).thenReturn("test_token_12345");

        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("testuser");
        loginBody.setPassword("admin123"); // 使用与加密密码匹配的明文

        mockMvc.perform(post("/app/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").value("test_token_12345"));
    }

    /**
     * 测试用户名为空
     */
    @Test
    public void testLoginEmptyUsername() throws Exception
    {
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("");
        loginBody.setPassword("123456");

        mockMvc.perform(post("/app/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("用户名不能为空"));
    }

    /**
     * 测试密码为空
     */
    @Test
    public void testLoginEmptyPassword() throws Exception
    {
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("testuser");
        loginBody.setPassword("");

        mockMvc.perform(post("/app/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("密码不能为空"));
    }

    /**
     * 测试用户不存在
     */
    @Test
    public void testLoginUserNotFound() throws Exception
    {
        when(appUserService.selectAppUserByUserName("nonexistent")).thenReturn(null);

        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("nonexistent");
        loginBody.setPassword("123456");

        mockMvc.perform(post("/app/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("用户不存在"));
    }

    /**
     * 测试账号被停用
     */
    @Test
    public void testLoginUserDisabled() throws Exception
    {
        // 模拟用户存在但已停用
        AppUser appUser = new AppUser();
        appUser.setUserId(1000L);
        appUser.setUserName("disableduser");
        appUser.setPassword("$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2");
        appUser.setStatus("1"); // 停用状态

        when(appUserService.selectAppUserByUserName("disableduser")).thenReturn(appUser);

        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("disableduser");
        loginBody.setPassword("admin123");

        mockMvc.perform(post("/app/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("账号已被停用，请联系管理员"));
    }
}
