package com.ruoyi.app.config;

import com.ruoyi.app.user.service.AppUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * APP安全配置扩展
 * 注意：此配置与SecurityConfig共存，专门处理APP用户认证
 */
@Configuration
public class AppSecurityConfig {

    @Autowired
    private AppUserDetailsServiceImpl appUserDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * APP用户认证管理器
     * 使用独立的UserDetailsService处理APP用户认证
     */
    @Bean("appAuthenticationManager")
    public AuthenticationManager appAuthenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(provider);
    }
}
