package com.pyj.paris.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;

public interface NoticeService {

    Map<String, Object> getNoticeList(HttpServletRequest request);
    void getNotice(int noticeNo, Model model);
    int addNotice(HttpServletRequest request);
    int modifyNotice(HttpServletRequest request);
    int removeNotice(HttpServletRequest request);
    int increaseHit(int noticeNo);

    Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest);
    List<String> getEditorImageList(String contents);
}
