package com.pyj.paris.service;

import com.pyj.paris.dao.NoticeMapper;
import com.pyj.paris.dto.NoticeDto;
import com.pyj.paris.dto.UserDto;
import com.pyj.paris.util.MyPageUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
        int userNo = Integer.parseInt((request.getParameter("userNo")));

        NoticeDto notice = NoticeDto.builder()
                .title(title)
                .contents(contents)
                .userDto(UserDto.builder()
                         .userNo(userNo)
                         .build())
                .build();

        int noticeCount = noticeMapper.insertNotice(notice);
        return noticeCount;
    }


}
