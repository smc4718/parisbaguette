package com.pyj.paris.service;

import com.pyj.paris.dao.EventMapper;
import com.pyj.paris.dto.EventDto;
import com.pyj.paris.dto.EventImageDto;
import com.pyj.paris.util.PbFileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;
    private final PbFileUtils pbFileUtils;

    @Transactional
    @Override
    public void handleEventUpload(String title, String description, String startDate,
                                  String endDate, MultipartFile imageFile) {

        // 날짜 유효성 검사
        if (startDate == null || startDate.isBlank() || endDate == null || endDate.isBlank()) {
            throw new IllegalArgumentException("날짜가 비어있습니다");
        }

        // 이미지 저장
        String imagePath = pbFileUtils.getEventImagePath(); // 예: D:/paris/event/2025/03/29
        String filesystemName = pbFileUtils.save(imagePath, imageFile);
        String dbPath = imagePath.replace("D:", "").replace("\\", "/");

        // 이미지 DTO 생성
        EventImageDto imageDto = EventImageDto.builder()
                .imagePath(dbPath)
                .filesystemName(filesystemName)
                .build();

        // 이벤트 DTO 생성
        EventDto eventDto = EventDto.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .images(List.of(imageDto))
                .build();

        addEvent(eventDto); // 기존 저장 로직 재사용
    }

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