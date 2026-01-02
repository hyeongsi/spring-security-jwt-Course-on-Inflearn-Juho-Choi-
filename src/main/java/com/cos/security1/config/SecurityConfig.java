package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// 스프링 시큐리티 활성화하고 웹 보안 설정 구성에 사용됨, WebSecurityConfigurerAdapter 클래스를 상속한 구성 클래스에서 사용됨
// 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
// securedEnabled = true: secured 어노테이션 활성화, 간단하게 권한확인할때 사용
// prePostEnabled = true: preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authorizeHttpRequestsConfigurer -> {
                    authorizeHttpRequestsConfigurer
                        .requestMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소!!
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().permitAll();
                });

        http.formLogin(httpSecurityFormLoginConfigurer -> {
            httpSecurityFormLoginConfigurer
                .loginPage("/loginForm")
                .permitAll()
                // <input type="text" name="username">의 name="username" 값과 UserDetailsService의 loadUserByUsername함수 매개변수 커스텀 매핑
                .usernameParameter("username")
                // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                // /login 요청 => Spring Container가 IoC로 UserDetailsService 등록된 빈 찾고 loadUserByUsername(..) 호출함.
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/");
        });

        http.oauth2Login(oauth2LoginConfigurer -> {
            oauth2LoginConfigurer.loginPage("/loginForm");
        });

        return http.build();
    }
}
