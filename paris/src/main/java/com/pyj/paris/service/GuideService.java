package com.pyj.paris.service;

import com.pyj.paris.dto.GuideDto;

import java.util.List;

public interface GuideService {
    List<GuideDto> getGuideList(int page, int size);
    GuideDto getGuideDetail(int guideNo);
    boolean addGuide(GuideDto guide);
    boolean deleteGuide(int guideNo);
}
