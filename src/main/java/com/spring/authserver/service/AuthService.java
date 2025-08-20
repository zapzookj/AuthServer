package com.spring.authserver.service;

import com.spring.authserver.dto.LoginRequestDto;
import com.spring.authserver.dto.SignupRequestDto;
import com.spring.authserver.exception.AppException;
import com.spring.authserver.exception.ErrorCode;
import com.spring.authserver.model.Role;
import com.spring.authserver.model.User;
import com.spring.authserver.repository.UserRepository;
import com.spring.authserver.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public User signup(SignupRequestDto requestDto) {
        userRepository.findByUsername(requestDto.getUsername())
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.ALREADY_EXISTS, "이미 가입된 사용자입니다.");
                });

        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);

        User user = new User(
                requestDto.getUsername(),
                passwordEncoder.encode(requestDto.getPassword()),
                requestDto.getNickname(),
                roles
        );

        return userRepository.save(user);
    }

    public String login(LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS, "아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return jwtTokenProvider.generateToken(user.getId(), user.getUsername(), user.getRoles());
    }
}
