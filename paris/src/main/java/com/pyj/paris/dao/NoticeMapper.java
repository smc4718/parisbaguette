package com.pyj.paris.dao;

import com.pyj.paris.dto.NoticeDto;
import com.pyj.paris.dto.NoticeImageDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper {

    int getNoticeCount();
    List<NoticeDto> getNoticeList(Map<String,Object> map);
    NoticeDto getNotice(int noticeNo);
    int insertNotice(NoticeDto notice);
    int updateNotice(NoticeDto notice);
    int deleteNotice(int noticeNo);
    int updateHit(int noticeNo);

    int insertNoticeImage(NoticeImageDto noticeImage);
    List<NoticeImageDto> getNoticeImageList(int noticeNo);
    int deleteNoticeImage(String filesystemName);
    int deleteNoticeImageList(int noticeNo);
}
