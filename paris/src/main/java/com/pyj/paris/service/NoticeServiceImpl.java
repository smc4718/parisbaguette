package com.pyj.paris.service;

import com.pyj.paris.dao.NoticeMapper;
import com.pyj.paris.dto.NoticeDto;
import com.pyj.paris.util.MyPageUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;
    private final MyPageUtils myPageUtils;

    @Override
    public void loadNoticeList(HttpServletRequest request, Model model) {

        Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
        int page = Integer.parseInt(opt.orElse("1"));
        int total = noticeMapper.getNoticeCount();
        int display = 5;

        myPageUtils.setPaging(page, total, display);

        Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                , "end", myPageUtils.getEnd());

        List<NoticeDto> noticeList = noticeMapper.getNoticeList(map);

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/notice/list.do"));
        model.addAttribute("beginNo", total - (page - 1) * display);
    }

    @Override
    public NoticeDto getNotice(int noticeNo, Model model) {
        NoticeDto notice = noticeMapper.getNotice(noticeNo);

        model.addAttribute("notice", notice);
        return notice;
    }

    @Override
    public int addNotice(HttpServletRequest request) {
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");

        // 현재 날짜 및 시간 설정
        String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        NoticeDto notice = NoticeDto.builder()
                .title(title)
                .contents(contents)
                .hit(0) // 기본값 설정
                .createdAt(createdAt)
                .build();

        int addResult = noticeMapper.insertNotice(notice);
        return addResult;
    }


}
