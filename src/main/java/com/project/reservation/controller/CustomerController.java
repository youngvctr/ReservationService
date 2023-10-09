package com.project.reservation.controller;

import com.project.reservation.domain.dto.MakeReservationDto;
import com.project.reservation.domain.dto.ReviewDto;
import com.project.reservation.domain.dto.StoreListResponseDto;
import com.project.reservation.domain.dto.VisitStoreDto;
import com.project.reservation.service.CustomerService;
import com.project.reservation.util.ValidUtil;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * [고객] 해당 상점에 예약
     */

    @PostMapping("/reservation/store")
    public ResponseEntity<String> makeStoreReserve(@Valid MakeReservationDto makeReservationDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) ValidUtil.responseErrorMessage(bindingResult);
        return ResponseEntity.ok(customerService.reservationStore(makeReservationDto));
    }

    /**
     * [고객] 이름순, 거리순, 평점 순으로 상점들을 조회
     */

    @GetMapping("/list/store/{listType}")
    public ResponseEntity<List<StoreListResponseDto>> getStoreList(
            @ApiParam(value = "Category: '이름' : Name, '거리' : Distance, '평점' : Rating", required = true)
            @PathVariable String listType) {
        return ResponseEntity.ok(customerService.getStoreList(listType));
    }

    /**
     * [고객] 키오스크에 도착 정보를 전달
     */

    @PutMapping("/reservation/visit")
    public ResponseEntity<String> customerVisit(
            @Valid VisitStoreDto visitStoreDto, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) ValidUtil.responseErrorMessage(bindingResult);
        return ResponseEntity.ok(customerService.visitComplete(visitStoreDto));
    }

    /**
     * [고객] 상점 리뷰
     */

    @PostMapping("/reservation/{reservationId}/review")
    public ResponseEntity<String> makeReview(
            @PathVariable Long reservationId, ReviewDto reviewDto, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) ValidUtil.responseErrorMessage(bindingResult);
        return ResponseEntity.ok(customerService.registerReview(reservationId, reviewDto));
    }

}
