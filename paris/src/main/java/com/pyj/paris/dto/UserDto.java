package com.pyj.paris.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
    private int userNo;
    private String id;
    private String pw;
    private String name;
    private String gender;
    private String email;
    private String hp;
    private String hpSmsYn;
    private String joinedAt;

}
