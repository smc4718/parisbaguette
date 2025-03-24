package com.pyj.paris.dao;

import com.pyj.paris.dto.GuideDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GuideMapper {
    List<GuideDto> selectGuideList(@Param("offset") int offset, @Param("limit") int limit);
    GuideDto selectGuideDetail(int guideNo);
    int insertGuide(GuideDto guide);
    int deleteGuide(int guideNo);
}