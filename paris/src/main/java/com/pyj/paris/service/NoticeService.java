package com.pyj.paris.service;

import com.pyj.paris.dto.NoticeDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

public interface NoticeService {

    public void loadNoticeList(HttpServletRequest request, Model model);
    public NoticeDto getNotice(int noticeNo, Model model);
    public int addNotice(HttpServletRequest request);
    public int modifyNotice(HttpServletRequest request);
    public int removeNotice(HttpServletRequest request);
}
