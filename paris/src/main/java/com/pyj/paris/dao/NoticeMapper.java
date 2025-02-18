package com.pyj.paris.dao;

import com.pyj.paris.dto.NoticeDto;
import com.pyj.paris.dto.NoticeImageDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper {

    public int getNoticeCount();
    public List<NoticeDto> getNoticeList(Map<String,Object> map);
    public NoticeDto getNotice(int noticeNo);
    public int insertNotice(NoticeDto notice);
    public int updateNotice(NoticeDto notice);
    public int deleteNotice(int noticeNo);
    public int updateHit(int noticeNo);

    public int insertNoticeImage(NoticeImageDto noticeImage);
    public List<NoticeImageDto> getNoticeImageList(int noticeNo);
    public int deleteNoticeImage(String filesystemName);
    public int deleteNoticeImageList(int noticeNo);
}
