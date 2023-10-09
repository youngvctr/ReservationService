package com.project.reservation.service.impl;

import com.project.reservation.domain.dto.*;
import com.project.reservation.domain.model.Customer;
import com.project.reservation.domain.model.Reservation;
import com.project.reservation.domain.model.Review;
import com.project.reservation.domain.model.Store;
import com.project.reservation.exception.ServiceException;
import com.project.reservation.exception.ErrorCode;
import com.project.reservation.repository.CustomerRepository;
import com.project.reservation.repository.ReservationRepository;
import com.project.reservation.repository.ReviewRepository;
import com.project.reservation.repository.StoreRepository;
import com.project.reservation.service.CustomerService;
import com.project.reservation.type.StoreListType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;

    private final ReservationRepository reservationRepository;

    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * [고객] 회원가입
     */

    @Override
    public String signUp(SignUpDto signUpDto) {
        if (customerRepository.existsByEmail(signUpDto.getEmail()))
            throw new ServiceException(ErrorCode.USER_ALREADY_EXIST);

        Customer customer = Customer.from(signUpDto);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        return customer.getName();
    }

    /**
     * [고객] 로그인
     */
    @Override
    public String login(LoginDto loginDto) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(loginDto.getEmail());
        if (optionalCustomer.isEmpty())
            throw new ServiceException(ErrorCode.CUSTOMER_INFORMATION_INVALID);

        Customer customer = optionalCustomer.get();
        if (!passwordEncoder.matches(loginDto.getPassword(), customer.getPassword())) {
            throw new ServiceException(ErrorCode.CUSTOMER_INFORMATION_INVALID);
        }

        return customer.getName();
    }

    /**
     * [고객] 점포에 요청할 예약
     */
    @Override
    public String reservationStore(MakeReservationDto makeReservationDto) {
        Optional<Store> optStore = storeRepository.findByName(makeReservationDto.getStoreName());
        if (optStore.isEmpty()) {
            throw new ServiceException(ErrorCode.STORE_NAME_NOT_FOUND);
        }
        Store store = optStore.get();

        Optional<Customer> optCustomer = customerRepository.findByPhoneNumber(makeReservationDto.getPhoneNumber());
        if (optCustomer.isEmpty()) {
            throw new ServiceException(ErrorCode.CUSTOMER_BY_PHONE_NUMBER_NOT_FOUND);
        }

        Customer customer = optCustomer.get();
        createReservation(makeReservationDto, store, customer);

        return "예약 완료!";
    }

    /**
     * [고객] 예약 생성
     */
    private void createReservation(MakeReservationDto makeReservationDto, Store store, Customer customer) {
        Reservation reservation = Reservation.builder()
                .customer(customer)
                .store(store)
                .time(makeReservationDto.getReservationTime())
                .phoneNumber(customer.getPhoneNumber())
                .isValid(true)
                .isRefused(false)
                .build();

        customer.getReservationList().add(reservation);

        customerRepository.save(customer);
    }

    /**
     * [고객] 점포 조회(점포명, 거리, 평점에 따른 순서로)
     */

    @Override
    public List<StoreListResponseDto> getStoreList(String listType) {
        List<StoreListResponseDto> storeResponseList;

        if (listType.equals(StoreListType.NAME.getStoreType())) {
            storeResponseList = findAllStoresAlphabetically();
        } else if (listType.equals(StoreListType.DISTANCE.getStoreType())) {
            storeResponseList = findAllStoresByDistance();
        } else {
            storeResponseList = findAllStore();
        }

        storeResponseList.forEach(dto -> dto.setRatingAverage(calculateAverageRating(dto.getName())));

        if (listType.equals(StoreListType.RATING.getStoreType()))
            Collections.sort(storeResponseList, Comparator.comparing(StoreListResponseDto::getRatingAverage).reversed());
        return storeResponseList;
    }

    private double calculateAverageRating(String storeName) {
        Store store = storeRepository.findByName(storeName).get();

        return store.getReviewList().stream()
                .mapToDouble(Review::getRating)
                .average().orElse(0.0);
    }

    private List<StoreListResponseDto> findAllStoresAlphabetically() {
        return storeToResponseDto(storeRepository.findAllByOrderByNameAsc());
    }

    private List<StoreListResponseDto> findAllStoresByDistance() {
        return storeToResponseDto(storeRepository.findAllByOrderByDistanceAsc());
    }

    private List<StoreListResponseDto> findAllStore() {
        return storeToResponseDto(storeRepository.findAll());
    }

    private List<StoreListResponseDto> storeToResponseDto(List<Store> storeList) {

        return storeList.stream().map(e -> StoreListResponseDto.from(e))
                .collect(Collectors.toList());
    }

    /**
     * [고객] 키오스크에 도착 전달
     */
    @Override
    public String visitComplete(VisitStoreDto visitStoreDto) {
        List<Reservation> reservationList = reservationRepository.findAllByPhoneNumber(visitStoreDto.getPhoneNumber());

        Optional<Reservation> optionalReservation = reservationList.stream()
                .filter(reservation -> reservation.getStore().getName().equals(visitStoreDto.getStoreName()) && reservation.isValid())
                .findFirst();

        optionalReservation.ifPresentOrElse(
                reservation -> {
                    if (reservation.isRefused()) {
                        throw new ServiceException(ErrorCode.RESERVATION_IS_REFUSED);
                    }

                    reservation.setValid(false);
                    reservationRepository.save(reservation);

                    LocalDateTime allowedArrivalTime = reservation.getTime().minusMinutes(10);
                    checkArrivalTime(allowedArrivalTime, visitStoreDto.getVisitTime());
                },
                () -> {
                    throw new ServiceException(ErrorCode.RESERVATION_NOT_FOUND);
                }
        );

        return "고객님의 도착 정보를 확인하였습니다.";
    }

    /**
     * [고객] 도착 확인
     */
    private void checkArrivalTime(LocalDateTime allowedArrivalTime, LocalDateTime visitTime) {
        if (visitTime.isAfter(allowedArrivalTime)) throw new ServiceException(ErrorCode.ARRIVE_TOO_LATE);
    }

    /**
     * [고객] 리뷰 등록
     */
    @Override
    public String registerReview(Long reservationId, ReviewDto reviewDto) {
        Reservation reservation = reservationRepository.findById(reservationId).get();

        if (reservation.isValid()) throw new ServiceException(ErrorCode.RESERVATION_NOT_USED);

        Customer customer = reservation.getCustomer();
        Store store = reservation.getStore();

        Review review = Review.builder()
                .customer(customer)
                .store(store)
                .reservation(reservation)
                .rating(reviewDto.getRating())
                .contents(reviewDto.getContents())
                .build();

        reviewRepository.save(review);

        return "고객님의 리뷰가 성공적으로 작성되었습니다.";
    }
}
