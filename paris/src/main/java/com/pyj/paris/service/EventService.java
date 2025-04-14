package com.pyj.paris.service;

import com.pyj.paris.dto.EventDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    void addEvent(EventDto eventDto);
    List<EventDto> getEventList();
    EventDto getEventDetail(int eventNo);

    void handleEventUpload(String title, String description, String startDate,
                           String endDate, MultipartFile imageFile);
}