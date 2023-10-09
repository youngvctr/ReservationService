package com.project.reservation.domain.dto;

import com.project.reservation.domain.model.Store;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * [고객] 상점 목록
 * */
public class StoreListResponseDto {

    private String name;
    private String location;
    private String description;
    private double distance;
    private double ratingAverage;

    public static StoreListResponseDto from(Store store){

        return StoreListResponseDto.builder()
                .name(store.getName())
                .distance(store.getDistance())
                .description(store.getDescription())
                .location(store.getLocation())
                .build();
    }
}
