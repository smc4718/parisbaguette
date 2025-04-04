package com.pyj.paris.controller;

import com.pyj.paris.dto.EventDto;
import com.pyj.paris.dto.EventImageDto;
import com.pyj.paris.service.EventService;
import com.pyj.paris.util.PbFileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final PbFileUtils pbFileUtils;

    // 이벤트 등록
    @PostMapping("/upload")
    public ResponseEntity<Void> uploadEvent(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam("image") MultipartFile imageFile
    ) {

        // 날짜 유효성 검사
        if (startDate == null || startDate.isBlank() || endDate == null || endDate.isBlank()) {
            throw new IllegalArgumentException("날짜가 비어있습니다");
        }

        // 1. 이미지 저장
        String imagePath = pbFileUtils.getEventImagePath(); // D:/paris/event/2025/03/29
        String filesystemName = pbFileUtils.save(imagePath, imageFile);

        // 2. DB에 저장할 경로로 변환
        String dbPath = imagePath.replace("D:", "").replace("\\", "/");

        // 3. 이미지 DTO 구성
        EventImageDto imageDto = EventImageDto.builder()
                .imagePath(dbPath) // 🔥 웹에서 접근할 수 있는 상대 경로로 저장
                .filesystemName(filesystemName)
                .build();

        // 4. 이벤트 DTO 구성
        EventDto eventDto = EventDto.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .images(List.of(imageDto))
                .build();

        // 5. DB 저장
        eventService.addEvent(eventDto);

        // 6. 응답
        return ResponseEntity.ok().build();
    }

    // 이벤트 전체 조회
    @GetMapping
    public ResponseEntity<List<EventDto>> list() {
        List<EventDto> events = eventService.getEventList();
        return ResponseEntity.ok(events);
    }

    // 이벤트 단건 조회
    @GetMapping("/{eventNo}")
    public ResponseEntity<EventDto> detail(@PathVariable int eventNo) {
        EventDto event = eventService.getEventDetail(eventNo);
        return ResponseEntity.ok(event);
    }

    // JSON 등록 (미사용 시 제거 가능)
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody EventDto eventDto) {
        eventService.addEvent(eventDto);
        return ResponseEntity.ok().build();
    }

    // 수정 (미구현)
    @PutMapping("/{eventNo}")
    public ResponseEntity<Void> update(@PathVariable int eventNo, @RequestBody EventDto eventDto) {
        eventDto.setEventNo(eventNo);
        return ResponseEntity.ok().build();
    }

    // 삭제 (미구현)
    @DeleteMapping("/{eventNo}")
    public ResponseEntity<Void> delete(@PathVariable int eventNo) {
        return ResponseEntity.ok().build();
    }
}