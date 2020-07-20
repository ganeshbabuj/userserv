package com.example.service;

import com.example.vo.LoginRequest;
import com.example.vo.LoginResponse;
import com.example.vo.RegistrationResponse;
import com.example.vo.User;

public interface UserService {

    LoginResponse login(LoginRequest loginRequest);

    RegistrationResponse register(User user);

    User search(String username);

    String refresh(String username);

}
