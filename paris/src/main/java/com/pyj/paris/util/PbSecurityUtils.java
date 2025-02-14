package com.pyj.paris.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;

@Component
public class PbSecurityUtils {

    /*
     * SHA256 암호화
     */
    public String getSHA256(String password) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            byte[] b = messageDigest.digest();  // 암호화된 32바이트 배열이 생성됨
            for(int i = 0; i < b.length; i++) {
                sb.append(String.format("%02X", b[i]));  // 2자리 16진수 문자열로 변환하기
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
