package com.pyj.paris.controller;

import com.pyj.paris.dto.ReservationDto;
import com.pyj.paris.dto.UserDto;
import com.pyj.paris.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/reservation")
@Controller
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/request")
    public String requestReservation(@RequestParam("reservationDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date reservationDate,
                                     HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        String contents = request.getParameter("contents");

        if (user == null) {
            return "redirect:/user/login.form";
        }

        ReservationDto reservation = ReservationDto.builder()
                .userNo(user.getUserNo())
                .reservationDate(reservationDate)
                .contents(contents)
                .status("PENDING")
                .build();

        reservationService.requestReservation(reservation);

        return "redirect:/reservation/user";
    }

    @PostMapping("/approve")
    public ResponseEntity<String> approveReservation(@RequestParam int reservationNo, @RequestParam String phoneNumber) {
        try {
            // 예약 승인 처리
            reservationService.approveReservation(reservationNo, phoneNumber);
            return ResponseEntity.ok("예약 승인 완료 및 SMS 발송 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("예약 승인 처리 중 오류 발생: " + e.getMessage());
        }
    }


    @PostMapping("/reject")
    public ResponseEntity<String> rejectReservation(@RequestParam int reservationNo, @RequestParam String phoneNumber) {
        try {
            // 예약 거절 처리
            reservationService.rejectReservation(reservationNo, phoneNumber);
            return ResponseEntity.ok("예약 거절 완료 및 SMS 발송 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("예약 거절 처리 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/pending")
    public String getPendingReservations(Model model) {
        model.addAttribute("pendingReservations", reservationService.getPendingReservations());
        model.addAttribute("allReservations", reservationService.getAllReservations());
        return "reservation/pending";
    }

    @GetMapping("/user")
    public String getUserReservations(HttpServletRequest request, Model model) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/user/login.form";
        }
        model.addAttribute("reservations", reservationService.getUserReservations(user.getUserNo()));
        return "reservation/user";
    }

    @GetMapping("/list")
    @ResponseBody
    public Map<String, List<ReservationDto>> getReservationsByMonth(@RequestParam("year") int year,
                                                                    @RequestParam("month") int month) {
        List<ReservationDto> reservations = reservationService.getReservationsByMonth(year, month);
        Map<String, List<ReservationDto>> reservationMap = new HashMap<>();

        for (ReservationDto reservation : reservations) {
            String dateKey = new SimpleDateFormat("yyyy-MM-dd").format(reservation.getReservationDate());
            reservationMap.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(reservation);
        }

        return reservationMap;
    }

}
