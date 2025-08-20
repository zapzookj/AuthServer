package com.spring.authserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private static final AtomicLong idCounter = new AtomicLong(1);
    private Long id = idCounter.getAndIncrement();
    private String username;
    private String password;
    private String nickname;
    private Set<Role> roles;

    public User(String username, String password, String nickname, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.roles = roles;
    }

}
