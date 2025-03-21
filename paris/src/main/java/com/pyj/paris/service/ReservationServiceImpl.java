package com.pyj.paris.service;

import com.pyj.paris.dao.ReservationMapper;
import com.pyj.paris.dto.ReservationDto;
import com.pyj.paris.util.PbSmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;
    private final PbSmsService pbSmsService;

    @Override
    public void requestReservation(ReservationDto reservation) {
        reservationMapper.insertReservation(reservation);
    }

    @Transactional
    @Override
    public void approveReservation(int reservationNo, String phoneNumber) {
        // 예약 승인 처리
        reservationMapper.approveReservation(reservationNo);

        // 승인 SMS 전송
        pbSmsService.sendSms(phoneNumber, "요청하신 휴일 예약이 승인되었습니다.");
    }

    @Transactional
    @Override
    public void rejectReservation(int reservationNo, String phoneNumber) {
        // 예약 거절 처리
        reservationMapper.rejectReservation(reservationNo);

        // 거절 SMS 전송
        pbSmsService.sendSms(phoneNumber, "요청하신 휴일 예약이 거절되었습니다.");
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
