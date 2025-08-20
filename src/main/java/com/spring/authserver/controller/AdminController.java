package com.spring.authserver.controller;

import com.spring.authserver.dto.AdminSignupRequestDto;
import com.spring.authserver.dto.AuthResponseDto;
import com.spring.authserver.dto.ErrorResponseDto;
import com.spring.authserver.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "관리자 회원가입", description = "관리자 계정을 생성. ADMIN 코드 필요 (기본값 : 123456)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "가입 성공",
            content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
        @ApiResponse(responseCode = "409", description = "이미 가입된 사용자",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "ADMIN 코드 불일치",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signupAdmin(@RequestBody @Valid AdminSignupRequestDto requestDto) {
        AuthResponseDto responseDto = new AuthResponseDto(adminService.signupAdmin(requestDto));
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "관리자 권한 부여", description = "사용자에게 관리자 권한을 부여. ADMIN만 접근 가능")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "관리자 권한 부여 성공",
            content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "409", description = "이미 어드민 권한이 있음",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "관리자 인증 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PatchMapping("/users/{userId}/roles")
    public ResponseEntity<AuthResponseDto> grantAdmin(@PathVariable Long userId) {
        AuthResponseDto responseDto = new AuthResponseDto(adminService.grantAdmin(userId));
        return ResponseEntity.ok(responseDto);
    }
}
