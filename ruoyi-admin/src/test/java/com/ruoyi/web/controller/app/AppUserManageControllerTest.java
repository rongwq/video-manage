package com.ruoyi.web.controller.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * APP用户管理Controller单元测试
 *
 * @author ruoyi
 */
@WebMvcTest(AppUserManageController.class)
public class AppUserManageControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAppUserService appUserService;

    @Autowired
    private ObjectMapper objectMapper;

    private AppUser testUser;

    @BeforeEach
    public void setUp()
    {
        testUser = new AppUser();
        testUser.setUserId(1L);
        testUser.setUserName("testuser");
        testUser.setNickName("测试用户");
        testUser.setPhonenumber("13800138000");
        testUser.setStatus("0");
        testUser.setAvatar("https://example.com/avatar.jpg");
    }

    /**
     * 测试查询APP用户列表
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:list"})
    public void testListAppUsers() throws Exception
    {
        List<AppUser> userList = Arrays.asList(testUser);
        when(appUserService.selectAppUserList(any(AppUser.class))).thenReturn(userList);

        mockMvc.perform(get("/app/manage/user/list")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows").isArray())
                .andExpect(jsonPath("$.rows[0].userId").value(1))
                .andExpect(jsonPath("$.rows[0].nickName").value("测试用户"));
    }

    /**
     * 测试查询APP用户列表 - 带筛选条件
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:list"})
    public void testListAppUsersWithFilter() throws Exception
    {
        List<AppUser> userList = Arrays.asList(testUser);
        when(appUserService.selectAppUserList(any(AppUser.class))).thenReturn(userList);

        mockMvc.perform(get("/app/manage/user/list")
                        .param("phonenumber", "13800138000")
                        .param("nickName", "测试")
                        .param("status", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows").isArray());
    }

    /**
     * 测试查询APP用户详情
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:query"})
    public void testGetAppUserInfo() throws Exception
    {
        when(appUserService.selectAppUserById(1L)).thenReturn(testUser);

        mockMvc.perform(get("/app/manage/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.nickName").value("测试用户"))
                .andExpect(jsonPath("$.data.phonenumber").value("13800138000"));
    }

    /**
     * 测试新增APP用户 - 成功
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:add"})
    public void testAddAppUserSuccess() throws Exception
    {
        AppUser newUser = new AppUser();
        newUser.setPhonenumber("13800138001");
        newUser.setNickName("新用户");
        newUser.setPassword("123456");
        newUser.setStatus("0");

        when(appUserService.checkPhoneUnique(any(AppUser.class))).thenReturn(true);
        when(appUserService.checkUserNameUnique(any(AppUser.class))).thenReturn(true);
        when(appUserService.insertAppUser(any(AppUser.class))).thenReturn(1);

        mockMvc.perform(post("/app/manage/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("操作成功"));
    }

    /**
     * 测试新增APP用户 - 手机号为空
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:add"})
    public void testAddAppUserEmptyPhone() throws Exception
    {
        AppUser newUser = new AppUser();
        newUser.setNickName("新用户");
        newUser.setPassword("123456");

        mockMvc.perform(post("/app/manage/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("手机号不能为空"));
    }

    /**
     * 测试新增APP用户 - 手机号格式错误
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:add"})
    public void testAddAppUserInvalidPhone() throws Exception
    {
        AppUser newUser = new AppUser();
        newUser.setPhonenumber("1380013800"); // 少一位
        newUser.setNickName("新用户");
        newUser.setPassword("123456");

        mockMvc.perform(post("/app/manage/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("请输入正确的手机号码"));
    }

    /**
     * 测试新增APP用户 - 手机号已存在
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:add"})
    public void testAddAppUserDuplicatePhone() throws Exception
    {
        AppUser newUser = new AppUser();
        newUser.setPhonenumber("13800138000");
        newUser.setNickName("新用户");
        newUser.setPassword("123456");

        when(appUserService.checkPhoneUnique(any(AppUser.class))).thenReturn(false);

        mockMvc.perform(post("/app/manage/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("新增用户失败，手机号码已存在"));
    }

    /**
     * 测试修改APP用户 - 成功
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:edit"})
    public void testUpdateAppUserSuccess() throws Exception
    {
        AppUser updateUser = new AppUser();
        updateUser.setUserId(1L);
        updateUser.setNickName("修改后的昵称");
        updateUser.setStatus("1");
        updateUser.setAvatar("https://example.com/new-avatar.jpg");

        when(appUserService.updateAppUser(any(AppUser.class))).thenReturn(1);

        mockMvc.perform(put("/app/manage/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("操作成功"));
    }

    /**
     * 测试删除APP用户 - 单条
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:remove"})
    public void testDeleteAppUserSingle() throws Exception
    {
        when(appUserService.deleteAppUserByIds(any(Long[].class))).thenReturn(1);

        mockMvc.perform(delete("/app/manage/user/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("操作成功"));
    }

    /**
     * 测试删除APP用户 - 批量
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:remove"})
    public void testDeleteAppUserBatch() throws Exception
    {
        when(appUserService.deleteAppUserByIds(any(Long[].class))).thenReturn(3);

        mockMvc.perform(delete("/app/manage/user/1,2,3")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("操作成功"));
    }

    /**
     * 测试重置密码
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:resetPwd"})
    public void testResetPwd() throws Exception
    {
        AppUser resetUser = new AppUser();
        resetUser.setUserId(1L);
        resetUser.setPassword("newpassword123");

        when(appUserService.updateAppUser(any(AppUser.class))).thenReturn(1);

        mockMvc.perform(put("/app/manage/user/resetPwd")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("操作成功"));
    }

    /**
     * 测试重置密码 - 密码为空
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:resetPwd"})
    public void testResetPwdEmpty() throws Exception
    {
        AppUser resetUser = new AppUser();
        resetUser.setUserId(1L);
        resetUser.setPassword("");

        mockMvc.perform(put("/app/manage/user/resetPwd")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("新密码不能为空"));
    }

    /**
     * 测试修改状态
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:edit"})
    public void testChangeStatus() throws Exception
    {
        AppUser statusUser = new AppUser();
        statusUser.setUserId(1L);
        statusUser.setStatus("1");

        when(appUserService.updateAppUser(any(AppUser.class))).thenReturn(1);

        mockMvc.perform(put("/app/manage/user/changeStatus")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("操作成功"));
    }

    /**
     * 测试校验手机号唯一性
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:add"})
    public void testCheckPhoneUnique() throws Exception
    {
        when(appUserService.checkPhoneUnique(any(AppUser.class))).thenReturn(true);

        mockMvc.perform(get("/app/manage/user/checkPhoneUnique")
                        .param("phonenumber", "13800138099"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }

    /**
     * 测试校验用户名唯一性
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:add"})
    public void testCheckUserNameUnique() throws Exception
    {
        when(appUserService.checkUserNameUnique(any(AppUser.class))).thenReturn(false);

        mockMvc.perform(get("/app/manage/user/checkUserNameUnique")
                        .param("userName", "existinguser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(false));
    }

    /**
     * 测试导出APP用户
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:export"})
    public void testExportAppUsers() throws Exception
    {
        List<AppUser> userList = Arrays.asList(testUser);
        when(appUserService.selectAppUserList(any(AppUser.class))).thenReturn(userList);

        mockMvc.perform(post("/app/manage/user/export")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AppUser())))
                .andExpect(status().isOk());
    }

    /**
     * 测试无权限访问
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"app:user:list"})
    public void testNoPermission() throws Exception
    {
        mockMvc.perform(post("/app/manage/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isForbidden());
    }
}
