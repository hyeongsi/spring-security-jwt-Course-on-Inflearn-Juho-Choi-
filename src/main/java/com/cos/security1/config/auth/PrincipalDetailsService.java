package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// 시큐리티 설정에서 .loginProcessingUrl("/login"); 걸어놨고
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어있는 loadUserByUsername()가 실행됨
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 매개변수의 username 이 명칭과 실제 페이지에서 <input type="text" name="username">에서의 user="username"과 일치해야 한다!!
    // config의 .usernameParameter("...")으로 변경도 가능함.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            return new PrincipalDetqails(userEntity);
        }

        return null;
        // return 진행하면 이 값이 Authentication 내부에 들어간다. 이 Authentication은 Session안에 들어가고
    }
}
