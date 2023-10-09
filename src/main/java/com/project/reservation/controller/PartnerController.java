package com.project.reservation.controller;

import com.project.reservation.domain.dto.AddStoreDto;
import com.project.reservation.domain.dto.ReservationListResponseDto;
import com.project.reservation.service.PartnerService;
import com.project.reservation.util.ValidUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/partner")
public class PartnerController {

    private final PartnerService partnerService;

    /**
     * [점주] 상점 등록
     */
    @PostMapping("/add/store")
    public ResponseEntity<String> registerStore(@Valid AddStoreDto addStoreDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) ValidUtil.responseErrorMessage(bindingResult);

        StringBuffer sb = new StringBuffer();
        sb
                .append("[점주] ")
                .append(partnerService.registerStore(addStoreDto))
                .append("님의 매장이 등록되었습니다.");
        return ResponseEntity.ok(new String(sb));
    }

    /**
     * [점주] 점포들의 예약 조회
     */

    @GetMapping("/reservation/list")
    public ResponseEntity<List<ReservationListResponseDto>> responseReservations(String email) {
        return ResponseEntity.ok(partnerService.getReservationList(email));
    }

    /**
     * [점주] 예약 거절
     */
    @PutMapping("/reservation/refuse")
    public ResponseEntity<String> partnerRefuseReservation(Long reservationId) {
        return ResponseEntity.ok(partnerService.refuseReservation(reservationId));
    }
}
