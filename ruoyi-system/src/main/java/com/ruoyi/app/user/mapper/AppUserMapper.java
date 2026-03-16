package com.ruoyi.app.user.mapper;

import java.util.List;
import com.ruoyi.app.user.domain.AppUser;

public interface AppUserMapper
{
    public List<AppUser> selectAppUserList(AppUser appUser);

    public AppUser selectAppUserByUserName(String userName);

    public AppUser selectAppUserById(Long userId);

    public int insertAppUser(AppUser appUser);

    public int updateAppUser(AppUser appUser);

    public int deleteAppUserById(Long userId);

    public int deleteAppUserByIds(Long[] userIds);

    public int checkUserNameUnique(String userName);

    public int checkPhoneUnique(String phonenumber);

    public int checkEmailUnique(String email);
}
