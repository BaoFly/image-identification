package com.bf.image.security.config;

import com.bf.image.security.filter.JwtAuthenticationTokenFilter;
import com.bf.image.security.service.SecurityUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityUserDetailService userDetailService;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(new BCryptPasswordEncoder()).and();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 1. 关闭CSRF保护（JWT无状态模式不需要，否则POST请求会被拦截）
                .csrf().disable()
                // 2. 配置请求授权规则（顺序重要：具体路径在前，通用规则在后）
                .authorizeRequests(authorize -> authorize
                        // 放行完整的登录路径：/identification/login（支持所有请求方法GET/POST等）
                        .antMatchers("/identification/login").permitAll()
                        // 可选：如果只允许POST请求（推荐，登录一般用POST），可以更精确配置
                        // .antMatchers(HttpMethod.POST, "/identification/login").permitAll()
                        // 其他所有请求都需要认证
//                        .anyRequest().authenticated()
                )
                // 3. 保持无状态会话（JWT模式必备）
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 4. 配置JWT过滤器
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
