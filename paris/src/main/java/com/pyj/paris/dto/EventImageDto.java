package com.pyj.paris.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EventImageDto {
    private int eventImageNo;
    private int eventNo;
    private String imagePath;
    private String filesystemName;
}