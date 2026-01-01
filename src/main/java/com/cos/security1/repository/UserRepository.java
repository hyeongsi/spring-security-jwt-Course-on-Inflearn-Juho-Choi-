package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository가 기본적인 CRUD 함수를 들고 있음
// @Repository라는 어노테이션이 없어도 IoC 동작함, JpaRepository를 상속했기에 자동 빈으로 등록
public interface UserRepository extends JpaRepository<User, Integer> {

    // findBy규칙 -> Username 문법
    // select * from user where username = #{username}
    // jpa query methods 찾아보면 만드는법 나옴.
    public User findByUsername(String username);
}
