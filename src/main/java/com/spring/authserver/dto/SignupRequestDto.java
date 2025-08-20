package com.spring.authserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
    @Size(min = 4, max = 10)
    private String username;

    @NotBlank
    @Size(min = 8, max = 15)
    private String password;

    private String nickname;
}
