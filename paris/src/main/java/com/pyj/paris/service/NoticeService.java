package com.pyj.paris.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;

public interface NoticeService {

    public Map<String, Object> getNoticeList(HttpServletRequest request);
    public void getNotice(int noticeNo, Model model);
    public int addNotice(HttpServletRequest request);
    public int modifyNotice(HttpServletRequest request);
    public int removeNotice(HttpServletRequest request);
    public int increaseHit(int noticeNo);

    public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest);
    public List<String> getEditorImageList(String contents);
}
