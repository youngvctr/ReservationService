package com.project.reservation.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * [점주] 예약 정보
 * */
public class ReservationListResponseDto {
    Long reservationNumber;
    String storeName;
    String phoneNumber;
    boolean isValid;
    LocalDateTime time;
}
