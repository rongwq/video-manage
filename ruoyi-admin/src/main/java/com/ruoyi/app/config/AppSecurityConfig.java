package com.ruoyi.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * APP端安全配置
 * 用于扩展APP用户相关的权限规则
 *
 * @author ruoyi
 */
@Configuration
public class AppSecurityConfig
{
    /**
     * APP端安全过滤链配置
     * 优先级设置为高于主安全配置
     */
    @Bean
    @Order(1)
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception
    {
        http
            // CSRF禁用
            .csrf(csrf -> csrf.disable())
            // 禁用HTTP响应标头
            .headers((headersCustomizer) -> {
                headersCustomizer.cacheControl(cache -> cache.disable()).frameOptions(options -> options.sameOrigin());
            })
            // 基于token，不需要session
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 配置请求授权
            .authorizeHttpRequests((requests) -> {
                // APP用户注册、登录接口允许匿名访问
                requests.requestMatchers("/app/api/user/register").permitAll()
                        .requestMatchers("/app/api/user/login").permitAll()
                        // APP用户列表查询允许匿名访问（可根据需求调整）
                        .requestMatchers("/app/api/user/list").permitAll()
                        // 其他APP接口需要认证
                        .anyRequest().authenticated();
            });
        return http.build();
    }
}
