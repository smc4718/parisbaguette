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
    private String email;
    private String pw;
    private String name;
    private String gender;
    private String mobile;
    private String joinedAt;

}
