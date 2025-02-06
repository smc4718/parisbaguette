package com.pyj.paris.service;

import com.pyj.paris.dto.NoticeDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface NoticeService {

    public void loadNoticeList(HttpServletRequest request, Model model);
    public NoticeDto getNotice(int noticeNo, Model model);
    public int addNotice(HttpServletRequest request);
}
