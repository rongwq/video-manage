package com.ruoyi.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;

@Configuration
@EnableWebSecurity
@Order(1)
public class AppSecurityConfig
{
    @Bean
    protected SecurityFilterChain appSecurityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        return httpSecurity
            .antMatcher("/app/api/**")
            .csrf(csrf -> csrf.disable())
            .headers((headersCustomizer) -> {
                headersCustomizer.cacheControl(cache -> cache.disable()).frameOptions(options -> options.sameOrigin());
            })
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((requests) -> {
                requests.anyRequest().permitAll();
            })
            .build();
    }
}
