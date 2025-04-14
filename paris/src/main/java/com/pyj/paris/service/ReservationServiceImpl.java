package com.pyj.paris.service;

import com.pyj.paris.dao.ReservationMapper;
import com.pyj.paris.dto.ReservationDto;
import com.pyj.paris.dto.UserDto;
import com.pyj.paris.util.PbSmsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;
    private final PbSmsService pbSmsService;

    // 예약 요청 처리
    @Override
    public String handleRequestReservation(Date reservationDate, HttpServletRequest request) {
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

        reservationMapper.insertReservation(reservation);
        return "redirect:/reservation/user";
    }

    // 예약 승인 처리
    @Transactional
    @Override
    public ResponseEntity<Map<String, String>> handleApproveReservation(int reservationNo, String phoneNumber) {
        try {
            reservationMapper.approveReservation(reservationNo);
            pbSmsService.sendSms(phoneNumber, "요청하신 휴일 예약이 승인되었습니다.");
            return ResponseEntity.ok(Map.of("message", "예약이 승인되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "예약 승인 처리 중 오류 발생: " + e.getMessage()));
        }
    }

    // 예약 거절 처리
    @Transactional
    @Override
    public ResponseEntity<Map<String, String>> handleRejectReservation(int reservationNo, String phoneNumber) {
        try {
            reservationMapper.rejectReservation(reservationNo);
            pbSmsService.sendSms(phoneNumber, "요청하신 휴일 예약이 거절되었습니다.");
            return ResponseEntity.ok(Map.of("message", "예약이 거절되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "예약 거절 처리 중 오류 발생: " + e.getMessage()));
        }
    }

    // 관리자 페이지용 - 대기 + 전체 예약 목록 구성
    @Override
    public String handlePendingReservationPage(Model model) {
        model.addAttribute("pendingReservations", getPendingReservations());
        model.addAttribute("allReservations", getAllReservations());
        return "reservation/pending";
    }

    // 사용자 예약 목록 구성
    @Override
    public String handleUserReservationPage(HttpServletRequest request, Model model) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/user/login.form";
        }
        model.addAttribute("reservations", getUserReservations(user.getUserNo()));
        return "reservation/user";
    }

    // 월별 예약 조회 → 날짜별로 그룹핑된 Map 반환
    @Override
    public Map<String, List<ReservationDto>> handleReservationsByMonth(int year, int month) {
        List<ReservationDto> reservations = getReservationsByMonth(year, month);
        Map<String, List<ReservationDto>> map = new HashMap<>();

        for (ReservationDto reservation : reservations) {
            String key = new SimpleDateFormat("yyyy-MM-dd").format(reservation.getReservationDate());
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(reservation);
        }

        return map;
    }

    @Override
    public List<ReservationDto> getPendingReservations() {
        return reservationMapper.getPendingReservations();
    }

    @Override
    public List<ReservationDto> getUserReservations(int userNo) {
        return reservationMapper.getUserReservations(userNo);
    }

    @Override
    public List<ReservationDto> getAllReservations() {
        return reservationMapper.getAllReservations();
    }

    @Override
    public List<ReservationDto> getReservationsByMonth(int year, int month) {
        return reservationMapper.getReservationsByMonth(year, month);
    }
}