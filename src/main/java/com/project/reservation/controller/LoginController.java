package com.project.reservation.controller;

import com.project.reservation.domain.dto.LoginDto;
import com.project.reservation.service.CustomerService;
import com.project.reservation.service.PartnerService;
import com.project.reservation.util.ValidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final CustomerService customerService;
    private final PartnerService partnerService;

    /**
     * [점주] 로그인
     */
    @PostMapping("/partner")
    public ResponseEntity<String> loginPartner(@Valid LoginDto loginDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ValidUtil.responseErrorMessage(bindingResult);
        }

        StringBuffer sb = new StringBuffer();
        return ResponseEntity.ok(new String(sb.append(partnerService.login(loginDto)).append("[점주] 로그인 성공!")));
    }

    /**
     * [고객] 로그인
     */
    @PostMapping("/customer")
    public ResponseEntity<String> loginCustomer(@Valid LoginDto loginDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ValidUtil.responseErrorMessage(bindingResult);
        }

        StringBuffer sb = new StringBuffer();
        return ResponseEntity.ok(new String(sb.append(customerService.login(loginDto)).append("[고객] 로그인 성공")));
    }
}
