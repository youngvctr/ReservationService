package com.project.reservation.domain.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

/**
 * [고객] 리뷰
 * */
public class ReviewDto {
    @Min(value = 1, message = "평점은 1 이상이여야 합니다.")
    @Max(value = 5, message = "평점은 5 이하여야 합니다.")
    private int rating;
    private String contents;
}
