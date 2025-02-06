package com.pyj.paris.dao;

import com.pyj.paris.dto.NoticeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper {

    public int getNoticeCount();
    public List<NoticeDto> getNoticeList(Map<String,Object> map);
    public NoticeDto getNotice(int noticeNo);
    public int insertNotice(NoticeDto notice);
}
