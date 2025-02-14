package com.pyj.paris.service;

import com.pyj.paris.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception;
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception;
    public UserDto getUser(String id);
}
