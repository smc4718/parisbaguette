package com.pyj.paris.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReservationDto {
    private int reservationNo;
    private int userNo;
    private String reservationDate;
    private String status;
    private Date createdAt;
}
