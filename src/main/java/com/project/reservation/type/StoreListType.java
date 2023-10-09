package com.project.reservation.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreListType {
    NAME("Name"),
    DISTANCE("Distance"),
    RATING("Rating");

    private final String storeType;
}
