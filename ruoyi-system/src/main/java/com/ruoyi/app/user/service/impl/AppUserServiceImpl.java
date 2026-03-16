package com.ruoyi.app.user.service.impl;

import java.util.List;

import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.mapper.AppUserMapper;
import com.ruoyi.app.user.service.IAppUserService;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * APP用户Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class AppUserServiceImpl implements IAppUserService
{
    private static final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);

    @Autowired
    private AppUserMapper appUserMapper;

    /**
     * 根据条件分页查询APP用户列表
     *
     * @param appUser APP用户信息
     * @return APP用户信息集合信息
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public List<AppUser> selectAppUserList(AppUser appUser)
    {
        return appUserMapper.selectAppUserList(appUser);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public AppUser selectAppUserByUserName(String userName)
    {
        return appUserMapper.selectAppUserByUserName(userName);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public AppUser selectAppUserById(Long userId)
    {
        return appUserMapper.selectAppUserById(userId);
    }

    /**
     * 用户注册
     *
     * @param appUser 用户信息
     * @return 结果
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public boolean registerAppUser(AppUser appUser)
    {
        appUser.setPassword(SecurityUtils.encryptPassword(appUser.getPassword()));
        return appUserMapper.insertAppUser(appUser) > 0;
    }

    /**
     * 修改用户信息
     *
     * @param appUser 用户信息
     * @return 结果
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public int updateAppUser(AppUser appUser)
    {
        return appUserMapper.updateAppUser(appUser);
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateAppUserAvatar(String userName, String avatar)
    {
        return appUserMapper.updateAppUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public boolean resetAppUserPwd(String userName, String password)
    {
        return appUserMapper.resetAppUserPwd(userName, SecurityUtils.encryptPassword(password)) > 0;
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public int deleteAppUserById(Long userId)
    {
        return appUserMapper.deleteAppUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public int deleteAppUserByIds(Long[] userIds)
    {
        return appUserMapper.deleteAppUserByIds(userIds);
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param appUser 用户信息
     * @return 结果
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public boolean checkUserNameUnique(AppUser appUser)
    {
        Long userId = appUser.getUserId() == null ? -1L : appUser.getUserId();
        AppUser info = appUserMapper.selectAppUserByUserName(appUser.getUserName());
        if (info != null && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param appUser 用户信息
     * @return 结果
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public boolean checkPhoneUnique(AppUser appUser)
    {
        Long userId = appUser.getUserId() == null ? -1L : appUser.getUserId();
        AppUser info = appUserMapper.checkPhoneUnique(appUser.getPhonenumber());
        if (info != null && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param appUser 用户信息
     * @return 结果
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public boolean checkEmailUnique(AppUser appUser)
    {
        Long userId = appUser.getUserId() == null ? -1L : appUser.getUserId();
        AppUser info = appUserMapper.checkEmailUnique(appUser.getEmail());
        if (info != null && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 记录登录信息
     *
     * @param appUser 用户信息
     */
    @Override
    public void recordLoginInfo(AppUser appUser)
    {
        appUserMapper.updateAppUser(appUser);
    }
}
