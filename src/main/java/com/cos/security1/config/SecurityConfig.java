package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

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
            oauth2LoginConfigurer
                .loginPage("/loginForm")
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(principalOauth2UserService));

            // 구글 로그인이 완료된 뒤의 후처리가 필요함.
            // 로그인 완료 후 코드 받지 않고, (엑세스 토큰 + 사용자 프로필 정보)를 받는다.
            // 1. 코드 받기 (인증)
            // 2. 엑세스 토큰 (권한)
            // 3. 사용자 프로필 정보를 가져오고
            // 4. 그 정보를 토대로 회원가입을 자동 진행 or 그 정보
        });

        return http.build();
    }
}
