package com.project.reservation.domain.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * [점포] 등록 정보
 * */
public class AddStoreDto {
    @NotBlank(message = "점주님의 아이디를 입력하세요.")
    private String email;

    @NotBlank(message = "점포명")
    private String name;

    @NotBlank(message = "점포 위치 입력")
    private String location;

    @NotNull(message = "점포까지 거리를 입력하세요.")
    private double distanceKm;

    @NotBlank(message = "점포 상세 정보")
    private String description;
}
