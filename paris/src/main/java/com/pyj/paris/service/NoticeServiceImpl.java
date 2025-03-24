package com.pyj.paris.service;

import com.pyj.paris.dao.NoticeMapper;
import com.pyj.paris.dto.NoticeDto;
import com.pyj.paris.dto.NoticeImageDto;
import com.pyj.paris.dto.UserDto;
import com.pyj.paris.util.PbFileUtils;
import com.pyj.paris.util.PbPageUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;
    private final PbPageUtils pbPageUtils;
    private final PbFileUtils pbFileUtils;


    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getNoticeList(HttpServletRequest request) {

        Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
        int page = Integer.parseInt(opt.orElse("1"));
        int total = noticeMapper.getNoticeCount();
        int display = 7;

        pbPageUtils.setPaging(page, total, display);

        Map<String, Object> map = Map.of("begin", pbPageUtils.getBegin(),
                                         "end", pbPageUtils.getEnd());

        List<NoticeDto> noticeList = noticeMapper.getNoticeList(map);

        return Map.of("noticeList", noticeList,
                      "totalPage", pbPageUtils.getTotalPage());
    }

    @Override
    public void getNotice(int noticeNo, Model model) {
        NoticeDto notice = noticeMapper.getNotice(noticeNo);

        model.addAttribute("notice", notice);
    }

    @Override
    public int addNotice(HttpServletRequest request) {
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        int userNo = Integer.parseInt(request.getParameter("userNo"));

        // SVG 태그 필터링 (JSoup을 사용하여 제거)
        contents = removeSvgTags(contents);

        // 현재 날짜 및 시간 설정
        String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        NoticeDto notice = NoticeDto.builder()
                .title(title)
                .contents(contents)
                .userDto(UserDto.builder()
                        .userNo(userNo)
                        .build())
                .createdAt(createdAt)
                .build();

        int addResult = noticeMapper.insertNotice(notice);

        // Editor에 추가한 이미지 목록 가져와서 NOTICE_IMAGE_T에 저장하기
        for(String editorImage : getEditorImageList(contents)) {
            NoticeImageDto noticeImage = NoticeImageDto.builder()
                    .noticeNo(notice.getNoticeNo())
                    .imagePath(pbFileUtils.getNoticeImagePath())
                    .filesystemName(editorImage)
                    .build();
            noticeMapper.insertNoticeImage(noticeImage);
        }

        return addResult;
    }

    @Override
    public int modifyNotice(HttpServletRequest request) {
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        int noticeNo = Integer.parseInt(request.getParameter("noticeNo"));

        // SVG 태그 필터링 (JSoup을 사용하여 제거)
        contents = removeSvgTags(contents);

        // DB에 저장된 기존 이미지 가져오기
        List<NoticeImageDto> noticeImageDtoList = noticeMapper.getNoticeImageList(noticeNo);
        List<String> noticeImageList = noticeImageDtoList.stream()
                .map(noticeImageDto -> noticeImageDto.getFilesystemName())
                .collect(Collectors.toList());

        // Editor에 포함된 이미지 이름(filesystemName)
        List<String> editorImageList = getEditorImageList(contents);

        // Editor에 포함되어 있으나 기존 이미지에 없는 이미지는 NOTICE_IMAGE_T에 추가해야 함
        editorImageList.stream()
                .filter(editorImage -> !noticeImageList.contains(editorImage))
                .map(editorImage -> NoticeImageDto.builder()
                        .noticeNo(noticeNo)
                        .imagePath(pbFileUtils.getNoticeImagePath())
                        .filesystemName(editorImage)
                        .build())
                .forEach(noticeImageDto -> noticeMapper.insertNoticeImage(noticeImageDto));

        // 기존 이미지에 있으나 Editor에 포함되지 않은 이미지는 삭제해야 함
        List<NoticeImageDto> removeList = noticeImageDtoList.stream()
                .filter(blogImageDto -> !editorImageList.contains(blogImageDto.getFilesystemName()))
                .collect(Collectors.toList());

        for(NoticeImageDto noticeImageDto : removeList) {
            // NOTICE_IMAGE_T에서 삭제
            noticeMapper.deleteNoticeImage(noticeImageDto.getFilesystemName());
            // 파일 삭제
            File file = new File(noticeImageDto.getImagePath(), noticeImageDto.getFilesystemName());
            if(file.exists()) {
                file.delete();
            }
        }

        NoticeDto notice = NoticeDto.builder()
                .title(title)
                .contents(contents)
                .noticeNo(noticeNo)
                .build();

        int modifyResult = noticeMapper.updateNotice(notice);

        return modifyResult;
    }

    // SVG 태그를 제거하는 메서드
    private String removeSvgTags(String contents) {
        Document document = Jsoup.parse(contents);
        // svg 태그를 제거
        Elements svgElements = document.getElementsByTag("svg");
        for (Element svg : svgElements) {
            svg.remove();
        }
        return document.html();
    }

    @Override
    public int removeNotice(HttpServletRequest request) {
        int noticeNo = Integer.parseInt(request.getParameter("noticeNo"));

        // NOTICE_IMAGE_T 목록 가져와서 파일 삭제
        List<NoticeImageDto> noticeImageList = noticeMapper.getNoticeImageList(noticeNo);
        for(NoticeImageDto noticeImage : noticeImageList) {
            File file = new File(noticeImage.getImagePath(), noticeImage.getFilesystemName());
            if(file.exists()) {
                file.delete();
            }
        }

        noticeMapper.deleteNoticeImageList(noticeNo);

        return noticeMapper.deleteNotice(noticeNo);
    }

    @Override
    public int increaseHit(int noticeNo) {
        return noticeMapper.updateHit(noticeNo);
    }

    @Override
    public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest) {

        // 이미지 저장 경로
        String imagePath = pbFileUtils.getNoticeImagePath();
        File dir = new File(imagePath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        // 이미지 파일 (CKEditor는 이미지를 upload라는 이름으로 보냄)
        MultipartFile upload = multipartRequest.getFile("upload");

        // 이미지가 저장될 이름
        String originalFilename = upload.getOriginalFilename();
        String filesystemName = pbFileUtils.getFilesystemName(originalFilename);

        // 이미지 File 객체
        File file = new File(dir, filesystemName);

        // 저장
        try {
            upload.transferTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // CKEditor로 저장된 이미지의 경로를 JSON 형식으로 반환
        String imageUrl = "/paris/notice/" + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now()) + "/" + filesystemName;
        return Map.of("uploaded", true, "url", imageUrl);
    }

    @Override
    public List<String> getEditorImageList(String contents) {

        // Editor에 추가한 이미지 목록 반환 (Jsoup)

        List<String> editorImageList = new ArrayList<>();

        Document document = Jsoup.parse(contents);
        Elements elements =  document.getElementsByTag("img");

        if(elements != null) {
            for(Element element : elements) {
                String src = element.attr("src");
                String filesystemName = src.substring(src.lastIndexOf("/") + 1);
                editorImageList.add(filesystemName);
            }
        }

        return editorImageList;
    }

}
