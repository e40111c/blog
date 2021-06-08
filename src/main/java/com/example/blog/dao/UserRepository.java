package com.example.blog.dao;

import com.example.blog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    //定義需要透過帳號密碼才能去資料庫查詢使用者
    User findByUsernameAndPassword(String username , String password);

}
