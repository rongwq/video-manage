package com.ruoyi.app.user.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.mapper.AppUserMapper;
import com.ruoyi.app.user.service.IAppUserService;

@Service
public class AppUserServiceImpl implements IAppUserService
{
    private static final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);

    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    public List<AppUser> selectAppUserList(AppUser appUser)
    {
        return appUserMapper.selectAppUserList(appUser);
    }

    @Override
    public AppUser selectAppUserByUserName(String userName)
    {
        return appUserMapper.selectAppUserByUserName(userName);
    }

    @Override
    public AppUser selectAppUserById(Long userId)
    {
        return appUserMapper.selectAppUserById(userId);
    }

    @Override
    public boolean checkUserNameUnique(AppUser appUser)
    {
        Long userId = StringUtils.isNull(appUser.getUserId()) ? -1L : appUser.getUserId();
        int count = appUserMapper.checkUserNameUnique(appUser.getUserName());
        if (count > 0)
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkPhoneUnique(AppUser appUser)
    {
        Long userId = StringUtils.isNull(appUser.getUserId()) ? -1L : appUser.getUserId();
        int count = appUserMapper.checkPhoneUnique(appUser.getPhonenumber());
        if (count > 0)
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkEmailUnique(AppUser appUser)
    {
        Long userId = StringUtils.isNull(appUser.getUserId()) ? -1L : appUser.getUserId();
        int count = appUserMapper.checkEmailUnique(appUser.getEmail());
        if (count > 0)
        {
            return false;
        }
        return true;
    }

    @Override
    public int insertAppUser(AppUser appUser)
    {
        appUser.setPassword(SecurityUtils.encryptPassword(appUser.getPassword()));
        return appUserMapper.insertAppUser(appUser);
    }

    @Override
    public boolean registerAppUser(AppUser appUser)
    {
        return appUserMapper.insertAppUser(appUser) > 0;
    }

    @Override
    public int updateAppUser(AppUser appUser)
    {
        if (StringUtils.isNotEmpty(appUser.getPassword()))
        {
            appUser.setPassword(SecurityUtils.encryptPassword(appUser.getPassword()));
        }
        return appUserMapper.updateAppUser(appUser);
    }

    @Override
    public int deleteAppUserById(Long userId)
    {
        return appUserMapper.deleteAppUserById(userId);
    }

    @Override
    public int deleteAppUserByIds(Long[] userIds)
    {
        return appUserMapper.deleteAppUserByIds(userIds);
    }
}
