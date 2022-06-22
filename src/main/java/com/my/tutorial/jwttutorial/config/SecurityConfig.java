package com.my.tutorial.jwttutorial.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// 기본 web보안 활성화
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // configure을 오버라이드 하여 /api/hello에 대한 접근이 인증없이 진행될 수 있도록 허용
    // 보안이 필요하지 않은 접속(로그인하기 전 페이지로 접근) 허용
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/hello").permitAll()
                .anyRequest().authenticated();
    }

}
