package com.pyj.paris.dao;

import com.pyj.paris.dto.EventDto;
import com.pyj.paris.dto.EventImageDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EventMapper {
    void insertEvent(EventDto eventDto);
    void insertEventImage(EventImageDto imageDto);
    List<EventDto> selectEventList();
    EventDto selectEventByNo(int eventNo);
}
