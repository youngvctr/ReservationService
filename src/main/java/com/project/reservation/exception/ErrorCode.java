package com.project.reservation.exception;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    PARTNER_ALREADY_EXIST(400 ,"E1", "이미 존재하는 점주입니다."),
    USER_ALREADY_EXIST(400, "E2", "이미 존재하는 고객입니다."),
    PARTNER_INFORMATION_INVALID(400, "E3", "점주님의 정보를 확인해주세요."),
    CUSTOMER_INFORMATION_INVALID(400, "E4", "고객님의 정보를 확인해주세요."),
    STORE_NAME_NOT_FOUND(400, "E6", "점포명을 확인해주세요."),
    CUSTOMER_BY_PHONE_NUMBER_NOT_FOUND(400, "E7", "고객 정보가 존재하지 않습니다."),
    RESERVATION_NOT_FOUND(400, "E8", "해당 예약 정보를 찾을 수 없습니다."),
    ARRIVE_TOO_LATE(400, "E9", "예약시간을 초과하여 예약 정보를 찾을 수 없습니다."),
    RESERVATION_NOT_USED(400, "E10", "리뷰를 작성해주세요."),
    RESERVATION_IS_REFUSED(400, "E11", "해당 예약은 점포의 사정으로 거절되었습니다.");

    private final int status;
    private final String code;
    private final String message;
}
