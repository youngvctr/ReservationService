package com.project.reservation.domain.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

/**
 * [고객] 점포에 대한 예약 정보
 * */
public class MakeReservationDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "날짜와 시간은 필수 항목입니다.")
    @ApiParam(value = "예약 날짜와 시간", example = "2024-01-01T09:30:00")
    LocalDateTime reservationTime;
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",
            message = "전화번호는 010-0000-0000 형식이어야 합니다.")
    @NotBlank(message = "전화번호는 입력은 필수입니다.")
    String phoneNumber;
    @NotBlank(message = "상점 이름 입력")
    private String storeName;
}

