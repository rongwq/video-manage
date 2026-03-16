package com.ruoyi.app.user.service;

import com.ruoyi.app.user.domain.AppLoginUser;
import com.ruoyi.app.user.domain.AppRegisterBody;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.mapper.AppUserMapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * APP登录服务
 */
@Service
public class AppLoginService {

    private static final Logger log = LoggerFactory.getLogger(AppLoginService.class);

    @Autowired
    private AppTokenService appTokenService;

    @Autowired
    @Qualifier("appAuthenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private AppUserMapper appUserMapper;

    public String login(String userName, String password) {
        Authentication authentication;
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userName, password);
            authentication = authenticationManager.authenticate(authToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGIN_FAIL, 
                    MessageUtils.message("user.password.not.match")));
                throw new ServiceException("用户名或密码错误");
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        
        AppLoginUser loginUser = (AppLoginUser) authentication.getPrincipal();
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGIN_SUCCESS, 
            MessageUtils.message("user.login.success")));
        
        appUserService.updateLoginInfo(loginUser.getUserId(), IpUtils.getIpAddr());
        
        return appTokenService.createToken(loginUser);
    }

    public String register(AppRegisterBody registerBody) {
        String userName = registerBody.getUserName();
        String password = registerBody.getPassword();

        if (!appUserService.checkUserNameUnique(userName)) {
            throw new ServiceException("注册用户'" + userName + "'失败，账号已存在");
        }
        
        if (StringUtils.isNotEmpty(registerBody.getPhonenumber()) 
            && !appUserService.checkPhoneUnique(registerBody.getPhonenumber())) {
            throw new ServiceException("注册用户'" + userName + "'失败，手机号码已存在");
        }

        AppUser appUser = new AppUser();
        appUser.setUserName(userName);
        appUser.setPassword(password);
        appUser.setPhonenumber(registerBody.getPhonenumber());
        appUser.setNickName(StringUtils.isEmpty(registerBody.getNickName()) ? userName : registerBody.getNickName());
        appUser.setDeviceId(registerBody.getDeviceId());

        int result = appUserService.insertAppUser(appUser);
        if (result <= 0) {
            throw new ServiceException("注册用户失败，请联系管理员");
        }

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.REGISTER, 
            MessageUtils.message("user.register.success")));

        return login(userName, password);
    }
}
