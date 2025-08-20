package com.spring.authserver.controller;

import com.spring.authserver.dto.AdminSignupRequestDto;
import com.spring.authserver.dto.AuthResponseDto;
import com.spring.authserver.model.User;
import com.spring.authserver.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signupAdmin(@RequestBody @Valid AdminSignupRequestDto requestDto) {
        AuthResponseDto responseDto = new AuthResponseDto(adminService.signupAdmin(requestDto));
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/users/{userId}/roles")
    public ResponseEntity<AuthResponseDto> grantAdmin(@PathVariable Long userId) {
        AuthResponseDto responseDto = new AuthResponseDto(adminService.grantAdmin(userId));
        return ResponseEntity.ok(responseDto);
    }
}
