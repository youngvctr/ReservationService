package com.project.reservation.controller;

import com.project.reservation.domain.dto.SignUpDto;
import com.project.reservation.service.CustomerService;
import com.project.reservation.service.PartnerService;
import com.project.reservation.util.ValidUtil;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpController {

    private final PartnerService partnerService;
    private final CustomerService customerService;

    /**
     * [점주] 회원 가입
     */
    @PostMapping("/partner")
    public ResponseEntity<String> signUpPartner(
            @ApiParam(value = "비밀번호", example = "비밀번호는 6~20자, 영문 대소문자, 숫자, 특수문자(!,@,#,$,%,^,&,+,=)가 최소 1개씩 포함")
            @Valid SignUpDto signUpDto, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) return ValidUtil.responseErrorMessage(bindingResult);

        StringBuffer sb = new StringBuffer();
        sb
                .append(partnerService.signUp(signUpDto))
                .append("[점주] 회원 가입 완료!");
        return ResponseEntity.ok(new String(sb));
    }

    /**
     * [고객] 회원 가입
     */
    @PostMapping("/customer")
    public ResponseEntity<String> signUpCustomer(
            @ApiParam(value = "비밀번호", example = "비밀번호는 6~20자, 영문 대소문자, 숫자, 특수문자(!,@,#,$,%,^,&,+,=)가 최소 1개씩 포함")
            @Valid SignUpDto signUpDto, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) return ValidUtil.responseErrorMessage(bindingResult);

        StringBuffer sb = new StringBuffer();
        sb
                .append(customerService.signUp(signUpDto))
                .append("[고객] 회원 가입 완료!");
        return ResponseEntity.ok(new String(sb));
    }
}
