package com.project.reservation.repository;

import com.project.reservation.domain.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByName(String name);
    List<Store> findAll();
    List<Store> findAllByOrderByNameAsc();
    List<Store> findAllByOrderByDistanceAsc();
}
