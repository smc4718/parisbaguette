package com.pyj.paris.controller;

import com.pyj.paris.dto.ReservationDto;
import com.pyj.paris.dto.UserDto;
import com.pyj.paris.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 요청 (사용자가 날짜를 선택하여 요청)
    @PostMapping("/request")
    public String requestReservation(@RequestParam("reservationDate") String reservationDateStr, HttpServletRequest request) {
        // 세션에서 사용자 정보 가져오기
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/user/login.form";
        }

        // String 날짜를 Date로 변환
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // 날짜 형식 지정 (예: "2025-03-10")
        Date reservationDate = null;
        try {
            reservationDate = sdf.parse(reservationDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "redirect:/reservation/user"; // 오류 시 예약 페이지로 이동
        }

        // 예약 DTO 생성 및 정보 설정
        ReservationDto reservation = new ReservationDto();
        reservation.setUserNo(user.getUserNo());
        reservation.setReservationDate(String.valueOf(reservationDate));  // Date 형식으로 예약 날짜 설정

        // 예약 요청 처리
        reservationService.requestReservation(reservation);

        return "redirect:/reservation/user"; // 예약 내역 페이지로 이동
    }

    // 예약 승인 (관리자가 요청을 승인)
    @PostMapping("/approve")
    public String approveReservation(@RequestParam("reservationNo") int reservationNo) {
        reservationService.approveReservation(reservationNo);
        return "redirect:/reservation/pending"; // 대기 중인 예약 목록 갱신
    }

    // 예약 거절 (관리자가 요청을 거절)
    @PostMapping("/reject")
    public String rejectReservation(@RequestParam("reservationNo") int reservationNo) {
        reservationService.rejectReservation(reservationNo);
        return "redirect:/reservation/pending"; // 대기 중인 예약 목록 갱신
    }

    // 대기 중인 예약 목록 조회 (관리자 페이지)
    @GetMapping("/pending")
    public String getPendingReservations(Model model) {
        List<ReservationDto> reservations = reservationService.getPendingReservations();
        model.addAttribute("reservations", reservations);
        return "reservation/pending";
    }

    // 특정 사용자의 예약 목록 조회 (사용자 페이지)
    @GetMapping("/user")
    public String getUserReservations(HttpServletRequest request, Model model) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/user/login.form";
        }
        int userNo = user.getUserNo(); // 세션에서 가져온 userNo

        // 사용자의 예약 목록을 가져와 모델에 추가
        List<ReservationDto> reservations = reservationService.getUserReservations(userNo);
        model.addAttribute("reservations", reservations);

        return "reservation/user"; // 예약 목록 페이지 반환
    }

}
