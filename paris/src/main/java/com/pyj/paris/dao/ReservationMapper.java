package com.pyj.paris.dao;

import com.pyj.paris.dto.ReservationDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationMapper {
    void insertReservation(ReservationDto reservation);
    void approveReservation(int reservationNo);
    void rejectReservation(int reservationNo);
    List<ReservationDto> getPendingReservations();
    List<ReservationDto> getUserReservations(int userNo);
    List<ReservationDto> getAllReservations();
    List<ReservationDto> getReservationsByMonth(int year, int month);
}
