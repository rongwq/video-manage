package com.ruoyi.app.user.service;

import java.util.List;
import com.ruoyi.app.user.domain.AppUser;

/**
 * APP用户Service接口
 *
 * @author ruoyi
 */
public interface IAppUserService
{
    /**
     * 根据条件分页查询APP用户列表
     *
     * @param appUser APP用户信息
     * @return APP用户信息集合信息
     */
    public List<AppUser> selectAppUserList(AppUser appUser);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public AppUser selectAppUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public AppUser selectAppUserById(Long userId);

    /**
     * 用户注册
     *
     * @param appUser 用户信息
     * @return 结果
     */
    public boolean registerAppUser(AppUser appUser);

    /**
     * 新增用户信息
     *
     * @param appUser 用户信息
     * @return 结果
     */
    public int insertAppUser(AppUser appUser);

    /**
     * 修改用户信息
     *
     * @param appUser 用户信息
     * @return 结果
     */
    public int updateAppUser(AppUser appUser);

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    public boolean updateAppUserAvatar(String userName, String avatar);

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    public boolean resetAppUserPwd(String userName, String password);

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteAppUserById(Long userId);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    public int deleteAppUserByIds(Long[] userIds);

    /**
     * 校验用户名称是否唯一
     *
     * @param appUser 用户信息
     * @return 结果
     */
    public boolean checkUserNameUnique(AppUser appUser);

    /**
     * 校验手机号码是否唯一
     *
     * @param appUser 用户信息
     * @return 结果
     */
    public boolean checkPhoneUnique(AppUser appUser);

    /**
     * 校验email是否唯一
     *
     * @param appUser 用户信息
     * @return 结果
     */
    public boolean checkEmailUnique(AppUser appUser);

    /**
     * 记录登录信息
     *
     * @param appUser 用户信息
     */
    public void recordLoginInfo(AppUser appUser);
}
