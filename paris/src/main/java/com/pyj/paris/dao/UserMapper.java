package com.pyj.paris.dao;

import com.pyj.paris.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UserMapper {
    UserDto getUser(Map<String, Object> map);

}
