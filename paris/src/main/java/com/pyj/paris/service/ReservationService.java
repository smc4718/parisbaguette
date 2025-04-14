package com.pyj.paris.service;

import com.pyj.paris.dto.ReservationDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReservationService {
    // 기존 로직
    List<ReservationDto> getPendingReservations();
    List<ReservationDto> getUserReservations(int userNo);
    List<ReservationDto> getAllReservations();
    List<ReservationDto> getReservationsByMonth(int year, int month);

    // 컨트롤러 로직 처리용
    String handleRequestReservation(Date reservationDate, HttpServletRequest request);
    ResponseEntity<Map<String, String>> handleApproveReservation(int reservationNo, String phoneNumber);
    ResponseEntity<Map<String, String>> handleRejectReservation(int reservationNo, String phoneNumber);
    String handlePendingReservationPage(Model model);
    String handleUserReservationPage(HttpServletRequest request, Model model);
    Map<String, List<ReservationDto>> handleReservationsByMonth(int year, int month);
}