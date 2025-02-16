package com.pyj.paris.service;

import com.pyj.paris.dao.UserMapper;
import com.pyj.paris.dto.UserDto;
import com.pyj.paris.util.PbSecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.util.Map;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PbSecurityUtils pbSecurityUtils;

    @Override
    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        String pw = pbSecurityUtils.getSHA256(request.getParameter("pw"));

        Map<String, Object> map = Map.of("id", id, "pw", pw);

        UserDto user = userMapper.getUser(map);

        if (user != null) {
            request.getSession().setAttribute("user", user);
        } else {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('일치하는 회원 정보가 없습니다.')");
            out.println("location.href='" + request.getContextPath() + "/main'");
            out.println("</script>");
            out.flush();
            out.close();
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().invalidate();

        response.sendRedirect(request.getContextPath() + "/user/login.form");
    }

    @Override
    public UserDto getUser(String id) {
        return userMapper.getUser(Map.of("id", id));
    }
}
