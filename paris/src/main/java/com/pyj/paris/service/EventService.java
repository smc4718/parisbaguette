package com.pyj.paris.service;

import com.pyj.paris.dto.EventDto;

import java.util.List;

public interface EventService {
    void addEvent(EventDto eventDto);
    List<EventDto> getEventList();
    EventDto getEventDetail(int eventNo);
}