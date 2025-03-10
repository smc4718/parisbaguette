package com.pyj.paris.service;

import com.pyj.paris.dao.ReservationMapper;
import com.pyj.paris.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;

    @Override
    public void requestReservation(ReservationDto reservation) {
        reservationMapper.insertReservation(reservation);
    }

    @Override
    public void approveReservation(int reservationNo) {
        reservationMapper.approveReservation(reservationNo);
    }

    @Override
    public void rejectReservation(int reservationNo) {
        reservationMapper.rejectReservation(reservationNo);
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
}
