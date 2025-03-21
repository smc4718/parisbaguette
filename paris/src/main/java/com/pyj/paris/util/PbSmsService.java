package com.pyj.paris.util;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PbSmsService {

    private final String apiKey;
    private final String apiSecret;

    public PbSmsService() {
        // .env 파일을 로드하고 API 키와 시크릿 키를 환경 변수에서 가져옴
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("COOLSMS_API_KEY");
        this.apiSecret = dotenv.get("COOLSMS_API_SECRET");
    }

    public void sendSms(String toPhoneNumber, String messageText) {
        // CoolSMS API 초기화 (환경변수에서 불러온 API 키와 시크릿 키 사용)
        DefaultMessageService messageService =
                NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");

        // Message 객체 생성 및 발신/수신 번호 설정
        Message message = new Message();
        message.setFrom("010-5040-0714");  // 발신번호 입력
        message.setTo(toPhoneNumber);  // 수신번호 입력
        message.setText(messageText);  // 전송할 문자 메시지 내용

        try {
            // send 메소드로 메시지를 발송
            messageService.send(message);
        } catch (NurigoMessageNotReceivedException exception) {
            // 발송 실패 시 실패한 메시지 목록과 오류 메시지 출력
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            // 그 외의 오류 메시지 출력
            System.out.println(exception.getMessage());
        }
    }
}
