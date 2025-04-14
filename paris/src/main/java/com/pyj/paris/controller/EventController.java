package com.pyj.paris.controller;

import com.pyj.paris.dto.EventDto;
import com.pyj.paris.service.EventService;
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

    // 이벤트 등록 요청 → 모든 로직은 서비스가 처리
    @PostMapping("/upload")
    public ResponseEntity<Void> uploadEvent(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam("image") MultipartFile imageFile
    ) {
        eventService.handleEventUpload(title, description, startDate, endDate, imageFile);
        return ResponseEntity.ok().build();
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<EventDto>> list() {
        return ResponseEntity.ok(eventService.getEventList());
    }

    // 단건 조회
    @GetMapping("/{eventNo}")
    public ResponseEntity<EventDto> detail(@PathVariable int eventNo) {
        return ResponseEntity.ok(eventService.getEventDetail(eventNo));
    }

    // JSON 등록 (미사용 시 제거 가능)
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody EventDto eventDto) {
        eventService.addEvent(eventDto);
        return ResponseEntity.ok().build();
    }
}