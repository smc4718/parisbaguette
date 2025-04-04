package com.pyj.paris.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class PbFileUtils {

    // 공지 작성시 사용된 이미지가 저장될 경로 반환
    public String getNoticeImagePath() {
        LocalDate today = LocalDate.now();
        String baseDir = System.getProperty("os.name").toLowerCase().contains("win") ? "D:/paris/notice/" : "/paris/notice/";
        return baseDir + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(today);
    }


    // 공지 게시판 작성시 첨부한 파일이 저장될 경로 반환
    public String getUploadPath() {
        LocalDate today = LocalDate.now();
        return "/paris/upload/" + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(today);
    }

    // 이벤트 이미지가 저장될 경로 반환
    public String getEventImagePath() {
        LocalDate today = LocalDate.now();
        String os = System.getProperty("os.name").toLowerCase();
        String baseDir = os.contains("win") ? "D:/paris/event/" : "/paris/event/";
        return baseDir + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(today);
    }

    public String save(String path, MultipartFile multipartFile) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filesystemName = getFilesystemName(multipartFile.getOriginalFilename());

        File file = new File(dir, filesystemName);
        try {
            multipartFile.transferTo(file);
            System.out.println("[파일 저장 성공] " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + file.getAbsolutePath(), e);
        }

        return filesystemName;
    }

    // 임시 파일이 저장될 경로 반환하기 (zip 파일)
    public String getTempPath() {
        return "/paris/temp/";
    }

    // 파일이 저장될 이름 반환하기
    public String getFilesystemName(String originalFilename) {

        /*  UUID.확장자  */

        String extName = null;
        if(originalFilename.endsWith("tar.gz")) {  // 확장자에 마침표가 포함되는 예외 경우를 처리한다.
            extName = "tar.gz";
        } else {
            String[] arr = originalFilename.split("\\.");  // [.] 또는 \\.
            extName = arr[arr.length - 1];
        }

        return UUID.randomUUID().toString().replace("-", "") + "." + extName;

    }

    // 임시 파일 이름 반환하기 (확장자는 제외하고 이름만 반환)
    public String getTempFilename() {
        return System.currentTimeMillis() + "";
    }
}
