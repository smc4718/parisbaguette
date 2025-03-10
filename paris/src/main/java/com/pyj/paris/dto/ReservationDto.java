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
    private UserDto userDto;
    private Date reservationDate;
    private String contents;
    private String status;
    private Date createdAt;

    public String getStatusLabel() {
        switch (this.status) {
            case "PENDING":
                return "승인대기중";
            case "APPROVED":
                return "승인";
            case "REJECTED":
                return "거절";
            default:
                return this.status;
        }
}

}
