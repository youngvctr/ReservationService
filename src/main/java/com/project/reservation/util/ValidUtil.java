package com.project.reservation.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public class ValidUtil {
    /**
     * 입력값의 유효성을 검사할때, 고객에게 이메일 형식 및 입력값 필수 여부를 전달
     */

    public static ResponseEntity<String> responseErrorMessage(BindingResult bindingResult) {
        StringBuffer sb = new StringBuffer();
        bindingResult.getFieldErrors().forEach(error -> {
            sb.append(error.getField() + ": " + error.getDefaultMessage() + "\n");
        });
        return new ResponseEntity<>(new String(sb), HttpStatus.BAD_REQUEST);
    }
}
