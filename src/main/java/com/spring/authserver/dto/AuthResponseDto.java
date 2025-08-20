package com.spring.authserver.dto;

import com.spring.authserver.model.Role;
import com.spring.authserver.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private Set<Role> roles;

    public AuthResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.roles = user.getRoles();
    }
}
