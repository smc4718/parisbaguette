package com.pyj.paris.service;

import com.pyj.paris.dao.GuideMapper;
import com.pyj.paris.dto.GuideDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GuideServiceImpl implements GuideService{

    private final GuideMapper guideMapper;

    @Override
    public List<GuideDto> getGuideList(int page, int size) {
        int offset = (page - 1) * size;
        return guideMapper.selectGuideList(offset, size);
    }

    @Override
    public GuideDto getGuideDetail(int guideNo) {
        return guideMapper.selectGuideDetail(guideNo);
    }

    @Override
    public boolean addGuide(GuideDto guide) {
        return guideMapper.insertGuide(guide) > 0;
    }

    @Override
    public boolean deleteGuide(int guideNo) {
        return guideMapper.deleteGuide(guideNo) > 0;
    }

}
