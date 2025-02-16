package com.pyj.paris.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class NoticeDto {
    private int noticeNo;
    private UserDto userDto;
    private String title;
    private String contents;
    private int hit;
    private String createdAt;
}
