package com.project.reservation.repository;

import com.project.reservation.domain.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByPhoneNumber(String phoneNumber);
}
