package com.pyj.paris.service;

import com.pyj.paris.dto.ReservationDto;

import java.util.List;

public interface ReservationService {
    void requestReservation(ReservationDto reservation); // 예약 요청
    void approveReservation(int reservationNo); // 예약 승인
    void rejectReservation(int reservationNo); // 예약 거절
    List<ReservationDto> getPendingReservations(); // 대기 중인 예약 목록
    List<ReservationDto> getUserReservations(int userNo); // 특정 사용자의 예약 목록
    List<ReservationDto> getAllReservations(); // 모든 예약 내역
}
