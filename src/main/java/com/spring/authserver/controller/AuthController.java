package com.spring.authserver.controller;

import com.spring.authserver.dto.AuthResponseDto;
import com.spring.authserver.dto.LoginRequestDto;
import com.spring.authserver.dto.LoginResponseDto;
import com.spring.authserver.dto.SignupRequestDto;
import com.spring.authserver.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody @Valid SignupRequestDto requestDto) {
        AuthResponseDto responseDto = new AuthResponseDto(authService.signup(requestDto));
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto) {
        String token = authService.login(requestDto);
        return ResponseEntity.ok(new LoginResponseDto("로그인 성공", token));
    }
}
