package com.pyj.paris.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EventDto {
    private int eventNo;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String createdAt;
    private List<EventImageDto> images;
}