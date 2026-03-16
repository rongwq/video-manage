package com.ruoyi.app.user.service;

import java.util.List;
import com.ruoyi.app.user.domain.AppUser;

public interface IAppUserService
{
    public List<AppUser> selectAppUserList(AppUser appUser);

    public AppUser selectAppUserByUserName(String userName);

    public AppUser selectAppUserById(Long userId);

    public boolean registerAppUser(AppUser appUser);

    public int insertAppUser(AppUser appUser);

    public int updateAppUser(AppUser appUser);

    public boolean updateAppUserAvatar(String userName, String avatar);

    public boolean resetAppUserPwd(String userName, String password);

    public int resetAppUserPwdById(AppUser appUser);

    public int updateAppUserStatus(AppUser appUser);

    public int deleteAppUserById(Long userId);

    public int deleteAppUserByIds(Long[] userIds);

    public boolean checkUserNameUnique(AppUser appUser);

    public boolean checkPhoneUnique(AppUser appUser);

    public boolean checkEmailUnique(AppUser appUser);

    public void recordLoginInfo(AppUser appUser);
}
