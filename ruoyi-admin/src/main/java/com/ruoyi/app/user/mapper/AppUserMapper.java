package com.ruoyi.app.user.mapper;

import com.ruoyi.app.user.domain.AppUser;
import java.util.List;

/**
 * APP用户Mapper接口
 */
public interface AppUserMapper {
    
    AppUser selectAppUserById(Long userId);

    AppUser selectAppUserByUserName(String userName);

    AppUser selectAppUserByPhonenumber(String phonenumber);

    List<AppUser> selectAppUserList(AppUser appUser);

    int insertAppUser(AppUser appUser);

    int updateAppUser(AppUser appUser);

    int deleteAppUserById(Long userId);

    int deleteAppUserByIds(Long[] userIds);

    int checkUserNameUnique(String userName);

    int checkPhoneUnique(String phonenumber);
}
