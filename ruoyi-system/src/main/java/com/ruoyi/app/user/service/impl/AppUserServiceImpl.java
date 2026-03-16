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

@Service
public class AppUserServiceImpl implements IAppUserService
{
    private static final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);

    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    @DataSource(DataSourceType.MASTER)
    public List<AppUser> selectAppUserList(AppUser appUser)
    {
        return appUserMapper.selectAppUserList(appUser);
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public AppUser selectAppUserByUserName(String userName)
    {
        return appUserMapper.selectAppUserByUserName(userName);
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public AppUser selectAppUserById(Long userId)
    {
        return appUserMapper.selectAppUserById(userId);
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public boolean registerAppUser(AppUser appUser)
    {
        appUser.setPassword(SecurityUtils.encryptPassword(appUser.getPassword()));
        return appUserMapper.insertAppUser(appUser) > 0;
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public int insertAppUser(AppUser appUser)
    {
        return appUserMapper.insertAppUser(appUser);
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public int updateAppUser(AppUser appUser)
    {
        return appUserMapper.updateAppUser(appUser);
    }

    @Override
    public boolean updateAppUserAvatar(String userName, String avatar)
    {
        return appUserMapper.updateAppUserAvatar(userName, avatar) > 0;
    }

    @Override
    public boolean resetAppUserPwd(String userName, String password)
    {
        return appUserMapper.resetAppUserPwd(userName, SecurityUtils.encryptPassword(password)) > 0;
    }

    @Override
    public int resetAppUserPwdById(AppUser appUser)
    {
        return appUserMapper.resetAppUserPwdById(appUser.getUserId(), appUser.getPassword(), appUser.getUpdateBy());
    }

    @Override
    public int updateAppUserStatus(AppUser appUser)
    {
        return appUserMapper.updateAppUserStatus(appUser.getUserId(), appUser.getStatus(), appUser.getUpdateBy());
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public int deleteAppUserById(Long userId)
    {
        return appUserMapper.deleteAppUserById(userId);
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public int deleteAppUserByIds(Long[] userIds)
    {
        return appUserMapper.deleteAppUserByIds(userIds);
    }

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

    @Override
    public void recordLoginInfo(AppUser appUser)
    {
        appUserMapper.updateAppUser(appUser);
    }
}
