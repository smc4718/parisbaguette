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

        int addResult = noticeMapper.insertNotice(notice); //일반적으로 INSERT 쿼리를 실행하면 영향을 받은 행(row)의 개수가 반환됩니다.
        return addResult;                                  //정상적으로 1개의 공지가 추가되면 1이 반환되고, 오류가 생겨서 데이터가 추가되지 않으면 0이 반환될 수도 있습니다.
    }         //noticeMapper.insertNotice(notice);가 반환하는 값을 addResult에 저장하는 이유는 이 값을 나중에 활용하기 위해서입니다. 이를 통해 "공지 추가" 성공 여부를 판단할 수 있습니다.
              //그래서 행의 개수가 반환되기 때문에, int 타입 변수에 넣어서 return시에 int 타입으로 반환한 것입니다.
              //이 행의 개수라는 것은, 성공하면 1, 실패하면 0을 반환한다는 뜻이다.

    @Override
    public int modifyNotice(HttpServletRequest request) {
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        int noticeNo = Integer.parseInt(request.getParameter("noticeNo"));

        NoticeDto notice = NoticeDto.builder()
                .title(title)
                .contents(contents)
                .noticeNo(noticeNo)
                .build();

        int modifyResult = noticeMapper.updateNotice(notice);

        return modifyResult;
    }

    @Override
    public int removeNotice(HttpServletRequest request) {
        int noticeNo = Integer.parseInt(request.getParameter("noticeNo"));
        return noticeMapper.deleteNotice(noticeNo);
    }


}
