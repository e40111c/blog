package com.example.blog.service;

import com.example.blog.dao.UserRepository;
import com.example.blog.po.User;
import com.example.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service //要加service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository; //建立操作資料庫的interface

    @Override
    public User checkUser(String username, String password) {

        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));


        return user;
    }
}
