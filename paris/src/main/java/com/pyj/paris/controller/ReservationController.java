package com.pyj.paris.controller;

import com.pyj.paris.dto.ReservationDto;
import com.pyj.paris.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/reservation")
@Controller
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 요청 - 서비스가 전체 로직 처리
    @PostMapping("/request")
    public String requestReservation(@RequestParam("reservationDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date reservationDate,
                                     HttpServletRequest request) {
        return reservationService.handleRequestReservation(reservationDate, request);
    }

    // 예약 승인
    @PostMapping("/approve")
    public ResponseEntity<Map<String, String>> approveReservation(@RequestParam int reservationNo,
                                                                  @RequestParam String phoneNumber) {
        return reservationService.handleApproveReservation(reservationNo, phoneNumber);
    }

    // 예약 거절
    @PostMapping("/reject")
    public ResponseEntity<Map<String, String>> rejectReservation(@RequestParam int reservationNo,
                                                                 @RequestParam String phoneNumber) {
        return reservationService.handleRejectReservation(reservationNo, phoneNumber);
    }

    // 대기 중 + 전체 예약 목록
    @GetMapping("/pending")
    public String getPendingReservations(Model model) {
        return reservationService.handlePendingReservationPage(model);
    }

    // 유저 개인 예약 목록
    @GetMapping("/user")
    public String getUserReservations(HttpServletRequest request, Model model) {
        return reservationService.handleUserReservationPage(request, model);
    }

    // 월별 예약 데이터 (캘린더용)
    @GetMapping("/list")
    @ResponseBody
    public Map<String, List<ReservationDto>> getReservationsByMonth(@RequestParam("year") int year,
                                                                    @RequestParam("month") int month) {
        return reservationService.handleReservationsByMonth(year, month);
    }

    // 전체 예약 목록
    @GetMapping("/all")
    @ResponseBody
    public List<ReservationDto> getAllReservations() {
        return reservationService.getAllReservations();
    }
}