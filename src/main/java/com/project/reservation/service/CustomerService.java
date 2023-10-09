package com.project.reservation.service;


import com.project.reservation.domain.dto.*;

import java.util.List;

public interface CustomerService {
    String signUp(SignUpDto signUpDto);
    String login(LoginDto loginDto);
    String reservationStore(MakeReservationDto makeReservationDto);
    List<StoreListResponseDto> getStoreList(String listType);
    String visitComplete(VisitStoreDto visitStoreDto);
    String registerReview(Long reservationId, ReviewDto reviewDto);
}
