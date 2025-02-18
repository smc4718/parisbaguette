package com.pyj.paris.util;

import org.springframework.stereotype.Component;

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
