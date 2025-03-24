package com.pyj.paris.controller;

import com.pyj.paris.dto.GuideDto;
import com.pyj.paris.service.GuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/guide")
@RestController
public class GuideController {

    private final GuideService guideService;

    @GetMapping("/list")
    public List<GuideDto> getGuideList(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return guideService.getGuideList(page, size);
    }

    @GetMapping("/{guideNo}")
    public GuideDto getGuideDetail(@PathVariable int guideNo) {
        return guideService.getGuideDetail(guideNo);
    }

    @PostMapping("/add")
    public boolean addGuide(@RequestBody GuideDto guide) {
        return guideService.addGuide(guide);
    }

    @DeleteMapping("/delete/{guideNo}")
    public boolean deleteGuide(@PathVariable int guideNo) {
        return guideService.deleteGuide(guideNo);
    }
}
