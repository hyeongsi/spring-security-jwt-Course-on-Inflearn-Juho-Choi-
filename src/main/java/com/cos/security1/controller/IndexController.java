package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    @ResponseBody
    // @AuthenticationPrincipal을 통해 세션정보 접근 가능
    public String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("/test/login ==============");
        System.out.println("authentication: " + authentication.getPrincipal());
        PrincipalDetails printDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("printDetails.getUser(): " + printDetails.getUser());

        System.out.println("principalDetails: " + principalDetails.getUser());
        // 유저 정보 찾는 방법 2가지, 1: Authentication을 통해, 2. @AuthenticationPrincipal 어노테이션을 통해
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    @ResponseBody
    // @AuthenticationPrincipal을 통해 세션정보 접근 가능
    public String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("/test/oauth/login ==============");
        System.out.println("authentication: " + authentication.getPrincipal());
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oauth2User.getAttributes(): " + oauth2User.getAttributes());
        System.out.println("oauthUser: " + oauth.getAttributes());

        return "OAuth 세션 정보 확인하기";
    }

    // localhost:8080/
    // localhost:8080
    @GetMapping({"", "/"})
    public String index() {
        // 머스테치 기본폴더 src/main/resources/
        // 뷰리졸버 설정: templates(prefix), .mustache (suffix), 생략 가능!
        return "index"; // src/main/resources/templates/index.mustache
    }

    @GetMapping("/user")
    @ResponseBody
    public String user() {
        return "user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        userRepository.save(user);  // 회원가입 잘됨. 비밀번호 a -> 시큐리티로 로그인 불가. 이유는 pw 암호화 안되었음.
        return "redirect:/loginForm";
    }

    // 하나의 권한 체크 걸 수 있음
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    @ResponseBody
    public String info() {
        return "개인정보";
    }

    // data()가 실행되기 직전에 실행됨, 한번에 여러개 권한 체크 걸 수 있음
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    @ResponseBody
    public String data() {
        return "데이터정보";
    }

}
