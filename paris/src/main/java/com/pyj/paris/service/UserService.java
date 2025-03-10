package com.pyj.paris.service;

import com.pyj.paris.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    void login(HttpServletRequest request, HttpServletResponse response) throws Exception;
    void logout(HttpServletRequest request, HttpServletResponse response) throws Exception;
    UserDto getUser(String id);
}
