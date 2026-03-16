package com.ruoyi.app.user.mapper;

import java.util.List;

import com.ruoyi.app.user.domain.AppUser;
import org.apache.ibatis.annotations.Param;

public interface AppUserMapper
{
    public List<AppUser> selectAppUserList(AppUser appUser);

    public AppUser selectAppUserByUserName(String userName);

    public AppUser selectAppUserById(Long userId);

    public int insertAppUser(AppUser appUser);

    public int updateAppUser(AppUser appUser);

    public int updateAppUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    public int resetAppUserPwd(@Param("userName") String userName, @Param("password") String password);

    public int resetAppUserPwdById(@Param("userId") Long userId, @Param("password") String password, @Param("updateBy") String updateBy);

    public int updateAppUserStatus(@Param("userId") Long userId, @Param("status") String status, @Param("updateBy") String updateBy);

    public int deleteAppUserById(Long userId);

    public int deleteAppUserByIds(Long[] userIds);

    public int checkUserNameUnique(String userName);

    public AppUser checkPhoneUnique(String phonenumber);

    public AppUser checkEmailUnique(String email);
}
