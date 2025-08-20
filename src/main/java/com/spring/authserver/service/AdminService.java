package com.spring.authserver.service;


import com.spring.authserver.dto.AdminSignupRequestDto;
import com.spring.authserver.exception.AppException;
import com.spring.authserver.exception.ErrorCode;
import com.spring.authserver.model.Role;
import com.spring.authserver.model.User;
import com.spring.authserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String adminCode = "123456"; // 예시


    public User signupAdmin(AdminSignupRequestDto requestDto) {
        if (!requestDto.getAdminCode().equals(adminCode)) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS, "어드민 코드가 틀렸습니다.");
        }

        userRepository.findByUsername(requestDto.getUsername())
            .ifPresent(user -> {
                throw new AppException(ErrorCode.ALREADY_EXISTS, "이미 가입된 사용자입니다.");
            });

        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);

        User user = new User(
            requestDto.getUsername(),
            passwordEncoder.encode(requestDto.getPassword()),
            requestDto.getNickname(),
            roles
        );

        return userRepository.save(user);
    }

    public User grantAdmin(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if (user.getRoles().contains(Role.ADMIN)) {
            throw new AppException(ErrorCode.ALREADY_EXISTS, "이미 어드민 권한이 있습니다.");
        }

        Set<Role> roles = new HashSet<>(user.getRoles());
        roles.add(Role.ADMIN);
        user.setRoles(roles);

        return userRepository.save(user);
    }
}
