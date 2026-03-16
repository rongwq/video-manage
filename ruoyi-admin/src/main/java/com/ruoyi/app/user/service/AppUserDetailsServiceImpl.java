package com.ruoyi.app.user.service;

import com.ruoyi.app.user.domain.AppLoginUser;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.mapper.AppUserMapper;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashSet;

/**
 * APP用户认证处理
 */
@Service("appUserDetailsService")
public class AppUserDetailsServiceImpl implements UserDetailsService {
    
    private static final Logger log = LoggerFactory.getLogger(AppUserDetailsServiceImpl.class);

    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserMapper.selectAppUserByUserName(username);
        if (user == null) {
            log.info("APP用户：{} 不存在.", username);
            throw new ServiceException("用户不存在");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("APP用户：{} 已被停用.", username);
            throw new ServiceException("用户已被停用，请联系管理员");
        }
        return new AppLoginUser(user, new HashSet<>());
    }
}
