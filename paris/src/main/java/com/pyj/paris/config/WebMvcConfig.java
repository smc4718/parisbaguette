package com.pyj.paris.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 정적 리소스
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/templates/");

        // 공지 이미지
        registry.addResourceHandler("/paris/notice/**")
                .addResourceLocations("file:/paris/notice/");

        // 파일 업로드
        registry.addResourceHandler("/paris/upload/**")
                .addResourceLocations("file:/paris/upload/");

        // 이벤트 이미지 추가
        registry.addResourceHandler("/paris/event/**") // URL 경로
                .addResourceLocations("file:D:/paris/event/"); // 실제 물리 경로

    }
}