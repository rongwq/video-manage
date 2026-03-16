package com.ruoyi.app.user.service.impl;

import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.mapper.AppUserMapper;
import com.ruoyi.app.user.service.IAppUserService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * APP用户Service实现
 */
@Service
public class AppUserServiceImpl implements IAppUserService {

    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    public AppUser selectAppUserById(Long userId) {
        return appUserMapper.selectAppUserById(userId);
    }

    @Override
    public AppUser selectAppUserByUserName(String userName) {
        return appUserMapper.selectAppUserByUserName(userName);
    }

    @Override
    public List<AppUser> selectAppUserList(AppUser appUser) {
        return appUserMapper.selectAppUserList(appUser);
    }

    @Override
    public int insertAppUser(AppUser appUser) {
        appUser.setPassword(SecurityUtils.encryptPassword(appUser.getPassword()));
        appUser.setStatus("0");
        appUser.setIntegral(0L);
        appUser.setVipLevel(0);
        return appUserMapper.insertAppUser(appUser);
    }

    @Override
    public int updateAppUser(AppUser appUser) {
        return appUserMapper.updateAppUser(appUser);
    }

    @Override
    public int deleteAppUserByIds(Long[] userIds) {
        return appUserMapper.deleteAppUserByIds(userIds);
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        return appUserMapper.checkUserNameUnique(userName) == 0;
    }

    @Override
    public boolean checkPhoneUnique(String phonenumber) {
        if (StringUtils.isEmpty(phonenumber)) {
            return true;
        }
        return appUserMapper.checkPhoneUnique(phonenumber) == 0;
    }

    @Override
    public void updateLoginInfo(Long userId, String ip) {
        AppUser appUser = new AppUser();
        appUser.setUserId(userId);
        appUser.setLoginIp(ip);
        appUser.setLoginDate(DateUtils.getNowDate());
        appUserMapper.updateAppUser(appUser);
    }
}
