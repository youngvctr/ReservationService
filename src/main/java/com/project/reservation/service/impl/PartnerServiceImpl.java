package com.project.reservation.service.impl;

import com.project.reservation.domain.dto.AddStoreDto;
import com.project.reservation.domain.dto.LoginDto;
import com.project.reservation.domain.dto.ReservationListResponseDto;
import com.project.reservation.domain.dto.SignUpDto;
import com.project.reservation.domain.model.Partner;
import com.project.reservation.domain.model.Reservation;
import com.project.reservation.domain.model.Store;
import com.project.reservation.exception.ServiceException;
import com.project.reservation.exception.ErrorCode;
import com.project.reservation.repository.PartnerRepository;
import com.project.reservation.repository.ReservationRepository;
import com.project.reservation.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * [점주] 회원가입
     */
    @Override
    public String signUp(SignUpDto signUpDto) {
        if (partnerRepository.existsByEmail(signUpDto.getEmail()))
            throw new ServiceException(ErrorCode.PARTNER_ALREADY_EXIST);
        Partner partner = Partner.from(signUpDto);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        partner.setPassword(encoder.encode(partner.getPassword()));
        partnerRepository.save(partner);
        return partner.getName();
    }

    /**
     * [점주] 로그인
     */
    @Override
    public String login(LoginDto loginDto) {
        Optional<Partner> optionalPartner = partnerRepository.findByEmail(loginDto.getEmail());
        if (optionalPartner.isEmpty()) throw new ServiceException(ErrorCode.PARTNER_INFORMATION_INVALID);

        Partner partner = optionalPartner.get();
        if (!passwordEncoder.matches(loginDto.getPassword(), partner.getPassword()))
            throw new ServiceException(ErrorCode.PARTNER_INFORMATION_INVALID);
        return partner.getName();
    }

    /**
     * [점주] 점포 등록
     */
    @Override
    public String registerStore(AddStoreDto addStoreDto) {
        Optional<Partner> optionalPartner = partnerRepository.findByEmail(addStoreDto.getEmail());
        if (optionalPartner.isEmpty()) throw new ServiceException(ErrorCode.PARTNER_INFORMATION_INVALID);

        Partner partner = optionalPartner.get();
        Store store = Store.from(addStoreDto);
        store.setPartner(partner);
        partner.getStoreList().add(store);
        partnerRepository.save(partner);
        return partner.getName();
    }

    /**
     * [점주] 점포의 예약 목록
     */
    @Override
    public List<ReservationListResponseDto> getReservationList(String email) {
        Partner partner = partnerRepository.findByEmail(email).get();

        List<Store> stores = partner.getStoreList();
        if (stores.size() == 0) throw new ServiceException(ErrorCode.STORE_NAME_NOT_FOUND);
        List<ReservationListResponseDto> result = getReservationsByStores(stores);
        result.sort(Comparator.comparing(ReservationListResponseDto::getTime));

        return result;
    }

    /**
     * [점주] 점포의 예약 정보 조회
     */
    private List<ReservationListResponseDto> getReservationsByStores(List<Store> stores) {
        List<ReservationListResponseDto> result = new ArrayList<>();

        for (Store store : stores) {
            List<Reservation> reservationList = store.getReservationList();
            for (Reservation reservation : reservationList) {
                ReservationListResponseDto dto = new ReservationListResponseDto(
                        reservation.getId(),
                        store.getName(),
                        reservation.getPhoneNumber(),
                        reservation.isValid(),
                        reservation.getTime()
                );
                result.add(dto);
            }
        }
        return result;
    }

    /**
     * [점주] 예약 거절
     */
    @Override
    public String refuseReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).get();
        reservation.setRefused(true);
        reservationRepository.save(reservation);
        return "예약이 거절되었습니다.";
    }
}
