package com.project.reservation.service;

import com.project.reservation.domain.dto.*;

import java.util.List;

public interface PartnerService {
    String signUp(SignUpDto signUpDto);
    String login(LoginDto loginDto);
    String registerStore(AddStoreDto addStoreDto);
    List<ReservationListResponseDto> getReservationList(String email);
    String refuseReservation(Long reservationId);
}
