package com.ruoyi.app.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.app.user.domain.AppLoginBody;
import com.ruoyi.app.user.domain.AppRegisterBody;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * APP用户Controller单元测试
 */
@SpringBootTest(classes = com.ruoyi.RuoYiApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppUserControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IAppUserService appUserService;

    private static final String TEST_USER_NAME = "controller_test_user_" + System.currentTimeMillis();
    private static final String TEST_PASSWORD = "test123456";
    private static String testToken;

    @Test
    @Order(1)
    public void testRegister() throws Exception
    {
        AppRegisterBody registerBody = new AppRegisterBody();
        registerBody.setUserName(TEST_USER_NAME);
        registerBody.setPassword(TEST_PASSWORD);
        registerBody.setNickName("控制器测试用户");
        registerBody.setPhonenumber("13600136001");

        MvcResult result = mockMvc.perform(post("/app/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").exists())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        System.out.println("注册接口响应: " + response.substring(0, Math.min(200, response.length())) + "...");
        
        testToken = objectMapper.readTree(response).path("data").path("token").asText();
    }

    @Test
    @Order(2)
    public void testRegisterWithEmptyUserName() throws Exception
    {
        AppRegisterBody registerBody = new AppRegisterBody();
        registerBody.setPassword(TEST_PASSWORD);

        mockMvc.perform(post("/app/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerBody)))
                .andExpect(status().isBadRequest());
        
        System.out.println("空用户名注册校验通过");
    }

    @Test
    @Order(3)
    public void testRegisterWithDuplicateUserName() throws Exception
    {
        AppRegisterBody registerBody = new AppRegisterBody();
        registerBody.setUserName(TEST_USER_NAME);
        registerBody.setPassword("another_password");

        mockMvc.perform(post("/app/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(not(200)));
        
        System.out.println("重复用户名注册校验通过");
    }

    @Test
    @Order(4)
    public void testLogin() throws Exception
    {
        AppLoginBody loginBody = new AppLoginBody();
        loginBody.setUserName(TEST_USER_NAME);
        loginBody.setPassword(TEST_PASSWORD);

        MvcResult result = mockMvc.perform(post("/app/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").exists())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        testToken = objectMapper.readTree(response).path("data").path("token").asText();
        System.out.println("登录接口测试通过");
    }

    @Test
    @Order(5)
    public void testLoginWithWrongPassword() throws Exception
    {
        AppLoginBody loginBody = new AppLoginBody();
        loginBody.setUserName(TEST_USER_NAME);
        loginBody.setPassword("wrong_password");

        mockMvc.perform(post("/app/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(not(200)));
        
        System.out.println("错误密码登录校验通过");
    }

    @Test
    @Order(6)
    public void testGetUserInfo() throws Exception
    {
        mockMvc.perform(get("/app/api/user/info")
                .header("Authorization", "Bearer " + testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.user.userName").value(TEST_USER_NAME));
        
        System.out.println("获取用户信息接口测试通过");
    }

    @Test
    @Order(7)
    public void testGetUserInfoWithoutToken() throws Exception
    {
        mockMvc.perform(get("/app/api/user/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(not(200)));
        
        System.out.println("无token获取用户信息校验通过");
    }

    @Test
    @Order(8)
    public void testUpdateUserInfo() throws Exception
    {
        AppUser updateData = new AppUser();
        updateData.setNickName("更新后的昵称");
        updateData.setEmail("updated@test.com");

        mockMvc.perform(put("/app/api/user/info")
                .header("Authorization", "Bearer " + testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
        
        System.out.println("更新用户信息接口测试通过");
    }

    @Test
    @Order(9)
    public void testLogout() throws Exception
    {
        mockMvc.perform(post("/app/api/user/logout")
                .header("Authorization", "Bearer " + testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
        
        System.out.println("退出登录接口测试通过");
    }

    @Test
    @Order(100)
    public void testCleanup()
    {
        AppUser user = appUserService.selectAppUserByUserName(TEST_USER_NAME);
        if (user != null)
        {
            appUserService.deleteAppUserById(user.getUserId());
            System.out.println("清理测试用户成功");
        }
    }
}
