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
        return "/notice/" + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(today);
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
}
