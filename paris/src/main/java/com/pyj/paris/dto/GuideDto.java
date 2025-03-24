package com.pyj.paris.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideDto {
    private int guideNo;
    private String title;
    private String youtubeUrl;
    private String description;
    private String createdAt;
}