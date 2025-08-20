package com.spring.authserver.security;

import com.spring.authserver.model.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private final Key key;
    private final long expirationTime;

    public JwtTokenProvider(@Value("${jwt.secret.key}") String secretKey,
                            @Value("${jwt.expiration-seconds}") long expirationTime) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(Base64.getEncoder().encodeToString(secretKey.getBytes())));
        this.expirationTime = expirationTime;
    }

    public String generateToken(Long userId, String username, Set<Role> roles) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationTime);
        String rolesStr = roles.stream().map(Enum::name).collect(Collectors.joining(","));

        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setSubject(String.valueOf(userId))
            .claim("username", username)
            .claim("roles", rolesStr)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(exp))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public Jws<Claims> parse(String token) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
