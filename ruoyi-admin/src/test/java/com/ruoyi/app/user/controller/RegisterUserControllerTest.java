package com.ruoyi.app.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
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
 * APP用户注册Controller单元测试
 *
 * @author ruoyi
 */
@WebMvcTest(RegisterUserController.class)
public class RegisterUserControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAppUserService appUserService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 测试正常注册
     */
    @Test
    public void testRegisterSuccess() throws Exception
    {
        // 模拟Service层返回
        when(appUserService.checkUserNameUnique(any(AppUser.class))).thenReturn(true);
        when(appUserService.checkPhoneUnique(any(AppUser.class))).thenReturn(true);
        when(appUserService.checkEmailUnique(any(AppUser.class))).thenReturn(true);
        when(appUserService.registerAppUser(any(AppUser.class))).thenReturn(true);

        AppUser appUser = new AppUser();
        appUser.setUserName("testuser");
        appUser.setPassword("123456");
        appUser.setNickName("测试用户");
        appUser.setEmail("test@example.com");
        appUser.setPhonenumber("13800138000");

        mockMvc.perform(post("/app/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("注册成功"));
    }

    /**
     * 测试用户名为空
     */
    @Test
    public void testRegisterEmptyUsername() throws Exception
    {
        AppUser appUser = new AppUser();
        appUser.setUserName("");
        appUser.setPassword("123456");

        mockMvc.perform(post("/app/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("用户名不能为空"));
    }

    /**
     * 测试密码为空
     */
    @Test
    public void testRegisterEmptyPassword() throws Exception
    {
        AppUser appUser = new AppUser();
        appUser.setUserName("testuser");
        appUser.setPassword("");

        mockMvc.perform(post("/app/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("密码不能为空"));
    }

    /**
     * 测试用户名长度不足
     */
    @Test
    public void testRegisterShortUsername() throws Exception
    {
        AppUser appUser = new AppUser();
        appUser.setUserName("a");
        appUser.setPassword("123456");

        mockMvc.perform(post("/app/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("用户名长度必须在2-30个字符之间"));
    }

    /**
     * 测试密码长度不足
     */
    @Test
    public void testRegisterShortPassword() throws Exception
    {
        AppUser appUser = new AppUser();
        appUser.setUserName("testuser");
        appUser.setPassword("123");

        mockMvc.perform(post("/app/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("密码长度必须在6-20个字符之间"));
    }

    /**
     * 测试用户名已存在
     */
    @Test
    public void testRegisterDuplicateUsername() throws Exception
    {
        // 模拟用户名已存在
        when(appUserService.checkUserNameUnique(any(AppUser.class))).thenReturn(false);

        AppUser appUser = new AppUser();
        appUser.setUserName("existinguser");
        appUser.setPassword("123456");

        mockMvc.perform(post("/app/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("注册用户'existinguser'失败，登录账号已存在"));
    }

    /**
     * 测试手机号已存在
     */
    @Test
    public void testRegisterDuplicatePhone() throws Exception
    {
        // 模拟用户名唯一，但手机号已存在
        when(appUserService.checkUserNameUnique(any(AppUser.class))).thenReturn(true);
        when(appUserService.checkPhoneUnique(any(AppUser.class))).thenReturn(false);

        AppUser appUser = new AppUser();
        appUser.setUserName("testuser");
        appUser.setPassword("123456");
        appUser.setPhonenumber("13800138000");

        mockMvc.perform(post("/app/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("注册用户'testuser'失败，手机号码已存在"));
    }
}
