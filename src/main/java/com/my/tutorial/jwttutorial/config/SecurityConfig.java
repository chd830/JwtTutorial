package com.my.tutorial.jwttutorial.config;

import com.my.tutorial.jwttutorial.jwt.JwtAccessDeniedHandler;
import com.my.tutorial.jwttutorial.jwt.JwtAuthenticationEntryPoint;
import com.my.tutorial.jwttutorial.jwt.JwtSecurityConfig;
import com.my.tutorial.jwttutorial.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// 기본 web보안 활성화
@EnableWebSecurity
// 메소드 단위로 @PreA    uthorize 검증 어노테이션 사용
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.tokenProvider = tokenProvider;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    // password Encode
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // h2-console의 하위요청, /favicon.ico에 대한 요청은 security 로직 수행 없이 접근할 수 있도록 선언
    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers(
                        "/h2-console/**"
                        ,"/favicon.ico"
                );
    }

    // 보안이 필요하지 않은 접속(로그인하기 전 페이지로 접근) 허용
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()// token방식을 사용하므로 csrf disable 처리하기
                // 예외처리
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console을 위한 설정
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션은 사용하지 않기 때문에 STATELESS로 지정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 특정 주소를 token 없이 호출할 수 있도록 허용
                .and()
                .authorizeRequests()
                .antMatchers("/api/hello").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/signup").permitAll()

                // JwtFilter를 사용하는 JwtSecurityConfig 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

    }
}