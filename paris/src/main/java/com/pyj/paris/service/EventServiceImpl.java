package com.pyj.paris.service;

import com.pyj.paris.dao.EventMapper;
import com.pyj.paris.dto.EventDto;
import com.pyj.paris.dto.EventImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;

    @Transactional
    @Override
    public void addEvent(EventDto eventDto) {
        eventMapper.insertEvent(eventDto);
        if (eventDto.getImages() != null) {
            for (EventImageDto image : eventDto.getImages()) {
                image.setEventNo(eventDto.getEventNo());
                eventMapper.insertEventImage(image);
            }
        }
    }

    @Override
    public List<EventDto> getEventList() {
        return eventMapper.selectEventList();
    }

    @Override
    public EventDto getEventDetail(int eventNo) {
        return eventMapper.selectEventByNo(eventNo);
    }
}