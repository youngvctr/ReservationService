package com.project.reservation.domain.model;

import com.project.reservation.domain.dto.SignUpDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Customer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phoneNumber;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Review> reviewList;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservation> reservationList;

    public static Customer from(SignUpDto signUpDto){
            return Customer.builder()
                    .name(signUpDto.getName())
                    .email(signUpDto.getEmail())
                    .password(signUpDto.getPassword())
                    .phoneNumber(signUpDto.getPhoneNumber())
                    .build();
    }
}
