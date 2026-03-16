package com.ruoyi.app.user.service;

import com.ruoyi.app.user.domain.AppUser;
import java.util.List;

/**
 * APP用户Service接口
 */
public interface IAppUserService {
    
    AppUser selectAppUserById(Long userId);

    AppUser selectAppUserByUserName(String userName);

    List<AppUser> selectAppUserList(AppUser appUser);

    int insertAppUser(AppUser appUser);

    int updateAppUser(AppUser appUser);

    int deleteAppUserByIds(Long[] userIds);

    boolean checkUserNameUnique(String userName);

    boolean checkPhoneUnique(String phonenumber);

    void updateLoginInfo(Long userId, String ip);
}
